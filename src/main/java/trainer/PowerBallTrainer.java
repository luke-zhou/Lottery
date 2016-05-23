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
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Created by Luke on 1/05/2016.
 */
public class PowerBallTrainer
{
    private int TRAINING_REPEAT_SIZE = 314;
    private int GENERATED_TRAINING_RESULT_SIZE = 20;
    private int TESTING_RESULTS_SIZE = 20;

    private File resultFile;

    public PowerBallTrainer(File resultFile)
    {
        this.resultFile = resultFile;
    }

    public void start()
    {
        List<PowerBallDraw> results = CsvUtil.loadPowerData(resultFile);

        LogUtil.log("PowerBall Result Sample Size: " + results.size());

        PowerBallAnalyser analyser = new PowerBallAnalyser(results);

        Map<Integer, AnalyseResult> analyseResultMap = analyser.start();

        calculatePowerBallBenchMark(results);
        trainFrequencyPowerBall(results, "PowerBall frequency Result");
        trainFrequencyNPowerBallMinDistancePattern(results, analyseResultMap, "PowerBall frequency N PB Mini Distance Pattern Result");

//        trainFrequencyPowerHit(results, potentialNumsGroup, "PowerHit frequency Result");


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

    private void trainFrequencyNPowerBallMinDistancePattern(List<PowerBallDraw> results, Map<Integer, AnalyseResult> analyseResultMap, String message)
    {
        List<TrainingResult> trainingResults = generateTrainingResults(
                trainingFrequencyNPowerBallMinDistancePattern(results, analyseResultMap)
        );
        TrainingResult finalResult = getAverageTrainingResult(trainingResults);

        LogUtil.log(message + ":" + finalResult.toString());
    }

    private void trainFrequencyPowerBall(List<PowerBallDraw> results, String message)
    {
        List<TrainingResult> trainingResults = generateTrainingResults(
                trainingFrequency(results)
        );
        TrainingResult finalResult = getAverageTrainingResult(trainingResults);

        LogUtil.log(message + ":" + finalResult.toString());
    }

    private List<TrainingResult> generateTrainingResults(Consumer<TrainingResult> action)
    {
        List<TrainingResult> trainingResults = IntStream.range(0, GENERATED_TRAINING_RESULT_SIZE).mapToObj(_i -> {
            TrainingResult trainingResult = new TrainingResult();
            IntStream.range(0, TRAINING_REPEAT_SIZE).forEach(_j -> action.accept(trainingResult));
            LogUtil.logProcessing();
            return trainingResult;
        }).collect(Collectors.toList());
        LogUtil.log("");
        return trainingResults;
    }

    private void trainFrequencyPowerHit(List<PowerBallDraw> results, AnalyseResult analyseResult, String message)
    {
        List<TrainingResult> trainingResults = new ArrayList<>();
        for (int k = 0; k < GENERATED_TRAINING_RESULT_SIZE; k++)
        {
            TrainingResult trainingResult = new TrainingResult();

            for (int i = 0; i < TRAINING_REPEAT_SIZE; i++)
            {
                trainingOneSetResultForFrequencyPowerHit(results, analyseResult, trainingResult);
            }
            trainingResults.add(trainingResult);
            System.out.print(".");
        }
        LogUtil.log("");

        TrainingResult finalResult = getAverageTrainingResult(trainingResults);

        LogUtil.log(message + ":" + finalResult.toString());
    }

    private void trainPowerHit(List<PowerBallDraw> results, String message, Rule rule)
    {
        List<TrainingResult> trainingResults = new ArrayList<>();
        for (int k = 0; k < GENERATED_TRAINING_RESULT_SIZE; k++)
        {
            TrainingResult trainingResult = new TrainingResult();

            for (int i = 0; i < TRAINING_REPEAT_SIZE; i++)
            {
                trainingOneSetResultForPowerHit(results, trainingResult, rule);
            }
            trainingResults.add(trainingResult);
            System.out.print(".");
        }
        LogUtil.log("");

        TrainingResult finalResult = getAverageTrainingResult(trainingResults);

        LogUtil.log(message + ":" + finalResult.toString());
    }

    private void calculatePowerBallBenchMark(List<PowerBallDraw> results)
    {
        List<TrainingResult> trainingResults = generateTrainingResults(trainingBenchMark(results));
        TrainingResult finalResult = getAverageTrainingResult(trainingResults);
        LogUtil.log("PowerBall Benchmark:" + finalResult.toString());
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
        IntStream.range(0, 3).forEach(_i -> {
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

    private Consumer<TrainingResult> trainingResultConsumer(List<PowerBallDraw> results, Function<TrainingResult, Consumer<PowerBallDraw>> function)
    {
        return (trainingResult) ->
                results.stream().skip(results.size() - TESTING_RESULTS_SIZE).forEach(function.apply(trainingResult));
    }

    private Consumer<PowerBallDraw> powerBallDrawConsumer(TrainingResult trainingResult, Function<PowerBallDraw, PowerBallDraw> generationFunction)
    {
        return r -> {
            //this is to make even with the power hit
            IntStream.range(0, 20).forEach(_i -> {
                int division = r.checkWin(generationFunction.apply(r));
                trainingResult.addResult(division);
            });
            trainingResult.setTotalTrainingSize(trainingResult.getTotalTrainingSize() - 19);
        };
    }

    private Consumer<TrainingResult> trainingBenchMark(List<PowerBallDraw> results)
    {
        return trainingResultConsumer(results,
                trainingResult -> powerBallDrawConsumer(trainingResult,
                        draw -> PowerBallDraw.generateRandomDraw()
                ));
    }

    private Consumer<TrainingResult> trainingFrequencyNPowerBallMinDistancePattern(List<PowerBallDraw> results, Map<Integer, AnalyseResult> analyseResultMap)
    {
        return trainingResultConsumer(results,
                trainingResult -> powerBallDrawConsumer(trainingResult,
                        draw -> PowerBallDraw.generateDraw(analyseResultMap.get(draw.getId() - 1), draw.getId())
                ));
    }

    private Consumer<TrainingResult> trainingFrequency(List<PowerBallDraw> results)
    {
        return trainingResultConsumer(results,
                trainingResult -> powerBallDrawConsumer(trainingResult,
                        draw -> PowerBallDraw.generateDraw(null, draw.getId())
                ));
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
            int division = r.checkWinPowerHit(PowerBallDraw.generateDraw(analyseResult, r.getId() + 1));
            trainingResult.addResult(division);
        });
    }
}

