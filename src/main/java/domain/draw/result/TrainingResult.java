package domain.draw.result;

/**
 * Created with IntelliJ IDEA.
 * User: lzhou
 * Date: 2/05/2016
 * Time: 8:01 AM
 */
public class TrainingResult
{
    Long totalWin = 0L;
    Long totalDivision = 0L;
    Long totalTrainingSize = 0L;

    public void addResult(int division)
    {
        totalWin += division > 0 ? 1 : 0;
        totalDivision += division;
        totalTrainingSize++;
    }

    @Override
    public String toString()
    {
        return "TrainingResult{" +
                "totalWin=" + totalWin +
                ", totalDivision=" + totalDivision +
                ", totalTrainingSize=" + totalTrainingSize +
                '}';
    }

    public Long getTotalWin()
    {
        return totalWin;
    }

    public void setTotalWin(Long totalWin)
    {
        this.totalWin = totalWin;
    }

    public Long getTotalDivision()
    {
        return totalDivision;
    }

    public void setTotalDivision(Long totalDivision)
    {
        this.totalDivision = totalDivision;
    }

    public Long getTotalTrainingSize()
    {
        return totalTrainingSize;
    }

    public void setTotalTrainingSize(Long totalTrainingSize)
    {
        this.totalTrainingSize = totalTrainingSize;
    }
}
