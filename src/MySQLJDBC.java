import java.sql.*;

public class MySQLJDBC {

	public static void main(String[] args) {

		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		try{

			//Register JDBC driver
			Class.forName("com.mysql.jdbc.Driver");

			//Open a connection
			System.out.println("Connecting to database...");
			conn = DriverManager.getConnection("jdbc:mysql://localhost/mydb?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC", "root", "5749");

			//Execute a query
			stmt = conn.createStatement();
			rs = stmt.executeQuery("SELECT * FROM instructor");

			//Print results
            while(rs.next()) {
            	System.out.println(rs.getString("dept_name") + "|" + rs.getString(2) + "|" + rs.getString(3) + "|" + rs.getDouble("salary"));

            }


            // close a connection
            stmt.close();
            conn.close();

		}catch (SQLException ex) {
			//Handle errors for JDBC
			ex.printStackTrace();
		} catch (Exception e){
		    //Handle errors for Class.forName
			e.printStackTrace();
		}
	}
}