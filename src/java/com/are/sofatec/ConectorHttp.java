package com.are.sofatec;

import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.PasswordAuthentication;
import java.net.URL;
import java.io.OutputStream;
import java.io.InputStream;

public class ConectorHttp {
	public ConectorHttp()
	  {

	  }

	  public InputStream MetodoPOST(String parametro, String p_url, String contentType)throws Exception
	  {
	    try
	    {

	      URL url = new URL(p_url);

	      HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	      conn.setDoOutput(true);

	      conn.setRequestMethod("POST");
	      conn.setRequestProperty("Content-Type", contentType);

	      //Escribir en la conexi�n
	      OutputStream out = conn.getOutputStream();
	      out.write(parametro.getBytes());
	      out.flush();
	      out.close();

	      //Verificar respuesta
	      int rc = conn.getResponseCode();
	      if (rc == HttpURLConnection.HTTP_OK)
	      {
	        //Se lee la respuesta
	        InputStream in = conn.getInputStream();
	        /*
	        StringBuffer inBuffer = new StringBuffer();
	        int inChar;
	        while ( (inChar = in.read()) != -1) {
	          inBuffer.append( (char) inChar);
	        }
	        in.close();
	        System.out.println(inBuffer);
	        */

	        return in;
	      }
	      else
	      {
	          throw new Exception("Ocurrió un error con el request: " + rc + ", " + conn.getResponseMessage());
	      }
	    }
	    catch( Exception e )
	    {
	      throw(e);
	    }
	  }

	  public InputStream MetodoPOST(String parametro, String p_url, String contentType, final String usuario, final String clave)throws Exception
	  {
	    try
	    {

	      URL url = new URL(p_url);

	      HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	      conn.setDoOutput(true);

	      conn.setRequestMethod("POST");
	      conn.setRequestProperty("Content-Type", contentType);
	      Authenticator au = new Authenticator() {
	          @Override
	          protected PasswordAuthentication
	             getPasswordAuthentication() {
	             return new PasswordAuthentication
	                (usuario, clave.toCharArray());
	          }
	       };
	       Authenticator.setDefault(au);
	       
	      //Escribir en la conexi�n
	      OutputStream out = conn.getOutputStream();
	      out.write(parametro.getBytes());
	      out.flush();
	      out.close();

	      //Verificar respuesta
	      int rc = conn.getResponseCode();
	      if (rc == HttpURLConnection.HTTP_OK)
	      {
	        //Se lee la respuesta
	        InputStream in = conn.getInputStream();
	        /*
	        StringBuffer inBuffer = new StringBuffer();
	        int inChar;
	        while ( (inChar = in.read()) != -1) {
	          inBuffer.append( (char) inChar);
	        }
	        in.close();
	        System.out.println(inBuffer);
	        */

	        return in;
	      }
	      else
	      {
	          throw new Exception("Ocurrió un error con el request: " + rc + ", " + conn.getResponseMessage());
	      }
	    }
	    catch( Exception e )
	    {
	      throw(e);
	    }
	  }
	  
	  public InputStream MetodoGET(String parametro, String p_url)throws Exception
	  {
	    try
	    {
	    	if (!parametro.equals("")) {
	    		p_url = p_url + "?" + parametro;
	    	}
	      URL url = new URL(p_url);

	      //URL url = new URL(p_url + "?" + parametro);

	      HttpURLConnection conn = (HttpURLConnection) url.openConnection();

	      //Verificar respuesta
	      int rc = conn.getResponseCode();
	      if (rc == HttpURLConnection.HTTP_OK)
	      {
	        //Se lee la respuesta
	        InputStream in = conn.getInputStream();	        
	        return in;
	      }
	      else
	      {
	          throw new Exception("Ocurrió un error con el request: " + rc + ", " + conn.getResponseMessage());
	      }
	    }
	    catch( Exception e )
	    {
	      throw(e);
	    }
	  }
	  
	  public InputStream MetodoGET(String parametro, String p_url, String usuario, String clave)throws Exception
	  {
	    try
	    {

	    	if (!parametro.equals("")) {
	    		p_url = p_url + "?" + parametro;
	    	}
	      URL url = new URL(p_url);
	      String userPassword = usuario + ":" + clave;
	      HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	      String encoding = new sun.misc.BASE64Encoder().encode(userPassword.getBytes());
	      conn.setRequestProperty("Authorization", "Basic " + encoding);
	      conn.connect();

	      //Verificar respuesta
	      int rc = conn.getResponseCode();
	      if (rc == HttpURLConnection.HTTP_OK)
	      {
	        //Se lee la respuesta
	        InputStream in = conn.getInputStream();	        
	        return in;
	      }
	      else
	      {
	          throw new Exception("Ocurrió un error con el request: " + rc + ", " + conn.getResponseMessage());
	      }
	    }
	    catch( Exception e )
	    {
	      throw(e);
	    }
	  }
	

}
