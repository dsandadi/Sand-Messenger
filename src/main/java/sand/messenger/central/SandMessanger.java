/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sand.messenger.central;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import sand.messenger.database.ClientModel;
import sand.messenger.database.ClientPacket;
import sand.messenger.database.ServerPacket;

/**
 *
 * @author DINESH
 */
public class SandMessanger extends Thread {

    private ServerSocket servSocket;
 //   private Database data;
//    private ServerLogic businessService = new ServerLogic();
    private BusinessLogic businessService;
    private static final ServerPacket centralPacket = new ServerPacket();
    private final int remotePort = 3034;

    public SandMessanger(int portNumber) throws IOException {
        servSocket = new ServerSocket(portNumber);
        servSocket.setSoTimeout(30000000);
        TimerTask repeatedTask = new TimerTask(){
            public void run(){
                updateHashMap();
            }
        };
        Timer timer = new Timer();
        long delay  = 1000L;
        long period = 1000L;
        timer.scheduleAtFixedRate(repeatedTask, delay, period);
    }

    public void addToCentralPacket(ClientPacket pack) {
        System.out.println("Adding " + pack.getUserName() + " and " + pack.getIPAddress() + " to Central server packet");
        centralPacket.putCentralMap(pack.getUserName(), pack.getIPAddress());
    }

    /*
    While publishing central server act as a client and
    nodes act as server. Broad cast is the thread that sends 
    the Central Server packet over network.
     */
    public void publish() {
        HashMap<String, String> mapper = centralPacket.getCentralMap();
        Thread[] userThreads = new Thread[mapper.size()];
        int index = 0;
        for (Map.Entry<String, String> entrySet : mapper.entrySet()) {
            //consider that each serverlocal runs on port number 3034.
            
            userThreads[index] = new BroadCast(entrySet.getValue(), centralPacket, remotePort);
            userThreads[index++].start();
            //System.out.println(entrySet.getKey()+":"+entrySet.getValue());
        }
    }
    public void updateHashMap(){
            HashMap<String, String> mapper = centralPacket.getCentralMap();
            for (Map.Entry<String, String> entrySet : mapper.entrySet()) {
            //consider that each serverlocal runs on port number 3034.
            if(!clientAvailabilityCheck(entrySet.getValue(),remotePort)){
                mapper.remove(entrySet.getKey());
            }
            //System.out.println(entrySet.getKey()+":"+entrySet.getValue());
        }
    }

    private static boolean clientAvailabilityCheck(String ServerAddress, int portNumber) {
        try (Socket s = new Socket(ServerAddress, portNumber)) {
            return true;
        } catch (IOException ex) {
            /* ignore */
        }
        return false;
    }

    public void run() {
        while (true) {
            System.out.println("Central/Sand server listening on port" + servSocket.getLocalPort());
            try {
                Socket server = servSocket.accept();
                System.out.println("Connected to .." + server.getRemoteSocketAddress());
                ObjectInputStream in = new ObjectInputStream(server.getInputStream());

                ClientModel m = (ClientModel) in.readObject();
                display(m);
                DataOutputStream out = new DataOutputStream(server.getOutputStream());

                //if (businessService.process(m)) {
                out.writeUTF("1");
                ClientPacket p = (ClientPacket) in.readObject();// Collect the client packet to get identity/IP of client. 
                // after successful login, publish the central server packet, which is the map of ipaddresseses.
                addToCentralPacket(p);
                publish();
                //} 
                //else {
                //  out.writeUTF("0");
                //}
                //After validation
                server.close();
            } catch (IOException ex) {
                Logger.getLogger(SandMessanger.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(SandMessanger.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    private boolean isSigningUp(ClientModel m) {
        return m.isSigningUp();
    }

    private void display(ClientModel m) {
        System.out.println("Server Received :" + m.getName());
        System.out.println("Server received passWord: " + m.getPassWord());
    }
}
