import java.util.*;

public class project{
	public static void main(String[] args){

		HashMap<String,String> db_struct = new HashMap<String, String>(); //connect to db and get info
		Database.dbconnect();
        	Database.dbinfo(db_struct);

        	/*for (Map.Entry<String, String> entry : db_struct.entrySet()){
        		//String key = entry.getKey();
			//String value = entry.getValue();
        		System.out.printf(entry.getValue()+"\t"+entry.getKey()+"\n");
        	}*/


		QueryStruct query_struct = new QueryStruct(); //read from query and get info
        	Query.query_process(query_struct);
        	//Query.output(query_struct);



        	Generator.generateCode(db_struct,query_struct);

	}
}