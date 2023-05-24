package edu.ufp.inf.sd.rabbitmqservices._advancewars.client.game.engine;

import edu.ufp.inf.sd.rmi._advancewars.client.game.buildings.Base;
import edu.ufp.inf.sd.rmi._advancewars.client.game.engine.Game;
import edu.ufp.inf.sd.rmi._advancewars.client.game.menus.EndBattle;
import edu.ufp.inf.sd.rmi._advancewars.server.State;
import edu.ufp.inf.sd.rmi._advancewars.server.SubjectRI;

import java.rmi.RemoteException;
import java.util.ArrayList;

/**Put the game stuff in here so all I have to do is end/start this to make a game work or not.*/
public class Battle {
	/**A count of all the edu.ufp.inf.sd.rmi._advancewars.client.game.players in the game, used before Game.player is populated so this is required.*/
	public int totalplayers = 2;
	/**The current player who is playing, this loops back to 0 when it goes too high.*/
	public int currentplayer = 0;
	public String mapname;
	public boolean GameOver;

	public String idFromGame;
	public SubjectRI subjectRI;
	
	//Game settings
	boolean FogOfWar;
	int startingmoney = 100;//How much you start with each round.
	int buildingmoney = 50;//How much each building provides.
	int day = 1;
	
	//Winning condition settings
	public int playersleft = 1;
	
	public void NewGame(String mapname) {
		edu.ufp.inf.sd.rmi._advancewars.client.game.engine.Game.player = new ArrayList<edu.ufp.inf.sd.rmi._advancewars.client.game.players.Base>();
		edu.ufp.inf.sd.rmi._advancewars.client.game.engine.Game.builds = new ArrayList<Base>();
		edu.ufp.inf.sd.rmi._advancewars.client.game.engine.Game.units = new ArrayList<edu.ufp.inf.sd.rmi._advancewars.client.game.units.Base>();
		edu.ufp.inf.sd.rmi._advancewars.client.game.engine.Game.view.Loc.x = 0;
		edu.ufp.inf.sd.rmi._advancewars.client.game.engine.Game.view.Loc.y = 0;
		if (!edu.ufp.inf.sd.rmi._advancewars.client.game.engine.Game.map.parse.decode(mapname)) {
			edu.ufp.inf.sd.rmi._advancewars.client.game.engine.Game.GameState = edu.ufp.inf.sd.rmi._advancewars.client.game.engine.Game.State.MENU;
			return;
		}
		this.mapname = mapname;
		playersleft = totalplayers;
		GameOver = false;
		
		/*
		String a = "%9*OUU?B=D T9BO";
        String b = "cVX*#0Mb\\dC;]'=}";
        char[] c = new char[16];
        for (int i = 0; i < 15; i++)
        {
            c[i] = a.charAt(i);
            c[i] ^= b.charAt(i);
        }
        System.out.println(new String(c));
        */
	}

	public void EndTurn() {
		System.out.println("ENTERED THE END TURN MENU");
		edu.ufp.inf.sd.rmi._advancewars.client.game.players.Base ply = edu.ufp.inf.sd.rmi._advancewars.client.game.engine.Game.player.get(currentplayer);
		for (edu.ufp.inf.sd.rmi._advancewars.client.game.units.Base unit : edu.ufp.inf.sd.rmi._advancewars.client.game.engine.Game.units) {
			unit.acted=false;
			unit.moved=false;
		}
		currentplayer++;
		if (currentplayer>=totalplayers) {currentplayer=0;day++;}
		ply = edu.ufp.inf.sd.rmi._advancewars.client.game.engine.Game.player.get(currentplayer);
		if (day!=1) {
			ply.money+=buildingmoney*Buildingcount(currentplayer);
		}
		for (edu.ufp.inf.sd.rmi._advancewars.client.game.units.Base unit : edu.ufp.inf.sd.rmi._advancewars.client.game.engine.Game.units) {
			if (unit.owner == currentplayer && unit.health<unit.maxhp && unit.bld!=-1) {
				unit.Medic();
			}
		}
		edu.ufp.inf.sd.rmi._advancewars.client.game.engine.Game.pathing.LastChanged++;
	}
	
	/**Grabs the number of edu.ufp.inf.sd.rmi._advancewars.client.game.buildings a player owns.*/
	private int Buildingcount(int owner) {
		int total = 0;
		for (Base bld : edu.ufp.inf.sd.rmi._advancewars.client.game.engine.Game.builds) {
			if (bld.owner==owner) {total++;}
		}
		return total;
	}
	
