/*package com.vassarlabs.soap.scraper;

import java.io.File;
import java.io.FileInputStream;
import java.util.Iterator;

import javax.xml.namespace.QName;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.MimeHeaders;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPBodyElement;
import javax.xml.soap.SOAPConnection;
import javax.xml.soap.SOAPConnectionFactory;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.google.gson.Gson;

public class SOAPClientSAAJ {

	*//**
	 * Starting point for the SAAJ - SOAP Client Testing
	 *//*
	public static void main(String args[]) {
		try {
			// Create SOAP Connection
			SOAPConnectionFactory soapConnectionFactory = SOAPConnectionFactory.newInstance();
			SOAPConnection soapConnection = soapConnectionFactory.createConnection();
			String url = "http://45.114.143.88/AreaSownServiceDetails/serviceDetails.php";
			
			
			String excelFilePath = "/home/srikanth/Downloads/Masters.xlsx";
	        FileInputStream inputStream = new FileInputStream(new File(excelFilePath));
	         
	        Workbook workbook = new XSSFWorkbook(inputStream);
	        Sheet firstSheet = workbook.getSheetAt(2);
	        Iterator<Row> iterator = firstSheet.iterator();
	        String villageId;
	        //dont parse header row
	        iterator.next();
	        int i=0;
	        {
	        	HSSFWorkbook writeWorkbook = new HSSFWorkbook();
	    		HSSFSheet sheet = writeWorkbook.createSheet("Data Sheet");
	    		Row headerRow = sheet.createRow(0);
	    		Cell cell = headerRow.createCell(0);
	    		cell.setCellValue("VCode");
	    		headerRow.createCell(1).setCellValue("Village Name");
	    		// headerRow.createCell(2).setCellValue("Village");
	    		headerRow.createCell(2).setCellValue("Date");
	    		headerRow.createCell(3).setCellValue("Soil Moisture Percent");
	    		headerRow.createCell(4).setCellValue("Rainfall (mm) ");
	        }
	        while (i++<10) {
	            Row nextRow = iterator.next();
	            villageId = nextRow.getCell(1).toString();
	            SOAPMessage soapResponse = soapConnection.call(createSOAPRequest(villageId), url);
				NodeList data = soapResponse.getSOAPPart().getElementsByTagName("c");
				Node res = data.item(0);
				System.out.println(" res"+res.getTextContent());
				// Process the SOAP Response
				//printSOAPResponse(soapResponse);

				Gson gson = new Gson();
				ParentClass list = gson.fromJson(res.getTextContent(), ParentClass.class);
				for(AreaSownData area : list.getList()){
					System.out.println(area.toString());
				}
	        }
	        inputStream.close();
			// Send SOAP Message to SOAP Server
			
			
			soapConnection.close();
		} catch (Exception e) {
			System.err.println("Error occurred while sending SOAP Request to Server");
			e.printStackTrace();
		}
	}

	private static SOAPMessage createSOAPRequest(String villageId) throws Exception {
		MessageFactory messageFactory = MessageFactory.newInstance();
		SOAPMessage soapMessage = messageFactory.createMessage();
		SOAPPart soapPart = soapMessage.getSOAPPart();

		String serverURI = "http://45.114.143.88/AreaSownServiceDetails/serviceDetails.php";

		// SOAP Envelope
		SOAPEnvelope envelope = soapPart.getEnvelope();
		// envelope.addNamespaceDeclaration("example", serverURI);

		
		 * Constructed SOAP Request Message: 
		 * <SOAP-ENV:Envelope
		 * xmlns:SOAP-ENV="http://schemas.xmlsoap.org/soap/envelope/"
		 * xmlns:SOAP-ENC="http://schemas.xmlsoap.org/soap/encoding/"
		 * xmlns:tns="http://schemas.xmlsoap.org/soap/encoding/"><SOAP-ENV:
		 * Header/><SOAP-ENV:Body><m:getAreaSown xmlns:m=
		 * "http://45.114.143.88/AreaSownServiceDetails/serviceDetails.php"><
		 * UserName>administrator</UserName><Password>administrator@1</Password>
		 * <Village>2039</Village></m:getAreaSown></SOAP-ENV:Body></SOAP-ENV:
		 * Envelope>
		 * 
		 

		// SOAP Body
		envelope.addNamespaceDeclaration("SOAP-ENC", "http://schemas.xmlsoap.org/soap/encoding/");
		envelope.addNamespaceDeclaration("tns", "http://schemas.xmlsoap.org/soap/encoding/");
		SOAPBody soapBody = envelope.getBody();
		// envelope.addNamespaceDeclaration("SOAP-ENC",
		// "http://schemas.xmlsoap.org/soap/encoding/");
		QName bodyName = new QName("http://45.114.143.88/AreaSownServiceDetails/serviceDetails.php", "getAreaSown",
				"m");
		SOAPBodyElement bodyElement = soapBody.addBodyElement(bodyName);

		SOAPElement soapBodyElem = bodyElement.addChildElement("UserName");
		soapBodyElem.addTextNode("administrator");
		SOAPElement soapBodyElem1 = bodyElement.addChildElement("Password");
		soapBodyElem1.addTextNode("administrator@1");
		SOAPElement soapBodyElem2 = bodyElement.addChildElement("Village");
		soapBodyElem2.addTextNode(villageId);
		MimeHeaders headers = soapMessage.getMimeHeaders();
		headers.addHeader("SOAPAction", "http://45.114.143.88/AreaSownServiceDetails/serviceDetails.php/getAreaSown");

		soapMessage.saveChanges();

		 Print the request message 
		System.out.println("Request SOAP Message = ");
		soapMessage.writeTo(System.out);
		System.out.println();

		return soapMessage;
	}

	*//**
	 * Method used to print the SOAP Response
	 *//*
	private static void printSOAPResponse(SOAPMessage soapResponse) throws Exception {
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		Source sourceContent = soapResponse.getSOAPPart().getContent();
		
		System.out.println("\nResponse SOAP Message = ");
		StreamResult result = new StreamResult(System.out);
		transformer.transform(sourceContent, result);
	}
}
*/