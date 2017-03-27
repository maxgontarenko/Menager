package JobControler;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import Initialization.InitConstants;

public class JobControler extends Thread{
	
	protected File JobSourceURL = null;
	protected BufferedReader ReaderFromFile = null;
	protected String JobURL = null;
	protected Thread t;
	
	public JobControler(){
		JobSourceURL = new File("JobSource.txt");
		try {
			this.ReaderFromFile = new BufferedReader(new FileReader(this.JobSourceURL));
		} catch (FileNotFoundException e) {
			/**
			 * Create new file 
			 */
			 PrintWriter writer;
			try {
				writer = new PrintWriter("JobSource.txt", "UTF-8");
				writer.println("");
				writer.close();
				JobSourceURL = new File("JobSource.txt");
				this.ReaderFromFile = new BufferedReader(new FileReader(this.JobSourceURL));
			} catch (FileNotFoundException e1) {
				System.out.println("Fatal error: in JobControler FileNotFoundException");
				e1.printStackTrace();
				System.exit(0);
			} catch (UnsupportedEncodingException e1) {
				System.out.println("Fatal error: in JobControler UnsupportedEncodingException");
				e1.printStackTrace();
				System.exit(0);
			}
		}
	}
	
	public void run(){
		this.WaitForNewJob();
		
	}
	
	public void start(){
		if(this.t == null){
			t = new Thread(this,"Job Controler");
			t.start();
		}
	}
	
	/**
	 * Waiting for new Job, by given a URL to jar file
	 * That function is implement by busy loop
	 */
	public void WaitForNewJob(){
		String text = null;
		try {
			//Get text from file
			while ((text = this.ReaderFromFile.readLine()) == null) {
			}
		} catch (IOException e) {
			System.out.println("Can't reade from Job file, Check if the file exist");
			e.printStackTrace();
		}
		this.JobURL = text;
	}
}
