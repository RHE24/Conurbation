package me.daddychurchill.Conurbation;


import org.bukkit.entity.Player;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CreateCMD implements CommandExecutor {
    private final Conurbation plugin;

    public CreateCMD(Conurbation plugin)
    {
        this.plugin = plugin;
    }

	public boolean onCommand(CommandSender sender, Command command, String label, String[] split) 
    {
		if (sender instanceof Player) {
			Player player = (Player) sender;
			if (player.hasPermission("conurbation.command")) {
				player.sendMessage("Loading/creating Conurbation... This might take a moment...");
				player.teleport(plugin.getConurbation().getSpawnLocation());
				return true;
			} else {
				sender.sendMessage("You do not have permission to use this command");
				return false;
			}
		} else {
			sender.sendMessage("This command is only usable by a player");
			return false;
		}
    }
}
