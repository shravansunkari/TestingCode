/*package com.vassarlabs.soap.scraper;

import java.util.Map;

import javax.xml.namespace.QName;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.MimeHeaders;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPBodyElement;
import javax.xml.soap.SOAPConnection;
import javax.xml.soap.SOAPConnectionFactory;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.google.gson.Gson;
import com.vassarlabs.scrapers.pojo.impl.ECropWebServiceResult;

public class SoapBucketWebService {

	public static void main(String args[]) {

		// Create SOAP Connection
		SOAPConnectionFactory soapConnectionFactory;
		try {
			soapConnectionFactory = SOAPConnectionFactory.newInstance();
			SOAPConnection soapConnection = soapConnectionFactory.createConnection();
			String url = "http://45.114.143.88/AreaSownServiceDetails/serviceDetails.php";
			SOAPMessage soapResponse = soapConnection.call(createSOAPRequest("30400"), url);
			NodeList data = soapResponse.getSOAPPart().getElementsByTagName("c");
			Node res = data.item(0);
			System.out.println(" res" + res.getTextContent());
			// Process the SOAP Response
			// printSOAPResponse(soapResponse);

			Gson gson = new Gson();
			ECropWebServiceResult list = gson.fromJson(res.getTextContent(), ECropWebServiceResult.class);
			for (Map<String, String> area : list.getListOfCropSownInfo()) {
				System.out.println("    aaaaaaaaaaxxx"+area.toString());
			}

		} catch (UnsupportedOperationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SOAPException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
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

		
		 * Constructed SOAP Request Message: <SOAP-ENV:Envelope
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
		QName bodyName = new QName("http://45.114.143.88/AreaSownServiceDetails/serviceDetails.php",
				"getAreaSownWeekWise", "m");
		SOAPBodyElement bodyElement = soapBody.addBodyElement(bodyName);

		SOAPElement soapBodyElem = bodyElement.addChildElement("UserName");
		soapBodyElem.addTextNode("administrator");
		SOAPElement soapBodyElem1 = bodyElement.addChildElement("Password");
		soapBodyElem1.addTextNode("administrator@1");
		SOAPElement soapBodyElem2 = bodyElement.addChildElement("Village");
		// soapBodyElem2.addTextNode(villageId);
		soapBodyElem2.setTextContent(villageId);
		MimeHeaders headers = soapMessage.getMimeHeaders();
		headers.addHeader("SOAPAction", "http://45.114.143.88/AreaSownServiceDetails/serviceDetails.php/getAreaSown");

		soapMessage.saveChanges();

		 Print the request message 
		System.out.println("Request SOAP Message = ");
		soapMessage.writeTo(System.out);
		System.out.println();

		return soapMessage;
	}

}
*/