/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sand.messenger.central;

import java.io.IOException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

/**
 *
 * @author DINESH
 */
public class Driver {
    
    //static SessionFactory factory= new Configuration().configure().buildSessionFactory();

     public static void main(String[] args) throws IOException {
        // TODO code application logic here
      Thread t1 = new SandMessanger(3434);
       t1.start();
     }
     
}