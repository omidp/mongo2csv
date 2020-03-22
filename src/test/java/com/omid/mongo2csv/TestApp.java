package com.omid.mongo2csv;

import org.junit.Ignore;
import org.junit.Test;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.MongoClient;

public class TestApp
{

    
    @Test
    @Ignore
    public void test()
    {
        MongoClient mongoClient = new MongoClient("localhost", 27017);
        DB database = mongoClient.getDB("mehdi");

        DBCollection collection = database.getCollection("omid");
        DBCursor cursor = collection.find();
        //
        Mongo2Csv mcsv = new Mongo2Csv();
        ProcessResult processResult = mcsv.processResult(cursor);
        System.out.println(processResult.getTitles());
        System.out.println(processResult.getValues());

    }
    
}
