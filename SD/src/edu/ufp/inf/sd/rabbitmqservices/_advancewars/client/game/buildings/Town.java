package edu.ufp.inf.sd.rabbitmqservices._advancewars.client.game.buildings;

import edu.ufp.inf.sd.rmi._advancewars.client.game.buildings.Base;

public class Town extends Base {

	public Town(int owner, int xx, int yy) {
		super(owner, xx, yy);
		name="Town";
		desc="Money Fodder.";
		img = 1;
	}
}
