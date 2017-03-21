package SocketTool;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

public class SocketTools {
	
	protected Socket sock = null;
	protected OutputStream OutPut = null;
	protected InputStream InPut = null;
	
	/**
	 * SocketTools - Provides tools to explore Socket connection 
	 * with non block functions
	 * @param ClientSocket - socket client
	 */
	public SocketTools(Socket ClientSocket){
		this.sock = ClientSocket;
		try {
			this.OutPut = this.sock.getOutputStream();
			this.InPut = this.sock.getInputStream();
		} catch (IOException e) {
			System.out.println("The Clieant Socket is NULL");
			e.printStackTrace();
			System.exit(0);
		}
	}
	
	/***
	 * Check if has new data from socket
	 * 
	 * @return true if had new data , false other
	 */
	public boolean IfNextReceive(){
		int numberOfByte = 0;
		try {
			numberOfByte = this.InPut.available();
		} catch (IOException e) {
			System.out.println("The Clieant Socket is NULL");
			e.printStackTrace();
		}
		if(numberOfByte>0)
			return true;
		return false;
	}
	
	/**
	 * Receive new massage from socket 
	 * @param type - Type of Object that gson gonna be converted 
	 * @return
	 */
	public Object ReceiveGson(Type type){
		Gson gsn = new Gson();
		String msg = this.ReceiveString();
		SendMessage rtn = gsn.fromJson(msg, new TypeToken<SendMessage>(){}.getType());
		return rtn;
	}
	
	/**
	 * Send Object to client whit Gson format
	 * @param msg - the Gson Object
	 * @param type - Type of Gson object
	 */
	public void SendGson(Object msg,Type type){
		Gson gsn = new Gson();
		this.SendString(gsn.toJson(msg,type));
	}
	
	/**
	 * Receive data from client and convert that data to string
	 * @return Received String
	 */
	public String ReceiveString(){
		String rtn = null;
		byte[] read = null;
		try {
			read = new byte[this.InPut.available()];
			this.InPut.read(read);
		} catch (IOException e) {
			System.out.println("Can't read bytes from client");
			e.printStackTrace();
		}
		rtn = new String(read);	//convert data to String Object
		return rtn;
	}
	
	/**
	 * Send string to client
	 * 
	 * @param str - That string we want to send to client
	 */
	public void SendString(String str){
		byte[] sendBytes = str.getBytes();
		try {
			this.OutPut.write(sendBytes, 0, str.length());
		} catch (IOException e) {
			System.out.println("The strint not sended to client");
			e.printStackTrace();
		}
	}
	
	/**
	 * Return true if connection is closed
	 * @return true - connection closed
	 * */
	public boolean isColse(){
		return this.sock.isClosed();
	}
	
}
