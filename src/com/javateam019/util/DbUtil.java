package com.javateam019.util; /**
 * database tool class
 */
import java.sql.Connection;
import java.sql.DriverManager;

public class    DbUtil {

    private String dbUrl = "jdbc:mysql://stusql.dcs.shef.ac.uk:3306/team019";//datebase connection address
    private String dbUserName= "team019";
    private String dbPassword= "fd0751c6";
    private String jdbcName= "com.mysql.jdbc.Driver";//driver name

    /**
     * get connection to database
     * @return
     * @throws Exception
     */
    public Connection getCon()throws Exception{
        Class.forName(jdbcName);
        Connection con =DriverManager.getConnection(dbUrl,dbUserName,dbPassword);
        return con;
    }

    /**
     * close database connection
     * @param con
     * @throws Exception
     */
    public void closeCon(Connection con)throws Exception{
        if(con!=null){
            con.close();
        }
    }

    public static void main(String[] args) {
        DbUtil dbUtil = new DbUtil();
        try {
            dbUtil.getCon();
            System.out.println("connection successful");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("connection fail");
        }
    }
}
