import java.util.*;

public class sudoku {

	// list of numbers as chars
	private static ArrayList<Character> numberChar = new ArrayList<Character>();
	static {
		Collections.addAll(numberChar, '1', '2', '3', '4', '5', '6', '7', '8', '9');
	}

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		boolean quit = false;
		while (quit == false){
		// This gets a board and prints it out
		System.out.println("Puzzle:");
		char[][] puzzle = SudokuP.puzzle();
		// This is a board that is invalid
		char[][] invalidPuzzle = { { '1', '2', '3', '.', '.', '.', '.', '.', '.' },
				{ '4', '5', '6', '.', '.', '.', '.', '.', '.' }, { '7', '8', '9', '.', '.', '.', '.', '.', '.' },
				{ '.', '.', '.', '.', '.', '.', '.', '.', '.' }, { '.', '.', '.', '.', '.', '.', '.', '.', '.' },
				{ '.', '.', '.', '.', '.', '.', '.', '.', '.' }, { '.', '.', '.', '.', '.', '.', '.', '.', '.' },
				{ '.', '.', '.', '.', '.', '.', '1', '.', '.' }, { '.', '.', '.', '.', '.', '.', '.', '1', '.' } };
		/*
		 * for(int i = 0; i < invalidPuzzle.length; i ++){ System.out.println("");
		 * for(int j = 0; j < invalidPuzzle[i].length; j ++){ System.out.print(" " +
		 * invalidPuzzle[i][j]); } }
		 */
		solve(puzzle);
		System.out.println();
		System.out.println("Solved Puzzle:");
		printPuzzle(puzzle);
		System.out.println("Hit enter to quit, or type anything to continue");
		String input = sc.nextLine();
		if (input.length() < 1){quit = true;}
		}
	}
	// isValid- validates a board, by checking if it passes all the tests

	public static boolean check(char[][] puzzle) {
		return (validateLateral(puzzle) && validateVertical(puzzle) && validateNearby(puzzle));
	}

	// validateLateral - This validates each row of a given board returning true if
	// there are no duplicate numbers and false otherwise
	public static boolean validateLateral(char[][] puzzle) {
		for (int i = 0; i < puzzle.length; i++) {
			ArrayList<Character> numbers = new ArrayList<Character>();
			numbers.add(puzzle[i][0]);
			for (int j = 1; j < puzzle.length; j++) {
				char currentVal = puzzle[i][j];
				for (int k = 0; k < numbers.size(); k++) {
					if (currentVal != '.' && currentVal == numbers.get(k)) {
						// System.out.println("");
						// ;
						// System.out.println("x:" + j);
						// System.out.println("y:" + i);
						return false;
					}
				}
				numbers.add(currentVal);
			}
		}
		return true;
	}

	// validateVertical - This validates each column of a given board returning true
	// if there are no duplicate numbers and false otherwise
	public static boolean validateVertical(char[][] puzzle) {
		for (int i = 0; i < puzzle.length; i++) {
			ArrayList<Character> numbers = new ArrayList<Character>();
			numbers.add(puzzle[0][i]);
			for (int j = 1; j < puzzle.length; j++) {
				char currentVal = puzzle[j][i];
				for (int k = 0; k < numbers.size(); k++) {
					if (currentVal != '.' && currentVal == numbers.get(k)) {
						// System.out.println("");
						// System.out.println("y:" + j);
						// System.out.println("x:" + i);
						return false;
					}
				}
				numbers.add(currentVal);
			}
		}
		return true;
	}

	// validateNearby - This validates each 3*3 space of a given board returning
	// true if there are no duplicate numbers and false otherwise
	public static boolean validateNearby(char[][] puzzle) {
		if (localConflicts(puzzle, 0, 0, 3, 3) && localConflicts(puzzle, 0, 3, 3, 6)
				&& localConflicts(puzzle, 0, 6, 3, 9) && localConflicts(puzzle, 3, 0, 6, 3)
				&& localConflicts(puzzle, 3, 3, 6, 6) && localConflicts(puzzle, 3, 6, 6, 9)
				&& localConflicts(puzzle, 6, 0, 9, 3) && localConflicts(puzzle, 6, 3, 9, 6)
				&& localConflicts(puzzle, 6, 6, 9, 9)) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean localConflicts(char[][] puzzle, int xStart, int yStart, int xEnd, int yEnd) {
		// This code only goes over 1 3*3 square
		ArrayList<Character> numbers = new ArrayList<Character>();
		for (int i = xStart; i < xEnd; i++) {
			for (int j = yStart; j < yEnd; j++) {
				char currentVal = puzzle[i][j];
				for (int k = 0; k < numbers.size(); k++) {
					if (currentVal != '.' && currentVal == numbers.get(k)) {
						// System.out.println("x " + i);
						// System.out.println("y " + j);
						return false;
					}
				}
				numbers.add(currentVal);
			}
		}
		return true;

	}
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	/////////////////////////// P A R T 2

	public static void solve(char[][] puzzle) {
		if (puzzle == null || puzzle.length == 0) {
			return;
		}
		solveSudoku(puzzle);
	}

	// recursive method,returns true if the guess makes a valid puzzle, false
	// otherwise
	private static boolean solveSudoku(char[][] puzzle) {
		// check validity
		if (!check(puzzle)) {
			return false;
		} 
		if (isSolved(puzzle)) {
			return true;
		} else {
			int[] thisBox = getCurrentRowCol(puzzle);
			if(thisBox[0] == -1) {
				return true;
			}
			for (char i = '1'; i <= '9'; i++) {
                if (check(tempPuzzle(puzzle,thisBox,i))) {
                    puzzle[thisBox[0]][thisBox[1]] = i;
                    if(solveSudoku(puzzle)) {
                    	return true;
                    }else {
                    	puzzle[thisBox[0]][thisBox[1]] = '.';
                    }
                }
            }
            return false;

		}
	}

	public static int[] getCurrentRowCol(char[][] puzzle) {
		int[] rowCol = new int[2];
		for (int row = 0; row <= puzzle.length - 1; row++) {
			for (int col = 0; col <= puzzle[0].length - 1; col++) {
				if (puzzle[row][col] == '.') {
					rowCol[0] = row;
					rowCol[1] = col;
					return rowCol;
				}

			}
		}
		// if this section is reached, then there are no more empty spaces
		rowCol[0] = -1;
		rowCol[1] = -1;
		return rowCol;
		// These values tell the isSolved method that the puzzle is indeed solved.
	}

	public static boolean isSolved(char[][] puzzle) {
		int[] check = { -1, -1 };
		return getCurrentRowCol(puzzle).equals(check);
	}

	public static void printPuzzle(char[][] puzzle) {
		for (char[] row : puzzle) {
			for (char thisChar : row) {
				System.out.print(thisChar + " ");
			}
			System.out.println();
		}
		System.out.println();
	}

	public static char[][] tempPuzzle(char[][] puzzle, int[] thisBox, char i){
		char[][] tempPuzzle = new char[9][9];
		for (int row = 0; row < puzzle.length; row++) {
			for (int col = 0; col < puzzle[0].length; col++) {
				tempPuzzle[row][col] = puzzle[row][col];
			}
		}
		tempPuzzle[thisBox[0]][thisBox[1]] = i;
		return tempPuzzle;
	}
	
}
