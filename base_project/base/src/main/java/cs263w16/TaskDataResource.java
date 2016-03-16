package cs263w16;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.*;
import java.util.logging.*;
import com.google.appengine.api.datastore.*;
import javax.xml.bind.JAXBElement;
import com.google.appengine.api.memcache.*;

public class TaskDataResource {
  @Context
  UriInfo uriInfo;
  @Context
  Request request;
  String keyname;

  public TaskDataResource(UriInfo uriInfo, Request request, String kname) {
    this.uriInfo = uriInfo;
    this.request = request;
    this.keyname = kname;
  }
  // for the browser
  @GET
  @Produces(MediaType.TEXT_XML)
  public String getTaskDataHTML() {
    //add your code here (get Entity from datastore using this.keyname)
    // throw new RuntimeException("Get: TaskData with " + keyname +  " not found");
    //if not found
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    MemcacheService syncCache = MemcacheServiceFactory.getMemcacheService();

    if (syncCache.contains(this.keyname)){
      Entity tne=(Entity)syncCache.get(this.keyname);
      String value=(String)tne.getProperty("code");
      Date date = (Date)tne.getProperty("date");
      TaskData td = new TaskData(this.keyname,value,date);
      return value;
    }
    Key k=KeyFactory.createKey("TaskData",this.keyname);
    try {
      Entity tne = datastore.get(k);
      syncCache.put(this.keyname,tne);
      String value = (String)tne.getProperty("code");
      Date date = (Date)tne.getProperty("date");
      TaskData td = new TaskData(this.keyname,value,date);
      return value;
    } catch(EntityNotFoundException err){
      throw new RuntimeException("Get: TaskData with "+this.keyname+" not found!");
    }
  }
  // for the application
  @GET
  @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
  public String getTaskData() {
    //same code as above method
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    MemcacheService syncCache = MemcacheServiceFactory.getMemcacheService();

    if (syncCache.contains(this.keyname)){
      Entity tne=(Entity)syncCache.get(this.keyname);
      String value=(String)tne.getProperty("code");
      Date date = (Date)tne.getProperty("date");
      TaskData td = new TaskData(this.keyname,value,date);
      return value;
    }
    Key k=KeyFactory.createKey("TaskData",this.keyname);
    try {
      Entity tne = (Entity)datastore.get(k);
      syncCache.put(this.keyname,tne);
      String value = (String)tne.getProperty("code");
      Date date = (Date)tne.getProperty("date");
      TaskData td = new TaskData(this.keyname,value,date);
      return value;
    } catch(EntityNotFoundException err){
      throw new RuntimeException("Get: TaskData with "+this.keyname+" not found!");
    }
  }

  @PUT
  @Consumes(MediaType.APPLICATION_XML)
  public Response putTaskData(String val) {
    Response res = null;
    //add your code here
    //first check if the Entity exists in the datastore
    //if it is not, create it and 
    //signal that we created the entity in the datastore 
    //res = Response.created(uriInfo.getAbsolutePath()).build();
    //else signal that we updated the entity
    //res = Response.noContent().build();

    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    MemcacheService syncCache = MemcacheServiceFactory.getMemcacheService();

    Key k = KeyFactory.createKey("TaskData",this.keyname);
    try {
      Entity tne = (Entity)datastore.get(k);
      tne.setProperty("value",val);
      Date date = new Date();
      tne.setProperty("date",date);
      datastore.put(tne);
      syncCache.put(this.keyname,tne);
      res = Response.noContent().build();
    }
    catch (EntityNotFoundException err){
      Entity tne = new Entity("TaskData",this.keyname);
      tne.setProperty("value",val);
      Date date = new Date();
      tne.setProperty("date",date);
      datastore.put(tne);
      syncCache.put(this.keyname,tne);
      res = Response.created(uriInfo.getAbsolutePath()).build();
    }



    return res;
  }

  @DELETE
  public void deleteIt() {
    //delete an entity from the datastore
    //just print a message upon exception (don't throw)
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    MemcacheService syncCache = MemcacheServiceFactory.getMemcacheService();

    Key k = KeyFactory.createKey("TaskData",this.keyname);
    try {
      Entity tne = (Entity)datastore.get(k);
      datastore.delete(k);
      if (syncCache.contains(this.keyname))
        syncCache.delete(this.keyname);
    } catch (EntityNotFoundException err){
      System.out.println(this.keyname+" not found in datastore!");
    }
  }

} 
