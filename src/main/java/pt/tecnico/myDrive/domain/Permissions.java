package pt.tecnico.myDrive.domain;

public class Permissions extends Permissions_Base {
    
	public Permissions() {
		super();
	}

	public Permissions(short umask) {
		super();
		setUmask(umask);
	}
    
}
