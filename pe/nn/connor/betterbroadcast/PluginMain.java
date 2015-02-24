package pe.nn.connor.betterbroadcast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class PluginMain extends JavaPlugin{
    private String tag;
    private List<Players> players;
    private FileConfiguration config;
    private String colours[];

    private void initConfig(){
        if(!(new File(getDataFolder(), "config.yml")).exists()){
            saveDefaultConfig();
        }
            
        tag = config.getString("tag", ChatColor.RED + "B" + ChatColor.GREEN + "B&r");
        tag = "[" + tag + ChatColor.RESET + "]";
        tag = ChatColor.translateAlternateColorCodes('&', tag);
    }

    public void onEnable(){
        players = new ArrayList();
        config = getConfig();
        colours = (new String[] {
            ChatColor.BLACK + "0",
            ChatColor.DARK_BLUE + "1",
            ChatColor.DARK_GREEN + "2",
            ChatColor.DARK_AQUA + "3",
            ChatColor.DARK_RED + "4",
            ChatColor.DARK_PURPLE + "5",
            ChatColor.GOLD + "6",
            ChatColor.GRAY + "7",
            ChatColor.DARK_GRAY + "8",
            ChatColor.BLUE + "9", 
            ChatColor.GREEN + "a",
            ChatColor.AQUA + "b",
            ChatColor.RED + "c",
            ChatColor.LIGHT_PURPLE + "d",
            ChatColor.YELLOW + "e",
            ChatColor.WHITE + "f",
            "k - MAGIC!",
            ChatColor.BOLD + "l" + ChatColor.RESET,
            ChatColor.STRIKETHROUGH + "m" + ChatColor.RESET,
            ChatColor.UNDERLINE + "n" + ChatColor.RESET, 
            ChatColor.ITALIC + "o" + ChatColor.RESET,
            ChatColor.RESET + "r - Resets any Colour and formatting Codes",
        });
        initConfig();
        PluginManager pm = Bukkit.getPluginManager();
        pm.addPermission(new Permission("bb.broadcast", PermissionDefault.OP));
        pm.addPermission(new Permission("bb.colours", PermissionDefault.OP));
        pm.addPermission(new Permission("bb.toggle", PermissionDefault.OP));
        pm.addPermission(new Permission("bb.reload", PermissionDefault.OP));
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String args[]){
        if(cmd.getName().equalsIgnoreCase("bb")){
            if(sender.hasPermission(Bukkit.getPluginManager().getPermission("bb.broadcast"))){
                if(args.length >= 1){
                    String bcast = "";
                    for(int x = 0; x < args.length; x++){
                        bcast = bcast + args[x] + " ";
                    }
                    
                    bcast = ChatColor.translateAlternateColorCodes('&', bcast);
                    if(!players.contains(sender.getName())){
                        Bukkit.broadcastMessage(tag + " " + bcast);
                    }else{
                        Bukkit.broadcastMessage(bcast);
                    }
                }else{
                    sender.sendMessage(ChatColor.RED + "What am I going to broadcast?");
                }
            }else{
                sender.sendMessage(ChatColor.RED + "You do not have permission!");
            }
            return true;
        }
        if(cmd.getName().equalsIgnoreCase("bbtoggle")){
            if(sender.hasPermission(Bukkit.getPluginManager().getPermission("bb.toggle"))){
                if(!players.contains(sender.getName())){
                    sender.sendMessage("The tag will no longer be shown");
                    players.add(sender.getName());
                }else{
                    sender.sendMessage("The tag will now be shown");
                    players.remove(sender.getName());
                }
            }else{
                sender.sendMessage(ChatColor.RED + "You do not have permission!");
            }
            return true;
        }
        if(cmd.getName().equalsIgnoreCase("bbcolours")){
            if(sender.hasPermission(Bukkit.getPluginManager().getPermission("bb.colours"))){
                String text = "";
                for(int i = 0; i < colours.length; i++){
                    String code = colours[i];
                    text = text + code + " ";
                }

                sender.sendMessage(text);
            }
            return true;
        }
        if(cmd.getName().equalsIgnoreCase("bbreload")){
            if(sender.hasPermission(Bukkit.getPluginManager().getPermission("bb.reload"))){
                reloadConfig();
                initConfig();
                sender.sendMessage(ChatColor.GREEN + "Reloaded");
            }else{
                sender.sendMessage(ChatColor.RED + "You do not have permission!";
            }
            return true;
        }else{
            return false;
        }
    }
}
