package com.envyful.combo.persistence.forge.task;

import com.envyful.combo.persistence.forge.ComboPersistenceForge;

public class TimeOutTask implements Runnable {

    @Override
    public void run() {
        ComboPersistenceForge.clearTimeOut();
    }
}
