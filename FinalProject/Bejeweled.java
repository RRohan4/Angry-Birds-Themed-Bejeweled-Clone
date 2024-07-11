/**
* Name: Rohan Raina
* Date: 2024-01-20
* Class: ICS3U1 - 11
* Description:
* This class represents a Bejeweled (TM)
* game, which allows player to make moves
* by swapping two pieces. Chains formed after
* valid moves disappears and the pieces on top
* fall to fill in the gap, and new random pieces
* fill in the empty slots.  Game ends after a
* certain number of moves or player chooses to 
* end the game.
*/

import java.awt.Color;
import java.io.*;

public class Bejeweled {




   BejeweledGUI gui;	// the object referring to the GUI

   
   //constants
   final Color COLOUR_DELETE = Color.RED;
   final Color COLOUR_SELECT = Color.YELLOW;
   final int CHAIN_REQ = 3;
   final int NUMMOVE = 10;
   final int NUMPIECESTYLE = 7;
   final int EMPTY = -1;
   final int NUMROW = 8;
   final int NUMCOL = 8;
   final String GAMEFILEFOLDER = "gamefiles";
   
   //global variables
   int board[][] = new int[NUMROW][NUMCOL];
   int score;
   int numMovesLeft;
   boolean firstSelection;
   int slot1Row, slot1Col;
   
   /**
 * Finds # of empty slots and returns the amount.
 *
 * @return total number of empty slots on game board.
 */
   public int checkPoints() {
      int roundPoints = 0;
   // Iterate through each row and column on the game board      
      for(int i = 0; i < NUMROW; i++) {
         for(int j = 0; j < NUMCOL; j++) {
            // Increment points when an empty slot is encountered
            if(board[i][j] == EMPTY)
               roundPoints++;
         }
      } 
      return roundPoints;
   }
   
	public void initBoard() {
   // Iterate through each row and column on the game board
      for(int i = 0; i < NUMROW; i++) {
         for(int j = 0; j < NUMCOL; j++) {
             // Generate and assign random number  to the board element
            board[i][j] = (int)(Math.random()*(NUMPIECESTYLE)+1);
         }
      }
   }
   
   
   /**
 *  assigns random numbers to each element.
 */
   public void displayBoard() {
   // Iterate through each row and column on the game board
      for(int i = 0; i < NUMROW; i++) {
         for(int j = 0; j < NUMCOL; j++) {
            // Call the setPiece method on the GUI to display the piece at the current board position
            gui.setPiece(i, j, board[i][j]-1);
         }
      }
   }
   /**
 * Checks if two board slots are vertically or horizontally adjacent to eachother.
 *
 * @param r1: The row index of the first slot.
 * @param c1: The column index of the first slot.
 * @param r2: The row index of the second slot.
 * @param c2: The column index of the second slot.
 * @return true if the slots are adjacent, otherwise false.
 */
   public boolean adjacentSlots(int r1, int c1, int r2, int c2) {
  // Return true if the horizontal component is the same for both indices and the column indices differ by 1,
   // or vice versa if the vertical component is the same and the row indices differ by 1.
      return   (r1 == r2 && Math.abs(c1 - c2) == 1) ||
               (c1 == c2 && Math.abs(r1 - r2) == 1);
   }
/**
 * Counts # of consecutive matching or empty slots to the left of a starting position.
 *
 * @param row:    The row index of the starting position.
 * @param column: The column index of the starting position.
 * @return  count of consecutive matching or empty slots to the left.
 */
   public int countLeft(int row, int column) {
    // Initialize count to 0 and start checking positions to the left
       int count = 0;
      int cur_col = column - 1;
    // Continue counting while within the bounds and the piece matches the starting piece or is empty
      while(cur_col>= 0 && (board[row][cur_col] == board[row][column] || board[row][cur_col] == EMPTY)){
         count++;
         cur_col--;
      }
      return count;
   }
   
