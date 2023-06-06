package edu.ufp.inf.sd.rabbitmqservices._advancewars.server;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.ArrayList;


public class GameLobby implements Serializable {
	private RSAPublicKey pubkey;
	private RSAPrivateKey privkey;

	private String id;
	private int numPlayers = 0;
	//numero de jogadores
	private int maxPlayers;
	private ArrayList<Integer> commanders = new ArrayList<>();
	private String map;
	//mapa
	private ArrayList<String> players = new ArrayList<>();

	private ArrayList<String> jwts = new ArrayList<>();

	//construtor

	public GameLobby() {
		this.setNumPlayers(0);
	}

	public GameLobby(String user, String id, int maxplayers, int commander) throws RemoteException, NoSuchAlgorithmException {
		this.players.add(user);
		KeyPair kp = keyPairGenerator();
		this.pubkey = (RSAPublicKey) kp.getPublic();
		this.privkey = (RSAPrivateKey) kp.getPrivate();
		this.jwts.add(generateJWT(user));
		this.setId(id);
		this.setMaxPlayers(maxplayers);
		this.setMap(mapByPlayers(maxplayers));
		this.setNumPlayers(1);
		this.commanders.add(commander);
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getNumPlayers() {
		return numPlayers;
	}

	public void setNumPlayers(int numPlayers) {
		this.numPlayers = numPlayers;
	}


	public int getMaxPlayers() {
		return maxPlayers;
	}

	public void setMaxPlayers(int maxPlayers) {
		this.maxPlayers = maxPlayers;
	}

	public String getMap() {
		return map;
	}

	public void setMap(String map) {
		this.map = map;
	}

	public ArrayList<String> getPlayers() {
		return players;
	}

	public void setPlayers(ArrayList<String> players) {
		this.players = players;
	}

	public ArrayList<Integer> getCommanders() {
		return commanders;
	}

	public void setCommanders(ArrayList<Integer> commanders) {
		this.commanders = commanders;
	}

	public RSAPublicKey getPubkey() {
		return pubkey;
	}

	public void setPubkey(RSAPublicKey pubkey) {
		this.pubkey = pubkey;
	}

	public RSAPrivateKey getPrivkey() {
		return privkey;
	}

	public void setPrivkey(RSAPrivateKey privkey) {
		this.privkey = privkey;
	}

	public ArrayList<String> getJwts() {
		return jwts;
	}

	public void setJwts(ArrayList<String> jwts) {
		this.jwts = jwts;
	}

	public int addToGameLobby(String user, int commander){
		if(numPlayers+1>getMaxPlayers()){
			return -1;
		}
		this.getPlayers().add(user);
		this.getJwts().add(generateJWT(user));
		this.numPlayers++;
		this.commanders.add(commander);
		return 0;
	}

	public String mapByPlayers(int maxPlayers){
		if(maxPlayers==4){
			return "FourCourners";
		}
		return "SmallVs";
	}

	public String returnCommanders(){
		StringBuilder build = new StringBuilder();
		for (int commander : commanders) {
			build.append(commander).append("");
		}
		return build.toString().trim();
	}

	public int[] getArrayOfCommanders(){
		int[] array = new int[commanders.size()];
		for(int i = 0; i < commanders.size(); i++){
			array[i] = commanders.get(i);
		}
		return array;
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



}
