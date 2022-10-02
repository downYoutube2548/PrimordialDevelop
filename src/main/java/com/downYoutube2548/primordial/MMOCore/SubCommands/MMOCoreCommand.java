package com.downYoutube2548.primordial.MMOCore.SubCommands;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.BlockPosition;
import com.comphenix.protocol.wrappers.WrappedBlockData;
import com.develop.primordial.primordialdevelop.CommandManager.SubCommand;
import com.develop.primordial.primordialdevelop.PrimordialDevelop;
import net.Indyuce.mmocore.MMOCore;
import net.Indyuce.mmocore.api.player.PlayerData;
import net.Indyuce.mmocore.api.player.profess.PlayerClass;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MMOCoreCommand extends SubCommand {
    private final PrimordialDevelop main;

    public MMOCoreCommand(PrimordialDevelop main) {
        super("mmocore", "primordial.mmocore");
        this.main = main;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        /*
        if (args[1].equals("a")) {
            TypeManager typeManager = MMOItems.plugin.getTypes();

            Type type = new Type(TypeSet.EXTRA, "ITEM_CURRENCY", false, EquipmentSlot.OTHER);
            typeManager.register(type);
            MMOItems.plugin.getTemplates().reload();

            try {
                type.load(PrimordialDevelop.configManager.getConfigurationSection("ItemType.ITEM_CURRENCY"));
            } catch (IllegalArgumentException var6) {
            }

            type.getAvailableStats().clear();

            String[] statList = {"MATERIAL", "ITEM_DAMAGE", "CUSTOM_MODEL_DATA", "MAX_DURABILITY", "NAME", "LORE", "CUSTOM_NBT", "ENCHANTS", "HIDE_ENCHANTS", "UNBREAKABLE", "TIER", "SKULL_TEXTURE", "DYE_COLOR", "HIDE_DYE", "POTION_EFFECT", "POTION_COLOR", "SHIELD_PATTERN", "HIDE_POTION_EFFECTS", "DURABILITY", "LORE_FORMAT"};

            MMOItems.plugin.getStats().getAll().stream().filter((stat) -> { return stat.isCompatible(type) && Arrays.stream(statList).toList().contains(stat.getId()); }).forEach((stat) -> { type.getAvailableStats().add(stat); });



        if (args[1].equals("test")) {

            PlayerData mmoPlayer = PlayerData.get(player);
            mmoPlayer.setClass(MMOCore.plugin.classManager.get(args[2]));

        } else if (args[1].equals("beam")) {
            PacketContainer block_change = PrimordialDevelop.protocolManager.
                    createPacket(PacketType.Play.Server.BLOCK_CHANGE);
            PacketContainer block_change2 = PrimordialDevelop.protocolManager.
                    createPacket(PacketType.Play.Server.MULTI_BLOCK_CHANGE);

            Location loc = player.getLocation();
            block_change.getBlockPositionModifier().
                    write(0, new BlockPosition(loc.getBlockX(), 100, loc.getBlockZ()));
            block_change.getBlockData().write(0, WrappedBlockData.createData(Material.END_GATEWAY));
            block_change2.getBlockPositionModifier().
                    write(0, new BlockPosition(loc.getBlockX(), 100, loc.getBlockZ()));
            block_change2.getBlockData().write(0, WrappedBlockData.createData(Material.AIR));



            Bukkit.getScheduler().runTaskTimerAsynchronously(main, () -> {
                try {

                    PrimordialDevelop.protocolManager.sendServerPacket(player, block_change2);
                    PrimordialDevelop.protocolManager.sendServerPacket(player, block_change);
                } catch (InvocationTargetException e) {
                }
            }, 0, 20);
        } else if (args[1].equals("class")) {
            PlayerClass playerClass = MMOCore.plugin.classManager.get("EMPERORI");
            PlayerData playerData = PlayerData.get(((Player) sender).getUniqueId());
            Bukkit.broadcastMessage(String.valueOf(playerClass.getAttributeDescription().size()));
            for (String s : playerClass.getAttributeDescription()) {
                Bukkit.broadcastMessage(s);
            }
        }
        
         */
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, String[] args) {
        List<String> out = new ArrayList<>();
        if (args.length == 2) {
            for (String s : new String[]{"beam", "class", "test"}) {
                if (s.toLowerCase(Locale.ROOT).startsWith(args[1])) {
                    out.add(s);
                }
            }
        }
        return out;
    }


}
