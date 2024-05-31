package mg.controller;

import annotations.*;
import mg.controller.MyReflect;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import jakarta.servlet.*;
import java.io.*;
import java.lang.reflect.Method;
import java.util.*;
import java.lang.annotation.*;

public class FrontController extends HttpServlet{
//Arguments
    String packageName;
    HashMap<String,String> map=new HashMap<>();
    //Getters Setters
    
//Fonctions

    @Override
    public void init(ServletConfig config)throws ServletException{
        super.init(config);
        try{
        packageName = config.getInitParameter("packageName");
        MyReflect.scanMethod(packageName,map);
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String url=request.getRequestURL().toString();
        PrintWriter out = response.getWriter();
        try{
            if(map.containsKey(url)){
                out.println("L'url donnee est: "+url);
                // out.println("La classe contenant la methode est: "+map.get(url).getClassName());
                // out.println("La methode est: "+map.get(url).getMethodName());
                out.println("La valeur retournée par cette methode est: "+map.get(url));
            }
            else{
            out.println("Ce chemin n'est associe a aucune methode");
            }
        }
        catch(Exception e){
            out.println(e.getMessage());
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request,response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }
}