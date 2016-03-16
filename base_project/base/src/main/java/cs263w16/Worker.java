// The Worker servlet should be mapped to the "/worker" URL.

package cs263w16;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;
import java.util.logging.*;
import com.google.appengine.api.datastore.*;
import com.google.appengine.api.memcache.*;
import com.google.appengine.api.channel.*;

public class Worker extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String line = request.getParameter("line");
        String col = request.getParameter("col");
        String heap = request.getParameter("heap");
        String result = request.getParameter("result");
        // Do something with key.
        MemcacheService syncCache = MemcacheServiceFactory.getMemcacheService();

        /*Entity tne=new Entity("TaskData",keyname);
        tne.setProperty("value",value);
        Date date = new Date();
        tne.setProperty("date",date);
        datastore.put(tne);*/

        syncCache.put("line",line);
        syncCache.put("col",col);
        syncCache.put("heap",heap);
        syncCache.put("result",result);
        if (syncCache.contains("userList")){
            ChannelService channelService = ChannelServiceFactory.getChannelService();

            List ul = (List)syncCache.get("userList");
            for(Iterator i = ul.iterator();i.hasNext(); ){
                String username = (String) i.next();
                channelService.sendMessage(new ChannelMessage(username, heap));
            }
        }
    }
}
