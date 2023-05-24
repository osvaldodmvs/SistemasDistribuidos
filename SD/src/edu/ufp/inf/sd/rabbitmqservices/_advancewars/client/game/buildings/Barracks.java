package edu.ufp.inf.sd.rabbitmqservices._advancewars.client.game.buildings;

import edu.ufp.inf.sd.rmi._advancewars.client.game.buildings.Base;

public class Barracks extends Base {

	public Barracks(int owner, int xx, int yy) {
		super(owner, xx, yy);
		name="Barracks";
		desc="Creates ground edu.ufp.inf.sd.rmi._advancewars.client.game.units.";
		img = 2;
		Menu = "barracks";
	}
}
