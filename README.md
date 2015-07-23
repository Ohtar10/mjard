# mjard
Maven Jar Deployer (mjard) is a command line tool to deploy orphan jar files to a maven repository, orphan meaning jars that are not present on any public maven repository but in har drive and need to be exposed in a maven repository in order to this dependencies to be used in a maven way.


### Usage:

	java - jar mjd.jar -groupId [THE GROUP ID] -masterVersion [MASTER VERSION] -d [JAR DIRS] -o [DEPENDENCIES OUPUT]
	
	-groupId		Specifies the maven group id to be 
					used when generating the directory
					Structure for the dependencies, for 
					example the groupId "com.test.groupid"
					will result in a directory structure 
					com/test/groupid where all the jars
					found by the command will reside under 
					the output path specified by the
					-o argument
					
	-masterVersion	As some of the jars that could be 
					found inside the directory where the
					jars reside may have no version specified
					on the file itself or his parent
					directory, this version will be then used
					to give the jar a default version, 
					otherwise a version found in the jar file
					like "log4j-1.7.0.jar" or the parent 
					directory ../log4j_1.7.0/log4j.jar
					will be used instead.
					
	-d				Specifies the directories where the 
					un-maven-formatted jars reside. it is
					possible to specify several directories
					if they are all comma separated, for 
					example /path/one, /path/two and so on.
					
	-o				Specifies the output directory of the 
					operation in other words, where the 
					maven-formatted dependencies will 
					reside.
					
	-l				Specifies a path to a custom log file
					where the output messages of the program
					can be appended (Optional).
					
	-h				Displays this help, if this argument is 
					present, no process will execute, this 
					message will be then shown. 

### Examples:
Given the following directory structure with jar files:
```
.
+- orphan_jars
|  +-- jar1.jar
|  +-- jar2
|  |  +--- jar2.jar
```
Execute: 
	java -jar mjd.jar -groupId com.test -masterVersion 1.0.0 -d orphan_jars -o output
knowing that:
* groupId is the maven group id you want this dependencies to have when exported in maven format.
* masterVersion is the default version you want your jars to have if a version pattern can't be found within the directories or jar files.
* d is the directory where the jars to be exported are, note the tool was executed at the same level of this directory
* o is the output directory for the jars in maven format to be placed

That would result in:
```
.
+- output
|  +-- com
|  |  +--- test
|  |  |  +---- jar1
|  |  |	 |  +----- 1.0.0
|  |  |  |  |  +------ jar1-1.0.0.jar
|  |  |  |  |  +------ jar1-1.0.0.pom
|  |  |  +---- jar2
|  |  |	 |  +----- 1.0.0
|  |  |  |  |  +------ jar2-1.0.0.jar
|  |  |  |  |  +------ jar2-1.0.0.pom
```

Additional to that, in the output root it will be generated an example pom using all the exporting dependencies to work as a template and facilitate using this dependencies in maven format.

You just need to copy the output directory content on the maven repository of your wish and the dependencies will then be available to use right away.

### Notes:
For the compiled and bundled version please use the jar placed at dist/ or you can build it yourself from source with mvn package this will generate a mjard-1.0.0-bundle.jar

### Requirements:
* This tool runs under java 8

### Disclaimer:
This tool was made with the intent of walking a directory with several jars that are not present on an existing maven repository, naturaly if you just have a single jar to deploy to a maven repository it would be easier to use command line or the repository console, this just facilitate the labor when there are several jars in a directory that need to be published.
The use of this tool is at your own discretion.
