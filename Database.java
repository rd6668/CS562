import java.sql.*;
import java.util.*;
public class Database{
	static void dbconnect(){
	try {
		Class.forName("org.postgresql.Driver");     //Loads the required driver
		System.out.println("Success loading Driver!");
	} catch(Exception exception) {
		System.out.println("Fail loading Driver!");
		exception.printStackTrace();
	}
	} 
	static void dbinfo(HashMap<String,String> db_struct){
	try {
		String usr ="jiangao";
		String pwd ="";
		String url ="jdbc:postgresql://localhost:5432/sales";

		Connection con = DriverManager.getConnection(url, usr, pwd);		//connect to the database using the password and username
		System.out.println("Success connecting server!\n");
		Statement st = con.createStatement();		//statement created to execute the query
		/*connect to information schema to get variable type*/
		ResultSet rs_type;                    //resultset object gets the set of values retreived from the database
		String query_type = "SELECT data_type,column_name from information_schema.\"columns\" WHERE \"table_name\"='sales' ";
		rs_type = st.executeQuery(query_type);              //executing the query 
		while(rs_type.next()){
			String temp="";
			if(rs_type.getString("data_type").equals("character varying"))
				temp = "String";
			else if(rs_type.getString("data_type").equals("integer"))
				temp = "int";
			db_struct.put(rs_type.getString("column_name"),temp);
		}
	}catch(SQLException e) {
		System.out.println("Connection URL or username or password errors!");
		e.printStackTrace();
	}
	}
}