package LNC_Assignment_1;
import java.util.Arrays;
import java.util.Scanner;

public class KaprekarIterations {

    private static final int KAPREKAR_CONSTANT = 6174;
    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.print("Enter a four-digit positive number with at least two different digits: ");
            int userInput = scanner.nextInt();
    
            if (userInput > 0 && isValidInput(userInput)) {
                int iterations = calculateIterations(userInput);
                System.out.println("Number of iterations to reach 6174: " + iterations);
            } else {
                System.out.println("Invalid input. Please enter a four-digit positive number with at least two different digits.");
            }
        }
    }
    

    private static boolean isValidInput(int number) {
        String numString = String.valueOf(number);
        return numString.length() == 4 && hasAtLeastTwoDifferentDigits(numString);
    }

    private static boolean hasAtLeastTwoDifferentDigits(String numString) {
        char[] digitsArray = numString.toCharArray();
        Arrays.sort(digitsArray);
    
        for (int i = 0; i < digitsArray.length - 1; i++) {
            if (digitsArray[i] != digitsArray[i + 1]) {
                return true;
            }
        }
    
        return digitsArray[0] != digitsArray[digitsArray.length - 1]; 
    }
    

    public static int calculateIterations(int number) {
        int iterations = 0; 
    
        while (number != KAPREKAR_CONSTANT && number != 0) {
            int[] digits = getDigitsArray(number);
            Arrays.sort(digits);
            int ascending = getNumberFromDigits(digits);
    
            Arrays.sort(digits);
            int descending = getNumberFromDigits(reverseArray(digits));
    
            number = descending - ascending;
            
            if (number < 1000) {
                number *= 10; 
            }
            iterations++;
            System.out.println(iterations + ". " + descending + " -" + ascending + " = " + number);
        }
    
        return iterations;
    }
    

    public static int[] getDigitsArray(int number) {
        String numString = String.valueOf(number);
        int[] digits = new int[numString.length()];

        for (int i = 0; i < numString.length(); i++) {
            digits[i] = Character.getNumericValue(numString.charAt(i));
        }

        return digits;
    }

    public static int getNumberFromDigits(int[] digits) {
        int result = 0;
        for (int digit : digits) {
            result = result * 10 + digit;
        }
        return result;
    }

    public static int[] reverseArray(int[] array) {
        int[] reversed = new int[array.length];
        for (int i = 0, j = array.length - 1; i < array.length; i++, j--) {
            reversed[i] = array[j];
        }
        return reversed;
    }
}