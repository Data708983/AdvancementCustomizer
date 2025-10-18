package org.data7.advancementCustomizer;

import com.fren_gor.ultimateAdvancementAPI.AdvancementTab;
import com.fren_gor.ultimateAdvancementAPI.UltimateAdvancementAPI;
import com.fren_gor.ultimateAdvancementAPI.advancement.RootAdvancement;
import com.fren_gor.ultimateAdvancementAPI.advancement.display.AdvancementDisplay;
import com.fren_gor.ultimateAdvancementAPI.advancement.display.AdvancementFrameType;
import com.fren_gor.ultimateAdvancementAPI.events.PlayerLoadingCompletedEvent;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.Event;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.data7.advancementCustomizer.adv.DynamicAdvancement;

public final class AdvancementCustomizer extends JavaPlugin{

    private AdvancementTab advancementTab;
    private RootAdvancement root;
    private UltimateAdvancementAPI api;

    @Override
    public void onEnable() {
        // Plugin startup logic

        api = UltimateAdvancementAPI.getInstance(this);

        advancementTab = api.createAdvancementTab("your_tab_name");

        AdvancementDisplay rootDisplay = new AdvancementDisplay(Material.GRASS_BLOCK, "Example root", AdvancementFrameType.TASK, true, true, 0, 0, "description");

        AdvancementDisplay Display = new AdvancementDisplay(Material.STONE, "Example one", AdvancementFrameType.TASK, true, true, 1, 0, "description");

        root = new RootAdvancement(advancementTab, "root", rootDisplay, "textures/block/stone.png");
        // 创建动态成就
        DynamicAdvancement mineAdv = new DynamicAdvancement(
                "mine_stone",
                Display,
                root,
                10,
                BlockBreakEvent.class,
                // 外部传入的条件
                event -> {
                    BlockBreakEvent e = (BlockBreakEvent) event;
                    return e.getBlock().getType() == Material.STONE;
                },
                // 外部传入的奖励
                player -> {
                    player.getInventory().addItem(new ItemStack(Material.STONE_BRICKS, 5));
                    player.sendMessage("恭喜你完成了挖矿成就!");
                }
        );

        advancementTab.registerAdvancements(root, mineAdv);

        advancementTab.getEventManager().register(advancementTab, PlayerLoadingCompletedEvent.class, event -> {
            advancementTab.showTab(event.getPlayer());
            advancementTab.grantRootAdvancement(event.getPlayer());
        });
    }

        @Override
        public void onDisable () {
            // Plugin shutdown logic
        }
    }
