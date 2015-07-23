# mjard
Maven Jar Deployer (mjard) is a command line tool to deploy orphan jar files to a maven repository, orphan meaning jars that are not present on any public maven repository but in har drive and need to be exposed in a maven repository in order to this dependencies to be used in a maven way.


## Usage:

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

## Requirements:
* This tool runs under java 8