	public void Action() {
		edu.ufp.inf.sd.rmi._advancewars.client.game.players.Base ply = edu.ufp.inf.sd.rmi._advancewars.client.game.engine.Game.player.get(currentplayer);
		if (ply.npc) {return;}
		if (ply.unitselected) {
			if (currentplayer== edu.ufp.inf.sd.rmi._advancewars.client.game.engine.Game.units.get(ply.selectedunit).owner) {//Action
				if (edu.ufp.inf.sd.rmi._advancewars.client.game.engine.Game.units.get(ply.selectedunit).moved&&!edu.ufp.inf.sd.rmi._advancewars.client.game.engine.Game.units.get(ply.selectedunit).acted) {
					edu.ufp.inf.sd.rmi._advancewars.client.game.engine.Game.units.get(ply.selectedunit).action(ply.selectx,ply.selecty);
					ply.unitselected=false;
				}
				else if (!edu.ufp.inf.sd.rmi._advancewars.client.game.engine.Game.units.get(ply.selectedunit).moved) {//Move
					edu.ufp.inf.sd.rmi._advancewars.client.game.engine.Game.units.get(ply.selectedunit).move(ply.selectx,ply.selecty);
				}
				else {ply.unitselected=false;}
			}
			else {ply.unitselected=false;}
		}
		else {
			if (!ply.FindUnit()) {
				ply.unitselected=false;
				ply.FindCity();
			}
		}
	}	
	/**This will be redone when I set up the unit buying menu.*/
	public void Buyunit(int type, int x, int y) throws RemoteException {
		subjectRI.setState(new State(idFromGame,"BUY UNIT " + type + " " + x + " " + y + " " + currentplayer));
	}

	public void MaxUsers(int max) {
		//Setup for max edu.ufp.inf.sd.rmi._advancewars.client.game.players
		if (max<2) {totalplayers = 2;}
		else if (max>12) {totalplayers = 12;}
		else {totalplayers = max;}
		//HACK: Change when I add more images to support more edu.ufp.inf.sd.rmi._advancewars.client.game.players.
		if (max>4) {totalplayers = 4;
			edu.ufp.inf.sd.rmi._advancewars.client.game.engine.Game.error.ShowError("The game currently supports only 4 players, not " + max + ".");
		}
	}
	public void AddCommanders(int[] coms, boolean[] npc, int start, int city) {
		startingmoney = start;
		buildingmoney = city;
		for (int i = 0;i<totalplayers;i++) {//TODO: Team setup needs to be added.
			edu.ufp.inf.sd.rmi._advancewars.client.game.engine.Game.player.add(edu.ufp.inf.sd.rmi._advancewars.client.game.engine.Game.list.CreateCommander(coms[i],i+1,start,npc[i]));
		}
		for (Base bld : edu.ufp.inf.sd.rmi._advancewars.client.game.engine.Game.builds) {
			if (bld.owner!=15) {
				bld.team = edu.ufp.inf.sd.rmi._advancewars.client.game.engine.Game.player.get(bld.owner).team;
			}
		}
	}

	public void CaptureCapital(int x, int y) {
		int loser = 0;
		for (int i = 0; i < edu.ufp.inf.sd.rmi._advancewars.client.game.engine.Game.builds.size(); i++) {
			if (edu.ufp.inf.sd.rmi._advancewars.client.game.engine.Game.builds.get(i).x == x && edu.ufp.inf.sd.rmi._advancewars.client.game.engine.Game.builds.get(i).y == y) {
				edu.ufp.inf.sd.rmi._advancewars.client.game.engine.Game.player.get(edu.ufp.inf.sd.rmi._advancewars.client.game.engine.Game.builds.get(i).owner).defeated=true;//Makes a player lose.
				loser = edu.ufp.inf.sd.rmi._advancewars.client.game.engine.Game.builds.get(i).owner;
				System.out.println("Grrr " + loser);
				edu.ufp.inf.sd.rmi._advancewars.client.game.engine.Game.builds.remove(i);
				edu.ufp.inf.sd.rmi._advancewars.client.game.engine.Game.builds.add(i, edu.ufp.inf.sd.rmi._advancewars.client.game.engine.Game.list.CreateCity(currentplayer, x, y, 1));
				playersleft--;
				if (playersleft<=1) {
					GameOver = true;
					new EndBattle();
				}
				break;
			}
		}
		for (int i = 0; i < edu.ufp.inf.sd.rmi._advancewars.client.game.engine.Game.units.size(); i++) {
			if (edu.ufp.inf.sd.rmi._advancewars.client.game.engine.Game.units.get(i).owner == loser) {
				System.out.println("Remove " + edu.ufp.inf.sd.rmi._advancewars.client.game.engine.Game.units.get(i).owner);
				Game.units.remove(i);
			}
		}
		//TODO: Change all edu.ufp.inf.sd.rmi._advancewars.client.game.buildings to be owned by the player.
	}

	public int getCurrentplayer() {
		return currentplayer;
	}

	public void setCurrentplayer(int currentplayer) {
		this.currentplayer = currentplayer;
	}
}
