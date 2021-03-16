# Tracer-user-testing
--------
# Java Tracer
---
## Goal of the tool
The goal of this tool is to record the execution of a Java program as it is being run.
## User survey
Please try using the tool following the instructions below and take note of how long the commands take to run. Then answer some short questions in this form: https://forms.gle/o7RS2b1SeiQedTZx6

There are two example programs to instrument. Example program 1 is a small program from http://www.wellho.net/resources/ex.php4?item=j713/d2x.java with 3 class files that processes railway statistics. Example program 2 is Commons Lang (https://commons.apache.org/proper/commons-lang/) and is accompanied by a set of tests and RunSomeTests.class that will run the tests and save the trace of each test to a file.

Give it a good try but if it doesn’t work, please do still fill out the form.
## Running the tracing tool

You will need Java to run the tool. It is compiled with Java 8 and should also work with later versions.

1.	Download the tracing tool.

2.	Try running the programs to be traced from the command line. 

    Example program 1: \
		Linux: java -cp classes:. RunStationStatistics rstats2019.xyz \
		Windows: java -cp "classes;." RunStationStatistics rstats2019.xyz
	
	Example program 2: \
	    Linux : java -cp junit-platform-console-standalone-1.7.0.jar:.:classes:test-classes/ RunSomeTests \
	    Windows: java -cp "junit-platform-console-standalone-1.7.0.jar;.;classes;test-classes/" RunSomeTests
	
	Before moving on, remove the folder/files pass, fail, timing, and notincluded. 

3.	Run the tracing tool on the program class files: java -jar Tracer.jar [trace options] [soot options] (see the example below)

    **Soot options:** You can use the soot option -process-dir to instrument all class files in a directory. Do this form the base directory, where Tracer.jar is. Replace [trace options] with the options described below or leave it blank. \
	java -jar Tracer.jar [trace options] -cp ./example_program_1/classes -process-dir ./example_program_1/classes \
	java -jar Tracer.jar [trace options] -cp ./example_program_2/classes -process-dir ./example_program_2/classes
    	
	For more Soot options see: https://soot-build.cs.uni-paderborn.de/public/origin/develop/soot/soot-develop/options/soot_options.htm 
    
    **Trace options:** There are two methods of tracing. Not specifying any trace options will result in tracing method invocations with arguments and return values. 
    
    1\)	Method 1 traces the method invocations in the order that they return to the caller method.  
        *invocations*  - Include the caller and callee method names in the trace lines.  
        *arguments* - Include the arguments values in the trace lines.  
        *return-values* - Include the return values in the trace lines.  
		*encode-unsigned* - Encodes the argument and return values. Encodes strings as byte arrays and converts numbers to their unsigned representation, excluding floats.
        
    2\)	Method 2 prints any method in the instrumented classes that are executed.  
        *visited-methods* - Will specify the use of method 2 and include the function name of the visited methods in the order they are executed.  
       
    Options that can be used with both methods:  
		*include-class* - Write the methods as: Class.method name.  
		
4.	Run the compiled output example program, by pointing the classpath to the sootOutput folder.

	For example program 1, 
		Linux: java -cp ../sootOutput:. RunStationStatistics rstats2019.xyz \
		Windows: java -cp "../sootOutput;." RunStationStatistics rstats2019.xyz
	
	For example program 2, you need to modify the classpath to point to the new classes in sootOutput, but still run the command from the original program folder: \
	    Linux : java -cp junit-platform-console-standalone-1.7.0.jar:.:../sootOutput:test-classes/ RunSomeTests \
	    Windows: java -cp "junit-platform-console-standalone-1.7.0.jar;.;../sootOutput;test-classes/" RunSomeTests








### Directory structure
* Tracer.jar the Tracer tool
* example_program_1
    * StationStatistics.class used to run the program
    * Stations.class used by StationStatistics 
    * MyStream.class used by StationStatistics
    * rstats2019.xyz contains the data used by the program
* example_program_2
    * RunSomeTests.class used to run the tests
    * TestRunnerSomeTests.class used by RunSomeTests
    * classes contains the package with the program class files
    * test-classes contains the package with tests
    * junit-platform-console-standalone-1.7.0.jar include in the classpath to run the tests

