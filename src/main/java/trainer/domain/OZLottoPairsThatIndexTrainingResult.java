package trainer.domain;

import domain.analyserresult.InOutPair;
import domain.draw.OZLottoDraw;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.IntStream;

/**
 * Created by Luke on 25/07/2017.
 */
public class OZLottoPairsThatIndexTrainingResult {
    Map<Integer, OZLottoPairsDistanceTrainingResult> pairsMap;

    public OZLottoPairsThatIndexTrainingResult() {
        pairsMap = new HashMap<>();
    }

    public void add(int thatIndexNum, int distance, List<InOutPair> pairs) {
        if (pairsMap.containsKey(thatIndexNum)){
            pairsMap.get(thatIndexNum).add(distance, pairs);
        }
        else{
            OZLottoPairsDistanceTrainingResult ozLottoPairsDistanceTrainingResult = new OZLottoPairsDistanceTrainingResult();
            ozLottoPairsDistanceTrainingResult.add(distance, pairs);
            pairsMap.put(thatIndexNum, ozLottoPairsDistanceTrainingResult);
        }
    }

    public Map<Integer, OZLottoPairsDistanceTrainingResult> getMap(){
        return pairsMap;
    }
}
