package pt.tecnico.myDrive.service.dto;

import org.joda.time.DateTime;


public class FileDto implements Comparable<FileDto> {

    private int _id;
    private String _name;
    private DateTime _modified;
    private Short _umask;
    private String _parent;
    private String _owner;
    private String _representation;

    /**
     * Construtor to FileDto
     * @param id (int) receives the id of the file.
     * @param name (String) receives the name of the file.
     * @param modified (DateTime) receives the date of the file.
     * @param umask (Short) receives the permission mask of the file.
     * @param parent (String) receives the name of the parent directory.
     * @param owner (String) receives the owner name of the file.
     */
    public FileDto(int id,
                   String name,
                   DateTime modified,
                   short umask,
                   String parent,
                   String owner, 
                   String representation) {
        _id = id;
        _name = name;
        _modified = modified;
        _umask = umask;
        _parent = parent;
        _owner = owner;
        _representation = representation;
    }

    /**
     * Getter for _id.
     * @return int returns the id of the file.
     */
    public final int getId() {
        return _id;
    }

    /**
     * Getter for _name.
     * @return String returns the name of the file.
     */
    public final String getName() {
        return _name;
    }

    /**
     * Getter for _modified.
     * @return Datetime returns the last modified date.
     */
    public final DateTime getModified() {
        return _modified;
    }

    /**
     * Getter for _umask.
     * @return Short returns the file mask.
     */
    public final Short getUmask() {
        return _umask;
    }

    /**
     * Getter for _owner.
     * @return String returns the username of the owner.
     */
    public String getOwner() {
        return _owner;
    }

    /**
     * Getter for _parent.
     * @return String returns the parent directory name.
     */
    public String getParent() {
        return _parent;
    }

    /**
     * Override of operator equal to a specific one.
     * @param o represents the other object.
     * @return boolean returns 1 if the name is equal to the other.
     */
    @Override
    public int compareTo(FileDto o) {
        return getName().compareTo(o.getName());
    }

    @Override
    public String toString(){
    	return _representation;
    }
}
