package me.reptiled.core;

import me.neznamy.tab.api.TabAPI;
import me.reptiled.core.ChatHandler.DiscordCommand;
import me.reptiled.core.JoinMessages.JoinMessages;
import me.reptiled.core.Spawn.Spawn;
import me.reptiled.core.TabFixer.TabFixer;
import me.reptiled.core.Vanish.VanishListener;
import me.reptiled.core.Vanish.VanishCommand;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.luckperms.api.LuckPerms;
import org.bukkit.plugin.java.JavaPlugin;


public final class CrackerNetworkCore extends JavaPlugin {

    public LuckPerms LuckpermsApi;
    public TabAPI TabApi;
    public Component chatPrefix = MiniMessage.miniMessage().deserialize(
            "<b><gradient:#AA00AA:#FF55FF>ᴄʀᴀᴄᴋᴇʀ ɴᴇᴛᴡᴏʀᴋ</gradient> <dark_gray>>></dark_gray></b> ");

    @Override
    public void onEnable() {
        LuckpermsApi = getServer().getServicesManager().load(LuckPerms.class);
        TabApi = TabAPI.getInstance();

        getCommand("vanish").setExecutor(new VanishCommand(this));
        getServer().getPluginManager().registerEvents(new VanishListener(this), this);
        getLogger().info("Loaded Vanish");

        getServer().getPluginManager().registerEvents(new JoinMessages(this), this);
        getLogger().info("Loaded JoinMessages");

        getServer().getPluginManager().registerEvents(new TabFixer(this), this);
        getLogger().info("Loaded TabFixer");

        getServer().getPluginManager().registerEvents(new Spawn(this), this);
        getLogger().info("Loaded Spawn");

        getServer().getPluginManager().registerEvents(new me.reptiled.crackernetworkcoresurvival.ChatHandler.ChatHandler(this), this);
        getLogger().info("Loaded ChatHandler");

        getCommand("discord").setExecutor(new DiscordCommand());
        getLogger().info("Loaded Discord");

        getLogger().info("CrackerNetworkCore has been loaded!");
    }

    @Override
    public void onDisable() {
        getLogger().info("Disabled CrackerNetworkCore");
    }
}
