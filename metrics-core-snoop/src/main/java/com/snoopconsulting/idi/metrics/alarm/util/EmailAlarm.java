package com.snoopconsulting.idi.metrics.alarm.util;

import java.util.HashMap;

import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;

import com.snoopconsulting.idi.metrics.alarm.Alarm;
import com.snoopconsulting.idi.metrics.alarm.AlarmConfig;
import com.yammer.metrics.core.Metric;

/**
 * The Class EmailAlarm. This alarm has the behavior to send a mail when it is
 * activated
 */
public class EmailAlarm extends Alarm {

    /**
     * Mail del SMTP
     */
    protected String mail;

    /**
     * Password of the SMTP mail
     */
    protected String password;

    /**
     * Address of the SMTP server
     */
    protected String smtpAddres;

    /**
     * SMTP Port
     */
    protected Integer smtpPort;

    /**
     * Indicates whether the connection to the SMTP server uses TLS
     */
    protected boolean TLS;

    /**
     * The subject of the mail
     */
    protected String issue;

    /**
     * The recipient of the mail
     */
    protected String addressee;

    /**
     * Collecting the positive results of the alarms
     */
    protected HashMap<String, String> col = new HashMap<String, String>();

    /**
     * Instantiates a new email alarm.
     *
     * @param mail SMTP Mail
     * @param password Password of the SMTP mail
     * @param smtp Address of the SMTP server
     * @param puertoSmtp SMTP server port
     * @param tLS Indicates whether the server uses TLS
     * @param asuntoMail The subject of the mail
     * @param destinatario addressee
     */
    public EmailAlarm(String mail, String password, String smtp,
            int puertoSmtp, boolean tLS, String asuntoMail, String destinatario) {
        super();
        this.mail = mail;
        this.password = password;
        this.smtpAddres = smtp;
        this.smtpPort = puertoSmtp;
        this.TLS = tLS;
        this.issue = asuntoMail;
        this.addressee = destinatario;
    }

    /*
     * Stored in an instance variable the positive results of screening alarm
     */
    @Override
    public void checkMetric(boolean resultado, Metric metric,
            Double valorActual, AlarmConfig alarmConfig) {
        if (resultado) {
            String str = "Se a activado la alerta correpondiente a \""
                    + alarmConfig.getMetricName() + "\"";

            str += " porque el parametro " + alarmConfig.getParameter();

            String negado = (alarmConfig.isNegated()) ? " no" : "";

            if (alarmConfig.getThreshold().equals(AlarmConfig.THRESHOLD.ABOVE)) {
                str += "= " + valorActual + negado
                        + " es superior al especificado ("
                        + alarmConfig.getValue() + ")";
            } else if (alarmConfig.getThreshold().equals(
                    AlarmConfig.THRESHOLD.BELOW)) {
                str += " es inferior al especificado ("
                        + alarmConfig.getValue() + ")";
            } else {
                str += negado + " se encuentra en el rango especificado ("
                        + alarmConfig.getValue() + ","
                        + alarmConfig.getValueMax() + ")";
            }

            col.put(alarmConfig.getMetricName(), str);
        }
    }

    /*
     * In case there are any results, it is sent by mail and deleted the results sent
     */
    @Override
    public void alarmTimer() {
        if (!this.col.isEmpty()) {
            try {
                String msg = "";
                for (String s : col.values()) {
                    msg += s + "\n";
                }

                Email email = new SimpleEmail();
                email.setSmtpPort(this.getSmtpPort());
                email.setAuthenticator(new DefaultAuthenticator(this.getMail(),
                        this.getPassword()));
                email.setDebug(false);
                email.setHostName(this.getSmtpAddres());
                email.setFrom(this.getMail());
                email.setSubject(this.getIssue());
                email.setMsg(msg);
                email.addTo(this.getAddressee());
                email.setTLS(this.isTLS());
                email.setCharset(org.apache.commons.mail.EmailConstants.UTF_8);
                email.send();
                col.clear();
                log.info("Se ha enviado el mail");
            } catch (EmailException e) {
                Alarm.log
                        .error("No se ha podido enviar el mail: la configuracion actual es: "
                                + this);
            }
        }
    }

    /**
     * Gets the mail.
     *
     * @return the mail
     */
    public String getMail() {
        return mail;
    }

    /**
     * Sets the mail.
     *
     * @param mail the new mail
     */
    public void setMail(String mail) {
        this.mail = mail;
    }

    /**
     * Gets the password.
     *
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the password.
     *
     * @param password the new password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Gets the smtp addres.
     *
     * @return the smtp addres
     */
    public String getSmtpAddres() {
        return smtpAddres;
    }

    /**
     * Sets the smtp addres.
     *
     * @param smtpAddres the new smtp addres
     */
    public void setSmtpAddres(String smtpAddres) {
        this.smtpAddres = smtpAddres;
    }

    /**
     * Gets the smtp port.
     *
     * @return the smtp port
     */
    public int getSmtpPort() {
        return smtpPort;
    }

    /**
     * Sets the smtp port.
     *
     * @param smtpPort the new smtp port
     */
    public void setSmtpPort(Integer smtpPort) {
        this.smtpPort = smtpPort;
    }

    /**
     * Checks if is tls.
     *
     * @return true, if is tls
     */
    public boolean isTLS() {
        return TLS;
    }

    /**
     * Sets the tls.
     *
     * @param tLS the new tls
     */
    public void setTLS(boolean tLS) {
        TLS = tLS;
    }

    /**
     * Gets the issue.
     *
     * @return the issue
     */
    public String getIssue() {
        return issue;
    }

    /**
     * Sets the issue.
     *
     * @param issue the new issue
     */
    public void setIssue(String issue) {
        this.issue = issue;
    }

    /**
     * Gets the addressee.
     *
     * @return the addressee
     */
    public String getAddressee() {
        return addressee;
    }

    /**
     * Sets the addressee.
     *
     * @param addressee the new addressee
     */
    public void setAddressee(String addressee) {
        this.addressee = addressee;
    }

    /**
     * Gets the col.
     *
     * @return the col
     */
    public HashMap<String, String> getCol() {
        return col;
    }

    /**
     * Sets the col.
     *
     * @param col the col
     */
    public void setCol(HashMap<String, String> col) {
        this.col = col;
    }

    @Override
    public String toString() {
        return "EmailAlarm [mail=" + mail + ", password=" + password
                + ", smtpAddres=" + smtpAddres + ", smtpPort=" + smtpPort
                + ", TLS=" + TLS + ", issue=" + issue + ", addressee="
                + addressee + ", col=" + col + "]";
    }

}
