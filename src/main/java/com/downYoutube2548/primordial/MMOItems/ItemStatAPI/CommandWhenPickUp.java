package com.downYoutube2548.primordial.MMOItems.ItemStatAPI;

import io.lumine.mythic.lib.api.item.ItemTag;
import net.Indyuce.mmoitems.api.item.build.ItemStackBuilder;
import net.Indyuce.mmoitems.api.item.mmoitem.ReadMMOItem;
import net.Indyuce.mmoitems.gui.edition.EditionInventory;
import net.Indyuce.mmoitems.stat.data.random.RandomStatData;
import net.Indyuce.mmoitems.stat.data.type.StatData;
import net.Indyuce.mmoitems.stat.type.ItemStat;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CommandWhenPickUp extends ItemStat {

    public CommandWhenPickUp() {
        super("COMMAND_WHEN_PICKUP", Material.COMMAND_BLOCK_MINECART, "Command When PickUp", new String[]{"lore1", "lore2"}, new String[]{"all"});
    }

    @Override
    public RandomStatData whenInitialized(Object o) {
        return null;
    }

    @Override
    public void whenApplied(@NotNull ItemStackBuilder itemStackBuilder, @NotNull StatData statData) {

    }

    @NotNull
    @Override
    public ArrayList<ItemTag> getAppliedNBT(@NotNull StatData statData) {
        return null;
    }

    @Override
    public void whenClicked(@NotNull EditionInventory inv, @NotNull InventoryClickEvent event) {
        inv.open();
    }

    @Override
    public void whenInput(@NotNull EditionInventory editionInventory, @NotNull String s, Object... objects) {

    }

    @Override
    public void whenLoaded(@NotNull ReadMMOItem readMMOItem) {

    }

    @Nullable
    @Override
    public StatData getLoadedNBT(@NotNull ArrayList<ItemTag> arrayList) {
        return null;
    }

    @Override
    public void whenDisplayed(List<String> list, Optional<RandomStatData> optional) {

    }

    @NotNull
    @Override
    public StatData getClearStatData() {
        return null;
    }
}
