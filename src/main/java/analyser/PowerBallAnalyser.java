package analyser;

import domain.draw.PowerBallDraw;
import util.CsvUtil;
import util.LogUtil;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Luke on 5/05/2016.
 */
public class PowerBallAnalyser
{
    private File resultFile;

    public PowerBallAnalyser(File resultFile)
    {
        this.resultFile = resultFile;
    }

    public void start()
    {
        List<PowerBallDraw> results = CsvUtil.loadPowerData(resultFile);

        LogUtil.consoleLog("PowerBall Result Sample Size: " + results.size());

        Map<Integer, Integer> countMap = new HashMap<>();

        results.stream().forEach(r ->{
            Arrays.stream(r.getNums()).forEach(num->{
                int count = countMap.containsKey(num) ? countMap.get(num) : 0;
                countMap.put(num, count+1);
            });
        });

        countMap.entrySet().stream().forEach(System.out::println);
        List<AnalyseResult> analyseResults = countMap.entrySet().stream()
                .map(e -> new AnalyseResult(e.getKey(), e.getValue())).collect(Collectors.toList());

        Collections.sort(analyseResults);

        analyseResults.stream().forEach(System.out::println);
    }
}
