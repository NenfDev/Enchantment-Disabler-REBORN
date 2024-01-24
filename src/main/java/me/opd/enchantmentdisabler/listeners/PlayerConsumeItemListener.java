package me.opd.enchantmentdisabler.listeners;

import me.opd.enchantmentdisabler.EnchantmentDisablerPlugin;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;

public class PlayerConsumeItemListener implements Listener {

    @EventHandler
    public void onPlayerEat(PlayerItemConsumeEvent e){
        for(ItemStack i : e.getPlayer().getInventory().getContents()){
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
