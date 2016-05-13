package analyser;

import domain.draw.PowerBallDraw;
import util.CsvUtil;
import util.LogUtil;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;
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

    public AnalyseResult start()
    {
        AnalyseResult analyseResult = new AnalyseResult(results.size());

        Map<Integer, Integer> powerBallLastResultMap = new HashMap<>();

        results.stream().forEach(r ->{
            Arrays.stream(r.getNums()).forEach(num->analyseResult.putNumFrequency(num, analyseResult.getNumFrequency(num)+1));
            analyseResult.putPowerBallFrequency(r.getPowerBall(), analyseResult.getPowerBallFrequency(r.getPowerBall())+1) ;
        });

        analyseResult.finalize();

        return analyseResult;
    }
}
