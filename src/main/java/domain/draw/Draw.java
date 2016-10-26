package domain.draw;

import java.util.Arrays;
import java.util.Date;
import java.util.Random;

/**
 * Created by Luke on 13/05/2014.
 */
public abstract class Draw
{
    private Integer id;
    private Date date;
    protected Integer[] nums;
    protected Integer[] sortedNums;

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public Date getDate()
    {
        return date;
    }

    public void setDate(Date date)
    {
        this.date = date;
    }

    //index is 1 based
    public Integer getNum(int index) throws Exception
    {
        if (index < 1 || index > getNumOfBall()) throw new Exception("not in range");
        return nums[index - 1];
    }

    public abstract Integer getNumOfBall();
    public abstract int checkWin(Draw draw);
    public abstract String toWinResultString(Draw actualResult);

    public Boolean hasNum(int num)
    {
        return Arrays.stream(nums).anyMatch(i -> i == num);
    }

    public Integer[] getNums()
    {
        return nums;
    }

    public Integer[] getSortedNums()
    {
        return sortedNums;
    }

}
