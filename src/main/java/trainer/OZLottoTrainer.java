package trainer;

import analyser.OZLottoAnalyser;
import domain.analyserresult.AbstractAnalyserResult;
import domain.analyserresult.OZLottoAnalyserResult;
import domain.draw.OZLottoDraw;
import domain.result.SeparateNumTrainingResult;
import domain.result.TrainingResult;
import util.LogUtil;
import util.NumberGenUtil;

import java.io.File;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.IntStream;

/**
 * Created with IntelliJ IDEA.
 * User: lzhou
 * Date: 24/10/2016
 * Time: 10:07 AM
 */
public class OZLottoTrainer extends AbstractTrainer<OZLottoDraw> {
    private Map<Integer, OZLottoAnalyserResult> analyseResultMap;

    public OZLottoTrainer(File resultFile) {
        super(resultFile, strings ->
        {
            Integer[] nums = IntStream.range(2, 9).mapToObj(i -> Integer.valueOf(strings[i])).toArray(size -> new Integer[size]);
            OZLottoDraw draw = new OZLottoDraw(nums);
            return draw;
        });

        LogUtil.log("OZLotto Result Sample Size: " + results.size());
        OZLottoAnalyser analyser = new OZLottoAnalyser(results);
        analyseResultMap = analyser.start();
    }

    @Override
    public Map<Integer, ? extends AbstractAnalyserResult> getAnalyseResultMap() {
        return analyseResultMap;
    }

    @Override
    public Consumer<TrainingResult> trainingBenchMark() {
        return generateTrainingResultConsumer(
                trainingResult -> generateOZLottoDrawConsumer(trainingResult,
                        draw -> OZLottoDraw.generateRandomDraw()
                ));
    }

    private Consumer<OZLottoDraw> generateOZLottoDrawConsumer(TrainingResult trainingResult, Function<OZLottoDraw, OZLottoDraw> generationFunction) {
        return r -> {
            OZLottoDraw generatedDraw = generationFunction.apply(r);
            int division = r.checkWin(generatedDraw);
            trainingResult.addResult(division);
            if (division > 0 && printOutResultOn) {
                LogUtil.log(generatedDraw.toWinResultString(r));
            }
        };
    }

    public Consumer<TrainingResult> trainingFrequency() {
        return generateTrainingResultConsumer(
                trainingResult -> generateOZLottoDrawConsumer(trainingResult,
                        draw -> OZLottoDraw.generateDrawFrequencyByGroup(analyseResultMap.get(draw.getId() - 1))
                ));
    }

