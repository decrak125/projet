package main;
import util.Util;
import util.Mapping;
import annotation.*;
import java.util.HashMap;
public class Main {
    public static void main (String[] args) {
        HashMap<String, Mapping> list=new HashMap<String, Mapping>();

        try {
            list=Util.getAnnotedMethods(Util.scanPackage( "controller", Controller.class), Get.class);
        } catch (Exception e ) {
            e.printStackTrace();
        }
    
        String url="localhost:8080/sprint3/call";

        int index=url.lastIndexOf("/");
        
        String methodMapping="/call";
        Mapping mapping=list.get(methodMapping);

        System.out.println(mapping.getClassName()+"/"+mapping.getMethodName());
    }    
}
