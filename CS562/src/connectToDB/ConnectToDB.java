package connectToDB;

import java.sql.*;

public class ConnectToDB {
	public static ResultSet DB_connect(){
		
		String usr = "zzhu4";
		String pwd = "Iloveyou5201314";
		String url = "jdbc:postgresql://155.246.89.29:5432/zzhu4";
		ResultSet rs = null;
		try {
			// loding the driver
			Class.forName("org.postgresql.Driver");
			System.out.println("Success loading Driver!");
		}

		catch (Exception e) {
			System.out.println("Fail loading Driver!");
			e.printStackTrace();
		}
		try {
			// using the driver to connect server
			Connection conn = DriverManager.getConnection(url, usr, pwd);
			System.out.println("Success connecting server!");

			Statement stmt = conn.createStatement();
			rs = stmt.executeQuery("SELECT * FROM Sales");
		} catch (SQLException e) {
			System.out
					.println("Connection URL or username or password errors!");
			e.printStackTrace();
		}
		return rs;
		
	}//end DB_connect
	
	
	
}
