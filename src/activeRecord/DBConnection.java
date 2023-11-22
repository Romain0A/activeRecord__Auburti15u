package activeRecord;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DBConnection {
    public static DBConnection instance;
    private String userName = "root";
    private String password = "";
    private String serverName = "localhost";
    private String portNumber = "3306";
    private String dbName = "testpersonne";
    private Connection connection;

    public DBConnection(){
    }

    public void setNomDB(String nomDB) throws SQLException {
        dbName = nomDB;
        connection = null;
    }

    public static synchronized DBConnection getInstance(){
        if(instance==null){
            instance = new DBConnection();
        }
        return instance;
    }

    public Connection getConnection() throws SQLException {
        if(connection == null){
            // creation de la connection
            Properties connectionProps = new Properties();
            connectionProps.put("user", userName);
            connectionProps.put("password", password);
            String urlDB = "jdbc:mysql://" + serverName + ":";
            urlDB += portNumber + "/" + dbName;
            Connection connect = DriverManager.getConnection(urlDB, connectionProps);
        }
        return connection;
    }
}
