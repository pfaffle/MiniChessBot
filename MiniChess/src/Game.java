/* Class:
 *   Game
 * Description:
 *   Data structure which stores information about a game offered
 *   by the ICMS server, which the ICMS client can accept.
 */
public class Game {
	public int id;
	public char color;
	public String opponent;
	
	public Game() {
		id = 0;
		color = '?';
		opponent = "";
	}
	public Game(int newid, char newcolor, String newopponent) {
		id = newid;
		color = newcolor;
		opponent = newopponent;
	}
	public String toString() {
		String str = "(" + id + "," + color + "," + opponent + ")";
		return str;
	}
}
