import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class XMLResume {

    public static void main(String[] args) throws Exception {

//      Write java program to read XML document called "resume_w_xsl.xml".
//      Your program will read the name and phone number element of the given xml document.
//      Also search and print the project started on 1996.

        String fileName= "src/resume_w_xsl.xml";
        Document document = getDocument(fileName);

        String xpathExpression = "";

        /*******Get attribute values using xpath******/

        //Get Resume owner
        xpathExpression = "/resumes/person/@name";
        System.out.println("Name : " + evaluateXPath(document, xpathExpression));

        //Get Owner's phone number
        xpathExpression = "/resumes/person/phone/text()";
        System.out.println("Phone number : " + evaluateXPath(document, xpathExpression));


        xpathExpression = "/resumes/person/project[contains(@start,'1996')]";

        XPathFactory xpathfactory = XPathFactory.newInstance();
        XPath xpath = xpathfactory.newXPath();

        XPathExpression expr = xpath.compile(xpathExpression);
        Object result = expr.evaluate(document, XPathConstants.NODESET);
        NodeList nodes = (NodeList) result;

        System.out.println("_______________________________");

        System.out.println("List of project started on 1996");

        for (int i = 0; i < nodes.getLength(); i++) {
            Node node = nodes.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element eElement = (Element) node;

                String clientName = getElementData("client", "name", eElement);
                String supervisorName = getElementData("supervisor", "name", eElement);
                String shortDesc = eElement.getElementsByTagName("short_desc").item(0).getTextContent();

                System.out.print("Client name: " + clientName);
                System.out.print(", Supervisor name: " + supervisorName);
                System.out.print(", Description: " + shortDesc);

                System.out.println();

            }
        }

    }

    private static String getElementData(String tagName, String attribute, Element element) {
        NodeList nodeList = element.getElementsByTagName(tagName);
        Element nodeElement = (Element)nodeList.item(0);
        if (nodeElement.hasAttribute(attribute)) {
            return nodeElement.getAttribute(attribute);
        }
        return "";
    }

    private static Document getDocument(String fileName) throws Exception
    {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(fileName);
        return doc;
    }

    private static List<String> evaluateXPath(Document document, String xpathExpression) throws Exception {
        // Create XPathFactory object
        XPathFactory xpathFactory = XPathFactory.newInstance();

        // Create XPath object
        XPath xpath = xpathFactory.newXPath();

        List<String> values = new ArrayList<>();
        try
        {
            // Create XPathExpression object
            XPathExpression expr = xpath.compile(xpathExpression);

            // Evaluate expression result on XML document
            NodeList nodes = (NodeList) expr.evaluate(document, XPathConstants.NODESET);

            for (int i = 0; i < nodes.getLength(); i++) {
                values.add(nodes.item(i).getNodeValue());
            }

        } catch (XPathExpressionException e) {
            e.printStackTrace();
        }
        return values;
    }
}
