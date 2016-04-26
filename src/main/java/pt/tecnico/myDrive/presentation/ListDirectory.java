package pt.tecnico.myDrive.presentation;

import pt.tecnico.myDrive.service.ListDirectoryService;


public class ListDirectory extends MyDriveCommand {

    public ListDirectory(Shell sh) { super(sh, "ls", "This command lists the current directory."); }
    public void execute(String[] args) {
        if (args.length < 3)
            throw new RuntimeException("USAGE: "+name()+"ls [<path>]");
        if (args.length > 3)
            System.out.println("FIXME");
        else
            System.out.println("FIXME");
    }
}
