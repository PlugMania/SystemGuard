package plugin;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.logging.Filter;
import java.util.logging.Level;
import java.util.logging.LogRecord;

import org.bukkit.Server;

public class LogFilter implements Filter{

	public final String nl = System.getProperty("line.separator");
	HashMap<String,Long> lastMsgs = new HashMap<String,Long>();
	File errorFile = null;
	Server server = null;
	
	public LogFilter(Server s, File f){
		errorFile = f;
		server = s;
	}
	
	@Override
	public boolean isLoggable(LogRecord arg0) {
		boolean logIt = false;		
		if(arg0.getLevel() == Level.SEVERE || arg0.getLevel() == Level.WARNING){
			try {
				if(lastMsgs.containsKey(arg0.getMessage())){
					if((System.currentTimeMillis() - lastMsgs.get(arg0.getMessage())) >= 900000){
						logIt = true;
					}else{
						logIt = false;	
					}
				}else{
					logIt = true;
				}
				if(logIt == true){
					lastMsgs.put(arg0.getMessage(), System.currentTimeMillis());
				}
				FileWriter errorWriter = new FileWriter(errorFile, true);
				Calendar c = Calendar.getInstance();
				String msg = "----- "+c.get(Calendar.WEEK_OF_YEAR)+" "+c.get(Calendar.YEAR)+" - "+c.get(Calendar.HOUR_OF_DAY)+":"+c.get(Calendar.MINUTE)+":"+c.get(Calendar.SECOND)+" -----"+nl;
				errorWriter.write(msg);
				
//				// --- Dumping all the current Thread's StackTraces to Variable 'st' --- \\
//				String st = "";
//				ThreadMXBean bean = ManagementFactory.getThreadMXBean();
//				ThreadInfo[] infos = bean.dumpAllThreads(true, true);
//				for (ThreadInfo info : infos) {
//					StackTraceElement[] ste = info.getStackTrace();
//					for(int i=0;i<ste.length;i++){
//						st = st + ste[i].toString() + nl;
//					}
//				}

				errorWriter.write(arg0.getMessage()+nl+nl);
				errorWriter.flush();
				Utility.sendMsg(server, "SystemGuard loged an error.", "systemguard.console");
			} catch (IOException e){
				Utility.sendMsg(server, "An Error occured. SystemGuard wasn't able to log it.", "systemguard.console");
			}
			if(logIt == true){
				Utility.sendMsg(server, "Error is new. Complete StackTrace in the Serverlog.", "systemguard.console");
				return true;
			}else{
				return false;
			}
		}
		
		return true;
	}

}