package trainer;

import analyser.OZLottoAnalyser;
import domain.analyserresult.AbstractAnalyserResult;
import domain.analyserresult.InOutPair;
import domain.analyserresult.InOutPairAnalyserResult;
import domain.analyserresult.OZLottoAnalyserResult;
import domain.draw.OZLottoDraw;
import domain.result.SeparateNumTrainingResult;
import domain.result.TrainingResult;
import trainer.domain.OZLottoPairsThatIndexTrainingResult;
import trainer.domain.OZLottoPairsThisIndexTrainingResult;
import util.LogUtil;
import util.NumberGenUtil;

import java.io.File;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Created with IntelliJ IDEA.
 * User: lzhou
 * Date: 24/10/2016
 * Time: 10:07 AM
 */
public class OZLottoTrainer extends AbstractTrainer<OZLottoDraw> {
    private Map<Integer, OZLottoAnalyserResult> analyseResultMap;
    private InOutPairAnalyserResult inOutPairAnalyserResult;

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
        inOutPairAnalyserResult = analyser.getInOutPairAnalyserResult();
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
        Set<String> pairSet = getPairInfo();
        IntStream.range(0, 200).forEach(l ->
        {
            long start = System.currentTimeMillis();
            Map<Integer, Integer> numMap = createNumMapping();
//        int numIndex =1;
            for (int thisNumIndex = 1; thisNumIndex <= OZLottoDraw.NUM_OF_BALL; thisNumIndex++) {

                for (int thatNumIndex = 1; thatNumIndex <= OZLottoDraw.NUM_OF_BALL; thatNumIndex++) {
                    System.out.print("Testing for position(" + thisNumIndex + ") on (" + thatNumIndex + ")");
                    boolean hasResult = false;
                    for (int distance = 1; distance <= distanceRange; distance++) {
                        if (distance % 10 == 0 && !hasResult) System.out.print(".");

                        String pairString = thisNumIndex + "|" + thatNumIndex + "|" + distance;
                        if (!pairSet.contains(pairString)) continue;

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

                                    if (result.getTotalMatch() > result.getTotalSize() / 6
                                            || result.getTotalDiff() < result.getTotalSize() * 6
                                            ) {
                                        System.out.println();
                                        System.out.print(result);
                                        hasResult = true;
                                    }
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

//        numMap.forEach((k, v) -> System.out.print("old : " + k + " new : " + v + "\t"));
        numMap.forEach((k, v) -> System.out.print(v + ","));
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

    public void testByPair() {
        OZLottoPairsThisIndexTrainingResult ozLottoPairsThisIndexTrainingResult = prepairPairTrainingResult();
        for (int i = results.size() - 50; i < results.size(); i++) {
            generateByPairResult(i, ozLottoPairsThisIndexTrainingResult);
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

            String conditionString1_1 = "a=-70, b=24, c=67, distance=20, thisNumIndex=1, thatNumIndex=2";
            int num1_1 = generateNum(conditionString1_1, index);
            //System.out.println("num1:"+num1);

            int[] index2NewNums = {20, 31, 37, 10, 44, 17, 8, 14, 1, 36, 43, 28, 15, 41, 13, 26, 45, 19, 22, 9, 21, 6, 18, 4, 27, 25, 35, 16, 38, 39, 29, 40, 2, 12, 32, 11, 30, 23, 42, 5, 33, 7, 34, 3, 24};
            String conditionString2_1 = "a=-79, b=38, c=23, distance=49, thisNumIndex=2, thatNumIndex=1";
            int num2_1 = generateNum(index2NewNums, conditionString2_1, index);
            String conditionString2_2 = "a=13, b=15, c=23, distance=49, thisNumIndex=2, thatNumIndex=1";
            int num2_2 = generateNum(index2NewNums, conditionString2_2, index);
            //System.out.println("num2:"+num2);
            //System.out.println("nu21:"+nu21);

            String conditionString3_1 = "a=-67, b=56, c=88, distance=73, thisNumIndex=3, thatNumIndex=7";
            int num3_1 = generateNum(conditionString3_1, index);
            String conditionString3_2 = "a=-72, b=42, c=30, distance=35, thisNumIndex=3, thatNumIndex=7";
            int num3_2 = generateNum(conditionString3_2, index);
            //System.out.println("num3:"+num3);
            //System.out.println("nu31:"+nu31);

            int[] index4NewNums = {37, 39, 30, 6, 24, 3, 45, 44, 4, 21, 29, 13, 18, 9, 11, 27, 16, 15, 5, 14, 32, 20, 42, 41, 22, 2, 26, 12, 33, 10, 8, 36, 23, 35, 1, 40, 43, 38, 25, 34, 28, 31, 19, 7, 17};
            String conditionString4_1 = "a=-63, b=57, c=50, distance=28, thisNumIndex=4, thatNumIndex=5";
            int num4_1 = generateNum(index4NewNums, conditionString4_1, index);
            int num4_2 = NumberGenUtil.randomGenerateNumber(OZLottoDraw.MAX_NUM);
            //System.out.println("nu41:"+nu41);

            int[] index5NewNums = {20, 31, 37, 10, 44, 17, 8, 14, 1, 36, 43, 28, 15, 41, 13, 26, 45, 19, 22, 9, 21, 6, 18, 4, 27, 25, 35, 16, 38, 39, 29, 40, 2, 12, 32, 11, 30, 23, 42, 5, 33, 7, 34, 3, 24};
            String conditionString5_1 = "a=-61, b=9, c=41, distance=68, thisNumIndex=5, thatNumIndex=2";
            int num5_1 = generateNum(index5NewNums, conditionString5_1, index);
            String conditionString5_2 = "a=81, b=23, c=87, distance=68, thisNumIndex=5, thatNumIndex=2";
            int num5_2 = generateNum(conditionString5_2, index);
            //System.out.println("num5:"+num5);
            //System.out.println("nu51:"+nu51);


            String conditionString6_1 = "a=-73, b=69, c=69, distance=33, thisNumIndex=6, thatNumIndex=5";
            int num6_1 = generateNum(conditionString6_1, index);
            String conditionString6_2 = "a=-65, b=48, c=62, distance=41, thisNumIndex=6, thatNumIndex=2";
            int num6_2 = generateNum(conditionString6_2, index);
            // System.out.println("num6:"+num6);
            //System.out.println("nu61:"+nu61);

            String conditionString7_1 = "a=-97, b=85, c=50, distance=74, thisNumIndex=7, thatNumIndex=5";
            int num7_1 = generateNum(conditionString7_1, index);
            String conditionString7_2 = "a=-95, b=45, c=38, distance=42, thisNumIndex=7, thatNumIndex=1";
            int num7_2 = generateNum(conditionString7_2, index);
            //System.out.println("num7:"+num7);
            //System.out.println("nu71:"+nu71);

            List<Integer> selectedNums = new ArrayList<>();
            selectedNums.add(num1_1);
            pickNum(num2_1, num2_2, selectedNums);
            pickNum(num3_1, num3_2, selectedNums);
            pickNum(num4_1, num4_2, selectedNums);
            pickNum(num5_1, num5_2, selectedNums);
            pickNum(num6_1, num6_2, selectedNums);
            pickNum(num7_1, num7_2, selectedNums);

            Integer[] selected = selectedNums.toArray(new Integer[selectedNums.size()]);
            OZLottoDraw draw = new OZLottoDraw(selected);
            System.out.println("selected: " + (index >= results.size() ? draw.toString() : draw.toWinResultString(results.get(index))));
            return draw;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private void pickNum(int numX_1, int numX_2, List<Integer> selectedNums) {
        int num = !selectedNums.contains(numX_1) ? numX_1 : (!selectedNums.contains(numX_2) ? numX_2 : NumberGenUtil.randomGenerateNumber(OZLottoDraw.MAX_NUM));
        selectedNums.add(num);
    }

    private Map<Integer, Integer> buildMap(int[] newNum) {
        Map<Integer, Integer> numMap = new HashMap<>();

        for (int i = 0; i < newNum.length; i++) {
            numMap.put(i + 1, newNum[i]);
        }
        return numMap;
    }

    public int generateNum(int[] mapArray, String conditionString, int index) throws Exception {
        //conditionString example
        //a=-97, b=85, c=50, distance=74, thisNumIndex=7, thatNumIndex=5
        Map<Integer, Integer> indexMap = mapArray[0] == 0 ? null : buildMap(mapArray);
        String[] conditions = conditionString.trim().split(",");
        int a = Integer.valueOf(conditions[0].substring(2));
        int b = Integer.valueOf(conditions[1].substring(3));
        int c = Integer.valueOf(conditions[2].substring(3));
        int distance = Integer.valueOf(conditions[3].substring(10));
        int thisNumIndex = Integer.valueOf(conditions[4].substring(14));
        int thatNumIndex = Integer.valueOf(conditions[5].substring(14));

        int thatNum = results.get(index - distance).getNum(thatNumIndex);
        int num = indexMap != null ? indexMap.get(thatNum) : thatNum;
        int generatedNum = Math.abs(((num * a) % c + b)) % OZLottoDraw.MAX_NUM + 1;
        return generatedNum;
    }

    public int generateNum(String conditionString, int index) throws Exception {
        return generateNum(new int[1], conditionString, index);
    }

    public Set<String> getPairInfo() {
        Set<String> result = new HashSet<>();
        IntStream.range(0, 100).forEach(distance -> {
            IntStream.range(0, OZLottoDraw.NUM_OF_BALL).forEach(thatIndexNum -> {
                IntStream.range(0, OZLottoDraw.NUM_OF_BALL).forEach(thisIndexNum -> {
                    List<InOutPair> test = inOutPairAnalyserResult.getInOutPairs().stream().filter(p -> p.getDistance() == distance + 1).filter(p -> (p.getThatIndexNum() == thatIndexNum + 1) && (p.getThisIndexNum() == thisIndexNum + 1)).sorted((p1, p2) -> p2.getCount() - p1.getCount()).collect(Collectors.toList());
                    if (test.size() <= 160) {
                        result.add(thisIndexNum + "|" + thatIndexNum + "|" + distance);
                    }
                });
            });
        });
        System.out.println("pair set size:" + result.size());
        return result;
    }

    private OZLottoPairsThisIndexTrainingResult prepairPairTrainingResult() {
        OZLottoPairsThisIndexTrainingResult ozLottoPairsThisIndexTrainingResult = new OZLottoPairsThisIndexTrainingResult();

        IntStream.range(0, 100).forEach(distance -> {
            IntStream.range(0, OZLottoDraw.NUM_OF_BALL).forEach(thatIndexNum -> {
                IntStream.range(0, OZLottoDraw.NUM_OF_BALL).forEach(thisIndexNum -> {
                    List<InOutPair> pairs = inOutPairAnalyserResult.getInOutPairs().stream().filter(p -> p.getDistance() == distance + 1).filter(p -> (p.getThatIndexNum() == thatIndexNum + 1) && (p.getThisIndexNum() == thisIndexNum + 1)).sorted((p1, p2) -> p2.getCount() - p1.getCount()).collect(Collectors.toList());
                    if (pairs.size() <= 190) {
                        ozLottoPairsThisIndexTrainingResult.add(thisIndexNum + 1, thatIndexNum + 1, distance + 1, pairs);
                    }
                });
            });
        });

        System.out.println("Valide total size:" + ozLottoPairsThisIndexTrainingResult.getSize());
        return ozLottoPairsThisIndexTrainingResult;
    }

    public OZLottoDraw generateByPairResult(int index) {
        return generateByPairResult(index, prepairPairTrainingResult());
    }

    public OZLottoDraw generateByPairResult(int index, OZLottoPairsThisIndexTrainingResult ozLottoPairsThisIndexTrainingResult) {

        List<Integer> selectedNums = new ArrayList<>();
        IntStream.range(0, OZLottoDraw.NUM_OF_BALL).forEach(i -> {
            OZLottoPairsThatIndexTrainingResult ozLottoPairsThatIndexTrainingResult = ozLottoPairsThisIndexTrainingResult.get(i + 1);
            int[] selectedNum = new int[1];
            int[] maxCount = {0};
            ozLottoPairsThatIndexTrainingResult.getMap().forEach((thatIndex, distanceMap) -> {
                distanceMap.getMap().forEach((distance, pairs) -> {
                    try {
                        int thatNum = results.get(index - distance).getNum(thatIndex);
                        List<InOutPair> validePairList = pairs.stream().filter(p -> p.getIn() == thatNum).sorted((p1, p2) -> p2.getCount() - p1.getCount()).collect(Collectors.toList());
                        if (validePairList.size() > 0) {
                            InOutPair pair = validePairList.get(0);
                            if (!selectedNums.contains(pair.getOut()) && pair.getCount() > maxCount[0]) {
                                selectedNum[0] = pair.getOut();
                                maxCount[0] = pair.getCount();
                            }
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            });
            selectedNums.add(selectedNum[0] != 0 ? selectedNum[0] : NumberGenUtil.randomGenerateNumber(OZLottoDraw.MAX_NUM));
            System.out.println("select num:" + selectedNum[0] + "  count:" + maxCount[0]);
        });
        Integer[] selected = selectedNums.toArray(new Integer[selectedNums.size()]);
        OZLottoDraw draw = new OZLottoDraw(selected);
        System.out.println("selected: " + (index >= results.size() ? draw.toString() : draw.toWinResultString(results.get(index))));
        return draw;
    }
}
