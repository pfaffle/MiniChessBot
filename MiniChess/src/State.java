import java.io.InputStream;
import java.io.PrintStream;
import java.util.Scanner;
import java.util.Vector;
import java.util.regex.Pattern;

/* Class:
 *   State
 * Description:
 *   Tracks the current state of the chess board, what turn it is and who is next
 *   to play. Also decides when there is a winner and who it is.
 */
public class State implements Cloneable {
	private char[][] board;  // Array grid representation of the chess board.
	private int num_rows;    // Number of rows in the chess board.
	private int num_columns; // Number of columns in the chess board.
	private int num_turns;   // Number of turns taken in the current game.
	private int max_turns;   // Maximum number of turns allowed before game end.
	private boolean white_is_next; // It is White's turn to play (True/False).
	private boolean game_is_over;
	private boolean white_wins;
	private boolean black_wins;
	
	/* Function:
	 *   State()
	 * Description:
	 *   Default constructor. Builds a fresh chess board for a new game.
	 */
	public State() {
		num_rows = 6;
		num_columns = 5;
		num_turns = 0;
		max_turns = 80;
		white_is_next = true;
		game_is_over = false;
		white_wins = false;
		black_wins = false;
		board = new char[num_columns][num_rows];
		
		/* Initialize board
		 *	      4
		 *    kqbnr 5
		 *    ppppp
		 *    .....
		 *    .....
		 *    PPPPP
		 *  0 RNBQK
		 *    0      
		 * Indexing: [x][y]
		 *           [col][row] 
		 */
		board[0][0] = 'R';
		board[1][0] = 'N';
		board[2][0] = 'B';
		board[3][0] = 'Q';
		board[4][0] = 'K';
		for (int i = 0; i < num_columns; i++) {
			board[i][1] = 'P';
		}
		for (int i = 0; i < num_columns; i++) {
			board[i][2] = '.';
			board[i][3] = '.';
		}
		for (int i = 0; i < num_columns; i++) {
			board[i][4] = 'p';
		}
		board[0][5] = 'k';
		board[1][5] = 'q';
		board[2][5] = 'b';
		board[3][5] = 'n';
		board[4][5] = 'r';
	}
	
	/* Function:
	 *   int ReadBoard(InputStream new_state)
	 * Description:
	 *   Takes a byte array representation of a chess board from an InputStream 
	 *   and uses it to construct a new board state.
	 * Return values:
	 *   0 : Success.
	 *  -1 : Cannot read new_state because it is null.
	 *  -2 : Invalid input: No turn counter, or turn counter is in the wrong location.
	 *  -3 : Invalid input: Current turn is greater than maximum allowed turns.
	 *  -4 : Invalid input: No current player turn indicator, or current player turn
	 *       indicator is in the wrong location.
	 *  -5 : Invalid input: Current player must be 'W' for White or 'B' for Black.
	 *  -6 : Invalid input: Not enough rows on the board.
	 *  -7 : Invalid input: Not enough columns in a row on the board.
	 *  -8 : Invalid input: Invalid piece on the board. 
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
		char[][] new_board = new char[num_columns][num_rows];  // New board layout.
		
		/* Begin reading and parsing input */
		
		/* Step 1. Get new turn number. */
		if (!(in.hasNextInt())) {
			in.close();
			return -2;
		}
		new_num_turns = in.nextInt();
		/* Input validation: Check that num_turns <= max_turns. */
		if (new_num_turns > max_turns) {
			in.close();
			return -3;
		}
		
		/* Step 2. Get current player's turn. */
		if (!(in.hasNext(Pattern.compile("\\w")))) {
			in.close();
			return -4;
		}
		raw_input = in.nextLine();
		/* Would be nice if this handled some arbitrary number of spaces. */
		if (raw_input.equalsIgnoreCase(" W")) {
			new_white_is_next = true;
		} else if (raw_input.equalsIgnoreCase(" B")) {
			new_white_is_next = false;
		} else {
			in.close();
			return -5;
		}
		
