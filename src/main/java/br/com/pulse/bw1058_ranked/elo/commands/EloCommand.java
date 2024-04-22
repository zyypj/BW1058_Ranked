package br.com.pulse.bw1058_ranked.elo.commands;

import br.com.pulse.bw1058_ranked.elo.EloManager;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class EloCommand implements CommandExecutor {

    private final EloManager eloManager;

    public EloCommand(EloManager eloManager) {
        this.eloManager = eloManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Esse comando só pode ser executado por jogadores.");
            return true;
        }
        Player player = (Player) sender;
        UUID playerUUID = player.getUniqueId();
        int eloSolo = eloManager.getElo(playerUUID, "rankedsolo");
        int elo1v1 = eloManager.getElo(playerUUID, "ranked1v1");
        int elo4v4 = eloManager.getElo(playerUUID, "ranked4v4");
        int eloSoma = eloSolo + elo1v1 + elo4v4;
        int eloGeral = eloSoma / 3;
        if (args.length == 0) {
            player.sendMessage("§5§lPulse §7§lRanked");
            player.sendMessage("");
            player.sendMessage("§7Elo de §l" + player.getName());
            player.sendMessage("§7Rank: " + eloManager.getRank(eloGeral));
            player.sendMessage("§7Geral: §5" + eloGeral);
            player.sendMessage("§7Solo: §5" + eloSolo);
            player.sendMessage("§71v1: §5" + elo1v1);
            player.sendMessage("§74v4: §5" + elo4v4);
            player.sendMessage("");
            return true;
        }
        if (args.length == 1) {
            String tipo = args[0];
            switch (tipo.toLowerCase()) {
                case "solo":
                    sender.sendMessage("§7Seu Elo Solo é: §5" + eloSolo);
                    return true;
                case "1v1":
                    sender.sendMessage("§7Seu Elo 1v1 é: §5" + elo1v1);
                    return true;
                case "4v4":
                    sender.sendMessage("§7Seu Elo 4v4 é: §5" + elo4v4);
                    return true;
                case "geral":
                    sender.sendMessage("§7Seu Elo Geral é: §5" + eloGeral);
                    return true;
                case "all":
                    sender.sendMessage("§5§lPulse §7§lRanked");
                    sender.sendMessage("");
                    sender.sendMessage("§7Elo de §l" + sender.getName());
                    sender.sendMessage("§7Rank: " + eloManager.getRank(eloGeral));
                    sender.sendMessage("§7Geral: §5" + eloGeral);
                    sender.sendMessage("§7Solo: §5" + eloSolo);
                    sender.sendMessage("§71v1: §5" + elo1v1);
                    sender.sendMessage("§74v4: §5" + elo4v4);
                    sender.sendMessage("");
                default:
                    sender.sendMessage("§5§lPulse §7§lRanked");
                    sender.sendMessage("");
                    sender.sendMessage("§7Elo de §l" + sender.getName());
                    sender.sendMessage("§7Rank: " + eloManager.getRank(eloGeral));
                    sender.sendMessage("§7Geral: §5" + eloGeral);
                    sender.sendMessage("§7Solo: §5" + eloSolo);
                    sender.sendMessage("§71v1: §5" + elo1v1);
                    sender.sendMessage("§74v4: §5" + elo4v4);
                    sender.sendMessage("");
            }
        }
        if (args.length == 2) {
            Player target = Bukkit.getPlayer(args[0]);
            String tipo = args[1];
            if (target == null) {
                sender.sendMessage("§cJogador não encontrado ou offline.");
                player.playSound(player.getLocation(), Sound.NOTE_BASS, 1, 1);
                return true;
            }
            UUID targetUUID = target.getUniqueId();
            int eloSoloT = eloManager.getElo(targetUUID, "rankedsolo");
            int elo1v1T = eloManager.getElo(targetUUID, "ranked1v1");
            int elo4v4T = eloManager.getElo(targetUUID, "ranked4v4");
            int eloSomaT = eloSoloT + elo1v1T + elo4v4T;
            int eloGeralT = eloSomaT / 3;
            switch (tipo.toLowerCase()) {
                case "solo":
                    sender.sendMessage("§7Elo Solo de §l" + target.getName() + " §7é: §5" + eloSoloT);
                    return true;
                case "1v1":
                    sender.sendMessage("§7Elo 1v1 de §l" + target.getName() + " §7é: §5" + elo1v1T);
                    return true;
                case "4v4":
                    sender.sendMessage("§7Elo 4v4 de §l" + target.getName() + " §7é: §5" + elo4v4T);
                    return true;
                case "geral":
                    sender.sendMessage("§7Elo Geral de §l" + target.getName() + " §7é: §5" + eloGeralT);
                    return true;
                case "all":
                    sender.sendMessage("§5§lPulse §7§lRanked");
                    sender.sendMessage("");
                    sender.sendMessage("§7Elo de §l" + target.getName());
                    sender.sendMessage("§7Rank: " + eloManager.getRank(eloGeralT));
                    sender.sendMessage("§7Geral: §5" + eloGeralT);
                    sender.sendMessage("§7Solo: §5" + eloSoloT);
                    sender.sendMessage("§71v1: §5" + elo1v1T);
                    sender.sendMessage("§74v4: §5" + elo4v4T);
                    sender.sendMessage("");
                default:
                    sender.sendMessage("§5§lPulse §7§lRanked");
                    sender.sendMessage("");
                    sender.sendMessage("§7Elo de §l" + target.getName());
                    sender.sendMessage("§7Rank: " + eloManager.getRank(eloGeralT));
                    sender.sendMessage("§7Geral: §5" + eloGeralT);
                    sender.sendMessage("§7Solo: §5" + eloSoloT);
                    sender.sendMessage("§71v1: §5" + elo1v1T);
                    sender.sendMessage("§74v4: §5" + elo4v4T);
                    sender.sendMessage("");
            }
        }
        if (sender.hasPermission("bwranked.admin")) {
            if (args.length == 4 && args[0].equalsIgnoreCase("add")) {
                String nick = args[1];
                String tipo = args[2];
                String number = args[3];
                Player target = Bukkit.getPlayer(nick);
                if (target == null) {
                    sender.sendMessage("§cUse: /elo add <nick> <modo> <novoElo>.");
                    return true;
                }
                UUID targetUUID = target.getUniqueId();
                int newElo;
                try {
                    newElo = Integer.parseInt(number);
                } catch (NumberFormatException e) {
                    sender.sendMessage("§cO número fornecido não é válido.");
                    return true;
                }
                switch (tipo.toLowerCase()) {
                    case "solo":
                        eloManager.setElo(targetUUID, "rankedsolo", newElo);
                        sender.sendMessage("§7O Elo Solo de §l" + target.getName() + " §7foi definido para §5" + newElo);
                        target.sendMessage("§7Seu Elo Solo foi definido para §5" + newElo + " §7por §6§lMASTER §6" + sender.getName());
                        return true;
                    case "1v1":
                        eloManager.setElo(targetUUID, "ranked1v1", newElo);
                        sender.sendMessage("§7O Elo 1v1 de §l" + target.getName() + " §7foi definido para §5" + newElo);
                        target.sendMessage("§7Seu Elo 1v1 foi definido para §5" + newElo + " §7por §6§lMASTER §6" + sender.getName());
                        return true;
                    case "4v4":
                        eloManager.setElo(targetUUID, "ranked4v4", newElo);
                        sender.sendMessage("§7O Elo 4v4 de §l" + target.getName() + " §7foi definido para §5" + newElo);
                        target.sendMessage("§7Seu Elo 4v4 foi definido para §5" + newElo + " §7por §6§lMASTER §6" + sender.getName());
                        return true;
                    default:
                        sender.sendMessage("§cTipo de partida inválido. Use 'Solo', '1v1' ou '4v4'.");
                        player.playSound(player.getLocation(), Sound.NOTE_BASS, 1, 1);
                        return true;

                }
            }
        } else {
            sender.sendMessage("§cComando não encontrado ou você não tem permissão");
            player.playSound(player.getLocation(), Sound.NOTE_BASS, 1, 1);
            return true;
        }
        return true;
    }
}
