package me.opd.enchantmentdisabler.listeners;

import me.opd.enchantmentdisabler.EnchantmentDisablerPlugin;
import me.opd.enchantmentdisabler.utils.ConfigUtilsEN;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;

import java.util.ArrayList;

public class ItemClickListener implements Listener {
    EnchantmentDisablerPlugin plugin;

    public ItemClickListener(EnchantmentDisablerPlugin plugin){
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerClickItem(InventoryClickEvent e){

        Player p = (Player) e.getWhoClicked();
        if(e.getView().getTitle().equals(ChatColor.GRAY + "" + ChatColor.BOLD + "Select Enchants")){
            ItemStack item = e.getCurrentItem();
            if(item == null || item.getType().equals(Material.AIR) || item.getType() != Material.ENCHANTED_BOOK){
                e.setCancelled(true);
                return;

            }
            if(item.getType().equals(Material.PAPER)){
                e.setCancelled(true);
                return;
            }

            EnchantmentStorageMeta itemMeta = (EnchantmentStorageMeta) item.getItemMeta();

            ArrayList<String> lore = (ArrayList<String>) itemMeta.getLore();

            if(lore.get(1).contains("ENABLED")){
                lore.set(1, "§c§lDISABLED");
                lore.set(2, "§7Click to §aEnable");
                EnchantmentDisablerPlugin.blockedEnchants.put((Enchantment) itemMeta.getStoredEnchants().keySet().toArray()[0], true);
                EnchantmentDisablerPlugin.allowedEnchant.remove(itemMeta.getStoredEnchants().keySet().toArray()[0]);
            }else{
                lore.set(1, "§a§lENABLED");
                lore.set(2, "§7Click to §cDisable");
                EnchantmentDisablerPlugin.blockedEnchants.put((Enchantment) itemMeta.getStoredEnchants().keySet().toArray()[0], false);
                EnchantmentDisablerPlugin.allowedEnchant.add((Enchantment) itemMeta.getStoredEnchants().keySet().toArray()[0]);
            }
            itemMeta.setLore(lore);
            item.setItemMeta(itemMeta);

            p.updateInventory();
            p.playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
            e.setCancelled(true);

            ConfigUtilsEN.syncHashMapWithConfig(plugin);
        }

        if(e.getClickedInventory()==null){
            return;
        }

        if(e.getClickedInventory().getType().equals(InventoryType.MERCHANT)){
            if(e.getCurrentItem().getType().equals(Material.ENCHANTED_BOOK)){
                EnchantmentStorageMeta em = (EnchantmentStorageMeta) e.getCurrentItem().getItemMeta();
                for(Enchantment en : em.getStoredEnchants().keySet()){
                    if(!EnchantmentDisablerPlugin.allowedEnchant.contains(en)){
                        p.sendMessage(ChatColor.RED + "You are not allowed to have that enchantment.");
                        e.setCancelled(true);
                        return;
                    }
                }
            }
            for(Enchantment en : e.getCurrentItem().getEnchantments().keySet()){
                if(!EnchantmentDisablerPlugin.allowedEnchant.contains(en)){
                    p.sendMessage(ChatColor.RED + "You are not allowed to have that enchantment.");
                    e.setCancelled(true);
                    return;
                }
            }
        }
    }
}
