package ui;
import bank.PrivateBank;
/** Singleton Pattern
 * Einzelnes globales Objekt zur Übergabe von User und Bank
 */

public class UserHolder {
    private static final UserHolder instance = new UserHolder();
    private String userName = "";
    private PrivateBank bankName;

    //Konstruktor
    private UserHolder() {
    }

    /**
     *
     * @return statisches Objekt (User+Bank)
     */
    public static UserHolder getInstance() {
        return instance;
    }

    /**
     * @return the userName
     */
    public String getUserName() {
        return userName;
    }

    /**
     * @return the userName
     */
    public PrivateBank getBank() {
        return bankName;
    }

    /**
     * @param userName the userName to set
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * @param bankName the userName to set
     */
    public void setBank(PrivateBank bankName) {
        this.bankName = bankName;
    }
}
