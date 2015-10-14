package com.rsicms.rsuite.projects.deploy.config

import java.util.List;

class RSuitePluginsConfig {

	List<String> rsuitePlugins = []
		
	void plugin(String scriptName){
		rsuitePlugins.add(scriptName)
	}
}


