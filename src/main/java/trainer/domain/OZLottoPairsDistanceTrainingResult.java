package trainer.domain;

import domain.analyserresult.InOutPair;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Luke on 25/07/2017.
 */
public class OZLottoPairsDistanceTrainingResult {
    Map<Integer, List<InOutPair>> pairsMap;

    public OZLottoPairsDistanceTrainingResult() {
        pairsMap = new HashMap<>();
    }

    public void add(int distance, List<InOutPair> pairs) {
        pairsMap.put(distance, pairs);
    }

    public Map<Integer, List<InOutPair>> getMap(){
        return pairsMap;
    }
}
