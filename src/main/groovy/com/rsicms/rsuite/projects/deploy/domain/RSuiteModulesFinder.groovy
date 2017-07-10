package com.rsicms.rsuite.projects.deploy.domain

import static groovy.io.FileType.FILES

import com.marklogic.client.modulesloader.Modules
import com.marklogic.client.modulesloader.ModulesFinder
import com.marklogic.client.modulesloader.impl.BaseModulesFinder

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource
import org.springframework.core.io.support.PathMatchingResourcePatternResolver
import org.springframework.core.io.support.ResourcePatternResolver

class RSuiteModulesFinder extends BaseModulesFinder implements ModulesFinder {

	private ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
	
		@Override
		public Modules findModules(File baseDir) {
			
			
			Modules modules = new Modules();
			
			addAssets(modules, baseDir)
			addServices(modules, baseDir);
			addAssetDirectories(modules, baseDir);
			addOptions(modules, baseDir);
			addTransforms(modules, baseDir);
			addNamespaces(modules, baseDir);
			addPropertiesFile(modules, baseDir);
			
			return modules
		}
		
		private addAssets(Modules modules, baseDir){
			
			modules.setAssets(getResources(baseDir, this.&createResource, "xqy", "xq"))
		}
		
		protected void addTransforms(Modules modules, File baseDir){
			modules.setTransforms(getResources(baseDir, this.&createResource, "xsl", "xslt"))
		}
		
		
		def createResource(path){
			new FileSystemResource(path)
		}
		
		def createPath(path){
			path
		}
		
		protected void addAssetDirectories(Modules modules, File baseDir) {
			List<Resource> dirs = new ArrayList<>();
			dirs.add(new FileSystemResource(baseDir));
			
//			File dir = new File(baseDir, "ext");
//			if (dir.exists()) {
//				
//			}
//			dir = new File(baseDir, "root");
//			if (dir.exists()) {
//				dirs.add(new FileSystemResource(dir));
//			}
//	
//			if (includeUnrecognizedPathsAsAssetPaths && baseDir != null && baseDir.exists()) {
//				List<String> recognizedPaths = getRecognizedPaths();
//				for (File f : baseDir.listFiles()) {
//					if (f.isDirectory() && !recognizedPaths.contains(f.getName())) {
//						dirs.add(new FileSystemResource(f));
//					}
//				}
//			}
	
			modules.setAssetDirectories(dirs);
		}
		
		private List getResources(File baseDir, Closure builder, String... extensions){
			def list= []
			
			for (String extension : extensions) {
				def ext = '.' + extension
				baseDir.eachFileRecurse(FILES) {
					if(it.name.endsWith(ext)) {
						list.add(new FileSystemResource(it))
					}}
			}
			
			list
		}

}
