package edu.ufp.inf.sd.rmi._advancewars.server;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.ArrayList;

public class GameLobby implements Serializable {

	private String id;
	private int numPlayers = 0;
	//numero de jogadores
	private int maxPlayers;
	private ArrayList<Integer> commanders = new ArrayList<>();
	private SubjectRI subject;
	private String map;
	//mapa
	private String state;
	//estado, a decorrer, espera ,etc

	//construtor

	public GameLobby() {
		this.setNumPlayers(0);
		this.setState("WAITING");
	}

	public GameLobby(String map, String ID) throws RemoteException {
		this.setId(ID);
		this.setMap(map);
		this.setMaxPlayers(playersbymap(map));
		System.out.println("Max players: <"+this.getMaxPlayers()+">");
		this.setNumPlayers(1);
		this.subject = new SubjectImpl(new State(id,"WAITING"));
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

	public ArrayList<Integer> getCommanders() {
		return commanders;
	}

	public void setCommanders(ArrayList<Integer> commanders) {
		this.commanders = commanders;
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

	@Override
	public String toString() {
		return map + " / players : " + numPlayers + "/" + maxPlayers + " / State : " + state + " / ID : " + id;
	}
}
