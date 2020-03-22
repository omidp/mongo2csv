package com.omid.mongo2csv;

/**
 * @author omidp
 *
 */
public class Tuple2
{
    private int recordIndex;
    private String title;

    public Tuple2(int recordIndex, String title)
    {
        this.recordIndex = recordIndex;
        this.title = title;
    }

    public int getRecordIndex()
    {
        return recordIndex;
    }

    public void setRecordIndex(int recordIndex)
    {
        this.recordIndex = recordIndex;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + recordIndex;
        result = prime * result + ((title == null) ? 0 : title.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Tuple2 other = (Tuple2) obj;
        if (recordIndex != other.recordIndex)
            return false;
        if (title == null)
        {
            if (other.title != null)
                return false;
        }
        else if (!title.equals(other.title))
            return false;
        return true;
    }

}