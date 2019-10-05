package baikal.web.footballapp.model;

public class SignIn {
    private String login;
    private String password;

    public SignIn(String login, String password) {
        this.login = login;
        this.password = password;
    }

    /**
     *
     * @return
     * The login
     */
    public String getLogin() {
        return login;
    }

    /**
     *
     * @param login
     * The email
     */
    public void setLogin(String login) {
        this.login = login;
    }

    /**
     *
     * @return
     * The password
     */
    public String getPassword() {
        return password;
    }

    /**
     *
     * @param password
     * The password
     */
    public void setPassword(String password) {
        this.password = password;
    }

}
