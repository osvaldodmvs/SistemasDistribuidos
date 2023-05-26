package edu.ufp.inf.sd.rabbitmqservices._advancewars.client.game.units;

import edu.ufp.inf.sd.rabbitmqservices._advancewars.client.game.units.Base;

public class Infantry extends Base {
	public Infantry(int owner, int xx, int yy, boolean active) {
		super(owner, xx, yy, active);
		name = "Infantry";
		nick = "Inf";
		desc = "Weakest units here.";
		img = 0;
		speed = 4;
		raider = true;
	}
}
