package cs263w16;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;
import java.util.logging.*;
import com.google.appengine.api.datastore.*;
import com.google.appengine.api.memcache.*;

@SuppressWarnings("serial")
public class DatastoreServlet extends HttpServlet {
    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        /*resp.setContentType("text/html");
        resp.getWriter().println("<html><body>");
        resp.getWriter().println("<h2>Hello World</h2>"); //remove this line*/

        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        MemcacheService syncCache = MemcacheServiceFactory.getMemcacheService();

        //String keyname=req.getParameter("keyname");
        //String value=req.getParameter("value");

        String prog = req.getParameter("program");
        
        String port = req.getParameter("port");
        String ip = req.getRemoteAddr();
        Entity ipt = new Entity("TaskData","ip");
        ipt.setProperty("ipadd",ip);
        datastore.put(ipt);

        Entity tne=new Entity("TaskData","program");
        tne.setProperty("code",prog);
        Date date = new Date();
        tne.setProperty("date",date);
        datastore.put(tne);
    }
}
