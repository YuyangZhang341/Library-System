package com.javateam019.dao;

import com.javateam019.model.User;
import com.mysql.cj.xdevapi.PreparableStatement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * user dao
 */
public class UserDao {


    /**
     * verify login
     * @param con
     * @param user
     * @return
     * @throws Exception
     */
    public User login(Connection con, User user) throws Exception{
        User resultUser = null;
        String sql="select * from users where email=? and password=?";
        PreparedStatement pstmt = con.prepareStatement(sql);
        pstmt.setString(1,user.getEmail());
        pstmt.setString(2,user.getPassword());
        ResultSet rs = pstmt.executeQuery();
        if(rs.next()){
            resultUser = new User();
            //resultUser.setTitle(rs.getString("title"));
            //resultUser.setForenames(rs.getString("forename"));
           // resultUser.setSurname(rs.getString("surname"));
            //resultUser.setUniversityAffiliation(rs.getString("universityAffiliation"));
            resultUser.setEmail(rs.getString("email"));
            resultUser.setPassword(rs.getString("password"));

        }
        return resultUser;
    }
}