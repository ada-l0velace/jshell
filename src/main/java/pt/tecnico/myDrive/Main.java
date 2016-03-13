package pt.tecnico.myDrive;

import pt.tecnico.myDrive.domain.*;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.DomainRoot;
import pt.ist.fenixframework.FenixFramework;

import org.jdom2.Element;
import org.jdom2.Document;
import org.jdom2.JDOMException;
import org.jdom2.output.XMLOutputter;
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
            setup();
            for (String s: args) xmlScan(new java.io.File(s));
            testCode();
            //applicationCodeGoesHere();
        } finally {
            // ensure an orderly shutdown
            FenixFramework.shutdown();
        }
    }

    public static void applicationCodeGoesHere() {
        someTransaction();
    }

    /**
     * Exports the Manager to a xml file
     */
    @Atomic
    public static void xmlPrint() {
        logger.trace("xmlPrint: " + FenixFramework.getDomainRoot());
        Document doc = Manager.getInstance().exportXml();
        XMLOutputter xmlOutput = new XMLOutputter(Format.getPrettyFormat());


        try { xmlOutput.output(doc, new PrintStream(System.out));
        } catch (IOException e) { System.out.println(e); }
    }

    @Atomic
    public static void testCode() {
        Manager m = Manager.getInstance();
        User campos = m.getUserByUsername("sagres");
        System.out.println(m.getDirHome().listContent());
        logger.trace(Integer.toBinaryString(campos.getPermissions().getUmask()));
        logger.trace(campos.getPermissions());
        logger.trace(Integer.toBinaryString(m.getSuperuser().getPermissions().getUmask()));
        logger.trace(m.getSuperuser().getPermissions());
    }

    @Atomic
    public static void setup() { // phonebook with debug data
        logger.trace("Setup: " + FenixFramework.getDomainRoot());
        Manager m = Manager.getInstance();
        /*m.createUser(new User("frodo", 
            "pedofrodo", 
            "mordor", 
            (short) Integer.parseInt("F2",16), 
            m));
        m.createUser(new User("Chavetas", 
            "biana", 
            "HorribleIndentation", 
            (short) Integer.parseInt("B2",16), 
            m));
        m.createUser(new User("Ines", 
            "ines", 
            "rootroot", 
            (short) Integer.parseInt("98",16), 
            m));
        m.createUser(new User("bras", 
            "bacalhau", 
            "lhaulhau", 
            (short) Integer.parseInt("FF",16), 
            m));
        m.createUser(new User("Daniel", 
            "poodle", 
            "pedigree", 
            (short) Integer.parseInt("FA",16), 
            m));
        m.createUser(new User("campos", 
            "sagres", 
            "jolaisthebest", 
            (short) Integer.parseInt("AA",16), 
            m));
        User u = m.getUserByUsername("sagres");
        u.getHome().addFile(new App(u, "beer", "yolo", m));
        u.getHome().addFile(new PlainFile(u, "yolo", "yolo", m));*/
        
    }   


    @Atomic
    public static void someTransaction() {
        xmlPrint();
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
