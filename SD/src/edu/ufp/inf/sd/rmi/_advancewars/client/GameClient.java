package edu.ufp.inf.sd.rmi._advancewars.client;

import edu.ufp.inf.sd.rmi._advancewars.client.game.engine.Game;
import edu.ufp.inf.sd.rmi._advancewars.client.game.menus.StartMenu;
import edu.ufp.inf.sd.rmi._advancewars.server.GameFactoryRI;
import edu.ufp.inf.sd.rmi._advancewars.server.GameSessionRI;
import edu.ufp.inf.sd.rmi.util.rmisetup.SetupContextRMI;


import java.rmi.RemoteException;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.registry.Registry;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;



public class GameClient {

    /**
     * Context for connecting a RMI client MAIL_TO_ADDR a RMI Servant
     */
    private SetupContextRMI contextRMI;
    /**
     * Remote interface that will hold the Servant proxy
     */
    private GameFactoryRI gameFactoryRI;

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

    public void loginSwitch(String choice) {
        Scanner sc = new Scanner(System.in);
        String username;
        String password;
        switch (choice) {
            case "1": {
                System.out.print("Username : ");
                username = sc.nextLine();
                System.out.print("Password : ");
                password = sc.nextLine();
                try {
                    GameSessionRI logged = gameFactoryRI.login(username, password);
                    if (logged == null) {
                        System.out.println("Wrong credentials or user doesnt exist!");
                        loginSwitch(choice);
                        break;
                    }
                    System.out.println("Logged in successfully!");
                    new Game(logged);
                } catch (RemoteException ex) {
                    System.out.println("REMOTE EXCEPTION ERROR" + ex);
                } catch (NoSuchAlgorithmException e) {
                    throw new RuntimeException(e);
                }
                break;
            }
            case "2": {
                System.out.print("Username : ");
                username = sc.nextLine();
                System.out.print("Password : ");
                password = sc.nextLine();
                try {
                    boolean registered = gameFactoryRI.register(username, password);
                    if (!registered) {
                        System.out.println("User already exists!");
                        loginSwitch(choice);
                        break;
                    }
                    System.out.println("Registered successfully! Would you like to login? [y] or [n]");
                    if (sc.nextLine().compareTo("y") == 0) {
                        GameSessionRI logged=gameFactoryRI.login(username, password);
                        System.out.println("Logged in successfully!");
                        new Game(logged);
                    } else {
                        playService();
                        break;
                    }
                } catch (RemoteException ex) {
                    System.out.println("REMOTE EXCEPTION ERROR");
                    break;
                } catch (NoSuchAlgorithmException e) {
                    throw new RuntimeException(e);
                }
            }
            default: playService();
        }
    }


    private void playService() {
        Scanner sc = new Scanner(System.in);

        System.out.println("Login [1]");
        System.out.println("Register [2]");

        System.out.print("Choose your option: ");
        String choice = sc.nextLine();

        loginSwitch(choice);
    }
}


