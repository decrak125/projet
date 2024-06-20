package controller;
import util.Util;
import util.Mapping;
import annotation.*;
import controller.Test1;
import java.util.HashMap;
import java.time.LocalDate;
public class Main {
    public static void main (String[] args) {
        // Mapping mapping = new Mapping(Test1.class.getName(), "testAgain");

        // try {
        //     String[] params = Util.getParametersName(mapping);
        
        //     for (int i = 0; i < params.length; i++) {
        //         System.out.println(params[i]);
        //     }
        // } catch (Exception e) {
        //     e.printStackTrace();
        // }

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("name", "Jean");
        params.put("firstName", "Laval");
        params.put("date", "2003-05-12");
        params.put("test", "test");

        try {
            Mapping map = new Mapping(Test1.class.getName(), "testAgain", Test1.class.getDeclaredMethod("testAgain", String.class, String.class, LocalDate.class, String.class));
            Object returned = Util.getReturnedObject(map, params); 

            System.out.println(returned);
        } catch (Exception e ) {
            e.printStackTrace();
        }
    }    
}
