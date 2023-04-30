package edu.ufp.inf.sd.rmi._advancewars.client.game.buildings;

public class Barracks extends Base {

	public Barracks(int owner, int xx, int yy) {
		super(owner, xx, yy);
		name="Barracks";
		desc="Creates ground edu.ufp.inf.sd.rmi._advancewars.client.game.units.";
		img = 2;
		Menu = "barracks";
	}
}
