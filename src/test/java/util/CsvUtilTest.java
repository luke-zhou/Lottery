package util;

import domain.draw.Draw;
import domain.draw.PowerBallDraw;
import org.junit.Test;

import java.io.File;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by Luke on 16/05/2016.
 */
public class CsvUtilTest
{
    File powerBallResultCsv = new File(getClass().getResource("Powerball.csv").getPath());

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

    @Test
    public void loadPowerData() throws Exception
    {
        List<PowerBallDraw> result = CsvUtil.loadPowerData(powerBallResultCsv);
        assertTrue(result.size() == 8);
//        result.stream().forEach(System.out::println);
        assertEquals(powerBallDraw1, result.get(0));
        assertEquals(powerBallDraw2, result.get(1));
        assertEquals(powerBallDraw3, result.get(2));
        assertEquals(powerBallDraw4, result.get(3));
        assertEquals(powerBallDraw5, result.get(4));
        assertEquals(powerBallDraw6, result.get(5));
        assertEquals(powerBallDraw7, result.get(6));
        assertEquals(powerBallDraw8, result.get(7));

        assertTrue(result.get(0).getId().equals(877));

    }
}