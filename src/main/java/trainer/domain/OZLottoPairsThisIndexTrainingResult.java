package trainer.domain;

import domain.analyserresult.InOutPair;
import domain.draw.OZLottoDraw;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

/**
 * Created by Luke on 25/07/2017.
 */
public class OZLottoPairsThisIndexTrainingResult {
    Map<Integer, OZLottoPairsThatIndexTrainingResult> pairsMap;

    public OZLottoPairsThisIndexTrainingResult() {
        pairsMap = new HashMap<>();
        IntStream.range(0, OZLottoDraw.NUM_OF_BALL).forEach(i->pairsMap.put(i+1, new OZLottoPairsThatIndexTrainingResult()));
    }

    public void add(int thisIndexNum, int thatIndexNum, int distance, List<InOutPair> pairs) {
        pairsMap.get(thisIndexNum).add(thatIndexNum, distance, pairs);
    }

    public OZLottoPairsThatIndexTrainingResult get(int i) {
       return pairsMap.get(i);
    }
}
