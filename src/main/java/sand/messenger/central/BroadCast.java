/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sand.messenger.central;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import sand.messenger.database.ServerPacket;

/**
 *
 * @author DINESH
 */
public class BroadCast extends Thread {
   private String IPAddress;
   private ServerPacket centralServerPacket;
   private int portNumber;
   public BroadCast(String address,ServerPacket packet,int portNumber){
       IPAddress = address;
       centralServerPacket = packet;     
       this.portNumber = portNumber;
   }
    @Override
    public void run(){
       try {
           Socket client = new Socket(IPAddress, portNumber);
           
           ObjectOutputStream out = new ObjectOutputStream(client.getOutputStream());
           out.writeObject(centralServerPacket);
           
       } catch (IOException ex) {
           Logger.getLogger(BroadCast.class.getName()).log(Level.SEVERE, null, ex);
       }
    
        
        
    }
    
}
