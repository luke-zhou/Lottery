package domain.draw;

import domain.analyserresult.AbstractAnalyserResult;
import domain.analyserresult.OZLottoAnalyserResult;
import util.NumberGenUtil;

import java.util.Arrays;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Created with IntelliJ IDEA.
 * User: lzhou
 * Date: 21/10/2016
 * Time: 12:06 PM
 */
public class OZLottoDraw extends Draw
{
    public static int MAX_NUM = 45;
    public static int NUM_OF_BALL = 7;

    public OZLottoDraw(Integer[] nums)
    {
        this.nums = Arrays.copyOf(nums, NUM_OF_BALL);
        this.sortedNums = Arrays.copyOf(nums, NUM_OF_BALL);
        Arrays.sort(sortedNums);
    }

    @Override
    public Integer getNumOfBall()
    {
        return NUM_OF_BALL;
    }

    public int checkWin(Draw draw)
    {
        return calculateDivision(sortedNums, draw.getSortedNums());
    }

    private int calculateDivision(Integer[] thisDraw, Integer[] testedDraw)
    {
        int count = 0;

        for (int j = 0, i = 0; i < NUM_OF_BALL && j < NUM_OF_BALL; )
        {
            if (thisDraw[i] < testedDraw[j])
            {
                i++;
            }
            else if (thisDraw[i] == testedDraw[j])
            {
                count++;
                i++;
                j++;
            }
            else if (thisDraw[i] > testedDraw[j])
            {
                j++;
            }
        }

        return getDivision(count, false);
    }

    private int getDivision(int correctCount, boolean supplyNumberCheck)
    {
        int division = 0;

        if (correctCount == 3 && supplyNumberCheck)
        {
            division = 1;//division 7
        }
        else if (correctCount == 4)
        {
            division = 2;//division 6
        }
        else if (correctCount == 5 && !supplyNumberCheck)
        {
            division = 4; //division 5
        }
        else if (correctCount == 5 && supplyNumberCheck)
        {
            division = 8; //division 4
        }
        else if (correctCount == 6 && !supplyNumberCheck)
        {
            division = 16; //division 3
        }
        else if (correctCount == 6 && supplyNumberCheck)
        {
            division = 32; //division 2
        }
        else if (correctCount == 7)
        {
            division = 64; //division 1
        }

        return division;
    }

    public String toWinResultString(Draw actualResult)
    {
        if ( !(actualResult instanceof OZLottoDraw)){
            throw new IllegalArgumentException("Wrong type of draw");
        }
        OZLottoDraw actualOZLottoDrawResult = (OZLottoDraw) actualResult;
        StringBuilder winResult = new StringBuilder();
        Arrays.stream(sortedNums).forEach(i -> {
            winResult.append(i);
            if (Arrays.stream(actualOZLottoDrawResult.getNums()).anyMatch(j -> j == i))
            {
                winResult.append("*");
            }
            winResult.append(", ");
        });
        return "OZLottoDraw{" +
                "nums=" + winResult.toString() +
                '}';
    }

    public static OZLottoDraw generateRandomDraw()
    {
        Integer[] selection;
        OZLottoDraw draw;

        selection = NumberGenUtil.randomGenerateSelection(NUM_OF_BALL, MAX_NUM);
        draw = new OZLottoDraw(selection);


        return draw;
    }

    public static OZLottoDraw generateDrawFrequencyByGroup(AbstractAnalyserResult analyserResult)
    {
        Integer[] selection;

        selection = NumberGenUtil.randomGenerateSelection(NUM_OF_BALL, analyserResult.getPotentialNumsGroup());
        OZLottoDraw draw = new OZLottoDraw(selection);


        return draw;
    }

    public static OZLottoDraw generateDrawFrequencyByWeight(AbstractAnalyserResult analyserResult)
    {
        Integer[] selection;

        selection = NumberGenUtil.randomGenerateSelection(NUM_OF_BALL, analyserResult.getNumFrequencies());
        OZLottoDraw draw = new OZLottoDraw(selection);


        return draw;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        OZLottoDraw draw = (OZLottoDraw) o;

        return Arrays.equals(sortedNums, draw.sortedNums);
    }

    @Override
    public int hashCode()
    {
        int result = Arrays.hashCode(sortedNums);
        return result;
    }

    @Override
    public String toString()
    {
        return "OZLottoDraw{" +
                "nums=" + Arrays.toString(sortedNums) +
                '}';
    }
}
