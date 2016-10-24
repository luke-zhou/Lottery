package util;

import java.util.*;
import java.util.stream.IntStream;

/**
 * Created with IntelliJ IDEA.
 * User: lzhou
 * Date: 24/10/2016
 * Time: 8:04 AM
 */
public class NumberGenUtil
{
    protected static Random random = new Random(System.currentTimeMillis());

    public static Integer[] randomGenerateSelection(Integer count, Integer upperBound)
    {
        Set<Integer> selectionSet = new HashSet<>();

        while (selectionSet.size() < count)
        {
            selectionSet.add(random.nextInt(upperBound) + 1);
        }

        return selectionSet.toArray(new Integer[count]);
    }

    public static Integer randomGenerateNumber(Integer upperBound)
    {
        return random.nextInt(upperBound) + 1;
    }

    public static Integer[] randomGenerateSelection(Integer count, List<List<Integer>> potentialNumsGroup)
    {
        Set<Integer> selectionSet = new HashSet<>();

        while (selectionSet.size() < count)
        {
            List<Integer> potentialNums = new ArrayList<Integer>();
            IntStream.range(0, selectionSet.size() + 1).forEach(j -> potentialNums.addAll(potentialNumsGroup.get(j)));
            int num = random.nextInt(potentialNums.size());
            selectionSet.add(potentialNums.get(num));
        }

        return selectionSet.toArray(new Integer[count]);
    }


}
