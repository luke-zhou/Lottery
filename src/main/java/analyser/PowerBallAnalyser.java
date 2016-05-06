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
    private List<PowerBallDraw> results;

    public PowerBallAnalyser(List<PowerBallDraw> results)
    {
        this.results = results;
    }

    public List<AnalyseResult> start()
    {
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

        return analyseResults;
    }
}
