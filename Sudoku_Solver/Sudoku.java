import java.util.List;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Sudoku {
	static boolean isFullSolution(int[][] board) {
              
                
                    for(int i = 0; i < 9; i++){
                         for(int j = 0; j < 9; j++){
                                if(board[i][j] == 0){
                                         return false;
                            }
                        }
                    }
		return !reject(board);
	}

	static boolean reject(int[][] board) {
        
	for(int k = 0; k < 9; k++){
            for(int i = 0; i < 9; i++){
                  for(int j = i + 1; j < 9; j++){
                        if(board[k][i] == board[k][j] && board[k][i] != 0){
                            return true;
                        }
                    }
                }
      }
      for(int k = 0; k < 9; k++){
            for(int i = 0; i < 9; i++){
                  for(int j = i + 1; j < 9; j++){
                        if(board[i][k] == board[j][k] && board[i][k] != 0){
                            return true;
                        }
                    }
                }
            }


        for(int k = 0; k < 9; k++){
                  int hor = 0;
                  int ver = 0;
                  switch(k){
                          case 0:
                              hor = 0;
                              ver = 0;
                              break;
                          case 1: 
                              hor = 3;
                              ver = 0;
                              break;
                          case 2:
                              hor = 6;
                              ver = 0;
                              break;
                          case 3:
                              hor = 0;
                              ver = 3;
                              break;
                          case 4:
                              hor = 3;
                              ver = 3;
                              break;
                          case 5:
                              hor = 6;
                              ver = 3;
                              break;
                          case 6:
                              hor = 0;
                              ver = 6;
                              break;
                          case 7:
                              hor = 3;
                              ver = 6;
                              break;
                          case 8:
                              hor = 6;
                              ver = 6;
                              break;
                  }
                    for(int i = 0; i < 3; i++){
                        for(int m = 0; m < 3; m++){
                            for(int n = 0; n < 3; n++){
                                for(int j = 0; j < 3; j++){
                            if(board[i + ver][m + hor] == board[n + ver][j + hor] && board[i + ver][m + hor] != 0){
                                if(j != m && i != n){
                                    return true;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
        
     return false;
	}
        

	static int[][] extend(int[][] board, int[][] metaBoard) {
            for(int i = 0; i < 9; i++){
                for(int k = 0; k < 9; k++){
                    if(board[i][k] == 0 && metaBoard[i][k] > 9){
                        board[i][k] = 1;
                        return board;
                    }
                }
            }
                
            return null;
	}
        
        static int maxCount(int[][] metaBoard){
            int maxCount = 0;
            for(int i = 0; i < 9; i++){
                for(int k = 0; k < 9; k++){
                        if(metaBoard[i][k] > maxCount){
                            maxCount = metaBoard[i][k];
                        }
                    }
                }
            return maxCount;
        }

	static int[][] next(int[][] board, int[][]metaBoard, int maxCount) {
            int currentLocation = 0;
            for(int i = 0; i < 9; i++){
                for(int k = 0; k < 9; k++){
                    if(board[i][k] == 0 && metaBoard[i][k] > 9){
                        currentLocation = metaBoard[i][k] - 1;
                        i = 10;
                        k = 10;
                    } 
                    if(i != 10){
                    if(metaBoard[i][k] == maxCount && metaBoard[i][k] > 9){
                        currentLocation = maxCount;
                        i = 10;
                        k = 10;
                        }
                    }
                }
            }
            for(int i = 0; i < 9; i++){
                for(int k = 0; k < 9; k++){
                    if(metaBoard[i][k] == currentLocation){
                        if(board[i][k] != 9){
                        board[i][k] = board[i][k] + 1;
                        return board;
                        } else
                            board[i][k] = 0;
                            currentLocation = currentLocation - 1;
                            if(currentLocation == 9){
                                System.out.println("This Board is Unsolvable!");
                                System.exit(0);
                            }
                            i = 0;
                            k = 0;
                                 
                    }
                }
            }
	
            return null;
        }
        

	static void testIsFullSolution() {
            
            int[][] obviouslyUnsolvableBoard = {
                {0,3,0,0,0,0,0,0,0},
                {0,3,0,0,0,0,0,0,0},
                {0,3,0,0,0,0,0,0,0}, 
                {0,3,0,0,0,0,0,0,0}, 
                {0,3,0,0,0,0,0,0,0}, 
                {0,3,0,0,0,0,0,0,0}, 
                {0,3,0,0,0,0,0,0,0}, 
                {0,3,0,0,0,0,0,0,0}, 
                {0,3,0,0,0,0,0,0,0}, 
            };
            int[][] obviouslyUnsolvableBoard2 = {
                {1,3,1,1,1,1,1,1,1},
                {1,3,1,1,1,1,1,1,1},
                {1,3,1,1,1,1,1,1,1}, 
                {1,3,1,1,1,1,1,1,1}, 
                {1,3,1,1,1,1,1,1,1}, 
                {1,3,1,1,1,1,1,1,1}, 
                {1,3,1,1,1,1,1,1,1},
                {1,3,1,1,1,1,1,1,1}, 
                {1,3,1,1,1,1,1,1,1}, 
            };
            int[][] solvableBoardContainingZeroes = {
                {0,0,0,0,0,0,0,0,0},
                {0,0,0,0,0,0,0,0,0},
                {0,0,0,0,0,0,0,0,0},
                {0,0,0,0,0,0,0,0,0},
                {0,0,0,0,0,0,0,0,0}, 
                {0,0,0,0,0,0,0,0,0}, 
                {0,0,0,0,0,0,0,0,0},
                {0,0,0,0,0,0,0,0,0}, 
                {0,0,0,0,0,0,0,0,0}, 
            };
            int[][] genericSolvableBoard = {
                {8,7,6,9,0,0,0,0,0},
                {0,1,0,0,0,6,0,0,0},
                {0,4,0,3,0,5,8,0,0},
                {4,0,0,0,0,0,2,1,0},
                {0,9,0,5,0,0,0,0,0}, 
                {0,5,0,0,4,0,3,0,6}, 
                {0,2,9,0,0,0,0,0,8},
                {0,0,4,6,9,0,1,7,3}, 
                {0,0,0,0,0,1,0,0,4}, 
            };
            int[][] solvedBoard = {
                {2,9,5,7,4,3,8,6,1},
                {4,3,1,8,6,5,9,2,7},
                {8,7,6,1,9,2,5,4,3},
                {3,8,7,4,5,9,2,1,6},
                {6,1,2,3,8,7,4,9,5}, 
                {5,4,9,2,1,6,7,3,8}, 
                {7,6,3,5,2,4,1,8,9},
                {9,2,8,6,7,1,3,5,4}, 
                {1,5,4,9,3,8,6,7,2}, 
            };
            int[][] filledUnsolvedBoard = {
                {2,9,5,7,4,3,8,6,1},
                {4,3,1,8,6,5,9,2,7},
                {8,7,6,1,9,2,5,4,3},
                {3,8,7,4,5,9,2,1,6},
                {6,1,2,3,8,7,4,9,5}, 
                {5,4,9,2,1,6,7,3,8}, 
                {7,6,3,5,2,4,1,8,9},
                {9,2,8,6,7,1,3,5,4}, 
                {1,5,4,9,3,8,2,7,2}, 
            };
            
		
            System.out.println(isFullSolution(obviouslyUnsolvableBoard));
            System.out.println(isFullSolution(obviouslyUnsolvableBoard2));
            System.out.println(isFullSolution(solvableBoardContainingZeroes));
            System.out.println(isFullSolution(genericSolvableBoard));
            System.out.println(isFullSolution(solvedBoard));
            System.out.println(isFullSolution(filledUnsolvedBoard));
            System.out.println();
            
            //Output should read: false, false, false, false, true, false
        }
	static void testReject() {
		int[][] obviouslyUnsolvableBoard = {
                {0,3,0,0,0,0,0,0,0},
                {0,3,0,0,0,0,0,0,0},
                {0,3,0,0,0,0,0,0,0}, 
                {0,3,0,0,0,0,0,0,0}, 
                {0,3,0,0,0,0,0,0,0}, 
                {0,3,0,0,0,0,0,0,0}, 
                {0,3,0,0,0,0,0,0,0}, 
                {0,3,0,0,0,0,0,0,0}, 
                {0,3,0,0,0,0,0,0,0}, 
            };
            int[][] obviouslyUnsolvableBoard2 = {
                {1,3,1,1,1,1,1,1,1},
                {1,3,1,1,1,1,1,1,1},
                {1,3,1,1,1,1,1,1,1}, 
                {1,3,1,1,1,1,1,1,1}, 
                {1,3,1,1,1,1,1,1,1}, 
                {1,3,1,1,1,1,1,1,1}, 
                {1,3,1,1,1,1,1,1,1},
                {1,3,1,1,1,1,1,1,1}, 
                {1,3,1,1,1,1,1,1,1}, 
            };
            int[][] solvableBoardContainingZeroes = {
                {0,0,0,0,0,0,0,0,0},
                {0,0,0,0,0,0,0,0,0},
                {0,0,0,0,0,0,0,0,0},
                {0,0,0,0,0,0,0,0,0},
                {0,0,0,0,0,0,0,0,0}, 
                {0,0,0,0,0,0,0,0,0}, 
                {0,0,0,0,0,0,0,0,0},
                {0,0,0,0,0,0,0,0,0}, 
                {0,0,0,0,0,0,0,0,0}, 
            };
            int[][] genericSolvableBoard = {
                {8,7,6,9,0,0,0,0,0},
                {0,1,0,0,0,6,0,0,0},
                {0,4,0,3,0,5,8,0,0},
                {4,0,0,0,0,0,2,1,0},
                {0,9,0,5,0,0,0,0,0}, 
                {0,5,0,0,4,0,3,0,6}, 
                {0,2,9,0,0,0,0,0,8},
                {0,0,4,6,9,0,1,7,3}, 
                {0,0,0,0,0,1,0,0,4}, 
            };
            int[][] solvedBoard = {
                {2,9,5,7,4,3,8,6,1},
                {4,3,1,8,6,5,9,2,7},
                {8,7,6,1,9,2,5,4,3},
                {3,8,7,4,5,9,2,1,6},
                {6,1,2,3,8,7,4,9,5}, 
                {5,4,9,2,1,6,7,3,8}, 
                {7,6,3,5,2,4,1,8,9},
                {9,2,8,6,7,1,3,5,4}, 
                {1,5,4,9,3,8,6,7,2}, 
            };
            int[][] filledUnsolvedBoard = {
                {2,9,5,7,4,3,8,6,1},
                {4,3,1,8,6,5,9,2,7},
                {8,7,6,1,9,2,5,4,3},
                {3,8,7,4,5,9,2,1,6},
                {6,1,2,3,8,7,4,9,5}, 
                {5,4,9,2,1,6,7,3,8}, 
                {7,6,3,5,2,4,1,8,9},
                {9,2,8,6,7,1,3,5,4}, 
                {1,5,4,9,3,8,2,7,2}, 
            };
            System.out.println(reject(obviouslyUnsolvableBoard));
            System.out.println(reject(obviouslyUnsolvableBoard2));
            System.out.println(reject(solvableBoardContainingZeroes));
            System.out.println(reject(genericSolvableBoard));
            System.out.println(reject(solvedBoard));
            System.out.println(reject(filledUnsolvedBoard));
            System.out.println();
            
            //Should return true, true, false, false, false, true
	}

	static void testExtend() {
            int[][] obviouslyUnsolvableBoard = {
                {0,3,0,0,0,0,0,0,0},
                {0,3,0,0,0,0,0,0,0},
                {0,3,0,0,0,0,0,0,0}, 
                {0,3,0,0,0,0,0,0,0}, 
                {0,3,0,0,0,0,0,0,0}, 
                {0,3,0,0,0,0,0,0,0}, 
                {0,3,0,0,0,0,0,0,0}, 
                {0,3,0,0,0,0,0,0,0}, 
                {0,3,0,0,0,0,0,0,0}, 
            };
               int obviouslyUnsolvableBoardMeta[][] = metaNumbers(obviouslyUnsolvableBoard);
               
            int[][] obviouslyUnsolvableBoard2 = {
                {1,3,1,1,1,1,1,1,1},
                {1,3,1,1,1,1,1,1,1},
                {1,3,1,1,1,1,1,1,1}, 
                {1,3,1,1,1,1,1,1,1}, 
                {1,3,1,1,1,1,1,1,1}, 
                {1,3,1,1,1,1,1,1,1}, 
                {1,3,1,1,1,1,1,1,1},
                {1,3,1,1,1,1,1,1,1}, 
                {1,3,1,1,1,1,1,1,1}, 
            };
                int obviouslyUnsolvableBoard2Meta[][] = metaNumbers(obviouslyUnsolvableBoard2);
            int[][] solvableBoardContainingZeroes = {
                {0,0,0,0,0,0,0,0,0},
                {0,0,0,0,0,0,0,0,0},
                {0,0,0,0,0,0,0,0,0},
                {0,0,0,0,0,0,0,0,0},
                {0,0,0,0,0,0,0,0,0}, 
                {0,0,0,0,0,0,0,0,0}, 
                {0,0,0,0,0,0,0,0,0},
                {0,0,0,0,0,0,0,0,0}, 
                {0,0,0,0,0,0,0,0,0}, 
            };
                int solvableBoardContainingZeroesMeta[][] = metaNumbers(solvableBoardContainingZeroes);
            int[][] genericSolvableBoard = {
                {8,7,6,9,0,0,0,0,0},
                {0,1,0,0,0,6,0,0,0},
                {0,4,0,3,0,5,8,0,0},
                {4,0,0,0,0,0,2,1,0},
                {0,9,0,5,0,0,0,0,0}, 
                {0,5,0,0,4,0,3,0,6}, 
                {0,2,9,0,0,0,0,0,8},
                {0,0,4,6,9,0,1,7,3}, 
                {0,0,0,0,0,1,0,0,4}, 
            };
            int genericSolvableBoardMeta[][] = metaNumbers(genericSolvableBoard);
            int[][] solvedBoard = {
                {2,9,5,7,4,3,8,6,1},
                {4,3,1,8,6,5,9,2,7},
                {8,7,6,1,9,2,5,4,3},
                {3,8,7,4,5,9,2,1,6},
                {6,1,2,3,8,7,4,9,5}, 
                {5,4,9,2,1,6,7,3,8}, 
                {7,6,3,5,2,4,1,8,9},
                {9,2,8,6,7,1,3,5,4}, 
                {1,5,4,9,3,8,6,7,2}, 
            };
            int solvedBoardMeta[][] = metaNumbers(solvedBoard);
            int[][] filledUnsolvedBoard = {
                {2,9,5,7,4,3,8,6,1},
                {4,3,1,8,6,5,9,2,7},
                {8,7,6,1,9,2,5,4,3},
                {3,8,7,4,5,9,2,1,6},
                {6,1,2,3,8,7,4,9,5}, 
                {5,4,9,2,1,6,7,3,8}, 
                {7,6,3,5,2,4,1,8,9},
                {9,2,8,6,7,1,3,5,4}, 
                {1,5,4,9,3,8,2,7,2}, 
            };
            int filledUnsolvedBoardMeta[][] = metaNumbers(filledUnsolvedBoard);
            
            System.out.println();
            printBoard(extend(obviouslyUnsolvableBoard, obviouslyUnsolvableBoardMeta));
            System.out.println();
            printBoard(extend(obviouslyUnsolvableBoard2, obviouslyUnsolvableBoard2Meta));
            System.out.println();
            printBoard(extend(genericSolvableBoard, genericSolvableBoardMeta));
            System.out.println();
            printBoard(extend(solvableBoardContainingZeroes, solvableBoardContainingZeroesMeta));
            System.out.println();
            printBoard(extend(solvedBoard, solvedBoardMeta));
            System.out.println();
            printBoard(extend(filledUnsolvedBoard, filledUnsolvedBoardMeta));
            System.out.println();
	} /*Should return null (Which will result in "Board Unsolvable!" as a result of an edit to the print
            function I have made. It is properly returning null - it's just that an extended board is never run
            in this context) for boards number 2, 5, and 6. For 1, 3, and 4 it will place a 1 at the location
            of the first 0*/

	static void testNext() {
            int[][] obviouslyUnsolvableBoard = {
                {0,3,0,0,0,0,0,0,0},
                {0,3,0,0,0,0,0,0,0},
                {0,3,0,0,0,0,0,0,0}, 
                {0,3,0,0,0,0,0,0,0}, 
                {0,3,0,0,0,0,0,0,0}, 
                {0,3,0,0,0,0,0,0,0}, 
                {0,3,0,0,0,0,0,0,0}, 
                {0,3,0,0,0,0,0,0,0}, 
                {0,3,0,0,0,0,0,0,0}, 
            };
               int obviouslyUnsolvableBoardMeta[][] = metaNumbers(obviouslyUnsolvableBoard);
               int maxCount1 = maxCount(obviouslyUnsolvableBoardMeta);
               
            int[][] obviouslyUnsolvableBoard2 = {
                {1,3,1,1,1,1,1,1,1},
                {1,3,1,1,1,1,1,1,1},
                {1,3,1,1,1,1,1,1,1}, 
                {1,3,1,1,1,1,1,1,1}, 
                {1,3,1,1,1,1,1,1,1}, 
                {1,3,1,1,1,1,0,1,1}, 
                {1,3,1,1,1,1,1,1,1},
                {1,3,1,1,1,1,1,1,1}, 
                {1,3,1,1,1,1,1,1,1}, 
            };
                int obviouslyUnsolvableBoard2Meta[][] = metaNumbers(obviouslyUnsolvableBoard2);
                int maxCount2 = maxCount(obviouslyUnsolvableBoard2Meta);
            int[][] solvableBoardContainingZeroes = {
                {0,0,0,0,0,0,0,0,0},
                {0,0,0,0,0,0,0,0,0},
                {0,0,0,0,0,0,0,0,0},
                {0,0,0,0,0,0,0,0,0},
                {0,0,0,0,0,0,0,0,0}, 
                {0,0,0,0,0,0,0,0,0}, 
                {0,0,0,0,0,0,0,0,0},
                {0,0,0,0,0,0,0,0,0}, 
                {0,0,0,0,0,0,0,0,0}, 
            };
                int solvableBoardContainingZeroesMeta[][] = metaNumbers(solvableBoardContainingZeroes);
                int maxCount3 = maxCount(solvableBoardContainingZeroesMeta);
            int[][] genericSolvableBoard = {
                {8,7,6,9,0,0,0,0,0},
                {0,1,0,0,0,6,0,0,0},
                {0,4,0,3,0,5,8,0,0},
                {4,0,0,0,0,0,2,1,0},
                {0,9,0,5,0,0,0,0,0}, 
                {0,5,0,0,4,0,3,0,6}, 
                {0,2,9,0,0,0,0,0,8},
                {0,0,4,6,9,0,1,7,3}, 
                {0,0,0,0,0,1,0,0,4}, 
            };
            int genericSolvableBoardMeta[][] = metaNumbers(genericSolvableBoard);
            int maxCount4 = maxCount(genericSolvableBoardMeta);
            int[][] solvedBoard = {
                {2,9,5,7,4,3,8,6,1},
                {4,3,1,8,6,5,9,2,7},
                {8,7,6,1,9,2,5,4,3},
                {3,8,7,4,5,9,2,1,6},
                {6,1,2,3,8,7,4,9,5}, 
                {5,4,9,2,1,6,7,3,8}, 
                {7,6,3,5,2,4,1,8,9},
                {9,2,8,6,7,1,3,5,4}, 
                {1,5,4,9,3,8,6,7,0}, 
            };
            int solvedBoardMeta[][] = metaNumbers(solvedBoard);
            int maxCount5 = maxCount(solvedBoardMeta);
            int[][] filledUnsolvedBoard = {
                {0,9,5,7,4,3,8,6,1},
                {4,3,1,8,6,5,9,2,7},
                {8,7,6,1,9,2,5,4,3},
                {3,8,7,4,5,9,2,1,6},
                {6,1,2,3,8,7,4,9,5}, 
                {5,4,9,2,1,6,7,3,8}, 
                {7,6,3,5,2,4,1,8,9},
                {9,2,8,6,7,1,3,5,4}, 
                {1,5,4,9,3,8,2,7,2}, 
            };
            int filledUnsolvedBoardMeta[][] = metaNumbers(filledUnsolvedBoard);
            int maxCount6 = maxCount(filledUnsolvedBoardMeta);
            
            System.out.println();
            printBoard(next(extend(obviouslyUnsolvableBoard, obviouslyUnsolvableBoardMeta), obviouslyUnsolvableBoardMeta, maxCount1));
            System.out.println();
            printBoard(next(extend(obviouslyUnsolvableBoard2, obviouslyUnsolvableBoard2Meta), obviouslyUnsolvableBoard2Meta, maxCount2));
            System.out.println();
            printBoard(next(extend(genericSolvableBoard, genericSolvableBoardMeta),genericSolvableBoardMeta, maxCount3));
            System.out.println();
            printBoard(next(extend(solvableBoardContainingZeroes, solvableBoardContainingZeroesMeta),solvableBoardContainingZeroesMeta, maxCount4));
            System.out.println();
            printBoard(next(extend(solvedBoard, solvedBoardMeta),solvedBoardMeta, maxCount5));
            System.out.println();
            printBoard(next(next(extend(filledUnsolvedBoard, filledUnsolvedBoardMeta), filledUnsolvedBoardMeta, maxCount6),filledUnsolvedBoardMeta, maxCount6));
            System.out.println();
		/*Based on the way I have written the next method, I have to extend the boards before I apply next to them (Or have already had applied next to them). For all of these boards
                 The next function searches for the previously edited value and replaces it with a 2. Other than the last method, which has been nexted twice, and replaces the value with a 3.
                */
                 
	}

        static int[][] metaNumbers(int[][] board) {
            int changeableNumberTags = 10;
            int[][] tempBoard = new int[9][9];
            for(int i = 0; i < 9; i++){
                for(int j = 0; j < 9; j++){
                    if(board[i][j] != 0){
                        tempBoard[i][j] = 1;
                    }
                    
                    if(board[i][j] == 0){
                        tempBoard[i][j] = changeableNumberTags;
                         changeableNumberTags = changeableNumberTags + 1;
                    }
                    
                }
            }
            return tempBoard;
        }
        
	static void printBoard(int[][] board) {
		if(board == null) {
			System.out.println("Board Unsolvable!");
			return;
		}
		for(int i = 0; i < 9; i++) {
			if(i == 3 || i == 6) {
				System.out.println("----+-----+----");
			}
			for(int j = 0; j < 9; j++) {
				if(j == 2 || j == 5) {
					System.out.print(board[i][j] + " | ");
				} else {
					System.out.print(board[i][j]);
				}
			}
			System.out.print("\n");
		}
	}

	static int[][] readBoard(String filename) {
		List<String> lines = null;
		try {
			lines = Files.readAllLines(Paths.get(filename), Charset.defaultCharset());
		} catch (IOException e) {
			return null;
		}
		int[][] board = new int[9][9];
		int val = 0;
		for(int i = 0; i < 9; i++) {
			for(int j = 0; j < 9; j++) {
				try {
					val = Integer.parseInt(Character.toString(lines.get(i).charAt(j)));
				} catch (Exception e) {
					val = 0;
				}
				board[i][j] = val;
			}
		}
		return board;
	}

	static int[][] solve(int[][] board, int[][]metaBoard, int maxCount) {
                
		if(reject(board)) return null;
		if(isFullSolution(board)) return board;
		int[][] attempt = extend(board, metaBoard);
		while (attempt != null) {
			int[][] solution = solve(attempt, metaBoard, maxCount);
			if(solution != null) return solution;
			attempt = next(attempt, metaBoard, maxCount);
		}
		return null;
	}

	public static void main(String[] args) {
		if(args[0].equals("-t")) {
			testIsFullSolution();
			testReject();
			testExtend();
			testNext();
		} else {
			int[][] board = readBoard(args[0]);
                        int[][] metaBoard = metaNumbers(board);
                        int maxCount = maxCount(metaBoard);
			System.out.println("Solution:");
			printBoard(solve(board, metaBoard, maxCount));
		}
	}

}

