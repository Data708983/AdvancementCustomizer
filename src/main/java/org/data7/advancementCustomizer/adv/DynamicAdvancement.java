package org.data7.advancementCustomizer.adv;

import com.fren_gor.ultimateAdvancementAPI.advancement.Advancement;
import com.fren_gor.ultimateAdvancementAPI.advancement.BaseAdvancement;
import com.fren_gor.ultimateAdvancementAPI.advancement.display.AdvancementDisplay;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

import java.util.function.Consumer;
import java.util.function.Predicate;

public class DynamicAdvancement extends BaseAdvancement {

    private final Predicate<Event> condition;
    private final Consumer<Player> reward;
    private final Class<? extends Event> eventType;

    public DynamicAdvancement(@NotNull String key,
                              @NotNull AdvancementDisplay display,
                              @NotNull Advancement parent,
                              @Range(from = 1L, to = 2147483647L) int maxProgression,
                              Class<? extends Event> eventType,
                              Predicate<Event> condition,
                              Consumer<Player> reward) {
        super(key, display, parent, maxProgression);

        this.eventType = eventType;
        this.condition = condition;
        this.reward = reward;

        // 动态注册事件
        registerEvent();
    }

    private void registerEvent() {
        // 使用反射来注册不同类型的事件
        if (eventType == BlockBreakEvent.class) {
            registerEvent(BlockBreakEvent.class, e -> {
                Player player = e.getPlayer();
                if (isVisible(player) && condition.test(e)) {
                    incrementProgression(player);
                }
            });
        }
        // 可以添加更多事件类型的处理...
    }

    @Override
    public void giveReward(@NotNull Player player) {
        if (reward != null) {
            reward.accept(player);
        }
    }
}