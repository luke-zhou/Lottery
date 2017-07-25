package analyser;

import domain.analyserresult.InOutPair;
import domain.analyserresult.InOutPairAnalyserResult;
import domain.analyserresult.OZLottoAnalyserResult;
import domain.analyserresult.PowerBallAnalyseResult;
import domain.draw.OZLottoDraw;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: lzhou
 * Date: 24/10/2016
 * Time: 12:22 PM
 */
public class OZLottoAnalyser extends AbstractAnalyser<OZLottoDraw> {
    private InOutPairAnalyserResult inOutPairAnalyserResult;

    public OZLottoAnalyser(List<OZLottoDraw> results) {
        super(results);
    }

    public Map<Integer, OZLottoAnalyserResult> start() {
        Map<Integer, OZLottoAnalyserResult> analyseResultMap = new HashMap<>();

        results.stream().forEach(draw -> {
            OZLottoAnalyserResult ozLottoAnalyserResult = calculateAnalyseResult(draw);
            analyseResultMap.put(draw.getId(), ozLottoAnalyserResult);
        });

        inOutPairAnalyserResult = new InOutPairAnalyserResult();
        for (int i = results.size() - 200; i < results.size(); i++) {
//            System.out.println("i:"+i);
            for (int thisIndexNum = 1; thisIndexNum <= OZLottoDraw.NUM_OF_BALL; thisIndexNum++) {
                for (int thatIndexNum = 1; thatIndexNum <= OZLottoDraw.NUM_OF_BALL; thatIndexNum++) {
                    for (int distance = 1; distance <= 100; distance++) {
                        try {
                            int outputNum = results.get(i).getNum(thisIndexNum);
                            int inputNum = results.get(i - distance).getNum(thatIndexNum);
                            InOutPair inOutPair = new InOutPair(inputNum, outputNum, distance, thisIndexNum, thatIndexNum);
                            inOutPairAnalyserResult.add(inOutPair);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }

        return analyseResultMap;
    }

    private OZLottoAnalyserResult calculateAnalyseResult(OZLottoDraw draw) {
        OZLottoAnalyserResult ozLottoAnalyserResult = new OZLottoAnalyserResult(results.indexOf(draw) + 1);

        results.stream().filter(r -> r.getId() <= draw.getId()).forEach(r -> {
            ozLottoAnalyserResult.updateNumFrequency(r);

        });

        ozLottoAnalyserResult.finalize();

        return ozLottoAnalyserResult;
    }

    public InOutPairAnalyserResult getInOutPairAnalyserResult() {
        return inOutPairAnalyserResult;
    }
}
