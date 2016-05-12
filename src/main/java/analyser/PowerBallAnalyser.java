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

    public List<AnalyseResult> start()
    {
        Map<Integer, Integer> countMap = new HashMap<>();

        results.stream().forEach(r ->{
            Arrays.stream(r.getNums()).forEach(num->{
                int count = countMap.containsKey(num) ? countMap.get(num) : 0;
                countMap.put(num, count+1);
            });
        });

//        countMap.entrySet().stream().forEach(System.out::println);
        List<AnalyseResult> analyseResults = countMap.entrySet().stream()
                .map(e -> new AnalyseResult(e.getKey(), e.getValue())).collect(Collectors.toList());

        Collections.sort(analyseResults);

        analyseResults.stream().forEach(System.out::println);

        return analyseResults;
    }

    public List<List<Integer>> groupResultByFrequency(List<AnalyseResult> analyseResults)
    {
        List<List<Integer>> potentialNumsGroup = new ArrayList<>();
        IntStream.range(0, PowerBallDraw.NUM_OF_BALL).forEach(i -> {
            List<Integer> potentialNums = new ArrayList<>();
            int sampleSize = results.size();
            while(!analyseResults.isEmpty())
            {
                AnalyseResult analyseResult = analyseResults.remove(0);
                sampleSize -= analyseResult.getCount();
                potentialNums.add(analyseResult.getNum());
                if (sampleSize <= 0)
                {
                    break;
                }
            }
            potentialNumsGroup.add(potentialNums);
        });

        return potentialNumsGroup;
    }
}
