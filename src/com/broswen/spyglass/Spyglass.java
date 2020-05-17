package com.broswen.spyglass;

import com.broswen.spyglass.listeners.BhopListener;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import org.bukkit.plugin.java.JavaPlugin;

public class Spyglass extends JavaPlugin {

    private ProtocolManager protocolManager;
    private BhopListener bhopListener;

    @Override
    public void onDisable() {
        protocolManager.removePacketListeners(this);
        protocolManager = null;
    }

    @Override
    public void onEnable() {
        bhopListener = new BhopListener(this);
        protocolManager = ProtocolLibrary.getProtocolManager();

        protocolManager.addPacketListener(bhopListener);
    }
}
