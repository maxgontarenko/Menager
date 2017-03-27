import Initialization.Init;
import JobControler.JobControler;
import ProcessUnit.ProcessUnits;
import SocketTool.NewConnection;

public class main {
	
	public static void main(String[] args){
		Init setting = new Init();
		NewConnection conn = new NewConnection(setting.ConnSet);
		ProcessUnits units = ProcessUnits.getInstance();
		JobControler job = new JobControler();
		conn.start();
		job.start();
		while(true){
			units.CheckHeartBeatOfUnits(5000);
			units.CheckAllMessagesFromUnits();
		}
	}
}
