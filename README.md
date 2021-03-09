# Tracer-user-testing
--------
# Java Tracer
---
## Goal of the tool
The goal of this tool is to record the execution of a Java program as it is being run.
## User survey
Please try using the tool following the instructions below and take note of how long the commands take to run (preferably in approximate seconds). Then answer some short questions in this form: https://forms.gle/o7RS2b1SeiQedTZx6

Two example programs are in the project. Example program 1 is a small program from http://www.wellho.net/resources/ex.php4?item=j713/d2x.java with 3 class files that processes rail station statistics. Example program 2 is Commons Lang (https://commons.apache.org/proper/commons-lang/) and is accompanied by a set of tests and RunSomeTests that will run the tests and save the trace of each test to a file. RunSomeTests also produces a file timing.txt that specifies how long it takes to run the tests.

The Tracer tool and the example programs are compiled with Java 8, but later versions will likely still work.

Give it a good try but if it doesn’t work, please do still answer the questions in the form and specify at what step it went wrong and what happened. 
## Running the tracing tool
1.	Download and unzip the tracing tool from the repository.

2.	Try running the program to be traced from the command line. 

    Example program 1 is run by running the main method in StationStatistics.
The tests of example program 2 is run by the main method in RunSomeTests. The junit jar, the class folder (where org is found, the base of the package), and the folder of the test classes. For example, from the example_program_1 directory, 
java -cp junit-platform-console-standalone-1.7.0.jar:.:classes:test-classes/ RunSomeTests

    Before moving on, remove the pass and fail folders and the timing and notincluded files. To avoid saving the trace files of example program 1 which might get large later, add the option no-saving at the end. 
3.	Run the tracing tool on the program class files:
java -jar Tracer.jar [trace options] [soot options]

    **Soot options:** You can specify which class files to instrument, or you can use the soot option -process-dir to instrument all class files in a directory. For example, instrumenting everything in the folder program/classes: 
java -jar Tracer.jar [trace options] -cp .program/classes -process-dir .program/classes

    The soot classpath needs to point to the folder which contains the classes to be instrumented. If you are instrumenting a package or classes in a package, the soot class path should point to the folder that contains the base of the package.

    On Windows, the path separator for the class path is the semi-colon ; and you might need to enclose the class path in quotation marks “”. 
    
    For more Soot options see: https://soot-build.cs.uni-paderborn.de/public/origin/develop/soot/soot-develop/options/soot_options.htm 
    
    **Trace options:** There are two methods of tracing. Not specifying the method will result invocations with arguments and return values. 
    
    1\)	Method 1 traces the method invocations in the order that they return to the caller method. 
        *invocations*  - Include the caller and callee method names in the trace. 
        *arguments* - Include the arguments values in the trace.
        *return-values* - Include the return values in the trace.
        
    2\)	Method 2 prints any method in the instrumented classes that are executed.
        *visited-methods* - Will include the function name of the visited methods in the order they are executed. 
        
    Options that can be used with both methods: 
*include-class* - Write the methods as: Class.method name. 
*encode-unsigned* - Encodes the Strings as byte arrays and converts numbers to their unsigned representation, excluding floats.
4.	Run the compiled output test program, found in the sootOutput folder, in the same way as in step 1. You may need to copy data files/folders to the target directory (rstats2019.xyz for example program 1) and modify class paths to point to the classes in sootOutput.



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

