package com.envyful.combo.persistence.forge;

import com.envyful.api.config.yaml.YamlConfigFactory;
import com.envyful.api.forge.concurrency.ForgeTaskBuilder;
import com.envyful.combo.persistence.forge.config.ComboPersistenceConfig;
import com.envyful.combo.persistence.forge.data.CustomCaptureCombo;
import com.envyful.combo.persistence.forge.task.TimeOutTask;
import com.google.common.collect.Maps;
import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import com.pixelmonmod.pixelmon.storage.PlayerPartyStorage;
import com.pixelmonmod.pixelmon.storage.playerData.CaptureCombo;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Mod(
        modid = "combopersistence",
        name = "Combo Persistence Forge",
        version = ComboPersistenceForge.VERSION,
        acceptableRemoteVersions = "*",
        updateJSON = "https://ogn.pixelmonmod.com/update/sm-rb/update.json"
)
public class ComboPersistenceForge {

    protected static final String VERSION = "1.0.0";

    private static ComboPersistenceForge instance;

    private final Map<UUID, PlayerCombo> storedCombo = Maps.newHashMap();

    private ComboPersistenceConfig config;

    @Mod.EventHandler
    public void onInit(FMLInitializationEvent event) {
        instance = this;
        this.reloadConfig();

        new ForgeTaskBuilder()
                .async(true)
                .task(new TimeOutTask())
                .delay(10L)
                .interval(20L)
                .start();
    }

    public void reloadConfig() {
        try {
            this.config = YamlConfigFactory.getInstance(ComboPersistenceConfig.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static ComboPersistenceForge getInstance() {
        return instance;
    }

    public static void storeCombo(EntityPlayerMP player) {
        PlayerPartyStorage party = Pixelmon.storageManager.getParty(player.getUniqueID());
        CaptureCombo captureCombo = party.transientData.captureCombo;

        if (captureCombo == null) {
            return;
        }

        getInstance().storedCombo.put(player.getUniqueID(), new PlayerCombo(player.getUniqueID(), captureCombo.getCurrentSpecies(), captureCombo.getCurrentCombo(), System.currentTimeMillis()));
    }

    public static PlayerCombo getCombo(EntityPlayerMP player) {
        return getInstance().storedCombo.get(player.getUniqueID());
    }

    public static void clearTimeOut() {
        getInstance().storedCombo.entrySet().removeIf(entry -> entry.getValue().hasTimedOut());
    }

    public static class PlayerCombo {

        private final UUID uuid;
        private final EnumSpecies species;
        private final int combo;
        private final long logout;

        private PlayerCombo(UUID uuid, EnumSpecies species, int combo, long logout) {
            this.uuid = uuid;
            this.species = species;
            this.combo = combo;
            this.logout = logout;
        }

        public boolean hasTimedOut() {
            return (System.currentTimeMillis() - this.logout) >= TimeUnit.SECONDS.toMillis(ComboPersistenceForge.getInstance().config.getTimeoutSeconds());
        }

        public void restoreCombo() {
            PlayerPartyStorage party = Pixelmon.storageManager.getParty(this.uuid);
            party.transientData.captureCombo = new CustomCaptureCombo(this.species, this.combo);
        }
    }
}
