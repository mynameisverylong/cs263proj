package cs263w16;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;
import java.util.logging.*;
import com.google.appengine.api.datastore.*;
import com.google.appengine.api.memcache.*;

@SuppressWarnings("serial")
public class Monitor extends HttpServlet {
    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("text/html");
        resp.getWriter().println("<html><body>");
        resp.getWriter().println("<h2>Hello World</h2>"); //remove this line

        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        MemcacheService syncCache = MemcacheServiceFactory.getMemcacheService();

        //String keyname=req.getParameter("keyname");
        //String value=req.getParameter("value");
        String prog = req.getParameter("program");


        String en=(String)syncCache.get("line");
        String out = String.format("<h2>line: %s</h2>",en);
        resp.getWriter().println(out);

        String en1=(String)syncCache.get("col");
        String out1 = String.format("<h2>col: %s</h2>",en1);
        resp.getWriter().println(out1);

        String en2=(String)syncCache.get("heap");
        String out2 = String.format("<h2>heap: %s</h2>",en2);
        resp.getWriter().println(out2);

        String en3=(String)syncCache.get("result");
        String out3 = String.format("<h2>result: </br>%s</h2>",en3);
        resp.getWriter().println(out3);
        
        if (syncCache.contains("userList")){
            List en6=(List)syncCache.get("userList");
            String out6 = String.format("<h2>userList: </br>%s</h2>",en6.toString());
            resp.getWriter().println(out6);
        }
        
        try{
            Key k=KeyFactory.createKey("TaskData","program");
            Entity en4=datastore.get(k);
            String nvalue = (String) en4.getProperty("code");
            String out4 = String.format("<h2>%s</h2>",nvalue);
            resp.getWriter().println(out4);
        }
        catch (EntityNotFoundException err){
            resp.getWriter().println("<h2>error</h2>");
        }
        //String nvalue = (String) en.getProperty("value");

        //Enumeration<String> e=req.getParameterNames();
        //Boolean flag=true;
        /*for (;e.hasMoreElements();){
            String s=e.nextElement();
            if (!s.equals("keyname")&&!s.equals("value")){
                //resp.getWriter().println(s);
                resp.getWriter().println("<h2>error:unknown parameter</h2>");
                flag=false;
                break;
            }
        }*/
        /*Entity tne=new Entity("TaskData","program");
        tne.setProperty("code",prog);
        Date date = new Date();
        tne.setProperty("date",date);
        datastore.put(tne);*/
        /*if (flag){
            if (keyname==null && value==null){
                Query q = new Query("TaskData");
                PreparedQuery pq = datastore.prepare(q);
                for (Entity result : pq.asIterable()){
                    Key k=result.getKey();
                    String nkeyname=k.getName();
                    String nvalue = (String) result.getProperty("value");
                    String out = String.format("<h2>name: %s, value: %s</h2>",nkeyname,nvalue);
                    //resp.getWriter().println(out);
                    if (syncCache.contains(nkeyname)){
                        out+="(Memcache)";
                    }
                    resp.getWriter().println(out);
                }
            }
            else if (keyname!=null && value==null){
                try{
                    Key k=KeyFactory.createKey("TaskData",keyname);
                    //Entity en=datastore.get(k);
                    //String nvalue = (String) en.getProperty("value");
                    //String out = String.format("<h2>name: %s, value: %s</h2>",keyname,nvalue);
                    if (syncCache.contains(keyname)){
                        Entity en=(Entity)syncCache.get(keyname);
                        String nvalue = (String) en.getProperty("value");
                        String out = String.format("<h2>name: %s, value: %s(Both)</h2>",keyname,nvalue);
                        resp.getWriter().println(out);
                    }
                    else {
                        Entity en=datastore.get(k);
                        String nvalue = (String) en.getProperty("value");
                        String out = String.format("<h2>name: %s, value: %s(Datastore)</h2>",keyname,nvalue);
                        resp.getWriter().println(out);
                        //String nvalue = (String) en.getProperty("value");
                        syncCache.put(keyname,en);
                    }
                    //String nvalue = (String) en.getProperty("value");
                    //String out = String.format("<h2>name: %s, value: %s</h2>",keyname,nvalue);
                    //resp.getWriter().println(out);
                } catch (EntityNotFoundException err){
                    resp.getWriter().println("<h2>Neither</h2>");
                }
                //resp.getWriter().println("<h2>name: %s, value: %s</h2>",keyname,value);
            }
            else if (keyname!=null && value!=null){
                Entity tne=new Entity("TaskData",keyname);
                tne.setProperty("value",value);
                Date date = new Date();
                tne.setProperty("date",date);
                datastore.put(tne);
                
                syncCache.put(keyname,tne);

                String out = String.format("<h2>Store %s and %s in Memcache</h2>",keyname,value);
                resp.getWriter().println(out);
                //resp.getWriter().println("<h2>Store %s and %s in Datastore</h2>",keyname,value);
            }
            else {
                resp.getWriter().println("<h2>error:unknown parameter</h2>");
            }
        }
        //Add your code here*/

        resp.getWriter().println("</body></html>");
    }
}
