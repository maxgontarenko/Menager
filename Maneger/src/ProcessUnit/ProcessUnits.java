package ProcessUnit;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.gson.reflect.TypeToken;

import SocketTool.NetConstants;
import SocketTool.SendMessage;
import SocketTool.SocketTools;

/**
 * This class is Singleton and Synchronized 
 * @author Max And Guy  
 *
 */
public class ProcessUnits {
	
	protected static ProcessUnits instance = null;
	protected static List<Unit> ListOfProcessUnit = null;
	
	protected ProcessUnits(){
		ProcessUnits.ListOfProcessUnit = new ArrayList<Unit>();
	}
	
	public void CheckAllMessagesFromUnits(){
		synchronized(ProcessUnits.ListOfProcessUnit){
			Type type = new TypeToken<SendMessage>(){}.getType();
			for(Unit x : ProcessUnits.ListOfProcessUnit){
				if(x.Connection.IfNextReceive()){
					SendMessage msg = (SendMessage) x.Connection.ReceiveGson(type);
					switch(msg.MessageId){
					case NetConstants.Heart_Beat_From_Worker: 
						this.UpdateHeartBeat(x);
						break;
					default:
						this.UpdateHeartBeat(x);
						break;
					}
				}
			}
		}
	}
	
	protected void UpdateHeartBeat(Unit unit){
		long CurrentTime = new Date().getTime();
		unit.HeartBeat = CurrentTime;
	}
	
	/**
	 * Check heart beat of all Process unit
	 * @param delta - Difference between last time and current time
	 */
	public void CheckHeartBeatOfUnits(long delta){
		synchronized(ProcessUnits.ListOfProcessUnit){
			for(Unit x : ProcessUnits.ListOfProcessUnit){
				long CurrentTime = new Date().getTime();
				if(Math.abs(x.HeartBeat-CurrentTime)>delta){
					//TODO
					System.out.println("TimeOut In Unit : "+x.UnitNumber + "Worker ID Sha-256: "+x.Id);
				}
			}
		}
	}
	
	/**
	 * Add new process unit to Data Structure
	 * @param unitnumber -Unit number (In one machine can be more than one process unit)
	 * @param id - The ID number of a Worker
	 * @param conn - Connection file
	 * @param heartbeat - The last communication with the worker
	 * @param jobid - The ID number of Job
	 * @param status - The Status of Worker
	 */
	public void AddNewUnit(int unitnumber,String id,SocketTools conn,long heartbeat,String jobid,int status){
		synchronized(ProcessUnits.ListOfProcessUnit){
			ProcessUnits.ListOfProcessUnit.add(new Unit(unitnumber,id,conn,heartbeat,jobid,status));
		}
	}
	
	/**
	 * 
	 * @return - Instance of ProcessUnits class
	 */
	public static ProcessUnits getInstance(){
		if(ProcessUnits.instance == null){
			synchronized(ProcessUnits.class){
				if(ProcessUnits.instance == null)
					ProcessUnits.instance = new ProcessUnits();
			}
		}
		return ProcessUnits.instance;
	}
}
