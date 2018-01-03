/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sand.messenger.local;

import UserInterface.ChatBox;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import sand.messenger.database.ClientLocalPacket;

/**
 *
 * @author DINESH
 */
public class ClientLocal extends Thread {

    private ChatBox chatBox;// Chat Box to add the contents.
    private String hostIPAddress;
    private int portNumber;
    String userName;

    public ClientLocal(ChatBox chatBox, String hostAddr, int portNumber, String userName) {
        this.chatBox = chatBox;
        this.hostIPAddress = hostAddr;
        this.portNumber = portNumber;
        this.userName = userName;
    }

    public void run() {
        try {
            System.out.println("Initializing new client Local socket");
            Socket clientSocket = new Socket(hostIPAddress, portNumber);
            ObjectOutputStream outObject = new ObjectOutputStream(clientSocket.getOutputStream());
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(clientSocket.getInputStream()));

            String fromServer;
            //Send the clientlocal packet telling the identity of client to the server.
            System.out.println("LOCAL CLIENT: sent client local packet " + userName);
            outObject.writeObject(new ClientLocalPacket(userName));
           System.out.println("Local client sent the clientlocal packet");
            chatBox.getSendButton().addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    String msg = chatBox.getMessage_field().getText();
                    out.println(msg);
//                    try {
//                        out.writeObject(new ClientLocalPacket(userName, msg));
//                    } catch (IOException ex) {
//                        System.out.println("Can't write clientPacket Object IOException." + ex);
//                    }
                }
            });
            while ((fromServer = in.readLine()) != null) {
                chatBox.getMessage_Display_Area().append("\n Server: " + fromServer);
            }
        } catch (IOException ex) {
            System.out.println("Can't create locl client socket connecting to portnumber" + portNumber);
        }

    }

}
