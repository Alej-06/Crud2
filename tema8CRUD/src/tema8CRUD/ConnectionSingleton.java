package tema8CRUD;

import java.sql.*;

public class ConnectionSingleton {
	private static Connection con;
	
	public static Connection getConnection() throws SQLException{
		String url = "jdbc:mysql://127.0.0.1:3307/agenda";
		String usuario= "alumno";
		String passwd= "alumno";
		
		if (con == null || con.isClosed()) {
			con=DriverManager.getConnection(url, usuario, passwd);
		}
		return con;
	}
	
	
	
	
}
