package com.rsicms.rsuite.projects.deploy.tasks

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

import com.rsicms.rsuite.projects.deploy.domain.RSuitePluginDeployer

class RSuiteDevelopmentJarOnlyDeployTasks extends DefaultTask {


	@TaskAction
	void deployPluginsJarOnly(){
		def extraProperties = project.getExtensions().extraProperties;
		extraProperties.set("deploymentMode", "development")
		extraProperties.set("localDevelopmentDeploy", "true")

		RSuitePluginDeployer pluginDeployer = new RSuitePluginDeployer()
		pluginDeployer.deployRSuitePlugins(project, 'build/libs');
	}
}
