package com.esselbr;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Main {

    volatile Map<String, Battlebot> Battlebots = new HashMap<>();
    private String Listpath;

    public static void main(String[] args) {
        new Main();
    }

    public Main(){
        Listpath = getClass().getProtectionDomain().getCodeSource().getLocation().toString();
        Listpath = Listpath.substring(Listpath.indexOf('/'),Listpath.lastIndexOf('/')) + "/BattleBotList";

        TcpSocket server = new TcpSocket(1337, Battlebots);
        new Thread(server).start();

        System.out.println("BattleBotServer version 1.0");

        loadBots();

        System.out.println((char)27 + "[32m" + Battlebots.size() + " Battlebot(s) found in list, trying to communicate" + (char)27 + "[39m");

        System.out.println("For a list of available commands type 'help'");
        while(true){
            Scanner scanner = new Scanner(System.in);
            String[] cmd = scanner.nextLine().split("\\s+");

            if(cmd[0].equals("help")){
                System.out.println("    add <MAC> - Add a Battleboi to the list");
                System.out.println("    remove <MAC> - Remove a Battleboi from the list");
                System.out.println("    list - Show list of bots");
                System.out.println("    lookatme - Take control of this ship, you are the captain now");
                System.out.println("    cmd <MAC> <command> - Sent command to battlebot");
                System.out.println("    reset <MAC> - Restart connection to Battlebot when not responding");
            }
            try {
                if (cmd[0].equals("add")) {
                    addBot(cmd[1]);
                }

                if (cmd[0].equals("remove")) {
                    removeBot(cmd[1]);
                }

                if (cmd[0].equals("reset")) {
                    restartBotConnection(cmd[1]);
                }

                if (cmd[0].equals("cmd")) {
                    Battlebots.get(cmd[1]).sendCommand(cmd[2]);
                }

                if (cmd[0].equals("lookatme")) {
                    System.out.println("Goodbye captain");
                    System.exit(0);
                }

                if (cmd[0].equals("list")) {
                    showBotList();
                }
            }catch (ArrayIndexOutOfBoundsException e){
                System.out.println((char)27 + "[31m" + "Command not recognized" + (char)27 + "[39m");
            }
        }

    }

    private void restartBotConnection(String macadd){
        System.out.println("Dropping connection with " + macadd);
        Battlebots.get(macadd).resetConnection();
    }

    private void showBotList(){
        for(Map.Entry<String, Battlebot> entry : Battlebots.entrySet()) {
            System.out.print("  "+entry.getValue().getMacAdress());
            if(entry.getValue().getConnectionStatus()){
                System.out.println((char)27 + "[32m" + " Connected" + (char)27 + "[39m");
            }else{
                System.out.println((char)27 + "[31m" + " Disconnected" + (char)27 + "[39m");
            }
        }
    }

    private void loadBots(){
        try (BufferedReader br = new BufferedReader(new FileReader(Listpath))) {
            String line;
            while ((line = br.readLine()) != null) {
                Battlebots.put( line, new Battlebot(line) );
            }
        }catch (Exception e){
        }
    }

    private void removeBot(String macadd){
        Battlebots.remove(macadd);
        try {
            PrintWriter writer = new PrintWriter(Listpath, "UTF-8");

            for(Map.Entry<String, Battlebot> entry : Battlebots.entrySet()) {
                writer.println(entry.getValue().getMacAdress());
            }

            writer.close();
            System.out.println("Bot list updated");
        }catch (Exception e){
            System.out.println(e);
        }
    }

    private void addBot(String macadd){
        Battlebots.put( macadd, new Battlebot(macadd) );
        try {
            PrintWriter writer = new PrintWriter(Listpath, "UTF-8");

            for(Map.Entry<String, Battlebot> entry : Battlebots.entrySet()) {
                writer.println(entry.getValue().getMacAdress());
            }

            writer.close();
            System.out.println("Bot list updated");
        }catch (Exception e){
            System.out.println(e);
        }
    }
}
