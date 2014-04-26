package connectToDB;

import java.sql.*;
import java.util.ArrayList;

public class Get_DB_info {
	public static ArrayList<String> get_db_info_schema(){
		ResultSet rs_type;      //resultset object gets the set of values retreived from the database
		String query_type = "SELECT data_type,column_name from information_schema.\"columns\" WHERE \"table_name\"='sales' ";
		rs_type = st.executeQuery(query_type);              //executing the query 
		
		
		
		return null;
		
	}
}
