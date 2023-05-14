package edu.ufp.inf.sd.rmi._advancewars.server;

import edu.ufp.inf.sd.rmi._advancewars.client.ObserverRI;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.UUID;

public class GameLobby {

	private String id;

	private int numPlayers;
	//numero de jogadores

	private int maxPlayers;

	/*********
	 * 	private ArrayList<ObserverRI> players;
	 * 	//arraylist de jogadores (observer RI)
	 * 	faria mais sentido um SubjectRI ? o jogo seria o necessario a observar e por sua vez teria os jogadores como observers?
	 *********/

	private SubjectRI subject;
	private String map;
	//mapa
	private String state;
	//estado, a decorrer, espera ,etc

	//construtor

	public GameLobby() {
		this.setNumPlayers(0);
		//this.setPlayers(new ArrayList<>());
		this.setState("WAITING");
	}

	public GameLobby(String map, String ID) throws RemoteException {
		this.setId(ID);
		this.setMap(map);
		this.setMaxPlayers(playersbymap(map));
		this.setNumPlayers(1);
		this.subject = new SubjectImpl(new State(id,"WAITING"));
		//this.setPlayers(new ArrayList<>());
		this.setState("WAITING");
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

	/*public ArrayList<ObserverRI> getPlayers() {
		return players;
	}

	public void setPlayers(ArrayList<ObserverRI> players) {
		this.players = players;
	}*/

	public SubjectRI getSubject() {
		return subject;
	}

	public void setSubject(SubjectRI subject) {
		this.subject = subject;
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

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public int playersbymap(String map){
		if(map.compareTo("FourCorners")==0){
			return 4;
		}
		return 2;
	}
}