    //(ax+b)%45+1
    public void trainSeparateNum() {

        int testRange = 100;
        int aRange = testRange;
        int bRange = testRange;
        int cRange = testRange;
        int distanceRange = testRange;
        IntStream.range(0, 10).forEach(l ->
        {
            long start = System.currentTimeMillis();
            Map<Integer, Integer> numMap = createNumMapping();
//        int numIndex =1;
            for (int thisNumIndex = 4; thisNumIndex <= 4; thisNumIndex++) {

                for (int thatNumIndex = 1; thatNumIndex <= OZLottoDraw.NUM_OF_BALL; thatNumIndex++) {
                    System.out.print("Testing for position(" + thisNumIndex + ") on (" + thatNumIndex + ")");
                    boolean hasResult = false;
                    for (int distance = 1; distance <= distanceRange; distance++) {
                        if (distance % 10 == 0 && !hasResult) System.out.print(".");
                        for (int c = 1; c <= cRange; c++) {
                            for (int a = -aRange; a <= aRange; a++) {
                                for (int b = 0; b <= bRange; b++) {
                                    SeparateNumTrainingResult result = new SeparateNumTrainingResult();
                                    result.setA(a);
                                    result.setB(b);
                                    result.setC(c);
                                    result.setDistance(distance);
                                    result.setThisNumIndex(thisNumIndex);
                                    result.setThatNumIndex(thatNumIndex);
                                    for (int i = results.size() - 100; i < results.size(); i++) {
                                        try {
                                            int testingNum = numMap.get(results.get(i - distance).getNum(thatNumIndex));
//                                            int testingNum = results.get(i - distance).getNum(thatNumIndex);
                                            int expectNum = Math.abs(((testingNum * a) % c + b) % OZLottoDraw.MAX_NUM) + 1;
                                            int actualNum = results.get(i).getNum(thisNumIndex);
                                            result.accumulateSize();
                                            result.accumulateMatch(expectNum == actualNum);
                                            result.accumulateDiff(Math.abs(expectNum - actualNum));

                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }

                                    if (result.getTotalMatch() > result.getTotalSize() / 7) {
                                        System.out.println();
                                        System.out.print(result);
                                        hasResult = true;
                                    }
//                                if (result.getTotalDiff() < result.getTotalSize() * 6) System.out.println(result);
                                }
                            }
                        }
                    }
                    System.out.println("");
                }
            }
            long end = System.currentTimeMillis();
            System.out.println("last:" + ((end - start) / 1000));
        });

    }

    private Map<Integer, Integer> createNumMapping() {
        Map<Integer, Integer> numMap = new HashMap<>();
        List<Integer> nums = new ArrayList<>();
        for (int i = 1; i <= OZLottoDraw.MAX_NUM; i++) {
            nums.add(i);
        }

        for (int i = 1; nums.size() > 0; i++) {
            int index = NumberGenUtil.randomGenerateNumber(nums.size()) - 1;
            numMap.put(i, nums.get(index));
            nums.remove(index);
        }

        numMap.forEach((k, v) -> System.out.print("old : " + k + " new : " + v + "\t"));
        System.out.println();
        return numMap;
    }

    public void testBySeparateNum() {
        for (int i = results.size() - 100; i < results.size(); i++) {
            generateBySeparateNum(i);
        }
    }

    public void testByRandom() {
        for (int i = results.size() - 100; i < results.size(); i++) {
            generateByRandom(i);
        }
    }

    public OZLottoDraw generateByRandom(int index) {
        OZLottoDraw draw = OZLottoDraw.generateRandomDraw();
        System.out.println(draw.toWinResultString(results.get(index)));
        return draw;
    }

    public OZLottoDraw generateBySeparateNum(int index) {
        if (index <= 100) return null;
        Set<Integer> selectionSet = new HashSet<>();
        try {
            //a=-70, b=24, c=67, distance=20, thisNumIndex=1, thatNumIndex=2
            int num1 = Math.abs(((results.get(index - 20).getNum(2) * -70) % 67 + 24)) % OZLottoDraw.MAX_NUM + 1;
            //System.out.println("num1:"+num1);
            int[] index2NewNums = {39, 40, 27, 10, 17, 12, 24, 41, 30, 25, 2, 8, 18, 19, 13, 21, 22, 5, 38, 34, 28, 36, 26, 20, 23, 33, 43, 44, 37, 7, 6, 16, 29, 45, 1, 9, 42, 11, 3, 14, 4, 15, 31, 35, 32};
            Map<Integer, Integer> index2Map = buildMap(index2NewNums);
            //a=-86, b=11, c=55, distance=90, thisNumIndex=2, thatNumIndex=6}
            int index2NewNum1 = index2Map.get(results.get(index - 90).getNum(6));
            int num2 = Math.abs(((index2NewNum1 * -86) % 55 + 11)) % OZLottoDraw.MAX_NUM + 1;
            //a=-88, b=76, c=90, distance=61, thisNumIndex=2, thatNumIndex=1
            int index2NewNum2 = index2Map.get(results.get(index - 61).getNum(1));
            int nu21 = Math.abs(((index2NewNum2 * -88) % 90 + 76)) % OZLottoDraw.MAX_NUM + 1;
            //System.out.println("num2:"+num2);
            //System.out.println("nu21:"+nu21);
            // a=-72, b=42, c=30, distance=35, thisNumIndex=3, thatNumIndex=7
            int num3 = Math.abs(((results.get(index - 35).getNum(7) * -72) % 30 + 42)) % OZLottoDraw.MAX_NUM + 1;
            // a=-67, b=56, c=88, distance=73, thisNumIndex=3, thatNumIndex=7
            int nu31 = Math.abs(((results.get(index - 73).getNum(7) * -67) % 88 + 56)) % OZLottoDraw.MAX_NUM + 1;
            //System.out.println("num3:"+num3);
            //System.out.println("nu31:"+nu31);
            int[] index4NewNums = {37, 39, 30, 6, 24, 3, 45, 44, 4, 21, 29, 13, 18, 9, 11, 27, 16, 15, 5, 14, 32, 20, 42, 41, 22, 2, 26, 12, 33, 10, 8, 36, 23, 35, 1, 40, 43, 38, 25, 34, 28, 31, 19, 7, 17};
            Map<Integer, Integer> index4Map = buildMap(index4NewNums);
            //a=-63, b=57, c=50, distance=28, thisNumIndex=4, thatNumIndex=5
            int index4NewNum1 = index2Map.get(results.get(index - 28).getNum(5));
            int num4 = Math.abs(((index4NewNum1 * -63) % 50 + 57)) % OZLottoDraw.MAX_NUM + 1;
            int nu41 = NumberGenUtil.randomGenerateNumber(OZLottoDraw.MAX_NUM);
            //System.out.println("nu41:"+nu41);
            // a=81, b=23, c=87, distance=68, thisNumIndex=5, thatNumIndex=2
            int num5 = Math.abs(((results.get(index - 68).getNum(2) * -81) % 87 + 23)) % OZLottoDraw.MAX_NUM + 1;
            //  a=-49, b=27, c=90, distance=18, thisNumIndex=5, thatNumIndex=5
            int nu51 = Math.abs(((results.get(index - 18).getNum(5) * -49) % 90 + 27)) % OZLottoDraw.MAX_NUM + 1;
            //System.out.println("num5:"+num5);
            //System.out.println("nu51:"+nu51);
            // a=-73, b=69, c=69, distance=33, thisNumIndex=6, thatNumIndex=5
            int num6 = Math.abs(((results.get(index - 33).getNum(5) * -73) % 69 + 69)) % OZLottoDraw.MAX_NUM + 1;
            //  a=-65, b=48, c=62, distance=41, thisNumIndex=6, thatNumIndex=2
            int nu61 = Math.abs(((results.get(index - 41).getNum(2) * -65) % 62 + 48)) % OZLottoDraw.MAX_NUM + 1;
            // System.out.println("num6:"+num6);
            //System.out.println("nu61:"+nu61);
            //a=-97, b=85, c=50, distance=74, thisNumIndex=7, thatNumIndex=5
            int num7 = Math.abs(((results.get(index - 74).getNum(5) * -97) % 50 + 85)) % OZLottoDraw.MAX_NUM + 1;
            // a=-95, b=45, c=38, distance=42, thisNumIndex=7, thatNumIndex=1
            int nu71 = Math.abs(((results.get(index - 42).getNum(1) * -95) % 38 + 45)) % OZLottoDraw.MAX_NUM + 1;
            //System.out.println("num7:"+num7);
            //System.out.println("nu71:"+nu71);

            num2 = num1 == num2 ? nu21 : num2;
            num3 = num3 == num1 || num3 == num2 ? nu31 : num3;
            num4 = num4 == num1 || num4 == num2 || num4 == num3 ? nu41 : num4;
            num5 = num5 == num1 || num5 == num2 || num5 == num3 || num5 == num4 ? nu51 : num5;
            num6 = num6 == num1 || num6 == num2 || num6 == num3 || num6 == num4 || num6 == num5 ? nu61 : num6;
            num7 = num7 == num1 || num7 == num2 || num7 == num3 || num7 == num4 || num7 == num5 || num7 == num6 ? nu71 : num7;

            Integer[] selected = {num1, num2, num3, num4, num5, num6, num7};
            OZLottoDraw draw = new OZLottoDraw(selected);
            System.out.println("selected: " + (index >= results.size() ? draw.toString() : draw.toWinResultString(results.get(index))));
            return draw;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private Map<Integer, Integer> buildMap(int[] newNum) {
        Map<Integer, Integer> numMap = new HashMap<>();

        for (int i = 0; i < newNum.length; i++) {
            numMap.put(i + 1, newNum[i]);
        }
        return numMap;
    }
}
