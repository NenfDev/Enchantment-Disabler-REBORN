package me.opd.enchantmentdisabler.listeners;

import me.opd.enchantmentdisabler.EnchantmentDisablerPlugin;
import org.bukkit.Bukkit;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentOffer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.enchantment.PrepareItemEnchantEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static org.bukkit.enchantments.Enchantment.*;

public class PrepareItemEnchantListener implements Listener {

//TODO Need to make so that they can't re roll enchants by spamming the item
    //TODO Need to make sure they are accurate for the level being applied to them
    @EventHandler
    public void onPlayerPrepEnchant(PrepareItemEnchantEvent e){
        Player p = e.getEnchanter();
        long seed = (long) p.getLevel() * 3469;
        for(EnchantmentOffer eo : e.getOffers()){
            if(eo == null){
                continue;
            }
            if(EnchantmentDisablerPlugin.blockedEnchants.get(eo.getEnchantment()) == true){
                eo.setEnchantment(newChosenEnchantment(e.getItem(), seed));
                //seed = seed + 30;
                //When setting the level, do something like this...
                int enchantLevel = ((eo.getCost()/30) * eo.getEnchantment().getMaxLevel());
                //Will have to make sure rounding and edge cases work
                if(enchantLevel<1){
                    enchantLevel=1;
                }

                eo.setEnchantmentLevel(enchantLevel);
                e.getEnchanter().updateInventory();
            }
        }
    }

    public Enchantment newChosenEnchantment(ItemStack item, long seed){
        Enchantment[] notTable = new Enchantment[]{VANISHING_CURSE,BINDING_CURSE,FROST_WALKER,MENDING,SOUL_SPEED,SWIFT_SNEAK};
        List notPossibleFromTable = Arrays.asList(notTable);

        Random rand = new Random();
        //Use seeded randomness to prevent spammers?
        rand.setSeed(seed);
        System.out.println("Seed: " + seed);

        int chosen = rand.nextInt(EnchantmentDisablerPlugin.allowedEnchant.size() - 1);
        System.out.println(EnchantmentDisablerPlugin.allowedEnchant.get(chosen).toString());
        if(EnchantmentDisablerPlugin.allowedEnchant.get(chosen).canEnchantItem(item) && !notPossibleFromTable.contains(EnchantmentDisablerPlugin.allowedEnchant.get(chosen))){
            return EnchantmentDisablerPlugin.allowedEnchant.get(chosen);
        }else{
            return newChosenEnchantment(item, seed+1);
        }
    }
}
