package analyser;

import domain.draw.PowerBallDraw;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

        results.stream().forEach(draw -> {
            AnalyseResult analyseResult = calculateAnalyseResult(draw);
            analyseResultMap.put(draw.getId(), analyseResult);
        });

        return analyseResultMap;
    }

    private AnalyseResult calculateAnalyseResult(PowerBallDraw draw)
    {
        AnalyseResult analyseResult = new AnalyseResult(results.indexOf(draw) + 1);

        results.stream().filter(r -> r.getId() <= draw.getId()).forEach(r -> {
            analyseResult.updateNumFrequency(r);

            analyseResult.updatePowerBallFrequency(r.getPowerBall());

            analyseResult.updatePowerBallMinDistancePattern(r.getPowerBall(), r.getId());

        });

        analyseResult.finalize();

        return analyseResult;
    }
}
