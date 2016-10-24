package domain.analyserresult;

import analyser.Frequency;
import domain.draw.Draw;
import domain.draw.PowerBallDraw;
import util.LogUtil;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Created with IntelliJ IDEA.
 * User: lzhou
 * Date: 24/10/2016
 * Time: 1:22 PM
 */
public abstract class AbstractAnalyserResult
{
    protected Integer sampleSize;
    protected Map<Integer, Integer> numFrequencyMap = new HashMap<>();
    protected List<Frequency> numFrequencies = new ArrayList<>();
    protected List<List<Integer>> potentialNumsGroup;

    public AbstractAnalyserResult(Integer sampleSize)
    {
        this.sampleSize = sampleSize;
    }

    public Integer getNumFrequency(Integer num)
    {
        return numFrequencyMap.containsKey(num) ? numFrequencyMap.get(num) : 0;
    }

    public void updateNumFrequency(Draw draw)
    {
        Arrays.stream(draw.getNums()).forEach(num -> numFrequencyMap.put(num, getNumFrequency(num) + 1));
    }

    private List<List<Integer>> groupResultByFrequency()
    {
        List<Frequency> frequencies = new ArrayList<>();
        frequencies.addAll(numFrequencies);

        List<List<Integer>> potentialNumsGroup = new ArrayList<>();
        IntStream.range(0, getNumOfBall()).forEach(i -> {
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

    protected abstract int getNumOfBall();

    public void finalize()
    {
        numFrequencies = numFrequencyMap.entrySet().stream()
                .map(e -> new Frequency(e.getKey(), e.getValue())).collect(Collectors.toList());
        Collections.sort(numFrequencies);
//        LogUtil.log("num frequency:");
//        numFrequencies.stream().forEach(System.out::println);

        potentialNumsGroup = groupResultByFrequency();
    }

    public List<Frequency> getNumFrequencies()
    {
        return numFrequencies;
    }

    public List<List<Integer>> getPotentialNumsGroup()
    {
        return potentialNumsGroup;
    }

    public Integer getSampleSize()
    {
        return sampleSize;
    }
}
