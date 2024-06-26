package br.com.pulse.bw1058_ranked.queue;

import br.com.pulse.bw1058_ranked.elo.EloManager;
import com.andrei1058.bedwars.api.BedWars;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class JoinQueueCommand implements CommandExecutor, Listener {

    private final QueueManager queueManager;
    private final EloManager eloManager;
    BedWars bedwarsAPI = Bukkit.getServicesManager().getRegistration(BedWars.class).getProvider();

    public JoinQueueCommand(QueueManager queueManager, EloManager eloManager) {
        this.queueManager = queueManager;
        this.eloManager = eloManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§cEste comando só pode ser executado por jogadores.");
            return true;
        }
        Player player = (Player) sender;
        if (!player.hasPermission("bw.vip") && bedwarsAPI.getStatsUtil().getPlayerWins(player.getUniqueId()) < 100) {
            player.sendMessage("");
            player.sendMessage("§c§lVocê precisa ter §a§lVIP §c§l ou mais de");
            player.sendMessage("§c§l100 WINS para entrar em uma fila");
            player.sendMessage("");
            return true;
        }

        openJoinMenu(player);
        return true;
    }

    public void openJoinMenu(Player player) {
        Inventory joinMenu = Bukkit.createInventory(player, 45, "§7Entrar em uma Fila");

        ItemStack infoItem = new ItemStack(Material.PAPER);
        ItemMeta infoMeta = infoItem.getItemMeta();
        infoMeta.setDisplayName("§aInformações Pessoais");
        List<String> infoLore = new ArrayList<>();
        infoLore.add("§7Veja suas informações pessoais.");
        infoLore.add("");
        infoLore.add("§7Seu Rank: " + eloManager.getRank(eloManager.getElo(player.getUniqueId(), "geral")));
        infoLore.add("§7Seu Elo Geral: §5" + eloManager.getElo(player.getUniqueId(), "geral"));
        infoMeta.setLore(infoLore);
        infoItem.setItemMeta(infoMeta);
        joinMenu.setItem(13, infoItem);

        ItemStack Ranked1v1Item = new ItemStack(Material.BED);
        ItemMeta Ranked1v1Meta = Ranked1v1Item.getItemMeta();
        Ranked1v1Meta.setDisplayName("§a1v1 Ranked");
        List<String> Ranked1v1Lore = new ArrayList<>();
        Ranked1v1Lore.add("§7Entrar na fila para 1v1 Ranked.");
        Ranked1v1Lore.add("");
        Ranked1v1Lore.add("§7Elo Ranked1v1: §5" + eloManager.getElo(player.getUniqueId(), "1v1"));
        Ranked1v1Lore.add("");
        Ranked1v1Lore.add("§eClique para entrar na fila.");
        Ranked1v1Meta.setLore(Ranked1v1Lore);
        Ranked1v1Item.setItemMeta(Ranked1v1Meta);
        joinMenu.setItem(30, Ranked1v1Item);

        ItemStack RankedSoloItem = new ItemStack(Material.BED);
        ItemMeta RankedSoloMeta = RankedSoloItem.getItemMeta();
        RankedSoloMeta.setDisplayName("§aSolo Ranked");
        List<String> RankedSoloLore = new ArrayList<>();
        RankedSoloLore.add("§7Entrar na fila para Solo Ranked.");
        RankedSoloLore.add("");
        RankedSoloLore.add("§7Elo RankedSolo: §5" + eloManager.getElo(player.getUniqueId(), "solo"));
        RankedSoloLore.add("");
        RankedSoloLore.add("§eClique para entrar na fila.");
        RankedSoloMeta.setLore(RankedSoloLore);
        RankedSoloItem.setItemMeta(RankedSoloMeta);
        joinMenu.setItem(32, RankedSoloItem);

        player.openInventory(joinMenu);
    }

    @EventHandler
    public void onClickEvent(InventoryClickEvent e) {
        Player player = (Player) e.getWhoClicked();
        if (e.getView().getTitle().equalsIgnoreCase("§7Entrar em uma Fila")) {
            e.setCancelled(true);
            if (e.getCurrentItem() == null || e.getCurrentItem().getType() == Material.AIR) {
                return;
            }
            if (e.getSlot() == 29 && e.getCurrentItem().getType() == Material.BED) {
                if (bedwarsAPI.getPartyUtil().hasParty(player)) {
                    player.sendMessage("§cVocê não pode entrar em uma fila ranqueada em party!");
                    player.closeInventory();
                    player.playSound(player.getLocation(), Sound.NOTE_BASS, 1, 1);
                    return;
                }
                queueManager.joinQueue(player, "Ranked1v1");
                player.closeInventory();
            } else if (e.getSlot() == 33 && e.getCurrentItem().getType() == Material.BED) {
                if (bedwarsAPI.getPartyUtil().hasParty(player)) {
                    player.sendMessage("§cVocê não pode entrar em uma fila ranqueada em party!");
                    player.closeInventory();
                    player.playSound(player.getLocation(), Sound.NOTE_BASS, 1, 1);
                    return;
                }
                bedwarsAPI.getArenaUtil().joinRandomFromGroup(player, "RankedSolo");
                player.closeInventory();
            }
        }
    }
}
