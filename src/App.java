import javax.xml.bind.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

public class App {

    private void addUser() {
        Scanner scan = new Scanner(System.in);
        User newUser = new User();
        System.out.println("--------------------------------------------------");
        System.out.println("Wprowadz imie:");
        newUser.setName(scan.nextLine());
        System.out.println("Wprowadz nazwisko:");
        newUser.setSurname(scan.nextLine());
        while (true) {
            System.out.println("Czy chcesz wprowadzić adres? T/n");
            String odp = scan.nextLine();
            if (odp.equals("N") || odp.equals("n")) {
                DBContext.insertUser(newUser);
                return;
            }
            if (odp.equals("T") || odp.equals("t")) {
                Address address = getAddress();
                Address address1 = DBContext.getAddress(address);
                if (address1 == null) {
                    DBContext.insertAddress(address);
                    address1 = DBContext.getAddress(address);
                    newUser.setAddress(address1.getId());
                } else {
                    newUser.setAddress(address1.getId());
                }
                DBContext.insertUser(newUser);
                System.out.println("Dodano osobe");
                return;
            }
        }
    }

    private void addAddress() {
        Address newAddres = getAddress();
        Address address1 = DBContext.getAddress(newAddres);
        if (address1 != null) {
            System.out.println("Address istnieje w bazie");
        } else {
            DBContext.insertAddress(newAddres);
            System.out.println("Dodano adres");
        }
    }

    private Address getAddress() {
        Scanner scan = new Scanner(System.in);
        Address newAddress = new Address();
        System.out.println("--------------------------------------------------");
        System.out.println("Wprowadz miasto:");
        String city = scan.nextLine();
        city = city.toLowerCase();
        String cityCap = city.substring(0, 1).toUpperCase();
        if (city.length() > 1) cityCap = cityCap + city.substring(1);
        newAddress.setCity(cityCap);

        System.out.println("Wprowadz ulice:");
        String street = scan.nextLine();
        street = street.toLowerCase();
        String streetCap = street.substring(0, 1).toUpperCase();
        if (street.length() > 1) streetCap = streetCap + street.substring(1);
        newAddress.setStreet(streetCap);

        System.out.println("Wprowadz numer:");
        String num = scan.nextLine();
        int number = Integer.parseInt(num);
        newAddress.setNumber(number);
        return newAddress;
    }

    private void showAllInCity() {

    }

    private void getUsersByCity() {
        Scanner scan = new Scanner(System.in);
        System.out.println("Wprowadz miasto:");
        String city = scan.nextLine();
        city = city.toLowerCase();
        String cityCap = city.substring(0, 1).toUpperCase();
        if (city.length() > 1) cityCap = cityCap + city.substring(1);
        ArrayList<User> users = DBContext.getUsersByCity(cityCap);
        for (int i = 0; i < users.size(); i++) {
            User user = users.get(i);
            System.out.println(i + ". " + user.getName() + " " + user.getSurname());
        }
    }

    private void updateAddress() {
        Scanner scan = new Scanner(System.in);
        User user = new User();
        System.out.println("--------------------------------------------------");
        System.out.println("Wprowadz imie:");
        user.setName(scan.nextLine());
        System.out.println("Wprowadz nazwisko:");
        user.setSurname(scan.nextLine());
        ArrayList<User> users = DBContext.getUsersByNameAndSurname(user.getName(), user.getSurname());
        if (users.size() == 0) System.out.println("Brak osoby w bazie");
        if (users.size() == 1) user = users.get(0);
        if (users.size() > 1) {
            for (int i = 0; i < users.size(); i++) {
                User user1 = users.get(i);
                String address = "NULL";
                if (user1.getAddress() != 0) {
                    Address address1 = DBContext.getAddressById(user1.getAddress());
                    address = address1.getCity() + " " + address1.getStreet() + " " + address1.getNumber();
                }
                System.out.println(i + ". " + user1.getName() + " " + user1.getSurname() + " " + address);
            }
            System.out.println("Wybierz użytkownika: ");
            user = users.get(Integer.parseInt(scan.nextLine()));
        }
        Address newAddress = getAddress();
        Address address1 = DBContext.getAddress(newAddress);
        if (address1 != null) {
            DBContext.updateAddressInUser(user, address1.getId());
        } else {
            DBContext.insertAddress(newAddress);
            address1 = DBContext.getAddress(newAddress);
            DBContext.updateAddressInUser(user, address1.getId());
        }

    }

    private void marshaller() throws JAXBException {
        JAXBContext ctx = JAXBContext.newInstance(Users.class);
        Marshaller marshaller = ctx.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        Users users = new Users();
        users.users = DBContext.getAllUsers();

        marshaller.marshal(users, new File("users.xml"));
    }

    private void unmarshaller() throws JAXBException {
        JAXBContext ctx = JAXBContext.newInstance(Users.class);
        Unmarshaller unmarshaller = ctx.createUnmarshaller();

        Users users = (Users) unmarshaller.unmarshal(new File("users.xml"));
        DBContext.deleteAllUsers();
        for (User user : users.users
        ) {
            DBContext.insertUser(user);
            System.out.println(user.toString());
        }
    }

    public void start() {
        Scanner scan = new Scanner(System.in);
        String wybor;
        while (true) {
            System.out.println(" ");
            System.out.println("--------------------------------------------------");
            System.out.println("1. - Dodaj osobe");
            System.out.println("2. - Dodaj adres");
            System.out.println("3. - Znajdz osoby zamieszkałe w danym miescie");
            System.out.println("4. - Zmien adres osobie");
            System.out.println("5. - Export users to XML");
            System.out.println("6. - Import users from XML");
            System.out.println("7. - Pokaż osoby zamieszkałe w danym miescie");
            System.out.println("0. - Exit");
            wybor = scan.nextLine();
            int choose = Integer.parseInt(wybor);
            switch (wybor) {
                case "1":
                    addUser();
                    break;
                case "2":
                    addAddress();
                    break;
                case "3":
                    getUsersByCity();
                    break;
                case "4":
                    updateAddress();
                    break;
                case "5":
                    try {
                        marshaller();
                    } catch (JAXBException e) {
                        e.printStackTrace();
                    }
                    break;
                case "6":
                    try {
                        unmarshaller();
                    } catch (JAXBException e) {
                        e.printStackTrace();
                    }
                    break;
                case "7":
                    showAllInCity();
                    break;
                case "8":
                    DBContext.deleteAllUsers();
                    break;
                case "0":
                    return;
                default:
                    System.out.println("Error, please try again");
            }
        }


    }
}
