package com.topica.jdbc.rollback;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Savepoint;

public class SimulationDemo {

    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/jdbc";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "1111";
    private static final String INSERT_SQL = "Insert into Customer values (?,?,?,?);";
    private static final String NAME_UPDATE_SQL = "Update Customer Set name = ? Where id = ? ;";
    
    private static void insertData(Connection connection, String id, String userName, int age, int balance) throws SQLException {
        PreparedStatement insertStatement = connection.prepareStatement(INSERT_SQL);
        insertStatement.setString(1, id);
        insertStatement.setString(2, userName);
        insertStatement.setInt(3, age);
        insertStatement.setInt(4, balance);
        insertStatement.executeUpdate();
        insertStatement.close();
    }
    private static void updateName(Connection connection, String id, String userName) throws SQLException {
        PreparedStatement nameUpdateStatement = connection.prepareStatement(NAME_UPDATE_SQL);
        nameUpdateStatement.setString(1, userName);
        nameUpdateStatement.setString(2, id);
        nameUpdateStatement.executeUpdate();
        nameUpdateStatement.close();
    }
    public static void main(String[] args) {
        Connection conn = null;
        Savepoint savepoint = null;
        try {
            conn = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
            conn.setAutoCommit(false);        
            insertData(conn, "C1921", "Nguyen Van b", 15, 8291812);
            savepoint = conn.setSavepoint("Savepoint");
            updateName(conn, "C1010","name changed");
            conn.commit();
            System.out.println("Name updated");
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                System.out.println("Execute query failed! Roll back to savepoint !~");
                conn.rollback(savepoint);
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        }finally {
            try {
                conn.close();
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

}
