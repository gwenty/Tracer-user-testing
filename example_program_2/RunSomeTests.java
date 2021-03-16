import org.junit.platform.launcher.Launcher;
import org.junit.platform.launcher.LauncherDiscoveryRequest;
import org.junit.platform.launcher.core.LauncherDiscoveryRequestBuilder;
import org.junit.platform.launcher.core.LauncherFactory;
import org.junit.platform.launcher.listeners.SummaryGeneratingListener;
import org.junit.platform.launcher.listeners.TestExecutionSummary;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.platform.engine.discovery.DiscoverySelectors;

public class RunSomeTests {
	public static void main(String[] args) {
    	if(args.length > 0 && args[0].equals("no-saving")) {
    		System.out.println("The traces will be written to the standard output, not saved as files.");
    		noSaving = true;
    	}
    	runOne("test-classes" + File.separator + "all_tests", "test-classes" + File.separator+ "failing_tests");
    }
	
	static boolean noSaving = false;

    public static void runOne(String inputFile, String failFile) {
    	
    	//Create the fail directory
    	if(!noSaving) {
	    	if(Files.exists(Paths.get("fail"))) {
	    		System.out.println("You have to delete the previous trace folders first.");
	    		
	    		System.exit(0);
	    	}
	    	try {
				Files.createDirectories(Paths.get("fail"));
			} catch (IOException e2) {
				e2.printStackTrace();
			}
    	}
    	//Load the failing tests
    	List<String> failingTests = new ArrayList<String>();
    	try (BufferedReader brfail = new BufferedReader(new FileReader(new File(failFile)))) {
    	    String line;
    	    while ((line = brfail.readLine()) != null) {
    	    	if(line.charAt(0) == '-' && line.charAt(1) == '-' && line.charAt(2) == '-')
    	    		failingTests.add(line);
    	    }
    	} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
    	
    	//Timing
    	long start = System.currentTimeMillis();
//    	try
//		{
//		    String filename= "timing.txt";
//		    FileWriter fw = new FileWriter(filename,true);
//		    SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
//		    Date date = new Date(System.currentTimeMillis());
//		    fw.write("Started on: " + formatter.format(date) + System.lineSeparator() + System.lineSeparator());
//		    fw.close();
//		}
//		catch(IOException ioe)
//		{
//		    ioe.printStackTrace();
//		}
    	
    	if(!noSaving) {
	    	//Make the directory, if it does not already exist
	    	File directory = new File("pass");
	    	if (! directory.exists()){
	            directory.mkdir();
	    	}
    	}
	    	
    	int i = 1;
    	int failCounter = 1;
    	int numberOfTestsTried = 0;
    	
    	try (BufferedReader br = new BufferedReader(new FileReader(new File(inputFile)))) {
    	    String line;
    	    while ((line = br.readLine()) != null) {
    	       // process the line.
    	    	//Fine the class and method names
    	    	System.out.println(i + " " + line);
    	    	numberOfTestsTried ++;
    	    	String[] parts = line.split("[(]");
    	    	String testMethod = parts[0];
    	    	String testClass = parts[1].split("[)]")[0];
    	    	//Name of the file to write the trace to
    	    	File newFile = null;
    	    	if(!noSaving) {
    	    		newFile = new File("pass/trace" + i + ".log");
    	    	}
    	    	
    	    	TestRunnerSomeTests rt = new TestRunnerSomeTests("pass/trace" + i + ".log", testMethod, testClass, noSaving);
    	    	boolean success = rt.runTest();
	    	    	
    	    	if(noSaving && success) {
    	    		i++;
    	    	}
    	    	if(noSaving && !success) {
    	    		failCounter++;
    	    	}
    	    	
	    	    if(!noSaving) {
	    	    	//If bigger than 20 MB, delete
	    	    	long newFileSize = newFile.length();
	    	    	if(success && (newFile.length() / (1024 * 1024) > 2 || newFile.length() == 0)) {
	    	    		newFile.delete();
	    	    		
	    	    		//Write the name to a file
	    	    		try
	    	    		{
	    	    		    String filename= "notincluded.txt";
	    	    		    FileWriter fw = new FileWriter(filename,true); //the true will append the new data
	    	    		    fw.write(line + " (too big: " + newFileSize + ")" + System.lineSeparator());//appends the string to the file
	    	    		    fw.close();
	    	    		}
	    	    		catch(IOException ioe)
	    	    		{
	    	    		    ioe.printStackTrace();
	    	    		}
	    	    	}
	    	    	else {
		    	    	if(success)
		    	    		i++;
		    	    	else if(!success) {
		    	    		boolean deleteFail = true;
		    	    		
		    	    		//Get the name of the test
		    	    		for(int j = 0; j < failingTests.size(); j++) {
		    	    			//See if we want to keep it
		    	    			if(failingTests.get(j).contains(testMethod) && failingTests.get(j).contains(testClass)) {
		    	    				//Move to fail folder
		    	    				
		    	    				File newFailFile = new File("fail/trace" + failCounter + ".log");
		    	    				Path temp = Files.move(Paths.get(newFile.getAbsolutePath()), Paths.get(newFailFile.getAbsolutePath())); 
		    	    				  
		    				        if(temp != null)
		    				        { 
		    				        	newFile.delete();
		    	    					deleteFail = false;
		    	    					failCounter ++;
		    	    				} 
		    				        else
		    				        {
		    				            System.out.println("Failed to move the file"); 
		    				        }
		    				        
		    	    				//Stop looking
		    	    				break;
		    	    			}
		    	    		}
		    	    		
		    	    		//If not in failing tests, delete
		    	    		if(deleteFail) {
		    	    			//Remove the file
		        	    		newFile.delete();
		        	    		//Write the name to a file
		        	    		try
		        	    		{
		        	    			System.out.println("Not included: " + line);
		        	    		    String filename= "notincluded.txt";
		        	    		    FileWriter fw = new FileWriter(filename,true); //the true will append the new data
		        	    		    fw.write(line + System.lineSeparator());//appends the string to the file
		        	    		    fw.close();
		        	    		}
		        	    		catch(IOException ioe)
		        	    		{
		        	    		    ioe.printStackTrace();
		        	    		}
		    	    		}
		    	    	}
		    	    }
	    	    
    	    }
		    	System.out.println();
    	    }
    	} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	//Timing
    	System.out.println();
		long finish = System.currentTimeMillis();
		long timeElapsed = finish - start;
		System.out.println("Time taken: " + timeElapsed/1000 + " seconds.");
//    	try
//		{
//		    String filename= "timing.txt";
//		    FileWriter fw = new FileWriter(filename,true);
//		    SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
//		    Date date = new Date(System.currentTimeMillis());
//		    fw.write("Ended on: " + formatter.format(date) + System.lineSeparator() + System.lineSeparator());
//		    fw.write(numberOfTestsTried + " tests ran" + System.lineSeparator());
//		    fw.write(i - 1 + " tests in pass" + System.lineSeparator());
//		    fw.write(failCounter - 1 + " tests in fail" +  System.lineSeparator() +  System.lineSeparator());
//		    fw.close();
//		}
//		catch(IOException ioe)
//		{
//		    ioe.printStackTrace();
//		}
    }

}
 
