package com.rsicms.rsuite.projects.deploy.tasks

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

import com.marklogic.client.DatabaseClient
import com.marklogic.client.DatabaseClientFactory
import com.marklogic.client.DatabaseClientFactory.Authentication
import com.marklogic.client.modulesloader.ModulesFinder
import com.marklogic.client.modulesloader.impl.*
import com.rsicms.rsuite.projects.deploy.config.RSuiteDeploymentConfig
import com.rsicms.rsuite.projects.deploy.config.RSuiteXqueryModulesConfig;
import com.rsicms.rsuite.projects.deploy.domain.RSuiteModulesFinder
import com.rsicms.rsuite.projects.deploy.domain.RSuiteXccAssetLoader

class RSuiteDeployXqueryModulesTask extends DefaultTask {

	@TaskAction
	deployXqueryModules(){

		RSuiteDeploymentConfig deploymentConfig = project.extensions.getByType(RSuiteDeploymentConfig)
		RSuiteXqueryModulesConfig xqueryModulesConfig = deploymentConfig.getRsuiteXqueryModulesConfig();

		def modulesHome = getModulesHome()

		removeCustomXqueryModules(xqueryModulesConfig, modulesHome)

		copyXqueryModules(xqueryModulesConfig, modulesHome)
		
		loadModulesToDatabase(deploymentConfig, modulesHome)
	}

	private loadModulesToDatabase(RSuiteDeploymentConfig deploymentConfig, String modulesHome) {
		def modulesDatabaseName = getModulesDatabaseName()

		if (modulesDatabaseName != null){
			
			Properties props = new Properties()
			
			new File(deploymentConfig.getRSuiteHome(),"/conf/rsuite.properties").withInputStream { stream ->
				props.load(stream)
			}

			def user = props.getProperty("rsuite.repository.user")
			def host = props.getProperty("rsuite.repository.server")
			def password = props.getProperty("rsuite.repository.password")
			password = password.replace("plain:", "")

			
			DatabaseClient client = DatabaseClientFactory.newClient(host, 8000, user, password, Authentication.DIGEST);

			RSuiteXccAssetLoader assetLoader = new RSuiteXccAssetLoader();
			assetLoader.setHost(host);
			assetLoader.setUsername(user);
			assetLoader.setPassword(password);
			assetLoader.setDatabaseName(modulesDatabaseName);
			
			println  "Loading moduels to DB $modulesDatabaseName: dir: $modulesHome"


			DefaultModulesLoader modulesLoader = new DefaultModulesLoader(assetLoader);
			File modulesDir = new File(modulesHome);
			ModulesFinder modulesFinder = new RSuiteModulesFinder();
			modulesLoader.loadModules(modulesDir, modulesFinder, client);
		}
	}

	private copyXqueryModules(RSuiteXqueryModulesConfig xqueryModulesConfig, String modulesHome) {
		new AntBuilder().copy( todir:modulesHome ) {
			fileset( dir:xqueryModulesConfig.sourceDir,  includes: '**/*' )
		  }
	}

	private removeCustomXqueryModules(RSuiteXqueryModulesConfig xqueryModulesConfig, String modulesHome) {
		if (xqueryModulesConfig.pattern != null){
			def tree = project.fileTree(modulesHome) {
				include xqueryModulesConfig.pattern
			}

			project.delete(tree)
		}
	}

	private String getModulesHome() {
		def modulesHome = "${project.ext.get('rsuite.home')}/modules_1_0"

		if (project.hasProperty("rsuite.modules.home"))
			modulesHome = "${project.ext.get('rsuite.modules.home')}"
		return modulesHome
	}
	
	private String getModulesDatabaseName() {
		def modulesDatabaseName = null;

		if (project.hasProperty("rsuite.modules.database.name"))
			modulesDatabaseName = "${project.ext.get('rsuite.modules.database.name')}"
		return modulesDatabaseName
	}
}
