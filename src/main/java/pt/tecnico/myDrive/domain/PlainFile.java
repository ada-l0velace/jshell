package pt.tecnico.myDrive.domain;

import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


import org.jdom2.Element;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.Arrays;

import org.apache.commons.lang.ArrayUtils;

import pt.tecnico.myDrive.presentation.Shell;
import pt.tecnico.myDrive.exception.ImportDocumentException;
import pt.tecnico.myDrive.exception.PublicAcessDeniedException;
import pt.tecnico.myDrive.exception.ReadPermissionException;
import pt.tecnico.myDrive.exception.WritePermissionException;
import pt.tecnico.myDrive.service.dto.FileDto;
import pt.tecnico.myDrive.service.dto.PlainFileDto;
import pt.tecnico.myDrive.exception.InvalidNameFileException;
import pt.tecnico.myDrive.exception.ExecutePermissionException;
import pt.tecnico.myDrive.exception.ExecuteFileException;
import pt.tecnico.myDrive.exception.FileNotFoundException;

import java.lang.ClassNotFoundException;
import java.lang.NoSuchMethodException;
import java.lang.SecurityException;
import java.lang.IllegalAccessException;
import java.lang.IllegalArgumentException;
import java.lang.reflect.InvocationTargetException;


public class PlainFile extends PlainFile_Base {

    /**
     * Default constructor to create PlainFile
     */
    public PlainFile() {
        super();
    }


    /**
     * Alternate constructor to create a File with xml.
     * @param  xml (Element JDOM) which represents a File.
     */
    public PlainFile(Element xml) {
        super();
        importXml(xml);
    }

    /**
     * Alternate constructor to create a PlainFile.
     * @param owner (User) which represents a User.
     * @param name (String) which represents the name a File.
     */
    public PlainFile(User owner, String name, String content, Directory parent, Manager m) {
        super();
        super.init(owner, name, parent, m);
        super.setContent(content);
    }

    protected void initContent(String content) {
        super.setContent(content);
    }

    /**
     * Imports a PlainFile from persistent state (XML format).
     * @throws ImportDocumentException occurs when there is an error in import.
     */
    @Override
    public void importXml (Element xml) {
        Element node = xml;
        super.importXml(node);
        String content = node.getChild("content").getValue();
        super.setContent(content);
    }

    /**
     * @deprecated and replaced with new exportXml
     */
    @Deprecated
    public Element xmlExport(){    
        Element node = super.xmlExport();
        node.setAttribute("content", getContent());
        return node;
    }


    /**
     * Exports a PlainFile to a persistent state (XML format),
     * @see File
     * @return (Element JDOM) which represents a PlainFile in xml format.
     */
    @Override
    public Element exportXml () {
        Element node = super.exportXml();

        Element content = new Element("content");
        content.addContent(getContent());

        node.addContent(content);
        
        return node;
    }


    /**
     * @param   link (String) receives a String with the link content.
     * @return  (File) returns himself.
     */
    public File getFileByPath (String link){
        return this;
    }

    /**
     * Gets directory dimension.
     * @return String represents the directory dimension in string.
     * @throws RuntimeException occurs when there is an error in an unicode character.
     */
    public String getDim() throws RuntimeException {
        String dim;
        try {
            dim = super.getContent().getBytes("UTF-8").length + "";
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        return dim;
    }

    /**
     * Removes a PlainFile from the domain
     * @param token (String) receives a token to identify the user.
     */
    @Override
    public void remove(String token) {
        super.remove(token);
        deleteDomainObject();
    }

    /**
     * Function that protects public content file modification.
     * @param content
     */
    @Override
    public void setContent(String content) {
        throw new PublicAcessDeniedException("setContent(content)", "setContent(content, token)");
    }

    /**
     * Function that protects public content file modification.
     */
    @Override
    public String getContent() {
        throw new PublicAcessDeniedException("getContent()", "getContent(token)");
    }

    /**
     * Modifies PlainFile content
     * @throws ReadPermissionException occurs when user does not have permissions to read file.
     */
    public void setContent(String content, String token) {
        writePermissions(token);
        super.setContent(content);
    }

    /**
     * Interface method.
     * @throws ReadPermissionException occurs when user does not have permissions to read file.
     */
    @Override
    public String getContent(String token) {
        super.getContent(token);
        User user = Manager.getInstance().getUserByToken(token);
        if(user.getPermissions().canRead(this)){
        	return super.getContent();
        }
        else	
        	throw new ReadPermissionException(getName(), user.getUsername());
    }

    protected String getContentAux() {
        return super.getContent();
    }

    @Override
    public FileDto getDtoData(String token) {
        return new PlainFileDto(getId(), getName(), getModified(), getPermissions().getUmask(),
                getParent().getName(), getOwner().getName(),
                getContent(token), toString());
    }


    /**
     * Overrides original toString() to the current object implementation.
     * @return String represents the output string of PlainFile.
     */
    public String toString() {
        String a = super.toString();
        String dim = getDim();
        String username = getOwner().getUsername();
        String modified = getModified().toString("MMM dd hh:mm");
        String rest = dim + " " + username + " " + getId() + " " + modified + " " + this.getName();
        return a + rest;
    }

	/**
     * Executes Plainfile
     */
    @Override
    public void execute(String token, String [] args){
        User u = Manager.getInstance().getUserByToken(token);
		if (!u.getPermissions().canExecute(this))
			throw new ExecutePermissionException(getName(), u.getUsername());

		String [] files = getContent(token).split("\n");
		for (String s : files){
			String [] plainArgs = s.split(" ", 2);
			try{
				File f = Manager.getInstance().getUserByToken(token).getFileByPath(plainArgs[0], token);
				String [] finalArgs = (plainArgs.length == 2) ? (String[])ArrayUtils.addAll(plainArgs[1].split(" "), args) : args;
				f.execute(token, finalArgs);
			} catch (FileNotFoundException fnfe) {  }
		}
		
		
		
	}
}
