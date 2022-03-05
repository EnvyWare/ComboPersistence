package com.envyful.combo.persistence.forge.config;

import com.envyful.api.config.data.ConfigPath;
import com.envyful.api.config.yaml.AbstractYamlConfig;
import info.pixelmon.repack.ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;

@ConfigPath("config/ComboPersistence/config.yml")
@ConfigSerializable
public class ComboPersistenceConfig extends AbstractYamlConfig {

    private long timeoutSeconds = 100;

    public ComboPersistenceConfig() {
        super();
    }

    public long getTimeoutSeconds() {
        return this.timeoutSeconds;
    }
}
