package me.opd.enchantmentdisabler.listeners;

import me.opd.enchantmentdisabler.EnchantmentDisablerPlugin;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentOffer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.enchantment.PrepareItemEnchantEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import static org.bukkit.enchantments.Enchantment.*;

public class PrepareItemEnchantListener implements Listener {

    @EventHandler
    public void onPlayerPrepEnchant(PrepareItemEnchantEvent e){
        Player p = e.getEnchanter();
        long seed = (long) p.getLevel() * 3469;

        for(EnchantmentOffer eo : e.getOffers()){
            if(eo == null){
                continue;
            }

            seed = seed + eo.getCost();

            if(EnchantmentDisablerPlugin.blockedEnchants.get(eo.getEnchantment())){
                eo.setEnchantment(newChosenEnchantment(e.getItem(), seed,
                        new Enchantment[]{e.getOffers()[0].getEnchantment(),
                                e.getOffers()[1].getEnchantment(),
                        e.getOffers()[2].getEnchantment()}));

                int enchantLevel = ((eo.getCost() /30) * eo.getEnchantment().getMaxLevel());
               // p.sendMessage((double)eo.getCost()/30 + " is the prepare level");
                //p.sendMessage(eo.getEnchantment().getMaxLevel() + " is prepare max level of " + eo.getEnchantment().getName());

                if(enchantLevel<1 || eo.getEnchantment().getMaxLevel() == 1){
                    enchantLevel=1;
                }else if (enchantLevel > eo.getEnchantment().getMaxLevel()){
                    enchantLevel = eo.getEnchantment().getMaxLevel();
                }

                eo.setEnchantmentLevel(enchantLevel);
                e.getEnchanter().updateInventory();
            }
        }
    }

    public Enchantment newChosenEnchantment(ItemStack item, long seed, Enchantment[] eo){
        Enchantment[] notTable = new Enchantment[]{VANISHING_CURSE,BINDING_CURSE,FROST_WALKER,MENDING,SOUL_SPEED,SWIFT_SNEAK};

        ArrayList<Enchantment> notPossibleFromTable = new ArrayList<>(Arrays.asList(notTable));
        Random rand = new Random();
        rand.setSeed(seed);

        int chosen = rand.nextInt(EnchantmentDisablerPlugin.allowedEnchant.size() - 1);

        if(item.getType()== Material.BOOK ||
                (!notPossibleFromTable.contains(EnchantmentDisablerPlugin.allowedEnchant.get(chosen)) && EnchantmentDisablerPlugin.allowedEnchant.get(chosen).canEnchantItem(item))){
            return EnchantmentDisablerPlugin.allowedEnchant.get(chosen);
        }else{
            return newChosenEnchantment(item, seed+1, eo);
        }
    }
}
