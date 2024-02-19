package edu.jsu.mcis.cs310.coursedb.dao;

import java.sql.*;
import com.github.cliftonlabs.json_simple.*;
import java.util.ArrayList;

public class DAOUtility {
    //In this class, you will find a term ID constant for Fall 2024 (used by the unit tests):
    public static final int TERMID_FA24 = 1;
    
    /*              As well as an incomplete method called "getResultSetAsJson()":
                    This method is intended to accept a ResultSet as an argument and return the data from the result set as 
                    a JSON array; this array should contain one JSON object for each record from the original result set (in 
                    the original order!), with the keys and values in each object matching the original result set column names 
                    and field values.  This function can be used by the JSON-returning methods in the section and registration DAOs,
                    to reduce duplicate JSON-related code. */
    
    public static String getResultSetAsJson(ResultSet rs) {
        
        JsonArray records = new JsonArray();
        
        try {
        
            if (rs != null) {
            //variable to hold our meta data:    
            ResultSetMetaData rsmd = rs.getMetaData();

                //store each row of information in our json array, by making each row information into json object
                
                while(rs.next()){
                    //JsonObject to hold our information: (which is to be added to the json array)
                    JsonObject informationToAdd = new JsonObject();
                  
                    for(int i = 1; i <= rsmd.getColumnCount(); ++i){
                        //Get column name:
                        String columnName = rsmd.getColumnName(i);
                        //Get column value: (and convert it to String)
                        Object fieldValues = rs.getObject(i).toString();
                        //Finally add our column name and value to our object: 
                        informationToAdd.put(columnName, fieldValues);
                    }
                    //Adding our information in JsonObject to our JsonArray (records)                          
                    records.add(informationToAdd);
                }
                
                
            }
            
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        
        return Jsoner.serialize(records);
        
    }
    
}
