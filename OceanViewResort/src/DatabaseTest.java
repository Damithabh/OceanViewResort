import util.DBConnectionPool;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class DatabaseTest {
    public static void main(String[] args) {
        System.out.println("🚀 Starting Database Connection Test...");
        try (Connection conn = DBConnectionPool.getInstance().getConnection()) {
            if (conn != null && !conn.isClosed()) {
                System.out.println("✅ Connection Successful!");

                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT username, role FROM users");

                System.out.println("--- Table: users ---");
                while (rs.next()) {
                    System.out.println("User: " + rs.getString("username") + " | Role: " + rs.getString("role"));
                }
                System.out.println("--------------------");
                System.out.println("✅ Data retrieval successful.");
            } else {
                System.out.println("❌ Connection failed.");
            }
        } catch (Exception e) {
            System.err.println("❌ An error occurred during database testing:");
            e.printStackTrace();
        }
    }
}
