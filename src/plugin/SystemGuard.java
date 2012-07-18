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
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class SystemGuard extends JavaPlugin implements Listener, Filter{

	public File errorFile = null;
	public final String nl = System.getProperty("line.separator");
	
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
				getServer().getLogger().log(Level.INFO, "SystemGuard loged an error.");
				Player[] a = getServer().getOnlinePlayers();
				if(a.length>0){
					for(int i=0;i<a.length;i++){
						if(a[i].hasPermission("systemguard.console") || a[i].isOp()){
							a[i].sendMessage(ChatColor.RED+"SystemGuard loged an error!");
						}
					}
				}
			} catch (IOException e){
				getServer().getLogger().log(Level.INFO, "SystemGuard wasn't able to log an error.");
			}
			return true;
		}
		return true;
	}
	
	public void onEnable(){
		errorFile = new File(getDataFolder(), "Errors.txt");
		if(!errorFile.exists()){
			try {
				getDataFolder().mkdir();
				errorFile.createNewFile();
			} catch (IOException e) {
				getServer().getLogger().log(Level.INFO, "SystemGuard wasn't able to create the file Errors.txt. This needs to be fixed in order to run SystemGuard. You may create the file by hand. SystemGuard is being disabled...");
				getServer().getPluginManager().disablePlugin(this);
			}
		}
		getServer().getLogger().setFilter(this);
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
		if(cmd.getName().equalsIgnoreCase("benchmark")){
			Benchmark b = new Benchmark(getServer(), this, sender);
			b.performBenchmark();
			return true;
		}
		if(cmd.getName().equalsIgnoreCase("npetest")){
			Location l = null;
			l.getBlock();
			return true;
		}
		return false;
	}
	
}