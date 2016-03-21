package pt.tecnico.myDrive.interfaces;

import pt.tecnico.myDrive.domain.*;

/**
 * Service Interface to change the directory of a current user session
 */

public interface ChangeDirectoryService {
    public String changeDirectory (Long token, String path);
}
