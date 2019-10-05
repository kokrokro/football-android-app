package baikal.web.footballapp.model;

public class LoginResult {
    private Boolean error;
    private String message;
    private Integer doctorid;
    private Boolean active;

    /**
     *
     * @return
     * The error
     */
    public Boolean getError() {
        return error;
    }

    /**
     *
     * @param error
     * The error
     */    public void setError(Boolean error) {
        this.error = error;
    }

    /**
     *
     * @return
     * The message
     */
    public String getMessage() {
        return message;
    }

    /**
     *
     * @param message
     * The message
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     *
     * @return
     * The doctorid
     */
    public Integer getDoctorid() {
        return doctorid;
    }

    /**
     *
     * @param doctorid
     * The doctorid
     */
    public void setDoctorid(Integer doctorid) {
        this.doctorid = doctorid;
    }

    /**
     *
     * @return
     * The active
     */
    public Boolean getActive() {
        return active;
    }

    /**
     *
     * @param active
     * The active
     */
    public void setActive(Boolean active) {
        this.active = active;
    }

}