class TestRunnerSomeTests {
	
     private String fileName;
     private String testMethod;
     private String testClass;
     private SummaryGeneratingListener listener;
     private boolean noSaving;
     
     
     public TestRunnerSomeTests(String fileName, String testMethod, String testClass, boolean noSaving) {
         this.fileName = fileName;
         this.testMethod = testMethod;
         this.testClass = testClass;
         listener = new SummaryGeneratingListener();
         this.noSaving = noSaving;
      }

     public boolean runTest() {
    	 try {
    		 
    		// Store current System.out before assigning a new value 
    		 PrintStream console = System.out; 
    		PrintStream toFile = null;
			
	        
	        if(!noSaving)
	        {
	        	toFile = new PrintStream(new File(fileName));
	        	System.setOut(toFile); //UNCOMMENT THIS FOR TO FILE
	        }
	        
	        //Running a single test
	        LauncherDiscoveryRequest request = LauncherDiscoveryRequestBuilder.request()
	  	          .selectors(DiscoverySelectors.selectMethod(testClass, testMethod))
	  	          .build();
	        
	        Launcher launcher = LauncherFactory.create();
	        //TestPlan testPlan = launcher.discover(request);
	        
	        launcher.registerTestExecutionListeners(listener);
	        launcher.execute(request);
	        
	        if(!noSaving)
	        	toFile.close();
			System.setOut(console);
			TestExecutionSummary summary = listener.getSummary();
			summary.printFailuresTo(new PrintWriter(System.out));
			if(summary.getTestsFailedCount()>0) {
				return false;
			}
			//summary.printTo(new PrintWriter(System.out));
			return true;
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		}
     }
  }
