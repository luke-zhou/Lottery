package analyser;

import domain.analyserresult.PowerBallAnalyseResult;
import domain.draw.PowerBallDraw;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Luke on 5/05/2016.
 */
public class PowerBallAnalyser extends AbstractAnalyser<PowerBallDraw>
{
    public PowerBallAnalyser(List<PowerBallDraw> results)
    {
        super(results);
    }

    public Map<Integer, PowerBallAnalyseResult> start()
    {
        Map<Integer, PowerBallAnalyseResult> analyseResultMap = new HashMap<>();

        results.stream().forEach(draw -> {
            PowerBallAnalyseResult powerBallAnalyseResult = calculateAnalyseResult(draw);
            analyseResultMap.put(draw.getId(), powerBallAnalyseResult);
        });

        return analyseResultMap;
    }

    private PowerBallAnalyseResult calculateAnalyseResult(PowerBallDraw draw)
    {
        PowerBallAnalyseResult powerBallAnalyseResult = new PowerBallAnalyseResult(results.indexOf(draw) + 1);

        results.stream().filter(r -> r.getId() <= draw.getId()).forEach(r -> {
            powerBallAnalyseResult.updateNumFrequency(r);

            powerBallAnalyseResult.updatePowerBallFrequency(r.getPowerBall());

            powerBallAnalyseResult.updatePowerBallMinDistancePattern(r.getPowerBall(), r.getId());

        });

        powerBallAnalyseResult.finalize();

        return powerBallAnalyseResult;
    }
}
