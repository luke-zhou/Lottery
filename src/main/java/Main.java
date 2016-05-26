import analyser.AnalyseResult;
import analyser.PowerBallAnalyser;
import domain.draw.PowerBallDraw;
import domain.rule.Rule;
import trainer.PowerBallTrainer;
import util.CsvUtil;
import util.LogUtil;

import java.io.File;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

/**
 * Created by Luke on 1/05/2016.
 */
public class Main
{
    private static final String RESULT_FILE_LOCATION = "data/Powerball-1044.csv";

    public static void main(String[] args) throws URISyntaxException
    {
        File powerBallResultCsv = new File(Main.class.getResource(RESULT_FILE_LOCATION).toURI());

//         doTraining(powerBallResultCsv);

        generateDraws(powerBallResultCsv);

    }

    private static void doTraining(File resultFile)
    {
        PowerBallTrainer trainer = new PowerBallTrainer(resultFile);
//        trainer.turnOnResultPrint();

        trainer.train("PowerBall Benchmark").by(trainer.trainingBenchMark());
        trainer.train("PowerHit Benchmark").by(trainer.trainingHitBenchMark());
        trainer.train("PowerBall frequency Result").by(trainer.trainingFrequency());
        trainer.train("PowerHit frequency Result").by(trainer.trainingHitFrequency());
//        trainer.train("PowerBall PB Mini Distance Pattern Result").by(trainer.trainingPowerBallMinDistancePattern());
//        trainer.train("PowerBall frequency N PB Mini Distance Pattern Result").by(trainer.trainingFrequencyNPowerBallMinDistancePattern());

        List<Rule> rules = new ArrayList<>();
        Rule rule = new Rule(2);
        rule.addArguments(1);
            rules.add(rule);
        trainer.train("PowerBall frequency with rule a=b+(1)").by(trainer.trainingFrequencyWithRule(rules));
        rules.clear();

        rule = new Rule(2);
        rule.addArguments(13);
        rules.add(rule);
            trainer.train("PowerBall frequency with rule a=b+(13)").by(trainer.trainingFrequencyWithRule(rules));

        rule = new Rule(2);
        rule.addArguments(1);
        rules.add(rule);
        rule = new Rule(2);
        rule.addArguments(13);
        rules.add(rule);
        trainer.train("PowerBall frequency with rule a=b+(1, 13)").by(trainer.trainingFrequencyWithRule(rules));

//        IntStream.range(1, 36).forEach(i -> {
//            Rule rule = new Rule(2);
//            rule.addArguments(i);
//            List<Rule> rules = new ArrayList<>();
//            rules.add(rule);
//            trainer.train("PowerBall frequency with rule a=b+" + i).by(trainer.trainingFrequencyWithRule(rules));
//        });
//        IntStream.range(1, 40).forEach(i -> {
//            Rule rule = new Rule(2);
//            rule.addArguments(i);
//            List<Rule> rules = new ArrayList<>();
//            rules.add(rule);
//            trainer.train("PoweHit frequency with rule a=b+" + i).by(trainer.trainingHitFrequencyWithRule(rules));
//        });
    }

    private static void generateDraws(File resultFile)
    {
        List<PowerBallDraw> results = CsvUtil.loadPowerData(resultFile);

        LogUtil.log("PowerBall Result Sample Size: " + results.size());

        PowerBallAnalyser analyser = new PowerBallAnalyser(results);

        Map<Integer, AnalyseResult> analyseResults = analyser.start();

        Rule rule = new Rule(2);
        rule.addArguments(1);
        List<Rule> rules = new ArrayList<>();
        rules.add(rule);
        rule = new Rule(2);
        rule.addArguments(13);
        rules.add(rule);
        LogUtil.log("generate for 1045");
        IntStream.range(0, 20).mapToObj(i -> PowerBallDraw.generateDrawFrequency(analyseResults.get(1044), rules)).forEach(System.out::println);
    }
}
