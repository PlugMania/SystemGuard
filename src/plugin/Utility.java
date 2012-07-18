package plugin;

import java.util.logging.Level;

import org.bukkit.Server;
import org.bukkit.entity.Player;

public class Utility {

	public static void sendMsg(Server s, String msg, String perm){
		s.getLogger().log(Level.INFO, msg);
		Player[] a = s.getOnlinePlayers();
		if(a.length>0){
			for(int i=0;i<a.length;i++){
				if(a[i].hasPermission(perm) || a[i].isOp()){
					a[i].sendMessage(msg);
				}
			}
		}
	}
	
}