		/* Step 3. Parse the layout of the board. */
		char[] new_row = new char[num_columns];
		for (int cur_row = num_rows - 1; cur_row >= 0; cur_row--) {
			if (!(in.hasNextLine())) {
				in.close();
				return -6;
			}
			
			/* Step 3a. Get next row. */
			raw_input = in.nextLine();
			/* Step 3b. Verify next row has the correct number of columns. */
			if (!(raw_input.length() == num_columns)) {
				in.close();
				return -7;
			}
			/* Step 3c. Verify that all pieces in this row are valid and
			 * add them to the new board. */
			new_row = raw_input.toCharArray();
			char[] valid_pieces = {'.','K','Q','R','B','N','P'};
			int num_valid_pieces = valid_pieces.length;
			
			for (int cur_column = 0; cur_column < num_columns; cur_column++) {
				char piece = new_row[cur_column];
				boolean piece_is_valid = false;
				int cur_valid_piece = 0;
				
				/* While we haven't confirmed that the current piece is valid,
				 * iterate through the list of valid pieces and check if it is
				 * one of them. */
				while (!(piece_is_valid) && (cur_valid_piece < num_valid_pieces)) {
					if (Character.toUpperCase(piece) == valid_pieces[cur_valid_piece]) {
						piece_is_valid = true;
					} else {
						cur_valid_piece++;
					}
				}
				if (!(piece_is_valid)) {
					in.close();
					return -8;
				}
				/* Piece is valid, add it to the new board. */
				new_board[cur_column][cur_row] = piece;
			}
		}
		
		/* Step 4. All previous steps were successful, so commit new board state. */
		board = new_board;
		white_is_next = new_white_is_next;
		num_turns = new_num_turns;
		
