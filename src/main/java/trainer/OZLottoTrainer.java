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
import java.util.Map;
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
        int aRange = 100;
        int bRange = 100;
        int cRange = 100;
        int distanceRange = 100;
//        int numIndex =1;
        for (int thatNumIndex = 1; thatNumIndex < OZLottoDraw.NUM_OF_BALL; thatNumIndex++)
            for (int distance = 1; distance <= distanceRange; distance++) {
                for (int c =1; c<=cRange; c++) {
                    for (int a = -aRange; a <= aRange; a++) {
                        for (int b = -bRange; b <= bRange; b++) {
                            SeparateNumTrainingResult result = new SeparateNumTrainingResult();
                            result.setA(a);
                            result.setB(b);
                            result.setC(c);
                            result.setDistance(distance);
                            result.setThisNumIndex(1);
                            result.setThatNumIndex(thatNumIndex);
                            for (int i = 0 + distance; i < results.size(); i++) {
                                try {
                                int expectNum = Math.abs(((results.get(i - distance).getNum(thatNumIndex) * a)%c + b) % OZLottoDraw.MAX_NUM) + 1;
//                                int expectNum = NumberGenUtil.randomGenerateNumber(45);
                                    int actualNum = results.get(i).getNum(1);
                                    result.accumulateSize();
                                    result.accumulateMatch(expectNum == actualNum);
                                    result.accumulateDiff(Math.abs(expectNum - actualNum));

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                            if (result.getTotalMatch() > result.getTotalSize() / 16) System.out.println(result);
                            if (result.getTotalDiff() < result.getTotalSize() * 8) System.out.println(result);
                        }
                    }
                }
            }

    }
}
