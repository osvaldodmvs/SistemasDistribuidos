package edu.ufp.inf.sd.rabbitmqservices._advancewars.client.game.menus;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;
import javax.swing.JButton;
import edu.ufp.inf.sd.rmi._advancewars.client.game.engine.Game;
import edu.ufp.inf.sd.rmi._advancewars.client.game.menus.MenuHandler;
import edu.ufp.inf.sd.rmi._advancewars.client.game.menus.Options;
import edu.ufp.inf.sd.rmi._advancewars.server.State;
import edu.ufp.inf.sd.rmi._advancewars.server.SubjectRI;

/**
 * This is the pause menu that is pulled up when you press the Enter button in game.
 * @author SergeDavid
 * @version 0.3
 */
public class Pause implements ActionListener {
	SubjectRI sri;

	Game g;
	JButton Help = new JButton("Help");
	JButton Save = new JButton("Save");
	JButton Options = new JButton("Options");
	JButton EndTurn = new JButton("EndTurn");
	JButton Resume = new JButton("Resume");
	JButton Quit = new JButton("Quit");
	
	public Pause(Game g,SubjectRI sri) {
		Point size = MenuHandler.PrepMenu(120,180);
		SetBounds(size);
		AddGui();
		AddListeners();
		this.sri = sri;
		this.g = g;
	}
	private void SetBounds(Point size) {
		Resume.setBounds(size.x+10, size.y+10, 100, 24);
		Save.setBounds(size.x+10, size.y+30*1+10, 100, 24);
		Options.setBounds(size.x+10, size.y+30*2+10, 100, 24);
		EndTurn.setBounds(size.x+10, size.y+30*3+10, 100, 24);
		Quit.setBounds(size.x+10, size.y+30*4+10, 100, 24);
	}
	private void AddGui() {
		Game.gui.add(Resume);
		Game.gui.add(Save);
		Game.gui.add(Options);
		Game.gui.add(EndTurn);
		Game.gui.add(Quit);
	}
	private void AddListeners() {
		Resume.addActionListener(this);
		Save.addActionListener(this);
		Options.addActionListener(this);
		EndTurn.addActionListener(this);
		Quit.addActionListener(this);
	}
	
	@Override public void actionPerformed(ActionEvent e) {
		Object s = e.getSource();
		if (s==Quit) {
			MenuHandler.CloseMenu();
			try {
				Game.gui.LoginScreen();
			} catch (RemoteException ex) {
				throw new RuntimeException(ex);
			}
		}
		else if (s==EndTurn) {
			try {
				sri.setState(new State(g.getId(),"END-TURN"));
			} catch (RemoteException ex) {
				throw new RuntimeException(ex);
			};
		}
		else if (s==Resume) {MenuHandler.CloseMenu();}
		else if (s==Save) {Game.save.SaveGame();}
		else if (s==Options) {new Options();}
	}
}