		in.close();
		return 0;
	}
	
	/* Function:
	 *   void WriteBoard(PrintStream out)
	 * Description:
	 *   Writes a byte array representation of a chess board and the current
	 *   game state to an OutputStream. If the PrintStream argument passed in is null,
	 *   then it will print to standard out.
	 */
	public void WriteBoard(PrintStream out) {
		if (out == null) {
			out = new PrintStream(System.out);
		}
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
		for (int i = num_rows - 1; i >= 0; i--) {
			for (int j = 0; j < num_columns; j++) {
				out.write(board[j][i]);
			}
			out.write('\n');
		}
		out.flush();
	}
	
	/* Function:
	 *   void WriteBoard()
	 * Description:
	 *   Overloaded function which calls WriteBoard(PrintStream out), passing it
	 *   standard out.
	 */
	public void WriteBoard() {
		WriteBoard(System.out);
	}

	/* Function:
	 *   Vector<Move> MoveScan(Square init_position, int dx, int dy, boolean allow_capture, int max_hops)
	 * Description:
	 *   Function which generates a list of valid moves for a particular piece in
	 *   a particular direction.
	 * Arguments:
	 *   init_position : A Square object which indicates the initial position of the
	 *                   piece we wish to move.
	 *              dx : Indicator of the direction and number of spaces we wish to move along
	 *                   the x axis.
	 *              dy : Indicator of the direction and number of spaces we wish to move along
	 *                   the y axis.
	 *   allow_capture : A boolean value which indicates if we want to consider possible captures
	 *                   by the piece in the given direction.
	 *         one_hop : Only get moves one hop in the given direction.
	 * 
	 * Return values:
	 *            null : If the given square has no piece on it (represented by a '.'), or is
	 *                   not on the board, there are no valid moves and the function returns null.
	 *    Vector<Move> : A Vector object containing all valid Moves that the piece in the given
	 *                   Square can make in the given direction.
	 */
	public Vector<Move> MoveScan(Square init_position, int dx, int dy, boolean allow_capture, boolean one_hop) { 
		if (!pieceIsValid(init_position)) {
			return null;
		}
		
		int x = init_position.x;
		int y = init_position.y;		
		boolean piece_is_white = Character.isUpperCase(board[x][y]);
		boolean more_moves = true;
		Vector<Move> valid_moves = new Vector<Move>(6);
		
		/* Begin scanning in the given direction for valid moves. */
		x += dx;
		y += dy;
		while (x < num_columns && x >= 0 && y < num_rows && y >= 0 && more_moves) {
			char cur_square = board[x][y];
			if (cur_square == '.') {
				/* If nothing is in the target square, it's a valid place to move so add it. */
				valid_moves.add(new Move(init_position,new Square(x,y)));
				x += dx;
				y += dy;
			} else {
				/* If there is another piece in the square, we can only move there if
				 * we are allowed to capture it. */
				boolean target_is_white = Character.isUpperCase(cur_square);
				if (piece_is_white != target_is_white) {
					if (allow_capture) {
						valid_moves.add(new Move(init_position,new Square(x,y)));
					}
				}
				more_moves = false;
			}
			/* If this is a piece which only moves one hop per turn, stop after the
			 * first iteration. */
			if (one_hop) {
				more_moves = false;
			}
		}
		
		return valid_moves;
	}
	
	/* Function:
	 *   char getPieceAtIndex(int x, int y)
	 * Description:
	 *   Function which returns the character representation of the piece at the
	 *   given coordinates. Mostly for debugging purposes.
	 */
	public char getPieceAtIndex(int x, int y) {
		return board[x][y];
	}
	
	/* Function:
	 *   char getPieceAtSquare(Square sq)
	 * Description:
	 *   Function which returns the character representation of the piece at the
	 *   given Square. Mostly for debugging purposes.
	 */
	public char getPieceAtSquare(Square sq) {
		return board[sq.x][sq.y];
	}
	
	/* Function:
	 *   Vector<Move> getMovesForPieceAtIndex(int x, int y)
	 * Description:
	 *   Function which returns the valid moves for the piece at the
	 *   given coordinates.
	 */
	public Vector<Move> getMovesForPieceAtIndex(int x, int y) {
		if (!pieceIsValid(x,y)) {
			return null;
		}
		
		int dx;
		int dy;
		boolean allow_capture;
		boolean one_hop;
		Square sq = new Square(x,y);
		Vector<Move> moves = new Vector<Move>(6,6);
		Vector<Move> pawn_possible_caps = new Vector<Move>(2);
		
		char piece = board[x][y];
		switch(piece) {
		
		case 'K':
		case 'k':
			allow_capture = true;
			one_hop = true;
			for (dx = -1; dx <= 1; dx++) {
				for (dy = -1; dy <= 1; dy++) {
					if (!(dx == 0 && dy == 0)) {
						moves.addAll(MoveScan(sq,dx,dy,allow_capture,one_hop));
					}
				}
			}
			break;
		case 'Q':
		case 'q':
			allow_capture = true;
			one_hop = false;
			for (dx = -1; dx <= 1; dx++) {
				for (dy = -1; dy <= 1; dy++) {
					if (!(dx == 0 && dy == 0)) {
						moves.addAll(MoveScan(sq,dx,dy,allow_capture,one_hop));
					}
				}
			}
			break;
		case 'R':
		case 'r':
			allow_capture = true;
			one_hop = false;
			for (dx = -1; dx <= 1; dx++) {
				dy = 0;
				if (!(dx == 0)) {
					moves.addAll(MoveScan(sq,dx,dy,allow_capture,one_hop));
				}
			}
			for (dy = -1; dy <= 1; dy++) {
				dx = 0;
				if (!(dy == 0)) {
					moves.addAll(MoveScan(sq,dx,dy,allow_capture,one_hop));
				}
			}
			break;
		case 'B':
		case 'b':
			allow_capture = true;
			one_hop = false;
			for (dx = -1; dx <= 1; dx++) {
				for (dy = -1; dy <= 1; dy++) {
					if (!(dx == 0 || dy == 0)) {
						moves.addAll(MoveScan(sq,dx,dy,allow_capture,one_hop));
					}
				}
			}
			allow_capture = false;
			one_hop = true;
			for (dx = -1; dx <= 1; dx++) {
				dy = 0;
				if (!(dx == 0)) {
					moves.addAll(MoveScan(sq,dx,dy,allow_capture,one_hop));
				}
			}
			for (dy = -1; dy <= 1; dy++) {
				dx = 0;
				if (!(dy == 0)) {
					moves.addAll(MoveScan(sq,dx,dy,allow_capture,one_hop));
				}
			}
			break;
		case 'N':
		case 'n':
			allow_capture = true;
			one_hop = true;
			for (dx = -1; dx <= 1; dx += 2) {
				for (dy = -2; dy <= 2; dy += 4) {
					moves.addAll(MoveScan(sq,dx,dy,allow_capture,one_hop));
				}
			}
			for (dx = -2; dx <= 2; dx += 4) {
				for (dy = -1; dy <= 1; dy += 2) {
					moves.addAll(MoveScan(sq,dx,dy,allow_capture,one_hop));
				}
			}
			break;
		case 'P':
			/* Get possible forward (non-capture) movement. */
			allow_capture = false;
			one_hop = true;
			dx = 0;
			dy = 1;
			moves.addAll(MoveScan(sq,dx,dy,allow_capture,one_hop));
			/* Get possible diagonal (capture) movement. */
			allow_capture = true;
			for (dx = -1; dx <= 1; dx += 2) {
				pawn_possible_caps.addAll(MoveScan(sq,dx,dy,allow_capture,one_hop));
			}
			for (int i = 0; i < pawn_possible_caps.size(); i++) {
				Square tgt_Square = pawn_possible_caps.elementAt(i).to_Square;
				char tgt_Piece = getPieceAtSquare(tgt_Square);
				if (Character.isLowerCase(tgt_Piece)) {
					moves.add(pawn_possible_caps.elementAt(i));
				}
			}
			break;
		case 'p':
			/* Get possible forward (non-capture) movement. */
			allow_capture = false;
			one_hop = true;
			dx = 0;
			dy = -1;
			moves.addAll(MoveScan(sq,dx,dy,allow_capture,one_hop));
			/* Get possible diagonal (capture) movement. */
			allow_capture = true;
			for (dx = -1; dx <= 1; dx += 2) {
				pawn_possible_caps.addAll(MoveScan(sq,dx,dy,allow_capture,one_hop));
			}
			for (int i = 0; i < pawn_possible_caps.size(); i++) {
				Square tgt_Square = pawn_possible_caps.elementAt(i).to_Square;
				char tgt_Piece = getPieceAtSquare(tgt_Square);
				if (Character.isUpperCase(tgt_Piece)) {
					moves.add(pawn_possible_caps.elementAt(i));
				}
			}
			break;
		default:
			/* Unknown error? */
			moves = null;
			break;
			
		}
		return moves;
	}
	
	/* Function:
	 *   Vector<Move> getMovesForPieceAtSquare(Square sq)
	 * Description:
	 *   Function which returns the valid moves for the piece at the
	 *   given Square.
	 */
	public Vector<Move> getMovesforPieceAtSquare(Square sq) {
		if (sq == null) {
			return null;
		} else {
			return getMovesForPieceAtIndex(sq.x, sq.y);
		}
	}
	
	/* Function:
	 *   pieceIsValid(int x, int y)
	 * Description:
	 *   Ensures that there is a valid piece at the given coordinates.
	 */
	private boolean pieceIsValid(int x, int y) {
		/* Ensure that the square we're checking actually exists and has a piece in it. */ 
		if (x >= num_columns || x < 0) {
			return false;
		}
		if (y >= num_rows || y < 0) {
			return false;
		}
		if (!(Character.isLetter(board[x][y]))) {
			return false;
		}
		return true;
	}
	
	/* Function:
	 *   pieceIsValid(Square sq)
	 * Description:
	 *   Ensures that there is a valid piece at the given Square.
	 */
	private boolean pieceIsValid(Square sq) {
		if (sq == null) {
			return false;
		} else {
			return pieceIsValid(sq.x,sq.y);
		}
	}
	
	/* Function:
	 *   boolean pieceIsOnMove(char ch)
	 * Description:
	 *   Returns true if the color of the piece given (i.e. the case) matches
	 *   the color of the player who is next to move. Otherwise, returns false.
	 */
	private boolean pieceIsOnMove(char ch) {
		if (ch == '.') {
			return false;
		} else if (Character.isLetter(ch)) {
			if (Character.isUpperCase(ch) && white_is_next) {
				return true;
			} else if (Character.isLowerCase(ch) && !white_is_next) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}
	
	/* Function:
	 *   State executeMove(Move move)
	 * Description:
	 *   Verifies that the given move is valid to execute and then executes it,
	 *   returning the new board state.
	 */
	public State executeMove(Move move) throws Exception {
		if (move == null) {
			throw new Exception("Invalid Move.");
		}
		
		/* Check that additional moves are allowed. */
		if (num_turns > max_turns) {
			throw new Exception("Game Over");
		}
		
		Square start_square = move.from_Square;
		Square end_square = move.to_Square;
		
		/* Check that originating square has a valid piece in it. */
		if (!pieceIsValid(start_square)) {
			throw new Exception("Invalid piece.");
		}
		
		/* Check that the piece in the originating square is on move. */
		char src_piece = getPieceAtSquare(start_square);
		char tgt_piece = getPieceAtSquare(end_square);
		if (!pieceIsOnMove(src_piece)) {
			throw new Exception("Piece not on move.");
		}
		
		/* Check that the piece in the originating square can move to the target location. */
		Vector<Move> valid_moves = new Vector<Move>();
		valid_moves = getMovesforPieceAtSquare(start_square);
		if (!valid_moves.contains(move)) {
			for (int i = 0; i < valid_moves.size(); i++) {
				System.out.println(valid_moves.elementAt(i));
			}
			throw new Exception("Move not allowed for given piece.");
		}
		
		
		
		/* Generate new state to return. */
		int from_x = move.from_Square.x;
		int from_y = move.from_Square.y;
		int to_x = move.to_Square.x;
		int to_y = move.to_Square.y;
		
		/* Check for pawn promotion. */
		if (src_piece == 'p' && to_y == 0) {
			src_piece = 'q';
		} else if (src_piece == 'P' && to_y == num_rows - 1) {
			src_piece = 'Q';
		}
		
		State new_gamestate = this.clone(); 
		new_gamestate.board[to_x][to_y] = src_piece;
		new_gamestate.board[from_x][from_y] = '.';
		new_gamestate.num_turns += 1;
		new_gamestate.white_is_next = !new_gamestate.white_is_next;
		
		/* Check for victory/draw. */
		/* Draw condition: Too many moves. */ 
		if (new_gamestate.num_turns > new_gamestate.max_turns) {
			new_gamestate.game_is_over = true;
			new_gamestate.white_wins = false;
			new_gamestate.black_wins = false;
		}
		
		/* Victory condition: Captured opposing king. */
		if (tgt_piece == 'k') {
			new_gamestate.game_is_over = true;
			new_gamestate.white_wins = true;
			new_gamestate.black_wins = false;
		} else if (tgt_piece == 'K') {
			new_gamestate.game_is_over = true;
			new_gamestate.white_wins = false;
			new_gamestate.black_wins = true;
		}

		return new_gamestate;
	}
	
	/* Function:
	 *   humanMove(String move)
	 * Description:
	 *   Takes a String in the form "a0-b1" and attempts to execute it as a move.
	 *   Columns (x) are parsed as A-E (0-4), and rows (y) are parsed as 0-5.
	 */
	public State humanMove(String rawmove) throws Exception {
		if (rawmove == null) {
			throw new Exception("Invalid Move.");
		}
		if (!rawmove.matches("\\w\\d-\\w\\d")) {
			throw new Exception("Improperly formatted move.");
		}
		String[] move = rawmove.split("-");
		
		String fromCol = move[0].substring(0,1).toUpperCase();
		String toCol = move[1].substring(0,1).toUpperCase();
		
		int from_x;
		int to_x;
		int from_y = Integer.parseInt(move[0].substring(1,2));
		int to_y = Integer.parseInt(move[1].substring(1,2));
		
		switch(fromCol) {
		case "A":
			from_x = 0;
			break;
		case "B":
			from_x = 1;
			break;
		case "C":
			from_x = 2;
			break;
		case "D":
			from_x = 3;
			break;
		case "E":
			from_x = 4;
			break;
		default:
			throw new Exception("Failed to parse column letter.");
		}
		
		switch(toCol) {
		case "A":
			to_x = 0;
			break;
		case "B":
			to_x = 1;
			break;
		case "C":
			to_x = 2;
			break;
		case "D":
			to_x = 3;
			break;
		case "E":
			to_x = 4;
			break;
		default:
			throw new Exception("Failed to parse row letter.");
		}
		Move humansMove = new Move(new Square(from_x, from_y), new Square(to_x, to_y));
		return executeMove(humansMove);
	}
	
	/* Function:
	 * (non-Javadoc)
	 * @see java.lang.Object#clone()
	 */
	public State clone() {
		State newState = new State();
		newState.num_rows = this.num_rows;
		newState.num_columns = this.num_columns;
		newState.num_turns = this.num_turns;
		newState.max_turns = this.max_turns;
		newState.white_is_next = this.white_is_next;
		newState.game_is_over = this.game_is_over;
		newState.white_wins = this.white_wins;
		newState.black_wins = this.black_wins;
		newState.board = new char[newState.num_columns][newState.num_rows];
		
		for (int i = 0; i < num_columns; i++) {
			for (int j = 0; j < num_rows; j++) {
				newState.board[i][j] = this.board[i][j];
			}
		}
		
		return newState;
	}
	
	/* Function:
	 *   GameOver()
	 * Description:
	 *   Returns true if one of the game's victory conditions has been met.
	 */
	public boolean GameOver() {
		return game_is_over;
		/* Also need to scan for if there are any valid moves to be made by the current player. */
	}
	
	/* Function:
	 *   WhiteWins()
	 * Description:
	 *   Returns true if white is the winner.
	 */
	public boolean WhiteWins() {
		return white_wins;
	}
	
	/* Function:
	 *   BlackWins()
	 * Description:
	 *   Returns true if black is the winner.
	 */
	public boolean BlackWins() {
		return black_wins;
	}
	
}
