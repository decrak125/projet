package mg.controller;

public class Mapping {
//Attributs
    String className;
    String methodName;

//Setters Getters 
    public void setClassName(String className){
        this.className=className;
    }
    public String getClassName(){
        return className;
    }
    public void setMethodName(String methodName){
        this.methodName=methodName;
    }
    public String getMethodName(){
        return methodName;
    }

//Constructeurs
    public Mapping(){}
    public Mapping(String classname,String methodName){
        this.setClassName(classname);
        this.setMethodName(methodName);
    }
}
