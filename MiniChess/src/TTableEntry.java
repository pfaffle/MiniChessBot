/* Class:
 *   TTableEntry
 * Description:
 *   An entry in the Transposition Table data structure which stores information
 *   about one previously-seen state.
 */
public class TTableEntry {
	public long hash;
	public int a;
	public int b;
	public int value;
	public boolean valid;
	
	public TTableEntry() {
		hash = 0;
		a = 0;
		b = 0;
		value = 0;
		valid = false;
	}
}
