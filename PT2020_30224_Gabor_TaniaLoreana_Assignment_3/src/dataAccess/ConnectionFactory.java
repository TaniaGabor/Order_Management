package dataAccess;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 * Clasa Connection face conexiunea cu baza de date.
 */
public class ConnectionFactory{
    public static Connection connection;
    public static  Connection getConnection(){


        String dbName="warehouse";
        String userName="root";
        String password="parola123@";

        try {


            connection= DriverManager.getConnection("jdbc:mysql://localhost/"+dbName,userName,password);


        } catch (Exception e) {
            e.printStackTrace();
        }


        return connection;
    }

}