package domain.analyserresult;

import domain.draw.OZLottoDraw;

/**
 * Created with IntelliJ IDEA.
 * User: lzhou
 * Date: 24/10/2016
 * Time: 12:54 PM
 */
public class OZLottoAnalyserResult extends AbstractAnalyserResult
{
    public OZLottoAnalyserResult(Integer sampleSize)
    {
        super(sampleSize);
    }

    @Override
    protected int getNumOfBall()
    {
        return OZLottoDraw.NUM_OF_BALL;
    }
}
