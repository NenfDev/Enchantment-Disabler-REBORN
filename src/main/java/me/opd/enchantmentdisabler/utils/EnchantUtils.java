package me.opd.enchantmentdisabler.utils;

import me.opd.enchantmentdisabler.EnchantmentDisablerPlugin;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import static org.bukkit.enchantments.Enchantment.*;
import static org.bukkit.enchantments.Enchantment.SWIFT_SNEAK;

public class EnchantUtils {

    public Enchantment newChosenEnchantment(ItemStack item, long seed){
        Enchantment[] notTable = new Enchantment[]{VANISHING_CURSE,BINDING_CURSE,FROST_WALKER,MENDING,SOUL_SPEED,SWIFT_SNEAK};

        ArrayList<Enchantment> notPossibleFromTable = new ArrayList<>(Arrays.asList(notTable));
        Random rand = new Random();
        rand.setSeed(seed);

        int chosen = rand.nextInt(EnchantmentDisablerPlugin.allowedEnchant.size() - 1);

        if(item.getType()== Material.BOOK || (EnchantmentDisablerPlugin.allowedEnchant.get(chosen).canEnchantItem(item))){
            if(!notPossibleFromTable.contains(EnchantmentDisablerPlugin.allowedEnchant.get(chosen))){
                return EnchantmentDisablerPlugin.allowedEnchant.get(chosen);
            }else{
                return newChosenEnchantment(item, seed+1);
            }
        }else{
            return newChosenEnchantment(item, seed+1);
        }
    }
}
