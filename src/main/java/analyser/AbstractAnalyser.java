package analyser;

import domain.draw.Draw;
import domain.draw.PowerBallDraw;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: lzhou
 * Date: 24/10/2016
 * Time: 12:22 PM
 */
public class AbstractAnalyser<T extends Draw>
{
    protected List<T> results;

    public AbstractAnalyser(List<T> results)
    {
        this.results = results;
    }
}
