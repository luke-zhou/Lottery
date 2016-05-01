import trainer.PowerBallTrainer;

import java.io.File;
import java.net.URISyntaxException;

/**
 * Created by Luke on 1/05/2016.
 */
public class Main
{
    public static void main(String[] args) throws URISyntaxException
    {
        File powerBallResultCsv = new File( Main.class.getResource( "data/Powerball-1041.csv" ).toURI() );

        PowerBallTrainer trainer = new PowerBallTrainer(powerBallResultCsv);

        trainer.start();
    }
}
