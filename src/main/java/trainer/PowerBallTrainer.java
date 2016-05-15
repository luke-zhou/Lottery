package trainer;

import analyser.AnalyseResult;
import analyser.PowerBallAnalyser;
import domain.draw.PowerBallDraw;
import domain.result.TrainingResult;
import domain.rule.Rule;
import util.CsvUtil;
import util.LogUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
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

        PowerBallAnalyser analyser = new PowerBallAnalyser(results);

        Map<Integer, AnalyseResult> analyseResultMap = analyser.start();

//        trainFrequencyPowerHit(results, potentialNumsGroup, "PowerHit frequency Result");

        trainFrequencyPowerBall(results, analyseResultMap, "PowerBall frequency Result");

//        calculatePowerBallBenchMark(results);

//        calculatePowerHitBenchMark(results);

//        calculatePowerHitOneNumberExisting(results);
//        calculatePowerHitTwoNumberRelation(results);
    }

    private void calculatePowerHitOneNumberExisting(List<PowerBallDraw> results)
    {
        IntStream.range(10, PowerBallDraw.MAX_NUM + 1).forEach(i -> {
            Rule rule = new Rule(1);
            rule.getArguments().add(i);
            trainPowerHit(results, "PowerHit (" + i + ")", rule);
        });
    }

    private void calculatePowerHitTwoNumberRelation(List<PowerBallDraw> results)
    {
        IntStream.range(1, 40).forEach(i -> {
            Rule rule = new Rule(2);
            rule.setInvolvedNumberCount(2);
            trainPowerHit(results, "PowerHit a=b+" + i, rule);
        });
    }

    private void calculatePowerHitBenchMark(List<PowerBallDraw> results)
    {
        trainPowerHit(results, "PowerHit Benchmark", Rule.NO_RULE);
    }

    private void trainFrequencyPowerBall(List<PowerBallDraw> results, Map<Integer, AnalyseResult> analyseResultMap, String message)
    {
        List<TrainingResult> trainingResults = new ArrayList<>();
        for (int k = 0; k < TRAINING_REPEAT_SIZE; k++)
        {
            TrainingResult trainingResult = new TrainingResult();

            for (int i = 0; i < TRAINING_SIZE; i++)
            {
                trainingOneSetResultForFrequency(results, analyseResultMap, trainingResult);
            }
            trainingResults.add(trainingResult);
            System.out.print(".");
        }
        LogUtil.consoleLog("");

        TrainingResult finalResult = getAverageTrainingResult(trainingResults);

        LogUtil.consoleLog(message + ":" + finalResult.toString());
    }

    private void trainFrequencyPowerHit(List<PowerBallDraw> results, AnalyseResult analyseResult, String message)
    {
        List<TrainingResult> trainingResults = new ArrayList<>();
        for (int k = 0; k < TRAINING_REPEAT_SIZE; k++)
        {
            TrainingResult trainingResult = new TrainingResult();

            for (int i = 0; i < TRAINING_SIZE; i++)
            {
                trainingOneSetResultForFrequencyPowerHit(results, analyseResult, trainingResult);
            }
            trainingResults.add(trainingResult);
            System.out.print(".");
        }
        LogUtil.consoleLog("");

        TrainingResult finalResult = getAverageTrainingResult(trainingResults);

        LogUtil.consoleLog(message + ":" + finalResult.toString());
    }

    private void trainPowerHit(List<PowerBallDraw> results, String message, Rule rule)
    {
        List<TrainingResult> trainingResults = new ArrayList<>();
        for (int k = 0; k < TRAINING_REPEAT_SIZE; k++)
        {
            TrainingResult trainingResult = new TrainingResult();

            for (int i = 0; i < TRAINING_SIZE; i++)
            {
                trainingOneSetResultForPowerHit(results, trainingResult, rule);
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
                trainingOneSetResult(results, trainingResult, Rule.NO_RULE);
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
        Long totalWin = (long) trainingResults.stream().mapToLong(TrainingResult::getTotalWin).average().getAsDouble();
        Long totalDivision = (long) trainingResults.stream().mapToLong(TrainingResult::getTotalDivision).average().getAsDouble();
        Long totalTrainingSize = (long) trainingResults.stream().mapToLong(TrainingResult::getTotalTrainingSize).average().getAsDouble();

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

    private void trainingOneSetResult(List<PowerBallDraw> results, TrainingResult trainingResult, Rule rule)
    {
        results.stream().forEach(r ->
                //this is to make even with the power hit
                IntStream.range(0, 20).forEach(i -> {
                    int division = r.checkWin(PowerBallDraw.generateDraw(rule));
                    trainingResult.addResult(division);
                })
        );
    }

    private void trainingOneSetResultForFrequency(List<PowerBallDraw> results, Map<Integer, AnalyseResult> analyseResultMap, TrainingResult trainingResult)
    {
        results.stream().forEach(r ->
                //this is to make even with the power hit
                IntStream.range(0, 20).forEach(i -> {
                    int division = r.checkWin(PowerBallDraw.generateDraw(analyseResultMap.get(r.getId()-1), r.getId()+1));
                    trainingResult.addResult(division);
                })
        );
    }

    private void trainingOneSetResultForPowerHit(List<PowerBallDraw> results, TrainingResult trainingResult, Rule rule)
    {
        results.stream().forEach(r -> {
            int division = r.checkWinPowerHit(PowerBallDraw.generateDraw(rule));
            trainingResult.addResult(division);
        });
    }

    private void trainingOneSetResultForFrequencyPowerHit(List<PowerBallDraw> results, AnalyseResult analyseResult, TrainingResult trainingResult)
    {
        results.stream().forEach(r -> {
            int division = r.checkWinPowerHit(PowerBallDraw.generateDraw(analyseResult, r.getId()+1));
            trainingResult.addResult(division);
        });
    }
}

