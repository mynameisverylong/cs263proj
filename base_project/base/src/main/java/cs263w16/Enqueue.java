// The Enqueue servlet should be mapped to the "/enqueue" URL.
package cs263w16;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;
import java.util.logging.*;
import com.google.appengine.api.datastore.*;
import com.google.appengine.api.memcache.*;

import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions;

public class Enqueue extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String line = request.getParameter("line");
        String col = request.getParameter("col");
        String heap = request.getParameter("heap");
        String result = request.getParameter("result");
        if (result==null)
        	result="noresult";
        // Add the task to the default queue.
        Queue queue = QueueFactory.getDefaultQueue();
        queue.add(TaskOptions.Builder.withUrl("/worker").param("line", line).param("col",col).param("heap",heap).param("result",result));
    }
}
