/* Class:
 *   Move
 * Description:
 *   Data structure which stores the originating square and
 *   target square for a piece that a player wants to move.
 */
public class Move {
	public Square from_Square;
	public Square to_Square;
	
	public Move() {
		from_Square = new Square();
		to_Square = new Square();
	}
	public Move(Square newfrom, Square newto) {
		from_Square = newfrom;
		to_Square = newto;
	}
	public String toString() {
		String str = from_Square + " -> " + to_Square;
		return str;
	}
}
