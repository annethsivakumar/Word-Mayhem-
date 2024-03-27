/*
 * Program Name: WordMayhem.java
 * Description: 
 * This program is a word game called Word Mayhem. 
 * Two players take turns guessing words based on a set of assigned letters.
 * The game continues until players choose to quit or are done guessing words. 
 * At the end of the game, the program displays the final scores and declares the winner.
 */
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.Scanner;

class WordMayhem {
  /**
   * The main method that runs the Word Mayhem game. 
   * @param args Command-line arguments which are not used in this program.
   * @throws Exception If an error occurs during file reading.
   */
    public static void main(String[] args) throws Exception {
      // Declare an ArrayList to store the words from the word bank.
      ArrayList<String> words = new ArrayList<String>();
      // Read wordlist text file into an ArrayList.
      readFile("wordlist.txt", words);
      // Create Scanner to allow user to interact with game.
      Scanner input = new Scanner(System.in);
      // Bubble sort the ArrayList of Strings for binary search function.
      bubbleSort(words);

      // Initialize variables.
      char[] player1Letters;
      char[] player2Letters;
      String player1Name;
      String player2Name;
      String playerGuess;
      int player1Score = 0;
      int player2Score = 0;
      ArrayList<String> duplicateWords = new ArrayList<String>();

      // Print title and start game.
      System.out.println("---------- WELCOME TO WORD MAYHEM ----------");
      // Ask player 1 for their name.
      System.out.print("Player 1: Please enter your name: ");
      player1Name = input.nextLine();
      // Ask player 2 for their name.
      System.out.print("Player 2: Please enter your name: ");
      player2Name = input.nextLine();
      // Generate and display eleven unique letters for player 1.
      player1Letters = generateLetters();
      System.out.print("\n" + player1Name + ", it is your turn! Here is your set of letters: "  + Arrays.toString(player1Letters) + "\n");

      // Get player 1 to guess words.
      System.out.print("Please enter a word (or 'quit' / 'done' to exit): ");
      // Remove any empty spaces and convert Strings to all lower case.
      playerGuess = input.nextLine().toLowerCase().trim();
      // Keep asking player 1 for words until they enter 'quit' or 'done'.
      while (!playerGuess.equals("quit") && !playerGuess.equals("done")) {
        // Check if player 1 has entered thier guess before, add to duplicateWords ArrayList otherwise.
        if(duplicateWords.contains(playerGuess)) {
          System.out.println("You have already entered this word.");
        } else {
          duplicateWords.add(playerGuess);
          // Check if guess contains only assigned characters, print 'unassigned letters entered' otherwise.
          if(validateLetters(playerGuess, player1Letters)){
            // Check if guess is in the wordlist, print 'not a word' otherwise. 
            if(binarySearch(words, playerGuess)){
              // Calculate and display score.
              player1Score = calculateScore(playerGuess, player1Score);
              System.out.println("Your score is " + player1Score);
            } else {
              System.out.println("The word that you entered cannot be found in our word bank.");
            } 
          } else {
          System.out.println("The word that you entered includes letters that you were not assigned.");
          }
        }
        // Ask player 1 to enter thier next guess.
        System.out.print("Please enter a new word (or 'quit' / 'done' to exit): ");
        // Remove any empty spaces and convert Strings to all lower case.
        playerGuess = input.nextLine().toLowerCase().trim();
      }
      
      // After player 1 quits, Tell them their final score and switch to player 2 turn.
      System.out.println("\n" + player1Name + ", your score is " + player1Score + ". ");
      System.out.println("\n" + player2Name +", it is your turn!");

      // Delete the contents of duplicateWords for player 2 use.
      duplicateWords.removeAll(duplicateWords);

      // Generate and display eleven unique letters for player 2.
      player2Letters = generateLetters();
      System.out.print("Here is your set of letters: " + Arrays.toString(player2Letters) + "\n");

      // Get player 2 to guess words.
      System.out.print("Please enter a word (or 'quit' / 'done' to exit): ");
      // Remove any empty spaces and convert Strings to all lower case.
      playerGuess = input.nextLine().toLowerCase().trim() ;
      // Keep asking player 2 for words until they enter 'quit' or 'done'.
      while (!playerGuess.equals("quit") && !playerGuess.equals("done")) {
        // Check if player 2 has entered thier guess before, add to duplicateWords ArrayList otherwise.
        if(duplicateWords.contains(playerGuess)) {
          System.out.println("You have already entered this word.");
        } else {
          duplicateWords.add(playerGuess);
          // Check if guess contains only assigned characters, print 'unassigned letters entered' otherwise.
          if(validateLetters(playerGuess, player2Letters)){
            // Check if guess is in the wordlist, print 'not a word' otherwise . 
            if(binarySearch(words, playerGuess)){
              // Calculate and display score.
              player2Score = calculateScore(playerGuess, player2Score);
              System.out.println("Your score is " + player2Score);
            } else {
              System.out.println("The word that you entered cannot be found in our word bank.");
            } 
          } else {
          System.out.println("The word that you entered includes letters that you were not assigned.");
          }
        }
        // Ask player 2 to enter thier next guess.
        System.out.print("Please enter a new word (or 'quit' / 'done' to exit): ");
        // Remove any empty spaces and convert Strings to all lower case.
        playerGuess = input.nextLine().toLowerCase().trim();
      }
      input.close();

      // Calculate winner.
      if (player1Score > player2Score){
        System.out.println("\n" + player1Name + ", you win the game with a score of " + player1Score + " points. Congratulations!");
      } else if(player1Score < player2Score){
        System.out.println("\n" + player2Name + ", you win the game with a score of " + player2Score + " points. Congratulations!");
      } else {
        System.out.println("\n" + "Tie game, " + player1Name + " and " + player2Name + " both finished with a score of " + player1Score + "!");
      }
      System.out.println("\n" + "--------------------------------------------");
    } // End of main method.

  
    /**
     * Reads a file line by line and adds each line to a given ArrayList.
     * @param filename The name of the file to read as a String.
     * @param mylist The ArrayList to store the file lines.
     * @throws Exception If an error occurs during file reading.
     */
    private static void readFile(String filename, ArrayList<String> mylist) throws Exception {
      // Open the file for reading.
      BufferedReader file = new BufferedReader(new FileReader(filename));
      String line;
      // Read each line and add it to the ArrayList.
      while ((line = file.readLine()) != null) {
        mylist.add(line);
      }
      // Close the file.
      file.close();
    } // End of readFile method.

    
    /**
     * Generates eleven unique letters for the players.
     * @return An array of characters representing the unique letters.
     */
    private static char[] generateLetters(){
      // Initialize variables.
      int ConsonantPosition;
      int VowelPosition;
      char[] vowels = {'a','e','i','o','u','y'};
      char[] consonant = {'b','c','d','f','g',
                          'h','j','k','l','m',
                          'n','p','q','r','s',
                          't','v','w','x','z'};
      char[] uniqueLetters = new char[11];
      // Tracks the used positions.
      boolean[] usedConsonantPositions = new boolean[20];
      boolean[] usedVowelPositions = new boolean[6];
      // Generate eleven unique consonants.
      for(int i = 0; i < 9; i++) {
        do {
          ConsonantPosition = (int)(Math.random() * 19);
        // Repeat if the position is already used.
        } while (usedConsonantPositions[ConsonantPosition]);
        // Mark the position as used.
        usedConsonantPositions[ConsonantPosition] = true;
        uniqueLetters[i] = consonant[ConsonantPosition];        
        }
        // Generate two unique vowels.
        for(int i = 9; i < 11; i++) {
          do {
            VowelPosition = (int)(Math.random() * 6);
          // Repeat if the position is already used.
          } while (usedVowelPositions[VowelPosition]);
          // Mark the position as used.
          usedVowelPositions[VowelPosition] = true;
          uniqueLetters[i] = vowels[VowelPosition];        
        } 
      return uniqueLetters;
    } // End of generateLetters method.


