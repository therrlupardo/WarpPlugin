package therr.WarpPlugin;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.util.LinkedList;
import java.util.List;

import static java.lang.Math.floor;


public class WarpPlugin extends JavaPlugin {

    //private HashMap<String, Location> Warps = new HashMap();
    private List<Warp> warplist = new LinkedList<>();


    @Override
    public void onEnable() {

        getLogger().info("WarpPlugin enabled!");
        File file = null;
        try{
            file = new File("warplist.th");
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            String[] fields;
            
            getLogger().info("Warps:");

            while ((line = br.readLine()) != null){
                getLogger().info(line);
                fields = line.split(";");

                warplist.add(new Warp(fields[0], new Location(Bukkit.getWorld(fields[1]),
                        Double.parseDouble(fields[2]),
                        Double.parseDouble(fields[3]),
                        Double.parseDouble(fields[4]),
                        Float.parseFloat(fields[5]),
                        Float.parseFloat(fields[6]))));
            }

        }
        catch(FileNotFoundException e){
            try {
                PrintWriter writer = new PrintWriter("warplist.th", "UTF-8");
            }
            catch(IOException ioe){
                ioe.printStackTrace();
            }
        }
        catch(IOException e){
            e.printStackTrace();
        }

    }

    @Override
    public void onDisable() {
        WriteWarpsToFile();
        getLogger().info("WarpContoll disabled!");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){


        if (cmd.getName().equalsIgnoreCase("warp")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                for (Warp it : warplist) {
                    if (it.getName().equals(args[1])) {
                        player.teleport(it.getLocation());
                        player.sendMessage("You were teleported to warp " + args[1]);
                        return true;
                    }
                }
            }
            return true;
        }
        if(cmd.getName().equalsIgnoreCase("setwarp")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;

                for (Warp it : warplist) {
                    if (it.getName().equals(args[1])) {
                        player.sendMessage("Warp name already used!");
                        return true;
                    }
                }

                Location location = player.getLocation();
                location.setX(floor(location.getX()) + 0.5);
                location.setY(floor(location.getY()));
                location.setZ(floor(location.getZ()) + 0.5);
                location.setPitch(0);
                location.setYaw(0);
                warplist.add(new Warp(args[1], location));

                getLogger().info("Warp " + args[1] + " set in location (" + location.getX() + ", " + location.getY() + ", " + location.getZ() + ")");
                player.sendMessage("Warp " + args[1] + " set in location (" + location.getX() + ", " + location.getY() + ", " + location.getZ() + ")");

                WriteWarpsToFile();
            }
            return true;
        }
        if(cmd.getName().equalsIgnoreCase("warplist")) {
            for (Warp it : warplist) {
                getLogger().info(it.simpleString());
                if (sender instanceof Player) {
                    Player player = (Player) sender;
                    player.sendMessage(it.toString());
                }
            }
            return true;
        }
        if(cmd.getName().equalsIgnoreCase("delwarp")) {
            Warp toRem = null;
            for (Warp it : warplist) {
                if (it.getName().equalsIgnoreCase(args[1])) {
                    toRem = it;
                    break;
                }
            }
            if (toRem != null) {
                warplist.remove(toRem);
                getLogger().info("Warp " + toRem.getName() + " removed");
                if (sender instanceof Player) {
                    Player player = (Player) sender;
                    player.sendMessage("Warp " + toRem.getName() + " removed");
                }
            }
            WriteWarpsToFile();
        }


        if(cmd.getName().equalsIgnoreCase("help") && args[0].equalsIgnoreCase("warpPlugin")) {
            sender.sendMessage("/setwarp [name] - sets warp with name");
            sender.sendMessage("/delwarp [name] - Deletes warp with name");
            sender.sendMessage("/warplist - lists warps");
            sender.sendMessage("warp [name] - teleports you to warp \"name\"");
        }

        return false;
    }

    private void WriteWarpsToFile(){
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


}
