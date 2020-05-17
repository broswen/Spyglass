package com.broswen.spyglass.listeners;

import com.broswen.spyglass.utils.RingBuffer;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.reflect.StructureModifier;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class BhopListener extends PacketAdapter {
    Map<Player, SimpleLoc> locationMap;
    Map<Player, RingBuffer> bufferMap;
    public BhopListener(Plugin plugin) {
        super(plugin, ListenerPriority.HIGH, PacketType.Play.Client.POSITION);
        locationMap = new HashMap<>();
        bufferMap = new HashMap<>();
    }

    @Override
    public void onPacketReceiving(PacketEvent event) {
        Player p = event.getPlayer();
        StructureModifier data = event.getPacket().getDoubles();
        SimpleLoc currentLoc = new SimpleLoc(data.getValues(), System.currentTimeMillis());

        if(!bufferMap.containsKey(p)){
            bufferMap.put(p, new RingBuffer(20));
        }

        if(locationMap.containsKey(p)) {
            Double vel = currentLoc.horizontalVelocityFrom(locationMap.get(p));
            bufferMap.get(p).add(vel);
            System.out.println(bufferMap.get(p).average());
        }
        locationMap.put(p, currentLoc);
    }

    private class SimpleLoc{
        private Double x;
        private Double y;
        private Double z;
        private Long millis;

        public SimpleLoc(List<Double> values, Long millis) {
            this.x = values.get(0);
            this.y = values.get(1);
            this.z = values.get(2);
            this.millis = millis;
        }

        //distance
        public Double dist(SimpleLoc loc){
            return Math.abs(Math.sqrt(Math.pow((loc.x - this.x),2) + Math.pow((loc.y - this.y),2) + Math.pow((loc.z - this.z),2)));
        }

        //distance ignoring y values
        public Double horizontalDist(SimpleLoc loc){
            return Math.abs(Math.sqrt(Math.pow((loc.x - this.x),2) + Math.pow((loc.z - this.z),2)));
        }

        //returns millis difference
        public Long timeDist(SimpleLoc loc){
            return this.millis - loc.millis;
        }

        //in meters/second
        public Double velocityFrom(SimpleLoc loc){
            return (dist(loc)/timeDist(loc))*1000;
        }

        //in meters/second. ignores y values
        public Double horizontalVelocityFrom(SimpleLoc loc){
            return (horizontalDist(loc)/timeDist(loc))*1000;
        }
    }
}
