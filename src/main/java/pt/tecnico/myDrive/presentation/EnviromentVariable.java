package pt.tecnico.myDrive.presentation;


public class EnviromentVariable extends MyDriveCommand {

    public EnviromentVariable(Shell sh) { super(sh, "env", "Creates or modifies an environment variable."); }
    public void execute(String[] args) {
        if (args.length < 3)
            throw new RuntimeException("USAGE: "+name()+"env [<name> [<value>]]");
        if (args.length > 3)
            System.out.println("FIXME");
        else
            System.out.println("FIXME");
    }
}
