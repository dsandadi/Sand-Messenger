/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sand.messenger.local;

import UserInterface.ChatBox;
import UserInterface.SignUpGUI;
import UserInterface.SignInGUI;
import UserInterface.ListGUI;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;
import sand.messenger.database.ClientModel;

/**
 *
 * @author DINESH
 */
public class DriverLocal {

    private boolean asClient = false;
    private boolean asServer = false;
    private ServerCentral server;
    private ClientCetral client = new ClientCetral();
    //private ClientGUILocalSignIn signIn;
    private final static int portNumber = 3434;
    private static String servAddr = "127.0.0.1";
    private ClientModel model;
    private SignInGUI signIn;
    private SignUpGUI signUp;
    private ListGUI listBox;
    // private ChatBox[] chatBox = new ChatBox[30];
    private final HashMap<String, ChatBox> chatBoxMapper = new HashMap<>();
    private int chatBoxCount = 0;
    // reference to the local server.
    private ServerLocal localServer;

    public ServerCentral getServer() {
        return server;
    }

    public void setServer(ServerCentral server) {
        this.server = server;
    }

    public ClientCetral getClient() {
        return client;
    }

    public void setClient(ClientCetral client) {
        this.client = client;
    }

    public boolean getAsClient() {
        return asClient;
    }

    public void setAsClient(boolean asClient) {
        this.asClient = asClient;
    }

    public boolean getAsServer() {
        return asServer;
    }

    public void setAsServer(boolean asServer) {
        this.asServer = asServer;
    }

    public DriverLocal() {

    }

//    public void clientDriver(ClientCetral c) throws IOException, InterruptedException
//    {
//        driveAsClient(c,DriverLocal.portNumber, "127.0.0.1");
//    }
    private void signUpActionPerformed(java.awt.event.ActionEvent evt, SignUpGUI signUp) {
        // TODO add your handling code here:
        JTextField name = signUp.getTextName();
        JTextField age = signUp.getAge();
        JTextField email = signUp.getEmail();
        JPasswordField passWord = signUp.getPassWord();

        String tempName = name.getText();
        String tempAge = age.getText();
        String tempEmail = email.getText();
        String tempPass = new String(passWord.getPassword());

        if (isValid(name.getText(), age.getText(), email.getText(), new String(passWord.getPassword()))) {
            this.createModel(name.getText(), Integer.parseInt(age.getText()),
                    email.getText(), new String(passWord.getPassword()));
        }

        name.setText("");
        age.setText("");
        email.setText("");
        passWord.setText("");

    }

    private void signInActionPerformed(java.awt.event.ActionEvent evt, SignInGUI signIn) {
        // TODO add your handling code here:
        JTextField userName = signIn.getUserName();
        JPasswordField passWord = signIn.getPassWord();
        String user = userName.getText();
        String pass = new String(passWord.getPassword());
        if (!isValid(user, pass)) {
            userName.setForeground(Color.red);
            userName.setText("Invalid Entry");
        } else {

            model = new ClientModel();
            //client.setClientModel(model);
            model.setName(user);
            model.setPassWord(pass);
            System.out.println("SET UserName !!!");

            // this.setVisible(false);
        }
        System.out.println("SucessFully ");

    }

    private boolean isValid(String name, String passWord) {
        return (!(name.isEmpty() && passWord.isEmpty()));
    }

    private boolean isValid(String name, String passWord, String email, String age) {
        return (!(name.isEmpty() && (age.isEmpty()) && email.isEmpty() && passWord.isEmpty()));
    }

    private void createModel(String name, int age, String email, String passWord) {

        model = new ClientModel(name, age, email, passWord);
        model.setSigningUp(true);
        //Database.addClient(m);
    }

    public void driveAsClient() throws IOException, InterruptedException {
        //ClientGUILocalSignIn l = new ClientGUILocalSignIn();

        signIn = new SignInGUI();
        //SignUpGUI signup;
        signIn.setVisible(true);
        signIn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                signInActionPerformed(e, signIn);
                try {
                    initializeClient();
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(DriverLocal.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        signIn.addSignUpActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                signIn.setVisible(false);
                signUp = new SignUpGUI();
                signUp.setVisible(true);
                signUp.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        signUpActionPerformed(e, signUp);
                        try {
                            initializeClient();
                        } catch (ClassNotFoundException ex) {
                            Logger.getLogger(DriverLocal.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                });

                // singUpGUI.addActionListener();
                //
                //initializeClient();
            }

        }
        );

        // while(!clientFrame.isValid){}      
        System.out.println("Name" + model.getName());
        //c.initializeClient(this.servAddr, this.portNumber);

    }
//    private void displayInListBox(ServerPacket p){
//        HashMap<String,String> mapper = p.getCentralMap();
//        ListGUI l1 = new ListGUI(mapper);
//        l1.setVisible(true);
//    }

    private void listNamesValueChanged(ListSelectionEvent evt, ListGUI listBox, ServerCentral server) {
        System.out.println("Selection Event called.");
        HashMap<String, String> mapper = server.getServerPacket().getCentralMap();
        String userName = listBox.getListNames().getSelectedValue();
        if (userName == null) {
            return;
        }
        listBox.getListNames().clearSelection();
        String IPAddress = mapper.get(userName);
        // if a value from list box is selected run client instance on local machine.
        ChatBox currentChatBox;

        if (!chatBoxMapper.containsKey(userName)) {
            currentChatBox = new ChatBox("SandMessenger Client [" + userName + "]");
            //currentChatBox  = chatBox[chatBoxCount];
            chatBoxMapper.put(userName, currentChatBox);
        } else {
            currentChatBox = chatBoxMapper.get(userName);
        }
        // Initializing the client.Local server instance runs port number 4434.
        ClientLocal localClient = new ClientLocal(currentChatBox, mapper.get(userName), 4434, userName);
        localClient.start();
        
        System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxSetting current charbox to be visible.");
        currentChatBox.setVisible(true);
    }

    public void driveAsServer(int port) {
        try {
            listBox = new ListGUI();
            // creating a new thread for local server instance.
            server = new ServerCentral(port, listBox);
            server.start();
            listBox.addSelectionListener(new javax.swing.event.ListSelectionListener() {
                public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                    listNamesValueChanged(evt, listBox, server);
                }
            });
            //displayInListBox(server.getServerPacket());
        } catch (IOException ex) {
            Logger.getLogger(DriverLocal.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void initializeClient() throws ClassNotFoundException {
        try {
            client.setClientModel(model);
            client.initializeClient(servAddr, portNumber, listBox);
        } catch (IOException ex) {
            System.out.println("IOException");
        }
    }

    private void driveAsLocalServer() {

        try {
            localServer = new ServerLocal(4434, chatBoxMapper);
            localServer.start();
        } catch (IOException ex) {
            System.out.println("Exception in creating local server in driver local.");
        }
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        DriverLocal driver = new DriverLocal();
        driver.setAsClient(true);
        DriverLocal.servAddr = "127.0.0.1";
        //ClientGUILocalSignIn signIn = new ClientGUILocalSignIn();
        //signIn.signInGUI();
        driver.driveAsServer(3034);
        driver.driveAsLocalServer();

        driver.driveAsClient();

        // after getting the hashmap from sand cetral server. Local machine acts as a server.
       
        // Now running the local server. Local machine acts as a server unless an item from the list box is
        //selected which makes the local machine to act as a client.
       
    }
}
