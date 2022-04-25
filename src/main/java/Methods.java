import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.List;

public class Methods {
    User user = new User();
    private final String url = "jdbc:postgresql://localhost:5432/timetable";
    private final String username = "postgres";
    private final String password = "1234";

    public Connection connect() throws SQLException {
        return DriverManager.getConnection(url, username, password);
    }

    public long insertUser(User user) {
        String sql = "INSERT INTO students(email, password, first_name, last_name, form)" + "VALUES (?, ?, ?, ?,?)";
        int id = 0;

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            String regexEmail = "^([a-zA-Z0-9-_.]{5,16})@(gmail|mail)\\.(com|org|ua)$";
            if (user.email.matches(regexEmail)) {
                pstmt.setString(1, user.getEmail());
            }
            Methods.getMD5(user.getPassword());
            pstmt.setString(2, user.getPassword());
            pstmt.setString(3, user.getFirstname());
            pstmt.setString(4, user.getLastname());
            pstmt.setInt(5, user.getForm());
            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        id = rs.getInt(1);
                    }
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }


        return id;
    }

    public void insertUsers(List<User> list) {
        String sql = "INSERT INTO students(first_name, last_name)" + "VALUES (?, ?)";
        try (Connection conn = connect();
             PreparedStatement statement = conn.prepareStatement(sql);) {
            int id = 0;
            for (User user : list) {
                statement.setString(1, user.getFirstname());
                statement.setString(2, user.getLastname());

                statement.addBatch();
                id++;

                if (id % 100 == 0 || id == list.size()) {
                    statement.executeBatch();
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public int getUserCount() {
        String sql = "SELECT id FROM students";
        int id = 0;
        try (Connection conn = connect();
             Statement statement = conn.createStatement();
             ResultSet rs = statement.executeQuery(sql)) {
            rs.next();
            id = rs.getInt(1);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return id;
    }

    public void getUsers() {
        String sql = "SELECT * FROM students";
        try (Connection conn = connect();
             Statement statement = conn.createStatement();
             ResultSet rs = statement.executeQuery(sql)) {
            displayUser(rs);

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }

    private void displayUser(ResultSet rs) throws SQLException {
        while (rs.next()) {
            System.out.println(rs.getString("id") + "\t"
                    + rs.getString("first_name") + "\t"
                    + rs.getString("last_name")
            );
        }
    }

    public void findUserById(int studentId) {
        String sql = "SELECT id, first_name, last_name"
                + "FROM students"
                + "WHERE id = ?";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, studentId);
            ResultSet rs = pstmt.executeQuery();
            displayUser(rs);

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }


    }

    public int updateLastName(int id, String lastName) {
        String sql = "UPDATE students"
                + "SET last_name = ?"
                + "WHERE id = ?";
        int affectedRows = 0;
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, lastName);
            pstmt.setInt(2, id);
            affectedRows = pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return affectedRows;
    }


    public int deleteActor(int id) {
        String sql = "DELETE FROM students WHERE id = ?";

        int affectedRows = 0;

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)
        ) {
            pstmt.setInt(1, id);

            affectedRows = pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return affectedRows;
    }

    public static String getMD5(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");

            byte[] messageDigest = md.digest(input.getBytes());

            BigInteger no = new BigInteger(1, messageDigest);

            String hashText = no.toString(16);
            while (hashText.length() < 32) {
                hashText = "0" + hashText;

            }
            return hashText;


        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }


    public void insertSubject(Subjects subject) {
        String sql = "INSERT INTO subjects(subject_name) VALUES(?)";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, subject.getName());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void categoriesSubjects(){
        // TODO: 25.04.2022 finish method 
     ResultSet rs;
     if (user.form >= 4){
         String sql = "INSERT INTO learning_subjects(allowed_subjects, form_id) VALUES(SELECT first_subjects FROM subjects, INN)";
     }
    }

    public void displaySubjects(ResultSet rs) throws SQLException {
        while (rs.next()) {
            System.out.println(
                    rs.getInt("id") + "\t"
                    + rs.getString("first_subjects") + "\t"
                    + rs.getString("second_subjects") + "\t"
                    + rs.getString("third_subjects") + "\t"
            );
        }
    }
}
