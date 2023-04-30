package edu.ufp.inf.sd.rmi._advancewars.client;

import edu.ufp.inf.sd.rmi._advancewars.client.game.engine.Game;
import edu.ufp.inf.sd.rmi._advancewars.server.GameFactoryRI;
import edu.ufp.inf.sd.rmi._advancewars.server.GameSessionRI;
import edu.ufp.inf.sd.rmi.util.rmisetup.SetupContextRMI;

import java.rmi.RemoteException;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.registry.Registry;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static edu.ufp.inf.sd.rmi._advancewars.server.User.generateJWT;
import static edu.ufp.inf.sd.rmi._advancewars.server.User.verifyJWT;

public class GameClient {

    /**
     * Context for connecting a RMI client MAIL_TO_ADDR a RMI Servant
     */
    private SetupContextRMI contextRMI;
    /**
     * Remote interface that will hold the Servant proxy
     */
    private GameFactoryRI gameFactoryRI;

    private static JTextField usernameField;
    private static JPasswordField passwordField;

    public static void main(String[] args) {
        if (args != null && args.length < 2) {
            System.exit(-1);
        } else {
            //1. ============ Setup client RMI context ============
            GameClient hwc=new GameClient(args);
            //2. ============ Lookup service ============
            hwc.lookupService();
            //3. ============ Play with service ============
            hwc.playService();
        }
    }

    public GameClient(String args[]) {
        try {
            //List ans set args
            SetupContextRMI.printArgs(this.getClass().getName(), args);
            String registryIP = args[0];
            String registryPort = args[1];
            String serviceName = args[2];
            //Create a context for RMI setup
            contextRMI = new SetupContextRMI(this.getClass(), registryIP, registryPort, new String[]{serviceName});
        } catch (RemoteException e) {
            Logger.getLogger(GameClient.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    private Remote lookupService() {
        try {
            //Get proxy MAIL_TO_ADDR rmiregistry
            Registry registry = contextRMI.getRegistry();
            //Lookup service on rmiregistry and wait for calls
            if (registry != null) {
                //Get service url (including servicename)
                String serviceUrl = contextRMI.getServicesUrl(0);
                Logger.getLogger(this.getClass().getName()).log(Level.INFO, "going MAIL_TO_ADDR lookup service @ {0}", serviceUrl);
                
                //============ Get proxy MAIL_TO_ADDR HelloWorld service ============
                gameFactoryRI = (GameFactoryRI) registry.lookup(serviceUrl);
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.INFO, "registry not bound (check IPs). :(");
                //registry = LocateRegistry.createRegistry(1099);
            }
        } catch (RemoteException | NotBoundException ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }
        return gameFactoryRI;
    }
    
    /*private void playService() {
        try {
            Scanner sc = new Scanner(System.in);

            System.out.print("Enter your name: ");
            String username = sc.nextLine();

            System.out.print("Enter your password: ");
            String password = sc.nextLine();

            gameFactoryRI.register(username,password);

            Logger.getLogger(this.getClass().getName()).log(Level.INFO, "Registered, [" + username + "]");

            GameSessionRI gameSessionRI = gameFactoryRI.login(username,password);
            Logger.getLogger(this.getClass().getName()).log(Level.INFO, "Logged in, hello [" + username + "]");

            System.out.println("------- Testing method only, sleeping for 2s -------");
            try {
                Thread.sleep(2000); // Sleep for 2 seconds
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            gameSessionRI.logout();

            Logger.getLogger(this.getClass().getName()).log(Level.INFO, "Logged out, goodbye");
        } catch (RemoteException ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }
    }*/

    private void playService() {
        JFrame frame = new JFrame("User Login/Registration");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300, 200);
        frame.setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 2));

        JLabel usernameLabel = new JLabel("Username:");
        JLabel passwordLabel = new JLabel("Password:");

        usernameField = new JTextField();
        passwordField = new JPasswordField();

        JButton loginButton = new JButton("Login");
        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String passwordString = String.valueOf(passwordField.getPassword());
                try {
                    GameSessionRI gameSessionRI = gameFactoryRI.login(username,passwordString);
                    if(gameSessionRI==null){
                        JOptionPane.showMessageDialog(frame, "User doesn't exist or wrong credentials");
                    }
                    else {
                        boolean isJwtValid = verifyJWT(gameSessionRI.getuser());

                        if (!isJwtValid) {
                            JOptionPane.showMessageDialog(frame, "User authenticated, but JWT is invalid or expired!");
                        } else {
                            JOptionPane.showMessageDialog(frame, "Logged in successfully, redirecting to game");
                            //new Game();
                        }
                    }
                } catch (RemoteException ex) {
                    Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        JButton registerButton = new JButton("Register");
        registerButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String passwordString = String.valueOf(passwordField.getPassword());
                try {
                    if(!gameFactoryRI.register(username, passwordString)){
                        JOptionPane.showMessageDialog(frame, "Error, User already successfully!");
                    }
                    else{
                        int option = JOptionPane.showOptionDialog(frame, "User registered successfully! Do you want to log in?",
                                "Registration Successful", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, new Object[]{"Yes", "No"},
                                "Yes");
                        if (option == JOptionPane.YES_OPTION) {
                            // Open the login window or perform login logic
                            GameSessionRI gameSessionRI = gameFactoryRI.login(username,passwordString);
                            JOptionPane.showMessageDialog(frame, "Logged in successfully, redirecting to game");
                            //new Game();
                        } else {
                            // Clear the fields or perform any other desired action
                            usernameField.setText("");
                            passwordField.setText("");
                        }
                    }
                } catch (RemoteException ex) {
                    Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        panel.add(usernameLabel);
        panel.add(usernameField);
        panel.add(passwordLabel);
        panel.add(passwordField);
        panel.add(loginButton);
        panel.add(registerButton);

        frame.add(panel);
        frame.setVisible(true);
    }
}


