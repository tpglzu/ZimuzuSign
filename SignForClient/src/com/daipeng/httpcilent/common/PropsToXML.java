package com.daipeng.httpcilent.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Properties;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

public class PropsToXML {

    /**
     * Provide a static entry point for running.
     */
    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Usage: java javaxml3.PropsToXML "
                    + "[properties file] [XML file for output]");
            System.exit(0);
        }

        try {
            PropsToXML propsToXML = new PropsToXML();
            propsToXML.convert(args[0], args[1]);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * This will take the supplied properties file, and convert that file to an
     * XML representation, which is then output to the supplied XML do*****ent
     * filename.
     * 
     * @param propertiesFilename
     *            file to read in as Java properties.
     * @param xmlFilename
     *            file to output XML representation to.
     * @throws <code>IOException</code> - when errors occur.
     */
    public void convert(String propertiesFilename, String xmlFilename)
            throws IOException {

        // Get Java Properties object
        FileInputStream input = new FileInputStream(propertiesFilename);
        Properties props = new Properties();
        props.load(input);

        File xmlFile = new File(xmlFilename);
        if(!xmlFile.exists()){
        	xmlFile.createNewFile();
        }
        
        // Convert to XML
        convertToXML(props, xmlFilename);
    }

    /**
     * This will handle the detail of conversion from a Java
     * <code>Properties</code> object to an XML do*****ent.
     * 
     * @param props
     *            <code>Properties</code> object to use as input.
     * @param xmlFilename
     *            file to output XML to.
     * @throws <code>IOException</code> - when errors occur.
     */
    private void convertToXML(Properties props, String xmlFilename)
            throws IOException {

        // Create a new JDOM Document with a root element "properties"
        Element root = new Element("properties");
        Document doc = new Document(root);

        // Get the property names
        Enumeration propertyNames = props.propertyNames();
        while (propertyNames.hasMoreElements()) {
            String propertyName = (String) propertyNames.nextElement();
            String propertyValue = props.getProperty(propertyName);
            createXMLRepresentation(root, propertyName, propertyValue);
        }

        // Output document to supplied filename
        XMLOutputter outputter = new XMLOutputter(Format.getPrettyFormat());
        FileOutputStream output = new FileOutputStream(xmlFilename);
        outputter.output(doc, output);

    }

    /**
     * This will convert a single property and its value to an XML element and
     * textual value.
     * 
     * @param root
     *            JDOM root <code>Element</code> to add children to.
     * @param propertyName
     *            name to base element creation on.
     * @param propertyValue
     *            value to use for property.
     */
    private void createXMLRepresentation(Element root, String propertyName,
            String propertyValue) {

        int split;
        String name = propertyName;
        Element current = root;
        Element test = null;

        while ((split = name.indexOf(".")) != -1) {
            String subName = name.substring(0, split);
            name = name.substring(split+1);

            // Check for existing element 
            if ((test = current.getChild(subName)) == null) {
                Element subElement = new Element(subName);
                current.addContent(subElement);
                current = subElement;
            } else {
                current = test;
            }
        }

        // When out of loop, what's left is the final element's name
        Element last = new Element(name); 
        last.setText(propertyValue);
        //Attribute attribute = new Attribute("value", propertyValue);
        //last.setAttribute(attribute);
        current.addContent(last);

    }

}

