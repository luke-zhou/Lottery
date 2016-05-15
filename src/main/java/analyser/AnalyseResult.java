package analyser;

import domain.draw.PowerBallDraw;
import util.LogUtil;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Created by Luke on 5/05/2016.
 */
public class AnalyseResult
{
    private Integer sampleSize;
    private Map<Integer, Integer> numFrequencyMap = new HashMap<>();
    private Map<Integer, Integer> powerBallFrequencyMap = new HashMap<>();
    private Map<Integer, Integer> powerBallMinDistanceMap = new HashMap<>();
    private Map<Integer, Integer> powerBallLastResultIdMap = new HashMap<>();

    private List<Frequency> numfrequencies = new ArrayList<>();
    private List<Frequency> powerBallfrequencies = new ArrayList<>();

    private List<List<Integer>> potentialNumsGroup;

    public AnalyseResult(Integer sampleSize)
    {
        this.sampleSize = sampleSize;
    }

    public Integer getNumFrequency(Integer num)
    {
        return numFrequencyMap.containsKey(num) ? numFrequencyMap.get(num) : 0;
    }

    public void putNumFrequency(Integer num, Integer frequency)
    {
        numFrequencyMap.put(num, frequency);
    }

    public void putPowerBallFrequency(Integer powerBall, Integer frequency)
    {
        powerBallFrequencyMap.put(powerBall, frequency);
    }

    public Integer getPowerBallFrequency(Integer powerBall)
    {
        return powerBallFrequencyMap.containsKey(powerBall) ? powerBallFrequencyMap.get(powerBall) : 0;
    }

    public Integer getPowerBallMinDistance(Integer powerBall)
    {
        return powerBallMinDistanceMap.containsKey(powerBall) ? powerBallMinDistanceMap.get(powerBall) : Integer.MAX_VALUE;
    }

    public void putPowerBallMinDistance(Integer powerBall, Integer distance)
    {
        powerBallMinDistanceMap.put(powerBall, distance);
    }

    public Integer getPowerBallLastResultId(Integer powerBall)
    {
        return powerBallLastResultIdMap.containsKey(powerBall) ? powerBallLastResultIdMap.get(powerBall) : 0;
    }

    public void putPowerBallLastResultId(Integer powerBall, Integer id)
    {
        powerBallLastResultIdMap.put(powerBall, id);
    }

    public void finalize()
    {
        numfrequencies = numFrequencyMap.entrySet().stream()
                .map(e -> new Frequency(e.getKey(), e.getValue())).collect(Collectors.toList());
        Collections.sort(numfrequencies);
        LogUtil.consoleLog("num frequency:");
        numfrequencies.stream().forEach(System.out::println);
        powerBallfrequencies = powerBallFrequencyMap.entrySet().stream()
                .map(e -> new Frequency(e.getKey(), e.getValue())).collect(Collectors.toList());
        Collections.sort(powerBallfrequencies);
        LogUtil.consoleLog("power ball frequency:");
        powerBallfrequencies.stream().forEach(System.out::println);

        potentialNumsGroup = groupResultByFrequency();
        LogUtil.consoleLog("power ball last result id:");
        powerBallLastResultIdMap.entrySet().stream().forEach(System.out::println);
        IntStream.range(1, PowerBallDraw.MAX_POWER_BALL_NUM+1).forEach(i ->{
            if (!powerBallMinDistanceMap.containsKey(i)) powerBallMinDistanceMap.put(i, 0);
        });
        LogUtil.consoleLog("power ball min distance:");
        powerBallMinDistanceMap.entrySet().stream().forEach(System.out::println);
    }

    private List<List<Integer>> groupResultByFrequency()
    {
        List<Frequency> frequencies = new ArrayList<>();
        frequencies.addAll(numfrequencies);

        List<List<Integer>> potentialNumsGroup = new ArrayList<>();
        IntStream.range(0, PowerBallDraw.NUM_OF_BALL).forEach(i -> {
            List<Integer> potentialNums = new ArrayList<>();
            int size = sampleSize;
            while(!frequencies.isEmpty())
            {
                Frequency frequency = frequencies.remove(0);
                size -= frequency.getCount();
                potentialNums.add(frequency.getNum());
                if (size <= 0)
                {
                    break;
                }
            }
            potentialNumsGroup.add(potentialNums);
        });

        return potentialNumsGroup;
    }

    public List<Frequency> getNumfrequencies()
    {
        return numfrequencies;
    }

    public List<Frequency> getPowerBallfrequencies()
    {
        return powerBallfrequencies;
    }

    public List<List<Integer>> getPotentialNumsGroup()
    {
        return potentialNumsGroup;
    }

    public Map<Integer, Integer> getPowerBallMinDistanceMap()
    {
        return powerBallMinDistanceMap;
    }

    public Map<Integer, Integer> getPowerBallLastResultIdMap()
    {
        return powerBallLastResultIdMap;
    }
}

class Frequency implements Comparable<Frequency>
{
    private Integer num;
    private Integer count;

    public Frequency(Integer num, Integer count)
    {
        this.num = num;
        this.count = count;
    }

    @Override
    public int compareTo(Frequency o)
    {
        return o.getCount().compareTo(this.getCount());
    }

    public Integer getNum()
    {
        return num;
    }

    public Integer getCount()
    {
        return count;
    }



    @Override
    public String toString()
    {
        return "Frequency{" +
                "num=" + num +
                ", count=" + count +
                '}';
    }
}
