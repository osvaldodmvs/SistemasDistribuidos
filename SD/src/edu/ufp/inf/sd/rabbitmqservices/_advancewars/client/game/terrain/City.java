package edu.ufp.inf.sd.rabbitmqservices._advancewars.client.game.terrain;

import edu.ufp.inf.sd.rmi._advancewars.client.game.terrain.Base;

public class City extends Base {
	public City() {
		name = "City";
	}
	public boolean building() {return true;}
	public double speed() {return 1;}
	public double defense() {return 1.3;}
}
