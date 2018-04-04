package com.esselbr;

import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;
import java.io.IOException;
import java.io.OutputStream;

public class Battlebot{

    private OutputStream bluetoothStream;
    private String MACAddr;
    private String url;
    private boolean connected = false;

    public Battlebot(String macadd){
        MACAddr = macadd;
        url = "btspp://"+MACAddr+":1;authenticate=false;encrypt=false;master=false";

        connect();
    }

    private void connect(){
        connected = false;
        new Thread() {
            public void run() {
                while(bluetoothStream == null) {
                    try {
                        StreamConnection streamConnection = (StreamConnection)
                                Connector.open(url);
                        bluetoothStream = streamConnection.openOutputStream();
                        System.out.println((char)27 + "[32m" + "Connected to bot " + MACAddr + (char)27 + "[39m");
                        connected = true;
                    } catch (Exception e) {

                    }
                }
            }
        }.start();
    }

    public void resetConnection(){
        try {
            connected = false;
            bluetoothStream.close();
            bluetoothStream = null;
            connect();
        }catch (Exception e){

        }
    }

    public void sendCommand(String command){
        new Thread() {
            public void run() {
                try {
                    bluetoothStream.write(command.getBytes());
                } catch (IOException e) {
                    System.out.println("Command not sent " + MACAddr);
                }
            }
        }.start();
    }

    public boolean getConnectionStatus(){
        return connected;
    }

    public String getMacAdress(){
        return MACAddr;
    }
}
