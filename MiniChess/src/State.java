import java.io.InputStream;
import java.io.PrintStream;
import java.util.Scanner;

/* Class:
 *   State
 * Description:
 *   Tracks the current state of the chess board, what turn it is and who is next
 *   to play. Also decides when there is a winner and who it is.
 */
public class State {
	private char[][] board;  // Array grid representation of the chess board.
	private int num_rows;    // Number of rows in the chess board.
	private int num_columns; // Number of columns in the chess board.
	private int num_turns;   // Number of turns taken in the current game.
	private int max_turns;   // Maximum number of turns allowed before game end.
	private boolean game_over;     // Game is over (True/False).
	private boolean white_wins;    // White won the game (True/False).
	private boolean white_is_next; // It is White's turn to play (True/False).
	private PrintStream out;
	
	/* Function:
	 *   State()
	 * Description:
	 *   Default constructor. Builds a fresh chess board for a new game.
	 */
	public State() {
		out = new PrintStream(System.out);
		num_rows = 6;
		num_columns = 5;
		num_turns = 0;
		max_turns = 40;
		white_is_next = true;
		game_over = false;
		white_wins = false;
		board = new char[num_rows][num_columns];
		
		/* Initialize board */
		/*	  0
			0 kqbnr
			  ppppp
			  .....
			  .....
			  PPPPP
			  RNBQK 5
		 	      4  */
		board[0][0] = 'k';
		board[0][1] = 'q';
		board[0][2] = 'b';
		board[0][3] = 'n';
		board[0][4] = 'r';
		for (int i = 0; i < num_columns; i++) {
			board[1][i] = 'p';
		}
		for (int i = 0; i < num_columns; i++) {
			board[2][i] = '.';
			board[3][i] = '.';
		}
		for (int i = 0; i < num_columns; i++) {
			board[4][i] = 'P';
		}
		board[5][0] = 'R';
		board[5][1] = 'N';
		board[5][2] = 'B';
		board[5][3] = 'Q';
		board[5][4] = 'K';
	}
	
	/* Function:
	 *   void ShowBoard()
	 * Description:
	 *   Prints the current layout of the board to stdout.
	 */
	public void ShowBoard() {
		System.out.print(num_turns);
		if (white_is_next) {
			System.out.println(" W");
		} else {
			System.out.println(" B");
		}
		for (int i = 0; i < num_rows; i++) {
			for (int j = 0; j < num_columns; j++) {
				System.out.print(board[i][j]);
			}
			System.out.println();
		}
	}
	
	/* Function:
	 *   int ReadBoard(InputStream new_state)
	 * Description:
	 *   Takes a byte array representation of a chess board from an InputStream 
	 *   and uses it to construct a new board state.
	 */
	public int ReadBoard(InputStream new_state) {
		if (new_state == null) {
			return -1;
		}
		/* Initialize local variables */
		Scanner in = new Scanner(new_state); // Input stream Scanner.
		int new_num_turns;                   // New number of turns taken this game.
		boolean new_white_is_next;           // New value for white_is_next (i.e. next to play).
		String raw_input;                    // String for holding/parsing input.
		char[][] new_board = new char[num_rows][num_columns];  // New board layout.
		
		/* Begin reading and parsing input */
		
		/* Step 1. Get new turn number. */
		if (!(in.hasNextInt())) {
			in.close();
			return -2;
		}
		new_num_turns = in.nextInt();
		/* Input validation: Check that num_turns <= max_turns. */
		if (!(new_num_turns <= max_turns)) {
			in.close();
			return -3;
		}
		
		/* Step 2. Get current player's turn. */
		if (!(in.hasNextLine())) {
			in.close();
			return -4;
		}
		raw_input = in.nextLine();
		if (raw_input.equalsIgnoreCase("W")) {
			new_white_is_next = true;
		} else if (raw_input.equalsIgnoreCase("B")) {
			new_white_is_next = false;
		} else {
			in.close();
			return -5;
		}
		
		/* Step 3. Parse the layout of the board. */
		for (int i = 0; i < num_rows; i++) {
			if (!(in.hasNextLine())) {
				in.close();
				return -6;
			}
			raw_input = in.nextLine();
			if (!(raw_input.length() == num_columns)) {
				in.close();
				return -6;
			}
		}
		
		in.close();
		return 0;
	}
	
	/* Function:
	 *   String WriteBoard()
	 * Description:
	 *   Writes a byte array representation of a chess board and the current
	 *   game state to an OutputStream.
	 */
	public void WriteBoard() {
		String str_num_turns = Integer.toString(num_turns);
		char[] char_num_turns = str_num_turns.toCharArray();
		for (int i = 0; i < char_num_turns.length; i++) {
			out.write(char_num_turns[i]);
		}
		out.write(' ');
		if (white_is_next) {
			out.write('W');
		} else {
			out.write('B');
		}
		out.write('\n');
		for (int i = 0; i < num_rows; i++) {
			for (int j = 0; j < num_columns; j++) {
				out.write(board[i][j]);
			}
			out.write('\n');
		}
		out.flush();
	}
}
