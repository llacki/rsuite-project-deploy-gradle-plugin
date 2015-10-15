package com.rsicms.rsuite.projects.deploy

import static com.rsicms.rsuite.projects.deploy.tasks.RSuiteRunGroovyScriptsTask.CONFIGURATION_RSUITE_GROOVY

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.artifacts.dsl.RepositoryHandler

import com.rsicms.rsuite.projects.deploy.config.RSuiteDeploymentConfig
import com.rsicms.rsuite.projects.deploy.tasks.RSuiteDeployRSuitePluginsTask
import com.rsicms.rsuite.projects.deploy.tasks.RSuiteDevelopmentBuildPackageTask
import com.rsicms.rsuite.projects.deploy.tasks.RSuiteDevelopmentJarOnlyDeployTasks
import com.rsicms.rsuite.projects.deploy.tasks.RSuiteRunGroovyScriptTask
import com.rsicms.rsuite.projects.deploy.tasks.RSuiteRunGroovyScriptsTask

class RSuiteProjectPlugin  implements Plugin<Project> {

	private static final String GRADLE_RSUITE = 'RSuite'

	public static final String RSUITE_DEPLOYMENT_EXTENSION = 'rsuiteDeployment'

	void apply(Project project) {

		RSuiteDeploymentConfig deploymentConfig = project.extensions.create(RSUITE_DEPLOYMENT_EXTENSION, RSuiteDeploymentConfig, project)

				
		configureDefaultRepositories(project)
		addRSuiteGroovyDependencies(project, deploymentConfig)
		addRSuitePluginsDependencies(project, deploymentConfig)

		project.task('runAllGroovyScripts', type: RSuiteRunGroovyScriptsTask) {
			group = GRADLE_RSUITE
			description = "This task run project groovy scripts"
		}

		project.task('runGroovyScript', type: RSuiteRunGroovyScriptTask) {
			group = GRADLE_RSUITE
			description = "This task runs specific groovy script, provided as parameter 'scriptName'"
		}

		project.task('deployRSuitePlugins', type: RSuiteDeployRSuitePluginsTask) {
			group = GRADLE_RSUITE
			description = "This task deploys all rsuite plugins related with the project"
		}
		
		Task deployRStuiteProjectTask = project.task('deployRSuiteProject', type: RSuiteDeployRSuitePluginsTask) {
			group = GRADLE_RSUITE
			description = "This task deploys all rsuite plugins related with the project and runs all groovy scripts"
		}
		deployRStuiteProjectTask.dependsOn('runAllGroovyScripts');

		project.task('developmentBuildPluginPackage', type: RSuiteDevelopmentBuildPackageTask) {
			group = GRADLE_RSUITE
			description = "Call buildPackage task from the main build gradle file"
		}

		Task task = project.task('developmentDeployPluginJarOnly', type: RSuiteDevelopmentJarOnlyDeployTasks) {
			group = GRADLE_RSUITE
			description = "Deploys RSuite plugins only without running groovy scripts"
		}
		task.dependsOn("developmentBuildPluginPackage")

		Task deployTask = project.task('developmentDeploy', type: RSuiteDevelopmentJarOnlyDeployTasks) {
			group = GRADLE_RSUITE
			description = "Deploys RSuite plugins only and runs groovy scripts"
		}

		deployTask.dependsOn("runAllGroovyScripts")
		deployTask.dependsOn("developmentDeployPluginJarOnly")
	}

	private String getRSuiteIvyRepositoryUrl(){
		Properties props = new Properties()
		InputStream is =  getClass().getResourceAsStream("/plugin.properties")
 		props.load(is)
		props.get("rsuiteIvyRepositoryUrl")
	}
	
	private void configureDefaultRepositories(Project project){
		RepositoryHandler repositories = project.getRepositories();
		repositories.mavenCentral();
		repositories.ivy({
			url getRSuiteIvyRepositoryUrl()
			layout "pattern", { artifact "[organisation]/[module]/[artifact]-[revision].[ext]" }
		}
		)
	}

	private void addRSuiteGroovyDependencies(Project project, RSuiteDeploymentConfig deploymentConfig){
		def providedConf = project.configurations.create(CONFIGURATION_RSUITE_GROOVY)

		project.afterEvaluate {
			project.dependencies.add(CONFIGURATION_RSUITE_GROOVY, "rsi:rsuite-tools-groovy:20130208")
			project.dependencies.add(CONFIGURATION_RSUITE_GROOVY, project.fileTree(dir:  deploymentConfig.getRSuiteHome() + '/clients/admin/lib', include: '*.jar'))
		}
	}

	private void addRSuitePluginsDependencies(Project project, RSuiteDeploymentConfig deploymentConfig){
		String CONFIGURATION_RSUITE_PLUGINS =  "rsuitePlugins";
		def providedConf = project.configurations.create(CONFIGURATION_RSUITE_PLUGINS)

		project.afterEvaluate {
			deploymentConfig.rsuitePluginsConfig.rsuitePlugins.each { plugin ->
				project.dependencies.add(CONFIGURATION_RSUITE_PLUGINS, plugin)
			}
		}
	}
}