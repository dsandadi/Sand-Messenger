/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sand.messenger.database;

import java.io.Serializable;

/**
 *
 * @author DINESH
 */
public class ClientLocalPacket implements Serializable {
    private String userName;
    private String message;
    public ClientLocalPacket(String userName){
        this.userName = userName;
    }
    public String getUserName(){
        return this.userName;
    }
}
