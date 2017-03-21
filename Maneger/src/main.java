import Initialization.Init;
import ProcessUnit.ProcessUnits;
import SocketTool.NewConnection;

public class main {
	
	public static void main(String[] args){
		Init setting = new Init();
		NewConnection conn = new NewConnection(setting.ConnSet);
		ProcessUnits units = ProcessUnits.getInstance();
		conn.start();
		while(true){
			units.CheckHeartBeatOfUnits(5000);
			units.CheckAllMessagesFromUnits();
		}
	}
}
