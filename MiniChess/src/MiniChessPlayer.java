import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Random;
import java.util.Scanner;
import java.util.Vector;

public class MiniChessPlayer {

	public static String server = "imcs.svcs.cs.pdx.edu";
	public static String port = "3589";
	public static String user = "ser_rodrik_castle";
	public static String pass = "foobar";
	
	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		Client connection = new Client(server,port,user,pass);
		Game selectedGame = mainMenu(connection);
		System.out.println("Selected game: " + selectedGame);
		//playSmartVsHuman();
		//playSmartVsSmart();
		//playRandomVsHuman();
		//playRandomVsRandom();
		//playSmartVsImcs();
		//testMoveGen();
	}
	
	public static long nextLong(Random rng, long n) {
	   // error checking and 2^x checking removed for simplicity.
	   long bits, val;
	   do {
	      bits = (rng.nextLong() << 1) >>> 1;
	      val = bits % n;
	   } while (bits-val+(n-1) < 0L);
	   return val;
	}
	
	public static Game mainMenu(Client connection) throws IOException {
		Vector<Game> availableGames = null;
		Scanner scan = new Scanner(System.in);
		char opponentColor = '?';
		String myOpponent = "unknown";
		Game selectedGame = null;
		System.out.println("a : accept an existing game offer.");
		System.out.println("o : offer a game.");
		System.out.println("r : start or accept a random game.");
		System.out.println("s : play against self.");
		System.out.println("h : play against human.");
		System.out.println("q : quit.");
		System.out.print("Choose an action:");
		String input = scan.nextLine();

		switch(input) {
		case "a":
			// prompt for game id
			availableGames = connection.list();
			System.out.print("Enter id of game to join: ");
			input = scan.nextLine();
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
				input = scan.nextLine();
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
			// start a new game, show game id.
			selectedGame = new Game();
			break;
		case "r":
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
			input = scan.nextLine();
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
		scan.close();
		
		return selectedGame;
	}
	
	public static void playSmartVsImcs() {
		State gamestate = new State();
		char my_color;
		
		System.out.println("Connecting to server.");
		try {
			Client connection = new Client(server,port,user,pass);
			Scanner scan = new Scanner(System.in);
			System.out.println("a : accept an existing game offer.");
			System.out.println("o : offer a game.");
			System.out.println("r : start or accept a random game.");
			System.out.print("Choose an action:");
			String input = scan.nextLine();
			String game_id = null;
			String my_opponent = null;
			Game selected_game = null;
			Vector<Game> available_games = connection.list();
			
			switch(input) {
			case "a":
				// prompt for game id
				System.out.print("Enter game id: ");
				input = scan.nextLine();
				int game_idnum = Integer.parseInt(input);
				selected_game = new Game(game_idnum,'?',"unknown");
				break;
			case "o":
				// start a new game, show game id.
				break;
			case "r":
				// auto-accept or offer
				if (available_games.size() == 0) {
					// No game offers currently available to accept. Create one!
					my_color = connection.offer('?');
				} else {
					// Find and accept one of the existing game offers.
					Random generator = new Random();
					int randomIndex = generator.nextInt(available_games.size());
					selected_game = available_games.elementAt(randomIndex);
				}
				break;
			default:
				// error, quit.
			}
			
			game_id = String.valueOf(selected_game.id);
			my_opponent = selected_game.opponent;
			if (selected_game.color == 'B') {
				my_color = connection.accept(game_id,'W');
			} else {
				my_color = connection.accept(game_id,'?');
			}
			System.out.println("My opponent: " + my_opponent + ".");
			
			
			if (my_color == 'W') {
				System.out.println("I am White!");
			} else {
				System.out.println("I am Black!");
			}
			
			gamestate.writeBoard();
			while (!gamestate.gameOver()) {
				if (my_color == 'W') {
					if (gamestate.whiteOnMove()) {
						// make a move
						String my_move = gamestate.getImcsMove();
						System.out.println("My move: " + my_move);
						gamestate = gamestate.makeImcsMove(my_move);
						connection.sendMove(my_move);
						//gamestate.writeBoard();
					} else {
						// wait for opponent's move.
						String opp_move = connection.getMove();
						System.out.println("Black moves: " + opp_move);
						gamestate = gamestate.makeImcsMove(opp_move);
						//gamestate.writeBoard();
					}
				} else {
					if (gamestate.blackOnMove()) {
						// make a move
						String my_move = gamestate.getImcsMove();
						System.out.println("My move: " + my_move);
						gamestate = gamestate.makeImcsMove(my_move);
						connection.sendMove(my_move);
						//gamestate.writeBoard();
					} else {
						// wait for opponent's move.
						String opp_move = connection.getMove();
						System.out.println("White moves: " + opp_move);
						gamestate = gamestate.makeImcsMove(opp_move);
						//gamestate.writeBoard();
					}	
				}
				gamestate.writeBoard();
			}
			connection.close();
			System.out.println("Game over!");
			if (gamestate.whiteWins()) {
				if (my_color == 'W')
					System.out.println("I win!");
				else
					System.out.println("I lose!");
			} else if (gamestate.blackWins()) {
				if (my_color == 'B')
					System.out.println("I win!");
				else
					System.out.println("I lose!");
			} else {
				System.out.println("Game is a draw.");
			}
		} catch (Exception e) {
			System.out.println("An error occurred!");
			e.printStackTrace();
		}
	}
	
	public static void playRandomVsHuman() {
		State gamestate = new State();
		Scanner scan = new Scanner(System.in);
		gamestate.writeBoard();
		// Play the game.
		while (!gamestate.gameOver()) {
			if (gamestate.whiteOnMove()) {
				System.out.print("Please enter a move in the form A1-B2: ");
				String in = scan.nextLine();
				try {
					gamestate = gamestate.makeHumanMove(in);
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				System.out.println("Black moves...");
				try {
					gamestate = gamestate.makeRandomGoodMove();
				} catch (Exception e) {
					e.printStackTrace();
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
		scan.close();
	}
	
	public static void playRandomVsRandom() {
		State gamestate = new State();
		gamestate.writeBoard();
		// Play the game.
		while (!gamestate.gameOver()) {
			if (gamestate.whiteOnMove()) {
				System.out.println("White moves...");
				try {
					gamestate = gamestate.makeRandomGoodMove();
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				System.out.println("Black moves...");
				try {
					gamestate = gamestate.makeRandomGoodMove();
				} catch (Exception e) {
					e.printStackTrace();
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
	
	public static void playSmartVsHuman() {
		State gamestate = new State();
		Scanner scan = new Scanner(System.in);
		gamestate.writeBoard();
		// Play the game.
		while (!gamestate.gameOver()) {
			if (gamestate.whiteOnMove()) {
				System.out.print("Please enter a move in the form A1-B2: ");
				String in = scan.nextLine();
				try {
					gamestate = gamestate.makeImcsMove(in);
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				System.out.println("Black moves...");
				try {
					String move = gamestate.getImcsMove(); 
					gamestate = gamestate.makeImcsMove(move);
				} catch (Exception e) {
					e.printStackTrace();
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
		scan.close();
	}
	
	public static void playSmartVsSmart() {
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
				}
			} else {
				System.out.println("Black moves...");
				try {
					gamestate = gamestate.makeSmartMove();
				} catch (Exception e) {
					e.printStackTrace();
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
	
	public static void testMoveGen() {
		//Print board starting state.
		State s = new State();
		
		String test_file_path;
		File test_file;
		FileInputStream test_stream;
		test_file_path = ".\\tests\\board\\endgame.txt";
		test_file = new File(test_file_path);
		try {
			test_stream = new FileInputStream(test_file);
			s.readBoard(test_stream);
			test_stream.close();
		} catch (FileNotFoundException e) {
		} catch (IOException e) {
			e.printStackTrace();
		}
		s.writeBoard();
		Vector<Move> possibleMoves = s.getAllValidMoves();
		System.out.println("Possible moves:");
		for (int i = 0; i < possibleMoves.size(); i++) {
			System.out.println(possibleMoves.elementAt(i));			
		}
		
		//Print ending starting state.
	}
}

