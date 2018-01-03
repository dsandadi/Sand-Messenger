/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sand.messenger.local;

import UserInterface.ListGUI;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import sand.messenger.database.ClientModel;
import sand.messenger.database.ClientPacket;


/**
 *
 * @author DINESH
 */
public class ClientCetral {

    private ClientModel client;
    private boolean validClient;

    public ClientModel getClient() {
        return client;
    }

    public void setClient(ClientModel client) {
        this.client = client;
    }

    public boolean isValidClient() {
        return validClient;
    }

    public void setValidClient(boolean validClient) {
        this.validClient = validClient;
    }

    public void setClientModel(ClientModel client) {
        this.client = client;
    }

    protected void display() {
        System.out.println("Name :" + client.getName() + "\nType" + client.isSigningUp());
    }

    public void initializeClient(String servAddress, int portNumb,ListGUI listBox) throws IOException, ClassNotFoundException {
        System.out.println("Initializing ..");
      //  display();
        String serverName = servAddress;
        int portNumber = portNumb;
        Socket client = new Socket(serverName, portNumber);
        System.out.println("Connected to server" + client.getRemoteSocketAddress());

        //comment it out
        //DataOutputStream dataOut = new DataOutputStream(client.getOutputStream());
        //dataOut.writeUTF("Hello Server..");
        //
        ObjectOutputStream out = new ObjectOutputStream(client.getOutputStream());
        out.writeObject(this.client);

        DataInputStream in = new DataInputStream(client.getInputStream());
        String val = in.readUTF();
        //System.out.println("Server says:" + val);

        if (val.equals("1")) {
            System.out.println("Client: Validation successful.");
            validClient = true;
            // if valid client send clientpacket.
            System.out.println("Client local address "+ client.getLocalAddress().toString());
            // remove the preceding / character in ip addresss, by using the substring method.
            ClientPacket p = new ClientPacket(this.client.getName(), client.getLocalAddress().toString().substring(1));
            out.writeObject(p);
            listBox.setVisible(true);
            //ObjectInputStream oIn = new ObjectInputStream(client.getInputStream());
            
        }
    }

}
