/* Class:
 *   TTable
 * Description:
 *   A Transposition Table which stores critical information about previously seen
 *   states to save computation time.
 */
public class TTable {

	public TTableEntry[] entries;
	
	public TTable() {
		TTableEntry[] entries = new TTableEntry[256];
	}
	
}
