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

import java.io.PrintStream;
import java.io.IOException;

public class Main {

    private static final Logger logger = LogManager.getRootLogger();
    
    public static void main(String [] args) {
        try {
            //logger.trace("Entering application.");
            //for (String s: args) xmlScan(new java.io.File(s));
            applicationCodeGoesHere();
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
        /*
        logger.trace(m.getHome().getName());
        for (File f : m.getHome().getFileSet() ) {
            if (f.getName().equals("home")) {
                logger.trace(f.getName());
                Directory a = (Directory) f;
                for (File f1 : a.getFileSet())
                    logger.trace(f1.getName());
            }

        }
        */
        //logger.trace(m.getDirHome().getName());
        Element element = new Element("mydrive");
        Manager m = Manager.getInstance();
        User su = m.getSuperuser();
        File f = new File(su, "myFile", m);
        PlainFile pf = new PlainFile(su, "poodlePlainFile", m);
        App a = new App(su, "poodleApp", m);
        Link l = new Link("poodleLink", f, m);
        //User u1 = new User("Daniel", "poodle", "poodlePass", (short) 1);


        su.getHome().addFile(f);
        //su.getHome().addFile(pf);
        su.getHome().addFile(l);
        m.createUser(su);
        
        element.addContent(su.exportXml());
        
        Document doc = m.exportXml();
        XMLOutputter xmlOutput = new XMLOutputter(Format.getPrettyFormat());
        try { xmlOutput.output(doc, new PrintStream(System.out));
        } catch (IOException e) { System.out.println(e); }
        //System.out.println("FenixFramework's root object is: " + FenixFramework.getDomainRoot());		
    }

    /**
     * Scans the xml file and imports to database.
     * @param file (Java.io.File) represents a file. 
     */
    @Atomic
    public static void xmlScan(java.io.File file) {
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
