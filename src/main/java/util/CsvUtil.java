package util;

import com.opencsv.CSVReader;
import domain.draw.Draw;
import domain.draw.OZLottoDraw;
import domain.draw.PowerBallDraw;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Created with IntelliJ IDEA.
 * User: Luke
 * Date: 28/02/2014
 * Time: 9:34 PM
 * To change this template use File | Settings | File Templates.
 */
public class CsvUtil
{
    public static <T extends Draw> List<T> loadData(File file, Function<String[], T> function)
    {
        List<T> draws = new ArrayList<>();
        try
        {
            CSVReader reader = new CSVReader(new FileReader(file));
            String[] nextLine;
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            reader.readNext();
            while ((nextLine = reader.readNext()) != null)
            {
                String[] lineContent = nextLine;
                int id = Integer.valueOf(lineContent[0]);
                Date date = sdf.parse(lineContent[1]);
                T draw = function.apply(lineContent);
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
