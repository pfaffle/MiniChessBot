
public class State {
	private char[][] board;
	private int num_rows = 6;
	private int num_columns = 5;
	
	public State() {
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
}
