package edu.ufp.inf.sd.rmi._advancewars.client.game.engine;

import edu.ufp.inf.sd.rmi._advancewars.server.GameLobby;
import edu.ufp.inf.sd.rmi._advancewars.server.GameSessionRI;
import edu.ufp.inf.sd.rmi._advancewars.server.State;
import edu.ufp.inf.sd.rmi._advancewars.server.SubjectRI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.rmi.RemoteException;

/**
 * Keyboard handling for the game along with the mouse setup for game handling.
 * Menus are being moved to edu.ufp.inf.sd.rmi._advancewars.client.game.gui.gms
 * @author SergeDavid
 * @version 0.1
 */
@SuppressWarnings("unused")
public class InputHandler implements KeyListener,MouseListener,ActionListener {
	
	//Development buttons and the exit game button (escape key)

	private GameSessionRI gsri;
	private String gameID;

	private final int dev1 = KeyEvent.VK_NUMPAD1;
	private final int dev2 = KeyEvent.VK_NUMPAD2;
	private final int dev3 = KeyEvent.VK_NUMPAD3;
	private final int dev4 = KeyEvent.VK_NUMPAD4;
	private final int dev5 = KeyEvent.VK_NUMPAD5;
	private final int dev6 = KeyEvent.VK_NUMPAD6;
	private final int dev7 = KeyEvent.VK_NUMPAD7;
	private final int dev8 = KeyEvent.VK_NUMPAD8;
	private final int dev9 = KeyEvent.VK_NUMPAD9;
	private final int exit = KeyEvent.VK_ESCAPE;
	
	//Movement buttons
	private final int up = KeyEvent.VK_UP;
	private final int down = KeyEvent.VK_DOWN;
	private final int left = KeyEvent.VK_LEFT;
	private final int right = KeyEvent.VK_RIGHT;

	//Command buttons
	private final int select = KeyEvent.VK_Z;
	private final int cancel = KeyEvent.VK_X;
	private final int start = KeyEvent.VK_ENTER;
	
	//Mouse (right/left clicks)
	private final int main = MouseEvent.BUTTON1;
	private final int alt = MouseEvent.BUTTON1;
	
	public InputHandler(GameSessionRI gsri, String id) {
		this.gsri=gsri;
		this.gameID=id;
		Game.gui.addKeyListener(this);
		Game.gui.addMouseListener(this);
	}

	int DevPathing = 1;
	public void keyPressed(KeyEvent e) {
		int i=e.getKeyCode();
		if (i==exit) {System.exit(0);}
		if (Game.GameState==Game.State.PLAYING) {
			edu.ufp.inf.sd.rmi._advancewars.client.game.players.Base ply = Game.player.get(Game.btl.currentplayer);
			try {
				GameLobby gl = gsri.getGameIDfromLobby(gameID);
				SubjectRI sri = gl.getSubject();
				if (i==up) sri.setState(new State(gameID,"UP"));
				else if (i==down) sri.setState(new State(gameID,"DOWN"));
				else if (i==left) sri.setState(new State(gameID,"LEFT"));
				else if (i==right) sri.setState(new State(gameID,"RIGHT"));
				else if (i==select) sri.setState(new State(gameID,"SELECT"));
				else if (i==cancel) sri.setState(new State(gameID,"CANCEL"));
				else if (i==start) sri.setState(new State(gameID,"START-MENU"));
			} catch (RemoteException ex) {
				throw new RuntimeException(ex);
			}
		}
		if (Game.GameState==Game.State.EDITOR) {
			if (i==up) {Game.edit.selecty--;if (Game.edit.selecty<0) {Game.edit.selecty++;} Game.edit.moved = true;}
			else if (i==down) {Game.edit.selecty++;if (Game.edit.selecty>=Game.map.height) {Game.edit.selecty--;} Game.edit.moved = true;}
			else if (i==left) {Game.edit.selectx--;if (Game.edit.selectx<0) {Game.edit.selectx++;} Game.edit.moved = true;}
			else if (i==right) {Game.edit.selectx++;if (Game.edit.selectx>=Game.map.width) {Game.edit.selectx--;} Game.edit.moved = true;}
			else if (i==select) {Game.edit.holding = true;}
			else if (i==cancel) {Game.edit.ButtButton();}
			else if (i==start) {
				new edu.ufp.inf.sd.rmi._advancewars.client.game.menus.EditorMenu();
			}
		}
		
		if (i==dev1) {
			try {
				Game.gui.LoginScreen();
			} catch (RemoteException ex) {
				throw new RuntimeException(ex);
			}
		}
		else if (i==dev2) {Game.load.LoadTexturePack("Test");}
		else if (i==dev3) {
			DevPathing++;
			switch (DevPathing) {
				case 1:Game.pathing.ShowCost=false;break;
				case 2:Game.pathing.ShowHits=true;break;
				case 3:Game.pathing.ShowHits=false;Game.pathing.ShowCost=true;DevPathing=0;break;
			}
		}
		else if (i==dev4) {Game.btl.EndTurn();}
		else if (i==dev5) {Game.player.get(Game.btl.currentplayer).npc = !Game.player.get(Game.btl.currentplayer).npc; Game.btl.EndTurn();}
		else if (i==dev6) {
			try {
				new edu.ufp.inf.sd.rmi._advancewars.client.game.menus.StartMenu();
			} catch (RemoteException ex) {
				throw new RuntimeException(ex);
			}
		}
	}
	public void keyReleased(KeyEvent e) {
		int i=e.getKeyCode();
		if (Game.GameState==Game.State.EDITOR) {
			if (i==select) {Game.edit.holding = false;}
		}
	}
	public void keyTyped(KeyEvent arg0) {}
	public void mousePressed() {}
	public void mouseClicked(MouseEvent arg0) {}
	public void mouseEntered(MouseEvent arg0) {}
	public void mouseExited(MouseEvent arg0) {}
	public void mousePressed(MouseEvent arg0) {}
	public void mouseReleased(MouseEvent arg0) {}

	@Override
	public void actionPerformed(ActionEvent e) {
		Game.gui.requestFocusInWindow();
		Object s = e.getSource();
	}
}
