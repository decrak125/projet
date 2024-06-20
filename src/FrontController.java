package servlet;
import java.io.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;
import java.lang.reflect.Method;
import util.Util;
import util.Mapping;
import util.ModelView;
import annotation.Controller;
import annotation.Get;
public class FrontController extends HttpServlet {
    String controllerPackage;
    List<Class<?>> controllerClasses=new ArrayList<>();
    HashMap<String, Mapping> methodList=new HashMap<String, Mapping>();

    public void initVariables () throws Exception {
        controllerPackage = this.getInitParameter("controller-package");
        controllerClasses = Util.scanPackage(controllerPackage, Controller.class);
        methodList = Util.getAnnotedMethods(controllerClasses, Get.class);
    }

    @Override
    public void init() throws ServletException {
        try {
            initVariables();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void processRequest (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        out.println("<HTML>");
        out.println("<BODY>");

        String url=request.getRequestURI();

        int index=url.lastIndexOf("/");
        String methodMapping=url.substring(index, url.length());
        Mapping mapping=null; 

        try {
            mapping = Util.getRelatedMapping(methodList, methodMapping);
        } catch (Exception e) {
            e.printStackTrace(out);
        }

        if (mapping!=null) {
            try {
                Object returnedObject=Util.getReturnedObject(mapping);

                if (returnedObject instanceof String) {
                    out.println("<h2>Méthode associée à l'url "+methodMapping+":</h2>");
                    out.println("<p>Classe: "+mapping.getClassName()+"/ Méthode:"+mapping.getMethodName()+"</p>");

                    out.println("<p>Retour de la fonction: "+returnedObject+" </p>");
                } else if (returnedObject instanceof ModelView) {
                    ModelView modelView=(ModelView)returnedObject;
                    HashMap<String, Object> datas=modelView.getData();
                    
                    for (Map.Entry<String, Object> data : datas.entrySet()) {
                        request.setAttribute(data.getKey(), data.getValue());
                    }

                    RequestDispatcher dispatcher=request.getRequestDispatcher(modelView.getDestinationURL());
                    dispatcher.forward(request, response);
                }
            } catch (Exception e) {
                e.printStackTrace(out);
            }
        } else {
            out.println("<p>Aucune méthode associée à l'url "+methodMapping+", vérifier vos annotations</p>");
        }
        out.println("</BODY></HTML>"); 
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }
}
