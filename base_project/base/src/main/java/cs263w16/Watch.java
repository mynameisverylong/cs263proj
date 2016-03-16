package cs263w16;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Context;

import javax.servlet.*;
import javax.inject.Inject;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;
import java.util.logging.*;
import com.google.appengine.api.datastore.*;
import com.google.appengine.api.memcache.*;
import com.google.appengine.api.channel.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;


// Plain old Java Object it does not extend as class or implements 
// an interface

// The class registers its methods for the HTTP GET request using the @GET annotation. 
// Using the @Produces annotation, it defines that it can deliver several MIME types,
// text, XML and HTML. 

// The browser requests per default the HTML MIME type.

//Sets the path to base URL + /hello
@Path("/watch")
public class Watch {

  @Context
  HttpServletResponse resp;
  // This method is called if TEXT_PLAIN is request
  @GET
  @Produces(MediaType.TEXT_PLAIN)
  public String sayPlainTextHello() {
    return "Hello Jersey";
  }

  // This method is called if XML is request
  @GET
  @Produces(MediaType.TEXT_XML)
  public String sayXMLHello() {
    return "<?xml version=\"1.0\"?>" + "<hello> Hello Jersey" + "</hello>";
  }

  // This method is called if HTML is request
  @GET
  @Produces(MediaType.TEXT_HTML)
  //public String sayHtmlHello(@Context HttpServletResponse response) {
  public String sayHtmlHello() {
    String index="";
    try{
      File file = new File("watch.html");

      StringBuilder sb = new StringBuilder();
      String s ="";
      BufferedReader br = new BufferedReader(new FileReader(file));

      while( (s = br.readLine()) != null) {
      sb.append(s + "\n");
      }

      br.close();
      index = sb.toString();
    }
    catch (Exception e){
      index = e.toString();
    }
    String base = "abcdefghijklmnopqrstuvwxyz0123456789";   
    Random random = new Random();   
    StringBuffer sb = new StringBuffer();
    for (int i = 0; i < 30; i++) {   
        int number = random.nextInt(base.length());   
        sb.append(base.charAt(number));   
    }
    String userId=sb.toString();
    ChannelService channelService = ChannelServiceFactory.getChannelService();
    String token = channelService.createChannel(userId);
    index = index.replaceAll("\\{\\{ token \\}\\}", token);
    MemcacheService syncCache = MemcacheServiceFactory.getMemcacheService();
    try{
        Key k=KeyFactory.createKey("TaskData","program");
        Entity en4=datastore.get(k);
        String nvalue = (String) en4.getProperty("code");
        index = index.replaceAll("\\{\\{ code \\}\\}", nvalue);
    }
    catch (EntityNotFoundException err){
        resp.getWriter().println("<h2>error</h2>");
    }

//<<<<<<< Updated upstream
    if (syncCache.contains("userList")){
      List usrlist = (List)syncCache.get("userList");
      usrlist.add(userId);
      syncCache.put("userList",usrlist);
    }
    else {
      List usrlist = new ArrayList();
      usrlist.add(userId);
      syncCache.put("userList",usrlist);
    }
//=======
    
    
//>>>>>>> Stashed changes
    return index;
  }

} 