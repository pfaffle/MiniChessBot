/* Class:
 *   Game
 * Description:
 *   Data structure which stores information about a game offered
 *   by the ICMS server, which the ICMS client can accept.
 */
public class Game {
	public int id;
	public char color;
	
	public Game() {
		id = 0;
		color = 'W';
	}
	public Game(int newid, char newcolor) {
		id = newid;
		color = newcolor;
	}
}
