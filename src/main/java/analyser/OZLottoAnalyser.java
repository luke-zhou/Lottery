package analyser;

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
public class OZLottoAnalyser extends AbstractAnalyser<OZLottoDraw>
{
    public OZLottoAnalyser(List<OZLottoDraw> results)
    {
        super(results);
    }

    public Map<Integer, OZLottoAnalyserResult> start()
    {
        Map<Integer, OZLottoAnalyserResult> analyseResultMap = new HashMap<>();

        results.stream().forEach(draw -> {
            OZLottoAnalyserResult ozLottoAnalyserResult = calculateAnalyseResult(draw);
            analyseResultMap.put(draw.getId(), ozLottoAnalyserResult);
        });

        return analyseResultMap;
    }

    private OZLottoAnalyserResult calculateAnalyseResult(OZLottoDraw draw)
    {
        OZLottoAnalyserResult ozLottoAnalyserResult = new OZLottoAnalyserResult(results.indexOf(draw) + 1);

        results.stream().filter(r -> r.getId() <= draw.getId()).forEach(r -> {
            ozLottoAnalyserResult.updateNumFrequency(r);

        });

        ozLottoAnalyserResult.finalize();

        return ozLottoAnalyserResult;
    }
}
