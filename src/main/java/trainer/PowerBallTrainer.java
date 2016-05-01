package trainer;

import domain.draw.PowerBallDraw;
import util.CsvUtil;
import util.LogUtil;

import java.io.File;
import java.util.List;
import java.util.stream.IntStream;

/**
 * Created by Luke on 1/05/2016.
 */
public class PowerBallTrainer
{
    private int BENCHMARK_TRAINING_REPEAT_TIMES = 100000;
    private File resultFile;

    public PowerBallTrainer(File resultFile)
    {
        this.resultFile = resultFile;
    }

    public void start()
    {
        List<PowerBallDraw> results = CsvUtil.loadPowerData(resultFile);

        LogUtil.consoleLog("PowerBall Result Sample Size: " + results.size());

        int totalWin = 0;
        int totalDivision = 0;
        int totalTrainingSize = 0;

        for (int i = 0; i < BENCHMARK_TRAINING_REPEAT_TIMES; i++)
        {
            for (PowerBallDraw draw : results)
            {
                int division = draw.checkWin(PowerBallDraw.generateDraw());
                totalWin += division > 0 ? 1 : 0;
                totalDivision += division;
                totalTrainingSize++;
            }
        }

        LogUtil.consoleLog(String.format("PowerBall Benchmark: (total win:%1d,total division:%2d, total training:%3d)", totalWin, totalDivision, totalTrainingSize));

        totalWin = 0;
        totalDivision = 0;
        totalTrainingSize = 0;

        for (int i = 0; i < BENCHMARK_TRAINING_REPEAT_TIMES; i++)
        {
            for (PowerBallDraw draw : results)
            {
                int division = draw.checkWinPowerHit(PowerBallDraw.generateDraw());
                totalWin += division > 0 ? 1 : 0;
                totalDivision += division;
                totalTrainingSize++;
            }
        }

        LogUtil.consoleLog(String.format("PowerHit Benchmark: (total win:%1d,total division:%2d, total training:%3d)", totalWin/20, totalDivision/20, totalTrainingSize));

    }
}

