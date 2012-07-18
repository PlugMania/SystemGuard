package plugin;

import java.util.logging.Level;

import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class Benchmark{
	
	Server server = null;
	Plugin plugin = null;
	CommandSender commandSender = null;
	int task1 = -1;
	int task2 = -1;
	int a = 0;
	int b = 0;
	int took = 0;
	long start = 0;
	long end = 0;
	int resultPer = 0;
	
	public Benchmark(Server s, Plugin p, CommandSender cs){
		server = s;
		plugin = p;
		commandSender = cs;
	}
	
	public void performBenchmark(){
		if(commandSender instanceof Player){
			Player p = (Player)commandSender;
			p.sendMessage("Benchmarking for 100 Seconds...");
		}else{
			server.getLogger().log(Level.INFO, "Benchmarking for 100 Seconds...");
		}
		task1 = server.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable(){
			public void run(){
				start = System.currentTimeMillis();
				task2 = server.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable(){
					public void run(){
						b++;
						if(b==20){
							Calculation1();
							b = 0;
							server.getScheduler().cancelTask(task2);
						}
					}
				}, 1, 1);
				a++;
				if(a==10){
					Calculation2();
					a = 0;
					server.getScheduler().cancelTask(task1);
				}
			}
		}, 200, 200);
	}
	
	private void Calculation1(){
		end = System.currentTimeMillis();
		long calc = end - start;
		took = took + (int)calc - 1000;
	}
	
	private void Calculation2(){
		took = took / 10;
		if(took == 0){
			resultPer = 100;
		}else{
			resultPer = 100 - (took / 5);
		}
		if(commandSender instanceof Player){
			Player p = (Player)commandSender;
			p.sendMessage("Server is running at a performance of "+resultPer+"%.");
		}else{
			server.getLogger().log(Level.INFO, "Server is running at a performance of "+resultPer+"%.");
		}
		took = 0;
		resultPer = 0;
	}
	
}