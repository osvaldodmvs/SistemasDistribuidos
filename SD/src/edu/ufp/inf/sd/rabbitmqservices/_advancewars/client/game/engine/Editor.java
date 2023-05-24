package edu.ufp.inf.sd.rabbitmqservices._advancewars.client.game.engine;

import edu.ufp.inf.sd.rmi._advancewars.client.game.engine.Game;

import java.util.ArrayList;

public class Editor {
	
	public enum Type {TILE, UNIT, CITY};
	public Type pick = Type.TILE;
	public int id = 0;
	public int owner = 15;
	public boolean holding = false;
	public boolean moved = false; //This keeps you from building a ton of cities in the same spot
	
	public int selecty = 0;
	public int selectx = 0;
	public String mapname;
	
	public void StartEditor(String name, int height, int width) {
		edu.ufp.inf.sd.rmi._advancewars.client.game.engine.Game.gui.removeAll();
		edu.ufp.inf.sd.rmi._advancewars.client.game.engine.Game.GameState= edu.ufp.inf.sd.rmi._advancewars.client.game.engine.Game.State.EDITOR;
		if (edu.ufp.inf.sd.rmi._advancewars.client.game.engine.Game.error.showing) {
            edu.ufp.inf.sd.rmi._advancewars.client.game.engine.Game.gui.add(edu.ufp.inf.sd.rmi._advancewars.client.game.engine.Game.error);}
		
		edu.ufp.inf.sd.rmi._advancewars.client.game.engine.Game.player = new ArrayList<edu.ufp.inf.sd.rmi._advancewars.client.game.players.Base>();
		edu.ufp.inf.sd.rmi._advancewars.client.game.engine.Game.builds = new ArrayList<edu.ufp.inf.sd.rmi._advancewars.client.game.buildings.Base>();
		edu.ufp.inf.sd.rmi._advancewars.client.game.engine.Game.units = new ArrayList<edu.ufp.inf.sd.rmi._advancewars.client.game.units.Base>();
		
		edu.ufp.inf.sd.rmi._advancewars.client.game.engine.Game.view.Loc.x = 0;
		edu.ufp.inf.sd.rmi._advancewars.client.game.engine.Game.view.Loc.y = 0;
		mapname = name;
		
		edu.ufp.inf.sd.rmi._advancewars.client.game.engine.Game.map.MapSetup(width, height);
		edu.ufp.inf.sd.rmi._advancewars.client.game.engine.Game.btl.totalplayers = 4;//HACK
	}
	
	public void AssButton() {
		switch (pick) {
		case TILE://Done
			if (edu.ufp.inf.sd.rmi._advancewars.client.game.engine.Game.map.map[selecty][selectx].building() == true) {RemoveBuilding();}
			edu.ufp.inf.sd.rmi._advancewars.client.game.engine.Game.map.map[selecty][selectx] = edu.ufp.inf.sd.rmi._advancewars.client.game.engine.Game.map.getTile(id);
		break;
		case CITY:
			AddBuilding();
			edu.ufp.inf.sd.rmi._advancewars.client.game.engine.Game.map.map[selecty][selectx] = new edu.ufp.inf.sd.rmi._advancewars.client.game.terrain.City();
		break;
		case UNIT:
			edu.ufp.inf.sd.rmi._advancewars.client.game.engine.Game.units.add(edu.ufp.inf.sd.rmi._advancewars.client.game.engine.Game.list.CreateUnit(id, 0, selectx, selecty, false));//Change 0 to player
		break;
		}
	}

	public void ButtButton() {
		new edu.ufp.inf.sd.rmi._advancewars.client.game.menus.EditorStuff();
	}
	
	private void AddBuilding() {
		moved = false;//Keeps game from generating lots of useless edu.ufp.inf.sd.rmi._advancewars.client.game.buildings.
		
		int newbld = (edu.ufp.inf.sd.rmi._advancewars.client.game.engine.Game.map.width * selecty) + selectx;//editor id of the new city (before it is made)
		
		for (int i = 0; i < edu.ufp.inf.sd.rmi._advancewars.client.game.engine.Game.builds.size(); i++) {//Cleans out any city at the current X and Y locations using their editorid.
			if (edu.ufp.inf.sd.rmi._advancewars.client.game.engine.Game.builds.get(i).editorid == newbld) {
				edu.ufp.inf.sd.rmi._advancewars.client.game.engine.Game.builds.remove(i);
			}
		}
		System.out.println("Editor level owner: " + owner);
		edu.ufp.inf.sd.rmi._advancewars.client.game.engine.Game.builds.add(edu.ufp.inf.sd.rmi._advancewars.client.game.engine.Game.list.CreateCity(owner, selectx, selecty, id));
	}
	private void RemoveBuilding() {
		for (int i = 0; i < edu.ufp.inf.sd.rmi._advancewars.client.game.engine.Game.builds.size(); i++) {
			if (edu.ufp.inf.sd.rmi._advancewars.client.game.engine.Game.builds.get(i).x == selectx && edu.ufp.inf.sd.rmi._advancewars.client.game.engine.Game.builds.get(i).y == selecty) {
				Game.builds.remove(i);
			}
		}
	}

	public void ChangePlayer(boolean right) {
		if (right) {
			owner++;
			if (owner>4) {owner=0;}
		}
		else {
			owner--;
			if (owner<0) {owner=4;}
		}
		new edu.ufp.inf.sd.rmi._advancewars.client.game.menus.EditorStuff();
	}
}
