package edu.ufp.inf.sd.rmi._advancewars.client.game.menus;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;
import javax.swing.*;

import edu.ufp.inf.sd.rmi._advancewars.client.game.engine.Game;
import edu.ufp.inf.sd.rmi._advancewars.server.GameLobby;
import edu.ufp.inf.sd.rmi._advancewars.server.User;

/**
 * This is the opening menu of the game.
 * @author SergeDavid
 * @version 0.2
 */
public class StartMenu implements ActionListener {
	protected int UUID = 36;
	//Single Player
	public JButton New = new JButton("New Game");
	//Online
	public JButton Load = new JButton("Continue");
	public JButton Join = new JButton("Join Game");

	//Other
	public JButton Editor = new JButton("Editor");
	public JButton Credits = new JButton("Credits");
	public JButton Options = new JButton("Options");
	public JButton Exit = new JButton("Exit");
	
	//Map list
	public JList maps_list = new JList();
	public JList games_list = new JList();
	DefaultListModel maps_model = new DefaultListModel();
	
	public StartMenu() throws RemoteException {
		Point size = MenuHandler.PrepMenu(600,280);
		MenuHandler.HideBackground();
		SetBounds(size);
		AddGui();
		AddListeners();
		MapList(size);
		GameList(size);
	}

	private void SetBounds(Point size) {
		New.setBounds(size.x,size.y+10, 100, 32);
		Join.setBounds(size.x,size.y+10+38*1, 100, 32);
		Load.setBounds(size.x,size.y+10+38*2, 100, 32);
		Editor.setBounds(size.x,size.y+10+38*3, 100, 32);
		Credits.setBounds(size.x,size.y+10+38*4, 100, 32);
		Options.setBounds(size.x,size.y+10+38*5, 100, 32);
		Exit.setBounds(size.x,size.y+10+38*6, 100, 32);
	}
	private void AddGui() {
		Game.gui.add(New);
		Game.gui.add(Join);
		//Game.gui.add(Load);
		Game.gui.add(Editor);
		Game.gui.add(Credits);
		Game.gui.add(Options);
		Game.gui.add(Exit);
	}
	private void MapList(Point size) {
		maps_model = Game.finder.GrabMaps();
		JScrollPane maps_pane = new JScrollPane(maps_list = new JList(maps_model));
		maps_pane.setBounds(size.x+220, size.y+10, 140, 260);//220,10
		Game.gui.add(maps_pane);
		maps_list.setBounds(0, 0, 140, 260);
		maps_list.setSelectedIndex(0);
	}

	private void GameList(Point size) throws RemoteException {
		DefaultListModel games_model = new DefaultListModel();
		//TODO : arranjar a lista dos jogos
		/*for (GameLobby g : Game.getGameSessionRI().getGames()) {
			games_model.addElement(g.getMap() + " - " + g.getNumPlayers() + " - " + g.getState() + "/" + g.getId());
		}*/
		JScrollPane games_pane = new JScrollPane(games_list = new JList(games_model));
		games_pane.setBounds(size.x+420, size.y+10, 140, 260);//420,10
		Game.gui.add(games_pane);
		games_list.setBounds(0, 0, 140, 260);
		games_list.setSelectedIndex(0);
	}

	private void AddListeners() {
		New.addActionListener(this);
		Load.addActionListener(this);
		Join.addActionListener(this);
		Editor.addActionListener(this);
		Credits.addActionListener(this);
		Options.addActionListener(this);
		Exit.addActionListener(this);
	}

	@Override public void actionPerformed(ActionEvent e) {
		Object s = e.getSource();
		//TODO : informar o playerselect de que botao é que foi clicado (new game or join game)
		if (s==New) {new PlayerSelection("New",maps_list.getSelectedValue()+"");}
		else if (s==Load) {Game.save.LoadGame();MenuHandler.CloseMenu();}
		else if (s==Join) {
			String findingId = (games_list.getSelectedValue()+"").substring((games_list.getSelectedValue()+"").length()-UUID);
			System.out.println("ID ----------------------------- " + findingId);
			new PlayerSelection("Join",null,findingId);
		}
		else if (s==Editor) {
			Game.edit.StartEditor(
					"MapName",
					16,
					20);
			MenuHandler.CloseMenu();
		}
		else if (s==Credits) {new Credits();}
		else if (s==Options) {new Options();}
		else if (s==Exit) {System.exit(0);}
	}
}
