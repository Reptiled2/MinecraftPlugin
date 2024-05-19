package me.reptiled.crackernetworkcoresurvival;

import me.reptiled.crackernetworkcoresurvival.ChatHandler.ChatHandler;
import me.reptiled.crackernetworkcoresurvival.JoinMessages.JoinMessages;
import me.reptiled.crackernetworkcoresurvival.SetHome.*;
import me.reptiled.crackernetworkcoresurvival.TabFixer.TabFixer;
import me.reptiled.crackernetworkcoresurvival.Tpa.Tpa;
import me.reptiled.crackernetworkcoresurvival.Tpa.TpaAutoComplete;
import me.reptiled.crackernetworkcoresurvival.Tpa.Tpaccept;
import me.reptiled.crackernetworkcoresurvival.Tpa.Tpadeny;
import me.reptiled.crackernetworkcoresurvival.Vanish.VanishCommand;
import me.reptiled.crackernetworkcoresurvival.Vanish.VanishListener;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.luckperms.api.LuckPerms;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Set;


public final class CrackerNetworkCore extends JavaPlugin {

    public LuckPerms LuckpermsApi;
    public Component chatPrefix = MiniMessage.miniMessage().deserialize(
            "<b><gradient:#AA00AA:#FF55FF>ᴄʀᴀᴄᴋᴇʀ ɴᴇᴛᴡᴏʀᴋ</gradient> <dark_gray>>></dark_gray></b> ");

    public SetHome setHomeClass;


    @Override
    public void onEnable() {
        saveResource("homes.json", false);
        LuckpermsApi = getServer().getServicesManager().load(LuckPerms.class);

        getCommand("vanish").setExecutor(new VanishCommand(this));
        getServer().getPluginManager().registerEvents(new VanishListener(this), this);
        getLogger().info("Loaded UltraVanish");

        getServer().getPluginManager().registerEvents(new JoinMessages(this), this);
        getLogger().info("Loaded JoinMessages");

        getServer().getPluginManager().registerEvents(new TabFixer(this), this);
        getLogger().info("Loaded TabFixer");

        getServer().getPluginManager().registerEvents(new ChatHandler(this), this);
        getLogger().info("Loaded ChatHandler");

        setHomeClass = new SetHome(this);
        HomeTabComplete homeTabComplete = new HomeTabComplete();
        getCommand("sethome").setExecutor(setHomeClass);
        getCommand("home").setExecutor(new Home(this));
        getCommand("homes").setExecutor(new Homes(this));
        getCommand("homes").setTabCompleter(homeTabComplete);
        getCommand("delhome").setExecutor(new DelHome(this));
        getCommand("delhome").setTabCompleter(homeTabComplete);
        getLogger().info("Loaded SetHome");

        getCommand("tpa").setExecutor(new Tpa(this));
        getCommand("tpa").setTabCompleter(new TpaAutoComplete(this));
        getCommand("tpaccept").setExecutor(new Tpaccept(this));
        getCommand("tpadeny").setExecutor(new Tpadeny(this));
        getLogger().info("Loaded Tpa");

        getLogger().info("CrackerNetworkCore-Survival has been loaded!");
    }

    @Override
    public void onDisable() {
        getLogger().info("Disabled CrackerNetworkCore-Survival");
        setHomeClass.updateJson();
    }
}
