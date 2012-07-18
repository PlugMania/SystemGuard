package plugin;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class SystemGuard extends JavaPlugin implements Listener{

	public File errorFile = null;
	
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
		getServer().getLogger().setFilter(new LogFilter(errorFile));
	}
	
	@SuppressWarnings("null")
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