/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sand.messenger.local;

import UserInterface.ListGUI;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import sand.messenger.database.ServerPacket;

/**
 *
 * @author DINESH
 */
public class ServerCentral extends Thread {

    private final ServerSocket servSocket;
    private ServerPacket serverPacket;
    private final ListGUI localList;

    public ServerCentral(int port, ListGUI l1) throws IOException {
        servSocket = new ServerSocket(port);
        localList = l1;
        servSocket.setSoTimeout(30000);
    }

    public ServerPacket getServerPacket() {
        return serverPacket;
    }

    public void run() {
        while (true) {
            //System.out.println("central server of local machine listening on port number " + servSocket.getLocalPort());
            try {
                Socket connfd = servSocket.accept();
                //System.out.println("Local Server Connected to " + connfd.getRemoteSocketAddress());

                //DataInputStream in = new DataInputStream(connfd.getInputStream());
                ObjectInputStream in = new ObjectInputStream(connfd.getInputStream());

                serverPacket = (ServerPacket) in.readObject();

                localList.setModelofListBox(serverPacket.getCentralMap());
                connfd.close();

            } catch (ClassNotFoundException ex) {
                System.out.println("Class not found exception.\n");
            } catch (IOException ex) {
               // System.out.println("IO Exception caught" + ex);
            }

        }
    }
}
