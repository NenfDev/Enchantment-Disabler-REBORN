package me.opd.enchantmentdisabler.listeners;

import me.opd.enchantmentdisabler.EnchantmentDisablerPlugin;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.enchantment.EnchantItemEvent;

import java.util.ArrayList;
import java.util.Map;

public class EnchantItemListener implements Listener {
    @EventHandler
    public void onItemEnchant(EnchantItemEvent e){
        ArrayList<Enchantment> toChange = new ArrayList<>();
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

            int enchantLevel = ((e.getExpLevelCost()/30) * (e.getEnchantmentHint().getMaxLevel()));
            //e.getEnchanter().sendMessage((double)e.getExpLevelCost()/30 + " is the listener level");
            //e.getEnchanter().sendMessage(en.getMaxLevel() + " is listener max level of " + en.getName());

            if(enchantLevel<1 || e.getEnchantmentHint().getMaxLevel() == 1){
                enchantLevel=1;
            }else if (enchantLevel > e.getEnchantmentHint().getMaxLevel()){
                enchantLevel = e.getEnchantmentHint().getMaxLevel();
            }
            e.getEnchantsToAdd().put(e.getEnchantmentHint(), enchantLevel);
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
