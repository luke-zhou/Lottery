import trainer.OZLottoTrainer;

import java.io.File;
import java.net.URISyntaxException;

/**
 * Created by Luke on 1/05/2016.
 */
public class OzLotto
{
    private static final String RESULT_FILE_LOCATION = "data/OzLotto.csv";

    public static void main(String[] args) throws URISyntaxException
    {
        File ozLottoResultCsv = new File(OzLotto.class.getResource(RESULT_FILE_LOCATION).toURI());

         doTraining(ozLottoResultCsv);

//        generateDraws(powerBallResultCsv);

    }

    private static void doTraining(File resultFile)
    {
        OZLottoTrainer trainer = new OZLottoTrainer(resultFile);
//        trainer.turnOnResultPrint();

        trainer.train("OZLotto Benchmark").by(trainer.trainingBenchMark());
        trainer.train("OZLotto frequency Result").by(trainer.trainingFrequency());

    }

//    private static void generateDraws(File resultFile)
//    {
//        List<PowerBallDraw> results = CsvUtil.loadPowerData(resultFile);
//
//        LogUtil.log("PowerBall Result Sample Size: " + results.size());
//
//        PowerBallAnalyser analyser = new PowerBallAnalyser(results);
//
//        Map<Integer, AnalyseResult> analyseResults = analyser.start();
//
//        List<Rule> rules = new ArrayList<>();
//        Rule rule = new Rule(2);
//        rule.addArguments(1);
//        rules.add(rule);
//        rule = new Rule(2);
//        rule.addArguments(15);
//        rules.add(rule);
//        LogUtil.log("generate for 1046");
//        IntStream.range(0, 20).mapToObj(i -> PowerBallDraw.generateDrawFrequency(analyseResults.get(1045), rules)).forEach(System.out::println);
//    }
}
