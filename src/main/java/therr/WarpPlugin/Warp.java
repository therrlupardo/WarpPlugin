package therr.WarpPlugin;

import org.bukkit.ChatColor;
import org.bukkit.Location;

import java.util.UUID;

public class Warp {
    private String name;
    private Location location;
    private String owner;

    public String getName(){return name;}
    public void setName(String name){this.name = name;}
    public Location getLocation() {return location;}
    public void setLocation(Location location) { this.location = location;}
    public String getOwner(){return owner;}
    public void setOwner(String owner){this.owner = owner;}
    public Warp(){}

    public Warp(String name, String owner, Location location){
        this.owner = owner;
        this.name = name;
        this.location = location;
    }

    public String prettyJson(){
        StringBuilder sb = new StringBuilder();
        sb.append("\n{\n")
          .append("\"name\": \"").append(this.name).append("\",\n")
          .append("\"owner\": \"").append(this.owner).append("\",\n")
          .append("\"location\": {\n")
          .append("\t\"world\": \"").append(this.location.getWorld().getName()).append("\",\n")
          .append("\t\"x\": ").append(this.location.getX()).append(",\n")
          .append("\t\"y\": ").append(this.location.getY()).append(",\n")
          .append("\t\"z\": ").append(this.location.getZ()).append(",\n")
          .append("\t\"yaw\": ").append(this.location.getYaw()).append(",\n")
          .append("\t\"pitch\": ").append(this.location.getPitch()).append(",\n")
          .append("\t}\n").append("}\n");
        return sb.toString();
    }

    public String simpleString(){
        StringBuilder sb = new StringBuilder();
        sb.append(this.name).append(";")
          .append(this.owner).append(";")
          .append(this.location.getWorld().getName()).append(";")
          .append(this.location.getX()).append(";")
          .append(this.location.getY()).append(";")
          .append(this.location.getZ()).append(";")
          .append(this.location.getYaw()).append(";")
          .append(this.location.getPitch()).append(";");
        return sb.toString();
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append(ChatColor.AQUA + this.name).append(ChatColor.WHITE + ": ")
          .append("(x: ").append(this.location.getX()).append(", ")
          .append("y: ").append(this.location.getY()).append(", ")
          .append("z: ").append(this.location.getZ()).append(")");
        return sb.toString();
    }
}
