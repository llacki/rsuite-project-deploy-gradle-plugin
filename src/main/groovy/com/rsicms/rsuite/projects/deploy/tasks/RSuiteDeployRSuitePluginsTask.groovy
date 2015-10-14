package com.rsicms.rsuite.projects.deploy.tasks

import org.gradle.api.DefaultTask
import org.gradle.api.artifacts.Configuration
import org.gradle.api.artifacts.ConfigurationContainer
import org.gradle.api.tasks.TaskAction

import com.rsicms.rsuite.projects.deploy.config.RSuiteDeploymentConfig
import com.rsicms.rsuite.projects.deploy.domain.RSuitePluginDeployer

class RSuiteDeployRSuitePluginsTask extends DefaultTask {

	@TaskAction
	deployRSuitePlugins(){
		RSuitePluginDeployer pluginDeployer = new RSuitePluginDeployer()
		pluginDeployer.deployRSuitePlugins(project, 'plugin');
	}

}
