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
                int i = 0;
                warplist.add(new Warp(fields[i++], fields[i++], new Location(Bukkit.getWorld(fields[i++]),
                        Double.parseDouble(fields[i++]),
                        Double.parseDouble(fields[i++]),
                        Double.parseDouble(fields[i++]),
                        Float.parseFloat(fields[i++]),
                        Float.parseFloat(fields[i++]))));
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
                    if (it.getName().equals(args[0])) {
                        player.teleport(it.getLocation());
                        player.sendMessage("You were teleported to warp " + args[0]);
                        return true;
                    }
                }
            }
            return true;
        }
        else if(cmd.getName().equalsIgnoreCase("setwarp")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;

                for (Warp it : warplist) {
                    if (it.getName().equals(args[0])) {
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
                warplist.add(new Warp(args[0], player.getName(), location));

                getLogger().info("Warp " + args[0] + " set in location (" + location.getX() + ", " + location.getY() + ", " + location.getZ() + ")");
                player.sendMessage("Warp " + args[0] + " set in location (" + location.getX() + ", " + location.getY() + ", " + location.getZ() + ")");

                WriteWarpsToFile();
            }
            return true;
        }

        else if(cmd.getName().equalsIgnoreCase("warplist")) {
            Player player = null;
            if (sender instanceof Player) {
                player = (Player) sender;
            }else {return true;}

            if(warplist.isEmpty()){
                player.sendMessage("There are no warps!");
            }
            else{
                player.sendMessage("List of warps:");
                for (Warp it : warplist) {
                    getLogger().info(it.simpleString());
                    player.sendMessage(it.toString());
                }
            }

            return true;
        }

        else if(cmd.getName().equalsIgnoreCase("delwarp")) {
            Warp toRem = null;
            for (Warp it : warplist) {
                if (it.getName().equalsIgnoreCase(args[0])) {
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
            return true;
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
