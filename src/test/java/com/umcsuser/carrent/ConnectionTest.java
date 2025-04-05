package com.umcsuser.carrent;

import com.umcsuser.carrent.db.JdbcConnectionManager;
import org.junit.Test;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class ConnectionTest {
    @Test
    public void testConnection() {
        try(Connection connection = JdbcConnectionManager.getInstance().getConnection();
            Statement stmt = connection.createStatement()){
            ResultSet rs = stmt.executeQuery("SELECT NOW()");
            if (rs.next()) {
                System.out.println(rs.getString(1));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
