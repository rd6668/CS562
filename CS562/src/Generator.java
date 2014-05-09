import java.util.*;
import java.io.*;
/*
*      CS 562 FINAL PROJECT
*      AUTHOR: DI REN, JIAN GAO, ZHOUYI ZHU
*       MF QUERY AND EMF QUERY
*/

/*
*this generator class is for generate mf query output file
*/
public class Generator{
    static void generateCode(HashMap<String,String> db_struct, QueryStruct qs){
        try{
        File file = new File("EMFoutput.java");
        PrintWriter output = new PrintWriter(file);
        //generate code part
        output.print("import java.sql.*;\n");
        output.print("import java.util.*;\n\n");
        output.print("/*  this is generated file \n * author: Di Ren, Zhouyi Zhu, Yi Gao \n");
        output.print("* CS 562 final project \n */\n");

        //generate class dbTuple
        output.print("class dbTuple{\n");
        for (Map.Entry<String, String> entry : db_struct.entrySet())
                output.print("\t"+entry.getValue()+"\t"+entry.getKey()+";\n");
        output.print("}\n\n");

        /*generate mf structure class*/
        output.print("class MF_structure{\n");
        //output grouping attributes
        for (String temp : qs.grouping_attributes)
            output.print("\t"+db_struct.get(temp)+"\t"+temp+";\n");
        //output select attributes
        for (Info temp : qs.select_attr_info){
            if (temp.aggregate.equals("avg")){
                output.print("\t"+db_struct.get(temp.attribute)+"\tsum_"+temp.attribute+"_"+temp.index+";\n");
                output.print("\t"+db_struct.get(temp.attribute)+"\tcount_"+temp.attribute+"_"+temp.index+";\n");
            }
            else
                output.print("\t"+db_struct.get(temp.attribute)+"\t"+temp.getName()+";\n");
        }
        output.print("\tvoid output(){\n");
        output.print("\t\tSystem.out.printf(");
        boolean found = false;
        for (String temp : qs.grouping_attributes){
            if (found == false){
                output.print(temp);
                found = true;
            }
            else if (found == true){
                output.print("+\"\\t\"+"+temp);
            }
        }
        output.print(");\n");
        for (Info temp : qs.select_attr_info){
            if (temp.aggregate.equals("avg")){
                output.print("\t\tif (count_"+temp.attribute+"_"+temp.index+" == 0)\n");
                output.print("\t\t\tSystem.out.printf(\"\\t0\");\n");
                output.print("\t\telse\n");
                output.print("\t\t\tSystem.out.printf(\"\\t\"+sum_"+temp.attribute+"_"+temp.index+"/count_"+temp.attribute+"_"+temp.index+");\n");
            }
            else if (temp.aggregate.equals("max")){
                output.print("\t\tif ("+temp.getName()+" == 0)\n");
                output.print("\t\t\tSystem.out.printf(\"\\t0\");\n");
                output.print("\t\telse\n");
                output.print("\t\t\tSystem.out.printf(\"\\t\"+"+temp.getName()+");\n");
            }
            else if (temp.aggregate.equals("min")){
                output.print("\t\tif ("+temp.getName()+" == Integer.MAX_VALUE)\n");
                output.print("\t\t\tSystem.out.printf(\"\\t0\");\n");
                output.print("\t\telse\n");
                output.print("\t\t\tSystem.out.printf(\"\\t\"+"+temp.getName()+");\n");
            }
            else {
                output.print("\t\tSystem.out.printf(\"\\t\"+"+temp.getName()+");\n");
            }
        }
        output.print("\t\tSystem.out.printf(\"\\n\");\n");
        output.print("\t}\n");

        output.print("}\n\n");

        //output
        output.print("public class EMFoutput {\n");
        output.print("\tString usr =\"jiangao\";\n");
        output.print("\tString pwd =\"\";\n");
        output.print("\tString url =\"jdbc:postgresql://localhost:5432/sales\";\n");
        output.print("\tArrayList<MF_structure> result_list = new ArrayList<MF_structure>();\n");


        //define the aggregate function with index 0
        if (qs.aggregate_function_info.size() > 0){
            for (Info info : qs.aggregate_function_info){
                if (info.aggregate.equals("avg")){
                    output.print("\t"+db_struct.get(info.attribute)+"\tsum_"+info.attribute+"_"+info.index+" = 0;\n");
                    output.print("\t"+db_struct.get(info.attribute)+"\tcount_"+info.attribute+"_"+info.index+" = 0;\n");

                }
                else 
                    output.print("\t"+db_struct.get(info.attribute)+"\t"+info.aggregate+"_"+info.attribute+"_"+info.index+" = 0;\n");
            }
        }

        output.print("\n\tpublic static void main(String[] args) {\n");
        output.print("\t\tEMFoutput emf = new EMFoutput();\n");
        output.print("\t\temf.connect();\n");
        output.print("\t\temf.retrieve();\n");  
        output.print("\t\temf.output();\n\t}\n");  

        output.print("\tvoid connect(){\n");
        output.print("\t\ttry {\n");
        output.print("\t\tClass.forName(\"org.postgresql.Driver\");\n");
        output.print("\t\tSystem.out.println(\"Success loading Driver!\");\n");
        output.print("\t\t} catch(Exception exception) {\n");
        output.print("\t\texception.printStackTrace();\n");
        output.print("\t\t}\n\t}\n");


        output.print("\tvoid retrieve(){\n");
        output.print("\t\ttry {\n");
        output.print("\t\tConnection con = DriverManager.getConnection(url, usr, pwd);\n");
        output.print("\t\tSystem.out.println(\"Success connecting server!\");\n");
        output.print("\t\tResultSet rs;\n");
        output.print("\t\tboolean more;\n");
        output.print("\t\tStatement st = con.createStatement();\n");
        output.print("\t\tString ret = \"select * from sales\";\n");


        //first while loop the add value to aggregate with index 0
        if (qs.aggregate_function_info.size() > 0){
            output.print("\t\trs = st.executeQuery(ret);\n");
            output.print("\t\tmore=rs.next();\n");
            output.print("\t\twhile(more){\n");
            output.print("\t\t\tdbTuple newtuple = new dbTuple();\n");
            for (Map.Entry<String, String> entry : db_struct.entrySet()){
                if (entry.getValue().equals("String")){
                    output.print("\t\t\tnewtuple."+entry.getKey()+" = rs.getString(\""+entry.getKey()+"\");\n");
                }
                if (entry.getValue().equals("int")){
                    output.print("\t\t\tnewtuple."+entry.getKey()+" = rs.getInt(\""+entry.getKey()+"\");\n");
                }
            }
            boolean flag = false;
            output.print("\t\t\tif(");
            if (qs.where.size() == 0){
                output.print("true");
            }
            else{
                for (String temp : qs.where){
                    if (flag == false){
                        output.print(temp);
                        flag = true;
                    }
                    else if (flag == true)
                        output.print(" && "+temp);
                }
            }
            flag = false;
            output.print("){\n");
            for (Info info : qs.aggregate_function_info){
                if (info.aggregate.equals("sum")){
                    output.print("\t\t\t\t"+info.getName()+" += newtuple."+info.attribute+";\n");
                }
                else if (info.aggregate.equals("max")){
                    output.print("\t\t\t\t"+info.getName()+" = ("+info.getName()+"< newtuple."+info.attribute+") ? newtuple."+info.attribute +" : "+info.getName()+";\n");
                }
                else if (info.aggregate.equals("min")){
                    output.print("\t\t\t\t"+info.getName()+" = ("+info.getName()+"> newtuple."+info.attribute+") ? newtuple."+info.attribute +" : "+info.getName()+";\n");
                }
                else if (info.aggregate.equals("count")){
                    output.print("\t\t\t\t"+info.getName()+" ++;\n");
                }
                else if (info.aggregate.equals("avg")){
                    output.print("\t\t\t\tsum_"+info.attribute+"_"+info.index+" += newtuple."+info.attribute+";\n");
                    output.print("\t\t\t\tcount_"+info.attribute+"_"+info.index+" ++;\n");

                }
            }
            output.print("\t\t\t}\n");
            output.print("\t\t\tmore=rs.next();\n");
            output.print("\t\t}\n\n");
        }

        //generate core code!
        for (int i=1; i<=qs.grouping_variable_number; i++){
            output.print("\t\trs = st.executeQuery(ret);\n");
            output.print("\t\tmore=rs.next();\n");
            output.print("\t\twhile(more){\n");
            //get each tuple from database
            output.print("\t\t\tdbTuple newtuple = new dbTuple();\n");
            for (Map.Entry<String, String> entry : db_struct.entrySet()){
                if (entry.getValue().equals("String")){
                    output.print("\t\t\tnewtuple."+entry.getKey()+" = rs.getString(\""+entry.getKey()+"\");\n");
                }
                if (entry.getValue().equals("int")){
                    output.print("\t\t\tnewtuple."+entry.getKey()+" = rs.getInt(\""+entry.getKey()+"\");\n");
                }
            }

            //output if(condition)
            boolean flag = false;
            output.print("\t\t\tif(");
            if (qs.where.size() == 0){
                output.print("true");
            }
            else{
                for (String temp : qs.where){
                    if (flag == false){
                        output.print(temp);
                        flag = true;
                    }
                    else if (flag == true)
                        output.print(" && "+temp);
                }
            }
            flag = false;
            output.print("){\n");
            output.print("\t\t\t\tif (");
            for (Condition temp : qs.condition){
                if (Integer.parseInt(temp.index) == i && flag == false){
                    flag = true;
                    output.print(temp.condition_info);
                }
                else if (Integer.parseInt(temp.index) == i && flag == true){
                    output.print("&&"+temp.condition_info);
                }
            }
            if (flag == false)
                    output.print("true");
            flag = false;
            output.print("){\n");
            output.print("\t\t\t\t\tboolean found = false;\n");
            output.print("\t\t\t\t\tfor (MF_structure temp : result_list){\n");

            //find if the grouping attributes are already in the list
            output.print("\t\t\t\t\t\tif(compare(temp.");
            for (String temp : qs.grouping_attributes){
                if (flag == false){
                    output.print(temp+",newtuple."+temp+")");
                    flag = true;
                }
                else if (flag == true){
                    output.print(" && compare(temp."+temp+",newtuple."+temp+")");
                }
            }
            output.print("){\n");
            for (Info info : qs.select_attr_info){
                if (Integer.parseInt(info.index) == i){
                    if (info.aggregate.equals("avg")){
                        output.print("\t\t\t\t\t\t\ttemp.sum_"+info.attribute+"_"+info.index+" += newtuple."+info.attribute+";\n");
                        output.print("\t\t\t\t\t\t\ttemp.count_"+info.attribute+"_"+info.index+" ++;\n");
                    }
                    if (info.aggregate.equals("sum")){
                        output.print("\t\t\t\t\t\t\ttemp."+info.getName()+" += newtuple."+info.attribute+";\n");
                    }
                    if (info.aggregate.equals("max")){
                        output.print("\t\t\t\t\t\t\ttemp."+info.getName()+" = (temp."+info.getName()+"< newtuple."+info.attribute+") ? newtuple."+info.attribute +" :temp."+info.getName()+";\n");
                    }
                    if (info.aggregate.equals("min")){
                        output.print("\t\t\t\t\t\t\ttemp."+info.getName()+" = (temp."+info.getName()+"> newtuple."+info.attribute+") ? newtuple."+info.attribute +" :temp."+info.getName()+";\n");
                    }
                    if (info.aggregate.equals("count")){
                        output.print("\t\t\t\t\t\t\ttemp."+info.getName()+" ++;\n");
                    }
                }
            }
            //output.print("\t\t\t\t\ttemp.\n");
            output.print("\t\t\t\t\t\t\tfound=true;\n");
            output.print("\t\t\t\t\t\t}\n");
            output.print("\t\t\t\t\t}\n");
            output.print("\t\t\t\t\tif (found == false){\n");
            output.print("\t\t\t\t\t\tMF_structure newrow = new MF_structure();\n");
            for (String temp : qs.grouping_attributes){
                output.print("\t\t\t\t\t\tnewrow."+temp+" = newtuple."+temp+";\n");
            }
            for (Info temp : qs.select_attr_info){
                if (temp.aggregate.equals("avg") && Integer.parseInt(temp.index) == i){
                    output.print("\t\t\t\t\t\tnewrow.sum_"+temp.attribute+"_"+temp.index+" = newtuple."+temp.attribute+";\n");
                    output.print("\t\t\t\t\t\tnewrow.count_"+temp.attribute+"_"+temp.index+" = 1;\n");
                }
                if ((temp.aggregate.equals("sum") || temp.aggregate.equals("max") || temp.aggregate.equals("min")) && Integer.parseInt(temp.index) == i)
                    output.print("\t\t\t\t\t\tnewrow."+temp.getName()+" = newtuple."+temp.attribute+";\n");
                if (temp.aggregate.equals("count") && Integer.parseInt(temp.index) == i)
                    output.print("\t\t\t\t\t\tnewrow."+temp.getName()+" = 1;\n");
                if (Integer.parseInt(temp.index) != i){
                    if(temp.aggregate.equals("avg")){
                        output.print("\t\t\t\t\t\tnewrow.sum_"+temp.attribute+"_"+temp.index+" = 0;\n");
                        output.print("\t\t\t\t\t\tnewrow.count_"+temp.attribute+"_"+temp.index+" = 0;\n");
                    }
                    else if (temp.aggregate.equals("min"))
                        output.print("\t\t\t\t\t\tnewrow."+temp.getName()+" = Integer.MAX_VALUE;\n");
                    else if (temp.aggregate.equals("max"))
                        output.print("\t\t\t\t\t\tnewrow."+temp.getName()+" = 0;\n");
                }
            }
            output.print("\t\t\t\t\t\tresult_list.add(newrow);\n");
            output.print("\t\t\t\t\t}\n");
            output.print("\t\t\t\t}\n");
            output.print("\t\t\t}\n");
            output.print("\t\t\tmore=rs.next();\n");
            output.print("\t\t}\n\n");
        }
        output.print("\t\t}catch(Exception e) {\n");
        output.print("\t\t\tSystem.out.println(\"errors!\");\n");
        output.print("\t\t\te.printStackTrace();\n");
        output.print("\t\t}\n");
        output.print("\t}\n");
        
        output.print("\tvoid output(){\n");
        output.print("\t\tfor (MF_structure temp : result_list)\n");
        output.print("\t\t\ttemp.output();\n");
        output.print("\t}\n");

        //generate compare method
        output.print("\tboolean compare(String s1, String s2){\n");
        output.print("\t\treturn s1.equals(s2);\n\t}\n");
        output.print("\tboolean compare(int i1, int i2){\n");
        output.print("\t\treturn (i1 == i2);\n\t}\n");
        
        output.print("}\n");



        output.print("\t\t\n");
        output.print("\t\t\n");
        output.print("\t\t\n");
        output.print("\t\t\n");
        output.print("\t\t\n");
        output.close();
    }catch(Exception e) {
         System.out.println("errors!");
        e.printStackTrace();
    }
    }
    
}
