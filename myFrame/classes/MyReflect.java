package mg.controller;
import annotations.*;
import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.*;

public class MyReflect {
//Fonction pour recuperer le nom d'une classe
    public static String getClassName(Object objet){
        String className=objet.getClass().getSimpleName();
        return className;
    }   

//Fonction pour scanner les classes pour recuperer les controlleurs
    public static ArrayList<Class<?>> scanClasses(String packageName) throws Exception {
        ArrayList<Class<?>> classes = new ArrayList<>();
        String path = packageName.replace('.', '/');
        URL url = Thread.currentThread().getContextClassLoader().getResource(path); 
        if (url == null) {
            throw new Exception("Package :" + packageName + " nom trouve");
        }
        File directory = new File(url.toURI());
        File[] files = directory.listFiles();
        for (File file : files) {
            String fileName = file.getName();
            System.out.println("File : " + fileName);
            if (fileName.endsWith(".class")) {
                String className = packageName + '.' + fileName.substring(0, fileName.length() - 6);
                try {
                    ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
                    Class<?> loadedClass = classLoader.loadClass(className);
                    if (loadedClass.isAnnotationPresent(ControllerAnnotation.class)){
                        classes.add(loadedClass);
                    }
                } 
                catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
        }
        return classes;
    }
// Function pour créer une instance
    private static Object createInstance(Class<?> clazz) throws Exception {
        Constructor<?> constructor = clazz.getDeclaredConstructor();
        constructor.setAccessible(true); // In case the constructor is not public
        return constructor.newInstance();
    }

    public static void scanMethod(String packageName,HashMap<String,String> map)throws Exception{
        ArrayList<Class<?>> controlleurs=MyReflect.scanClasses(packageName);
        for (Class<?> controlleur : controlleurs){
            Method[] methods=controlleur.getDeclaredMethods();
            for(Method method : methods){
                if(method.isAnnotationPresent(Get.class)){
                    Get annotation=method.getAnnotation(Get.class);
                    // Object obj=null;
                    Object obj=createInstance(controlleur);
                    String url="http://localhost:8080/test/"+annotation.value();
                    map.put(url,(String)method.invoke(obj));
                }
            }
        }

    } 
    // public static String invokeMethod(){
        
    // }
}