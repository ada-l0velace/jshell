package pt.tecnico.myDrive.presentation;

import pt.tecnico.myDrive.service.ChangeDirectoryService;


public class ChangeDirectory extends MyDriveCommand {

    public ChangeDirectory(Shell sh) { super(sh, "cwd", "This command changes the current directory."); }
    public void execute(String[] args) {
        if (args.length < 3)
            throw new RuntimeException("USAGE: "+name()+" [<path>]");
        if (args.length > 3)
            System.out.println("FIXME");
        else
            System.out.println("FIXME");
    }
}
