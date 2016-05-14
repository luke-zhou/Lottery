import analyser.AnalyseResult;
import analyser.PowerBallAnalyser;
import domain.draw.PowerBallDraw;
import trainer.PowerBallTrainer;
import util.CsvUtil;
import util.LogUtil;

import java.io.File;
import java.net.URISyntaxException;
import java.util.List;
import java.util.stream.IntStream;

/**
 * Created by Luke on 1/05/2016.
 */
public class Main
{
    public static void main(String[] args) throws URISyntaxException
    {
        File powerBallResultCsv = new File(Main.class.getResource("data/Powerball-1042.csv").toURI());

        PowerBallTrainer trainer = new PowerBallTrainer(powerBallResultCsv);

        trainer.start();

//        generateDraws(powerBallResultCsv);

    }

    private static void generateDraws(File resultFile)
    {
        List<PowerBallDraw> results = CsvUtil.loadPowerData(resultFile);

        LogUtil.consoleLog("PowerBall Result Sample Size: " + results.size());

        PowerBallAnalyser analyser = new PowerBallAnalyser(results);

        AnalyseResult analyseResult = analyser.start();

        IntStream.range(0, 20).mapToObj(i -> PowerBallDraw.generateDraw(analyseResult, results.size())).forEach(System.out::println);
    }
}
