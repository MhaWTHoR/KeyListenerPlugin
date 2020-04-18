package com.mhawthor.keylistener;

import com.mhawthor.keylistener.events.KeyPressEvent;
import com.mhawthor.keylistener.thread.ServerThread;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class MainClass extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        ServerThread thread = new ServerThread();
        thread.start();
        Bukkit.getServer().getPluginManager().registerEvents(this, this);
    }


    @EventHandler
    public void onKeyPress(KeyPressEvent e) {
        if (e.getKey() == 'm') {
            Player player = e.getPlayer();
            player.setVelocity(player.getLocation().getDirection().multiply(2));
        }
    }


}
