package edu.ufp.inf.sd.rmi._advancewars.client.game.engine;


import edu.ufp.inf.sd.rmi._advancewars.client.game.menus.MenuHandler;
import edu.ufp.inf.sd.rmi._advancewars.client.game.menus.Pause;
import edu.ufp.inf.sd.rmi._advancewars.client.game.players.Base;
import edu.ufp.inf.sd.rmi._advancewars.server.GameLobby;
import edu.ufp.inf.sd.rmi._advancewars.server.GameSessionRI;
import edu.ufp.inf.sd.rmi._advancewars.server.SubjectRI;

import java.awt.Dimension;
import java.awt.Image;
import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.swing.JFrame;

public class Game extends JFrame implements Serializable {
	private static final String id = UUID.randomUUID().toString();
	private static GameSessionRI gameSessionRI;

	private static Game gg;
	private static final long serialVersionUID = 1L;
	
	//Application Settings
	private static final String build = "0";
	private static final String version = "2";
	public static final String name = "Strategy Game";
	public static int ScreenBase = 32;//Bit size for the screen, 16 / 32 / 64 / 128
	public static boolean dev = true;//Is this a dev copy or not... useless? D:
	
	public static enum State {STARTUP, MENU, PLAYING, EDITOR};
	public static State GameState = State.STARTUP;
		
	//Setup the quick access to all of the other class files.
	public static Map map;
	public static Gui gui;
	public static LoadImages load;
	public static InputHandler input;
	public static Editor edit = new Editor();
	public static Battle btl = new Battle();
	public static ErrorHandler error = new ErrorHandler();
	public static Pathfinding pathing = new Pathfinding();
	public static ListData list;
	public static Save save = new Save();
	public static ComputerBrain brain;

	static {
		try {
			brain = new ComputerBrain();
		} catch (RemoteException e) {
			throw new RuntimeException(e);
		}
	}

	public static FileFinder finder = new FileFinder();
	public static ViewPoint view = new ViewPoint();
	
	//Image handling settings are as follows
	public int fps;
	public int fpscount;
	public static Image[] img_menu = new Image[5];
	public static Image img_tile;
	public static Image img_char;
	public static Image img_plys;
	public static Image img_city;
	public static Image img_exts;
	public static Boolean readytopaint;
	
	//This handles the different edu.ufp.inf.sd.rmi._advancewars.client.game.players and also is used to speed logic arrays (contains a list of all characters they own)
	public static List<edu.ufp.inf.sd.rmi._advancewars.client.game.players.Base> player = new ArrayList<edu.ufp.inf.sd.rmi._advancewars.client.game.players.Base>();
	public static List<edu.ufp.inf.sd.rmi._advancewars.client.game.buildings.Base> builds = new ArrayList<edu.ufp.inf.sd.rmi._advancewars.client.game.buildings.Base>();
	public static List<edu.ufp.inf.sd.rmi._advancewars.client.game.units.Base> units = new ArrayList<edu.ufp.inf.sd.rmi._advancewars.client.game.units.Base>();
	//These are the lists that will hold commander, building, and unit data to use in the menu's
	public static List<edu.ufp.inf.sd.rmi._advancewars.client.game.players.Base> displayC = new ArrayList<edu.ufp.inf.sd.rmi._advancewars.client.game.players.Base>();
	public static List<edu.ufp.inf.sd.rmi._advancewars.client.game.buildings.Base> displayB = new ArrayList<edu.ufp.inf.sd.rmi._advancewars.client.game.buildings.Base>();
	public static List<edu.ufp.inf.sd.rmi._advancewars.client.game.units.Base> displayU = new ArrayList<edu.ufp.inf.sd.rmi._advancewars.client.game.units.Base>();
	
	public Game(GameSessionRI gsRI) throws RemoteException{
		super (name);
		//Default Settings of the JFrame
		//TODO : criar um observerImpl;
		gameSessionRI = gsRI;
	    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    setSize(new Dimension(20*ScreenBase+6,12*ScreenBase+12));
		setBounds(0,0,20*ScreenBase+6,12*ScreenBase+12);
	    setUndecorated(false);
		setResizable(false);
	    setLocationRelativeTo(null);
				
		//Creates all the edu.ufp.inf.sd.rmi._advancewars.client.game.gui elements and sets them up
		gui = new Gui(this);
		add(gui);
		gui.setFocusable(true);
		gui.requestFocusInWindow();
		
		//load images, initialize the map, and adds the input settings.
		load = new LoadImages();
		map = new Map();
		input = new InputHandler(gsRI,id);
		list = new ListData();
		
		setVisible(true);//This has been moved down here so that when everything is done, it is shown.
		gui.LoginScreen();
		save.LoadSettings();
		GameLoop();
	}

