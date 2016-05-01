package util;

/**
 * Created with IntelliJ IDEA.
 * User: lzhou
 * Date: 26/08/2015
 * Time: 9:48 AM
 */
public class LogUtil
{
    public static void consoleLog(String content)
    {
        System.out.println(content);
    }
    public static void consoleLog(int content)
    {
        consoleLog(String.valueOf(content));
    }
}
