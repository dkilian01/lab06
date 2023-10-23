import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class DbConfig {

    //region sqlconfig
    private static String dbName = "DataBase.db";
    private static String pathToDatabase = System.getProperty("user.dir") + "/src/db/";
    private static String driver = "jdbc:sqlite:";
    private static String connectionString = driver + pathToDatabase + dbName;
    //endregion

   // public static final String formatDate = "yyyy-MM-dd HH:mm";

    public static String getConnectionString() {
       return connectionString;
    }
}
