package com.envyful.combo.persistence.forge.listener;

import com.envyful.api.forge.listener.LazyListener;
import com.envyful.combo.persistence.forge.ComboPersistenceForge;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

public class PlayerQuitListener extends LazyListener {

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onPlayerQuit(PlayerEvent.PlayerLoggedOutEvent event) {
        ComboPersistenceForge.storeCombo((EntityPlayerMP) event.player);
    }
}
