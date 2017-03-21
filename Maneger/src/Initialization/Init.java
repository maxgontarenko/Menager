package Initialization;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

public class Init {
	
	public SystemSetting Info = null;
	public ConnectionSettings ConnSet = null;
	
	public Init() {
		GetSystemSettings();
		GetConnectionSetings();
	    System.out.println("Available processors (cores): " + Info.AvailableProcessors);
	    System.out.println("Free memory (bytes): " + Info.FreeMemory);
	    System.out.println("Maximum memory (bytes): " + Info.MaxMemory);
	    System.out.println("Total memory available to JVM (bytes): " + Info.TotalMemory);
	    System.out.println("Port number: " + ConnSet.Port);
	    System.out.println("Ip: "+ ConnSet.ServerIp);
	  }
	
	protected void GetConnectionSetings(){
		Gson gson = new Gson();
		JsonReader reader = null;
		ConnectionSettings conn = null;
		try {
			reader = new JsonReader(new FileReader(InitConstants.Connection_Setting_File_Name));
		} catch (FileNotFoundException e) {
			System.out.println("Create new "+InitConstants.Connection_Setting_File_Name);
			conn = new ConnectionSettings(InitConstants.Port_Number,InitConstants.Server_Ip);
			try{
			    PrintWriter writer = new PrintWriter(InitConstants.Connection_Setting_File_Name, "UTF-8");
			    writer.println(gson.toJson(conn));
			    writer.close();
			} catch (IOException e1) {
			  System.out.println("Fatal error: can't create file settings");
			  System.exit(0);
			}
			try{
				reader = new JsonReader(new FileReader(InitConstants.Connection_Setting_File_Name));
			}catch(FileNotFoundException e2){
				 System.out.println("Fatal error: can't open file settings");
				  System.exit(0);
			}
		}
		this.ConnSet = gson.fromJson(reader, ConnectionSettings.class);
		try {
			reader.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	protected void GetSystemSettings(){
    	int cores = Runtime.getRuntime().availableProcessors();
    	/*free memory and max memory in bytes*/
    	long freeMemory = Runtime.getRuntime().freeMemory();
    	long maxMemory = Runtime.getRuntime().maxMemory();
    	long totalMemory = Runtime.getRuntime().totalMemory();
    	this.Info = new SystemSetting(cores,freeMemory,maxMemory,totalMemory);
    }
	
}

/*	     Get a list of all filesystem roots on this system 
	    File[] roots = File.listRoots();

	     For each filesystem root, print some info 
	    for (File root : roots) {
	      System.out.println("File system root: " + root.getAbsolutePath());
	      System.out.println("Total space (bytes): " + root.getTotalSpace());
	      System.out.println("Free space (bytes): " + root.getFreeSpace());
	      System.out.println("Usable space (bytes): " + root.getUsableSpace());
	    }*/
