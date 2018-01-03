/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sand.messenger.central;

import java.util.HashMap;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import sand.messenger.database.ClientModel;

/**
 *
 * @author DINESH
 */
public class BusinessLogic {

    static SessionFactory factory = new Configuration().configure().buildSessionFactory();

    //private ClientModel m;
//    public static void addClient(String name, int age, String email, String passWord) {
//        ClientModel m = new ClientModel(name, age, email, passWord, true);
//        //Database.addClient(m);
//        ClientLocal newClient = new ClientLocal();
//        newClient.setClient(m);
//    }
//
//    public static boolean isValid(String name, String passWord) {
//        if (!(name.isEmpty() && passWord.isEmpty())) {
//            ClientModel m = new ClientModel(name, passWord, false);
//            ClientLocal newClient = new ClientLocal();
//            newClient.setClient(m);
//            return true;
//        }
//
//        return false;
//
//    }

    private static boolean createRecord(ClientModel m) {

        Session session = factory.openSession();
        session.beginTransaction();
        ClientModel model = session.get(ClientModel.class, m.getName());
        if(model != null) return false;
      
        session.save(m);
        session.getTransaction().commit();
        session.close();
        return true;
    }

    private static boolean validateRecord(ClientModel model) {
        Session session = factory.openSession();
        session.beginTransaction();
        ClientModel m = session.get(ClientModel.class, model.getName());
        session.getTransaction().commit();
        session.close();
        return (m != null)&&(m.getPassWord() == model.getPassWord());

    }
    
 public static boolean process (ClientModel m){
    
    if(m.isSigningUp())
    {   
        return createRecord(m);
    }
    else
        return validateRecord(m);
    
    
    }
}
