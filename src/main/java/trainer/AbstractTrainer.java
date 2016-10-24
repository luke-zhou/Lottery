package trainer;

import domain.draw.Draw;
import domain.draw.OZLottoDraw;
import domain.draw.PowerBallDraw;
import domain.result.TrainingResult;
import util.CsvUtil;
import util.LogUtil;

import java.io.File;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Created with IntelliJ IDEA.
 * User: lzhou
 * Date: 24/10/2016
 * Time: 10:17 AM
 */
public abstract class AbstractTrainer<T extends Draw>
{
    protected int TRAINING_REPEAT_SIZE = 31415;
    protected int GENERATED_TRAINING_RESULT_SIZE = 20;
    protected int TESTING_RESULTS_SIZE = 50;

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
        this.printOutResultOn =true;
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

        public void by(Consumer<TrainingResult> trainingResultConsumer)
        {
            List<TrainingResult> trainingResults = generateTrainingResults(trainingResultConsumer);

            TrainingResult finalResult = getAverageTrainingResult(trainingResults);

            LogUtil.log(message + ":" + finalResult.toString());
        }
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
        getRidAbnormalResults(trainingResults);
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
        //temporary just remove first and last three elements
        IntStream.range(0, 3).forEach(_i -> {
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
