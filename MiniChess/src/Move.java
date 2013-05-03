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
		String str = from_Square + "-" + to_Square;
		return str;
	}
	/* Had some trouble with Vector.contains() working as expected, so to fix it I've
	 * borrowed some suggested code for overriding the default .equals() function from:
	 * http://stackoverflow.com/questions/588503/java-removing-a-custom-object-from-a-vector
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object o) {
		if (o == null || o.getClass() != this.getClass()) {
			return false;
		}
		Move othermove = (Move)o;
		if (!othermove.from_Square.equals(this.from_Square)) {
			return false;
		} else if (!othermove.to_Square.equals(this.to_Square)) {
			return false;
		}
		return true;
	}
}
