package domain.draw;

import domain.analyserresult.PowerBallAnalyseResult;
import analyser.Frequency;
import domain.rule.Rule;
import util.NumberGenUtil;

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
    protected static Random random = new Random(System.currentTimeMillis());

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
        int division = IntStream.range(0,19).map(_i->calculateDivision(sortedNums, draw.getSortedNums(), false)).sum();
        return calculateDivision(sortedNums, draw.getSortedNums(), true)+ division;
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

    public String toStringUnSorted()
    {
        return "PowerBallDraw{" +
                "nums=" + Arrays.toString(nums) +
                ", powerBall=" + powerBall +
                '}';

    }

    @Override
    public String toString()
    {
        return "PowerBallDraw{" +
                "nums=" + Arrays.toString(sortedNums) +
                ", powerBall=" + powerBall +
                '}';
    }

    public String toWinResultString(PowerBallDraw actualResult)
    {
        StringBuilder winResult = new StringBuilder();
        Arrays.stream(sortedNums).forEach(i->{
            winResult.append(i);
            if (Arrays.stream(actualResult.getNums()).anyMatch(j-> j==i))
            {
                winResult.append("*");
            }
            winResult.append(", ");
        });
        return "PowerBallDraw{" +
                "nums=" + winResult.toString() +
                "powerBall=" + powerBall + (powerBall==actualResult.getPowerBall()?"*":"")+
                '}';
    }

    public Integer getPowerBall()
    {
        return powerBall;
    }

    @Override
    public Integer getNumOfBall()
    {
        return NUM_OF_BALL;
    }

    public static PowerBallDraw generateDraw(Rule rule)
    {
        Integer[] selection;
        PowerBallDraw draw;
        do
        {
            selection = NumberGenUtil.randomGenerateSelection(NUM_OF_BALL, MAX_NUM);
            int powerBallSelection = NumberGenUtil.randomGenerateNumber(MAX_POWER_BALL_NUM);
            draw = new PowerBallDraw(selection, powerBallSelection);
        } while (!draw.follow(rule));


        return draw;
    }

    public static PowerBallDraw generateRandomDraw()
    {
        return generateDraw(Rule.NO_RULE);
    }

    public static PowerBallDraw generateDrawFrequencyNPBMinDistance(PowerBallAnalyseResult powerBallAnalyseResult, Integer id)
    {
        Integer[] selection;
        PowerBallDraw draw;
        do
        {
            selection = NumberGenUtil.randomGenerateSelection(NUM_OF_BALL, powerBallAnalyseResult.getPotentialNumsGroup());
            int powerBallSelection = randomGeneratePowerBall(powerBallAnalyseResult, id);
            draw = new PowerBallDraw(selection, powerBallSelection);

        } while (!draw.follow(Rule.NO_RULE));

        return draw;
    }

    public static PowerBallDraw generateDrawFrequency(PowerBallAnalyseResult powerBallAnalyseResult, List<Rule> rules)
    {
        Integer[] selection;
        PowerBallDraw[] draw = new PowerBallDraw[1];
        do
        {
            selection = NumberGenUtil.randomGenerateSelection(NUM_OF_BALL, powerBallAnalyseResult.getPotentialNumsGroup());
            int powerBallSelection = random.nextInt(MAX_POWER_BALL_NUM) + 1;
            draw[0] = new PowerBallDraw(selection, powerBallSelection);
        } while (rules.stream().anyMatch(rule -> !draw[0].follow(rule)));

        return draw[0];
    }

    public static PowerBallDraw generateDrawFrequencyNPB(PowerBallAnalyseResult powerBallAnalyseResult, List<Rule> rules)
    {
        Integer[] selection;
        PowerBallDraw[] draw = new PowerBallDraw[1];
        do
        {
            selection = NumberGenUtil.randomGenerateSelection(NUM_OF_BALL, powerBallAnalyseResult.getPotentialNumsGroup());
            int powerBallSelection = randomGeneratePowerBall(powerBallAnalyseResult);
            draw[0] = new PowerBallDraw(selection, powerBallSelection);
        } while (rules.stream().anyMatch(rule -> !draw[0].follow(rule)));

        return draw[0];
    }

    public static PowerBallDraw generateDrawFrequencyNPB(PowerBallAnalyseResult powerBallAnalyseResult)
    {
        return generateDrawFrequencyNPB(powerBallAnalyseResult, new ArrayList<>());
    }

    public static PowerBallDraw generateDrawFrequency(PowerBallAnalyseResult powerBallAnalyseResult)
    {
        return generateDrawFrequency(powerBallAnalyseResult, new ArrayList<>());
    }

    public static PowerBallDraw generateDrawPBMinDistance(PowerBallAnalyseResult powerBallAnalyseResult, Integer id)
    {
        Integer[] selection;
        PowerBallDraw draw;
        do
        {
            selection = NumberGenUtil.randomGenerateSelection(NUM_OF_BALL, MAX_NUM);
            int powerBallSelection = randomGeneratePowerBall(powerBallAnalyseResult, id);
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

    private static Integer randomGeneratePowerBall(PowerBallAnalyseResult powerBallAnalyseResult)
    {
        int randomResult = random.nextInt(powerBallAnalyseResult.getSampleSize());
        for (Frequency frequency : powerBallAnalyseResult.getPowerBallfrequencies())
        {
            if (randomResult<frequency.getCount()) return frequency.getNum();
            randomResult -= frequency.getCount();
        }
        //would never happen
        return 0;
    }

    private static Integer randomGeneratePowerBall(PowerBallAnalyseResult powerBallAnalyseResult, Integer id)
    {
        int randomResult;
        do
        {
            randomResult = random.nextInt(MAX_POWER_BALL_NUM) + 1;
        }
        while (powerBallAnalyseResult.getPowerBallLastResultId(randomResult) + powerBallAnalyseResult.getPowerBallMinDistance(randomResult) > id);

        return randomResult;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PowerBallDraw draw = (PowerBallDraw) o;

        // Probably incorrect - comparing Object[] arrays with Arrays.equals
        if (!Arrays.equals(sortedNums, draw.sortedNums)) return false;
        return powerBall != null ? powerBall.equals(draw.powerBall) : draw.powerBall == null;

    }

    @Override
    public int hashCode()
    {
        int result = Arrays.hashCode(sortedNums);
        result = 31 * result + (powerBall != null ? powerBall.hashCode() : 0);
        return result;
    }
}
