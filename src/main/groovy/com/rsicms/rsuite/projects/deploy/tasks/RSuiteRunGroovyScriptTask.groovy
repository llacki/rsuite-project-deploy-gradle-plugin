package com.rsicms.rsuite.projects.deploy.tasks

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

import com.rsicms.rsuite.projects.deploy.config.RSuiteDeploymentConfig
import com.rsicms.rsuite.projects.deploy.domain.RSuiteGroovyScriptRunner

class RSuiteRunGroovyScriptTask extends DefaultTask {

	@TaskAction
	def runGroovyScript() {
		RSuiteDeploymentConfig deploymentConfig = project.extensions.getByType(RSuiteDeploymentConfig)

		def RSuiteGroovyScriptRunner groovyRunner = new RSuiteGroovyScriptRunner(project, deploymentConfig)

		if (!project.hasProperty("scriptName")){
			throw new RuntimeException("'scriptName' is required")
		}

		groovyRunner.runGroovyScript(project.getProperties().get("scriptName"))
	}
}
