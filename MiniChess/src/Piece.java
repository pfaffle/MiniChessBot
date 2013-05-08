/* Class:
 *   Piece
 * Description:
 *   Has basic functions for testing/manipulating MiniChess pieces to make
 *   them simpler to work with.
 */
public class Piece {
		public char piece_ch;          // The character representation of this piece.
		public Square position;  // The square that this piece resides on.
		
		public Piece() {
			piece_ch = 'P';
			position = new Square(0,1);
		}
		
		public Piece(char newpiece, Square newpos) {
			piece_ch = newpiece;
			position = new Square(newpos.x, newpos.y);
		}
		
		public Piece(char newpiece, int x, int y) {
			piece_ch = newpiece;
			position = new Square(x,y);
		}
		
		/* Function:
		 *   isDeveloped
		 * Description:
		 *   Returns true if this piece is off of its home square.
		 * Inputs:
		 *   None.
		 * Return values:
		 *    True : Returned if this piece is off of the square it starts in in a new game of MiniChess.
		 *   False : Returned if this piece is in the same square it starts in in a new game of MiniChess.
		 */
		public boolean isDeveloped() {
			switch(piece_ch) {
			case 'K':
				return !(position.x == 4 && position.y == 0); // x=4 y=0
			case 'Q':
				return !(position.x == 3 && position.y == 0); // x=3 y=0
			case 'B':
				return !(position.x == 2 && position.y == 0); // x=2 y=0
			case 'N':
				return !(position.x == 1 && position.y == 0); // x=1 y=0
			case 'R':
				return !(position.x == 0 && position.y == 0); // x=0 y=0
			case 'k':
				return !(position.x == 0 && position.y == 5); // x=0 y=5
			case 'q':
				return !(position.x == 1 && position.y == 5); // x=1 y=5
			case 'b':
				return !(position.x == 2 && position.y == 5); // x=2 y=5
			case 'n':
				return !(position.x == 3 && position.y == 5); // x=3 y=5
			case 'r':
				return !(position.x == 4 && position.y == 5); // x=4 y=5
			default:
				return false;
			}
		}
		
		/* Function:
		 *   isWhite
		 * Description:
		 *   Returns true if the given piece is White, false if not.
		 * Inputs:
		 *   ch : The character representation of the given piece.
		 * Return values:
		 *    True : Returned if the piece in the given square is White (upper case).
		 *   False : Returned if the piece in the given square is Black (lower case) or the square is empty.
		 */
		public static boolean isWhite(char ch) {
			return Character.isUpperCase(ch);
		}
		
		/* Function:
		 *   isWhite
		 * Description:
		 *   Returns true if this piece is White, false if not.
		 * Inputs:
		 *   None.
		 * Return values:
		 *    True : Returned if this piece is White (upper case).
		 *   False : Returned if this piece is Black (lower case).
		 */
		public boolean isWhite() {
			return Character.isUpperCase(piece_ch);
		}
		
		/* Function:
		 *   isBlack
		 * Description:
		 *   Returns true if the given piece is Black, false if not.
		 * Inputs:
		 *   ch : The character representation of the given piece.
		 * Return values:
		 *    True : Returned if the piece in the given square is Black (lower case).
		 *   False : Returned if the piece in the given square is White (upper case) or the square is empty.
		 */
		public static boolean isBlack(char ch) {
			return Character.isLowerCase(ch);
		}
		
		/* Function:
		 *   isBlack
		 * Description:
		 *   Returns true if this piece is Black, false if not.
		 * Inputs:
		 *   None.
		 * Return values:
		 *    True : Returned if this piece is Black (lower case).
		 *   False : Returned if this piece is White (upper case).
		 */
		public boolean isBlack() {
			return Character.isLowerCase(piece_ch);
		}
}
