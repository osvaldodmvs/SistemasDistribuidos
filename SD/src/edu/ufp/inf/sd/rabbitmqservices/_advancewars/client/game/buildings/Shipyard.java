package edu.ufp.inf.sd.rabbitmqservices._advancewars.client.game.buildings;

//import edu.ufp.inf.sd.rmi._advancewars.client.game.engine.Game;

import edu.ufp.inf.sd.rmi._advancewars.client.game.buildings.Base;

public class Shipyard extends Base {

	public Shipyard(int owner, int xx, int yy) {
		super(owner, xx, yy);
		name="Capital";
		desc="Creates water units.";
		img = 4;
		Menu = "shipyard";
		//Game.map.map[yy][xx].swim = true;
	}
}
