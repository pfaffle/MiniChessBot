/* Class:
 *   Square
 * Description:
 *   Data structure which stores the x and y coordinates
 *   of a particular square on the MiniChess board.
 */
public class Square {
	public int x;
	public int y;
	
	public Square() {
		x = y = 0;
	}
	public Square(int newx, int newy) {
		x = newx;
		y = newy;
	}
	public String toString() {
		String col;
		String row;
		switch (x) {
		case 0:
			col = "a";
			break;
		case 1:
			col = "b";
			break;
		case 2:
			col = "c";
			break;
		case 3:
			col = "d";
			break;
		case 4:
			col = "e";
			break;
		default:
			col = "?";
			break;
		}
		row = Integer.toString(y + 1);
		return col + row;
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
		Square othersquare = (Square)o;
		if (othersquare.x != this.x) {
			return false;
		} else if (othersquare.y != this.y){
			return false;
		}
		return true;
	}
}
