package therr.WarpPlugin;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;

import static java.lang.Math.floor;


public class WarpPlugin extends JavaPlugin {

    private List<Warp> warplist = new LinkedList<>();
    public static final String WARPS_JSON = "plugins/wp/warps.json";


    @Override
    public void onEnable() {

        try {
            Files.createDirectories(Paths.get("plugins/wp"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            Gson gson = new Gson();
            JsonReader reader = new JsonReader(new FileReader(WARPS_JSON));
            warplist = gson.fromJson(reader, new TypeToken<LinkedList<Warp>>() {
            }.getType());
            if (warplist == null) {
                warplist = new LinkedList<>();
            }
            reader.close();

        }
        catch(FileNotFoundException e) {
            File file = new File(WARPS_JSON);
            try {
                file.createNewFile();
                onEnable();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
        catch(IOException e){
            e.printStackTrace();
        }

        getLogger().info("WarpPlugin enabled!");

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
                    if (it.getName().equalsIgnoreCase(args[0]) && player.getName().equalsIgnoreCase(it.getOwner())) {
                            player.teleport(it.getLocation());
                            player.sendMessage("You were teleported to warp " + ChatColor.AQUA + args[0]);
                        return true;
                    }
                }
                player.sendMessage("This warp doesn't exist!");
            }
            return true;
        }
        else if(cmd.getName().equalsIgnoreCase("setwarp")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;

                for (Warp it : warplist) {
                    if (it.getName().equalsIgnoreCase(args[0]) && player.getName().equalsIgnoreCase(it.getOwner())) {
                        player.sendMessage("You already have warp " + ChatColor.AQUA + args[0] + ChatColor.WHITE + "!");
                        return true;
                    }
                }

                warplist.add(new Warp(args[0], player.getName(), player.getLocation().getWorld().getName(), floor(player.getLocation().getX()) + 0.5, floor(player.getLocation().getY()), floor(player.getLocation().getZ()) + 0.5, 0 ,0));

                player.sendMessage("Warp " +
                        ChatColor.AQUA + args[0] +
                        ChatColor.WHITE + " set in location (" + (floor(player.getLocation().getX()) + 0.5) + ", " + floor(player.getLocation().getY()) +  ", " + (floor(player.getLocation().getZ()) + 0.5) +  ")");

                WriteWarpsToFile();
            }
            return true;
        }

        else if(cmd.getName().equalsIgnoreCase("warplist")) {
            Player player = null;
            if (sender instanceof Player) {
                player = (Player) sender;
            }else {return true;}

            Boolean playerHasWarps = false;
            for(Warp it : warplist){
                if (player.getName().equalsIgnoreCase(it.getOwner())){
                    playerHasWarps = true;
                    break;
                }
            }

            if(!playerHasWarps){
                player.sendMessage(ChatColor.RED + "You don't have any warp!");
            }
            else{
                player.sendMessage(ChatColor.YELLOW + "List of your warps:");
                for (Warp it : warplist) {
                    if(player.getName().equalsIgnoreCase(it.getOwner())){
                        player.sendMessage(it.toString());
                    }

                }
            }

            return true;
        }

        else if(cmd.getName().equalsIgnoreCase("delwarp")) {
            Warp toRem = null;
            Player player = null;
            if(sender instanceof Player){
                player = (Player) sender;
                for (Warp it : warplist) {
                    if (it.getName().equalsIgnoreCase(args[0]) && player.getName().equalsIgnoreCase(it.getOwner())) {
                        toRem = it;
                        break;
                    }
                }
                if (toRem != null) {
                    warplist.remove(toRem);
                    player.sendMessage("Warp " + ChatColor.AQUA + toRem.getName() + ChatColor.WHITE + " removed");
                }
                WriteWarpsToFile();
            }

            return true;
        }




        return false;
    }

    private void WriteWarpsToFile(){
//        PrintWriter writer = null;
//        try{
//            writer = new PrintWriter("warplist.th", "UTF-8");
//            for(Warp it: warplist){
//                //writer.println(it.simpleString());
//            }
//        }
//        catch(IOException e){
//            e.printStackTrace();
//        }
//        finally{
//            writer.close();
//        }

        try{
            Gson gson = new GsonBuilder().create();
            FileWriter writer = new FileWriter(WARPS_JSON);
            gson.toJson(warplist, writer);
            writer.close();
        }
        catch(IOException e){
            e.printStackTrace();
        }

    }

    


}
