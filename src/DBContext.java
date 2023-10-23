import java.sql.*;
import java.util.ArrayList;


public class DBContext {

    private static String query;

    public static void connectionTest() {
        Connection connection = null;
        try {
            // create a connection to the database
            connection = DriverManager.getConnection(DbConfig.getConnectionString());

            System.out.println("Connection to SQLite has been established.");

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }


    //Get Users
    private static User convertDbSetToUser(ResultSet resultSet) throws SQLException {

        return new User(resultSet.getInt("Id"),
                resultSet.getString("Name"),
                resultSet.getString("Surname"),
                resultSet.getInt("AddressId"));
    }

    private static ArrayList<User> convertDbSetToUsers(ResultSet resultSet) throws SQLException {
        ArrayList<User> users = new ArrayList();

        while (resultSet.next()) {
            users.add(new User(resultSet.getInt("Id"),
                    resultSet.getString("Name"),
                    resultSet.getString("Surname"),
                    resultSet.getInt("AddressId")));
        }

        return users;
    }

    private static User executeSelectUser(String command) {
        Connection connection = null;
        ResultSet resultSet = null;
        User user = null;
        try {
            Class.forName("org.sqlite.JDBC");
            // create a connection to the database
            connection = DriverManager.getConnection(DbConfig.getConnectionString());
            Statement statement = connection.createStatement();

            user = convertDbSetToUser(statement.executeQuery(command));


        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }
        return user;
    }

    private static ArrayList<User> executeSelectUsers(String command) {
        Connection connection = null;
        ArrayList<User> users = new ArrayList<>();
        try {
            Class.forName("org.sqlite.JDBC");
            // create a connection to the database
            connection = DriverManager.getConnection(DbConfig.getConnectionString());
            Statement statement = connection.createStatement();

            //executeQuery
            users = convertDbSetToUsers(statement.executeQuery(command));

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }
        return users;
    }

    public static  ArrayList<User> getUsersByNameAndSurname(String name, String surname) {
        query = "Select * FROM User " +
                "Where Name = \"" + name + "\"" +
        " And Surname = \"" + surname + "\"";

        ArrayList<User> users = null;
        users = executeSelectUsers(query);

        return users;
    }

    public static  ArrayList<User> getUsersByCity(String city) {
        query = "Select * FROM User u " +
                "JOIN Address a ON u.AddressId = a.Id " +
                "Where a.City = \"" + city + "\"";

        ArrayList<User> users = null;
        users = executeSelectUsers(query);

        return users;
    }

    public static  ArrayList<User> getAllUsers() {
        query = "Select * FROM User ";

        ArrayList<User> users = null;
        users = executeSelectUsers(query);

        return users;
    }

    //Get Address

    private static Address convertDbSetToAddress(ResultSet resultSet) throws SQLException {

        return new Address(resultSet.getInt("Id"),
                resultSet.getString("City"),
                resultSet.getString("Street"),
                resultSet.getInt("Number"));
    }

    private static Address executeSelectAddressById(String command) {
        Connection connection = null;
        ResultSet resultSet = null;
        Address address = null;

        try {
            Class.forName("org.sqlite.JDBC");
            // create a connection to the database
            connection = DriverManager.getConnection(DbConfig.getConnectionString());
            Statement statement = connection.createStatement();

            resultSet = statement.executeQuery(command);
            address = convertDbSetToAddress(resultSet);

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }
        return address;
    }

    private static ArrayList<Address> convertDbSetToAddresses(ResultSet resultSet) throws SQLException {
        ArrayList<Address> addresses = new ArrayList<>();

        while (resultSet.next()) {
            addresses.add(new Address(resultSet.getInt("Id"),
                    resultSet.getString("City"),
                    resultSet.getString("Street"),
                    resultSet.getInt("Number")));
        }

        return addresses;
    }

    public static Address getAddressById(int addressID) {
        query = "Select * FROM Address" +
                " Where Id = " + addressID;

        Address address = null;
        address = executeSelectAddressById(query);

        return address;
    }

    public static Address getAddress(Address address) {
        query = "Select * FROM Address" +
                " Where City = \"" +  address.getCity() +
                "\"  AND Street  = \"" + address.getStreet() +
                "\" AND Number = " + address.getNumber();

        Address addressRes = null;
        addressRes = executeSelectAddressById(query);

        return addressRes;
    }


    //Insert User
    public static void insertUser(User user) {
        String addressID;
        if(user.getAddress() == null){
            addressID = "NULL";
        }else addressID = user.getAddress().toString();
        query = "Insert Into User " +
                "( Name, Surname, AddressId)" +
                "values (" +
                "'" +user.getName() + "'," +
                "'" +user.getSurname() + "'," +
                addressID +
                ")";

        executeUpdate(query);
    }

    //Insert Address
    public static void insertAddress(Address address){
        query = "Insert Into Address " +
                "( City, Street, Number )" +
                "values (" +
                "'" + address.getCity() + "'," +
                "'" + address.getStreet() + "'," +
                address.getNumber() +
                ")";

        executeUpdate(query);
    }


    //Drop User
    public static void deleteUser(User user){

        query = "Delete From User " +
                "Where Id = " +user.getId();

        executeUpdate(query);
    }

    //Drop Address
    public static void deleteEvent(Address address){
        deleteAddressInUser(address);

        query = "Delete From Address " +
                "Where Id = " + address.getId();

        executeUpdate(query);
    }


    private static void deleteAddressInUser(Address address){
        query = "Delete From User " +
                "Where AddressId = " + address.getId() ;

        executeUpdate(query);
    }

    public static void deleteAllUsers(){
        query = "Delete From User; ";

        executeUpdate(query);
    }

    //Update Addres
    public static void updateAddressInUser(User user, int addressId){
        query = "Update User " +
                "Set AddressId = " + addressId +
                " Where Id = " + user.getId();
        executeUpdate(query);
    }


    //Insert, Update, Delete, Create
    private static void executeUpdate(String command) {
        Connection connection = null;

        try {
            Class.forName("org.sqlite.JDBC");
            // create a connection to the database
            connection = DriverManager.getConnection(DbConfig.getConnectionString());
            Statement statement = connection.createStatement();


            statement.executeUpdate(command);

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }
}
