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
		State gamestate = new State();
		
		// Print board starting state.
		System.out.println("MiniChess board starting state:");
		gamestate.WriteBoard();

		test_ReadBoard(gamestate);

		// Print ending starting state.
		System.out.println("MiniChess board ending state:");
		gamestate.WriteBoard(null);

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
			e.printStackTrace();
		}
		
		System.out.print("Test 4: Missing next player indicator.\nTest 4 Result: ");
		test_file_path = ".\\tests\\board\\no_player_turn.txt";
		test_file = new File(test_file_path);
		try {
			test_stream = new FileInputStream(test_file);
			retval = gamestate.ReadBoard(test_stream);
			if (retval == -4) {
				System.out.println("Passed.");
			} else {
				System.out.println("Failed! ReadBoard returned code: " + retval);
			}
			test_stream.close();
		} catch (FileNotFoundException e) {
			System.out.println("Failed to open test file " + test_file_path);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		System.out.print("Test 5: Invalid next player indicator.\nTest 5 Result: ");
		test_file_path = ".\\tests\\board\\invalid_player.txt";
		test_file = new File(test_file_path);
		try {
			test_stream = new FileInputStream(test_file);
			retval = gamestate.ReadBoard(test_stream);
			if (retval == -5) {
				System.out.println("Passed.");
			} else {
				System.out.println("Failed! ReadBoard returned code: " + retval);
			}
			test_stream.close();
		} catch (FileNotFoundException e) {
			System.out.println("Failed to open test file " + test_file_path);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		System.out.print("Test 6: Not enough rows on board.\nTest 6 Result: ");
		test_file_path = ".\\tests\\board\\not_enough_rows.txt";
		test_file = new File(test_file_path);
		try {
			test_stream = new FileInputStream(test_file);
			retval = gamestate.ReadBoard(test_stream);
			if (retval == -6) {
				System.out.println("Passed.");
			} else {
				System.out.println("Failed! ReadBoard returned code: " + retval);
			}
			test_stream.close();
		} catch (FileNotFoundException e) {
			System.out.println("Failed to open test file " + test_file_path);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		System.out.print("Test 7: Not enough columns in some row.\nTest 7 Result: ");
		test_file_path = ".\\tests\\board\\not_enough_columns.txt";
		test_file = new File(test_file_path);
		try {
			test_stream = new FileInputStream(test_file);
			retval = gamestate.ReadBoard(test_stream);
			if (retval == -7) {
				System.out.println("Passed.");
			} else {
				System.out.println("Failed! ReadBoard returned code: " + retval);
			}
			test_stream.close();
		} catch (FileNotFoundException e) {
			System.out.println("Failed to open test file " + test_file_path);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		System.out.print("Test 8: Invalid piece on the board.\nTest 8 Result: ");
		test_file_path = ".\\tests\\board\\invalid_piece.txt";
		test_file = new File(test_file_path);
		try {
			test_stream = new FileInputStream(test_file);
			retval = gamestate.ReadBoard(test_stream);
			if (retval == -8) {
				System.out.println("Passed.");
			} else {
				System.out.println("Failed! ReadBoard returned code: " + retval);
			}
			test_stream.close();
		} catch (FileNotFoundException e) {
			System.out.println("Failed to open test file " + test_file_path);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		System.out.print("Test 9: Read valid board.\nTest 9 Result: ");
		test_file_path = ".\\tests\\board\\valid_board.txt";
		test_file = new File(test_file_path);
		try {
			test_stream = new FileInputStream(test_file);
			retval = gamestate.ReadBoard(test_stream);
			if (retval == -0) {
				System.out.println("Passed.");
			} else {
				System.out.println("Failed! ReadBoard returned code: " + retval);
			}
			test_stream.close();
		} catch (FileNotFoundException e) {
			System.out.println("Failed to open test file " + test_file_path);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
