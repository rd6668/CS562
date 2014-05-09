import java.util.*;
import java.io.*;

/*
*      CS 562 FINAL PROJECT
*      AUTHOR: DI REN, JIAN GAO, ZHOUYI ZHU
*       MF QUERY AND EMF QUERY
*/

/*
*THIS IS THE MAIN FUNCTION THAT OPEN THE INPUT FILE AND GET USER INPUT; THEN RUN THE GENERATOR FUNCTION
*/

public class project{
        public static void main(String[] args){

                HashMap<String,String> db_struct = new HashMap<String, String>(); 
                //connect to db and get info
                Database.dbconnect();
                Database.dbinfo(db_struct);

                //get user input to set emf or mf 
                Scanner in = new Scanner(System.in);
                System.out.println("is this a MF query or EMF query? enter 'mf' or 'emf' ");
                String s = in.nextLine();
                /*for (Map.Entry<String, String> entry : db_struct.entrySet()){
                        //String key = entry.getKey();
                        //String value = entry.getValue();
                        System.out.printf(entry.getValue()+"\t"+entry.getKey()+"\n");
                }*/

                //MF 
                if (s.equals("mf")){     
                        File file = new File("sampleQuery.txt");
                        QueryStruct query_struct = new QueryStruct(); 
                        //read from query and get info
                        Query.query_process(query_struct, file);
                        //Query.output(query_struct);
                        Generator.generateCode(db_struct,query_struct);
                }
                //EMF
                else if (s.equals("emf")){
                        File file = new File("sampleQuery2.txt");
                        QueryStruct query_struct = new QueryStruct(); 
                        //read from query and get info
                        Query.query_process(query_struct, file);
                        //Query.output(query_struct);
                        Generator2.generateCode(db_struct,query_struct);
                }
                //ERROR INPUT
                else{
                        System.out.println("input error");
                        return ;
                }


                

        }
}
