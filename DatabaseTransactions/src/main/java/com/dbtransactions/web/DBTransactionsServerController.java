/*
 * Copyright notice: CreativeHummers@gmail.com Restricted for now. Will consider any other license after Project completion
 * @author: Sudhakar Krishnamachari
 */
package com.dbtransactions.web;

import com.dbtransactions.DatabaseTransactions;
import com.dbtransactions.enums.DBName;
import lombok.extern.java.Log;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Log
public class DBTransactionsServerController extends HttpServlet
{
 
	public void init(ServletConfig config){
	   //placeholder
	}	

	public void doGet(HttpServletRequest request,HttpServletResponse response) 
	                     throws ServletException {
		log.info("doGet initiated: " );
		response.setContentType("text/html");
		doHandleRequest( request , response );
	}

	public void doHandleRequest(HttpServletRequest request, HttpServletResponse response)
	{
		try{	
		log.info("doHandleRequest initiated: " );

		ServletOutputStream outputStream = response.getOutputStream();
		//String jsonResponseData = "{ 'msg':'Hello First Servlet Response' , 'response':{ 'rows' : 'All data rows' } }";
		//outputStream.println(jsonResponseData);

		String resultFormat = request.getParameter("jdbcResultFormat");
		resultFormat = resultFormat==null? "PostgreJSON" : resultFormat;

		String tranNumber = request.getParameter("transactionNumber");
		tranNumber = tranNumber==null ? "100" : tranNumber;

		DatabaseTransactions instance = new DatabaseTransactions(DBName.PostGreSQLJDBC, "default"); //("default" , "jndi" , queryType);
		String result = ""; //instance.run( tranNumber, "SELECT", new String[]{});
		
		outputStream.println(result);
		outputStream.flush();	

		} catch(IOException ex){
			log.info("doHandleRequest Error: " + ex.getMessage());
		}
		
	}
	

}
