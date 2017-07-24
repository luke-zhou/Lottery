package domain.analyserresult;

/**
 * Created by lukezhou on 24/7/17.
 */
public class InOutPair {
    int in;
    int out;
    int distance;
    int thisIndexNum;
    int thatIndexNum;
    int count;

    public InOutPair(int in, int out, int distance, int thisIndexNum, int thatIndexNum) {
        int factor = gcdr(in, out);
        this.in = in / factor;
        this.out = out / factor;
        this.distance = distance;
        this.thisIndexNum = thisIndexNum;
        this.thatIndexNum = thatIndexNum;
        count = 1;

    }

    private int gcdr ( int a, int b )
    {
        if ( a==0 ) return b;
        return gcdr ( b%a, a );
    }

    public void increase(){
        count++;
    }

    public int getIn() {
        return in;
    }

    public int getOut() {
        return out;
    }

    public int getDistance() {
        return distance;
    }

    public int getThisIndexNum() {
        return thisIndexNum;
    }

    public int getThatIndexNum() {
        return thatIndexNum;
    }

    public int getCount() {
        return count;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        InOutPair inOutPair = (InOutPair) o;

        if (in != inOutPair.in) return false;
        if (out != inOutPair.out) return false;
        if (distance != inOutPair.distance) return false;
        if (thisIndexNum != inOutPair.thisIndexNum) return false;
        return thatIndexNum == inOutPair.thatIndexNum;
    }

    @Override
    public int hashCode() {
        int result = in;
        result = 31 * result + out;
        result = 31 * result + distance;
        result = 31 * result + thisIndexNum;
        result = 31 * result + thatIndexNum;
        return result;
    }
}
