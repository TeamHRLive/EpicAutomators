package com.songoda.epicautomators.commands;

import com.craftaro.core.commands.AbstractCommand;
import com.songoda.epicautomators.EpicAutomators;
import com.songoda.epicautomators.automator.levels.Level;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class CommandGive extends AbstractCommand {
    private final EpicAutomators plugin;

    public CommandGive(EpicAutomators plugin) {
        super(CommandType.CONSOLE_OK, "give");
        this.plugin = plugin;
    }

    @Override
    protected ReturnType runCommand(CommandSender sender, String... args) {
        if (args.length == 1) {
            return ReturnType.SYNTAX_ERROR;
        }

        Level level = this.plugin.getLevelManager().getLowestLevel();
        Player player;
        if (args.length != 0 && Bukkit.getPlayer(args[0]) == null) {
            this.plugin.getLocale().newMessage("&cThat player does not exist or is currently offline.").sendPrefixedMessage(sender);
            return ReturnType.FAILURE;
        } else if (args.length == 0) {
            if (!(sender instanceof Player)) {
                this.plugin.getLocale().newMessage("&cYou need to be a player to give a farm item to yourself.").sendPrefixedMessage(sender);
                return ReturnType.FAILURE;
            }
            player = (Player) sender;
        } else {
            player = Bukkit.getPlayer(args[0]);
        }


        if (args.length >= 2 && !this.plugin.getLevelManager().isLevel(Integer.parseInt(args[1]))) {
            this.plugin.getLocale().newMessage("&cNot a valid level... The current valid levels are: &4"
                    + this.plugin.getLevelManager().getLowestLevel().getLevel() + "-"
                    + this.plugin.getLevelManager().getHighestLevel().getLevel() + "&c.").sendPrefixedMessage(sender);
            return ReturnType.FAILURE;
        } else if (args.length != 0) {
            level = this.plugin.getLevelManager().getLevel(Integer.parseInt(args[1]));
        }
        player.getInventory().addItem(plugin.createLeveledAutomator(level.getLevel()));
        this.plugin.getLocale().getMessage("command.give.success")
                .processPlaceholder("level", level.getLevel()).sendPrefixedMessage(sender);

        return ReturnType.SUCCESS;
    }

    @Override
    protected List<String> onTab(CommandSender sender, String... args) {
        return null;
    }

    @Override
    public String getPermissionNode() {
        return "epicautomators.admin.give";
    }

    @Override
    public String getSyntax() {
        return "give [player] <level>";
    }

    @Override
    public String getDescription() {
        return "Give a leveled automator to a player.";
    }
}
