package com.morandror.scserver.database;

import org.junit.Test;

import java.sql.*;

public class DBHandlerTest {

    @Test
    public void connectionTest() throws SQLException {
        Connection con = null;
        String url = "jdbc:mysql://localhost:3306/scdb?useSSL=false";
        //String db = "scdb";
        String driver = "com.mysql.jdbc.Driver";
        String user = "admin";
        String pass = "Password1!";
        try{
            Class.forName(driver).newInstance();
            con = DriverManager.getConnection(url, user, pass);
            Statement st = con.createStatement();
            ResultSet res = st.executeQuery("SELECT * FROM  testtable");
            System.out.println("ID: " + "\t" + "Name: ");
            while (res.next()) {
                int i = res.getInt("idTestTable");
                String s = res.getString("Name");
                System.out.println(i + "\t\t" + s);
            }
        }
        catch (SQLException s){
            System.out.println("SQL code does not execute.");
        }
        catch (Exception e){
            e.printStackTrace();
        }
        finally {
            con.close();
        }
    }
}
