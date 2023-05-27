package edu.ufp.inf.sd.rabbitmqservices._advancewars.client.game.menus;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;

import javax.swing.JButton;
import javax.swing.JLabel;

import edu.ufp.inf.sd.rabbitmqservices._advancewars.client.game.engine.Game;

/**
 * This deals with player and battle options setup (might split it) such as npc, team, commander, starting money, turn money, fog, etc.
 * @author SergeDavid
 * @version 0.2
 */
public class PlayerSelection implements ActionListener {
	//TODO: Scale with map size.
	//Commander Selection
	String pressed;
	String gameIDtoJoin;
	Game g;
	JButton Prev = new JButton("Prev");
	JButton Next = new JButton("Next");
	JLabel Name = new JLabel("Andy");
	int plyer = 0;
	
	//NPC Stuff
	JButton ManOrMachine = new JButton("PLY");
	boolean[] npc = {false,false,false,false};
	
	//Other
	JButton Return = new JButton("Return");
	JButton StartMoney = new JButton ("$ 100");int start = 100;
	JButton CityMoney = new JButton ("$ 50");int city = 50;
	JButton ThunderbirdsAreGo = new JButton ("Start");
	
	String mapname;
	
	public PlayerSelection(String pressed, String map, Game game) {
		this.g = game;
		this.pressed = pressed;
		this.mapname = map;
		Point size = edu.ufp.inf.sd.rabbitmqservices._advancewars.client.game.menus.MenuHandler.PrepMenu(275,200);
		Prev.addActionListener(this);
		Prev.setBounds(size.x+10+84, size.y+10, 64, 32);
		Game.gui.add(Prev);
		Next.addActionListener(this);
		Next.setBounds(size.x+10+84, size.y+100, 64, 32);
		Game.gui.add(Next);
		//ManOrMachine.addActionListener(this);
		ManOrMachine.setBounds(size.x+12+84, size.y+68, 58, 24);
		Game.gui.add(ManOrMachine);
		Name.setBounds(size.x+10+84, size.y+40, 64, 32);
		Game.gui.add(Name);
		SetBounds(size);
		AddGui();
		AddListeners();
	}

	public PlayerSelection(String pressed, String map, String gameIDtoJoin, Game gameIam){
		this.pressed = pressed;
		this.mapname = map;
		this.g = gameIam;
		this.gameIDtoJoin = gameIDtoJoin;
		System.out.println("Mapa : <"+mapname+">");
		Point size = edu.ufp.inf.sd.rabbitmqservices._advancewars.client.game.menus.MenuHandler.PrepMenu(275,200);
		Prev.addActionListener(this);
		Prev.setBounds(size.x+10+84, size.y+10, 64, 32);
		Game.gui.add(Prev);
		Next.addActionListener(this);
		Next.setBounds(size.x+10+84, size.y+100, 64, 32);
		Game.gui.add(Next);
		//ManOrMachine.addActionListener(this);
		ManOrMachine.setBounds(size.x+12+84, size.y+68, 58, 24);
		Game.gui.add(ManOrMachine);
		Name.setBounds(size.x+10+84, size.y+40, 64, 32);
		Game.gui.add(Name);
		SetBounds(size);
		AddGui();
		AddListeners();
	}

	private void SetBounds(Point size) {
		ThunderbirdsAreGo.setBounds(size.x+150, size.y+170, 100, 24);
		Return.setBounds(size.x+20, size.y+170, 100, 24);
	}
	private void AddGui() {
		Return.addActionListener(this);
		Game.gui.add(ThunderbirdsAreGo);
		Game.gui.add(Return);
	}
	private void AddListeners() {
		ThunderbirdsAreGo.addActionListener(this);
		Return.addActionListener(this);
	}
	
	@Override public void actionPerformed(ActionEvent e) {
		Object s = e.getSource();
		if (s == Return) {
			edu.ufp.inf.sd.rabbitmqservices._advancewars.client.game.menus.MenuHandler.CloseMenu();
			Game.gui.LoginScreen();
		}
		//quando cliente clica no start
		else if(s == ThunderbirdsAreGo) {
			//
		}
		if (s == Prev) {
			plyer--;
			if (plyer<0) {plyer=Game.displayC.size()-1;}
			Name.setText(Game.displayC.get(plyer).name);
		}
		else if (s == Next) {
			plyer++;
			if (plyer>Game.displayC.size()-1) {plyer=0;}
			Name.setText(Game.displayC.get(plyer).name);
		}
	}
}
