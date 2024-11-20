package application;

import ui.UserInput;
import ui.UserOutput;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class TicTacToe {
    //given - constants for board size and markers
    public static final int MAX_BOARD_POSITIONS = 9;
    public static final char X_MARKER = 'X';
    public static final char M_MARKER = 'M'; //changed this from 0 to M bc it was really throwing me off when I ran the
    //code and the numbers are already showing / couldn't easily see where the CPU placed their mark

    //lists I made to track the positions occupied by the user and CPU
    static ArrayList<Integer> playerPositions = new ArrayList<>(); //added both lists to make it easier to cycle through the loops
    static ArrayList<Integer> cpuPositions = new ArrayList<>();

    //given - objects that handle user input and output
    UserOutput userOutput;
    UserInput userInput;

    //given - gotta make the board as an array of characters
    char[] board;

    //given constructor to initialize above objects
    public TicTacToe() {
        userInput = new UserInput();
        userOutput = new UserOutput();
    }

    //given - this gives a visual representation of available positions
    private char[] initializeBoard() {
        char[] board = new char[MAX_BOARD_POSITIONS];

        for (int i = 0; i < board.length; i++) {
            board[i] = Character.forDigit(i, 10);
        }

        return board;
    }

    public void run() {
        this.board = initializeBoard();
        userOutput.gameIntroduction();
        userOutput.displayBoard(board);

        //given - all above lines set up the beginning of the game

        Scanner scan = new Scanner(System.in); //continues prompting for moves until win or tie ending

        //challenge
        char playerMarker = pickYourMarker(scan); //references method to allow marker choice

        char cpuMarker;
        if (playerMarker == X_MARKER) {
            cpuMarker = M_MARKER;
        } else {
            cpuMarker = X_MARKER;
        }

        System.out.println("You chose " + playerMarker + ".");
        System.out.println("The CPU will play as " + cpuMarker + ". Good luck!");
        //statements to confirm the user's choice.

        // 1. Keep playing while there are still options for the user or opponent
        // to select, i.e. not all the elements in the board are X_MARKER or O_MARKER.

        // 2. Display the board

        // 3. Ask the user to select an available position on the board.
        // A valid position is one that's not already selected,
        // i.e. not X_MARKER or O_MARKER


        while (true) {
            //user's turn
            System.out.println("Enter your placement (0-8)");
            int playerSpot = scan.nextInt();

            //this is accounting for the user being unpredictable and preventing user from overriding an already taken position
            while (playerSpot < 0 || playerSpot >= MAX_BOARD_POSITIONS ||
                    playerPositions.contains(playerSpot) || cpuPositions.contains(playerSpot)) {
                System.out.println("Hey, play nice! The position is already taken. Enter a new position!");
                playerSpot = scan.nextInt();
            }


            // 4. If the position is available, mark it on the board.
            placePiece(board, playerSpot, "player", playerMarker, cpuMarker); //mark the chosen position using the "what", "where" and "who".
            //updated to include new marker choices

            // 5. Check if the player has won (see winningPosition arrays).

            String result = checkWinner();

            // 6. If the player has won, print a congratulatory message and
            // exit or ask to play another game.
            if (!result.isEmpty()) {
                System.out.println(result);
                break;
            }

            // 7. Repeat steps 2 to 6 for the opponent's turn
            //cpu's turn
            Random rand = new Random();
            int cpuSpot;
            do {
                cpuSpot = rand.nextInt(MAX_BOARD_POSITIONS);
            } while (playerPositions.contains(cpuSpot) || cpuPositions.contains(cpuSpot));

            placePiece(board, cpuSpot, "cpu", playerMarker, cpuMarker); //mark the chosen position using the "what", "where", and "who"
            //changed to include marker choices

            userOutput.displayBoard(board); //display updated board

            result = checkWinner();
            if (!result.isEmpty()) {
                System.out.println(result);
                break;
            }
        }
    }

    //what is needed to place the marks on the board?

    //need the board itself (identified as a char[] already), the chosen position (a number), and the user (a word/String)
    //what, where, and who
    public static void placePiece(char[] board, int position, String user, char playerMarker, char cpuMarker) { //added new player marker choices
        char symbol = ' '; //default

        if (user.equals("player")) {
            symbol = playerMarker; //changed so its no longer hardcoded
            playerPositions.add(position);
        } else if (user.equals("cpu")) {
            symbol = cpuMarker; //changed so its no longer hardcoded
            cpuPositions.add(position);
        }
        board[position] = symbol; //place the marker on the board at the correct index
    }

    public static String checkWinner() {

        //given - winning positions for reference
        int[] winningPosition0 = {0, 1, 2};    // top horizontal
        int[] winningPosition1 = {3, 4, 5};    // middle horizontal
        int[] winningPosition2 = {6, 7, 8};    // bottom horizontal
        int[] winningPosition3 = {0, 3, 6};    // left vertical
        int[] winningPosition4 = {1, 4, 7};    // middle vertical
        int[] winningPosition5 = {2, 5, 8};    // right vertical
        int[] winningPosition6 = {0, 4, 8};    // top-left, bottom-right diagonal
        int[] winningPosition7 = {2, 4, 6};    // top-right, bottom-left diagonal

        //initialize the 2D array (or array of arrays) that cover all possible winning conditions
        //int[][] indicates that the variable winningConditions stores an array where each element is an array of ints.
        int[][] winningConditions = {winningPosition0, winningPosition1, winningPosition2, winningPosition3,
                winningPosition4, winningPosition5, winningPosition6, winningPosition7};

        //for each iteration, "condition" will be one of the 8 winning positions
        //each "inner" array has 3 numbers. These numbers represent the positions on the board that need to be filled in order to win.

        //the first iteration of the loop, condition will be {0, 1, 2} (the top row). Condition[0] is 0, condition[1] is 1, and condition[2] is 2.
        //the second iteration, condition will be {3, 4, 5} (the middle row), so condition[0] is 3, condition[1] is 4, and condition[2] is 5.
        //the third iteration, condition will be {6, 7, 8} (the bottom row), so condition[0] is 6, condition[1] is 7, and condition[2] is 8.

        for (int[] condition : winningConditions) {
            if (playerPositions.contains(condition[0])
                    && playerPositions.contains(condition[1]) && playerPositions.contains(condition[2])) {
                return "Congrats - you won! Play again?";


            } else if (cpuPositions.contains(condition[0])
                    && cpuPositions.contains(condition[1]) && cpuPositions.contains(condition[2])) {
                return "CPU won! Try again?";
            }
        }
        if (playerPositions.size() + cpuPositions.size() == MAX_BOARD_POSITIONS) {
            return "Tie! Try again?";
        }
        return ""; //no winner yet
    }

    // Challenge: let user pick Xs or Os - 0s was switched to "M" for clarity!
    public static char pickYourMarker(Scanner scan) {
        char symbol = ' ';

        while (true) {
            System.out.println("Choose your marker: X or M?");
            String input = scan.nextLine().toUpperCase();

            if (input.equals("X")) {
                symbol = X_MARKER;
                break;
            } else if (input.equals("M")) {
                symbol = M_MARKER;
                break;
            } else {
                System.out.println("Hey, play nice! Pick either X or M!");
            }
            input.equals(scan.nextLine());
        }
            return symbol;
        }


        /*
         * Challenge: Add a 2-player mode
         * Challenge: Program a more sophisticated AI opponent 😎
         */
    }


