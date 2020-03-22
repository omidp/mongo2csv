package com.omid.mongo2csv;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

/**
 * @author omidp
 *
 */
public class Mongo2Csv
{

    Map<Tuple2, Object> valueMap = new HashMap<>();

    Set<String> titleSet = new LinkedHashSet<>();

    public ProcessResult processResult(DBCursor cursor)
    {
        int recordCount = 0;
        try
        {
            while (cursor.hasNext())
            {
                DBObject next = cursor.next();
                processRow(next, recordCount);
                recordCount++;
            }
        }
        finally
        {
            cursor.close();
        }
        //
        StringBuilder titleCsv = new StringBuilder();
        StringBuilder valueCsv = new StringBuilder();
        for (int i = 0; i < recordCount; i++)
        {
            for (String title : titleSet)
            {
                if (i == 0)
                    titleCsv.append(title).append(",");
                Object val = valueMap.get(new Tuple2(i, title));
                valueCsv.append(val == null ? "" : val).append(",");
            }
            valueCsv.append("\r\n");
        }
        return new ProcessResult(titleCsv.toString(), valueCsv.toString());
    }

    private void processRow(DBObject next, int recordCount)
    {
        processMap(next.toMap(), "", recordCount);
    }

    private class DefaultValueFieldHandler implements FieldValueHandler
    {

        @Override
        public void process(String key, Object value, int recordCount)
        {
            titleSet.add(key);
            if (value == null)
                value = "";
            valueMap.put(new Tuple2(recordCount, key), value);
        }

    }

    private class BasicObjectFieldHandler implements FieldValueHandler
    {

        @Override
        public void process(String key, Object value, int recordCount)
        {
            BasicDBObject bdo = (BasicDBObject) value;
            Map<String, Object> m = bdo.toMap();
            processMap(m, key + ".", recordCount);
        }

    }

    private class ListObjectFieldHandler implements FieldValueHandler
    {

        @Override
        public void process(String key, Object value, int recordCount)
        {
            BasicDBList bdo = (BasicDBList) value;
            int index = 0;
            for (Object object : bdo)
            {
                if (BasicDBObject.class.isAssignableFrom(object.getClass()))
                {
                    BasicDBObject bd = (BasicDBObject) object;
                    Map<String, Object> m = bd.toMap();
                    processMap(m, key + index + ".", recordCount);
                }
                else
                {
                    throw new UnsupportedOperationException("not implemented yet");
                }
                index++;
            }
        }

    }

    public interface FieldValueHandler
    {
        public void process(String key, Object value, int recordCount);
    }

    private FieldValueHandler getHandler(Object fieldValue)
    {
        if (fieldValue == null)
            return new DefaultValueFieldHandler();
        Class<? extends Object> clz = fieldValue.getClass();
        if (clz.isAssignableFrom(BasicDBObject.class))
            return new BasicObjectFieldHandler();
        else if (clz.isAssignableFrom(BasicDBList.class))
            return new ListObjectFieldHandler();
        else
            return new DefaultValueFieldHandler();
    }

    private void processMap(Map<String, Object> map, String key, int recordCount)
    {
        for (Map.Entry<String, Object> field : map.entrySet())
        {
            getHandler(field.getValue()).process(key + field.getKey(), field.getValue(), recordCount);
        }
    }

}
