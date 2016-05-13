package pt.tecnico.myDrive.presentation;

import pt.tecnico.myDrive.service.EnvironmentVariableService;

public class EnvVariable extends MyDriveCommand {

    public EnvVariable(Shell sh) { super(sh, "env", "Creates or modifies an environment variable."); }

    public void execute(String[] args) {
        if (args.length < 2 || args.length > 2)
            throw new RuntimeException("USAGE: "+name()+" [<name> [<value>]]");
        else {
            EnvironmentVariableService service = new EnvironmentVariableService(Token.token, args[0], args[1]);
            service.execute();
        }
    }
}
