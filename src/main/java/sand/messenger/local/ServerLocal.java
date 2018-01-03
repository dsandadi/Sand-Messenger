/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sand.messenger.local;

import UserInterface.ChatBox;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import sand.messenger.database.ClientLocalPacket;

/**
 *
 * @author DINESH
 */
public class ServerLocal extends Thread {

    private ServerSocket serverSocket;
    private HashMap<String, ChatBox> chatBoxMapper;

    public ServerLocal(int port, HashMap<String, ChatBox> chatBoxMapper) throws IOException {
        serverSocket = new ServerSocket(port);
        this.chatBoxMapper = chatBoxMapper;
        //serverSocket.setSoTimeout(10000);
    }

    public void run() {
        while (true) {
            try {
                System.out.println("Server Local Waiting for client on port "
                        + serverSocket.getLocalPort() + "...");
                Socket server = serverSocket.accept();

                System.out.println("Server Local Just connected to " + server.getRemoteSocketAddress());

                ObjectInputStream inObject = new ObjectInputStream(server.getInputStream());
                ClientLocalPacket p = (ClientLocalPacket) inObject.readObject();
                String clientUserName = p.getUserName();
                System.out.println("Local Server received client localPacket " + clientUserName);
                if (chatBoxMapper.containsKey(clientUserName)) {
                    continue;
                }
                new ServerTalk(server, chatBoxMapper.get(clientUserName), clientUserName).start();

                //server.close();
            } catch (SocketTimeoutException s) {
                System.out.println("Socket timed out!");
                break;
            } catch (IOException e) {
                e.printStackTrace();
                break;
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(ServerLocal.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }
}
