/* Class:
 *   Piece
 * Description:
 *   Has basic functions for testing/manipulating MiniChess pieces to make
 *   them simpler to work with.
 */
public class Piece {

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
}
