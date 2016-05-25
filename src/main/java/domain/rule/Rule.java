package domain.rule;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: lzhou
 * Date: 3/05/2016
 * Time: 10:31 AM
 */
//0
//1:a=1
//2:a=b+1
public class Rule
{
    public static Rule NO_RULE = new Rule(0);

    String description;
    Integer involvedNumberCount;
    List<Integer> arguments = new ArrayList<>();


    public Rule(Integer involvedNumberCount)
    {
        this.involvedNumberCount = involvedNumberCount;
    }

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

    public void addArguments(Integer arg)
    {
        arguments.add(arg);
    }
}
