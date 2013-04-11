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
		String str = "(" + x + "," + y + ")";
		return str;
	}
}
