package pt.tecnico.myDrive.service.dto;

import org.joda.time.DateTime;


public abstract class FileDto implements Comparable<FileDto> {

    private int _id;
    private String _name;
    private DateTime _modified;
    private Short _umask;

    public FileDto(int id, String name, DateTime modified, short umask) {
        _id = id;
        _name = name;
        _modified = modified;
        _umask = umask;
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
     * Override of operator equal to a specific one.
     * @param o represents the other object.
     * @return boolean returns 1 if the name is equal to the other.
     */
    @Override
    public int compareTo(FileDto o) {
        return getName().compareTo(o.getName());
    }


}
