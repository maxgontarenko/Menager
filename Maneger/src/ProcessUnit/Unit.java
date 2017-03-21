package ProcessUnit;

import SocketTool.SocketTools;

public class Unit {
	
	public int UnitNumber;
	public String Id;
	public SocketTools Connection;
	public long HeartBeat;
	public String JobId;
	public int Status;
	
	public Unit(int unitnumber,String id,SocketTools conn,long heartbeat,String jobid,int status){
		this.UnitNumber = unitnumber;
		this.Id = id;
		this.Connection = conn;
		this.HeartBeat = heartbeat;
		this.JobId = jobid;
		this.Status = status;
	}
}
