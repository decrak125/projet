package mg.itu.framework.objects;

import java.io.IOException;
import java.io.PrintWriter;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import mg.itu.framework.utils.Reflect;

public class Mapping {
    String className;
    String methodName;

    public String getClassName() {
        return className;
    }
    public void setClassName(String className) {
        this.className = className;
    }
    public String getMethodName() {
        return methodName;
    }
    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public Mapping(String className, String methodName) {
        this.className = className;
        this.methodName = methodName;
    }

    public void execute(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
        PrintWriter out=resp.getWriter();
        try{
            Class<?> class1= Class.forName(className);
            Object caller= class1.getConstructor().newInstance();
            
            Object result=Reflect.execMeth(caller, methodName, null, null);

            if(result instanceof String){
                out.println(result.toString());
            }
            else if(result instanceof ModelView){
                ModelView mv = (ModelView)result;
                mv.dispatchRequest(req, resp);
            }
            else{
                out.println("La methode doit retourner soit un modele valide soit un String");
                return;
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
        
    }
}
