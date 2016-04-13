package pt.tecnico.myDrive.service;

import static org.junit.Assert.assertFalse;

import org.junit.Test;

import pt.tecnico.myDrive.domain.Manager;
import pt.tecnico.myDrive.domain.User;
import pt.tecnico.myDrive.domain.Directory;
import pt.tecnico.myDrive.exception.FileNotFoundException;
import pt.tecnico.myDrive.exception.ReadPermissionException;
import static org.junit.Assert.assertFalse;

public class CreateFileTest extends TokenVerificationTest {

    private Directory _dir
    
	
    protected void populate() {
    }

    @Test
    public void success() {
    }
	
    @Test(expected = FileNotFoundException.class)
    public void deleteNonExistingFile() {
    }

    @Test
    public void createdPlainFile() {
    }

    @Test(expected = ReadPermissionException.class)
    public void createNonAcessibleFile() {
    }

    @Test(expected = ReadPermissionException.class)
    public void creatNonAcessiblePlainFile() {
    }

    
    @Override
    public MyDriveService CreateService(String token) {
    }


}