    /**
     * Checks if the given word contains only the letters assigned to the player. 
     * @param givenWord The guess entered by the player as a String.
     * @param playerLetters An array of characters assigned to the player.
     * @return true if the word is valid, false otherwise.
     */
    private static boolean validateLetters(String givenWord, char[] playerLetters) {
      // Convert the givenWord string to a character array for easy comparison.
      char[] givenWordArray = givenWord.toCharArray();
      // Iterate over each character in chars1.
      for (char c : givenWordArray) {
        // Check if the character is present in playerLetters.
        if (!containsChar(playerLetters, c))
          return false;
      }
      return true;
    } // End of validateLetters method.


    /**
     * Checks if an array of characters contains a specific target character.
     * @param array The array of characters to search in.
     * @param target The character to find.
     * @return true if the charcter is found, false otherwise.
     */
    private static boolean containsChar(char[] array, char target) {
      // Iterate over each character in the array.
      for (char c : array) {
        // Check if the character matches the target character.
        if (c == target)
        return true;  
      }
      return false;
    } // End of containsChar method.


    /**
     * Sorts an ArrayList of Strings using the bubble sort algorithm.
     * @param wordList the ArrayList of Strings to be sorted.
     */
    private static void bubbleSort(ArrayList<String> wordList){
      for (int i = 0; i < wordList.size() - 1; i++) {
        for (int j = 0; j < wordList.size() - i - 1; j++) {
          // Compare adjacent strings.
          if (wordList.get(j).compareTo(wordList.get(j + 1)) > 0) {
            // Swap elements at j and j+1.
            String temp = wordList.get(j);
            wordList.set(j, wordList.get(j + 1));
            wordList.set(j + 1, temp);
          }
        }
      }
    }


    /**
     * Searches for a target String in the ArrayList using binary search algorithm.
     * @param wordList The ArrayList of sorted Strings from the wordlist.
     * @param target The String to search for.
     * @return true if the word is found, false otherwise.
     */
    private static boolean binarySearch(ArrayList<String> wordList, String target) {
      int start = 0;
      int end = wordList.size() - 1;
      while (start <= end) {
        int mid = start + (end - start) / 2;
        int comparison = target.compareTo(wordList.get(mid));
        // Find the target element.
        if (comparison == 0) {
          return true;
        // Search in the left half.
        } else if (comparison < 0) {
          end = mid - 1;
        // Search in the right half.
        } else {
          start = mid + 1;
        }
      }
      return false;
    }


    /**
     * Calculates the score for a given word based on its length.
     * @param word A String word to calculate the score of the player.
     * @param playerScore The current score of the player as an integer.
     * @return The updated score after adding the score of the word.
     */
    private static int calculateScore(String word, int playerScore) {
      // Initialize variables.
      int length = word.length();
      // Determine the score given the length of the word.
      if (length == 1) {
        return playerScore += 1;
      } else if (length == 2) {
        return playerScore += 2;
      } else if (length == 3 || length == 4) {
        return playerScore += 3;
      } else if (length == 5) {
        return playerScore += 4;
      } else if (length == 6) {
        return playerScore += 5;
      } else if (length == 7) {
        return playerScore += 6;
      } else {
        return playerScore += 10;
      }
    } // End of calculateScore method.

} // End Main class
