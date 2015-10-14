package com.rsicms.rsuite.projects.deploy.tasks

import java.io.File

import org.gradle.api.tasks.GradleBuild

class RSuiteDevelopmentBuildPackageTask extends GradleBuild {

	RSuiteDevelopmentBuildPackageTask(){
		setTasks(['buildPluginPackage']);
	}
	
	@Override
	public File getBuildFile() {

		return new File("build.gradle");
	}	
}