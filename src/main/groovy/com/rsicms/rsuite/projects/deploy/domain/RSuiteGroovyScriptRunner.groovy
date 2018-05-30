package com.rsicms.rsuite.projects.deploy.domain

import org.gradle.api.Project
import org.gradle.api.artifacts.ConfigurationContainer

import com.rsicms.rsuite.projects.deploy.config.RSuiteConnectionDetails
import com.rsicms.rsuite.projects.deploy.config.RSuiteDeploymentConfig

class RSuiteGroovyScriptRunner {

	RSuiteConnectionDetails rsuiteDetail

	String groovyDirPath

	String setUpPath;
	
	Project project;
	
	RSuiteDeploymentConfig deploymentConfig


	public RSuiteGroovyScriptRunner(Project project, RSuiteDeploymentConfig deploymentConfig){
		this.project = project
		rsuiteDetail = deploymentConfig.getRSuiteConnectionDetails();
		groovyDirPath = deploymentConfig.getGroovyBasePath();
		setUpPath = deploymentConfig.getSetupDir().absolutePath;
		this.deploymentConfig = deploymentConfig
	}


	public runGroovyScript(String scriptLine){
		def arguments = createGroovyScriptArguments(scriptLine)
		println "Running script: " + serializeArguments(arguments)
		runRSuiteGroovyScript(arguments)
	}

	private serializeArguments(arguments){
		def args = [] as List
		def param = ""

		for (String item : arguments) {

		  if (item.startsWith("-")) {
		    param = item;
		  }else if ("--password".equals(param)){
		    item="*****"
		  }
		
		  args.add(item)
		}
		
		args.toString()
	}

	private createGroovyScriptArguments(scriptLine){

		def scriptsArgs = scriptLine.split(";") as List
		def scriptFile = new File(groovyDirPath, scriptsArgs[0])
		scriptsArgs[0] = scriptFile.toURI().toURL()
		scriptsArgs.push("-DsetupPath=" + setUpPath)
		scriptsArgs.push("-DrsuiteHome=" + deploymentConfig.getRSuiteHome())

		def args = ["run", "-s"]+ scriptsArgs +   [
			"--user",
			rsuiteDetail.userName ,
			"--password",
			rsuiteDetail.password,
			"-h" ,
			rsuiteDetail.host,
			"-p" ,
			rsuiteDetail.port
		]
	}



	private runRSuiteGroovyScript(arguments) {
		ConfigurationContainer configurations = project.configurations;

		project.javaexec {
			classpath configurations.rsuiteGroovy
			main = 'com.reallysi.rsuite.admin.client.RSuiteAdminClient'
			args = arguments
		}
	}
}
