package me.reptiled.crackernetworkcoresurvival;

import me.neznamy.tab.api.TabAPI;
import me.reptiled.crackernetworkcoresurvival.ChatHandler.ChannelAutoComplete;
import me.reptiled.crackernetworkcoresurvival.ChatHandler.ChannelCommand;
import me.reptiled.crackernetworkcoresurvival.ChatHandler.ChatHandler;
import me.reptiled.crackernetworkcoresurvival.ChatHandler.TagCommand;
import me.reptiled.crackernetworkcoresurvival.InvSee.InvSeeCommand;
import me.reptiled.crackernetworkcoresurvival.Teams.LeaveTeam;
import me.reptiled.crackernetworkcoresurvival.Teams.TeamCommands;
import me.reptiled.crackernetworkcoresurvival.Tpa.*;
import me.reptiled.crackernetworkcoresurvival.Tpa.InvSeeTabComplete;
import me.reptiled.crackernetworkcoresurvival.Tpa.TagAutoComplete;
import me.reptiled.crackernetworkcoresurvival.Tpa.TeamAutoComplete;
import me.rofl.CombatLog.CombatLog;
import me.reptiled.crackernetworkcoresurvival.JoinMessages.JoinMessages;
import me.reptiled.crackernetworkcoresurvival.SetHome.*;
import me.reptiled.crackernetworkcoresurvival.TabFixer.TabFixer;
import me.reptiled.crackernetworkcoresurvival.Teams.CreateTeam;
import me.reptiled.crackernetworkcoresurvival.Vanish.VanishCommand;
import me.reptiled.crackernetworkcoresurvival.Vanish.VanishListener;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.luckperms.api.LuckPerms;
import org.bukkit.plugin.java.JavaPlugin;


public final class CrackerNetworkCore extends JavaPlugin {

    public LuckPerms LuckpermsApi;
    public TabAPI TabApi;
    public Component chatPrefix = MiniMessage.miniMessage().deserialize(
        "<b><gradient:#AA00AA:#FF55FF>ᴄʀᴀᴄᴋᴇʀ ɴᴇᴛᴡᴏʀᴋ</gradient> <dark_gray>>></dark_gray></b> ");

    public SetHome setHomeClass;
    public CreateTeam teamClass;

    @Override
    public void onEnable() {
        saveResource("homes.json", false);
        saveResource("teams.json", false);
        LuckpermsApi = getServer().getServicesManager().load(LuckPerms.class);
        TabApi = TabAPI.getInstance();

        getCommand("vanish").setExecutor(new VanishCommand(this));
        getServer().getPluginManager().registerEvents(new VanishListener(this), this);
        getLogger().info("Loaded Vanish");

        getServer().getPluginManager().registerEvents(new JoinMessages(this), this);
        getLogger().info("Loaded JoinMessages");

        getServer().getPluginManager().registerEvents(new TabFixer(this), this);
        getLogger().info("Loaded TabFixer");

        getServer().getPluginManager().registerEvents(new ChatHandler(this), this);
        getCommand("changeTag").setExecutor(new TagCommand(this));
        getCommand("changeTag").setTabCompleter(new TagAutoComplete(this));
        getLogger().info("Loaded ChatHandler");

        this.setHomeClass = new SetHome(this);
        HomeTabComplete homeTabComplete = new HomeTabComplete();
        getCommand("sethome").setExecutor(setHomeClass);
        getCommand("home").setExecutor(new Home(this));
        getCommand("home").setTabCompleter(homeTabComplete);
        getCommand("homes").setExecutor(new Homes(this));
        getCommand("delhome").setExecutor(new DelHome(this));
        getCommand("delhome").setTabCompleter(homeTabComplete);
        getLogger().info("Loaded SetHome");

        getCommand("tpa").setExecutor(new Tpa(this));
        getCommand("tpa").setTabCompleter(new TpaAutoComplete());
        getCommand("tpaccept").setExecutor(new Tpaccept(this));
        getCommand("tpadeny").setExecutor(new Tpadeny(this));
        getLogger().info("Loaded Tpa");

        getServer().getPluginManager().registerEvents(new CombatLog(this), this);
        getLogger().info("Loaded CombatLog");

        this.teamClass = new CreateTeam(this);
        getCommand("createTeam").setExecutor(teamClass);
        getCommand("leaveTeam").setExecutor(new LeaveTeam(this));
        getCommand("team").setExecutor(new TeamCommands(this));
        getCommand("team").setTabCompleter(new TeamAutoComplete(this));
        getLogger().info("Loaded Teams");

        getCommand("channel").setExecutor(new ChannelCommand(this));
        getCommand("channel").setTabCompleter(new ChannelAutoComplete());
        getLogger().info("Loaded Channels");

        getCommand("invsee").setExecutor(new InvSeeCommand());
        getCommand("invsee").setTabCompleter(new InvSeeTabComplete());
        getLogger().info("Loaded InvSee");

        getLogger().info("CrackerNetworkCore-Survival has been loaded!");
    }

    @Override
    public void onDisable() {
        this.setHomeClass.updateJson();
        this.teamClass.saveData();
        getLogger().info("Disabled CrackerNetworkCore-Survival");
    }
}