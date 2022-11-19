package pl.coderslab.entity;

import com.google.protobuf.StringValue;
import org.mindrot.jbcrypt.BCrypt;
import pl.coderslab.MainDao.DbUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;

public class UserDao {
    private static final String ADD_USER = "INSERT INTO Workshop2.Users (email, username, password) VALUES (?, ?, ?)";
    private static final String UPDATE_USER = "UPDATE Workshop2.Users SET email = ?, username = ?, password = ? WHERE id = ?";
    private static final String SELECT_BY_ID = "SELECT email, username, password FROM Workshop2.Users WHERE id = ?";
    private static final String DELETE_USER = "DELETE FROM Workshop2.Users WHERE id = ?";
    private static final String SELECT_ALL_USERS = "SELECT * FROM Workshop2.Users";
    public static final String CREATE_DATABASE =
            "CREATE DATABASE IF NOT EXISTS Workshop2\n" +
            "CHARACTER SET utf8mb4\n" +
            "COLLATE utf8mb4_unicode_ci\n";
    public static final String CREATE_TABLE =
            "CREATE TABLE IF NOT EXISTS Workshop2.Users\n" +
            "(\n" +
            "    id int(11) NOT NULL PRIMARY KEY AUTO_INCREMENT,\n" +
            "    email VARCHAR(255) UNIQUE,\n" +
            "    username VARCHAR(255) NOT NULL ,\n" +
            "    password VARCHAR(255) NOT NULL\n" +
            ")";

    public String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    public User create(User user) {

        try (Connection conn = DbUtil.connect()) {

            PreparedStatement statement = conn.prepareStatement(ADD_USER, Statement.RETURN_GENERATED_KEYS);

            statement.setString(1, user.getEmail());
            statement.setString(2, user.getUsername());
            statement.setString(3, hashPassword(user.getPassword()));
            statement.executeUpdate();
            //Pobieramy wstawiony do bazy identyfikator, a następnie ustawiamy id obiektu user.
            ResultSet resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                user.setId(resultSet.getInt(1));
            }
            return user;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public User read(int userId) {
        String email = null;
        String username = null;
        String password = null;
        try (Connection connection = DbUtil.connect()) {
            PreparedStatement preparedStatement = connection.prepareStatement(SELECT_BY_ID);
            preparedStatement.setInt(1, userId);
            try (ResultSet rs = preparedStatement.executeQuery()) {
                if (rs.next()) {
                    email = rs.getString(1);
                    username = rs.getString(2);
                    password = rs.getString(3);
                    System.out.println("email: " + email + ", username: " + username + ", password: " + password);
                } else System.out.println("null");
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        } catch (SQLException e2) {
            e2.printStackTrace();
        }
        return null;
    }

    public void update(User user) {
        System.out.println("Aktualne dane:");
        read(user.getId());
        Scanner scanner = new Scanner(System.in);
        System.out.println("Podaj nowy email:");
        String mail = scanner.nextLine();
        System.out.println("Podaj nowy userName: ");
        String userName = scanner.nextLine();
        System.out.println("Podaj nowe hasło: ");
        String pass = scanner.nextLine();

        try (Connection connection = DbUtil.connect()) {
            PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_USER);
            preparedStatement.setInt(4, user.getId());
            preparedStatement.setString(1, mail);
            preparedStatement.setString(2, userName);
            preparedStatement.setString(3, hashPassword(pass));
            preparedStatement.executeUpdate();
        } catch (SQLException e3) {
            e3.printStackTrace();
        }
    }
    public void delete(int userId) {
        try (Connection connection = DbUtil.connect()) {
            PreparedStatement preparedStatement = connection.prepareStatement(DELETE_USER);
            preparedStatement.setInt(1, userId);
            preparedStatement.execute();
            System.out.println("Użytkownik o id " + userId + " został usunięty z bazy użytkowników");
        } catch (SQLException e4) {
            e4.printStackTrace();
        }
    }

    public ArrayList<User> findAll() {
        ArrayList<User> List = new ArrayList<>(0);
        User user = new User();
        try (Connection connection = DbUtil.connect()) {
            PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_USERS);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                user.setId(rs.getInt("id"));
                user.setEmail(rs.getString("email"));
                user.setUsername(rs.getString("username"));
                user.setPassword(rs.getString("password"));
                List.add(user);
                for (User user1 : List) {
                    System.out.println(user1);
                }
            }
        } catch (SQLException e5) {
            e5.printStackTrace();
        }
        return List;
    }
}