   /**
 * Counts the number of consecutive matching or empty slots to the right of a specified position.
 *
 * @param row    The row index of the starting position.
 * @param column The column index of the starting position.
 * @return The count of consecutive matching or empty slots to the right.
 */
   public int countRight(int row, int column) {
    // Initialize count to 0 and start checking positions to the right

      int count = 0;
      int cur_col = column + 1;
    // Continue counting while within the bounds and the piece matches the starting piece or is empty
      while(cur_col< NUMCOL && (board[row][cur_col] == board[row][column]|| board[row][cur_col] == EMPTY)) {
         count++;
         cur_col++;
      } 

      return count;
   }
   
   
   /**
 * Counts the number of consecutive matching or empty slots above a specified position.
 *
 * @param row    The row index of the starting position.
 * @param column The column index of the starting position.
 * @return The count of consecutive matching or empty slots above.
 */
   public int countUp(int row, int column) {
    // Initialize count to 0 and start checking positions above

      int count = 0;
      int cur_row = row-1;
      // Continue counting while within the bounds and the piece matches the starting piece or is empty
      while(cur_row>= 0 && (board[cur_row][column] == board[row][column] || board[cur_row][column] == EMPTY)) {
        count++;
        cur_row--;
      } 
      return count;
   }
   
   /**
 * Counts the number of consecutive matching or empty slots below a specified position.
 *
 * @param row    The row index of the starting position.
 * @param column The column index of the starting position.
 * @return The count of consecutive matching or empty slots below.
 */
   
   public int countDown(int row, int column) {
      // Initialize count to 0 and start checking positions below
      
      int count = 0;
      int cur_row = row+1;
      // Continue counting while within the bounds and the piece matches the starting piece or is empty

      while(cur_row < NUMROW && (board[cur_row][column] == board[row][column]|| board[cur_row][column] == EMPTY)) {
          count++;
          cur_row++;
      }
      return count;
   }
/**
 * Swaps the pieces between two positions on game board.
 *
 * @param row1: The row index of the first position.
 * @param column1:The column index of the first position.
 * @param row2: The row index of the second position.
 * @param column2: The column index of the second position.
 */
   public void swapPieces(int row1, int column1, int row2, int column2) {
      //swap the board values of the two positions in the board array
      int temp = board[row1][column1];//store the first piece in a temporary variable
      board[row1][column1] = board[row2][column2];//move the value at the second position to the first
      board[row2][column2] = temp;//set the value of the second position to that stored in the first
   }

/**
 * Marks a game board piece for deletion.
 *
 * @param row: The row index of the piece.
 * @param column: The column index of the piece.
 */
   public void markDeletePiece(int row, int column) {
      //sets the value of given index to empty so it can be deleted.
      board[row][column] = EMPTY;
      //highlights the slot on the gui using the given colour
      gui.highlightSlot(row, column, COLOUR_DELETE);

   }
   
   /**
 * Marks game board pieces to the left of given position for deletion up to a given distance.
 *
 * @param row    The row index of the starting position.
 * @param column The column index of the starting position.
 * @param dist   The distance to the left for marking pieces for deletion.
 */
   
   public void markDeletePieceLeft(int row, int column, int dist) {
   //sets the value to empty for all the values to the left from column-1 to column - dist
   //it also highlights the slots on the gui
      for(int i = 1; i <= dist; i++) {
         board[row][column - i] = EMPTY;
         gui.highlightSlot(row, column-i, COLOUR_DELETE);

      }
   }
/**
 * Marks game board pieces to the right of a specified position for deletion up to a certain distance.
 *
 * @param row    The row index of the starting position.
 * @param column The column index of the starting position.
 * @param dist   The distance to the right for marking pieces for deletion.
 */
   public void markDeletePieceRight(int row, int column, int dist) {
   //sets the value to empty for all the values to the right from column+1 to column + dist
   //it also highlights the slots on the gui
      for(int i = 1; i <= dist; i++) {
         board[row][column + i] = EMPTY;
         gui.highlightSlot(row, column+i, COLOUR_DELETE);

      }
   }
/**
 * Marks game board pieces above a specified position for deletion up to a certain distance.
 *
 * @param row    The row index of the starting position.
 * @param column The column index of the starting position.
 * @param dist   The distance upward for marking pieces for deletion.
 */
   public void markDeletePieceUp(int row, int column, int dist) {
//sets the value to empty for all the values above+ from row-1 to row - dist
//it also highlights the slots on the gui
        for(int i = 1; i <= dist; i++) {
            board[row - i][column] = EMPTY;
            gui.highlightSlot(row-i, column, COLOUR_DELETE);

        }
   }
/**
 * Marks game board pieces below a specified position for deletion up to a certain distance.
 *
 * @param row    The row index of the starting position.
 * @param column The column index of the starting position.
 * @param dist   The distance downward for marking pieces for deletion.
 */
   public void markDeletePieceDown(int row, int column, int dist) {
//sets the value to empty for all the values above+ from row+1 to row + dist  
//it also highlights the slots on the gui
         for(int i = 1; i <= dist; i++) {
            board[row+i][column] = EMPTY;
            gui.highlightSlot(row+i, column, COLOUR_DELETE);

         }
   }
   
