package com.mhawthor.keylistener.thread;

import com.mhawthor.keylistener.events.KeyPressEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.DatagramChannel;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import static com.mhawthor.keylistener.MainClass.socketMap;

public class ServerThread extends Thread {
    public void test() {
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

                String input = in.readUTF();
                String[] split = input.split(":");
                Player player = Bukkit.getPlayer(split[1]);
                socketMap.put(player,server);
                char character = split[0].charAt(0);
                KeyPressEvent event = new KeyPressEvent(player, character);
                Bukkit.getPluginManager().callEvent(event);

            } catch (SocketTimeoutException s) {
                System.out.println("Socket timed out!");
                break;
            } catch (IOException e) {
                e.printStackTrace();
                break;
            }
        }

    }
    public void test2() {
        try {
            AsynchronousServerSocketChannel server =
                    AsynchronousServerSocketChannel.open();
            server.bind(new InetSocketAddress("localhost", 1231));
            Future<AsynchronousSocketChannel> acceptCon = server.accept();
            AsynchronousSocketChannel client = acceptCon.get(2,
                    TimeUnit.DAYS);
            if (client.isOpen() && client != null) {
                while (true) {
                    ByteBuffer buffer = ByteBuffer.allocate(256); //32 karakterlik string yollanacak max, bizim için yeterli
                    Future<Integer> readval = client.read(buffer);
                    String data = new String(buffer.array()).trim();
                    System.out.println(data + "asdasdasd");
                    String[] split = data.split(":"); //Örnek format m:MhaWTHoR yani:basilan_tus:nickname
                    Player player = Bukkit.getPlayer(split[1]);
                    char character = split[0].charAt(0);
                    KeyPressEvent event = new KeyPressEvent(player, character);
                    Bukkit.getPluginManager().callEvent(event);
                    readval.get();
                    buffer.flip();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            DatagramChannel server =DatagramChannel.open();
            InetSocketAddress sAddr = new InetSocketAddress( 1231);
            server.bind(sAddr);
            System.out.println("dinleniyor.");
            ByteBuffer buffer = ByteBuffer.allocate(256);
            while (true) {
                SocketAddress remoteAddr = server.receive(buffer);
                System.out.println("dinleniyor.");
                buffer.flip();
                int limits = buffer.limit();
                byte bytes[] = new byte[limits];
                buffer.get(bytes, 0, limits);
                String msg = new String(bytes);
                String[] split = msg.split(":"); //Örnek format m:MhaWTHoR yani:basilan_tus:nickname
                Player player = Bukkit.getPlayer(split[1]);
                char character = split[0].charAt(0);
                KeyPressEvent event = new KeyPressEvent(player, character);
                Bukkit.getPluginManager().callEvent(event);
                buffer.rewind();
                server.send(buffer, remoteAddr);
                buffer.clear();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
