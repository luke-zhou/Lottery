package domain.rule;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: lzhou
 * Date: 3/05/2016
 * Time: 10:31 AM
 */
public class Rule
{
    public static Rule NO_RULE;

    static
    {
        NO_RULE = new Rule();
        NO_RULE.setInvolvedNumberCount(0);
    }

    String description;
    Integer involvedNumberCount;
    List<Integer> arguments = new ArrayList<>();


    public String getDescription()
    {
        return description;
    }

    public Integer getInvolvedNumberCount()
    {
        return involvedNumberCount;
    }

    public List<Integer> getArguments()
    {
        return arguments;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public void setInvolvedNumberCount(Integer involvedNumberCount)
    {
        this.involvedNumberCount = involvedNumberCount;
    }

    public void setArguments(List<Integer> arguments)
    {
        this.arguments = arguments;
    }
}