   /**
 * Updates the game board by shifting pieces down, randomly filling in empty slots and removing  highlighting.
 */
   public void updateBoard() {
      //shift pieces down starting with the bottom row
      for(int c = 0; c < NUMCOL; c++) {
         for(int r = NUMROW - 1; r >= 0; r--) {
            if(board[r][c] != EMPTY) {
               int i = r;
                // Continue moving the piece down while the slot below is empty
               while(i < NUMROW-1 && board[i+1][c] == EMPTY) {
                  // Move the piece down
                  board[i+1][c] = board[i][c];
                  board[i][c] = EMPTY;
                  i++;
               }
               
            }
         }
      }
    // Refill empty slots with random pieces and remove GUI highlighting
      for(int i = 0; i < NUMROW; i++) {
         for(int j = 0; j < NUMCOL; j++) {
            if(board[i][j] == EMPTY)
               // Generate a random piece for the empty slot and unhighlight
               board[i][j] = (int)(Math.random()*(NUMPIECESTYLE)+1);
               gui.unhighlightSlot(i, j);

         }
      }
      
      
   }
   
   


   
   public Bejeweled(BejeweledGUI gui) {
      this.gui = gui;      
      start();   
   }
/**
 * starts the game
 */
   public void start() {
      //set numerical values
      score = 0;
      numMovesLeft = NUMMOVE;
      initBoard();//set board values
      displayBoard();//display images corresponding to the values in the board array to the screen
      firstSelection = true;
      gui.setMoveLeft(numMovesLeft);

   }
/**
 * Makes a move at the selected position.
 * Handles the selection of pieces, move validation, swapping, calculating score, updating the game state and graphics.
 * @param row: The row index of the selected slot.
 * @param column:The column index of the selected slot.
 */
   public void play (int row, int column) {
      
      if(firstSelection) {
         //stores first selection and highlights slot
         gui.highlightSlot(row, column, COLOUR_SELECT);
         slot1Row = row;
         slot1Col = column;
     } else {
         //second selection
         gui.highlightSlot(row,column, COLOUR_SELECT);
         //check if the two slots are adjacent
         if(!adjacentSlots(row,column, slot1Row, slot1Col)) { 
            gui.showInvalidMoveMessage();
         } else { 
            //if they are adjacent

            
            boolean pointsGained1 = false;
            boolean pointsGained2 = false;

            swapPieces(row, column, slot1Row, slot1Col);
            
            //calcute how many points we would get going in the 4 directions for the first piece
            int countLeft1 = countLeft(slot1Row, slot1Col);
            int countRight1 = countRight(slot1Row, slot1Col);
            int countUp1 = countUp(slot1Row, slot1Col);
            int countDown1 = countDown(slot1Row, slot1Col);

            //calculate how many points we would get in the 4 directions for the second piece
            int countLeft2 = countLeft(row, column);
            int countRight2 = countRight(row, column);
            int countUp2 = countUp(row, column);
            int countDown2 = countDown(row, column);

            //check if the chains formed horizontally and vertically are long enough to meet the chain requirement
            //otherwise set their values to 0
            
            if(1 + countLeft1 + countRight1 >= CHAIN_REQ) {
               pointsGained1 = true;
            } else {
               countLeft1 = 0;
               countRight1 = 0;
            }
            if(1 + countUp1 + countDown1 >= CHAIN_REQ) {
               pointsGained1 = true;
            } else {
               countUp1 = 0;
               countDown1 = 0;
            }
            if(1 + countLeft2 + countRight2 >= CHAIN_REQ) {
               pointsGained2 = true;
            } else {
               countLeft2 = 0;
               countRight2 = 0;
            }
            if(1 + countUp2 + countDown2 >= CHAIN_REQ) {
               pointsGained2 = true;
            }  else {
                  countUp2 = 0;
                  countDown2 = 0;
            }
            // Handle move outcome            
            if(!pointsGained1 && !pointsGained2) {
             // Display invalid move message and revert the swap if the move doesnt gain us enough points

               gui.showInvalidMoveMessage();
               swapPieces(slot1Row, slot1Col, row, column);
            } else {
               // Unhighlight selected slots
               gui.unhighlightSlot(slot1Row, slot1Col);
               gui.unhighlightSlot(row, column);
               
               //update gui with swapped pieces
               gui.setPiece(slot1Row, slot1Col, board[slot1Row][slot1Col] - 1);
               gui.setPiece(row, column, board[row][column] - 1);
               //delete all the pieces in each direction for slot 1 and slot 2 if it will gain us points
               if(pointsGained1){
                  
                  markDeletePiece(slot1Row, slot1Col);
                  markDeletePieceLeft(slot1Row, slot1Col, countLeft1);
                  markDeletePieceRight(slot1Row, slot1Col, countRight1);
                  markDeletePieceUp(slot1Row, slot1Col, countUp1);
                markDeletePieceDown(slot1Row, slot1Col, countDown1);
               }if(pointsGained2){
                  markDeletePiece(row, column);
                  markDeletePieceLeft(row, column, countLeft2);
                  markDeletePieceRight(row,column, countRight2);
                  markDeletePieceUp(row,column, countUp2);
                  markDeletePieceDown(row,column, countDown2);
             }
               //get points gained update score
              int points_round = checkPoints();
               score+= points_round;
               gui.showChainSizeMessage(points_round); 
               numMovesLeft--; 
               //bring down blocks and fill in extra gaps            
               updateBoard();
               //update gui 
               updateGameBoard();
              }



         }
         gui.unhighlightSlot(row, column);
         gui.unhighlightSlot(slot1Row, slot1Col);       
         
       }
      if(numMovesLeft <= 0)
         endGame();
      
      firstSelection = !firstSelection;  

   }
/**
 * Saves game state to specified file
 *
 * @param fileName:  name of the file to save the game state to.
 * @return true if the save was successful, false otherwise.
 */
   public boolean saveToFile (String fileName) {
      boolean saved = true;
      
      try {
         //declare a buffered writer to the specified file in a predetermined folder
         BufferedWriter out= new BufferedWriter(new FileWriter(GAMEFILEFOLDER + "\\" + fileName));
         out.write(score + "\n" + numMovesLeft + "\n");//put score and remaining moves on 2 lines
         //write the game board to file
         for(int i = 0; i < NUMROW; i++) {
            for(int j = 0; j < NUMCOL; j++) {
               out.write(board[i][j] + " ");
            }
            out.write("\n");
         }
         
         out.close();
      } catch (IOException iox) {
         saved = false;
      }
      
      return saved;//return true if the save was successful
   }
/**
 * Loads game state from file and displays accordingly
 * @param fileName The name of the file to load game state from.
 * @return true if the load succeeded, false otherwise.
 */
   public boolean loadFromFile(String fileName) {
      boolean loaded = true;
      try {
         // Open a BufferedReader to read from the specified file

          BufferedReader in = new BufferedReader(new FileReader(GAMEFILEFOLDER + "\\" + fileName));
          score = Integer.parseInt(in.readLine());
          numMovesLeft = Integer.parseInt(in.readLine());
          
           String[] input;
            // Read the game board from the file

           for(int i = 0; i < NUMROW; i++) {
               input = in.readLine().split(" ");
               for(int j = 0; j < NUMCOL; j++) {
                  board[i][j] = Integer.parseInt(input[j]);
               }
           }   
          in.close();

         // Update the GUI with the loaded game state
          updateGameBoard();
          
      } catch(IOException iox) {
          loaded = false;
      }
      return loaded;//return true if the load was successful
   }

/**
 * Sets the score, moves remaining, and displays the pieces on the game board through the GUI.
 */
   public void updateGameBoard() {
      //set the score, moves remaining and display the pieces to the board
      gui.setScore(score);
      gui.setMoveLeft(numMovesLeft);
      displayBoard();
   }
/**
 * Ends the  game and displays the game over message with the final score and moves used.
 */
   public void endGame() {
   
      gui.showGameOverMessage(score, NUMMOVE - numMovesLeft);
   
   }


}