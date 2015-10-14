package com.rsicms.rsuite.projects.deploy.tasks

import org.gradle.api.DefaultTask
import org.gradle.api.artifacts.ConfigurationContainer
import org.gradle.api.tasks.TaskAction

import com.rsicms.rsuite.projects.deploy.config.RSuiteConnectionDetails
import com.rsicms.rsuite.projects.deploy.config.RSuiteDeploymentConfig
import com.rsicms.rsuite.projects.deploy.config.RSuiteGroovyConfig
import com.rsicms.rsuite.projects.deploy.domain.RSuiteGroovyScriptRunner

class RSuiteRunGroovyScriptsTask extends DefaultTask {

	public static final CONFIGURATION_RSUITE_GROOVY = "rsuiteGroovy";

	@TaskAction
	def runGroovyScripts() {

		RSuiteDeploymentConfig deploymentConfig = project.extensions.getByType(RSuiteDeploymentConfig)
		RSuiteGroovyConfig groovyConfig = deploymentConfig.groovyScripts

		runRSuiteGroovyScriptGroup(groovyConfig.commonScripts, deploymentConfig)

		if (deploymentConfig.getDeploymentMode() == "development"){
			runRSuiteGroovyScriptGroup(groovyConfig.developmentScripts, deploymentConfig)
		}else if (deploymentConfig.getDeploymentMode() == "production"){
			runRSuiteGroovyScriptGroup(groovyConfig.productionScripts, deploymentConfig)
		}
	}

	def runRSuiteGroovyScriptGroup(group, RSuiteDeploymentConfig deploymentConfig){

		def RSuiteGroovyScriptRunner groovyRunner = new RSuiteGroovyScriptRunner(project, deploymentConfig)

		group.each { script ->
			groovyRunner.runGroovyScript(script)
		}
	}
}



