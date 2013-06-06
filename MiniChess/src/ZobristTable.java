import java.util.Random;

public class ZobristTable {
	private int numSquares;
	private int numPieces;
	private long[][] entries;
	private Random rnd;
	
	/* Initializes the Zobrist Table with random long
	 * integer values. */
	public ZobristTable() {
		numSquares = 30;
		numPieces = 13;
		entries = new long[numSquares][numPieces];
		rnd = new Random();
		for (int i = 0; i < numSquares; i++) {
			for (int j = 0; j < numPieces; j++) {
				entries[i][j] = rnd.nextLong();
			}
		}
	}
	
	/* Gets the entry in the hash table that corresponds to
	 * the given piece and square combination. */
	public long getHash(Square sq, char piece) {
		int pieceIndex = getIndexForPiece(piece);
		int squareIndex = getIndexForSquare(sq);
		return entries[squareIndex][pieceIndex];
	}
	
	/* Gets the index in the table for the given square. */
	private int getIndexForSquare(Square sq) {
		return (5 * sq.y) + sq.x;
	}
	
	/* Gets the index in the table for the given piece. */
	private int getIndexForPiece(char piece) {
		switch(piece) {
		case 'P':
			return 0;
		case 'R':
			return 1;
		case 'N':
			return 2;
		case 'B':
			return 3;
		case 'Q':
			return 4;
		case 'K':
			return 5;
		case 'p':
			return 6;
		case 'r':
			return 7;
		case 'n':
			return 8;
		case 'b':
			return 9;
		case 'q':
			return 10;
		case 'k':
			return 11;
		case '.':
			return 12;
		default:
			return -1;
		}
	}
}
