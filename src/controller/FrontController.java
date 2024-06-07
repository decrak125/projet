package mg.itu.framework.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URI;
import java.util.HashMap;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import mg.itu.framework.objects.Mapping;
import mg.itu.framework.utils.ControlUtils;

public class FrontController extends HttpServlet{
    HashMap<String,Mapping> routeHashMap;
    
    @Override
    public void init() throws ServletException {
        super.init();

        initVariables();
    }

    public void initVariables(){
        ControlUtils instUtils=new ControlUtils();

        String packageName=getInitParameter("controllerspackage");

        this.routeHashMap=instUtils.getRoutes(packageName);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processRequest(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processRequest(req, resp);
    }

    protected void processRequest(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter out = resp.getWriter();
        URI requestURI=URI.create(req.getRequestURI());
        String key=requestURI.getPath();
        key=key.substring(key.indexOf("/", 1));

        if(routeHashMap.containsKey(key)){
            Mapping found=routeHashMap.get(key);

            found.execute(req, resp);
        }
        else{
            out.println("Ce lien n'est associé à aucune méthode");
        }
    }
}