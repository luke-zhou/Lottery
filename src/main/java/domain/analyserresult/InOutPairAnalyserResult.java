package domain.analyserresult;

import domain.draw.OZLottoDraw;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Created by lukezhou on 24/7/17.
 */
public class InOutPairAnalyserResult {

    List<InOutPair> inOutPairs = new ArrayList<>();

    Map<Integer, List<InOutPair>> inOutPairsMap = new HashMap<>();

    public InOutPairAnalyserResult() {
        IntStream.range(0, OZLottoDraw.NUM_OF_BALL).forEach(thisIndex -> inOutPairsMap.put(thisIndex + 1, new ArrayList<>()));
    }

    public void add(InOutPair inOutPair) {
        List<InOutPair> inOutPairsForThisIndex = inOutPairsMap.get(inOutPair.getThisIndexNum());
        if (inOutPairsForThisIndex.contains(inOutPair)) {
            inOutPairsForThisIndex.get(inOutPairsForThisIndex.indexOf(inOutPair)).increase();
        } else {
            inOutPairsForThisIndex.add(inOutPair);
        }
    }

    public List<InOutPair> getInOutPairs() {
        inOutPairs.clear();
        inOutPairsMap.forEach((k, v) -> inOutPairs.addAll(v));
        return inOutPairs;
    }
}
