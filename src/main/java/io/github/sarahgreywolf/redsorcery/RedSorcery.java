package io.github.sarahgreywolf.redsorcery;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import org.bukkit.command.PluginCommand;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import net.kyori.adventure.text.Component;

import io.github.sarahgreywolf.redsorcery.commands.HelpCommand;
import io.github.sarahgreywolf.redsorcery.interfaces.ICommand;
import io.github.sarahgreywolf.redsorcery.interfaces.IRitual;
import io.github.sarahgreywolf.redsorcery.listeners.KillListener;
import io.github.sarahgreywolf.redsorcery.rituals.HaltRain;

public final class RedSorcery extends JavaPlugin {

    public static RedSorcery plugin;
    public static PluginManager pm;
    public static final String pluginPrefix = "\u00A75[RedSorcery]\u00A7r";

    private Map<String, ICommand> commands = new HashMap<>();
    private List<IRitual> rituals = new ArrayList<>();

    @Override
    public void onEnable() {
        plugin = this;
        pm = getServer().getPluginManager();
        pm.registerEvents(new KillListener(), this);
        CommandHandler RedSorceryCommand = new CommandHandler();
        PluginCommand command = getCommand("RedSorcery");
        command.setExecutor(RedSorceryCommand);
        command.setTabCompleter(RedSorceryCommand);
        command.permissionMessage(Component.text("\u00A74 You do not have permission to execute this command"));
        registerCommands();
        registerRituals();
        registerPermissions();
    }

    @Override
    public void onDisable() {
        commands.clear();
        rituals.clear();
        pm = null;
        plugin = null;
    }

    public Map<String, ICommand> getCommands() {
        return commands;
    }

    public void addCommand(ICommand command) {
        commands.put(command.getName(), command);
    }

    private void registerCommands() {
        addCommand(new HelpCommand());
    }

    private void registerPermissions() {
        for (String command : getCommands().keySet()) {
            pm.addPermission(new Permission("redsorcery." + getCommands().get(command).getPermission()));
        }
        pm.addPermission(new Permission("redsorcery.commands.*"));
        pm.addPermission(new Permission("redsorcery.rituals.*"));
        pm.addPermission(new Permission("redsorcery.*"));
        pm.addPermission(new Permission("redsorcery"));
        for (IRitual ritual : getRituals()) {
            pm.addPermission(new Permission("redsorcery.ritual." + ritual.getPermission()));
        }
    }

    public List<IRitual> getRituals() {
        return rituals;
    }

    public void addRitual(IRitual ritual) {
        rituals.add(ritual);
    }

    private void registerRituals() {
        addRitual(new HaltRain());
    }

}
