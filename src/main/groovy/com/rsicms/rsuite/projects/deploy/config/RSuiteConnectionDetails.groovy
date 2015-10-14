package com.rsicms.rsuite.projects.deploy.config

class RSuiteConnectionDetails {

	String userName;
	
	String password;
	
	String port;
	
	String host;

	public RSuiteConnectionDetails(String userName, String password, String host, String port) {
		this.userName = userName;
		this.password = password;
		this.port = port;
		this.host = host;
	}
	
}
