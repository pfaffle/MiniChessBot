/*import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Vector;*/
import java.io.IOException;
import java.util.Scanner;

public class MiniChessPlayer {

	public static String server = "imcs.svcs.cs.pdx.edu";
	public static String port = "3589";
	public static String user = "pfaffle";
	public static String pass = "foo";
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		//playSmartVsHuman();
		//playSmartVsSmart();
		//playRandomVsHuman();
		//playRandomVsRandom();
		playSmartVsImcs();
		
		
	}
	
	public static void playSmartVsImcs() {
		System.out.println("Connecting to server.");
		State gamestate = new State();
		
		try {
			Client connection = new Client(server,port,user,pass);
			connection.offer('B');
			
			while (!gamestate.gameOver()) {
				String opp_move = connection.getMove();
				System.out.println("White moves: " + opp_move);
				gamestate = gamestate.makeImcsMove(opp_move);
				//gamestate.writeBoard();
				String my_move = gamestate.getImcsMove();
				System.out.println("My move: " + my_move);
				gamestate = gamestate.makeImcsMove(my_move);
				connection.sendMove(my_move);
				gamestate.writeBoard();
			}
			connection.close();
		} catch (Exception e) {
			System.out.println("Failed to connect to server.");
			e.printStackTrace();
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
}