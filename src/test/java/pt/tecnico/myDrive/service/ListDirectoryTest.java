package pt.tecnico.myDrive.service;

import org.joda.time.DateTime;
import org.junit.Test;
import pt.tecnico.myDrive.domain.*;
import pt.tecnico.myDrive.exception.ReadPermissionException;
import pt.tecnico.myDrive.filefactory.AbstractFactory.FileType;
import pt.tecnico.myDrive.filefactory.FileFactory;
import pt.tecnico.myDrive.service.dto.FileDto;

import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Created by lolstorm on 09/04/16.
 */
public class ListDirectoryTest extends TokenVerificationTest {

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
    private static final String FILENAME [] = {
            "EvilDirectory",
            "PlainFileText",
            "LinkToThePast",
            "AppTotheFuture"
    };

    private static final String LINKPATH = "/home/root/EvilDirectory";
    private static final String PASSWORD = "lovely";
    private static final String NAME = "Herman Horton";
    private static final Short UMASK = 0xF0;

    private FileFactory _fileFactory;
    private String _token;
    private String _rootToken;
    private User _user;

    protected void populate() {
        Manager m =  Manager.getInstance();
        _fileFactory = new FileFactory();
        for (String username : USERNAME) {
            _user = createUser(username, PASSWORD, NAME, UMASK);
            _token = createSession(username, PASSWORD);
        }
        _rootToken = createSession("root", "***");

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
        Directory d = (Directory) _user.getFileByPath("/home", _token);
        Manager.getInstance().getSessionByToken(_rootToken).setCurrentDirectory(d);
        ListDirectory service = new ListDirectory(_rootToken);
        service.execute();


        List<FileDto> ds = service.result();
        if (ds != null) {
            assertEquals("List with 10 files", 12, ds.size());
            assertEquals("1st file is", ".", ds.get(0).getName());
            assertEquals("2nd file is", "..", ds.get(1).getName());
            assertEquals("3rd file is", "1scarlett", ds.get(2).getName());
            assertEquals("4th file is", "caroline", ds.get(3).getName());
            assertEquals("5th file is", "chang", ds.get(4).getName());
            assertEquals("6th file is", "dessie", ds.get(5).getName());
            assertEquals("7th file is", "genesis", ds.get(6).getName());
            assertEquals("8th file is", "root", ds.get(7).getName());
            assertEquals("9th file is", "rosari", ds.get(8).getName());
            assertEquals("10th file is", "shakita", ds.get(9).getName());
            assertEquals("11th file is", "shawnda", ds.get(10).getName());
            assertEquals("12th file is", "suzette", ds.get(11).getName());
        }

    }

    @Test
    public void contentEmptyDirectory() {
        Manager m = Manager.getInstance();
        Session s = m.getSessionByToken(_rootToken);
        User su = s.getUser();
        Directory slash = m.getHome();
        Directory slashHome = (Directory) slash.searchFile("home", _rootToken);
        slashHome.setModified(new DateTime(2016, 4, 12, 4, 28, 0, 0));
        su.getHome().setModified(new DateTime(2016, 4, 12, 4, 28, 0, 0));
        ListDirectory service = new ListDirectory(_rootToken);
        
        service.execute();
        String fOut0 = "D rwxdr-x- 2 root 3 "+new DateTime(2016, 4, 12, 4, 28, 0, 0).toString("MMM dd hh:mm")+" .\n";
        String fOut1 = "D rwxdr-x- 12 root 2 "+new DateTime(2016, 4, 12, 4, 28, 0, 0).toString("MMM dd hh:mm")+" ..\n";
        List<FileDto> ds = service.result();
        if (ds != null) {
            assertEquals("List with 2 files", 2, ds.size());
            assertEquals("Directory output", fOut0 + fOut1, service.output());
        }
    }

