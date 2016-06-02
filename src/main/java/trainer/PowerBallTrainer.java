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
    private int TESTING_RESULTS_SIZE = 50;

    private File resultFile;
    private List<PowerBallDraw> results;
    private Map<Integer, AnalyseResult> analyseResultMap;
    private Boolean printOutResultOn = false;

    public PowerBallTrainer(File resultFile)
    {
        this.resultFile = resultFile;

        results = CsvUtil.loadPowerData(resultFile);
        LogUtil.log("PowerBall Result Sample Size: " + results.size());
        PowerBallAnalyser analyser = new PowerBallAnalyser(results);
        analyseResultMap = analyser.start();
    }

    public void start()
    {
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
            int division = r.checkWinPowerHit(PowerBallDraw.generateDrawFrequencyNPBMinDistance(analyseResult, r.getId() + 1));
            trainingResult.addResult(division);
        });
    }

    public Consumer<TrainingResult> trainingBenchMark()
    {
        return trainingResultConsumer(
                trainingResult -> powerBallDrawConsumer(trainingResult,
                        draw -> PowerBallDraw.generateRandomDraw()
                ));
    }

    public Consumer<TrainingResult> trainingHitBenchMark()
    {
        return trainingResultConsumer(
                trainingResult -> powerHitDrawConsumer(trainingResult,
                        draw -> PowerBallDraw.generateRandomDraw()
                ));
    }

    public Consumer<TrainingResult> trainingFrequencyNPowerBallMinDistancePattern()
    {
        return trainingResultConsumer(
                trainingResult -> powerBallDrawConsumer(trainingResult,
                        draw -> PowerBallDraw.generateDrawFrequencyNPBMinDistance(analyseResultMap.get(draw.getId() - 1), draw.getId())
                ));
    }

    public Consumer<TrainingResult> trainingPowerBallMinDistancePattern()
    {
        return trainingResultConsumer(
                trainingResult -> powerBallDrawConsumer(trainingResult,
                        draw -> PowerBallDraw.generateDrawPBMinDistance(analyseResultMap.get(draw.getId() - 1), draw.getId())
                ));
    }

    public Consumer<TrainingResult> trainingFrequency()
    {
        return trainingResultConsumer(
                trainingResult -> powerBallDrawConsumer(trainingResult,
                        draw -> PowerBallDraw.generateDrawFrequency(analyseResultMap.get(draw.getId() - 1))
                ));
    }

    public Consumer<TrainingResult> trainingFrequencyNPB()
    {
        return trainingResultConsumer(
                trainingResult -> powerBallDrawConsumer(trainingResult,
                        draw -> PowerBallDraw.generateDrawFrequencyNPB(analyseResultMap.get(draw.getId() - 1))
                ));
    }

    public Consumer<TrainingResult> trainingFrequencyNPB(List<Rule> rules)
    {
        return trainingResultConsumer(
                trainingResult -> powerBallDrawConsumer(trainingResult,
                        draw -> PowerBallDraw.generateDrawFrequencyNPB(analyseResultMap.get(draw.getId() - 1), rules)
                ));
    }

    public Consumer<TrainingResult> trainingFrequency(List<Rule> rules)
    {

        return trainingResultConsumer(
                trainingResult -> powerBallDrawConsumer(trainingResult,
                        draw -> PowerBallDraw.generateDrawFrequency(analyseResultMap.get(draw.getId() - 1), rules)
                ));
    }

    public Consumer<TrainingResult> trainingHitFrequencyWithRule(List<Rule> rules)
    {
        return trainingResultConsumer(
                trainingResult -> powerHitDrawConsumer(trainingResult,
                        draw -> PowerBallDraw.generateDrawFrequency(analyseResultMap.get(draw.getId() - 1), rules)
                ));
    }

    public Consumer<TrainingResult> trainingHitFrequency()
    {
        return trainingResultConsumer(
                trainingResult -> powerHitDrawConsumer(trainingResult,
                        draw -> PowerBallDraw.generateDrawFrequency(analyseResultMap.get(draw.getId() - 1))
                ));
    }


    public TargetedPowerBallTrainerChain train(String message)
    {
        return new TargetedPowerBallTrainerChain(message);
    }

    private Consumer<TrainingResult> trainingResultConsumer(Function<TrainingResult, Consumer<PowerBallDraw>> function)
    {
        return (trainingResult) ->
                results.stream().skip(results.size() - TESTING_RESULTS_SIZE).forEach(function.apply(trainingResult));
    }

    private Consumer<PowerBallDraw> powerBallDrawConsumer(TrainingResult trainingResult, Function<PowerBallDraw, PowerBallDraw> generationFunction)
    {
        return r -> {
            //this is to make even with the power hit
            IntStream.range(0, 20).forEach(_i -> {
                PowerBallDraw generatedDraw = generationFunction.apply(r);
                int division = r.checkWin(generatedDraw);
                trainingResult.addResult(division);
                if (division > 0 && printOutResultOn)
                {
                    LogUtil.log(generatedDraw.toWinResultString(r));
                }
            });
            trainingResult.setTotalTrainingSize(trainingResult.getTotalTrainingSize() - 19);
        };
    }

    private Consumer<PowerBallDraw> powerHitDrawConsumer(TrainingResult trainingResult, Function<PowerBallDraw, PowerBallDraw> generationFunction)
    {
        return r -> {
            PowerBallDraw generatedDraw = generationFunction.apply(r);
            int division = r.checkWinPowerHit(generatedDraw);
            trainingResult.addResult(division);
            if (division > 0 && printOutResultOn)
            {
                LogUtil.log(generatedDraw.toWinResultString(r));
            }
        };
    }

    private TrainingResult getAverageTrainingResult(List<TrainingResult> trainingResults)
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

    public void turnOnResultPrint()
    {
        this.printOutResultOn =true;
    }

    public class TargetedPowerBallTrainerChain
    {
        private String message;

        public TargetedPowerBallTrainerChain(String message)
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
}

