package pt.tecnico.myDrive.presentation;

import pt.tecnico.myDrive.service.ListDirectoryService;
import pt.tecnico.myDrive.service.ChangeDirectoryService;

import java.util.Arrays;

public class ListDirectory extends MyDriveCommand {

	public static String token;
	
    public ListDirectory(Shell sh) { super(sh, "ls", "This command lists the current directory."); }
    
    public void execute(String[] args) {
        if (args.length < 0)
            throw new RuntimeException("USAGE: " + name() + " [<path>]");
     
        if (args.length == 0) {
        	ListDirectoryService ld = new ListDirectoryService(token);
            ld.execute();
        	System.out.println(ld.output());
        }
        else {
        	ListDirectoryService ld = new ListDirectoryService(token, args[0]);
            ld.execute();
        	System.out.println(ld.output());
        }
    }
}
