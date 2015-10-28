package com.rsicms.rsuite.projects.deploy.tasks

import java.io.File

import org.gradle.api.tasks.GradleBuild

class RSuiteDevelopmentCompileGroovyHelperClassesTask extends GradleBuild {

	RSuiteDevelopmentCompileGroovyHelperClassesTask(){
		setTasks(['compileGroovyHelper']);
	}
	
	@Override
	public File getBuildFile() {

		return new File("build.gradle");
	}	
}