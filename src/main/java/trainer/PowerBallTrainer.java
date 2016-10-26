package trainer;

import analyser.PowerBallAnalyser;
import domain.analyserresult.AbstractAnalyserResult;
import domain.analyserresult.PowerBallAnalyseResult;
import domain.draw.PowerBallDraw;
import domain.result.TrainingResult;
import domain.rule.Rule;
import util.LogUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.IntStream;

/**
 * Created by Luke on 1/05/2016.
 */
public class PowerBallTrainer extends AbstractTrainer<PowerBallDraw>
{


    private Map<Integer, PowerBallAnalyseResult> analyseResultMap;

    public PowerBallTrainer(File resultFile)
    {
        super(resultFile, strings ->
        {
            Integer[] nums = IntStream.range(2, 8).mapToObj(i -> Integer.valueOf(strings[i])).toArray(size -> new Integer[size]);
            int powerBall = Integer.valueOf(strings[8]);
            PowerBallDraw draw = new PowerBallDraw(nums, powerBall);
            return draw;
        });

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

    private void trainingOneSetResultForFrequencyPowerHit(List<PowerBallDraw> results, PowerBallAnalyseResult powerBallAnalyseResult, TrainingResult trainingResult)
    {
        results.stream().forEach(r -> {
            int division = r.checkWinPowerHit(PowerBallDraw.generateDrawFrequencyNPBMinDistance(powerBallAnalyseResult, r.getId() + 1));
            trainingResult.addResult(division);
        });
    }

    @Override
    public Map<Integer, ? extends AbstractAnalyserResult> getAnalyseResultMap()
    {
        return analyseResultMap;
    }

    public Consumer<TrainingResult> trainingBenchMark()
    {
        return generateTrainingResultConsumer(
                trainingResult -> powerBallDrawConsumer(trainingResult,
                        draw -> PowerBallDraw.generateRandomDraw()
                ));
    }

    public Consumer<TrainingResult> trainingHitBenchMark()
    {
        return generateTrainingResultConsumer(
                trainingResult -> powerHitDrawConsumer(trainingResult,
                        draw -> PowerBallDraw.generateRandomDraw()
                ));
    }

    public Consumer<TrainingResult> trainingFrequencyNPowerBallMinDistancePattern()
    {
        return generateTrainingResultConsumer(
                trainingResult -> powerBallDrawConsumer(trainingResult,
                        draw -> PowerBallDraw.generateDrawFrequencyNPBMinDistance(analyseResultMap.get(draw.getId() - 1), draw.getId())
                ));
    }

    public Consumer<TrainingResult> trainingPowerBallMinDistancePattern()
    {
        return generateTrainingResultConsumer(
                trainingResult -> powerBallDrawConsumer(trainingResult,
                        draw -> PowerBallDraw.generateDrawPBMinDistance(analyseResultMap.get(draw.getId() - 1), draw.getId())
                ));
    }

    public Consumer<TrainingResult> trainingFrequency()
    {
        return generateTrainingResultConsumer(
                trainingResult -> powerBallDrawConsumer(trainingResult,
                        draw -> PowerBallDraw.generateDrawFrequency(analyseResultMap.get(draw.getId() - 1))
                ));
    }

    public Consumer<TrainingResult> trainingFrequencyNPB()
    {
        return generateTrainingResultConsumer(
                trainingResult -> powerBallDrawConsumer(trainingResult,
                        draw -> PowerBallDraw.generateDrawFrequencyNPB(analyseResultMap.get(draw.getId() - 1))
                ));
    }

    public Consumer<TrainingResult> trainingFrequencyNPB(List<Rule> rules)
    {
        return generateTrainingResultConsumer(
                trainingResult -> powerBallDrawConsumer(trainingResult,
                        draw -> PowerBallDraw.generateDrawFrequencyNPB(analyseResultMap.get(draw.getId() - 1), rules)
                ));
    }

    public Consumer<TrainingResult> trainingFrequency(List<Rule> rules)
    {

        return generateTrainingResultConsumer(
                trainingResult -> powerBallDrawConsumer(trainingResult,
                        draw -> PowerBallDraw.generateDrawFrequency(analyseResultMap.get(draw.getId() - 1), rules)
                ));
    }

    public Consumer<TrainingResult> trainingHitFrequencyWithRule(List<Rule> rules)
    {
        return generateTrainingResultConsumer(
                trainingResult -> powerHitDrawConsumer(trainingResult,
                        draw -> PowerBallDraw.generateDrawFrequency(analyseResultMap.get(draw.getId() - 1), rules)
                ));
    }

    public Consumer<TrainingResult> trainingHitFrequency()
    {
        return generateTrainingResultConsumer(
                trainingResult -> powerHitDrawConsumer(trainingResult,
                        draw -> PowerBallDraw.generateDrawFrequency(analyseResultMap.get(draw.getId() - 1))
                ));
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
}

