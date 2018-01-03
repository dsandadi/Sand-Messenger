/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sand.messenger.database;

/**
 *
 * @author DINESH
 */
import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Id;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author DINESH
 */
@Entity(name="client")
public class ClientModel implements Serializable {
    @Id
    private String name;
    private int age;
    private String email;
    private String passWord;
    private boolean signingUp;

    public ClientModel(String name, String passWord, boolean signUp) {
        this.name = name;
        this.passWord = passWord;
        this.signingUp = signUp;

    }
    public ClientModel() {
    }

    public ClientModel(String name, int age, String email, String passWord) {
        this.name = name;
        this.age = age;
        this.email = email;
        this.passWord = passWord;
    }

    public boolean isSigningUp() {
        return signingUp;
    }

    public void setSigningUp(boolean signingUp) {
        this.signingUp = signingUp;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPassWord(String pass) {
        this.passWord = pass;
    }
    public String getPassWord(){
        return passWord;
    }
}
