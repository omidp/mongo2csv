package com.omid.mongo2csv;

/**
 * @author omidp
 *
 */
public class ProcessResult
{

    private String titles;
    private String values;

    public ProcessResult(String titles, String values)
    {
        this.titles = titles;
        this.values = values;
    }

    public String getTitles()
    {
        return titles;
    }

    public String getValues()
    {
        return values;
    }

}
