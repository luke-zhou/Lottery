package trainer;

import domain.analyserresult.AbstractAnalyserResult;
import domain.analyserresult.OZLottoAnalyserResult;
import domain.draw.Draw;
import domain.result.TrainingResult;
import util.CsvUtil;
import util.LogUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Created with IntelliJ IDEA.
 * User: lzhou
 * Date: 24/10/2016
 * Time: 10:17 AM
 */
public abstract class AbstractTrainer<T extends Draw>
{
    protected int TRAINING_REPEAT_SIZE = 314;
    protected int GENERATED_TRAINING_RESULT_SIZE = 20;
    protected int TESTING_RESULTS_SIZE = 200;

    protected File resultFile;

    protected List<T> results;

    protected Boolean printOutResultOn = false;

    public AbstractTrainer(File resultFile, Function<String[], T> function)
    {
        this.resultFile = resultFile;
        results = CsvUtil.loadData(resultFile, function);
    }

    public void turnOnResultPrint()
    {
        this.printOutResultOn = true;
    }

    public TrainerChain train(String message)
    {
        return new TrainerChain(message);
    }

    public class TrainerChain
    {
        private String message;

        public TrainerChain(String message)
        {
            this.message = message;
        }

        public void by_bk(Consumer<TrainingResult> trainingResultConsumer)
        {
            List<TrainingResult> trainingResults = generateTrainingResults(trainingResultConsumer);

            TrainingResult finalResult = getAverageTrainingResult(trainingResults);

            LogUtil.log(message + ":" + finalResult.toString());
        }

        public void by(Supplier<T> drawGenerator)
        {
            List<TrainingResult> trainingResults = new ArrayList<>();
            testingResults().forEach(draw -> {
                TrainingResult trainingResult = new TrainingResult();
                for (int i = 0; i < TRAINING_REPEAT_SIZE; i++)
                {
                    T generatedDraw = drawGenerator.get();
                    int division = draw.checkWin(generatedDraw);
                    trainingResult.addResult(division);
                }
                LogUtil.log(draw + ":" + trainingResult.toString());
                trainingResults.add(trainingResult);
            });
            TrainingResult averageTrainingResult = getAverageTrainingResult(trainingResults);
            LogUtil.log(message + " averageTrainingResult: " + ":" + averageTrainingResult.toString());

        }

        public void by(Function<AbstractAnalyserResult, T> drawGenerator)
        {
            List<TrainingResult> trainingResults = new ArrayList<>();
            testingResults().forEach(draw -> {
                TrainingResult trainingResult = new TrainingResult();
                for (int i = 0; i < TRAINING_REPEAT_SIZE; i++)
                {
                    T generatedDraw = drawGenerator.apply(getAnalyseResultMap().get(draw.getId() - 1));
                    int division = draw.checkWin(generatedDraw);
                    trainingResult.addResult(division);
                }
                LogUtil.log(draw + ":" + trainingResult.toString());
                trainingResults.add(trainingResult);
            });
            TrainingResult averageTrainingResult = getAverageTrainingResult(trainingResults);
            LogUtil.log(message + " averageTrainingResult: " + ":" + averageTrainingResult.toString());

        }
    }

    public abstract Map<Integer, ? extends AbstractAnalyserResult> getAnalyseResultMap();

    private Stream<T> testingResults()
    {
        return results.stream().skip(results.size() - TESTING_RESULTS_SIZE);
    }

    private List<TrainingResult> generateTrainingResults(Consumer<TrainingResult> consumer)
    {
        List<TrainingResult> trainingResults = IntStream.range(0, GENERATED_TRAINING_RESULT_SIZE).mapToObj(_i -> {
            TrainingResult trainingResult = new TrainingResult();
            IntStream.range(0, TRAINING_REPEAT_SIZE).forEach(_j -> consumer.accept(trainingResult));
            LogUtil.logProcessing();
            return trainingResult;
        }).collect(Collectors.toList());
        LogUtil.log("");
        return trainingResults;
    }


    protected TrainingResult getAverageTrainingResult(List<TrainingResult> trainingResults)
    {
//        getRidAbnormalResults(trainingResults);
        Long totalWin = (long) trainingResults.stream().mapToLong(TrainingResult::getTotalWin).average().getAsDouble();
        Long totalDivision = (long) trainingResults.stream().mapToLong(TrainingResult::getTotalDivision).average().getAsDouble();
        Long totalTrainingSize = (long) trainingResults.stream().mapToLong(TrainingResult::getTotalTrainingSize).average().getAsDouble();

        TrainingResult averageTrainingResult = new TrainingResult();
        averageTrainingResult.setTotalWin(totalWin);
        averageTrainingResult.setTotalDivision(totalDivision);
        averageTrainingResult.setTotalTrainingSize(totalTrainingSize);

        return averageTrainingResult;
    }

    private void getRidAbnormalResults(List<TrainingResult> trainingResults)
    {
        Collections.sort(trainingResults, (r1, r2) -> r1.getTotalDivision().compareTo(r2.getTotalDivision()));
        //temporary just remove first and last two elements
        IntStream.range(0, 2).forEach(_i -> {
            trainingResults.remove(0);
            trainingResults.remove(trainingResults.size() - 1);
        });
    }

    public abstract Consumer<TrainingResult> trainingBenchMark();

    protected Consumer<TrainingResult> generateTrainingResultConsumer(Function<TrainingResult, Consumer<T>> function)
    {
        return (trainingResult) ->
                results.stream().skip(results.size() - TESTING_RESULTS_SIZE).forEach(function.apply(trainingResult));
    }

}
