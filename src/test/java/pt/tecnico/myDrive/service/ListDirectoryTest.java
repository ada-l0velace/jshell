package pt.tecnico.myDrive.service;

import org.joda.time.DateTime;
import org.junit.Test;
import pt.tecnico.myDrive.domain.Directory;
import pt.tecnico.myDrive.domain.Manager;
import pt.tecnico.myDrive.domain.User;
import pt.tecnico.myDrive.exception.ReadPermissionException;
import pt.tecnico.myDrive.service.dto.FileDto;

import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Created by lolstorm on 09/04/16.
 */
public class ListDirectoryTest extends BaseServiceTest {

    private static final String USERNAME [] = {
        "shakita",
        "suzette",
        "1scarlett",
        "genesis",
        "shawnda",
        "dessie",
        "rosari",
        "chang",
        "caroline"
    };
    private static final String PASSWORD = "lovely";
    private static final String NAME = "Herman Horton";
    private static final Short UMASK = 0xFA;

    private String _token;
    private String _rootToken;
    private User _user;

    protected void populate() {
        for (String username : USERNAME) {
            _user = createUser(username, PASSWORD, NAME, UMASK);
            _token = createSession(username);
        }
        _rootToken = createSession("root");

    }

    @Test
    public void emptyDirectory() {
        ListDirectory service = new ListDirectory(_rootToken);
        service.execute();
        List<FileDto> ds = service.result();
        if (ds != null) {
            assertEquals("List with 2 files", 2, ds.size());
        }
    }

    @Test
    public void sortedDirectory() {
        Directory d = (Directory) _user.getFileByPath("/home");
        Manager.getInstance().getSessionByToken(_rootToken).setCurrentDirectory(d);
        ListDirectory service = new ListDirectory(_rootToken);
        service.execute();


        List<FileDto> ds = service.result();
        if (ds != null) {
            assertEquals("List with 10 files", 12, ds.size());
            assertEquals("1st file is", "1scarlett", ds.get(0).getName());
            assertEquals("2nd file is", "caroline", ds.get(1).getName());
            assertEquals("3rd file is", "chang", ds.get(2).getName());
            assertEquals("4th file is", "dessie", ds.get(3).getName());
            assertEquals("5th file is", "genesis", ds.get(4).getName());
            assertEquals("6th file is", "root", ds.get(5).getName());
            assertEquals("7th file is", "rosari", ds.get(6).getName());
            assertEquals("8th file is", "shakita", ds.get(7).getName());
            assertEquals("9th file is", "shawnda", ds.get(8).getName());
            assertEquals("10th file is", "suzette", ds.get(9).getName());
        }

    }

    @Test
    public void contentEmptyDirectory() {
        Manager m = Manager.getInstance();
        ListDirectory service = new ListDirectory(_rootToken);
        m.getDirHome().setModified(new DateTime(2016, 4, 12, 4, 28, 0, 0));
        m.getHome().setModified(new DateTime(2016, 4, 12, 4, 28, 0, 0));
        service.execute();
        String fOut0 = "D rwxdr-x- 1 root 3 Apr 12 04:28 .\n";
        String fOut1 = "D rwxdr-x- 12 root 2 Apr 12 04:28 ..\n";
        List<FileDto> ds = service.result();
        if (ds != null) {
            assertEquals("List with 2 files", 2, ds.size());
            assertEquals("Directory output", fOut0 + fOut1, ds);
        }

    }

    @Test(expected = ReadPermissionException.class)
    public void readPermissionDeniedDirectory() {
        Manager m = Manager.getInstance();
        Directory d = (Directory) _user.getFileByPath("/home/shakita");
        m.getSessionByToken(_token).setCurrentDirectory(d);
        ListDirectory service = new ListDirectory(_token);
        service.execute();
    }

}
