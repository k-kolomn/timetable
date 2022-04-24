import java.sql.*;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        Main main = new Main();
        User user = new User();


//         main.getUsers();
//        int userId = main.getUserCount();
//        System.out.println(String.format("%d users found", userId));
//
//        long id = main.insertUser(user1);
//
//        System.out.println(
//                String.format("%s %s user was inserted with id %d", user1.getFirstname(), user1.getLastname(), id)
//        );
//        main.insertUsers(list);
    }

    private final String url = "jdbc:postgresql://localhost:5432/school";
    private final String user = "postgres";
    private final String password = "kolbasa84";

    public Connection connect() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }

    public long insertUser(User user) {
        String sql = "INSERT INTO students(first_name, last_name)" + "VALUES (?,?)";
        int id = 0;

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, user.getFirstname());
            pstmt.setString(2, user.getLastname());
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



    public int deleteActor(int id){
        String sql = "DELETE FROM students WHERE id = ?";

        int affectedRows = 0;

        try(Connection conn = connect();
            PreparedStatement pstmt = conn.prepareStatement(sql)
        ){
            pstmt.setInt(1, id);

            affectedRows = pstmt.executeUpdate();
        } catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return affectedRows;
    }


}

