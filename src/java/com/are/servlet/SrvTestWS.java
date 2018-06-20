package com.are.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.are.sofatec.ConectorHttp;

/**
 * Servlet implementation class SrvTestWS
 */
@WebServlet("/SrvTestWS")
public class SrvTestWS extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public SrvTestWS() {
		super();
		// TODO Auto-generated constructor stub
	}

	protected void ProcesarPeticion(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html;charset=ISO-8859-1");
		PrintWriter out = response.getWriter();

		String nic = request.getParameter("nic");

		String parametro = "<?xml version=\"1.0\" encoding=\"utf-8\"?>"
				+ "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:ws=\"https://webservices.electricaribe.com/ws_sicomercial/\">"
				+ "<soapenv:Header/>" + "<soapenv:Body>"
				+ "<ws:ConsultaIdCobro>" + "<ws:nic>2318803</ws:nic>"
				+ "</ws:ConsultaIdCobro>" + "</soapenv:Body>"
				+ "</soapenv:Envelope>";

		String url = "https://servicios.electricaribe.com/wssistemacomercial/SistemaComercialServicios.asmx";
		String contentType = "text/xml; charset=utf-8";

		InputStream in = null;
		ConectorHttp con = new ConectorHttp();
		try {
			in = con.MetodoPOST(parametro, url, contentType);
			
			BufferedReader reader = new BufferedReader(new InputStreamReader(in,"utf-8"));
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}
			
			out.print(sb.toString());
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (Exception exc) {
				}
			}
		}

	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		this.ProcesarPeticion(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		this.ProcesarPeticion(request, response);
	}

}
