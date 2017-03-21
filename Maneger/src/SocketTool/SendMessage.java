package SocketTool;

public class SendMessage extends Object{
	
	public int MessageId;
	public String MessageString;
	
	public SendMessage(int id,String str){
		this.MessageId = id;
		this.MessageString = str;
	}
	public SendMessage(){
		this.MessageId = 0;
		this.MessageString = null;
	}

}
