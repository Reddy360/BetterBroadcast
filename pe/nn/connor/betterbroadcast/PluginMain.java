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
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class PluginMain extends JavaPlugin{
    private String tag;
    private List<String> players;
    private FileConfiguration config;
    private String colours[];

    private void initConfig(){
        if(!(new File(getDataFolder(), "config.yml")).exists()){
            saveDefaultConfig();
        }
            
        tag = config.getString("tag", ChatColor.DARK_RED + "B" + ChatColor.GREEN + "B&r");
        tag = "[" + tag + ChatColor.RESET + "]";
        tag = ChatColor.translateAlternateColorCodes('&', tag);
    }

    public void onEnable(){
        players = new ArrayList<String>();
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
            "k - " + ChatColor.MAGIC + "MAGIC!",
            ChatColor.BOLD + "l" + ChatColor.RESET,
            ChatColor.STRIKETHROUGH + "m" + ChatColor.RESET,
            ChatColor.UNDERLINE + "n" + ChatColor.RESET, 
            ChatColor.ITALIC + "o" + ChatColor.RESET,
            "r - Resets any Colour and formatting Codes",
        });
        initConfig();
        PluginManager pm = Bukkit.getPluginManager();
        pm.addPermission(new Permission("bb.broadcast", PermissionDefault.OP));
        pm.addPermission(new Permission("bb.colours", PermissionDefault.OP));
        pm.addPermission(new Permission("bb.toggle", PermissionDefault.OP));
        pm.addPermission(new Permission("bb.reload", PermissionDefault.OP));
        pm.addPermission(new Permission("bb.info", PermissionDefault.TRUE));
        pm.addPermission(new Permission("bb.config", PermissionDefault.OP));
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
                    if(players.contains(sender.getName()) && config.getBoolean("AllowNoTag", true)){
                        Bukkit.broadcastMessage(bcast);
                    }else{
                        Bukkit.broadcastMessage(tag + " " + bcast);
                    }
                }else{
                    sender.sendMessage(ChatColor.DARK_RED + "What am I going to broadcast?");
                }
            }else{
                sender.sendMessage(ChatColor.DARK_RED + "You do not have permission!");
            }
            return true;
        }else if(cmd.getName().equalsIgnoreCase("bbtoggle")){
            if(sender.hasPermission(Bukkit.getPluginManager().getPermission("bb.toggle")) && 
                    !config.getBoolean("AllowNoTag", true)){
                if(!players.contains(sender.getName())){
                    players.add(sender.getName());
                    sender.sendMessage("The tag will no longer be shown");
                }else{
                    players.remove(sender.getName());
                    sender.sendMessage("The tag will now be shown");
                }
            }else{
                sender.sendMessage(ChatColor.DARK_RED + "You do not have permission!");
            }
            return true;
        }else if(cmd.getName().equalsIgnoreCase("bbcolours")){
            if(sender.hasPermission(Bukkit.getPluginManager().getPermission("bb.colours"))){
                String text = "";
                for(int i = 0; i < colours.length; i++){
                    String code = colours[i];
                    text = text + code + " ";
                }

                sender.sendMessage(text);
            }
            return true;
        }else if(cmd.getName().equalsIgnoreCase("bbreload")){
            if(sender.hasPermission(Bukkit.getPluginManager().getPermission("bb.reload"))){
                this.reloadConfig();
                initConfig();
                sender.sendMessage(ChatColor.GREEN + "Reloaded");
            }else{
                sender.sendMessage(ChatColor.DARK_RED + "You do not have permission!");
            }
            return true;
        }else if(cmd.getName().equalsIgnoreCase("bbinfo")){
            if(sender.hasPermission(Bukkit.getPluginManager().getPermission("bb.info"))){
                PluginDescriptionFile pdf = this.getDescription();
                sender.sendMessage("Version: " + pdf.getVersion());
                sender.sendMessage("Created by " + ChatColor.DARK_PURPLE + "Reddy360");
                sender.sendMessage("Bukkit: http://dev.bukkit.org/bukkit-plugins/better-broadcast/");
                sender.sendMessage("GitHub: https://github.com/Reddy360/BetterBroadcast Fork me");
                sender.sendMessage("Plugin binary compiled on DD/MM/YY HH:MM"); //Not really compiled yet, will be populated for releases
                sender.sendMessage("Thanks for using my plugin :)");
                //Feel free to add your fork info if you've forked this
            }else{
                sender.sendMessage(ChatColor.DARK_RED + "You do not have permission!"); //I really don't know why you wouldn't
            }
            return true;
        }else if(cmd.getName().equalsIgnoreCase("bbconfig")){
            if(sender.hasPermission(Bukkit.getPluginManager().getPermission("bb.config"))){
                if(args.length == 0){
                    sender.sendMessage("bbconfig set <key> <value>");
                    sender.sendMessage("bbconfig get <key>");
                    sender.sendMessage("bbconfig list");
                    sender.sendMessage("bbconfig remove <key>");
                    return true;
                }else if(args.length == 1){
                    if(args[0].equalsIgnoreCase("list")){
                        //List config values
                    }else{
                        sender.sendMessage(ChatColor.DARK_RED + "Unknown argument");
                    }
                    return true;
                }else if(args.length == 2){
                    if(args[0].equalsIgnoreCase("get")){
                        sender.sendMessage(args[1] + ": " + config.getString(args[1], "No config value found"));
                    }else if(args[0].equalsIgnoreCase( "remove")){
                        config.set(args[1], null);
                        sender.sendMessage(args[1] + " has been removed");
                    }
                    return true;
                }else if(args.length >= 3){
                    if(args[0].equalsIgnoreCase("set")){
                        String value = "";
                        for(int i = 2; i > args.length; i++){
                            value = value + args[i] + " ";
                        }
                        config.set(args[1], value);
                        this.saveConfig();
                        sender.sendMessage(args[1] + " has been set to " + value);
                        return true;
                    }
                    return false;
                }
            }else{
                sender.sendMessage(ChatColor.DARK_RED + "You do not have permission!"); //I wouldn't trust anyone who tries this
                return true;
            }
        }else{
            return false;
        }
		return false;
    }
}
