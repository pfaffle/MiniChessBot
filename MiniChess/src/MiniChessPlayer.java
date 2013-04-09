import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Scanner;

public class MiniChessPlayer {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		State gamestate = new State();
		
		// Print board starting state.
		System.out.println("MiniChess board starting state:");
		gamestate.WriteBoard();

		// Read new state.
		/*Scanner in = new Scanner(System.in);
		System.out.print("Enter turn # and current player turn: ");
		String statusline = in.nextLine();
		String[] board = new String[6];
		for (int i = 0; i < 6; i++) {
			System.out.print("Enter board row " + i + ":");
			board[i] = in.nextLine();
		}
		
		// Print board ending state.
		System.out.println("MiniChess board ending state:");
		System.out.println(statusline);
		for (int i = 0; i < 6; i++) {
			System.out.println(board[i]);
		}
		in.close(); */
		test_ReadBoard(gamestate);
		
	}
	
	public static void test_ReadBoard(State gamestate) {
		int retval = 0;
		String test_file_path;
		File test_file;
		FileInputStream test_stream;
		
		System.out.println("Testing ReadBoard function...");
		
		System.out.print("Test 1: Argument passed to ReadBoard is null.\nTest 1 Result: ");
		retval = gamestate.ReadBoard(null);
		if (retval == -1) {
			System.out.println("Passed.");
		} else {
			System.out.println("Failed! ReadBoard returned code: " + retval);
		}
		
		System.out.print("Test 2: No turn counter.\nTest 2 Result: ");
		test_file_path = ".\\tests\\board\\no_turn_counter.txt";
		test_file = new File(test_file_path);
		try {
			test_stream = new FileInputStream(test_file);
			retval = gamestate.ReadBoard(test_stream);
			if (retval == -2) {
				System.out.println("Passed.");
			} else {
				System.out.println("Failed! ReadBoard returned code: " + retval);
			}
			test_stream.close();
		} catch (FileNotFoundException e) {
			System.out.println("Failed to open test file " + test_file_path);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.print("Test 3: Turn counter beyond max allowed turns.\nTest 3 Result: ");
		test_file_path = ".\\tests\\board\\over_max_turns.txt";
		test_file = new File(test_file_path);
		try {
			test_stream = new FileInputStream(test_file);
			retval = gamestate.ReadBoard(test_stream);
			if (retval == -3) {
				System.out.println("Passed.");
			} else {
				System.out.println("Failed! ReadBoard returned code: " + retval);
			}
			test_stream.close();
		} catch (FileNotFoundException e) {
			System.out.println("Failed to open test file " + test_file_path);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
