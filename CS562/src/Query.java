import java.util.*;
import java.io.*;

/*
*      CS 562 FINAL PROJECT
*      AUTHOR: DI REN, JIAN GAO, ZHOUYI ZHU
*       MF QUERY AND EMF QUERY
*/

/*
*this is the class storage those query information
*/

//info save aggregate functions
class Info{
    String aggregate="";
    String attribute="";
    String index="";
    Info(String index, String attribute){
        this.index=index;
        this.attribute=attribute;
    }
    Info(String index, String aggregate, String attribute){
        this.index = index;
        this.aggregate = aggregate;
        this.attribute = attribute;
    }
    String getName(){
        return aggregate+"_"+attribute+"_"+index;
    }
}

//save such that stuff which is the last line
class Condition{
    String index;
    String condition_info;
    Condition(String index, String condition_info){
        this.index = index;
        this.condition_info = condition_info;
    }
}

//struct to save all the query info
class QueryStruct{
    ArrayList<String> grouping_attributes = new ArrayList<String>();
    ArrayList<String> where = new ArrayList<String>(); 
    int grouping_variable_number;
    ArrayList<Info> select_attr_info = new ArrayList<Info>();
    ArrayList<Info> aggregate_function_info = new ArrayList<Info>();
    ArrayList<Condition> condition = new ArrayList<Condition>();
}

//deal with the input query and save them into our query sturct
public class Query{
    static void query_process(QueryStruct qs, File file){
    try{
        Scanner input = new Scanner(file);

        String [] grouping_attributes = input.nextLine().split(", ");
        String [] where = input.nextLine().split(", ");
        qs.grouping_variable_number = Integer.parseInt(input.nextLine());
        String [] select_attributes = input.nextLine().split(", ");
        String [] aggregate_functions = input.nextLine().split(", ");
        String [] conditions = input.nextLine().split(", ");
        for (String temp : grouping_attributes)
            qs.grouping_attributes.add(temp);

        for (String temp : where){
            if (temp.equals("")){
                break;
            } 
            qs.where.add(temp);

        }
        for (String temp : select_attributes){
            String [] item = temp.split("_");

            Info newinfo = new Info(item[0], item[1], item[2]);
            qs.select_attr_info.add(newinfo);
            //qs.query_info.get(Integer.parseInt(item[0])).select_attributes.add(item[0]+"_"+item[1]+"_"+item[2]);
        }
        for (String temp : aggregate_functions){
            if (temp.equals("")){
                break;
            }
            String [] item = temp.split("_");
            Info newinfo = new Info(item[0], item[1], item[2]);
            qs.aggregate_function_info.add(newinfo);
            //qs.query_info.get(Integer.parseInt(item[0])).aggregate_functions.add(item[0]+"_"+item[1]+"_"+item[2]);
        }
        for (String temp : conditions){
            String [] item = temp.split("#");
            Condition con = new Condition(item[0], item[1]); 
            qs.condition.add(con);
        }
        //    qs.query_info.get(Integer.parseInt(item[0])).conditions.add(item[0]+"_"+item[1]);
        }catch(Exception e){
        System.out.println("Error!");
        }
    }
    static void output(QueryStruct qs){
        for (String temp : qs.grouping_attributes){
            System.out.printf(temp+"\t");
        }
        System.out.printf("\n");
        System.out.printf(qs.grouping_variable_number+"\n");

        for (Info temp : qs.select_attr_info){
            System.out.printf(temp.getName()+"    ");
        }
        System.out.printf("\n");
        for (Info temp : qs.aggregate_function_info){
            System.out.printf(temp.getName()+"    ");
        }
        System.out.printf("\n");
        for (Condition temp : qs.condition){
            System.out.printf(temp.index+"\t"+temp.condition_info+"\n");
        }
    }
}
