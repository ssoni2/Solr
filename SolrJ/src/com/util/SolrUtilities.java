package com.util;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
 
public class SolrUtilities {
 private static final String solrDirectory = "C:/MyStuff/IR/SOLR/apache-solr-4.0.0/apache-solr-4.0.0";
 
 public static void startSolrServer() {
  try {
   String[] command = { "cmd", };
   Process p = Runtime.getRuntime().exec(command);
   new Thread(new SyncPipe(p.getErrorStream(), System.err)).start();
   new Thread(new SyncPipe(p.getInputStream(), System.out)).start();
   PrintWriter stdin = new PrintWriter(p.getOutputStream());
   //stdin.println("cd /d D:");
   stdin.println("cd " + solrDirectory + "/example");
   stdin.println("java -jar start.jar");
   stdin.close();
 
  } catch (IOException e) {
   // TODO Auto-generated catch block
   e.printStackTrace();
  }
 }
 
 public static boolean isSolrServerAlive() {
 
  try {
   LoggedPrintStream lpsOut = LoggedPrintStream.create(System.out);
   LoggedPrintStream lpsErr = LoggedPrintStream.create(System.err);
 
   System.setOut(lpsOut);
   System.setErr(lpsErr);
 
   String[] command = { "cmd", };
   Process p = Runtime.getRuntime().exec(command);
   new Thread(new SyncPipe(p.getInputStream(), System.out)).start();
   new Thread(new SyncPipe(p.getInputStream(), System.out)).start();
   PrintWriter stdin = new PrintWriter(p.getOutputStream());
   stdin.println("netstat -o -n -a | findstr 0:8983");
   stdin.flush();
   while (lpsOut.buf.lastIndexOf("findstr") == -1) {
    System.out.println("lpsOut.buf = " + lpsOut.buf);
    try {
     Thread.sleep(250);
    } catch (InterruptedException e) {
     e.printStackTrace();
    }
   }
   System.setOut(lpsOut.underlying);
   stdin.close();
   if (lpsOut.buf.indexOf("LISTENING") == -1) {
    return false;
   } else {
    return true;
   }
 
  } catch (Exception e) {
   e.printStackTrace();
   return false;
  }
 
 }
 
 public static void closeSolrServer() {
 
  try {
   LoggedPrintStream lpsOut = LoggedPrintStream.create(System.out);
   System.setOut(lpsOut);
 
   String[] command = { "cmd", };
   Process p = Runtime.getRuntime().exec(command);
   new Thread(new SyncPipe(p.getErrorStream(), System.err)).start();
   new Thread(new SyncPipe(p.getInputStream(), System.out)).start();
   OutputStream outputStream = p.getOutputStream();
   PrintWriter stdin = new PrintWriter(outputStream);
   stdin.println("netstat -o -n -a | findstr 0:8983");
   stdin.flush();
   while (lpsOut.buf.lastIndexOf("findstr") == -1) {
    try {
     Thread.sleep(200);
    } catch (InterruptedException e) {
     e.printStackTrace();
    }
   }
   System.setOut(lpsOut.underlying);
 
   if (lpsOut.buf.indexOf("LISTENING") == -1) {
    System.err.println("SolrUtilities 62 the Solr server is already closed");
    return;
   } else {
    String thePIDString = lpsOut.buf.substring(
      lpsOut.buf.indexOf("LISTENING") + 9).trim();
    int i = 0;
    while (0 <= thePIDString.charAt(i) - '0'
      && thePIDString.charAt(i) - '0' <= 9)
     i++;
    thePIDString = thePIDString.substring(0, i);
    stdin.println("taskkill /f /pid " + thePIDString);
   }
   stdin.close();
 
  } catch (Exception e) {
   // TODO Auto-generated catch block
   e.printStackTrace();
  }
 
 }
  
 public static void main(String[] args) {
 
  boolean isSolrAlive = isSolrServerAlive();
  System.out.println("SolrUtilities.main() 122 isSolrServerAlive? " + isSolrAlive);
   
  if (!isSolrAlive) {
   startSolrServer();
   System.out.println("SolrUtilities.main() the Solr server is started");
  }
   
  /*try {
   Thread.sleep(30000);
  } catch (InterruptedException e) {
   // TODO Auto-generated catch block
   e.printStackTrace();
  }
   
  closeSolrServer();
  System.out.println("SolrUtilities.main() the Solr server is closed");*/
   
 }
}
