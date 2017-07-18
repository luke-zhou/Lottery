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
        long start = System.currentTimeMillis();
        int testRange = 100;
        int aRange = testRange;
        int bRange = testRange;
        int cRange = testRange;
        int distanceRange = testRange;
        IntStream.range(0,10).forEach(l->
        {
            Map<Integer, Integer> numMap = createNumMapping();
//        int numIndex =1;
            for (int thisNumIndex = 1; thisNumIndex <= OZLottoDraw.NUM_OF_BALL; thisNumIndex++) {

                for (int thatNumIndex = 1; thatNumIndex <= OZLottoDraw.NUM_OF_BALL; thatNumIndex++) {
                    System.out.println("Testing for position(" + thisNumIndex + ") on (" + thatNumIndex + ")");
                    for (int distance = 1; distance <= distanceRange; distance++) {
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
                                    for (int i = results.size()-100; i < results.size(); i++) {
                                        try {
//                                            int testingNum = numMap.get(results.get(i - distance).getNum(thatNumIndex));
                                            int testingNum = results.get(i - distance).getNum(thatNumIndex);
                                            int expectNum = Math.abs(((testingNum * a) % c + b) % OZLottoDraw.MAX_NUM) + 1;
                                            int actualNum = results.get(i).getNum(thisNumIndex);
                                            result.accumulateSize();
                                            result.accumulateMatch(expectNum == actualNum);
                                            result.accumulateDiff(Math.abs(expectNum - actualNum));

                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }

                                    if (result.getTotalMatch() > result.getTotalSize() / 7) System.out.println(result);
//                                if (result.getTotalDiff() < result.getTotalSize() * 6) System.out.println(result);
                                }
                            }
                        }
                    }
                }
            }
        });
        long end = System.currentTimeMillis();
        System.out.println("last:" + ((end - start) / 1000));
    }

    private Map<Integer, Integer> createNumMapping() {
        Map<Integer, Integer> numMap = new HashMap<>();
        List<Integer> nums = new ArrayList<>();
        for (int i = 1; i <= OZLottoDraw.MAX_NUM; i++) {
            nums.add(i);
        }

        for (int i = 1; nums.size() > 0; i++) {
            int index = NumberGenUtil.randomGenerateNumber(nums.size())-1;
            numMap.put(i, nums.get(index));
            nums.remove(index);
        }

        numMap.forEach((k,v)->System.out.print("old : " + k + " new : " + v+"\t"));
        System.out.println();
        return numMap;
    }

    public void testBySeparateNum() {
        for (int i = 200; i < results.size(); i++) {
            generateBySeparateNum(i);
        }
    }

    public void testByRandom() {
        for (int i = 200; i < results.size(); i++) {
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
            int num1 = Math.abs(((results.get(index - 88).getNum(5) * 47) % 81 + 81)) % OZLottoDraw.MAX_NUM + 1;
            //System.out.println("num1:"+num1);
            // a=-99	 b=66	 c=43	 distance=97	 thisNumIndex=2	 thatNumIndex=5}	0.071129707
            int num2 = Math.abs(((results.get(index - 97).getNum(5) * -99) % 43 + 66)) % OZLottoDraw.MAX_NUM + 1;
            //  a=-79	 b=21	 c=61	 distance=70	 thisNumIndex=2	 thatNumIndex=4}	0.069306931
            int nu21 = Math.abs(((results.get(index - 70).getNum(4) * -79) % 61 + 21)) % OZLottoDraw.MAX_NUM + 1;
            //System.out.println("num2:"+num2);
            //System.out.println("nu21:"+nu21);
            // a=-62	 b=-82	 c=88	 distance=87	 thisNumIndex=3	 thatNumIndex=7}	0.071721311
            int num3 = Math.abs(((results.get(index - 87).getNum(7) * -62) % 88 - 82)) % OZLottoDraw.MAX_NUM + 1;
            // a=-95	 b=-58	 c=23	 distance=34	 thisNumIndex=3	 thatNumIndex=1}	0.068391867
            int nu31 = Math.abs(((results.get(index - 34).getNum(1) * -95) % 23 - 58)) % OZLottoDraw.MAX_NUM + 1;
            //System.out.println("num3:"+num3);
            //System.out.println("nu31:"+nu31);
            // a=-30	 b=-77	 c=98	 distance=100	 thisNumIndex=4	 thatNumIndex=4}	0.069473684
            int num4 = Math.abs(((results.get(index - 100).getNum(4) * -30) % 98 - 77)) % OZLottoDraw.MAX_NUM + 1;
            // a=-68	 b=-71	 c=56	 distance=97	 thisNumIndex=4	 thatNumIndex=4}	0.069037657
            int nu41 = Math.abs(((results.get(index - 97).getNum(4) * -68) % 56 - 71)) % OZLottoDraw.MAX_NUM + 1;
            //System.out.println("nu41:"+nu41);
            // a=-69	 b=97	 c=89	 distance=90	 thisNumIndex=5	 thatNumIndex=1}	0.070103093
            int num5 = Math.abs(((results.get(index - 90).getNum(1) * -69) % 89 + 97)) % OZLottoDraw.MAX_NUM + 1;
            //  a=-88	 b=32	 c=23	 distance=96	 thisNumIndex=5	 thatNumIndex=3}	0.066805846
            int nu51 = Math.abs(((results.get(index - 96).getNum(3) * -88) % 23 + 32)) % OZLottoDraw.MAX_NUM + 1;
            //System.out.println("num5:"+num5);
            //System.out.println("nu51:"+nu51);
            // a=-60	 b=56	 c=96	 distance=58	 thisNumIndex=6	 thatNumIndex=4}	0.073500967
            int num6 = Math.abs(((results.get(index - 58).getNum(4) * -60) % 96 + 56)) % OZLottoDraw.MAX_NUM + 1;
            //  a=-100	 b=40	 c=24	 distance=94	 thisNumIndex=6	 thatNumIndex=1}	0.072765073
            int nu61 = Math.abs(((results.get(index - 94).getNum(1) * -100) % 24 + 40)) % OZLottoDraw.MAX_NUM + 1;
            // System.out.println("num6:"+num6);
            //System.out.println("nu61:"+nu61);
            // a=-55	 b=-63	 c=99	 distance=92	 thisNumIndex=7	 thatNumIndex=4}	0.070393375
            int num7 = Math.abs(((results.get(index - 92).getNum(4) * -55) % 99 - 63)) % OZLottoDraw.MAX_NUM + 1;
            //  a=-75	 b=-71	 c=82	 distance=82	 thisNumIndex=7	 thatNumIndex=2}	0.06693712
            int nu71 = Math.abs(((results.get(index - 82).getNum(2) * -75) % 82 - 71)) % OZLottoDraw.MAX_NUM + 1;
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
}
