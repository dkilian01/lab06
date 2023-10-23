import java.sql.*;

public class Migration {

    //private static final String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    //private static SecureRandom rnd = new SecureRandom();

    private static String query = "";

    public static void createNewDatabaseFile() {
        try {
            Class.forName("org.sqlite.JDBC");

            Connection conn = DriverManager.getConnection(DbConfig.getConnectionString());
            if (conn != null) {

                DatabaseMetaData meta = conn.getMetaData();
                System.out.println("The driver name is " + meta.getDriverName());
                System.out.println("A new database has been created.");
                createDatabaseTables(conn);
            }

        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void executeUpdateSQL(String command, Connection connection) throws SQLException {
            Statement statement = connection.createStatement();
            statement.executeUpdate(command);
    }

    private static void excuteQuerySQL(String command, Connection connection) throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(command);
        System.out.println(resultSet.toString());
    }

    private static void createDatabaseTables(Connection connection) throws SQLException {
        createUserTable(connection);
        createAddressTable(connection);
    }

    private static void createUserTable(Connection connection) throws SQLException {
        query = "create table User (" +
                "Id integer not null " +
                "constraint User_pk " +
                "primary key autoincrement," +
                "Name varchar not null," +
                "Surname varchar not null," +
                "AddressId integer" +
                ");";

        executeUpdateSQL(query,connection);

        query = "create unique index User_Id_uindex " +
                "on User (Id);";

        executeUpdateSQL(query,connection);
        System.out.println("A new database has been created.");
    }

    private static void createAddressTable(Connection connection) throws SQLException {
        query = "create table Address (" +
                "Id INTEGER not null " +
                "constraint Address_pk " +
                "primary key autoincrement," +
                "City        varchar not null," +
                "Street        varchar," +
                "Number int     not null" +
                ");";

        executeUpdateSQL(query,connection);

        query = "create unique index Address_Id_uindex" +
                "  on Address (Id);";

        executeUpdateSQL(query,connection);
    }

}
