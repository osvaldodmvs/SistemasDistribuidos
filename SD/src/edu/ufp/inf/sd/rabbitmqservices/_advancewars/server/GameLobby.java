package edu.ufp.inf.sd.rabbitmqservices._advancewars.server;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GameLobby implements Serializable {

	private String id;
	private int numPlayers = 0;
	//numero de jogadores
	private int maxPlayers;
	private ArrayList<Integer> commanders = new ArrayList<>();
	private String map;
	//mapa
	private ArrayList<String> players = new ArrayList<>();

	//construtor

	public GameLobby() {
		this.setNumPlayers(0);
	}

	public GameLobby(String user, String id, int maxplayers, int commander) throws RemoteException {
		this.players.add(user);
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

	public int addToGameLobby(String user, int commander){
		if(numPlayers+1>getMaxPlayers()){
			return -1;
		}
		this.getPlayers().add(user);
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
}
