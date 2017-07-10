package com.rsicms.rsuite.projects.deploy.config

import java.util.List;

class RSuiteXqueryModulesConfig {

	String pattern
	
	String sourceDir = 'setup/xquery_modules'
		
	void modulesPattern(String pattern){
		this.pattern = pattern
	}
	
	void sourceDir(String sourceDir){
		this.sourceDir = sourceDir 
	}
	
}


