package edu.ufp.inf.sd.rmi._advancewars.server;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.HashMap;

//criar sessoes de utilizadores, iniciar database

public class GameFactoryImpl extends UnicastRemoteObject implements GameFactoryRI {
    private DBMockup db;
    private HashMap<String, GameSessionImpl> sessions;
    private RSAPublicKey pubkey;
    private RSAPrivateKey privkey;

    public GameFactoryImpl() throws RemoteException, NoSuchAlgorithmException {
        super();
        this.db = new DBMockup();
        this.sessions = new HashMap<>();
        KeyPair kp = keyPairGenerator();
        this.pubkey = (RSAPublicKey) kp.getPublic();
        this.privkey = (RSAPrivateKey) kp.getPrivate();
    }

    @Override
    public boolean register(String username, String pwd) throws RemoteException {
        if(db.exists(username)){
            return false;
        }
        db.register(username, pwd);
        return true;
    }

    @Override
    public GameSessionRI login(String username, String pwd) throws RemoteException {
        if(db.exists(username)) {
            if (db.validate(username, pwd)) {
                if (sessions.containsKey(username)) {
                    User user = db.getUser(username);
                    user.setJwt(generateJWT(username));
                    for (User u: db.getUsers()) {
                        System.out.println(u.getUname());
                    }
                    return sessions.get(username);
                } else {
                    GameSessionImpl session = new GameSessionImpl(this, username);
                    User user = db.getUser(username);
                    user.setJwt(generateJWT(username));
                    sessions.put(username, session);
                    /* print loop
                    for (User u: db.getUsers()) {
                        System.out.println(u.getJwt());
                    }
                    */
                    return session;
                }
            }
        }
        return null;
    }

    public KeyPair keyPairGenerator () throws NoSuchAlgorithmException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048);
        return keyPairGenerator.generateKeyPair();
    }

    public String generateJWT(String username) {
        try {
            Algorithm algorithm = Algorithm.RSA256(pubkey,privkey);
            return JWT.create().withIssuer(username).sign(algorithm);
        } catch (JWTCreationException exception){
            System.out.println("Invalid Signing configuration / Couldn't convert Claims.");
        }
        return null;
    }

    public boolean verifyJWT(String token, String username){
        try {
            Algorithm algorithm = Algorithm.RSA256(this.pubkey, this.privkey); // Pass the public key here
            JWTVerifier verifier = JWT.require(algorithm).withIssuer(username).build();

            DecodedJWT decodedJWT = verifier.verify(token);
            //System.out.println(decodedJWT);
            return true;
        } catch (JWTVerificationException exception) {
            // Invalid signature/claims
            return false;
        }
    }

    public HashMap<String, GameSessionImpl> getSessions() {
        return sessions;
    }

    public void setSessions(HashMap<String, GameSessionImpl> sessions) {
        this.sessions = sessions;
    }

    public void deleteSession(String user){
        this.getSessions().remove(user);
    }

    public DBMockup getDb() {
        return db;
    }

    public void setDb(DBMockup db) {
        this.db = db;
    }

}
