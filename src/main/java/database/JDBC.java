package database;

import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import main.Main;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;

public abstract class JDBC extends Main{
    private static final String protocol = "jdbc";
    private static final String vendor = ":mysql:";
    private static final String location = "//localhost/";
    private static final String databaseName = "client_schedule";
    private static final String jdbcUrl = protocol + vendor + location + databaseName + "?connectionTimeZone = SERVER"; // LOCAL
    private static final String driver = "com.mysql.cj.jdbc.Driver"; // Driver reference
    private static final String userName = "sqlUser"; // Username
    private static String passWord = "Passw0rd!"; // Password
    public static Connection connection;  // Connection Interface

    public static void openConnection()
    {
        try {
            Class.forName(driver); // Locate Driver
            connection = DriverManager.getConnection(jdbcUrl, userName, passWord); // Reference Connection object
            System.out.println("Connection successful!");
        }
        catch(Exception e)
        {
            System.out.println("Error:" + e.getMessage());
        }
    }

    public static Connection getConnection(){
        return connection;
    }

    public static void closeConnection() {
        try {
            connection.close();
            System.out.println("Connection closed!");
        }
        catch(Exception e)
        {
            System.out.println("Error:" + e.getMessage());
        }
    }

    public static boolean validateLogin(String username, String password) throws IOException {
        FileWriter fw = new FileWriter("login_activity.txt", true);
        PrintWriter pw = new PrintWriter(fw);
        LocalDateTime logInDateTime = ZonedDateTime.now().toLocalDateTime();
        String logInTime = logInDateTime.toLocalDate() + " " + logInDateTime.toLocalTime().truncatedTo(ChronoUnit.SECONDS);

        try{
            String sql = "SELECT * FROM users WHERE User_Name = ? AND Password = ?";

            PreparedStatement ps = getConnection().prepareStatement(sql);
            ps.setString(1, username);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();

            rs.next();

            pw.println(String.format("User %s successfully logged in at %s", username, logInTime));
            pw.close();
            return true;

        } catch (SQLException sqlException) {
            pw.println(String.format("User %s gave invalid log-in at %s", username, logInTime));
            pw.close();
            return false;
        }
    }
}
