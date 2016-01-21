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
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("text/html");
        resp.getWriter().println("<html><body>");
        resp.getWriter().println("<h2>Hello World</h2>"); //remove this line

        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        
        String keyname=req.getParameter("keyname");
        String value=req.getParameter("value");

        Enumeration<String> e=req.getParameterNames();
        Boolean flag=true;
        for (;e.hasMoreElements();){
            String s=e.nextElement();
            if (!s.equals("keyname")&&!s.equals("value")){
                //resp.getWriter().println(s);
                resp.getWriter().println("<h2>error:unknown parameter</h2>");
                flag=false;
                break;
            }
        }
        if (flag){
            if (keyname==null && value==null){
                Query q = new Query("TaskData");
                PreparedQuery pq = datastore.prepare(q);
                for (Entity result : pq.asIterable()){
                    Key k=result.getKey();
                    String nkeyname=k.getName();
                    //String nkeyname=(String) result.getProperty("keyname");
                    String nvalue = (String) result.getProperty("value");
                    String out = String.format("<h2>name: %s, value: %s</h2>",nkeyname,nvalue);
                    resp.getWriter().println(out);
                }
            }
            else if (keyname!=null && value==null){
                try{
                    Key k=KeyFactory.createKey("TaskData",keyname);
                    Entity en=datastore.get(k);
                    String nvalue = (String) en.getProperty("value");
                    String out = String.format("<h2>name: %s, value: %s</h2>",keyname,nvalue);
                    resp.getWriter().println(out);
                } catch (EntityNotFoundException err){
                    resp.getWriter().println("<h2>error: EntityNotFound</h2>");
                }

                //resp.getWriter().println("<h2>name: %s, value: %s</h2>",keyname,value);
            }
            else if (keyname!=null && value!=null){
                Entity tne=new Entity("TaskData",keyname);
                tne.setProperty("value",value);
                Date date = new Date();
                tne.setProperty("date",date);
                datastore.put(tne);
                String out = String.format("<h2>Store %s and %s in Datastore</h2>",keyname,value);
                resp.getWriter().println(out);
                //resp.getWriter().println("<h2>Store %s and %s in Datastore</h2>",keyname,value);
            }
            else {
                resp.getWriter().println("<h2>error:unknown parameter</h2>");
            }
        }
        //Add your code here

        resp.getWriter().println("</body></html>");
    }
}
