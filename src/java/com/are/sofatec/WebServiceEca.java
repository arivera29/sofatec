package com.are.sofatec;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class WebServiceEca {

	private String parametro;
	private String url = "https://servicios.electricaribe.com/wssistemacomercial/SistemaComercialServicios.asmx";
	String contentType = "text/xml; charset=utf-8";

	public String AsociarOrdenesServicio(String sarta) throws ParserConfigurationException, IOException, Exception {
		this.parametro = sarta;
		return this.CallAsociarOrdenesServicio();
	}

	public String ConsultaIdCobro(String nic) throws ParserConfigurationException, IOException, Exception {
		this.parametro = nic;
		return this.CallConsultaIdCobro();
	}

	public String EsClienteMoroso(String nic) throws ParserConfigurationException, IOException, Exception {
		this.parametro = nic;
		return this.CallEsClienteMoroso();
	}

	public String ValorFacturaActual(String nic) throws ParserConfigurationException, IOException, Exception {
		this.parametro = nic;
		return this.CallValorFacturaActual();
	}

	public String ObtenerDatosCliente(String numApa) throws ParserConfigurationException, IOException, Exception {
		this.parametro = numApa;
		return this.CallObtenerDatosCliente();
	}

	private String CallAsociarOrdenesServicio()
			throws ParserConfigurationException, IOException, Exception {
		String respuesta = "";
		InputStream in = null;
		ConectorHttp con = new ConectorHttp();
		in = con.MetodoPOST(this.XmlAsociarOrdenesServicio(), url, contentType);

		// BufferedReader reader = new BufferedReader(new
		// InputStreamReader(in,"utf-8"));
		// StringBuilder sb = new StringBuilder();
		// String line = null;
		// while ((line = reader.readLine()) != null) {
		// sb.append(line);
		// }

		DocumentBuilderFactory fabricaCreadorDocumento = DocumentBuilderFactory
				.newInstance();
		DocumentBuilder creadorDocumento = fabricaCreadorDocumento
				.newDocumentBuilder();
		org.w3c.dom.Document documento = creadorDocumento.parse(in);
		org.w3c.dom.Element raiz = documento.getDocumentElement();

		NodeList Nodos = raiz
				.getElementsByTagName("AsociarOrdenesServicioResponse");
		if (Nodos.getLength() > 0) {
			Node Nodo = Nodos.item(0);
			respuesta = Nodo.getTextContent();

		}

		in.close();

		return respuesta;
	}

	private String CallConsultaIdCobro() throws ParserConfigurationException,
			IOException, Exception {
		String respuesta = "";
		InputStream in = null;
		ConectorHttp con = new ConectorHttp();
		in = con.MetodoPOST(this.XmlConsultaIdCobro(), url, contentType);
		
		//BufferedReader reader = new BufferedReader(new InputStreamReader(in,"utf-8"));
		//StringBuilder sb = new StringBuilder();
		//String line = null;
		//while ((line = reader.readLine()) != null) {
		//	 sb.append(line);
		//}

		
		
		DocumentBuilderFactory fabricaCreadorDocumento = DocumentBuilderFactory.newInstance();
		DocumentBuilder creadorDocumento = fabricaCreadorDocumento.newDocumentBuilder();
		org.w3c.dom.Document documento = creadorDocumento.parse(in);
		org.w3c.dom.Element raiz = documento.getDocumentElement();

		
		NodeList Nodos = raiz.getElementsByTagName("ConsultaIdCobroResponse");
		if (Nodos.getLength() > 0) {
			Node Nodo = Nodos.item(0);
			respuesta = Nodo.getTextContent();
		}

		
		in.close();
		return respuesta;
	}

	private String CallEsClienteMoroso() throws ParserConfigurationException,
			IOException, Exception {
		String respuesta = "";
		InputStream in = null;
		ConectorHttp con = new ConectorHttp();
		in = con.MetodoPOST(this.XmlEsClienteMoroso(), url, contentType);

		// BufferedReader reader = new BufferedReader(new
		// InputStreamReader(in,"utf-8"));
		// StringBuilder sb = new StringBuilder();
		// String line = null;
		// while ((line = reader.readLine()) != null) {
		// sb.append(line);
		// }

		DocumentBuilderFactory fabricaCreadorDocumento = DocumentBuilderFactory
				.newInstance();
		DocumentBuilder creadorDocumento = fabricaCreadorDocumento
				.newDocumentBuilder();
		org.w3c.dom.Document documento = creadorDocumento.parse(in);
		org.w3c.dom.Element raiz = documento.getDocumentElement();

		NodeList Nodos = raiz.getElementsByTagName("EsClienteMorosoResponse");
		if (Nodos.getLength() > 0) {
			Node Nodo = Nodos.item(0);
			respuesta = Nodo.getTextContent();

		}

		in.close();

		return respuesta;
	}

	private String CallValorFacturaActual()
			throws ParserConfigurationException, IOException, Exception {
		String respuesta = "";
		InputStream in = null;
		ConectorHttp con = new ConectorHttp();
		in = con.MetodoPOST(this.XmlValorFacturaActual(), url, contentType);

		// BufferedReader reader = new BufferedReader(new
		// InputStreamReader(in,"utf-8"));
		// StringBuilder sb = new StringBuilder();
		// String line = null;
		// while ((line = reader.readLine()) != null) {
		// sb.append(line);
		// }

		DocumentBuilderFactory fabricaCreadorDocumento = DocumentBuilderFactory
				.newInstance();
		DocumentBuilder creadorDocumento = fabricaCreadorDocumento
				.newDocumentBuilder();
		org.w3c.dom.Document documento = creadorDocumento.parse(in);
		org.w3c.dom.Element raiz = documento.getDocumentElement();

		NodeList Nodos = raiz
				.getElementsByTagName("ValorFacturaActualResponse");
		if (Nodos.getLength() > 0) {
			Node Nodo = Nodos.item(0);
			respuesta = Nodo.getTextContent();

		}

		in.close();

		return respuesta;
	}

	private String CallObtenerDatosCliente()
			throws ParserConfigurationException, IOException, Exception {
		String respuesta = "";
		InputStream in = null;
		ConectorHttp con = new ConectorHttp();
		in = con.MetodoPOST(this.XmlObtenerDatosCliente(), url, contentType);

		// BufferedReader reader = new BufferedReader(new
		// InputStreamReader(in,"utf-8"));
		// StringBuilder sb = new StringBuilder();
		// String line = null;
		// while ((line = reader.readLine()) != null) {
		// sb.append(line);
		// }

		DocumentBuilderFactory fabricaCreadorDocumento = DocumentBuilderFactory
				.newInstance();
		DocumentBuilder creadorDocumento = fabricaCreadorDocumento
				.newDocumentBuilder();
		org.w3c.dom.Document documento = creadorDocumento.parse(in);
		org.w3c.dom.Element raiz = documento.getDocumentElement();

		NodeList Nodos = raiz
				.getElementsByTagName("ObtenerDatosClienteResponse");
		if (Nodos.getLength() > 0) {
			Node Nodo = Nodos.item(0);
			respuesta = Nodo.getTextContent();

		}

		in.close();

		return respuesta;
	}

	private String XmlAsociarOrdenesServicio() {
		String xml = "<?xml version=\"1.0\" encoding=\"utf-8\"?>"
				+ "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:ws=\"https://webservices.electricaribe.com/ws_sicomercial/\">"
				+ "<soapenv:Header/>" + "<soapenv:Body>"
				+ "<ws:AsociarOrdenesServicio>" + "<ws:sarta>"
				+ this.parametro.trim() + "</ws:sarta>"
				+ "</ws:AsociarOrdenesServicio>" + "</soapenv:Body>"
				+ "</soapenv:Envelope>";

		return xml;

	}

	private String XmlConsultaIdCobro() {
		String xml = "<?xml version=\"1.0\" encoding=\"utf-8\"?>"
				+ "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:ws=\"https://webservices.electricaribe.com/ws_sicomercial/\">"
				+ "<soapenv:Header/>" + "<soapenv:Body>"
				+ "<ws:ConsultaIdCobro>" + "<ws:nic>" + this.parametro.trim()
				+ "</ws:nic>" + "</ws:ConsultaIdCobro>" + "</soapenv:Body>"
				+ "</soapenv:Envelope>";

		return xml;

	}

	private String XmlEsClienteMoroso() {
		String xml = "<?xml version=\"1.0\" encoding=\"utf-8\"?>"
				+ "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:ws=\"https://webservices.electricaribe.com/ws_sicomercial/\">"
				+ "<soapenv:Header/>" + "<soapenv:Body>"
				+ "<ws:EsClienteMoroso>" + "<ws:nic>" + this.parametro.trim()
				+ "</ws:nic>" + "</ws:EsClienteMoroso>" + "</soapenv:Body>"
				+ "</soapenv:Envelope>";

		return xml;

	}

	private String XmlValorFacturaActual() {
		String xml = "<?xml version=\"1.0\" encoding=\"utf-8\"?>"
				+ "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:ws=\"https://webservices.electricaribe.com/ws_sicomercial/\">"
				+ "<soapenv:Header/>" + "<soapenv:Body>"
				+ "<ws:ValorFacturaActual>" + "<ws:nic>"
				+ this.parametro.trim() + "</ws:nic>"
				+ "</ws:ValorFacturaActual>" + "</soapenv:Body>"
				+ "</soapenv:Envelope>";

		return xml;

	}

	private String XmlObtenerDatosCliente() {
		String xml = "<?xml version=\"1.0\" encoding=\"utf-8\"?>"
				+ "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:ws=\"https://webservices.electricaribe.com/ws_sicomercial/\">"
				+ "<soapenv:Header/>" + "<soapenv:Body>"
				+ "<ws:ObtenerDatosCliente>" + "<ws:numApa>"
				+ this.parametro.trim() + "</ws:numApa>"
				+ "</ws:ObtenerDatosCliente>" + "</soapenv:Body>"
				+ "</soapenv:Envelope>";

		return xml;

	}

}
