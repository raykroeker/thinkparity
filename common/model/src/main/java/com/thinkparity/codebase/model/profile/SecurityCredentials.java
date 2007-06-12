/*
 * Created On:  30-May-07 10:01:57 AM
 */
package com.thinkparity.codebase.model.profile;

/**
 * <b>Title:</b>thinkParity CommonModel Profile Security Credentials<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class SecurityCredentials {

    /** A security question answer <code>String</code>. */
    private String answer;

    /** A security question <code>String</code>. */
    private String question;

    /**
     * Create SecurityCredentials.
     *
     */
    public SecurityCredentials() {
        super();
    }

    /**
     * Obtain answer.
     *
     * @return A String.
     */
    public String getAnswer() {
        return answer;
    }

    /**
     * Obtain question.
     *
     * @return A String.
     */
    public String getQuestion() {
        return question;
    }

    /**
     * Set answer.
     *
     * @param answer
     *		A String.
     */
    public void setAnswer(String answer) {
        this.answer = answer;
    }

    /**
     * Set question.
     *
     * @param question
     *		A String.
     */
    public void setQuestion(String question) {
        this.question = question;
    }
}
