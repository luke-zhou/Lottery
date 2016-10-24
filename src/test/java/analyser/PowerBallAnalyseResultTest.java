package analyser;

import domain.analyserresult.PowerBallAnalyseResult;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Luke on 22/05/2016.
 */
public class PowerBallAnalyseResultTest
{
    PowerBallAnalyseResult powerBallAnalyseResult = new PowerBallAnalyseResult(20);

    @Test
    public void updatePowerBallMinDistancePattern() throws Exception
    {
        powerBallAnalyseResult.updatePowerBallMinDistancePattern(17, 900);

        assertTrue(powerBallAnalyseResult.getPowerBallLastResultId(17)==900);
        assertTrue(powerBallAnalyseResult.getPowerBallMinDistance(17)==Integer.MAX_VALUE);
    }

    @Test
    public void updatePowerBallMinDistancePattern_2() throws Exception
    {
        powerBallAnalyseResult.updatePowerBallMinDistancePattern(17, 900);
        powerBallAnalyseResult.updatePowerBallMinDistancePattern(2, 901);

        assertTrue(powerBallAnalyseResult.getPowerBallLastResultId(17)==900);
        assertTrue(powerBallAnalyseResult.getPowerBallLastResultId(2)==901);
        assertTrue(powerBallAnalyseResult.getPowerBallMinDistance(17)==Integer.MAX_VALUE);
        assertTrue(powerBallAnalyseResult.getPowerBallMinDistance(2)==Integer.MAX_VALUE);
    }

    @Test
    public void updatePowerBallMinDistancePattern_3() throws Exception
    {
        powerBallAnalyseResult.updatePowerBallMinDistancePattern(17, 900);
        powerBallAnalyseResult.updatePowerBallMinDistancePattern(2, 901);
        powerBallAnalyseResult.updatePowerBallMinDistancePattern(17, 1000);

        assertTrue(powerBallAnalyseResult.getPowerBallLastResultId(17)==1000);
        assertTrue(powerBallAnalyseResult.getPowerBallLastResultId(2)==901);
        assertTrue(powerBallAnalyseResult.getPowerBallMinDistance(17)==100);
        assertTrue(powerBallAnalyseResult.getPowerBallMinDistance(2)==Integer.MAX_VALUE);
    }

    @Test
    public void updatePowerBallMinDistancePattern_4() throws Exception
    {
        powerBallAnalyseResult.updatePowerBallMinDistancePattern(17, 900);
        powerBallAnalyseResult.updatePowerBallMinDistancePattern(2, 901);
        powerBallAnalyseResult.updatePowerBallMinDistancePattern(17, 1000);
        powerBallAnalyseResult.updatePowerBallMinDistancePattern(17, 1200);

        assertTrue(powerBallAnalyseResult.getPowerBallLastResultId(17)==1200);
        assertTrue(powerBallAnalyseResult.getPowerBallLastResultId(2)==901);
        assertTrue(powerBallAnalyseResult.getPowerBallMinDistance(17)==100);
        assertTrue(powerBallAnalyseResult.getPowerBallMinDistance(2)==Integer.MAX_VALUE);

        System.out.println(powerBallAnalyseResult);
    }
}