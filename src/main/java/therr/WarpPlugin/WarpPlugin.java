package therr.WarpPlugin;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;


public class WarpPlugin extends JavaPlugin {

    private HashMap<String, Location> Warps = new HashMap();

    @Override
    public void onEnable() {
        getLogger().info("WarpPlugin enabled!");
    }

    @Override
    public void onDisable() {
        getLogger().info("WarpContoll disabled!");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("setwarp")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                Warps.put(args[0], player.getLocation());
                getLogger().info("Warp " + args[0] + " set in location " + player.getLocation());
            }
            return true;
        }

        if (cmd.getName().equalsIgnoreCase("warp")) {
            if(sender instanceof Player){
                Player player = (Player) sender;
                Location location = Warps.get(args[0]);
                player.teleport(location);
            }
            return true;
        }

        if (cmd.getName().equalsIgnoreCase("warplist")) {
            for(HashMap.Entry<String, Location> it : Warps.entrySet()){
                getLogger().info(it.getKey() + " " + it.getValue());
            }
            return true;
        }

        return false;
    }



}
