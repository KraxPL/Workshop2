package pl.coderslab.MainDao;

import pl.coderslab.entity.User;
import pl.coderslab.entity.UserDao;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

import static pl.coderslab.entity.UserDao.CREATE_DATABASE;
import static pl.coderslab.entity.UserDao.CREATE_TABLE;


public class Main {
    public static void main(String[] args) {
        try (Connection connection = DbUtil.connect();
             Statement statement = connection.createStatement())
             {

            statement.execute(CREATE_DATABASE);
            statement.execute(CREATE_TABLE);

            Scanner scanner = new Scanner(System.in);
            while (true) {
                System.out.println(" add - dodaj użytkownika,\n listId - wyświetl użytkownika o danym ID,\n " +
                        "listAll - wyświetl wszystkich użytkowników z bazy,\n delete - usuń użytkownika o danym id z bazy,\n " +
                        "edit - aby edytować dane użytkownika,\n quit - wyjść z programu\n");
                System.out.println("Wybierz opcję z listy: ");
                String choice = scanner.nextLine();
                User nowy = new User();
                UserDao userDao = new UserDao();
                if ("quit".equals(choice)) {
                    System.out.println("Bye bye");
                    break;
                }
                if ("listAll".equals(choice)) {
                    System.out.println("Lista wszystkich użytkowników");
                    userDao.findAll();
                }
                if ("delete".equals(choice)) {
                    System.out.println("Podaj id użytkownika, którego chcesz usunąć: ");
                    int idToDelete = Integer.parseInt(scanner.nextLine());
                    userDao.delete(idToDelete);
                }
                if ("edit".equals(choice)) {
                    System.out.println("Podaj id użytkownika, którego dane chcesz edytować");
                    int editID = scanner.nextInt();
                    nowy.setId(editID);
                    userDao.update(nowy);
                }
                if ("listId".equals(choice)) {
                    System.out.println("Podaj id szukanego użytkownika");
                    int searchedId = Integer.parseInt(scanner.nextLine());
                    nowy.setId(searchedId);
                    userDao.read(nowy.getId());
                }
                if ("add".equals(choice)) {
                    System.out.println("Podaj e-mail");
                    String email = scanner.nextLine();
                    nowy.setEmail(email);
                    System.out.println("Podaj nazwę użytkownika");
                    String username = scanner.nextLine();
                    nowy.setUsername(username);
                    System.out.println("Podaj hasło");
                    String pass = scanner.nextLine();
                    nowy.setPassword(pass);
                    userDao.create(nowy);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}