package trainer;

import org.junit.Test;

import java.io.File;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * Created by Luke on 24/07/2017.
 */
public class OZLottoTrainerTest {
    OZLottoTrainer ozLottoTrainer = new OZLottoTrainer(new File(getClass().getResource("OzLotto.csv").getPath()));

    @Test
    public void generateNum() throws Exception {
        int[] index4NewNums = {37, 39, 30, 6, 24, 3, 45, 44, 4, 21, 29, 13, 18, 9, 11, 27, 16, 15, 5, 14, 32, 20, 42, 41, 22, 2, 26, 12, 33, 10, 8, 36, 23, 35, 1, 40, 43, 38, 25, 34, 28, 31, 19, 7, 17};
        String conditions = "a=-100, b=100, c=50, distance=100, thisNumIndex=4, thatNumIndex=5";
        ozLottoTrainer.generateNum(index4NewNums,conditions, 100);
    }
}