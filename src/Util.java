package util;
import java.lang.reflect.Parameter;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.Thread;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.net.URL;
import java.net.URLDecoder;
import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import annotation.Controller;
import annotation.Get;
import annotation.Param;
import jakarta.servlet.http.*;
import java.util.HashMap;
public class Util 
{
    public static List<Class<?>> scanPackage (String packageName, Class<? extends Annotation> annotationClass) throws Exception
    {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

        String packagePath = packageName.replace('.', '/');

        List<Class<?>> classes=new ArrayList<>();

        try {
            Enumeration<URL> resources=classLoader.getResources(packagePath);
            resources = classLoader.getResources(packagePath);
            
            while (resources.hasMoreElements()) {
                URL resource = resources.nextElement();
                List<String> classNames = extractClassNames(resource, packageName);
                
                for (int i = 0; i < classNames.size(); i++) {
                    Class<?> clazz=null;

                    try {
                        clazz=Class.forName(classNames.get(i));
                    } catch (ClassNotFoundException ex) {
                        ex.printStackTrace();
                    }

                    if (clazz!=null) {
                        if (clazz.isAnnotationPresent(annotationClass)) {
                            classes.add(clazz);
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (classes.size()==0) {
            throw new Exception("Le package est vide!");
        }

        return classes;
    }

    public static List<String> extractClassNames (URL resourceUrl, String packageName) {
        List<String> classNames = new ArrayList<>();
        try {
            // Décoder l'URL pour obtenir le chemin du répertoire
            String directoryPath = URLDecoder.decode(resourceUrl.getFile(), "UTF-8");

            // Créer un objet File pour le répertoire
            File directory = new File(directoryPath);

            // Vérifier si le répertoire existe et si c'est un répertoire
            if (directory.exists() && directory.isDirectory()) {
                // Récupère la liste des fichiers dans le répertoire
                File[] files = directory.listFiles();
                if (files != null) {
                    for (File file : files) {
                        if (file.isFile() && file.getName().endsWith(".class")) {
                            String filePath = file.getPath();
                            String className = filePath.substring(filePath.indexOf(packageName), filePath.length() - ".class".length());
                            className = className.replace(File.separatorChar, '.');
                            classNames.add(className);
                        }
                    }
                }
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return classNames;
    }

    public static HashMap<String, Mapping> getAnnotedMethods (List<Class<?>> classList, Class<? extends Annotation> annotationClass) throws Exception
    {
        HashMap<String, Mapping> methodList=new HashMap<String, Mapping>();

        for (int i = 0; i < classList.size(); i++) {
            Method[] methods=classList.get(i).getDeclaredMethods();

            for (int j = 0; j < methods.length; j++) {
                if (methods[j].isAnnotationPresent(annotationClass)) {
                    Annotation myAnnotation=methods[j].getAnnotation(annotationClass);

                    if (methodList.get(((Get)myAnnotation).urlMapping())!=null) {
                        Mapping mapping=methodList.get(((Get)myAnnotation).urlMapping());

                        throw new Exception("L'URL "+((Get)myAnnotation).urlMapping()+" que vous tentez d'attribuer à la méthode "+classList.get(i).getName()+"."+methods[j].getName()+" est déjà prise par "+mapping.getClassName()+"."+mapping.getMethodName());
                    }

                    methodList.put(((Get)myAnnotation).urlMapping(), new Mapping(classList.get(i).getName(), methods[j].getName(), methods[j]));
                }
            }
        }

        return methodList;
    }

    public static String getReturnedString (Mapping methodMapping) {
        String className=methodMapping.getClassName();

        String returnedString="";

        Class<?> clazz=null; 
        try {
            clazz=Class.forName(className);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (clazz!=null) {
            Method method=null; 
            
            try {
                method=clazz.getMethod(methodMapping.getMethodName());

                if (method!=null) {
                    Object instance = clazz.getConstructor().newInstance();
                    Object returnedObject = null;

                    returnedObject = method.invoke(instance);

                    System.out.println(returnedObject);

                    if (returnedObject!=null) {
                        returnedString = returnedObject.toString();
                    }

                    System.out.println("<p>Retour de la fonction: "+returnedString+" </p>");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return returnedString;
    }

    public static Object getReturnedObject (Mapping methodMapping) throws Exception {
        String className=methodMapping.getClassName();

        Object returnedObject=null;

        Class<?> clazz=null; 
        try {
            clazz=Class.forName(className);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (clazz!=null) {
            Method method=null; 
            
            try {
                method=clazz.getMethod(methodMapping.getMethodName());

                if (method!=null) {
                    Object instance = clazz.getConstructor().newInstance();

                    returnedObject = method.invoke(instance);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (!(returnedObject instanceof String) && !(returnedObject instanceof ModelView)) {
            throw new Exception("Type de retour inconnu au framework");
        }

        return returnedObject;
    }

    public static Object getReturnedObject (Mapping methodMapping, HttpServletRequest request) throws Exception {
        String className=methodMapping.getClassName();

        Object returnedObject=null;

        Class<?> clazz=null; 
        try {
            clazz=Class.forName(className);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (methodMapping.getMethod()!=null) {
            Method method=methodMapping.getMethod(); 
            
            try {
                if (method!=null) {
                    Object instance = clazz.getConstructor().newInstance();
                    String[] parametersName = Util.getParametersName(method);
                    Class<?>[] parametersType = method.getParameterTypes();
                    Object[] args=new Object[parametersName.length];

                    for (int i = 0; i < parametersName.length; i++) {
                        args[i]=Util.fromStringTo(request.getParameter(parametersName[i]), parametersType[i]);
                    }

                    if (args.length == 0) {
                        returnedObject = method.invoke(instance);
                    } else {
                        returnedObject = method.invoke(instance, args);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (!(returnedObject instanceof String) && !(returnedObject instanceof ModelView)) {
            throw new Exception("Type de retour inconnu au framework");
        }

        return returnedObject;
    }

    public static Object getReturnedObject (Mapping methodMapping, HashMap<String, String> methodParams) throws Exception {
        String className=methodMapping.getClassName();

        Object returnedObject=null;

        Class<?> clazz=null; 
        try {
            clazz=Class.forName(className);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (methodMapping.getMethod()!=null) {
            Method method=methodMapping.getMethod(); 
            
            try {
                if (method!=null) {
                    Object instance = clazz.getConstructor().newInstance();
                    String[] parametersName = Util.getParametersName(method);
                    Class<?>[] parametersType = method.getParameterTypes();
                    Object[] args=new Object[parametersName.length];

                    for (int i = 0; i < parametersName.length; i++) {
                        args[i]=Util.fromStringTo(methodParams.get(parametersName[i]), parametersType[i]);
                    }

                    if (args.length == 0) {
                        returnedObject = method.invoke(instance);
                    } else {
                        returnedObject = method.invoke(instance, args);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (!(returnedObject instanceof String) && !(returnedObject instanceof ModelView)) {
            throw new Exception("Type de retour inconnu au framework");
        }

        return returnedObject;
    }

    public static Mapping getRelatedMapping (HashMap<String, Mapping> methodList, String methodMapping) throws Exception {
        Mapping mapping = methodList.get(methodMapping);

        if (mapping == null) {
            throw new Exception("Aucune méthode associé à l'URL "+methodMapping);
        }

        return mapping;
    }

    // Retourne l'annotation elle-même, sinon retourne null
    public static Annotation isAnnotedParam (Parameter parameter) throws Exception {
        Annotation[] annotations = parameter.getAnnotations();

        // System.out.println("Nombre d'annotation:"+annotations.length);

        Annotation annotation = null;

        for (int i = 0; i < annotations.length; i++) {
            // System.out.println(Param.class);
            if (annotations[i].annotationType().equals(Param.class)) {
                return annotations[i];
            }
        }

        return annotation;
    }

    public static String[] getParametersName (Mapping mapping) throws Exception {
        ArrayList<String> pamametersName = new ArrayList<String>();
        Class<?> clazz  = Class.forName(mapping.getClassName());

        Method concernedMethod = null;
        Method[] methods = clazz.getDeclaredMethods();

        for (int i = 0; i < methods.length; i++) {
            if (methods[i].getName().compareTo(mapping.getMethodName()) == 0) {
                concernedMethod = methods[i];
            }
        }
        
        Parameter[] parameters = new Parameter[0];

        if (concernedMethod != null) {
            parameters = concernedMethod.getParameters();
        }

        for (int i = 0; i < parameters.length; i++) {
            if (Util.isAnnotedParam(parameters[i])!=null)
            {
                pamametersName.add(((Param)Util.isAnnotedParam(parameters[i])).name());
            } else {
                pamametersName.add(parameters[i].getName());
            }
        }

        return pamametersName.toArray(new String[0]);
    }

    public static String[] getParametersName (Method concernedMethod) throws Exception {
        ArrayList<String> pamametersName = new ArrayList<String>();

        Parameter[] parameters = new Parameter[0];

        if (concernedMethod != null) {
            parameters = concernedMethod.getParameters();
        }

        for (int i = 0; i < parameters.length; i++) {
            if (Util.isAnnotedParam(parameters[i])!=null)
            {
                pamametersName.add(((Param)Util.isAnnotedParam(parameters[i])).name());
            } else {
                pamametersName.add(parameters[i].getName());
            }
        }

        return pamametersName.toArray(new String[0]);
    }

    public static Object fromStringTo (String source, Object result) throws Exception //transforme un string en l'objet demandé (limité au type de base déjà mentionné dans le rule.txt)
    {
        if (result instanceof LocalDate)
        {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate date = LocalDate.parse(source, formatter);

            return date;
        }
        else if (result instanceof Date) {
            return Date.valueOf(source);
        }
        else if (result instanceof Integer)
        {
            return Integer.valueOf(Integer.parseInt(source));
        }
        else if (result instanceof Double)
        {
            return Double.valueOf(Double.parseDouble(source));
        }
        else if (result instanceof Float)
        {
            return Float.valueOf(Float.parseFloat(source));
        }
        else if (result instanceof Boolean)
        {
            if (source.equals("true"))
            {
                return true;
            }
            else if (source.equals("false"))
            {
                return false;
            }
        }
        else if (result instanceof String)
        {
            if (!source.contains("'"))
            {
                return new String(source);
            }
            else 
            {
                return new String(source.substring(1, source.length()-1)); //on ajoute les '' pour povoir mettre des espaces dans les Strings sans que la fonction cleanUp ne les enlève
            }
        }

        return null;
    }

    public static Object fromStringTo (String source, Class<?> clazz) throws Exception //transforme un string en l'objet demandé (limité au type de base déjà mentionné dans le rule.txt)
    {
        if (source != null) {
            if (clazz.equals(LocalDate.class))
            {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                LocalDate date = LocalDate.parse(source, formatter);

                return date;
            }
            else if (clazz.equals(Date.class)) {
                return Date.valueOf(source);
            }
            else if (clazz.equals(Integer.class))
            {
                return Integer.valueOf(Integer.parseInt(source));
            }
            else if (clazz.equals(Double.class))
            {
                return Double.valueOf(Double.parseDouble(source));
            }
            else if (clazz.equals(Float.class))
            {
                return Float.valueOf(Float.parseFloat(source));
            }
            else if (clazz.equals(Boolean.class))
            {
                Boolean.valueOf(source);
            }
            else if (clazz.equals(String.class))
            {
                if (!source.contains("'"))
                {
                    return new String(source);
                }
                else 
                {
                    return new String(source.substring(1, source.length()-1)); //on ajoute les '' pour povoir mettre des espaces dans les Strings sans que la fonction cleanUp ne les enlève
                }
            }
        }

        return source;
    }
}
