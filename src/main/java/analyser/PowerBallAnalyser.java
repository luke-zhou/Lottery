package analyser;

import domain.draw.PowerBallDraw;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

/**
 * Created by Luke on 5/05/2016.
 */
public class PowerBallAnalyser
{
    private List<PowerBallDraw> results;

    public PowerBallAnalyser(List<PowerBallDraw> results)
    {
        this.results = results;
    }

    public Map<Integer, AnalyseResult> start()
    {
        Map<Integer, AnalyseResult> analyseResultMap = new HashMap<>();


        results.stream().forEach(i -> {
            AnalyseResult analyseResult = new AnalyseResult(results.indexOf(i)+1);

            results.stream().filter(r -> r.getId() <= i.getId()).forEach(r -> {
                Arrays.stream(r.getNums()).forEach(num -> analyseResult.putNumFrequency(num, analyseResult.getNumFrequency(num) + 1));

                int powerBall = r.getPowerBall();
                analyseResult.putPowerBallFrequency(powerBall, analyseResult.getPowerBallFrequency(powerBall) + 1);

                int lastResultId = analyseResult.getPowerBallLastResultId(powerBall);
                analyseResult.putPowerBallLastResultId(powerBall, r.getId());
                analyseResult.putPowerBallMinDistance(powerBall, Integer.min(r.getId() - lastResultId, analyseResult.getPowerBallMinDistance(powerBall)));
            });

            analyseResult.finalize();
            analyseResultMap.put(i.getId(), analyseResult);
        });

        return analyseResultMap;
    }
}
