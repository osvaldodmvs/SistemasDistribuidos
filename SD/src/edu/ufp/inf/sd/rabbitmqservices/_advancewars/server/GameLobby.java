package edu.ufp.inf.sd.rabbitmqservices._advancewars.server;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.ArrayList;

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

	public GameLobby(String map, String ID, String user, int commander) throws RemoteException {
		this.setId(ID);
		this.setMap(map);
		this.setMaxPlayers(playersbymap(map));
		this.setNumPlayers(1);
		this.players.add(user);
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

	public void addToGameLobby(GameLobby g, String user, int commander){
		g.getPlayers().add(user);
		g.numPlayers++;

	}

	public int playersbymap(String map){
		if(map.compareTo("FourCorners")==0){
			return 4;
		}
		return 2;
	}

	public int[] getArrayOfCommanders(){
		int[] array = new int[commanders.size()];
		for(int i = 0; i < commanders.size(); i++){
			array[i] = commanders.get(i);
		}
		return array;
	}
}
