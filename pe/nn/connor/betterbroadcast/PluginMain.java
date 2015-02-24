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


    private void initConfig(){
        if(!(new File(getDataFolder(), "config.yml")).exists())
            saveDefaultConfig();
        tag = config.getString("tag", (new StringBuilder()).append(ChatColor.RED).append("B").append(ChatColor.GREEN).append("B&r").toString());
        tag = (new StringBuilder("[")).append(tag).append(ChatColor.RESET).append("]").toString();
        tag = ChatColor.translateAlternateColorCodes('&', tag);
    }

    public void onEnable()
    {
        players = new ArrayList();
        config = getConfig();
        colours = (new String[] {
            (new StringBuilder()).append(ChatColor.BLACK).append("0").toString(), (new StringBuilder()).append(ChatColor.DARK_BLUE).append("1").toString(), (new StringBuilder()).append(ChatColor.DARK_GREEN).append("2").toString(), (new StringBuilder()).append(ChatColor.DARK_AQUA).append("3").toString(), (new StringBuilder()).append(ChatColor.DARK_RED).append("4").toString(), (new StringBuilder()).append(ChatColor.DARK_PURPLE).append("5").toString(), (new StringBuilder()).append(ChatColor.GOLD).append("6").toString(), (new StringBuilder()).append(ChatColor.GRAY).append("7").toString(), (new StringBuilder()).append(ChatColor.DARK_GRAY).append("8").toString(), (new StringBuilder()).append(ChatColor.BLUE).append("9").toString(), 
            (new StringBuilder()).append(ChatColor.GREEN).append("a").toString(), (new StringBuilder()).append(ChatColor.AQUA).append("b").toString(), (new StringBuilder()).append(ChatColor.RED).append("c").toString(), (new StringBuilder()).append(ChatColor.LIGHT_PURPLE).append("d").toString(), (new StringBuilder()).append(ChatColor.YELLOW).append("e").toString(), (new StringBuilder()).append(ChatColor.WHITE).append("f").toString(), "k - MAGIC!", (new StringBuilder()).append(ChatColor.BOLD).append("l").append(ChatColor.RESET).toString(), (new StringBuilder()).append(ChatColor.STRIKETHROUGH).append("m").append(ChatColor.RESET).toString(), (new StringBuilder()).append(ChatColor.UNDERLINE).append("n").append(ChatColor.RESET).toString(), 
            (new StringBuilder()).append(ChatColor.ITALIC).append("o").append(ChatColor.RESET).toString(), (new StringBuilder()).append(ChatColor.RESET).append("r - Resets any Colour Codes").toString()
        });
        initConfig();
        PluginManager pm = Bukkit.getPluginManager();
        pm.addPermission(new Permission("bb.broadcast", PermissionDefault.OP));
        pm.addPermission(new Permission("bb.colours", PermissionDefault.OP));
        pm.addPermission(new Permission("bb.toggle", PermissionDefault.OP));
        pm.addPermission(new Permission("bb.reload", PermissionDefault.OP));
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String args[])
    {
        if(cmd.getName().equalsIgnoreCase("bb"))
        {
            if(sender.hasPermission(Bukkit.getPluginManager().getPermission("bb.broadcast")))
            {
                if(args.length >= 1)
                {
                    String bcast = "";
                    for(int x = 0; x < args.length; x++)
                        bcast = (new StringBuilder(String.valueOf(bcast))).append(args[x]).append(" ").toString();

                    bcast = ChatColor.translateAlternateColorCodes('&', bcast);
                    if(!players.contains(sender.getName()))
                        Bukkit.broadcastMessage((new StringBuilder(String.valueOf(tag))).append(" ").append(bcast).toString());
                    else
                        Bukkit.broadcastMessage(bcast);
                } else
                {
                    sender.sendMessage((new StringBuilder()).append(ChatColor.RED).append("What am I going to broadcast?").toString());
                }
            } else
            {
                sender.sendMessage((new StringBuilder()).append(ChatColor.RED).append("You do not have permission!").toString());
            }
            return true;
        }
        if(cmd.getName().equalsIgnoreCase("bbtoggle"))
        {
            if(sender.hasPermission(Bukkit.getPluginManager().getPermission("bb.toggle")))
            {
                if(!players.contains(sender.getName()))
                {
                    sender.sendMessage("The tag will no longer be shown");
                    players.add(sender.getName());
                } else
                {
                    sender.sendMessage("The tag will now be shown");
                    players.remove(sender.getName());
                }
            } else
            {
                sender.sendMessage((new StringBuilder()).append(ChatColor.RED).append("You do not have permission!").toString());
            }
            return true;
        }
        if(cmd.getName().equalsIgnoreCase("bbcolours"))
        {
            if(sender.hasPermission(Bukkit.getPluginManager().getPermission("bb.colours")))
            {
                String text = "";
                String as[];
                int j = (as = colours).length;
                for(int i = 0; i < j; i++)
                {
                    String code = as[i];
                    text = (new StringBuilder(String.valueOf(text))).append(code).append(" ").toString();
                }

                sender.sendMessage(text);
            }
            return true;
        }
        if(cmd.getName().equalsIgnoreCase("bbreload"))
        {
            if(sender.hasPermission(Bukkit.getPluginManager().getPermission("bb.reload")))
            {
                reloadConfig();
                initConfig();
                sender.sendMessage((new StringBuilder()).append(ChatColor.GREEN).append(" Reloaded").toString());
            } else
            {
                sender.sendMessage((new StringBuilder()).append(ChatColor.RED).append("You do not have permission!").toString());
            }
            return true;
        } else
        {
            return false;
        }
    }

    public String tag;
    List players;
    FileConfiguration config;
    String colours[];
}
