package edu.ufp.inf.sd.rabbitmqservices._advancewars.client.game.units;

import edu.ufp.inf.sd.rabbitmqservices._advancewars.client.game.units.Base;

public class Mechanic extends Base {

	public Mechanic(int owner, int xx, int yy, boolean active) {
		super(owner, xx, yy, active);
		name = "Mechanic";
		nick = "Mech";
		desc = "Slow infantry with some anti vehicle stuff.";
		img = 1;
		speed = 3;
		cost = 200;
		raider = true;
	}
}
