/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sand.messenger.database;

import java.io.Serializable;
import java.util.HashMap;

/**
 *
 * @author DINESH
 */
public class ServerPacket implements Serializable {
      private final HashMap<String,String> centralMap = new HashMap<>(); 
   
      public synchronized void putCentralMap(String userName,String IPAddress){
          centralMap.put(userName,IPAddress);
      }
      public void deleteFromCentralMap(String userName){
          centralMap.remove(userName);
      }
      public HashMap<String,String> getCentralMap(){
          return centralMap;
      }
}
