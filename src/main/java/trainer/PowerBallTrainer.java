package trainer;

import domain.draw.PowerBallDraw;
import domain.draw.result.TrainingResult;
import util.CsvUtil;
import util.LogUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.IntStream;

/**
 * Created by Luke on 1/05/2016.
 */
public class PowerBallTrainer
{
    private int TRAINING_SIZE = 31415;
    private int TRAINING_REPEAT_SIZE = 20;

    private File resultFile;

    public PowerBallTrainer(File resultFile)
    {
        this.resultFile = resultFile;
    }

    public void start()
    {
        List<PowerBallDraw> results = CsvUtil.loadPowerData(resultFile);

        LogUtil.consoleLog("PowerBall Result Sample Size: " + results.size());

        calculatePowerBallBenchMark(results);

        trainPowerHit(results, "PowerHit Benchmark", num -> true);

        IntStream.range(1, PowerBallDraw.MAX_NUM).forEach(i -> trainPowerHit(results, "PowerHit ("+i+")", num -> num==i));

    }

    private void trainPowerHit(List<PowerBallDraw> results, String message, Predicate<Integer> predicate)
    {
        List<TrainingResult> trainingResults = new ArrayList<>();
        for (int k = 0; k < TRAINING_REPEAT_SIZE; k++)
        {
            TrainingResult trainingResult = new TrainingResult();

            for (int i = 0; i < TRAINING_SIZE; i++)
            {
                trainingOneSetResultForPowerHit(results, trainingResult, predicate);
            }
            trainingResults.add(trainingResult);
            System.out.print(".");
        }
        LogUtil.consoleLog("");

        TrainingResult finalResult = getAverageTrainingResult(trainingResults);

        LogUtil.consoleLog(message + ":" + finalResult.toString());
    }

    private void calculatePowerBallBenchMark(List<PowerBallDraw> results)
    {
        List<TrainingResult> trainingResults = new ArrayList<>();
        for (int k = 0; k < TRAINING_REPEAT_SIZE; k++)
        {
            TrainingResult trainingResult = new TrainingResult();

            for (int i = 0; i < TRAINING_SIZE; i++)
            {
                trainingOneSetResult(results, trainingResult, j -> true);
            }
            trainingResults.add(trainingResult);
            System.out.print(".");
        }
        LogUtil.consoleLog("");

        TrainingResult finalResult = getAverageTrainingResult(trainingResults);

        LogUtil.consoleLog("PowerBall Benchmark:" + finalResult.toString());
    }

    private TrainingResult getAverageTrainingResult(List<TrainingResult> trainingResults)
    {
        getRideAbnormalResults(trainingResults);
        Long totalWin = (long)trainingResults.stream().mapToLong(TrainingResult::getTotalWin).average().getAsDouble();
        Long totalDivision = (long)trainingResults.stream().mapToLong(TrainingResult::getTotalDivision).average().getAsDouble();
        Long totalTrainingSize = (long)trainingResults.stream().mapToLong(TrainingResult::getTotalTrainingSize).average().getAsDouble();

        TrainingResult averageTrainingResult = new TrainingResult();
        averageTrainingResult.setTotalWin(totalWin);
        averageTrainingResult.setTotalDivision(totalDivision);
        averageTrainingResult.setTotalTrainingSize(totalTrainingSize);

        return averageTrainingResult;
    }

    private void getRideAbnormalResults(List<TrainingResult> trainingResults)
    {
        Collections.sort(trainingResults, (r1, r2) -> r1.getTotalDivision().compareTo(r2.getTotalDivision()));
        //temporary just remove first and last three elements
        IntStream.range(1, 3).forEach(i -> {
            trainingResults.remove(0);
            trainingResults.remove(trainingResults.size() - 1);
        });
    }

    private void trainingOneSetResult(List<PowerBallDraw> results, TrainingResult trainingResult, Predicate<Integer> predicate)
    {
        results.stream().forEach(r ->
                //this is to make even with the power hit
                IntStream.range(1, 20).forEach(i -> {
                    int division = r.checkWin(PowerBallDraw.generateDraw(predicate));
                    trainingResult.addResult(division);
                })
        );
    }

    private void trainingOneSetResultForPowerHit(List<PowerBallDraw> results, TrainingResult trainingResult, Predicate<Integer> predicate)
    {
        results.stream().forEach(r -> {
            int division = r.checkWinPowerHit(PowerBallDraw.generateDraw(predicate));
            trainingResult.addResult(division);
        });
    }
}

