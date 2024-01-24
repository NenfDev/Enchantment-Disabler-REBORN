package me.opd.enchantmentdisabler.listeners;

import me.opd.enchantmentdisabler.EnchantmentDisablerPlugin;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.Random;

import static org.bukkit.enchantments.Enchantment.*;
import static org.bukkit.enchantments.Enchantment.SWIFT_SNEAK;

public class EnchantItemListener implements Listener {
    @EventHandler
    public void onItemEnchant(EnchantItemEvent e){
        ArrayList<Enchantment> toChange = new ArrayList<>();
        long seed = (long) (e.getEnchanter().getLevel() * 3469) + e.getExpLevelCost();
        Enchantment replacementEnchant = EnchantmentDisablerPlugin.enchantUtils.newChosenEnchantment(e.getItem(), seed);

        //e.getEnchanter().sendMessage("Listener seed " + seed);
        //e.getEnchanter().sendMessage(String.valueOf(e.getEnchanter().getLevel()));
        e.getEnchanter().sendMessage(ChatColor.DARK_RED + "Enchants to add " + e.getEnchantsToAdd().keySet());

//        if(!e.getEnchantsToAdd().keySet().contains(e.getEnchantmentHint())){
//            e.getEnchantsToAdd().put(e.getEnchantmentHint(),((e.getExpLevelCost()/30) * (replacementEnchant.getMaxLevel())));
//        }

        for(Map.Entry<Enchantment, Integer> entry : e.getEnchantsToAdd().entrySet()){

            if(entry.getKey() == null){
                continue;
            }

            if(!EnchantmentDisablerPlugin.blockedEnchants.get(entry.getKey())){
                continue;
            }
            toChange.add(entry.getKey());
        }

        for(Enchantment en : toChange){
            e.getEnchantsToAdd().remove(en, e.getEnchantsToAdd().get(en));
            //e.getEnchanter().sendMessage(ChatColor.RED + "Removed enchantment " + en.getName());

            int enchantLevel = ((e.getExpLevelCost()/30) * (replacementEnchant.getMaxLevel()));
            //e.getEnchanter().sendMessage(ChatColor.YELLOW + "Hint enchant was " + replacementEnchant.getName() + " with max level " + replacementEnchant.getMaxLevel());

            if(enchantLevel < 1 || replacementEnchant.getMaxLevel() == 1){
                enchantLevel=1;
            }else if(enchantLevel == replacementEnchant.getMaxLevel()){
                enchantLevel = enchantLevel - 1;
            } else if (enchantLevel > replacementEnchant.getMaxLevel()){
                enchantLevel = replacementEnchant.getMaxLevel();
            }
            //e.getEnchanter().sendMessage(ChatColor.BLUE + "Listener Level " + enchantLevel);
            //TODO add more specific debug messages that are all encompasing to figure out how to fix when there are more enchants than just removed and/or hint
            e.getEnchantsToAdd().put(replacementEnchant, enchantLevel);
        }

        Enchantment conflictedOne = null;

        for(Enchantment ent : e.getEnchantsToAdd().keySet()){
            if(ent == e.getEnchantmentHint()){
                continue;
            }
            if(ent.conflictsWith(e.getEnchantmentHint())){
                //e.getEnchantsToAdd().remove(ent);
                conflictedOne = ent;
            }
        }
        if(conflictedOne != null){
            e.getEnchantsToAdd().remove(conflictedOne);
        }
    }

}
