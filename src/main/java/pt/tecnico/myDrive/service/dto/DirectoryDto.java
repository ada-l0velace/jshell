package pt.tecnico.myDrive.service.dto;

import org.joda.time.DateTime;

public class DirectoryDto extends FileDto {
   
	
    /**
     * Construtor to DirectoryDto
     * @param id (int) receives the id of the file.
     * @param name (String) receives the name of the file.
     * @param modified (DateTime) receives the date of the file.
     * @param umask (Short) receives the permission mask of the file.
     * @param parent (String) receives the name of the parent directory.
     * @param owner (String) receives the owner name of the file.
     */
    public DirectoryDto(int id,
                   String name,
                   DateTime modified,
                   short umask,
                   String parent,
                   String owner, 
                   String representation) {
        super(id, name, modified, umask, parent, owner, representation);   
    }

       /**
     * Override of operator equal to a specific one.
     * @param o represents the other object.
     * @return boolean returns 1 if the name is equal to the other.
     */
    public int compareTo(DirectoryDto o) {
        return getName().compareTo(o.getName());
    }
    
}