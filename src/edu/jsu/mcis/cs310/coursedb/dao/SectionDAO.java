package edu.jsu.mcis.cs310.coursedb.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;


public class SectionDAO {
    
    private static final String QUERY_FIND = "SELECT * FROM section WHERE termid = ? AND subjectid = ? AND num = ? ORDER BY crn";
    
    private final DAOFactory daoFactory;
    
    SectionDAO(DAOFactory daoFactory) {
        this.daoFactory = daoFactory;
    }
   

    
    public String find(int termid, String subjectid, String num) {
        
        String result = "[]";
        
        PreparedStatement ps = null;
        ResultSet rs = null;
        ResultSetMetaData rsmd = null;
        
        try {
            
            Connection conn = daoFactory.getConnection();
            
            if (conn.isValid(0)) {
                
                ps = conn.prepareStatement(QUERY_FIND);
                //set int in the first column with term id:
                ps.setInt(1, termid);
                // set String for second column with subject id:
                ps.setString(2, subjectid);
                //set String for third column with num:
                ps.setString(3, num);
                
                rs = ps.executeQuery();
                //just converting our results to Json
                result = DAOUtility.getResultSetAsJson(rs);
                
                /* Search the schedule (with the "find()" method of the SectionDAO class) for sections 
                  matching the specified subject ID, course number, and term ID.  Your method should 
                  accept these criteria as arguments; the term ID should be an integer, and the subject ID 
                  and course number should be strings.  Your database query should match these values to the
                  "subjectid" and "num" fields in the "section" table.  The query results, 
                  containing a list of sections that match the specified criteria, should include all available
                  information for the matching sections, and it should be returned by the method as a JSON array
                  (serialized to a string).  The array should contain JSON objects, one for every matching section,
                  containing the data for each section; these objects should use key names corresponding to the original 
                  field names in the database, and should be ordered by CRN number.
                  */
            }
            return result;
        }
        
        catch (Exception e) { e.printStackTrace(); }
        
        finally {
            
            if (rs != null) { try { rs.close(); } catch (Exception e) { e.printStackTrace(); } }
            if (ps != null) { try { ps.close(); } catch (Exception e) { e.printStackTrace(); } }
            
        }
        
        return result;
        
    }
    
}