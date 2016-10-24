package domain.analyserresult;

import analyser.Frequency;
import domain.draw.PowerBallDraw;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Created by Luke on 5/05/2016.
 */
public class PowerBallAnalyseResult extends AbstractAnalyserResult
{
    private Map<Integer, Integer> powerBallFrequencyMap = new HashMap<>();
    private Map<Integer, Integer> powerBallMinDistanceMap = new HashMap<>();
    private Map<Integer, Integer> powerBallLastResultIdMap = new HashMap<>();

    private List<Frequency> powerBallfrequencies = new ArrayList<>();

    public PowerBallAnalyseResult(Integer sampleSize)
    {
        super(sampleSize);
    }

    @Override
    protected int getNumOfBall()
    {
        return PowerBallDraw.NUM_OF_BALL;
    }

    public Integer getPowerBallFrequency(Integer powerBall)
    {
        return powerBallFrequencyMap.containsKey(powerBall) ? powerBallFrequencyMap.get(powerBall) : 0;
    }

    public Integer getPowerBallMinDistance(Integer powerBall)
    {
        return powerBallMinDistanceMap.containsKey(powerBall) ? powerBallMinDistanceMap.get(powerBall) : Integer.MAX_VALUE;
    }

    public void updatePowerBallMinDistancePattern(Integer powerBall, Integer drawId)
    {
        Integer lastResultId = getPowerBallLastResultId(powerBall);
        if (lastResultId != 0)
        {
            powerBallMinDistanceMap.put(powerBall, Integer.min(drawId - lastResultId, getPowerBallMinDistance(powerBall)));
        }
        else
        {
            powerBallMinDistanceMap.put(powerBall, Integer.MAX_VALUE);
        }
        putPowerBallLastResultId(powerBall, drawId);
    }

    public void updatePowerBallFrequency(int powerBall)
    {
        powerBallFrequencyMap.put(powerBall, getPowerBallFrequency(powerBall) + 1);
    }

    public Integer getPowerBallLastResultId(Integer powerBall)
    {
        return powerBallLastResultIdMap.containsKey(powerBall) ? powerBallLastResultIdMap.get(powerBall) : 0;
    }

    public void putPowerBallLastResultId(Integer powerBall, Integer id)
    {
        powerBallLastResultIdMap.put(powerBall, id);
    }


    public List<Frequency> getPowerBallfrequencies()
    {
        return powerBallfrequencies;
    }


    public Map<Integer, Integer> getPowerBallMinDistanceMap()
    {
        return powerBallMinDistanceMap;
    }

    public Map<Integer, Integer> getPowerBallLastResultIdMap()
    {
        return powerBallLastResultIdMap;
    }

    public void finalize()
    {
        super.finalize();
        powerBallfrequencies = powerBallFrequencyMap.entrySet().stream()
                .map(e -> new Frequency(e.getKey(), e.getValue())).collect(Collectors.toList());
        Collections.sort(powerBallfrequencies);
//        LogUtil.log("power ball frequency:");
//        powerBallfrequencies.stream().forEach(System.out::println);

        IntStream.range(1, PowerBallDraw.MAX_POWER_BALL_NUM+1).forEach(i ->{
            if (!powerBallMinDistanceMap.containsKey(i)) powerBallMinDistanceMap.put(i, 0);
        });
//        LogUtil.log("power ball min distance:");
//        powerBallMinDistanceMap.entrySet().stream().forEach(System.out::println);
    }

    @Override
    public String toString()
    {
        return "AnalyseResult{" +
                "sampleSize=" + sampleSize +
                ", numFrequencyMap=" + numFrequencyMap +
                ", powerBallFrequencyMap=" + powerBallFrequencyMap +
                ", powerBallMinDistanceMap=" + powerBallMinDistanceMap +
                ", powerBallLastResultIdMap=" + powerBallLastResultIdMap +
                ", numFrequencies=" + numFrequencies +
                ", powerBallfrequencies=" + powerBallfrequencies +
                ", potentialNumsGroup=" + potentialNumsGroup +
                '}';
    }
}
