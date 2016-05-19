package analyser;

import domain.draw.PowerBallDraw;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * Created with IntelliJ IDEA.
 * User: LZhou
 * Date: 17/05/2016
 * Time: 10:21 AM
 */
public class PowerBallAnalyserTest
{
    PowerBallAnalyser powerBallAnalyser;

    Integer[] selection1 = {8, 16, 26, 29, 19, 25};
    Integer[] selection2 = {37, 5, 35, 30, 9, 23};
    Integer[] selection3 = {22, 3, 26, 38, 34, 32};
    Integer[] selection4 = {28, 5, 26, 27, 2, 21};
    Integer[] selection5 = {34, 39, 21, 38, 35, 7};
    Integer[] selection6 = {28, 35, 24, 40, 4, 7};
    Integer[] selection7 = {32, 29, 25, 33, 34, 20};
    Integer[] selection8 = {26, 13, 22, 18, 4, 17};

    PowerBallDraw powerBallDraw1 = new PowerBallDraw(selection1, 5);
    PowerBallDraw powerBallDraw2 = new PowerBallDraw(selection2, 18);
    PowerBallDraw powerBallDraw3 = new PowerBallDraw(selection3, 18);
    PowerBallDraw powerBallDraw4 = new PowerBallDraw(selection4, 19);
    PowerBallDraw powerBallDraw5 = new PowerBallDraw(selection5, 5);
    PowerBallDraw powerBallDraw6 = new PowerBallDraw(selection6, 12);
    PowerBallDraw powerBallDraw7 = new PowerBallDraw(selection7, 2);
    PowerBallDraw powerBallDraw8 = new PowerBallDraw(selection8, 20);

    List<PowerBallDraw> powerBallDraws = new ArrayList<>();

    @Before
    public void setUp() throws Exception
    {
        powerBallDraws.add(powerBallDraw1);
        powerBallDraws.add(powerBallDraw2);
        powerBallDraws.add(powerBallDraw3);
        powerBallDraws.add(powerBallDraw4);
        powerBallDraws.add(powerBallDraw5);
        powerBallDraws.add(powerBallDraw6);
        powerBallDraws.add(powerBallDraw7);
        powerBallDraws.add(powerBallDraw8);
        powerBallDraw1.setId(877);
        powerBallDraw2.setId(878);
        powerBallDraw3.setId(879);
        powerBallDraw4.setId(880);
        powerBallDraw5.setId(881);
        powerBallDraw6.setId(882);
        powerBallDraw7.setId(883);
        powerBallDraw8.setId(884);

        powerBallAnalyser = new PowerBallAnalyser(powerBallDraws);
    }

    @Test
    public void start() throws Exception
    {
        Map<Integer, AnalyseResult> result = powerBallAnalyser.start();

//        result.entrySet().forEach(System.out::println);

        assertTrue(result.get(877).getSampleSize() == 1);
        assertTrue(result.get(878).getSampleSize() == 2);
        assertTrue(result.get(879).getSampleSize() == 3);
        assertTrue(result.get(880).getSampleSize() == 4);
        assertTrue(result.get(881).getSampleSize() == 5);
        assertTrue(result.get(882).getSampleSize() == 6);
        assertTrue(result.get(883).getSampleSize() == 7);
        assertTrue(result.get(884).getSampleSize() == 8);
        assertTrue(result.get(877).getNumFrequency(8) == 1);
        assertTrue(result.get(877).getNumFrequency(19) == 1);
        assertTrue(result.get(878).getNumFrequency(26) == 1);
        assertTrue(result.get(878).getNumFrequency(19) == 1);
        assertTrue(result.get(878).getNumFrequency(8) == 1);
        assertTrue(result.get(879).getNumFrequency(26) == 2);
        assertTrue(result.get(879).getNumFrequency(19) == 1);
        assertTrue(result.get(879).getNumFrequency(8) == 1);
        assertTrue(result.get(880).getNumFrequency(26) == 3);
        assertTrue(result.get(880).getNumFrequency(5) == 2);
        assertTrue(result.get(880).getNumFrequency(23) == 1);
        assertTrue(result.get(881).getNumFrequency(34) == 2);
        assertTrue(result.get(881).getNumFrequency(2) == 1);
        assertTrue(result.get(881).getNumFrequency(26) == 3);
        assertTrue(result.get(882).getNumFrequency(7) == 2);
        assertTrue(result.get(882).getNumFrequency(26) == 3);
        assertTrue(result.get(882).getNumFrequency(29) == 1);
        assertTrue(result.get(883).getNumFrequency(7) == 2);
        assertTrue(result.get(883).getNumFrequency(26) == 3);
        assertTrue(result.get(883).getNumFrequency(27) == 1);
        assertTrue(result.get(884).getNumFrequency(7) == 2);
        assertTrue(result.get(884).getNumFrequency(26) == 4);
        assertTrue(result.get(884).getNumFrequency(27) == 1);

        assertTrue(result.get(877).getPowerBallFrequency(5) == 1);
        assertTrue(result.get(878).getPowerBallFrequency(18) == 1);
        assertTrue(result.get(878).getPowerBallFrequency(5) == 1);
        assertTrue(result.get(879).getPowerBallFrequency(5) == 1);
        assertTrue(result.get(879).getPowerBallFrequency(18) == 2);
        assertTrue(result.get(880).getPowerBallFrequency(18) == 2);
        assertTrue(result.get(880).getPowerBallFrequency(19) == 1);
        assertTrue(result.get(880).getPowerBallFrequency(5) == 1);
        assertTrue(result.get(881).getPowerBallFrequency(18) == 2);
        assertTrue(result.get(881).getPowerBallFrequency(19) == 1);
        assertTrue(result.get(881).getPowerBallFrequency(5) == 2);
        assertTrue(result.get(882).getPowerBallFrequency(18) == 2);
        assertTrue(result.get(882).getPowerBallFrequency(19) == 1);
        assertTrue(result.get(882).getPowerBallFrequency(5) == 2);
        assertTrue(result.get(882).getPowerBallFrequency(12) == 1);
        assertTrue(result.get(883).getPowerBallFrequency(18) == 2);
        assertTrue(result.get(883).getPowerBallFrequency(19) == 1);
        assertTrue(result.get(883).getPowerBallFrequency(5) == 2);
        assertTrue(result.get(883).getPowerBallFrequency(2) == 1);
        assertTrue(result.get(884).getPowerBallFrequency(18) == 2);
        assertTrue(result.get(884).getPowerBallFrequency(19) == 1);
        assertTrue(result.get(884).getPowerBallFrequency(5) == 2);
        assertTrue(result.get(884).getPowerBallFrequency(20) == 1);

        assertTrue(result.get(877).getPowerBallLastResultId(5)==877);
        assertTrue(result.get(878).getPowerBallLastResultId(5)==877);
        assertTrue(result.get(878).getPowerBallLastResultId(18)==878);
        assertTrue(result.get(879).getPowerBallLastResultId(5)==877);
        assertTrue(result.get(879).getPowerBallLastResultId(18)==879);
        assertTrue(result.get(880).getPowerBallLastResultId(19)==880);
        assertTrue(result.get(880).getPowerBallLastResultId(18)==879);
        assertTrue(result.get(881).getPowerBallLastResultId(19)==880);
        assertTrue(result.get(881).getPowerBallLastResultId(5)==881);
        assertTrue(result.get(882).getPowerBallLastResultId(19)==880);
        assertTrue(result.get(882).getPowerBallLastResultId(5)==881);
    }
}