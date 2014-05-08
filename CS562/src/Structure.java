class Structure {  //class defined to store database and mf_structure and grouping attributes information
    String type;
    String variable_name;
    Structure (){}
    Structure (String type, String variable_name){
        this.type = type;
        this.variable_name = variable_name;
    }

    String attr_name(){
        return variable_name;
    }
    void output (){
        System.out.printf(type+"\t"+variable_name+"\n");
    }
}