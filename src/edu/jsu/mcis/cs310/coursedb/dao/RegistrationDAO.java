package edu.jsu.mcis.cs310.coursedb.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;



public class RegistrationDAO {
    
    private final DAOFactory daoFactory;
    
    //Create Strings for our Queries: 
    
    //insert query for registration: (create)
    private static final String QUERY_REGISTRATION = "INSERT INTO registration (studentid, termid, crn) VALUES (?, ?, ?)";
    //delete query to drop one course: delete(int studentid, int termid, int crn)
    private static final String QUERY_DROP = "DELETE FROM registration WHERE (studentid = ?) AND (termid = ?) AND (crn = ?)";
    //delete query to delete all courses that a person is registered for (withdrawl)
    private static final String QUERY_WITHDRAWL = "DELETE FROM registration WHERE (studentid = ?) AND (termid = ?)";
    //select all of the courses which a student is registered for (list()):
    private static final String QUERY_LIST = "SELECT * FROM registration WHERE (studentid = ?) AND (termid = ?)";
    
    RegistrationDAO(DAOFactory daoFactory) {
        this.daoFactory = daoFactory;
    }
    
    
/*
Register for a course (with the "create()" method of the RegistrationDAO class). 
This will involve inserting a new record into the "registration" table containing: 
the numeric student ID assigned to the student's user account (the same ID that was 
created earlier in the "student" table), the term ID (from the "term" table), and the
CRN for the specified section.  All three values should be given as arguments to the method,
which should return a Boolean value of true if the insertion was successful.
*/
    
    public boolean create(int studentid, int termid, int crn) {
        
        boolean result = false;
        
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try {
            
            Connection conn = daoFactory.getConnection();
            
            if (conn.isValid(0)) {
                
                // use our QUERY_REGISTRATION
                ps = conn.prepareStatement(QUERY_REGISTRATION);
                //set int in the first piece of query with studentid
                ps.setInt(1, studentid);
                //set int in the second piece of query with termid
                ps.setInt(2, termid);
                //set int in the third piece of query with crn
                ps.setInt(3, crn);
                
                //flip our boolean flag if we added to our registration:
                if (ps.executeUpdate() > 0){
                    
                    result = true;
                }
            }
        }
        
        catch (Exception e) { e.printStackTrace(); }
        
        finally {
            
            if (rs != null) { try { rs.close(); } catch (Exception e) { e.printStackTrace(); } }
            if (ps != null) { try { ps.close(); } catch (Exception e) { e.printStackTrace(); } }
            
        }
        
        return result;
        
    }
    
    /*
    Drop a previous registration for a single course (with the "delete()" method of the RegistrationDAO class). 
    This will involve a query which deletes the corresponding record from the "registration" table.  The method 
    should accept the student ID, term ID, and CRN number as arguments, and should return a Boolean value of true
    if the deletion was successful.
    */

    public boolean delete(int studentid, int termid, int crn) {
        
        boolean result = false;
        
        PreparedStatement ps = null;
        
        try {
            
            Connection conn = daoFactory.getConnection();
            
            if (conn.isValid(0)) {
                
                //using our QUERY_DROP: 
                ps = conn.prepareStatement(QUERY_DROP);
                //passing student id to first box of the query:
                ps.setInt(1, studentid);
                //passing termid to second box of query:
                ps.setInt(2, termid);
                //passing crn to third box of query:
                ps.setInt(3, crn);
                
                //flipping our boolean flag if the course was deleted from registration:
                
                if (ps.executeUpdate() > 0){
                    
                    result = true;
                }
                
            }
            
        }
        
        catch (Exception e) { e.printStackTrace(); }
        
        finally {

            if (ps != null) { try { ps.close(); } catch (Exception e) { e.printStackTrace(); } }
            
        }
        
        return result;
        
    }
    
    /*
    Withdraw from all registered courses (with an overloaded "delete()" method of the RegistrationDAO class).  
    This will again involve deleting records from the "registration" table, this time for all registrations which
    match the given term ID and student ID.  The method should accept both values as arguments, and should return
    a Boolean value of true if the deletion was successful.
    */
    public boolean delete(int studentid, int termid) {
        
        boolean result = false;
        
        PreparedStatement ps = null;
        
        try {
            
            Connection conn = daoFactory.getConnection();
            
            if (conn.isValid(0)) {
                
                //using our QUERY_WITHDRAWL:
                ps = conn.prepareStatement(QUERY_WITHDRAWL);
                //passing studentid to the first box of query:
                ps.setInt(1, studentid);
                //passing termid to the second box of query:
                ps.setInt(2, termid);
                
                //flipping our boolean flags if withdrawl went through:
                
                if (ps.executeUpdate() > 0){
                    
                    result = true;
                    
                }
                
            }
            
        }
        
        catch (Exception e) { e.printStackTrace(); }
        
        finally {

            if (ps != null) { try { ps.close(); } catch (Exception e) { e.printStackTrace(); } }
            
        }
        
        return result;
        
    }
    
    /*
    List all registered courses for a given student ID and term ID (with the "list()" method of the RegistrationDAO class).  
    The query results should return the student ID, term ID, and CRN of all the registered sections, as given in the
    "registration"table.  The method should return the query results as a JSON array (serialized to a string), which 
    should contain one JSON object per registered course.  (Again, these objects should use the original database field 
    names as the keys and should be ordered by CRN number.)
    */
    

    public String list(int studentid, int termid) {
        
        String result = null;
        
        PreparedStatement ps = null;
        ResultSet rs = null;
        ResultSetMetaData rsmd = null;
        
        try {
            
            Connection conn = daoFactory.getConnection();
            
            if (conn.isValid(0)) {
                
                    //Using our QUERY_LIST:
                    ps = conn.prepareStatement(QUERY_LIST);
                    //putting our student id into the first box
                    ps.setInt(1, studentid);
                    //putting our termid into the second box:
                    ps.setInt(2, termid);
                    
                    //execute: 
                    
                    rs = ps.executeQuery();
                    
                   //using our DAOutility file to convert our result to Json:
                   
                   result = DAOUtility.getResultSetAsJson(rs);
                    
            }
            
        }
        
        catch (Exception e) { e.printStackTrace(); }
        
        finally {
            
            if (rs != null) { try { rs.close(); } catch (Exception e) { e.printStackTrace(); } }
            if (ps != null) { try { ps.close(); } catch (Exception e) { e.printStackTrace(); } }
            
        }
        
        return result;
        
    }
    
}
