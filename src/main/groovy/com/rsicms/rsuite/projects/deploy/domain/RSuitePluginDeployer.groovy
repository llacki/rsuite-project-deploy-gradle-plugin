package com.rsicms.rsuite.projects.deploy.domain

import org.gradle.api.Project;
import org.gradle.api.artifacts.Configuration
import org.gradle.api.artifacts.ConfigurationContainer

import com.rsicms.rsuite.projects.deploy.config.RSuiteDeploymentConfig

class RSuitePluginDeployer {

	public void deployRSuitePlugins(Project project, String projectPluginLocation){
		ConfigurationContainer configurations = project.configurations;
		Configuration configuration = configurations.getByName("rsuitePlugins")
		
		RSuiteDeploymentConfig deploymentConfig = project.extensions.getByType(RSuiteDeploymentConfig)
		File projectPluginDir = new File(deploymentConfig.baseDir, projectPluginLocation)
		
		project.copy {
			from configuration
			into deploymentConfig.getRSuitePluginsPath()
			rename '(.*)-[0-9]+.*(-SNAPSHOT)?\\.jar', '$1.jar'
		}
		
		project.copy {
			from projectPluginDir
			into deploymentConfig.getRSuitePluginsPath()
			rename '(.*)-[0-9]+.*(-SNAPSHOT)?\\.jar', '$1.jar'
		}
	}
}
