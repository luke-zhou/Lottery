package domain.analyserresult;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lukezhou on 24/7/17.
 */
public class InOutPairAnalyserResult {

    List<InOutPair> inOutPairs = new ArrayList<>();

    public void add(InOutPair inOutPair) {
        if (inOutPairs.contains(inOutPair)) {
            inOutPairs.get(inOutPairs.indexOf(inOutPair)).increase();
        }
        else{
            inOutPairs.add(inOutPair);
        }
    }

    public List<InOutPair> getInOutPairs() {
        return inOutPairs;
    }
}
