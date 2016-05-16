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

    @Test
    public void loadPowerData() throws Exception
    {
        List<PowerBallDraw> result = CsvUtil.loadPowerData(powerBallResultCsv);
        assertTrue(result.size() == 8);
        result.stream().forEach(System.out::println);
    }
}