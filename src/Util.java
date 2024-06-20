package util;
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
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import annotation.Controller;
import annotation.Get;
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

                    // Class<?> returnedType=methods[j].getReturnType();

                    // if (returnedType!=null) {
                    //     if (!returnedType.equals(String.class) && !returnedType.equals(ModelView.class)) {
                    //         throw new Exception("La fonction "+classList.get(i).getName()+"."+methods[j].getName()+" doit soit retourner un String soit un ModelView, trouvé: "+returnedType.getName());
                    //     }
                    // } else {
                    //     throw new Exception("La fonction "+classList.get(i).getName()+"."+methods[j].getName()+" doit soit retourner un String soit un ModelView, trouvé: null");
                    // }

                    methodList.put(((Get)myAnnotation).urlMapping(), new Mapping(classList.get(i).getName(), methods[j].getName()));
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

    public static Mapping getRelatedMapping (HashMap<String, Mapping> methodList, String methodMapping) throws Exception {
        Mapping mapping = methodList.get(methodMapping);

        if (mapping == null) {
            throw new Exception("Aucune méthode associé à l'URL "+methodMapping);
        }

        return mapping;
    }
}
