package pt.tecnico.myDrive;

import pt.tecnico.myDrive.domain.*;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.DomainRoot;
import pt.ist.fenixframework.FenixFramework;

import org.jdom2.Element;
import org.jdom2.Document;
import org.jdom2.output.XMLOutputter;
import org.jdom2.JDOMException;
import org.jdom2.output.Format;
import org.jdom2.input.SAXBuilder;
import org.joda.time.DateTime;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.PrintStream;
import java.io.IOException;

public class Main {

    private static final Logger logger = LogManager.getRootLogger();
    
    public static void main(String [] args) {
        try {
            logger.trace("Entering application.");
            for (String s: args) xmlScan(new File(s));
        } finally {
            // ensure an orderly shutdown
            FenixFramework.shutdown();
        }
    }

    public static void applicationCodeGoesHere() {
        someTransaction();
    }

    @Atomic
    public static void someTransaction() {
        // Manager m = Manager.getInstance();
        // User u = m.getSuperuser();
        // User u1 = new User("Daniel", "Poodle", "GangnamStyle", (short)1);
        // //m.createUser(u1);
        // //m.getUser(u).exportXml();
        // Element element = new Element("mydrive");
        // //Element element = new Element("users");
        // //element0.addContent(element);
        // File f = new File(u, "CRL");
        // element.addContent(f.exportXml());
        // System.out.println(f.exportXml());      
        // element.addContent(u.exportXml());
        // System.out.println(u.exportXml());      
        // element.addContent(u1.exportXml());
        // //m.importXml(u1.exportXml());
        // Document doc = new Document(element);	
        
        // XMLOutputter xmlOutput = new XMLOutputter(Format.getPrettyFormat());
        // try { xmlOutput.output(doc, new PrintStream(System.out));
        // } catch (IOException e) { System.out.println(e); }
        // System.out.println("FenixFramework's root object is: " + FenixFramework.getDomainRoot());		
    }

    /**
     * Scans the xml file and imports to database.
     * @param file (Java.io.File) represents a file. 
     */
    @Atomic
    public static void xmlScan(File file) {
        Manager m = Manager.getInstance();
        SAXBuilder builder = new SAXBuilder();
        try {
            Document document = (Document)builder.build(file);
            m.importXml(document.getRootElement());
        } catch (JDOMException | IOException e) {
            e.printStackTrace();
        }
    }
}
