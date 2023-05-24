package edu.ufp.inf.sd.rabbitmqservices._advancewars.client.game.buildings;

import edu.ufp.inf.sd.rmi._advancewars.client.game.buildings.Base;

public class Airport extends Base {

	public Airport(int owner, int xx, int yy) {
		super(owner, xx, yy);
		name="Airport";
		desc="Creates Air edu.ufp.inf.sd.rmi._advancewars.client.game.units.";
		img = 3;
		Menu = "airport";
	}
}
