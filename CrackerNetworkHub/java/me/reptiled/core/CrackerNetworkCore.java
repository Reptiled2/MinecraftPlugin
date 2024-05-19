package me.reptiled.core;

import me.reptiled.core.ChatHandler.ChatHandler;
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
    public Component chatPrefix = MiniMessage.miniMessage().deserialize(
            "<b><gradient:#AA00AA:#FF55FF>ᴄʀᴀᴄᴋᴇʀ ɴᴇᴛᴡᴏʀᴋ</gradient> <dark_gray>>></dark_gray></b> ");

    @Override
    public void onEnable() {
        LuckpermsApi = getServer().getServicesManager().load(LuckPerms.class);


        getCommand("vanish").setExecutor(new VanishCommand(this));
        getServer().getPluginManager().registerEvents(new VanishListener(this), this);
        getLogger().info("Loaded UltraVanish");

        getServer().getPluginManager().registerEvents(new JoinMessages(this), this);
        getLogger().info("Loaded JoinMessages");

        getServer().getPluginManager().registerEvents(new TabFixer(this), this);
        getLogger().info("Loaded TabFixer");

        getServer().getPluginManager().registerEvents(new Spawn(this), this);
        getLogger().info("Loaded Spawn");

        getServer().getPluginManager().registerEvents(new ChatHandler(this), this);
        getLogger().info("Loaded ChatHandler");

        getLogger().info("CrackerNetworkCore has been loaded!");
    }

    @Override
    public void onDisable() {
        getLogger().info("Disabled CrackerNetworkCore");
    }
}
