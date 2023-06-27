package me.bubbles.bosspve.util;

import me.bubbles.bosspve.BossPVE;
import me.bubbles.bosspve.entities.manager.IEntity;
import me.bubbles.bosspve.mysql.MySQL;
import org.bukkit.entity.Player;

public class UtilUser {

    private Player player;
    private BossPVE plugin;

    public UtilUser(BossPVE plugin, Player player) {
        this.player=player;
        this.plugin=plugin;
    }

    public void giveXp(int xp, boolean message) {
        MySQL mySQL = plugin.getMySQL();
        UtilUserData uud = mySQL.getData(player.getUniqueId());
        int level=uud.getLevel();
        uud.setXp(uud.getXp()+xp);
        if(message) {
            if(level!=uud.getLevel()) {
                sendMessage(MessageType.KILL_MESSAGE, "%prefix% %primary%You have level up from %secondary%"+level+"%primary% to %secondary%"+uud.getLevel()+"%primary%.");
            } else {
                sendMessage(MessageType.KILL_MESSAGE, "%prefix% %primary%You have gained %secondary%"+xp+"%primary% XP.");
            }
        }
        mySQL.save(uud);
        plugin.getUserManager().getUser(player.getUniqueId()).update();
    }

    public void giveXp(int xp, IEntity entity) {
        MySQL mySQL = plugin.getMySQL();
        UtilUserData uud = mySQL.getData(player.getUniqueId());
        int level=uud.getLevel();
        uud.setXp(uud.getXp()+xp);
        if(level!=uud.getLevel()) {
            sendMessage(MessageType.KILL_MESSAGE, "%prefix% %primary%You killed a %secondary%"+entity.getUncoloredName()+"%primary% and level up from %secondary%"+level+"%primary% to %secondary%"+uud.getLevel()+"%primary%.");
        } else {
            sendMessage(MessageType.KILL_MESSAGE, "%prefix% %primary%You killed a %secondary%"+entity.getUncoloredName()+"%primary% and gained %secondary%"+xp+"%primary% XP.");
        }
        mySQL.save(uud);
        plugin.getUserManager().getUser(player.getUniqueId()).update();
    }

    public void giveXpAndMoney(int xp, double money, IEntity entity) {
        MySQL mySQL = plugin.getMySQL();
        UtilUserData uud = mySQL.getData(player.getUniqueId());
        int level=uud.getLevel();
        uud.setXp(uud.getXp()+xp);
        if(level!=uud.getLevel()) {
            sendMessage(MessageType.KILL_MESSAGE, "%prefix% %primary%You killed a %secondary%"+entity.getUncoloredName()+"%primary% and gained %secondary%$"+money+
                    " %primary%and leveled up from %secondary%"+level+"%primary% to %secondary%"+uud.getLevel()+"%primary%.");
        } else {
            sendMessage(MessageType.KILL_MESSAGE, "%prefix% %primary%You killed a %secondary%"+entity.getUncoloredName()+"%primary% and gained %secondary%$"+money+"%primary% and %secondary%"+xp+"%primary% XP.");
        }
        mySQL.save(uud);
        plugin.getEconomy().depositPlayer(player,money);
        plugin.getUserManager().getUser(player.getUniqueId()).update();
    }

    public void giveXpAndMoney(int xp, double money, boolean message) {
        MySQL mySQL = plugin.getMySQL();
        UtilUserData uud = mySQL.getData(player.getUniqueId());
        int level=uud.getLevel();
        uud.setXp(uud.getXp()+xp);
        if(message) {
            if(level!=uud.getLevel()) {
                sendMessage(MessageType.KILL_MESSAGE, "%prefix% %primary%You gained %secondary%$"+money+
                        " %primary%and leveled up from %secondary%"+level+"%primary% to %secondary%"+uud.getLevel()+"%primary%.");
            } else {
                sendMessage(MessageType.KILL_MESSAGE, "%prefix% %primary%You gained %secondary%$"+money+"%primary% and %secondary%"+xp+"%primary% XP.");
            }
        }
        mySQL.save(uud);
        plugin.getEconomy().depositPlayer(player,money);
        plugin.getUserManager().getUser(player.getUniqueId()).update();
    }

    public void giveMoney(int money, boolean message) {
        if(message) {
            sendMessage(MessageType.KILL_MESSAGE, "%prefix% %primary%You gained %secondary%$"+money+"%primary%.");
        };
        plugin.getEconomy().depositPlayer(player,money);
        plugin.getUserManager().getUser(player.getUniqueId()).update();
    }

    public void sendMessage(MessageType type, String message) {
        new UtilPlayerMessage(plugin,player).sendMessage(type, message);
    }

}
