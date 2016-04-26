package pt.tecnico.myDrive.presentation;


public class EnvVariable extends MyDriveCommand {

    public EnvVariable(Shell sh) { super(sh, "env", "Creates or modifies an environment variable."); }
    public void execute(String[] args) {
        if (args.length < 3)
            throw new RuntimeException("USAGE: "+name()+" [<name> [<value>]]");
        if (args.length > 3)
            System.out.println("FIXME");
        else
            System.out.println("FIXME");
    }
}
