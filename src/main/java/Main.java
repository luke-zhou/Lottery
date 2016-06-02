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
    private static final String RESULT_FILE_LOCATION = "data/Powerball-1045.csv";

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
        rules.clear();
        rules.add(rule);
        trainer.train("PowerBall frequency with rule a=b+(1)").by(trainer.trainingFrequencyWithRule(rules));

        rule = new Rule(2);
        rule.addArguments(13);
        rules.clear();
        rules.add(rule);
            trainer.train("PowerBall frequency with rule a=b+(13)").by(trainer.trainingFrequencyWithRule(rules));


        IntStream.range(1,2).forEach(i->{
            IntStream.range(10,20).forEach(j->{
                rules.clear();
                Rule rule1 = new Rule(2);
                rule1.addArguments(i);
                rules.add(rule1);
                Rule rule2 = new Rule(2);
                rule2.addArguments(j);
                rules.add(rule1);
                trainer.train("PowerBall frequency with rule a=b+("+i+", "+j+")").by(trainer.trainingFrequencyWithRule(rules));
            });
        });


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

        List<Rule> rules = new ArrayList<>();
        Rule rule = new Rule(2);
        rule.addArguments(1);
        rules.add(rule);
        rule = new Rule(2);
        rule.addArguments(15);
        rules.add(rule);
        LogUtil.log("generate for 1046");
        IntStream.range(0, 20).mapToObj(i -> PowerBallDraw.generateDrawFrequency(analyseResults.get(1045), rules)).forEach(System.out::println);
    }
}
