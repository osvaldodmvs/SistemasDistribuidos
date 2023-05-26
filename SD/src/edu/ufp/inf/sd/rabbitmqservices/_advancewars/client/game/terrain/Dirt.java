package edu.ufp.inf.sd.rabbitmqservices._advancewars.client.game.terrain;

import edu.ufp.inf.sd.rabbitmqservices._advancewars.client.game.terrain.Base;

public class Dirt extends Base {
	public Dirt() {
		name = "Dirt";
	}
	public double speed() {return 1;}
	public double defense() {return 1;}
}
