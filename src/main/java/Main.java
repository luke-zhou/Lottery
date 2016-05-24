import analyser.AnalyseResult;
import analyser.PowerBallAnalyser;
import domain.draw.PowerBallDraw;
import trainer.PowerBallTrainer;
import util.CsvUtil;
import util.LogUtil;

import java.io.File;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

/**
 * Created by Luke on 1/05/2016.
 */
public class Main
{
    private static final String RESULT_FILE_LOCATION = "data/Powerball-1043.csv";

    public static void main(String[] args) throws URISyntaxException
    {
        File powerBallResultCsv = new File(Main.class.getResource(RESULT_FILE_LOCATION).toURI());

        PowerBallTrainer trainer = new PowerBallTrainer(powerBallResultCsv);

        trainer.train("PowerBall Benchmark").by(trainer.trainingBenchMark());
        trainer.train("PowerBall frequency Result").by(trainer.trainingFrequency());
        trainer.train("PowerBall frequency N PB Mini Distance Pattern Result").by(trainer.trainingFrequencyNPowerBallMinDistancePattern());

//        generateDraws(powerBallResultCsv);

    }

    private static void generateDraws(File resultFile)
    {
        List<PowerBallDraw> results = CsvUtil.loadPowerData(resultFile);

        LogUtil.log("PowerBall Result Sample Size: " + results.size());

        PowerBallAnalyser analyser = new PowerBallAnalyser(results);

        Map<Integer, AnalyseResult> analyseResults = analyser.start();

        LogUtil.log("generate for 1044");
        IntStream.range(0, 20).mapToObj(i -> PowerBallDraw.generateDraw(analyseResults.get(1043), 1044)).forEach(System.out::println);
    }
}
