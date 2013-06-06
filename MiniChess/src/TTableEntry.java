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
	public int v;
	public int d;
	public boolean valid;
	
	public TTableEntry() {
		hash = 0;
		a = 0;
		b = 0;
		v = 0;
		d = 0;
		valid = false;
	}
	public TTableEntry(long newH, int newA, int newB, int newV, int newD) {
		hash = newH;
		a = newA;
		b = newB;
		v = newV;
		d = newD;
		valid = true;
	}
}
