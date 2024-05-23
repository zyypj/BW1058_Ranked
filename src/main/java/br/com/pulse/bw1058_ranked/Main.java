package br.com.pulse.bw1058_ranked;

import br.com.pulse.bw1058_ranked.elo.EloListener;
import br.com.pulse.bw1058_ranked.elo.EloManager;
import br.com.pulse.bw1058_ranked.elo.commands.EloCommand;
import br.com.pulse.bw1058_ranked.misc.RankCommand;
import br.com.pulse.bw1058_ranked.queue.JoinQueueCommand;
import br.com.pulse.bw1058_ranked.queue.LeaveQueueCommand;
import br.com.pulse.bw1058_ranked.queue.QueueManager;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public final class Main extends JavaPlugin {

    private static EloManager eloManager;
    private static QueueManager queueManager;

    @Override
    public void onEnable() {
        Plugin bedWarsPlugin = Bukkit.getPluginManager().getPlugin("BedWars1058");
        if (Bukkit.getPluginManager().getPlugin("BedWars1058") == null) {
            getLogger().severe("BedWars1058 was not found. Disabling...");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }
        File dataFolder = new File(bedWarsPlugin.getDataFolder(), "Addons/Ranked");
        if (!dataFolder.exists()) {
            dataFolder.mkdirs();
        }
        File playerDataFile = new File(dataFolder, "playersElo.yml");
        if (!playerDataFile.exists()) {
            try {
                playerDataFile.createNewFile();
            } catch (IOException e) {
                getLogger().severe("Erro ao criar o arquivo playersElo.yml: " + e.getMessage());
            }
        }
        FileConfiguration playerData = YamlConfiguration.loadConfiguration(playerDataFile);
        queueManager = new QueueManager(this, eloManager);
        eloManager = new EloManager(this, playerData);
        Bukkit.getPluginManager().registerEvents(new JoinQueueCommand(queueManager, eloManager), this);
        Bukkit.getPluginManager().registerEvents(new EloListener(eloManager, this, playerData), this);
        getCommand("joinqueue").setExecutor(new JoinQueueCommand(queueManager, eloManager));
        getCommand("leavequeue").setExecutor(new LeaveQueueCommand(queueManager));
        getCommand("rank").setExecutor(new RankCommand(eloManager));
        getCommand("elo").setExecutor(new EloCommand(eloManager));
        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            Bukkit.getScheduler().runTaskLater(this, () ->
                    getLogger().info("Hook to PlaceholderAPI support!"), 20L);
            new Placeholder(this, eloManager).register();
        }
        getLogger().info("");
        getLogger().info("BW1058 Ranked");
        getLogger().info("by tadeu");
        getLogger().info("1.0");
        getLogger().info("");
    }

    @Override
    public void onDisable() {
        HandlerList.unregisterAll();
    }

    public static EloAPI getEloAPI() {
        return eloManager;
    }

    public static QueueAPI getQueueAPI() {
        return queueManager;
    }
}
