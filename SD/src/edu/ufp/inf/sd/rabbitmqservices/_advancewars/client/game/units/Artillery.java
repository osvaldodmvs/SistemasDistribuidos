package edu.ufp.inf.sd.rabbitmqservices._advancewars.client.game.units;

import edu.ufp.inf.sd.rmi._advancewars.client.game.units.Base;

public class Artillery extends Base {

	public Artillery(int owner, int xx, int yy, boolean active) {
		super(owner, xx, yy, active);
		name = "Artillery";
		nick = "Art";
		desc = "A ranged unit.";
		MoveAndShoot = false;
		legs = Move.TIRE;
		img = 3;
		speed = 4;
		MinAtkRange = 2;
		MaxAtkRange = 4;
		MainAttack = new int[] {0,100,100,100,100,100,100,100};
	}
}
