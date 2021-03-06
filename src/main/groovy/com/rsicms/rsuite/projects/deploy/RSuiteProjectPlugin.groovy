package com.rsicms.rsuite.projects.deploy

import static com.rsicms.rsuite.projects.deploy.tasks.RSuiteRunGroovyScriptsTask.CONFIGURATION_RSUITE_GROOVY

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.artifacts.dsl.RepositoryHandler

import com.rsicms.rsuite.projects.deploy.config.RSuiteDeploymentConfig
import com.rsicms.rsuite.projects.deploy.tasks.*;

class RSuiteProjectPlugin  implements Plugin<Project> {

	private static final String GRADLE_RSUITE = 'RSuite'

	public static final String RSUITE_DEPLOYMENT_EXTENSION = 'rsuiteDeployment'

	void apply(Project project) {

		RSuiteDeploymentConfig deploymentConfig = project.extensions.create(RSUITE_DEPLOYMENT_EXTENSION, RSuiteDeploymentConfig, project)


		configureDefaultRepositories(project)
		addRSuiteGroovyDependencies(project, deploymentConfig)
		addRSuitePluginsDependencies(project, deploymentConfig)

		Task compileGroovyHelperClassesTask = project.task('developmentCompileGroovyHelperClasses', type: RSuiteDevelopmentCompileGroovyHelperClassesTask) {
			group = GRADLE_RSUITE
			description = "This task compiles groovy helper classes"
		}
		
		
		project.task('runAllGroovyScripts', type: RSuiteRunGroovyScriptsTask) {
			group = GRADLE_RSUITE
			description = "This task run project groovy scripts"
		}


		Task runGroovyScript = project.task('runGroovyScript', type: RSuiteRunGroovyScriptTask) {
			group = GRADLE_RSUITE
			description = "This task runs specific groovy script, provided as parameter 'scriptName'"
		}
		
		Task devRunGroovyScript = project.task('developmentRunGroovyScript', type: RSuiteRunGroovyScriptTask) {
			group = GRADLE_RSUITE
			description = "This task runs specific groovy script, provided as parameter 'scriptName'"
		}
		
		devRunGroovyScript.dependsOn("developmentCompileGroovyHelperClasses")
		
		

		project.task('deployRSuitePlugins', type: RSuiteDeployRSuitePluginsTask) {
			group = GRADLE_RSUITE
			description = "This task deploys all rsuite plugins related with the project"
		}
		
		Task moduleTask = project.task('deployXqueryModules', type: RSuiteDeployXqueryModulesTask) {
			group = GRADLE_RSUITE
			description = "This task deploys xquery modules"
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
		task.dependsOn("deployXqueryModules")

		Task deployTask = project.task('developmentDeploy', type: RSuiteDevelopmentJarOnlyDeployTasks) {
			group = GRADLE_RSUITE
			description = "Deploys RSuite plugins and runs groovy scripts"
		}
		
		
		deployTask.dependsOn("developmentCompileGroovyHelperClasses")
		deployTask.dependsOn("runAllGroovyScripts")
		deployTask.dependsOn("developmentDeployPluginJarOnly")
		deployTask.dependsOn("deployXqueryModules")
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
			project.dependencies.add(CONFIGURATION_RSUITE_GROOVY, project.files('build/groovyHelperClasses'))
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
