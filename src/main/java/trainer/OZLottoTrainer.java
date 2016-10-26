package trainer;

import analyser.OZLottoAnalyser;
import domain.analyserresult.AbstractAnalyserResult;
import domain.analyserresult.OZLottoAnalyserResult;
import domain.draw.OZLottoDraw;
import domain.result.TrainingResult;
import util.LogUtil;

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
public class OZLottoTrainer extends AbstractTrainer<OZLottoDraw>
{
    private Map<Integer, OZLottoAnalyserResult> analyseResultMap;

    public OZLottoTrainer(File resultFile)
    {
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
    public Map<Integer, ? extends AbstractAnalyserResult> getAnalyseResultMap()
    {
        return analyseResultMap;
    }

    @Override
    public Consumer<TrainingResult> trainingBenchMark()
    {
        return generateTrainingResultConsumer(
                trainingResult -> generateOZLottoDrawConsumer(trainingResult,
                        draw -> OZLottoDraw.generateRandomDraw()
                ));
    }

    private Consumer<OZLottoDraw> generateOZLottoDrawConsumer(TrainingResult trainingResult, Function<OZLottoDraw, OZLottoDraw> generationFunction)
    {
        return r -> {
            OZLottoDraw generatedDraw = generationFunction.apply(r);
            int division = r.checkWin(generatedDraw);
            trainingResult.addResult(division);
            if (division > 0 && printOutResultOn)
            {
                LogUtil.log(generatedDraw.toWinResultString(r));
            }
        };
    }

    public Consumer<TrainingResult> trainingFrequency()
    {
        return generateTrainingResultConsumer(
                trainingResult -> generateOZLottoDrawConsumer(trainingResult,
                        draw -> OZLottoDraw.generateDrawFrequencyByGroup(analyseResultMap.get(draw.getId() - 1))
                ));
    }
}
