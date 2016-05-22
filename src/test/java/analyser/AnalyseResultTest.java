package analyser;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Luke on 22/05/2016.
 */
public class AnalyseResultTest
{
    AnalyseResult analyseResult = new AnalyseResult(20);

    @Test
    public void updatePowerBallMinDistancePattern() throws Exception
    {
        analyseResult.updatePowerBallMinDistancePattern(17, 900);

        assertTrue(analyseResult.getPowerBallLastResultId(17)==900);
        assertTrue(analyseResult.getPowerBallMinDistance(17)==Integer.MAX_VALUE);
    }

    @Test
    public void updatePowerBallMinDistancePattern_2() throws Exception
    {
        analyseResult.updatePowerBallMinDistancePattern(17, 900);
        analyseResult.updatePowerBallMinDistancePattern(2, 901);

        assertTrue(analyseResult.getPowerBallLastResultId(17)==900);
        assertTrue(analyseResult.getPowerBallLastResultId(2)==901);
        assertTrue(analyseResult.getPowerBallMinDistance(17)==Integer.MAX_VALUE);
        assertTrue(analyseResult.getPowerBallMinDistance(2)==Integer.MAX_VALUE);
    }

    @Test
    public void updatePowerBallMinDistancePattern_3() throws Exception
    {
        analyseResult.updatePowerBallMinDistancePattern(17, 900);
        analyseResult.updatePowerBallMinDistancePattern(2, 901);
        analyseResult.updatePowerBallMinDistancePattern(17, 1000);

        assertTrue(analyseResult.getPowerBallLastResultId(17)==1000);
        assertTrue(analyseResult.getPowerBallLastResultId(2)==901);
        assertTrue(analyseResult.getPowerBallMinDistance(17)==100);
        assertTrue(analyseResult.getPowerBallMinDistance(2)==Integer.MAX_VALUE);
    }

    @Test
    public void updatePowerBallMinDistancePattern_4() throws Exception
    {
        analyseResult.updatePowerBallMinDistancePattern(17, 900);
        analyseResult.updatePowerBallMinDistancePattern(2, 901);
        analyseResult.updatePowerBallMinDistancePattern(17, 1000);
        analyseResult.updatePowerBallMinDistancePattern(17, 1200);

        assertTrue(analyseResult.getPowerBallLastResultId(17)==1200);
        assertTrue(analyseResult.getPowerBallLastResultId(2)==901);
        assertTrue(analyseResult.getPowerBallMinDistance(17)==100);
        assertTrue(analyseResult.getPowerBallMinDistance(2)==Integer.MAX_VALUE);

        System.out.println(analyseResult);
    }
}