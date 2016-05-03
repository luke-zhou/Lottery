package trainer;

import domain.result.TrainingResult;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Luke on 2/05/2016.
 */
public class PowerBallTrainerTest
{
    PowerBallTrainer trainer = new PowerBallTrainer(new File(""));

    TrainingResult trainingResult1 = new TrainingResult();
    TrainingResult trainingResult2 = new TrainingResult();
    TrainingResult trainingResult3 = new TrainingResult();
    TrainingResult trainingResult4 = new TrainingResult();
    TrainingResult trainingResult5 = new TrainingResult();
    TrainingResult trainingResult6 = new TrainingResult();
    TrainingResult trainingResult7 = new TrainingResult();
    TrainingResult trainingResult8 = new TrainingResult();
    TrainingResult trainingResult9 = new TrainingResult();
    TrainingResult trainingResult10 = new TrainingResult();

    List<TrainingResult> trainingResults = new ArrayList<>();


    public PowerBallTrainerTest() throws URISyntaxException
    {
    }

    @Before
    public void setUp() throws Exception
    {
        trainingResult1.setTotalWin(11L);
        trainingResult2.setTotalWin(22L);
        trainingResult3.setTotalWin(33L);
        trainingResult4.setTotalWin(44L);
        trainingResult5.setTotalWin(55L);
        trainingResult6.setTotalWin(66L);
        trainingResult7.setTotalWin(77L);
        trainingResult8.setTotalWin(88L);
        trainingResult9.setTotalWin(99L);
        trainingResult10.setTotalWin(101L);

        trainingResult1.setTotalDivision(111l);
        trainingResult2.setTotalDivision(222l);
        trainingResult3.setTotalDivision(333l);
        trainingResult4.setTotalDivision(444l);
        trainingResult5.setTotalDivision(555l);
        trainingResult6.setTotalDivision(666l);
        trainingResult7.setTotalDivision(777L);
        trainingResult8.setTotalDivision(888l);
        trainingResult9.setTotalDivision(999l);
        trainingResult10.setTotalDivision(1010l);

        trainingResult1.setTotalTrainingSize(100L);
        trainingResult2.setTotalTrainingSize(100L);
        trainingResult3.setTotalTrainingSize(100L);
        trainingResult4.setTotalTrainingSize(100L);
        trainingResult5.setTotalTrainingSize(100L);
        trainingResult6.setTotalTrainingSize(100L);
        trainingResult7.setTotalTrainingSize(100L);
        trainingResult8.setTotalTrainingSize(100L);
        trainingResult9.setTotalTrainingSize(100L);
        trainingResult10.setTotalTrainingSize(100L);

        trainingResults.add(trainingResult1);
        trainingResults.add(trainingResult2);
        trainingResults.add(trainingResult3);
        trainingResults.add(trainingResult4);
        trainingResults.add(trainingResult5);
        trainingResults.add(trainingResult6);
        trainingResults.add(trainingResult7);
        trainingResults.add(trainingResult8);
        trainingResults.add(trainingResult9);
        trainingResults.add(trainingResult10);

    }

    @Test
    public void getAverageTrainingResult() throws Exception
    {
        Method method = trainer.getClass().getDeclaredMethod("getAverageTrainingResult", List.class);
        method.setAccessible(true);
        TrainingResult result = (TrainingResult) method.invoke(trainer, trainingResults);

        System.out.println(result);

    }
}