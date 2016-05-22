package domain.draw;

import domain.rule.Rule;
import org.junit.Before;
import org.junit.Test;
import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;

import static org.junit.Assert.*;

/**
 * Created by Luke on 1/05/2016.
 */
public class PowerBallDrawTest
{
    PowerBallDraw draw;

    @Before
    public void setUp() throws Exception
    {
        Integer[] nums =  {4,8,24,13,3,6};
        draw = new PowerBallDraw(nums, 17);

    }

    @Test
    public void hasNum() throws Exception
    {
       assertTrue(draw.hasNum(3));
       assertTrue(draw.hasNum(4));
       assertTrue(draw.hasNum(8));
       assertTrue(draw.hasNum(13));
       assertTrue(draw.hasNum(6));
       assertTrue(draw.hasNum(24));
       assertFalse(draw.hasNum(7));
    }

    @Test
    public void getNum() throws Exception
    {
        assertEquals(Integer.valueOf(4),draw.getNum(1));
        assertEquals(Integer.valueOf(8),draw.getNum(2));
        assertEquals(Integer.valueOf(24),draw.getNum(3));
        assertEquals(Integer.valueOf(13),draw.getNum(4));
        assertEquals(Integer.valueOf(3),draw.getNum(5));
        assertEquals(Integer.valueOf(6),draw.getNum(6));

    }

    @Test
    public void testToString() throws Exception
    {
       String result = draw.toString();

        String expected = "PowerBallDraw{nums=[3, 4, 6, 8, 13, 24], powerBall=17}";

        assertEquals(expected, result);
    }

    @Test
    public void testToStringUnsorted() throws Exception
    {
        String result = draw.toStringUnSorted();
        String expected = "PowerBallDraw{nums=[4, 8, 24, 13, 3, 6], powerBall=17}";

        assertEquals(expected, result);
    }

    @Test
    public void checkWin() throws Exception
    {
        Integer[] nums = {2,7,4,24,9,10};
        PowerBallDraw powerBallDraw = new PowerBallDraw(nums, 17);
        int division = draw.checkWin(powerBallDraw);

        assertTrue(1==division);

        Integer[] nums2 = {2,7,4,24,9,10};
        powerBallDraw = new PowerBallDraw(nums2, 9);
        division = draw.checkWin(powerBallDraw);

        assertTrue(0==division);

        Integer[] nums3 = {2,3,4,24,9,10};
        powerBallDraw = new PowerBallDraw(nums3, 17);
        division = draw.checkWin(powerBallDraw);

        assertTrue(3==division);

        Integer[] nums4 = {24,13,8,6,4,3};
        powerBallDraw = new PowerBallDraw(nums4, 17);
        division = draw.checkWin(powerBallDraw);

        assertTrue(500000==division);
    }

    @Test
    public void checkWinPowerHit() throws Exception
    {
        Integer[] nums = {2,7,4,24,9,10};
        PowerBallDraw powerBallDraw = new PowerBallDraw(nums, 0);
        int division = draw.checkWinPowerHit(powerBallDraw);

        assertTrue(1==division);

        Integer[] nums2 = {2,7,4,24,9,10};
        powerBallDraw = new PowerBallDraw(nums2, 0);
        division = draw.checkWinPowerHit(powerBallDraw);

        assertTrue(1==division);

        Integer[] nums3 = {2,3,4,24,9,10};
        powerBallDraw = new PowerBallDraw(nums3, 0);
        division = draw.checkWinPowerHit(powerBallDraw);

        assertTrue(3==division);

        Integer[] nums4 = {24,13,8,6,4,3};
        powerBallDraw = new PowerBallDraw(nums4, 0);
        division = draw.checkWinPowerHit(powerBallDraw);

        assertTrue(500000==division);
    }

    @Test
    public void generateDraw() throws Exception
    {
        System.out.println(PowerBallDraw.generateDraw(Rule.NO_RULE));

    }

    @Test
    public void generateDraw_with_1_7() throws Exception
    {
        Rule rule = new Rule(1);
        rule.getArguments().add(7);
        System.out.println(PowerBallDraw.generateDraw(rule));

    }


    @Test
    public void generateDraw_with_2_1() throws Exception
    {
        Rule rule = new Rule(2);
        rule.getArguments().add(1);
        System.out.println(PowerBallDraw.generateDraw(rule));

    }
}