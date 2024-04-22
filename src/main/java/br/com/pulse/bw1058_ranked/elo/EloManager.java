package br.com.pulse.bw1058_ranked.elo;

import br.com.pulse.bw1058_ranked.Main;
import br.com.pulse.bw1058_ranked.mvp.MvpManager;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class EloManager {

    private final FileConfiguration playerData;
    private final MvpManager mvpManager;

    public EloManager(Main plugin, FileConfiguration playerData, MvpManager mvpManager) {
        this.mvpManager = mvpManager;
        this.playerData = playerData;
    }

    public int getElo(UUID playerUUID, String type) {
        return playerData.getInt(playerUUID + "." + type, 1000);
    }

    public void setElo(UUID playerUUID, String type, int elo) {
        playerData.set(playerUUID.toString() + "." + type, elo);
        savePlayerData();
    }

    public void addElo(UUID playerUUID, int eloChange, String type) {
        int currentElo = getElo(playerUUID, type);
        int newElo = currentElo + eloChange;

        if (newElo < 0) {
            newElo = 0; // Elo não pode ser negativo
        }

        setElo(playerUUID, type, newElo);
        savePlayerData();
    }

    public String getRank(int elo) {
        if (elo < 1000) {
            return "§4[Bronze III]";
        } else if (elo < 1050) {
            return "§4[Bronze II]";
        } else if (elo < 1100) {
            return "§4[Bronze I]";
        } else if (elo < 1150) {
            return "§8[Prata III]";
        } else if (elo < 1200) {
            return "§8[Prata II]";
        } else if (elo < 1250) {
            return "§8[Prata I]";
        } else if (elo < 1300) {
            return "§6[Ouro III]";
        } else if (elo < 1350) {
            return "§6[Ouro II]";
        } else if (elo < 1400) {
            return "§6[Ouro I]";
        } else {
            return "§c[Sem Rank]";
        }
    }

    public void savePlayerData() {
        File dataFolder = new File(Bukkit.getPluginManager().getPlugin("BedWars1058").getDataFolder(), "Addons/Ranked");
        if (!dataFolder.exists()) {
            dataFolder.mkdirs();
        }
        File playerDataFile = new File(dataFolder, "playersElo.yml");
        try {
            YamlConfiguration config = new YamlConfiguration();
            for (String uuidString : playerData.getKeys(false)) {

                // Define o elo inicial como 1000 em todos os modos
                int eloSolo = 1000;
                int elo1v1 = 1000;
                int elo4v4 = 1000;
                int eloSoma = eloSolo + elo1v1 + elo4v4;
                int eloGeral = eloSoma / 3;
                int mvpCount = 0;

                // Se o jogador já tiver um elo registrado, mantém o elo atual
                if (playerData.contains(uuidString + ".rankedsolo")) {
                    eloSolo = playerData.getInt(uuidString + ".rankedsolo");
                }
                if (playerData.contains(uuidString + ".ranked1v1")) {
                    elo1v1 = playerData.getInt(uuidString + ".ranked1v1");
                }
                if (playerData.contains(uuidString + ".ranked4v4")) {
                    elo4v4 = playerData.getInt(uuidString + ".ranked4v4");
                }
                if (playerData.contains(uuidString + ".rankedgeral")) {
                    eloGeral = playerData.getInt(uuidString + ".rankedgeral");
                }
                if (playerData.contains(uuidString + ".mvp")) {
                    mvpCount = playerData.getInt(uuidString + ".mvp");
                }

                config.set(uuidString + ".rankedgeral", eloGeral);
                config.set(uuidString + ".rankedsolo", eloSolo);
                config.set(uuidString + ".ranked1v1", elo1v1);
                config.set(uuidString + ".ranked4v4", elo4v4);
                config.set(uuidString + ".mvp", mvpCount);
            }
            config.save(playerDataFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public FileConfiguration getPlayerData() {
        return playerData;
    }
}
