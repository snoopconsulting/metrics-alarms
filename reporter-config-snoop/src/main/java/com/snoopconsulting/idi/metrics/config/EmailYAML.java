package com.snoopconsulting.idi.metrics.config;

import javax.validation.constraints.NotNull;

public class EmailYAML {
	@NotNull
	protected String mail;
	@NotNull
	protected String password;
	@NotNull
	protected String smtpAddres;
	@NotNull
	protected Integer smtpPort;
	@NotNull
	protected boolean TLS;
	@NotNull
	protected String issue;
	@NotNull
	protected String addressee;

	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getSmtpAddres() {
		return smtpAddres;
	}

	public void setSmtpAddres(String smtpAddres) {
		this.smtpAddres = smtpAddres;
	}

	public Integer getSmtpPort() {
		return smtpPort;
	}

	public void setSmtpPort(Integer smtpPort) {
		this.smtpPort = smtpPort;
	}

	public boolean isTLS() {
		return TLS;
	}

	public void setTLS(boolean tLS) {
		TLS = tLS;
	}

	public String getIssue() {
		return issue;
	}

	public void setIssue(String issue) {
		this.issue = issue;
	}

	public String getAddressee() {
		return addressee;
	}

	public void setAddressee(String addressee) {
		this.addressee = addressee;
	}

}
