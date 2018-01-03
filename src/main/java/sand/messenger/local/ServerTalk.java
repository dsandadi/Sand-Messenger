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
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author DINESH
 */
public class ServerTalk extends Thread {

    private Socket clientSocket;
    private ChatBox chatBox;
    private String userName;

    public ServerTalk(Socket servSocket, ChatBox chatBox, String userName) {
        clientSocket = servSocket;
        this.chatBox = chatBox;
        this.userName = userName;
    }

    public void run() {
        while (true) {
            try (
                    PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                    BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));) {
                String inputLine, outputLine;
                //Action listener for sendbutton.
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
                // Initiate conversation with client
                while ((inputLine = in.readLine()) != null) {
                    System.out.println(inputLine);
                    out.println(inputLine);
                }
                clientSocket.close();
            } catch (IOException e) {
                System.out.println("Exception caught when trying to listen on port or listening for a connection");
                System.out.println(e.getMessage());
            }
        }
    }
}
