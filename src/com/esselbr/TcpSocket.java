package com.esselbr;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import org.json.*;

import java.util.Map;

public class TcpSocket implements Runnable {

    protected int serverPort;
    protected ServerSocket serverSocket = null;
    protected boolean isStopped = false;
    protected Thread runningThread = null;
    volatile Map<String, Battlebot> bots;

    public TcpSocket(int port, Map<String, Battlebot> bots) {
        this.bots = bots;
        this.serverPort = port;
        System.out.println("TCP Server started");
    }

    public void run() {
        synchronized (this) {
            this.runningThread = Thread.currentThread();
        }
        openServerSocket();
        while (!isStopped()) {
            final Socket clientSocket;

            try {
                clientSocket = this.serverSocket.accept();
                //System.out.println(clientSocket.getRemoteSocketAddress().toString() + " connected");

                (new Thread() {
                    public void run() {
                       waitForCommand(clientSocket);
                    }
                }).start();

            } catch (IOException e) {
                if (isStopped()) {
                    System.out.println("Server Stopped.");
                    return;
                }
                throw new RuntimeException("Error accepting client connection", e);
            }

        }
        System.out.println("Server Stopped.");
    }

    private void waitForCommand(Socket socket){
        (new Thread() {
            public void run() {
                try{
                    BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    OutputStream output = socket.getOutputStream();

                    String command;
                    command = in.readLine();

                    if(command.equals("getConnectedBots")){
                        JSONArray json = new JSONArray();
                        for(Map.Entry<String, Battlebot> entry : bots.entrySet()) {

                            if(entry.getValue().getConnectionStatus()) {
                                json.put(entry.getValue().getMacAdress());
                            }

                        }
                        output.write((json.toString()).getBytes());

                    }else{
                        String[] cmd = command.split(",");
                        if(cmd[0]!=null && cmd[1]!= null){
                            Battlebot bot = bots.get(cmd[0]);
                            if(bot != null) {
                                if (!bot.getConnectionStatus()) {
                                    output.write(("Error: BattleBot " + cmd[0] + " not connected").getBytes());
                                } else {
                                    bot.sendCommand(cmd[1]);
                                    output.write(("OK").getBytes());
                                }
                            }else{
                                output.write(("Error: BattleBot does not exist").getBytes());
                            }
                        }
                    }

                    in.close();
                    output.close();

                }catch (Exception e){

                }
            }
        }).start();
    }

    private synchronized boolean isStopped() {
        return this.isStopped;
    }

    public synchronized void stop() {
        this.isStopped = true;
        try {
            this.serverSocket.close();
        } catch (IOException e) {
            throw new RuntimeException("Error closing server", e);
        }
    }

    private void openServerSocket() {
        try {
            this.serverSocket = new ServerSocket(this.serverPort);
        } catch (IOException e) {
            throw new RuntimeException("Cannot open port", e);
        }
    }

}
