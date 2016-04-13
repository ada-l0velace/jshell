package pt.tecnico.myDrive.service.dto;

import org.joda.time.DateTime;


public class PlainFileDto extends FileDto {

   
    private String _content;

    /**
     * Construtor to PlainFileDto
     * @param id (int) receives the id of the file.
     * @param name (String) receives the name of the file.
     * @param modified (DateTime) receives the date of the file.
     * @param umask (Short) receives the permission mask of the file.
     * @param parent (String) receives the name of the parent directory.
     * @param owner (String) receives the owner name of the file.
     * @param content (String) receives the owner name of the file.
     */
    public PlainFileDto(int id,
                   String name,
                   DateTime modified,
                   short umask,
                   String parent,
                   String owner,
                   String content,
                   String representation) {
        super(id, name, modified, umask, parent, owner, representation);
        _content = content; 
    }

    /**
     * Getter for _content.
     * @return int returns the content of the file.
     */
    public final String getContent() {
        return _content;
    }
  
    /**
     * Override of operator equal to a specific one.
     * @param o represents the other object.
     * @return boolean returns 1 if the name is equal to the other.
     */
    
    public int compareTo(PlainFileDto o) {
        return getName().compareTo(o.getName());
    }

}
