import java.io.IOException;
import java.util.Random;
import java.util.Scanner;
import java.util.Vector;

public class MiniChessPlayer {

	public static String server = "imcs.svcs.cs.pdx.edu";
	public static String port = "3589";
	public static String user = "ser_rodrik_castle";
	public static String pass = "foobar";
	public static Client connection = null;
	public static Scanner in = new Scanner(System.in);
	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		Game selectedGame = mainMenu();
		if (selectedGame != null) {
			if (selectedGame.id == -1) {
				// Play vs. self.
				playVsSelf();
			} else if (selectedGame.id == -2) {
				// Play vs. human.
				playVsHuman(selectedGame);
			} else {
				// Play on IMCS.
				playVsImcs(selectedGame);
			}
		}
		in.close();
	}
	
	public static Game mainMenu() throws IOException {
		Vector<Game> availableGames = null;
		char opponentColor = '?';
		String myOpponent = "unknown";
		Game selectedGame = null;
		System.out.println("a : accept an offered IMCS game.");
		System.out.println("o : offer an IMCS game.");
		System.out.println("r : start or accept a random IMCS game.");
		System.out.println("s : play against self.");
		System.out.println("h : play against human.");
		System.out.println("q : quit.");
		System.out.print("Choose an action:");
		String input = in.nextLine();
		
		switch(input) {
		case "a":
			// connect to server
			connection = new Client(server,port,user,pass);
			// prompt for game id
			availableGames = connection.list();
			System.out.print("Enter id of game to join: ");
			input = in.nextLine();
			int gameId;
			try {
				gameId = Integer.parseInt(input);
			} catch (NumberFormatException e) {
				System.out.println("Invalid game id.");
				break;
			}
			if (gameId < 1) {
				System.out.println("Invalid game id.");
				break;
			}
			for (int i = 0; i < availableGames.size(); i++) {
				 if (availableGames.elementAt(i).id == gameId) {
					 myOpponent = availableGames.elementAt(i).opponent;
					 opponentColor = availableGames.elementAt(i).color;
				 }
			}
			if (opponentColor == '?') {
				System.out.print("Enter side to play (? for auto): ");
				input = in.nextLine();
				char myColor = input.toUpperCase().charAt(0);
				if (myColor == 'W') {
					opponentColor = 'B';
				} else if (myColor == 'B') {
					opponentColor = 'W';
				} else if (myColor == '?') {
					// Do nothing.
				} else {
					System.out.println("Invalid color selection.");
					break;
				}
			}
			selectedGame = new Game(gameId, opponentColor, myOpponent);
			break;
		case "o":
			// connect to server
			connection = new Client(server,port,user,pass);
			// start a new game, show game id.
			selectedGame = new Game();
			break;
		case "r":
			// connect to server
			connection = new Client(server,port,user,pass);
			// auto-accept or offer
			availableGames = connection.list();
			if (availableGames.size() == 0) {
				// No game offers currently available to accept. Create one!
				System.out.println("No games available to join. Offering new game.");
				selectedGame = new Game();
			} else {
				// Find and accept one of the existing game offers.
				Random generator = new Random();
				int randomIndex = generator.nextInt(availableGames.size());
				selectedGame = availableGames.elementAt(randomIndex);
			}
			break;
		case "s":
			selectedGame = new Game(-1,'?',"self");
			break;
		case "h":
			System.out.print("Enter your side (? for auto): ");
			input = in.nextLine();
			opponentColor = input.toUpperCase().charAt(0);
			if (opponentColor != 'W' && opponentColor != 'B' && opponentColor != '?') {
				System.out.println("Invalid color selection.");
				break;
			}
			selectedGame = new Game(-2,opponentColor,"human");
			break;
		case "q":
			break;
		default:
			// error, quit.
			System.out.println("Invalid input.");
			break;
		}
		
		return selectedGame;
	}
	
	public static void playVsImcs(Game selectedGame) throws Exception {
		State gamestate = new State();
		char myColor;
		String gameId = String.valueOf(selectedGame.id);
		String opponent = selectedGame.opponent;
		if (gameId.equals("0")) {
			// offer new game.
			if (selectedGame.color == 'B') {
				myColor = connection.offer('W');
			} else if (selectedGame.color == 'W') {
				myColor = connection.offer('B');
			} else {
				myColor = connection.offer('?');
			}
		} else {
			if (selectedGame.color == 'B') {
				myColor = connection.accept(gameId,'W');
			} else if (selectedGame.color == 'W') {
				myColor = connection.accept(gameId,'B');
			} else {
				myColor = connection.accept(gameId,'?');
			}
		}
		
		System.out.println("My opponent: " + opponent + ".");
		if (myColor == 'W') {
			System.out.println("I am White!");
		} else {
			System.out.println("I am Black!");
		}
		
		// play game	
		gamestate.writeBoard();
		while (!gamestate.gameOver()) {
			if (myColor == 'W') {
				if (gamestate.whiteOnMove()) {
					// make a move
					String myMove = gamestate.getImcsMove();
					System.out.println("My move: " + myMove);
					gamestate = gamestate.makeImcsMove(myMove);
					connection.sendMove(myMove);
					//gamestate.writeBoard();
				} else {
					// wait for opponent's move.
					String oppMove = connection.getMove();
					System.out.println("Black moves: " + oppMove);
					gamestate = gamestate.makeImcsMove(oppMove);
					//gamestate.writeBoard();
				}
			} else {
				if (gamestate.blackOnMove()) {
					// make a move
					String myMove = gamestate.getImcsMove();
					System.out.println("My move: " + myMove);
					gamestate = gamestate.makeImcsMove(myMove);
					connection.sendMove(myMove);
					//gamestate.writeBoard();
				} else {
					// wait for opponent's move.
					String oppMove = connection.getMove();
					System.out.println("White moves: " + oppMove);
					gamestate = gamestate.makeImcsMove(oppMove);
					//gamestate.writeBoard();
				}	
			}
			gamestate.writeBoard();
		}
		connection.close();
		System.out.println("Game over!");
		if (gamestate.whiteWins()) {
			if (myColor == 'W')
				System.out.println("I win!");
			else
				System.out.println("I lose!");
		} else if (gamestate.blackWins()) {
			if (myColor == 'B')
				System.out.println("I win!");
			else
				System.out.println("I lose!");
		} else {
			System.out.println("Game is a draw.");
		}
	}
	
	public static void playVsHuman(Game selectedGame) {
		State gamestate = new State();
		char myColor = 'B';
		if (selectedGame.color == 'B') {
			myColor = 'W';
			System.out.println("You are Black!");
		} else {
			System.out.println("You are White!");
		}
		
		// Play the game.
		gamestate.writeBoard();
		while (!gamestate.gameOver()) {
			if (gamestate.whiteOnMove()) {
				System.out.print("Please enter a move in the form A1-B2: ");
				String input = in.nextLine();
				try {
					gamestate = gamestate.makeImcsMove(input);
				} catch (IllegalArgumentException e) {
					System.out.println("Invalid move.");
				} catch (Exception e) {
					e.printStackTrace();
					break;
				}
			} else {
				System.out.println("Black moves...");
				try {
					String move = gamestate.getImcsMove(); 
					gamestate = gamestate.makeImcsMove(move);
				} catch (Exception e) {
					e.printStackTrace();
					break;
				}
			}
			gamestate.writeBoard();
		}
		System.out.println("Game over!");
		if (gamestate.whiteWins()) {
			System.out.println("White wins!");
		} else if (gamestate.blackWins()) {
			System.out.println("Black wins!");
		} else {
			System.out.println("Game is a draw.");
		}
	}
	
	public static void playVsSelf() {
		State gamestate = new State();
		gamestate.writeBoard();
		// Play the game.
		while (!gamestate.gameOver()) {
			if (gamestate.whiteOnMove()) {
				System.out.println("White moves...");
				try {
					gamestate = gamestate.makeSmartMove();
				} catch (Exception e) {
					e.printStackTrace();
					break;
				}
			} else {
				System.out.println("Black moves...");
				try {
					gamestate = gamestate.makeSmartMove();
				} catch (Exception e) {
					e.printStackTrace();
					break;
				}
			}
			gamestate.writeBoard();
		}
		System.out.println("Game over!");
		if (gamestate.whiteWins()) {
			System.out.println("White wins!");
		} else if (gamestate.blackWins()) {
			System.out.println("Black wins!");
		} else {
			System.out.println("Game is a draw.");
		}
	}
}

