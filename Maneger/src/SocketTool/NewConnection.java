package SocketTool;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import Initialization.ConnectionSettings;
import Initialization.SystemSetting;
import ProcessUnit.ProcessUnits;

public class NewConnection extends Thread{
	protected ServerSocket servsock = null;
	protected Thread t;
	public NewConnection(ConnectionSettings settings){
		try {
			servsock = new ServerSocket(settings.Port);
		} catch (IOException e) {
			System.out.println("Fatal error: can't open new Server Socket");
			e.printStackTrace();
			System.exit(0);
		}
	}
	
	public void run(){
		Socket sock = null;
		SocketTools s = null;
		System.out.println("NewConnection Thread is start");
		while(true){
			try {
				System.out.println("Wait for new connection");
				sock = servsock.accept();
				s = new SocketTools(sock);
				System.out.println("Accept new connection");
				System.out.println("Ip: "+sock.getInetAddress());
				RegistrasionNewClieant(s);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	protected void RegistrasionNewClieant(SocketTools cl){
		ProcessUnits unit = ProcessUnits.getInstance();
		String id = this.SendFirstMessage(cl);
		SystemSetting settings = this.GetSeckondMessage(cl);
		if(settings == null){ //If that settings is null that's means registration failed
			return;
		}
		this.SendThirdMessage(cl);
		long heartbeat = new Date().getTime();
		/*Add new worker to Data Structure*/
		for(int i = 0;i<settings.AvailableProcessors;i++){
			unit.AddNewUnit(i, id, cl, heartbeat, null, NetConstants.Wait_For_Job);
		}
	}
	
	protected void SendThirdMessage(SocketTools cl){
		SendMessage msg = new SendMessage();
		msg.MessageId = NetConstants.Wait_For_Job;
		msg.MessageString = null;
		Type type = new TypeToken<SendMessage>(){}.getType();
		cl.SendGson(msg, type);
	}
	
	@SuppressWarnings("static-access")
	protected SystemSetting GetSeckondMessage(SocketTools cl){
		SystemSetting rtn = null;
		Gson gsn = new Gson();
		int count = 0;
		Type type = new TypeToken<SendMessage>(){}.getType();
		/*waiting message from cliaent , this action is blocking*/
		while(!cl.IfNextReceive() && count<100){
			try {
				this.t.sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			count ++;
		}
		
		SendMessage msg = (SendMessage) cl.ReceiveGson(type); // Receive the message
		if(msg==null || msg.MessageId!=NetConstants.System_Settings){ //check if that message is second in protocol
			return null;
		}
		type = new TypeToken<SystemSetting>(){}.getType();
		rtn = gsn.fromJson(msg.MessageString, type); //Get System Settings from the message
		return rtn;
	}
	
	protected String SendFirstMessage(SocketTools cl){
		String sha = null;
		SendMessage msg = null;
		Type type = new TypeToken<SendMessage>(){}.getType();
		sha = GetClieantId(cl.sock.getInetAddress().getHostAddress());
		msg = new SendMessage(NetConstants.Clieant_Id_Number,sha);
		cl.SendGson(msg, type);
		return sha;
	}
	
	
	/**
	 * Calc Hash with SHA256 function and solt. 
	 * @param ip - IP of clieant .
	 * @return SHA256 string.
	 */
	protected String GetClieantId(String ip){
		String SHA256Id = null;
		StringBuffer sb = new StringBuffer();
		// The key of comp and solt (Random number between 1 - 1000)
		String key = ip +" "+Double.toString(Math.random()*1000);
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("SHA-256");
		} catch (NoSuchAlgorithmException e) {
			System.out.println("NoSuchAlgorithmException");
			e.printStackTrace();
		}
		md.update(key.getBytes());
		byte byteData[] = md.digest();
		for (int i = 0; i < byteData.length; i++) {
	         sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
	        }
		SHA256Id = sb.toString();
		return SHA256Id;
	}
	
	public void start(){
		if(this.t == null){
			t = new Thread(this,"Wait for new connection");
			t.start();
		}
	}
	
	/*Like destructor*/
	protected void finalize(){
		 try {
			this.servsock.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	 }
}
