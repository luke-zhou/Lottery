package domain.draw;

import domain.rule.Rule;

import java.util.*;
import java.util.stream.IntStream;

/**
 * Created by Luke on 13/05/2014.
 */
public class PowerBallDraw extends Draw
{
    public static int MAX_NUM = 40;
    public static int MAX_POWER_BALL_NUM = 20;
    public static int NUM_OF_BALL = 6;
    private static Random random = new Random(System.currentTimeMillis());

    private Integer[] nums;
    private Integer[] sortedNums;
    private Integer powerBall;

    public PowerBallDraw(Integer[] nums, Integer powerBall)
    {
        this.nums = Arrays.copyOf(nums, NUM_OF_BALL);
        this.sortedNums = Arrays.copyOf(nums, NUM_OF_BALL);
        Arrays.sort(sortedNums);
        this.powerBall = powerBall;
    }

    public int checkWinPowerHit(PowerBallDraw draw)
    {
        return calculateDivision(sortedNums, draw.getSortedNums(), true);
    }

    private int calculateDivision(Integer[] thisDraw, Integer[] testedDraw, boolean powerBallCheck)
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

        return getDivision(count, powerBallCheck);
    }

    private int getDivision(int correctCount, boolean powerBallCheck)
    {
        int division = 0;

        if (correctCount == 2 && powerBallCheck)
        {
            division = 1;//division 8
        }
        else if (correctCount == 4)
        {
            division = 2;//division 7
        }
        else if (correctCount == 3 && powerBallCheck)
        {
            division = 3; //division 6
        }
        else if (correctCount == 4 && powerBallCheck)
        {
            division = 5; //division 5
        }
        else if (correctCount == 5 && !powerBallCheck)
        {
            division = 10; //division 4
        }
        else if (correctCount == 5 && powerBallCheck)
        {
            division = 500; //division 3
        }
        else if (correctCount == 6 && !powerBallCheck)
        {
            division = 3500; //division 2
        }
        else if (correctCount == 6 && powerBallCheck)
        {
            division = 500000; //division 1
        }

        return division;
    }

    private Double getDiffer(Integer[] thisDraw, Integer[] testedDraw)
    {
        List<Integer> thisList = new ArrayList<Integer>();
        List<Integer> testedList = new ArrayList<Integer>();

        for (int j = 0, i = 0; i < NUM_OF_BALL || j < NUM_OF_BALL; )
        {
            if (j == NUM_OF_BALL)
            {
                thisList.add(thisDraw[i]);
                i++;
            }
            else if (i == NUM_OF_BALL)
            {
                testedList.add(testedDraw[j]);
                j++;
            }
            else if (thisDraw[i] < testedDraw[j])
            {
                thisList.add(thisDraw[i]);
                i++;
            }
            else if (thisDraw[i] == testedDraw[j])
            {
                i++;
                j++;
            }
            else if (thisDraw[i] > testedDraw[j])
            {
                testedList.add(testedDraw[j]);
                j++;
            }
        }
        int differ = 0;
        for (int i = 0; i < thisList.size(); i++)
        {
            differ += (testedList.get(i) - thisList.get(i)) * (testedList.get(i) - thisList.get(i));
        }
        return Double.valueOf(differ);
    }

    public int checkWin(PowerBallDraw draw)
    {
        return calculateDivision(sortedNums, draw.getSortedNums(), powerBall == draw.getPowerBall());
    }

    public String toStringSorted()
    {
        return "PowerBallDraw{" +
                "nums=" + Arrays.toString(sortedNums) +
                ", powerBall=" + powerBall +
                '}';

    }

    @Override
    public String toString()
    {
        return "PowerBallDraw{" +
                "nums=" + Arrays.toString(nums) +
                ", powerBall=" + powerBall +
                '}';
    }

    //index is 1 based
    @Override
    public Integer getNum(int index) throws Exception
    {
        if (index < 1 || index > NUM_OF_BALL) throw new Exception("not in range");
        return nums[index - 1];
    }

    public Integer getPowerBall()
    {
        return powerBall;
    }

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

    public static PowerBallDraw generateDraw(Rule rule)
    {
        Integer[] selection;
        PowerBallDraw draw;
        do
        {
            selection = randomGenerateSelection();
            int powerBallSelection = random.nextInt(MAX_POWER_BALL_NUM) + 1;
            draw = new PowerBallDraw(selection, powerBallSelection);
        } while (!draw.follow(rule));


        return draw;
    }

    public static PowerBallDraw generateDraw(List<List<Integer>> potentialNumsGroup)
    {
        Integer[] selection;
        PowerBallDraw draw;
        do
        {
            selection = randomGenerateSelection(potentialNumsGroup);
            int powerBallSelection = random.nextInt(MAX_POWER_BALL_NUM) + 1;
            draw = new PowerBallDraw(selection, powerBallSelection);
        } while (!draw.follow(Rule.NO_RULE));


        return draw;
    }

    private boolean follow(Rule rule)
    {
        boolean result = false;
        if (rule.getInvolvedNumberCount() == 0)
        {
            result = Arrays.stream(this.getNums()).anyMatch(i -> true);
        }
        else if (rule.getInvolvedNumberCount() == 1)
        {
            result = Arrays.stream(this.getNums()).anyMatch(i -> i == rule.getArguments().get(0));
        }
        else if (rule.getInvolvedNumberCount() == 2)
        {
            result = Arrays.stream(this.getNums()).anyMatch(i -> Arrays.stream(this.getNums()).anyMatch(j -> j == i + rule.getArguments().get(0)));
        }
        return result;
    }

    private static Integer[] randomGenerateSelection()
    {
        Set<Integer> selectionSet = new HashSet<>();

        while (selectionSet.size() < NUM_OF_BALL)
        {
            selectionSet.add(random.nextInt(MAX_NUM) + 1);
        }

        return selectionSet.toArray(new Integer[NUM_OF_BALL]);
    }

    private static Integer[] randomGenerateSelection(List<List<Integer>> potentialNumsGroup)
    {
        Set<Integer> selectionSet = new HashSet<>();

        while (selectionSet.size() < NUM_OF_BALL)
        {
            List<Integer> potentialNums = new ArrayList<Integer>();
            IntStream.range(0,selectionSet.size()+1).forEach(j->potentialNums.addAll(potentialNumsGroup.get(j)));
            int num = random.nextInt(potentialNums.size());
            selectionSet.add(potentialNums.get(num));
        }

        return selectionSet.toArray(new Integer[NUM_OF_BALL]);
    }
}