	private void GameLoop() throws RemoteException {
		boolean loop=true;
		long last = System.nanoTime();
		long lastCPSTime = 0;
		long lastCPSTime2 = 0;
		@SuppressWarnings("unused")
		int logics = 0;
		logics++;
		while (loop) {
			//Used for logic stuff
			@SuppressWarnings("unused")
			long delta = (System.nanoTime() - last) / 1000000;
			delta++;
			last = System.nanoTime();
			
			//FPS settings
			if (System.currentTimeMillis() - lastCPSTime > 1000) {
				lastCPSTime = System.currentTimeMillis();
				fpscount = fps;
				fps = 0;
				error.ErrorTicker();
				setTitle(name + " v" + build + "." + version + " : FPS " + fpscount);
				if (GameState == State.PLAYING) {
					if (player.get(btl.currentplayer).npc&&!btl.GameOver) {
						brain.ThinkDamnYou(player.get(btl.currentplayer));
					}
				}
			}
			else fps++;
			//Current Logic and frames per second location (capped at 20 I guess?)
			if (System.currentTimeMillis() - lastCPSTime2 > 100) {
				lastCPSTime2 = System.currentTimeMillis();
				logics = 0;
				if (GameState==State.PLAYING || GameState==State.EDITOR) {
					view.MoveView();
				}//This controls the view-point on the map
				if (GameState == State.EDITOR) {
					if (edit.holding && edit.moved) {edit.AssButton();}
				}
				Game.gui.frame++;//This is controlling the current frame of animation.
				if (Game.gui.frame>=12) {Game.gui.frame=0;}
				gui.repaint();
			}
			else logics++;
			
			//Paints the scene then sleeps for a bit.
			try { Thread.sleep(30);} catch (Exception e) {};
		}
	}

	public String getId() {
		return id;
	}

	public static GameSessionRI getGameSessionRI() {
		return gameSessionRI;
	}

	public void Start() throws RemoteException {
		GameLobby gl = gameSessionRI.getGameIDfromLobby(id);
		System.out.println("MY ID IS " + id + " AND I FOUND LOBBY GL WITH ID: "+gl.getId());
		Game.btl.NewGame(gl.getMap());
		int[] commanders = gl.getArrayOfCommanders();
		boolean[] placeHolderNPC = {false,false,false,false};
		Game.btl.AddCommanders(commanders, placeHolderNPC, 100, 50);
		MenuHandler.CloseMenu();
		Game.gui.InGameScreen();
		Game.btl.subjectRI = gl.getSubject();
		Game.btl.idFromGame = id;
		this.setGg(this);
	}

	public static void updateGUI(String MovementOrAction) throws RemoteException {
		Base ply = Game.player.get(Game.btl.currentplayer);
		SubjectRI sri = getGameSessionRI().getGameIDfromLobby(id).getSubject();
		if(MovementOrAction.startsWith("BUY-UNIT")){
			String[] split = MovementOrAction.split(" "); //BUY UNIT TYPE X Y CURRENTPLAYER
			int type = Integer.parseInt(split[2]);
			int x = Integer.parseInt(split[3]);
			int y = Integer.parseInt(split[4]);
			int currentplayer = Integer.parseInt(split[5]);
			double cost = Game.displayU.get(type).cost*Game.player.get(currentplayer).CostBonus;
			if (Game.player.get(currentplayer).money>=cost) {
				Game.units.add(Game.list.CreateUnit(type, currentplayer, x, y, false));
				Game.player.get(currentplayer).money-=cost;
				MenuHandler.CloseMenu();
			}
			return;
		}
		switch (MovementOrAction) {
			case "UP":
				ply.selecty--;
				if (ply.selecty < 0)
					ply.selecty++;
				break;
			case "DOWN":
				ply.selecty++;
				if (ply.selecty >= Game.map.height)
					ply.selecty--;
				break;
			case "LEFT":
				ply.selectx--;
				if (ply.selectx<0)
					ply.selectx++;
				break;
			case "RIGHT":
				ply.selectx++;
				if (ply.selectx>=Game.map.width)
					ply.selectx--;
				break;
			case "SELECT":
				Game.btl.Action();
				break;
			case "CANCEL":
				Game.player.get(Game.btl.currentplayer).Cancle();
				break;
			case "START-MENU":
				new Pause(getGg(),sri);
				break;
			case "END-TURN":
				MenuHandler.CloseMenu();
				Game.btl.EndTurn();
				break;
			default: break;
		}
	}

	public static Game getGg() {
		return gg;
	}

	public void setGg(Game gg) {
		Game.gg = gg;
	}

	/**Starts a new game when launched.*/
	public static void main(String args[]) throws Exception {new Game(null);}
}
