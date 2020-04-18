package com.mhawthor.keylistener.thread;

import com.mhawthor.keylistener.events.KeyPressEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

public class ServerThread extends Thread {


    @Override
    public void run() {
        ServerSocket serverSocket = null;

        try {

            serverSocket = new ServerSocket(1231);
            serverSocket.setSoTimeout(Integer.MAX_VALUE);
        } catch (IOException e) {
            e.printStackTrace();
        }
        while (true) {
    try {
        Socket server = serverSocket.accept(); //waits the client to connect
        DataInputStream in = new DataInputStream(server.getInputStream());
        String input =in.readUTF();
        String[] split = input.split(":");
        Player player = Bukkit.getPlayer(split[1]);
        char character = split[0].charAt(0);
        KeyPressEvent event = new KeyPressEvent(player,character);
        Bukkit.getPluginManager().callEvent(event);
        server.close();

    } catch (SocketTimeoutException s) {
        System.out.println("Socket timed out!");
        break;
    } catch (IOException e) {
        e.printStackTrace();
        break;
    }
}

    }
}
