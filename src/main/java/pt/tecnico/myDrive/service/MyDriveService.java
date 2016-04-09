package pt.tecnico.myDrive.service;
import pt.tecnico.myDrive.exception.MyDriveException;

/**
 * Created by lolstorm on 08/04/16.
 */
public abstract class MyDriveService  {
    public final void execute() throws MyDriveException {
        dispatch();
    }

    protected abstract void dispatch() throws MyDriveException;
}
