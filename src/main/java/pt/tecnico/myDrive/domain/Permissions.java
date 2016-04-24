package pt.tecnico.myDrive.domain;

import pt.tecnico.myDrive.exception.WritePermissionException;

public class Permissions extends Permissions_Base {
    
    /**
     * Default constructor
     */
    public Permissions() {
        super();
    }

    /**
     * Alternate constructor for Permissions with Umask
     * @param umask represents the numeric user mask.
     */
    public Permissions(short umask) {
        super();
        setUmask(umask);
    }

    /**
     * Returns the byte position
     * @param  position (int) Which represents the position of the byte.
     * @return byte which represents the byte on that position.         
     */
    public byte getBit(int position) {
       
       return (byte)((getUmask() >> position) & 1);
    }

    /**
     * @return true if can read the file, false otherwise.
     */
    public boolean worldCanRead(){
        return (getBit(3) == 1) ? true : false;
    }
    
    /**
     * @return true if can write the file, false otherwise.
     */
    public boolean worldCanWrite() {
        return (getBit(2) == 1) ? true : false;
    }

    /**
     * @return true if can world can execute the file, false otherwise.
     */    
    public boolean worldCanExecute() {
	return (getBit(1) == 1) ? true : false;
    }

    
    public boolean worldCanDelete() {
        return (getBit(0) == 1) ? true : false;
    }

    
    /**
     * @return true if owner can read the file, false otherwise.
     */
    public boolean userCanRead(){
        return (getBit(7) == 1) ? true : false;
    }

    
    /**
     * @return true if owner can write the file, false otherwise.
     */
    public boolean userCanWrite() {
        return (getBit(6) == 1) ? true : false;
    }

    /**
     * @return true if can execute the file, false otherwise.
     */    
    public boolean userCanExecute() {
	return (getBit(5) == 1) ? true : false;
    }    
    
    public boolean userCanDelete() {
        return (getBit(4) == 1) ? true : false;
    }

    /**
     * Checks if a user can write on a specific file.
     * @param f represents the file to write.
     * @return File returns the file that can be modified.
     */
    public boolean canWrite(File f) {
        String o = f.getOwner().getUsername();
        if(getUser().isSuperUser())
            return true;
        String o2 = getUser().getUsername();
        if ((userCanWrite() && o.equals(o2)) ||
                f.getPermissions().worldCanWrite()){
            return true;
        }
        return false;
    }
    
    public boolean canRead(File f) {
        String userOwner = f.getOwner().getUsername();
        String testedUser = getUser().getUsername();

        if(getUser().isSuperUser())
            return true;

        if ((userCanRead() && userOwner.equals(testedUser)) ||
                f.getPermissions().worldCanRead()){
            return true;
        }
        return false;
    }

    public boolean canExecute(File f) {
	String owner = f.getOwner().getUsername();
	String user = getUser().getUsername();

	if (getUser().isSuperUser())
	    return true;
	else if (owner.equals(user) && userCanExecute())
	    return true;
	else if (f.getPermissions().worldCanExecute())
	    return true;
	return false;
    }
    
    public boolean canDelete(File file) {
        String owner = file.getOwner().getUsername();
        String user = getUser().getUsername();
        if(getUser().isSuperUser())
            return true;
        if ((userCanDelete() && owner.equals(user)) ||
                file.getPermissions().worldCanDelete()){
            return true;
        }
        return false;
    }

    /**
     * Permission table
     * @param  position (int) Which represents the position of the byte.
     * @return char represents the string form of that byte. 
     */
    public char getBitToChar(int position) {
       char permission;
       switch (position) {
            case 0:  permission = readToString(getBit(7));
                     break;
            case 1:  permission = writeToString(getBit(6));
                     break;
            case 2:  permission = executeToString(getBit(5));
                     break;
            case 3:  permission = deleteToString(getBit(4));
                     break;
            case 4:  permission = readToString(getBit(3));
                     break;
            case 5:  permission = writeToString(getBit(2));
                     break;
            case 6:  permission = executeToString(getBit(1));
                     break;
            case 7:  permission = deleteToString(getBit(0));
                     break;
            default: permission = '\0';
                     break;
        }
        return permission;
    }    

    /**
     * Returns the string representation of a byte.
     * @param  b (byte) which represents the a byte.
     * @return  char represents the string form of that byte.
     */
    public char readToString (byte b) {
        return (b == 1) ? 'r' : '-';
    }

    /**
     * Returns the string representation of a byte.
     * @param  b (byte) which represents the a byte.
     * @return  char represents the string form of that byte.
     */
    public char writeToString (byte b) {
        return (b == 1) ? 'w' : '-';
    }

    /**
     * Returns the string representation of a byte.
     * @param  b (byte) which represents the a byte.
     * @return  char represents the string form of that byte.
     */
    public char executeToString (byte b) {
        return (b == 1) ? 'x' : '-';
    }

    /**
     * Returns the string representation of a byte.
     * @param  b (byte) which represents the a byte.
     * @return  char represents the string form of that byte.
     */
    public char deleteToString (byte b) {
        return (b == 1) ? 'd' : '-';
    }

    /**
     * toString override for unix permission style representation.
     * @return String which is the final output representation.
     */
    public String toString() {
        String pa = "";
        for (int i=0; i <= 7; i++ )
            pa += getBitToChar(i);
        
        return pa;
    }
    
}
