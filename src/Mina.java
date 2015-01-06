
import sql.SqlServer;
import utils.TimeUtil;
import net.nio.MainServer;
import net.nio.SecurityXMLServer;



public class Mina {


	public static void main(String[] args) throws Exception
	{
		TimeUtil.start();
		MainServer.getInstances().register();
		SqlServer.gets();
		SecurityXMLServer.gets().start();
		while(true){
			if(TimeUtil.getTimer()>1000){
				
				break;
			}	
		}
		
	}
	//ends
}
