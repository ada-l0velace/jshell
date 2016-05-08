package pt.tecnico.myDrive.domain;

public class Guest extends Guest_Base {

    public static String GUEST_USERNAME = "nobody";
    public static String GUEST_NAME = "Guest";
    public static Short GUEST_UMASK = (short) Integer.parseInt("FA",16);

    public Guest() {
        super();
    }

    /**
     * Alternative constructor to create a guest.
     * @param m (Manager) receives the manager of the app.
     */
    public Guest(Manager m) {
        this();
        init(GUEST_NAME, GUEST_USERNAME, "", GUEST_UMASK, m);
        initHome();
    }

    /**
     * Alternative constructor to create a guest.
     * @param pHomePath (String) represents the parent home path.
     */
    public Guest(String pHomePath, Manager m) {
        super();
        init(GUEST_NAME, GUEST_USERNAME, "", GUEST_UMASK, m);
        initHome(pHomePath);
    }

    /**
     * Overrides setPassword to byPass 8 characters password.
     * @param password receives the password to set.
     */
    @Override
    public void setPassword(String password) {
        setPasswordAux(password);
    }

    /**
     * Function to set the token live time of the user
     * @return the time in minutes of live time of the token.
     */
    @Override
    public int tokenTimeExpiration() {
        return Integer.MAX_VALUE;
    }
}
