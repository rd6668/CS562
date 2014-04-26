import java.sql.*;
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
	static void dbinfo(Generator gen){
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
			Structure temp = new Structure();
			if(rs_type.getString(1).equals("character varying"))
				temp.type = "String";
			else if(rs_type.getString(1).equals("integer"))
				temp.type = "int";
			temp.variable_name=rs_type.getString(2);
			gen.db_struct_list.add(temp);
		}
	}catch(SQLException e) {
		System.out.println("Connection URL or username or password errors!");
		e.printStackTrace();
	}
	}
}