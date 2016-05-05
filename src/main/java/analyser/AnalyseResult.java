package analyser;

import java.util.Comparator;

/**
 * Created by Luke on 5/05/2016.
 */
public class AnalyseResult implements Comparable<AnalyseResult>
{
    private Integer num;
    private Integer count;

    public AnalyseResult(Integer num, Integer count)
    {
        this.num = num;
        this.count = count;
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
    public int compareTo(AnalyseResult o)
    {
        return o.getCount().compareTo(this.getCount());
    }

    @Override
    public String toString()
    {
        return "AnalyseResult{" +
                "num=" + num +
                ", count=" + count +
                '}';
    }
}