    @Test
    public void listRootDirectory() {
        Manager m = Manager.getInstance();
        Directory slash = m.getHome();
        Directory home = (Directory) slash.searchFile("home", _rootToken);
        m.getSessionByToken(_rootToken).setCurrentDirectory(slash);
        ListDirectory service = new ListDirectory(_rootToken);

        service.execute();
        String fOut0 = "D rwxdr-x- 3 root 1 "+home.getModified().toString("MMM dd hh:mm")+" .\n";
        String fOut1 = "D rwxdr-x- 3 root 1 "+home.getModified().toString("MMM dd hh:mm")+" ..\n";
        String fOut2 = "D rwxdr-x- 12 root 2 "+home.getModified().toString("MMM dd hh:mm")+" home\n";

        List<FileDto> ds = service.result();
        if (ds != null) {
            //log.trace(service.output());
            assertEquals("List with 3 files", 3, ds.size());
            assertEquals("Directory output", fOut0 + fOut1 + fOut2, service.output());
        }
    }

    @Test
    public void listSuperUserRootDirectory() {
        Manager m = Manager.getInstance();

        Session s = m.getSessionByToken(_rootToken);
        for (int i = 0; i < FILENAME.length; i++) {
            FileType fileType = FileType.values()[i];
            if (fileType == FileType.DIRECTORY)
                _fileFactory.CreateFile(FILENAME[i], fileType,"", s.getCurrentDirectory(), s.getUser(), _rootToken);
            else if (fileType == FileType.LINK)
                _fileFactory.CreateFile(FILENAME[i], fileType, LINKPATH, s.getCurrentDirectory(), s.getUser(), _rootToken);
            else
                _fileFactory.CreateFile(FILENAME[i], fileType, "Yolo", s.getCurrentDirectory(), s.getUser(), _rootToken);
        }
        ListDirectory service = new ListDirectory(_rootToken);
        service.execute();

        Directory home = Manager.getInstance().getUserByToken(_rootToken).getHome();
        File d = m.getHome();
        File ev = home.search(FILENAME[0], _rootToken);
        File p = home.search(FILENAME[1], _rootToken);
        File l = home.search(FILENAME[2], _rootToken);
        File a= home.search(FILENAME[3], _rootToken);
        String fOut0 = "D rwxdr-x- 6 root 3 "+home.getModified().toString("MMM dd hh:mm") + " .\n";
        String fOut1 = "D rwxdr-x- 12 root 2 " + d.getModified().toString("MMM dd hh:mm") + " ..\n";
        String fOut2 = "D rwxdr-x- 2 root 13 " + ev.getModified().toString("MMM dd hh:mm") + " " + FILENAME[0] + "\n";
        String fOut3 = "P rwxdr-x- 4 root 14 " + p.getModified().toString("MMM dd hh:mm") + " " + FILENAME[1] + "\n";
        String fOut4 = "L rwxdr-x- 24 root 15 " + l.getModified().toString("MMM dd hh:mm") + " " + FILENAME[2] + " -> " + LINKPATH + "\n";
        String fOut5 = "A rwxdr-x- 4 root 16 "+a.getModified().toString("MMM dd hh:mm") + " " + FILENAME[3] + "\n";

        List<FileDto> ds = service.result();
        if (ds != null) {
            //log.trace(service.output());
            assertEquals("List with 6 files", 6, ds.size());
            assertEquals("Directory output", fOut0 + fOut1 + fOut5 + fOut2 + fOut4 + fOut3, service.output());
        }
    }


    @Test(expected = ReadPermissionException.class)
    public void readPermissionDeniedDirectory() {
        Manager m = Manager.getInstance();
        Directory d = (Directory) _user.getFileByPath("/home/shakita", _token);
        m.getSessionByToken(_token).setCurrentDirectory(d);
        ListDirectory service = new ListDirectory(_token);
        service.execute();
    }

    @Override
    public MyDriveService CreateService(String token) {
        return new ListDirectory(token);
    }
}
