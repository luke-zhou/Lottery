package domain.result;

/**
 * Created by lukezhou on 12/7/17.
 */
public class SeparateNumTrainingResult {
    Integer totalMatch = 0;
    Integer totalDiff = 0;
    Integer totalSize =0;
    Integer a;
    Integer b;
    Integer distance;
    Integer numIndex;

    public void accumulateMatch(boolean isMatch) {
        totalMatch += isMatch ? 1 : 0;
    }

    public void accumulateDiff(int i) {
        totalDiff += i;
    }

    public void accumulateSize() {
        totalSize++;
    }

    public Integer getTotalMatch() {
        return totalMatch;
    }

    public Integer getTotalDiff() {
        return totalDiff;
    }

    public Integer getTotalSize() {
        return totalSize;
    }

    public void setA(Integer a) {
        this.a = a;
    }

    public void setB(Integer b) {
        this.b = b;
    }

    public void setDistance(Integer distance) {
        this.distance = distance;
    }

    public void setNumIndex(Integer numIndex) {
        this.numIndex = numIndex;
    }


    @Override
    public String toString() {
        return "SeparateNumTrainingResult{" +
                "totalMatch=" + totalMatch +
                ", totalDiff=" + totalDiff +
                ", totalSize=" + totalSize +
                ", a=" + a +
                ", b=" + b +
                ", distance=" + distance +
                ", numIndex=" + numIndex +
                '}';
    }
}
