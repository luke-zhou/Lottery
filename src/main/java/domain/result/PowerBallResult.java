package domain.result;

/**
 * Created with IntelliJ IDEA.
 * User: lzhou
 * Date: 5/02/2015
 * Time: 1:43 PM
 */
public class PowerBallResult
{
    Double division;
    Double differ;

    public PowerBallResult(Double division, Double differ)
    {
        this.division = division;
        this.differ = differ;
    }

    public Double getDivision()
    {
        return division;
    }

    public Double getDiffer()
    {
        return differ;
    }

    @Override
    public String toString()
    {
        return "PowerBallResult{" +
                "division=" + division +
                ", differ=" + differ +
                '}';
    }
}
