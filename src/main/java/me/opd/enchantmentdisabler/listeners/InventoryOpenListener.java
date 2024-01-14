package me.opd.enchantmentdisabler.listeners;

import me.opd.enchantmentdisabler.EnchantmentDisablerPlugin;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.ItemStack;

public class InventoryOpenListener implements Listener {
    @EventHandler
    public void onPlayerOpenAnInventory(InventoryOpenEvent e){
        //Player p = (Player) e.getPlayer();

        if(e.getView().getTitle().equals(ChatColor.GRAY + "" + ChatColor.BOLD + "Select Enchants")){
            return;
        }

        for(ItemStack i : e.getInventory().getContents()){
            if(i==null || i.getType().equals(Material.AIR)){
                continue;
            }
            for(Enchantment en : i.getEnchantments().keySet()){
                if(!EnchantmentDisablerPlugin.allowedEnchant.contains(en)){
                    i.removeEnchantment(en);
                }
            }
        }
    }
}
