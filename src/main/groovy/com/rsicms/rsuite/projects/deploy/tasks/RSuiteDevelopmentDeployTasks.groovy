package com.rsicms.rsuite.projects.deploy.tasks

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

import com.rsicms.rsuite.projects.deploy.domain.RSuitePluginDeployer

class RSuiteDevelopmentDeployTasks extends RSuiteDevelopmentJarOnlyDeployTasks {

	@TaskAction
	void deploy(){
		deployPluginsJarOnly();
	}
}
