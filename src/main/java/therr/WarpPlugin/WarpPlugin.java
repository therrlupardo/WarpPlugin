package therr.WarpPlugin;

import com.google.gson.Gson;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import static java.lang.Math.floor;


public class WarpPlugin extends JavaPlugin {

    //private HashMap<String, Location> Warps = new HashMap();
    private List<Warp> warplist = new LinkedList<>();


    @Override
    public void onEnable() {

        getLogger().info("WarpPlugin enabled!");
        File file =null;
        try{
            file = new File("warplist.th");
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            Warp warp = new Warp();
            Location location = null;
            while((line = br.readLine())!= null){
                String[] fields = line.split(";");
                warp.setName(fields[0]);
                location.setWorld(Bukkit.getServer().getWorld(fields[1]));
                location.setX(Double.parseDouble(fields[2]));
                location.setY(Double.parseDouble(fields[3]));
                location.setZ(Double.parseDouble(fields[4]));
                location.setYaw(Float.parseFloat(fields[5]));
                location.setPitch(Float.parseFloat(fields[6]));
                warp.setLocation(location);
            }
        }
        catch(IOException e){
            e.printStackTrace();
        }

    }

    @Override
    public void onDisable() {
        getLogger().info("WarpContoll disabled!");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
        if (cmd.getName().equalsIgnoreCase("setwarp")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;

                for(Warp it : warplist){
                    if(it.getName().equals(args[0])){
                        player.sendMessage("Warp name already used!");
                        return true;
                    }
                }

                Location location = player.getLocation();
                location.setX(floor(location.getX())+0.5);
                location.setY(floor(location.getY()));
                location.setZ(floor(location.getZ())+0.5);
                location.setPitch(0);
                location.setYaw(0);
                warplist.add(new Warp(args[0], location));

                getLogger().info("Warp " + args[0] + " set in location " + location);
                player.sendMessage("Warp " + args[0] + " set in location " + location);

                PrintWriter writer = null;
                try{
                    writer = new PrintWriter("warplist.th", "UTF-8");
                    for(Warp it: warplist){
                        writer.println(it.simpleString());
                    }
                }
                catch(IOException e){
                    e.printStackTrace();
                }
                finally{
                    writer.close();
                }


            }
            return true;
        }

        if (cmd.getName().equalsIgnoreCase("warp")) {
            if(sender instanceof Player){
                Player player = (Player) sender;
                for(Warp it : warplist){
                    if(it.getName().equals(args[0])){
                        player.teleport(it.getLocation());
                        player.sendMessage("You were teleported to warp " + args[0]);
                        return true;
                    }
                }
            }
            return true;
        }

        if (cmd.getName().equalsIgnoreCase("warplist")) {
            for(Warp it : warplist){
                getLogger().info(it.prettyJson());
                if(sender instanceof Player){
                    Player player = (Player) sender;
                    player.sendMessage(it.toString());
                }
            }
            return true;
        }

        return false;
    }




}
