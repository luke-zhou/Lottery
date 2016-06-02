package analyser;

/**
 * Created by Luke on 2/06/2016.
 */
public class Frequency implements Comparable<Frequency>
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
