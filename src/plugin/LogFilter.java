package plugin;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;
import java.util.Calendar;
import java.util.logging.Filter;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class LogFilter implements Filter{

	public final String nl = System.getProperty("line.separator");
	public File errorFile = null;
	
	public LogFilter(File f){
		errorFile = f;
	}
	
	@Override
	public boolean isLoggable(LogRecord arg0) {
		if(arg0.getLevel() == Level.SEVERE || arg0.getLevel() == Level.WARNING){
			try {
				FileWriter errorWriter = new FileWriter(errorFile, true);
				Calendar c = Calendar.getInstance();
				String msg = "----- "+c.get(Calendar.WEEK_OF_YEAR)+" "+c.get(Calendar.YEAR)+" - "+c.get(Calendar.HOUR_OF_DAY)+":"+c.get(Calendar.MINUTE)+":"+c.get(Calendar.SECOND)+" -----"+nl;
				errorWriter.write(msg);
				
				String st = "";
				ThreadMXBean bean = ManagementFactory.getThreadMXBean();
				ThreadInfo[] infos = bean.dumpAllThreads(true, true);
				for (ThreadInfo info : infos) {
					StackTraceElement[] ste = info.getStackTrace();
					for(int i=0;i<ste.length;i++){
						st = st + ste[i].toString() + nl;
					}
				}

				errorWriter.write(arg0.getMessage()+nl+st+nl+nl);
				errorWriter.flush();
				Utility.sendMsg(s, "SystemGuard loged an error.", "systemguard.console");
			} catch (IOException e){
				Utility.sendMsg(s, "An Error occured. SystemGuard wasn't able to log it.", "systemguard.console");
			}
			return false;
		}
		return true;
	}

}