package me.bubbles.bosspve.stages;

import me.bubbles.bosspve.BossPVE;
import me.bubbles.bosspve.entities.manager.IEntityBase;
import me.bubbles.bosspve.ticker.Timer;
import me.bubbles.bosspve.util.UtilLocation;
import me.bubbles.bosspve.util.UtilUserData;
import net.minecraft.world.entity.Entity;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.craftbukkit.v1_20_R1.CraftWorld;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.stream.Collectors;

public class Stage extends Timer {

    public BossPVE plugin;
    private Location pos1;
    private Location pos2;
    private Location spawn;
    private double xpMultiplier;
    private double moneyMultiplier;
    private int maxEntities;
    private HashSet<StageEntity> entityList;
    private HashSet<Entity> spawnedEntities;
    private ConfigurationSection section;
    private boolean valid;
    private final List<String> requiredStageKeys =
            List.of(
                    "spawn",
                    "pos1",
                    "pos2",
                    "xpMultiplier",
                    "moneyMultiplier",
                    "maxEntities"
            );

    private final List<String> requiredEntityKeys =
            List.of(
                    "entity",
                    "pos",
                    "interval"
            );

    public Stage(BossPVE plugin, ConfigurationSection section) {
        super(plugin,section.getInt("killAll"));
        this.plugin=plugin;
        this.section=section;
        for(String key : requiredStageKeys) {
            if(!section.contains(key)) {
                valid=false;
                plugin.getLogger().log(Level.SEVERE,"Could not load stage: " + getLevelRequirement() +" @ "+key);
                return;
            }
        }
        this.entityList=new HashSet<>();
        this.spawnedEntities=new HashSet<>();
        this.spawn=UtilLocation.toLocation(plugin,section.getString("spawn"));
        this.pos1=UtilLocation.toLocation(plugin,section.getString("pos1"));
        this.pos2=UtilLocation.toLocation(plugin,section.getString("pos2"));
        this.xpMultiplier=section.getDouble("xpMultiplier");
        this.moneyMultiplier=section.getDouble("moneyMultiplier");
        this.maxEntities=section.getInt("maxEntities");
        valid=loadEntities();
        if(valid) {
            setEnabled(true);
        }
    }

    @Override
    public void onComplete() {
        killAll();
        restart();
    }

    public void killAll() {
        Iterator<Entity> iterator = spawnedEntities.iterator();
        while(iterator.hasNext()) {
            Entity entity = iterator.next();
            if(entity.isAlive()) {
                entity.kill();
            }
            iterator.remove();
        }
    }

    private boolean loadEntities() {
        ConfigurationSection entities = section.getConfigurationSection("entities");
        if(entities==null) {
            return false;
        }
        boolean result=false;
        for(String entityID : entities.getKeys(false)) {
            String entityKey = entities.getConfigurationSection(entityID).getString("entity");
            IEntityBase entityBase = plugin.getEntityManager().getEntityByName(entityKey);
            if(entityBase==null) {
                plugin.getLogger().log(Level.WARNING, "Could not load entity: "+entityKey+" @ "+getLevelRequirement());
                continue;
            }
            ConfigurationSection entitySection = entities.getConfigurationSection(entityID);
            boolean cont=true;
            for(String key : requiredEntityKeys) {
                if(!entitySection.contains(key)) {
                    plugin.getLogger().log(Level.SEVERE,"Could not load entity: " + entityKey +"."+key+" @ "+key);
                    cont=false;
                }
            }
            if(!cont) {
                result=false;
                continue;
            }
            StageEntity stageEntity = new StageEntity(
                    this,
                    entityBase,
                    UtilLocation.toLocation(plugin,entitySection.getString("pos")),
                    entitySection.getInt("interval")
            );
            entityList.add(stageEntity);
            result=true;
        }
        if(!result) {
            plugin.getLogger().log(Level.SEVERE, "Could not find any entities @ " +getLevelRequirement() + ", stage will not be loaded.");
        }
        return result;
    }

    public Stage getStage() {
        if(valid) {
            return this;
        } else {
            return null;
        }
    }

    public Stage setEnabled(boolean bool) {
        entityList.forEach(stageEntity -> stageEntity.setEnabled(bool));
        if(plugin.getTimerManager().containsTimer(this)==bool) {
            return this;
        }
        if(bool) {
            plugin.getTimerManager().addTimer(this);
        } else {
            plugin.getTimerManager().removeTimer(this);
        }
        return this;
    }

    public int getLevelRequirement() {
        return Integer.parseInt(section.getName());
    }

    public double getXpMultiplier() {
        return xpMultiplier;
    }

    public double getMoneyMultiplier() {
        return moneyMultiplier;
    }

    public boolean isInside(Location location) {

        if(!(location.getWorld().equals(pos1.getWorld())||location.getWorld().equals(pos2.getWorld()))) {
            return false;
        }

        // POS 1
        double x1 = pos1.getX();
        double z1 = pos1.getZ();
        double y1 = pos1.getY();

        // POS 2
        double x2 = pos2.getX();
        double z2 = pos2.getZ();
        double y2 = pos2.getY();

        // WITHIN
        boolean withinX = (location.getX() > Math.min(x1,x2) && (location.getX() < Math.max(x1,x2)));
        boolean withinZ = (location.getZ() > Math.min(z1,z2) && (location.getZ() < Math.max(z1,z2)));
        boolean withinY = (location.getY() > Math.min(y1,y2) && (location.getY() < Math.max(y1,y2)));

        return withinX && withinY && withinZ;

    }

    public boolean isAllowed(Player player) {
        UtilUserData utilUserData = plugin.getMySQL().getData(player.getUniqueId());
        return utilUserData.getLevel()>=getLevelRequirement();
    }

    public Location getSpawn() {
        return spawn;
    }

    public void spawnEntity(Entity entity) {
        spawnedEntities.add(entity);
    }

    public boolean allowSpawn() {
        return spawnedEntities.stream().filter(Entity::isAlive).collect(Collectors.toList()).size()<maxEntities;
    }

    public ConfigurationSection getConfigurationSection() {
        return section;
    }

}