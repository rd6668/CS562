import java.sql.*;
import java.util.*;

class dbTuple{
	int	quant;
	String	state;
	int	month;
	int	year;
	int	day;
	String	prod;
	String	cust;
}

class MF_structure{
	String	cust;
	String	prod;
	int	month;
	int	max_quant_1;
	int	sum_quant_2;
	int	count_quant_2;
	int	min_quant_3;
	int	max_quant_3;
	void output(){
		System.out.printf(cust+"\t"+prod+"\t"+month);
		if (max_quant_1 == 0)
			System.out.printf("\t0");
		else
			System.out.printf("\t"+max_quant_1);
		if (count_quant_2 == 0)
			System.out.printf("\t0");
		else
			System.out.printf("\t"+sum_quant_2/count_quant_2);
		if (min_quant_3 == Integer.MAX_VALUE)
			System.out.printf("\t0");
		else
			System.out.printf("\t"+min_quant_3);
		if (max_quant_3 == 0)
			System.out.printf("\t0");
		else
			System.out.printf("\t"+max_quant_3);
		System.out.printf("\n");
	}
}

public class EMFoutput {
	String usr ="jiangao";
	String pwd ="";
	String url ="jdbc:postgresql://localhost:5432/sales";
	ArrayList<MF_structure> result_list = new ArrayList<MF_structure>();
	int	sum_quant_0 = 0;
	int	count_quant_0 = 0;

	public static void main(String[] args) {
		EMFoutput emf = new EMFoutput();
		emf.connect();
		emf.retrieve();
		emf.output();
	}
	void connect(){
		try {
		Class.forName("org.postgresql.Driver");
		System.out.println("Success loading Driver!");
		} catch(Exception exception) {
		exception.printStackTrace();
		}
	}
	void retrieve(){
		try {
		Connection con = DriverManager.getConnection(url, usr, pwd);
		System.out.println("Success connecting server!");
		ResultSet rs;
		boolean more;
		Statement st = con.createStatement();
		String ret = "select * from sales";
		rs = st.executeQuery(ret);
		more=rs.next();
		while(more){
			dbTuple newtuple = new dbTuple();
			newtuple.quant = rs.getInt("quant");
			newtuple.state = rs.getString("state");
			newtuple.month = rs.getInt("month");
			newtuple.year = rs.getInt("year");
			newtuple.day = rs.getInt("day");
			newtuple.prod = rs.getString("prod");
			newtuple.cust = rs.getString("cust");
			sum_quant_0 += newtuple.quant;
			count_quant_0 ++;
			more=rs.next();
		}

		rs = st.executeQuery(ret);
		more=rs.next();
		while(more){
			dbTuple newtuple = new dbTuple();
			newtuple.quant = rs.getInt("quant");
			newtuple.state = rs.getString("state");
			newtuple.month = rs.getInt("month");
			newtuple.year = rs.getInt("year");
			newtuple.day = rs.getInt("day");
			newtuple.prod = rs.getString("prod");
			newtuple.cust = rs.getString("cust");
			if (newtuple.state.equals("NY")){	
				boolean found = false;
				for (MF_structure temp : result_list){
					if(compare(temp.cust,newtuple.cust) && compare(temp.prod,newtuple.prod) && compare(temp.month,newtuple.month)){	
						temp.max_quant_1 = (temp.max_quant_1< newtuple.quant) ? newtuple.quant :temp.max_quant_1;
						found=true;
					}
				}
				if (found == false){
					MF_structure newrow = new MF_structure();
					newrow.cust = newtuple.cust;
					newrow.prod = newtuple.prod;
					newrow.month = newtuple.month;
					newrow.max_quant_1 = newtuple.quant;
					newrow.sum_quant_2 = 0;
					newrow.count_quant_2 = 0;
					newrow.min_quant_3 = Integer.MAX_VALUE;
					newrow.max_quant_3 = 0;
					result_list.add(newrow);
				}
			}
			more=rs.next();
		}

		rs = st.executeQuery(ret);
		more=rs.next();
		while(more){
			dbTuple newtuple = new dbTuple();
			newtuple.quant = rs.getInt("quant");
			newtuple.state = rs.getString("state");
			newtuple.month = rs.getInt("month");
			newtuple.year = rs.getInt("year");
			newtuple.day = rs.getInt("day");
			newtuple.prod = rs.getString("prod");
			newtuple.cust = rs.getString("cust");
			if (newtuple.state.equals("NJ")&&newtuple.quant>sum_quant_0/count_quant_0){	
				boolean found = false;
				for (MF_structure temp : result_list){
					if(compare(temp.cust,newtuple.cust) && compare(temp.prod,newtuple.prod) && compare(temp.month,newtuple.month)){	
						temp.sum_quant_2 += newtuple.quant;
						temp.count_quant_2 ++;
						found=true;
					}
				}
				if (found == false){
					MF_structure newrow = new MF_structure();
					newrow.cust = newtuple.cust;
					newrow.prod = newtuple.prod;
					newrow.month = newtuple.month;
					newrow.max_quant_1 = 0;
					newrow.sum_quant_2 = newtuple.quant;
					newrow.count_quant_2 = 1;
					newrow.min_quant_3 = Integer.MAX_VALUE;
					newrow.max_quant_3 = 0;
					result_list.add(newrow);
				}
			}
			more=rs.next();
		}

		rs = st.executeQuery(ret);
		more=rs.next();
		while(more){
			dbTuple newtuple = new dbTuple();
			newtuple.quant = rs.getInt("quant");
			newtuple.state = rs.getString("state");
			newtuple.month = rs.getInt("month");
			newtuple.year = rs.getInt("year");
			newtuple.day = rs.getInt("day");
			newtuple.prod = rs.getString("prod");
			newtuple.cust = rs.getString("cust");
			if (newtuple.state.equals("CT")){	
				boolean found = false;
				for (MF_structure temp : result_list){
					if(compare(temp.cust,newtuple.cust) && compare(temp.prod,newtuple.prod) && compare(temp.month,newtuple.month)){	
						temp.min_quant_3 = (temp.min_quant_3> newtuple.quant) ? newtuple.quant :temp.min_quant_3;
						temp.max_quant_3 = (temp.max_quant_3< newtuple.quant) ? newtuple.quant :temp.max_quant_3;
						found=true;
					}
				}
				if (found == false){
					MF_structure newrow = new MF_structure();
					newrow.cust = newtuple.cust;
					newrow.prod = newtuple.prod;
					newrow.month = newtuple.month;
					newrow.max_quant_1 = 0;
					newrow.sum_quant_2 = 0;
					newrow.count_quant_2 = 0;
					newrow.min_quant_3 = newtuple.quant;
					newrow.max_quant_3 = newtuple.quant;
					result_list.add(newrow);
				}
			}
			more=rs.next();
		}

		}catch(Exception e) {
			System.out.println("errors!");
			e.printStackTrace();
		}
	}
	void output(){
		for (MF_structure temp : result_list)
			temp.output();
	}
	boolean compare(String s1, String s2){
		return s1.equals(s2);
	}
	boolean compare(int i1, int i2){
		return (i1 == i2);
	}
}
		
		
		
		
		
