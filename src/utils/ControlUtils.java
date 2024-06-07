package mg.itu.framework.utils;

import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;

import mg.itu.framework.annotation.Controller;
import mg.itu.framework.annotation.GET;
import mg.itu.framework.objects.Mapping;

public class ControlUtils {
    public ArrayList<Class<?>> scanPackage(String packageName){
        ArrayList<Class<?>> toReturn=new ArrayList<Class<?>>();
        
        packageName=packageName.replaceAll("\\.","/");
        File baseFile=new File(getClass().getClassLoader().getResource(packageName).getFile());

        File[] allClasses=baseFile.listFiles();

        for (File file : allClasses) {
            String className=file.getName().split("\\.")[0];

            className=packageName+"."+className;

            try{
                Class<?> toAdd=Class.forName(className);
                toReturn.add(toAdd);
            }

            catch(ClassNotFoundException e){
                System.out.println(className+" n'est pas une classe.");
                continue;
            }
        }

        return toReturn;
    }

    public ArrayList<Class<?>> getControllerList(String packageName){
        ArrayList<Class<?>> controllers=new ArrayList<Class<?>>();
        ArrayList<Class<?>> toAnalyse=scanPackage(packageName);
        for (Class<?> class1 : toAnalyse) {
            if(class1.isAnnotationPresent(Controller.class)) controllers.add(class1);
        }

        return controllers;
    }

    public HashMap<String,Mapping> getRoutes(String packageName){
        HashMap<String,Mapping> toReturn=new HashMap<String,Mapping>();

        ArrayList<Class<?>> toSearchInto= getControllerList(packageName);

        for (Class<?> class1 : toSearchInto) {
            Method[] lsMethods=class1.getDeclaredMethods();

            for (Method meth : lsMethods) {
                GET annot=meth.getAnnotation(GET.class);

                if(annot!=null){
                    Mapping map=new Mapping(class1.getName(), meth.getName());
                    toReturn.put(annot.urlPattern(), map);
                }    
            }
        }

        return toReturn;
    }
}