package com.rsicms.rsuite.projects.deploy.config

import org.gradle.api.Project
import org.gradle.api.plugins.ExtraPropertiesExtension;

import javax.inject.Inject

class RSuiteDeploymentConfig {

	private final Project project

	RSuiteGroovyConfig groovyScripts = new RSuiteGroovyConfig();

	RSuitePluginsConfig rsuitePluginsConfig = new RSuitePluginsConfig()
	
	String rsuiteHomeDir
	
	String rsuitePluginsDir

	File setupDir =  new File("setup")
	
	private String deploymentMode = "production"

	@Inject
	RSuiteDeploymentConfig(Project project) {
		this.project = project		
	}

	void rsuiteGroovy(Closure c) {
		project.configure(groovyScripts, c)
	}
	
	void rsuitePlugins(Closure c) {
		project.configure(rsuitePluginsConfig, c)
	}

	String getGroovyBasePath(){
		File groovyScriptsBaseDir = new File(setupDir, "scripts/groovy");
		groovyScriptsBaseDir.canonicalPath
	}

	String getRSuiteHome(){
		getPropertyValue("rsuite.home", rsuiteHomeDir)
	}
	
	String getRSuitePluginsPath(){
		if (rsuitePluginsDir != null){
			return rsuitePluginsDir
		}
		
		getRSuiteHome() + "/plugins"
	}
	
	String getDeploymentMode(){
		if (!project.hasProperty("deploymentMode")) {
			return deploymentMode
		}
		
		project.getProperties().get("deploymentMode")
	}

	RSuiteConnectionDetails getRSuiteConnectionDetails(){
		loadRSuiteProperties()	
		def userName = getPropertyValue("rsuite.userName", "")
		def password = getPropertyValue("rsuite.password", "")
		def host = getPropertyValue("rsuite.server.host", "")
		def port = getPropertyValue("rsuite.server.port", "")

		return new RSuiteConnectionDetails(userName, password, host, port);
	}

	private String getPropertyValue(String propertyName, String defaultValue){

		def value = project.extensions.getExtraProperties().get(propertyName)
		if (value == null){
			defaultValue
		}

		value
	}
		
	private void loadRSuiteProperties() {
		Properties props = new Properties()
		props.load(new FileInputStream(getRSuiteHome() + "/conf/rsuite.properties"))
		ExtraPropertiesExtension extProps = project.extensions.getExtraProperties()
		extProps.set("rsuite.server.host", props.get("rsuite.server.host"))
		extProps.set("rsuite.server.port", props.get("rsuite.server.port"))
	}
}
