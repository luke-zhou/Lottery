package util;

import com.opencsv.CSVReader;
import domain.draw.PowerBallDraw;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Luke
 * Date: 28/02/2014
 * Time: 9:34 PM
 * To change this template use File | Settings | File Templates.
 */
public class CsvUtil
{
    public static List<PowerBallDraw> loadPowerData(File file)
    {
        List<PowerBallDraw> draws = new ArrayList<>();
        try
        {
            CSVReader reader = new CSVReader(new FileReader(file));
            String [] nextLine;
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            reader.readNext();
            while ((nextLine = reader.readNext()) != null)
            {

                    int id = Integer.valueOf(nextLine[0]);
                    Date date = sdf.parse(nextLine[1]);
                    Integer[] nums = {Integer.valueOf(nextLine[2]),
                            Integer.valueOf(nextLine[3]),
                            Integer.valueOf(nextLine[4]),
                            Integer.valueOf(nextLine[5]),
                            Integer.valueOf(nextLine[6]),
                            Integer.valueOf(nextLine[7])};

                    int powerBall = Integer.valueOf(nextLine[8]);
                    PowerBallDraw draw = new PowerBallDraw(nums, powerBall);
                    draw.setId(id);
                    draw.setDate(date);
                draws.add(draw);
            }
        } catch (FileNotFoundException e)
        {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (IOException e)
        {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (ParseException e)
        {
            e.printStackTrace();
        } catch (Exception e)
        {
            e.printStackTrace();
        }

        return draws;
    }
}
