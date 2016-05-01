package domain.draw;

import java.util.Date;

/**
 * Created by Luke on 13/05/2014.
 */
public abstract class Draw
{
    private Integer id;
    private Date date;

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public Date getDate()
    {
        return date;
    }

    public void setDate(Date date)
    {
        this.date = date;
    }

    public abstract Integer getNum(int index) throws Exception;
    public abstract Boolean hasNum(int num);
}
