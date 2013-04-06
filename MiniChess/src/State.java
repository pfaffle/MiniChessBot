
public class State {
	private char[][] board;
	private int num_rows;
	private int num_columns;
	private int num_turns;
	private int max_turns;
	private boolean game_over;
	private boolean white_wins;
	private boolean white_is_next;
	
	public State() {
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
	
	public void ShowBoard() {
		for (int i = 0; i < num_rows; i++) {
			for (int j = 0; j < num_columns; j++) {
				System.out.print(board[i][j]);
			}
			System.out.println();
		}
	}
}
