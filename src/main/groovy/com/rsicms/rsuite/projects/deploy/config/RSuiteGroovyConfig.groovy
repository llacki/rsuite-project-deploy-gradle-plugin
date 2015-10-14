package com.rsicms.rsuite.projects.deploy.config


class RSuiteGroovyConfig {

	List<String> commonScripts = []

	List<String> developmentScripts = []

	List<String> productionScripts = []

	void common(String scriptName){
		commonScripts.add(scriptName)
	}
	
	void development(String scriptName){
		developmentScripts.add(scriptName)
	}
	
	void production(String scriptName){
		productionScripts.add(scriptName)
	}
}
