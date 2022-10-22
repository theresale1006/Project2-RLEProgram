import java.util.Scanner;

public class RleProgram {
    public static void main(String[] args) {
        // Display welcome message
        System.out.println("Welcome to the RLE image encoder!");
        // Display color test with the message
        System.out.println("\nDisplaying Spectrum Image: ");
        ConsoleGfx.displayImage(ConsoleGfx.testRainbow);

        byte[] imageData = null;                      // Stores and conversion of the user input is placed in this byte
        boolean exit = false;                         // Sets up boolean so that the while loop will keep running until it is true
        Scanner scnr = new Scanner(System.in);


        // Display the menu and will continuously print it until exit is true
        while (!exit){

            String userInput = "";

            System.out.println("\nRLE Menu");
            System.out.println("--------");
            System.out.println("0. Exit");
            System.out.println("1. Load File");
            System.out.println("2. Load Test Image");
            System.out.println("3. Read RLE String");
            System.out.println("4. Read RLE Hex String");
            System.out.println("5. Read Data Hex String");
            System.out.println("6. Display Image");
            System.out.println("7. Display RLE String");
            System.out.println("8. Display Hex RLE Data");
            System.out.println("9. Display Hex Flat Data");

            // Asks for user to input one of the menu options above
            System.out.print("\nSelect a Menu Option: ");
            int option = scnr.nextInt();

            if (option == 1) {
                // Takes what the user inputs and the file is loaded using ConsoleGfx.loadFile(userInput) and you want to store the returned byte[] into the imageData
                System.out.print("Enter name of file to load: ");
                userInput = scnr.next();
                imageData = ConsoleGfx.loadFile(userInput);

            }
            else if (option == 2) {
                // Stores Console.Gfx.testImage to the imageData array
                System.out.println("Test image data loaded.");
                imageData = ConsoleGfx.testImage;

            }
            else if (option == 3) {
                // Takes what the user inputted and converts the string to RLE byte array
                // It also takes the RLE byte array and decompresses it to be used
                System.out.print("Enter an RLE string to be decoded: ");
                userInput = scnr.next();
                imageData = stringToRle(userInput);
                imageData = decodeRle(imageData);

            }
            else if (option == 4) {
                // Takes the user input that is a hex string and converts it into byte data
                // It takes the byte data and then decompresses it to be used
                System.out.print("Enter the hex string holding RLE data: ");
                userInput = scnr.next();
                imageData = stringToData(userInput);
                imageData = decodeRle(imageData);
            }
            else if (option == 5) {
                // Takes what the user input (raw data) and converts it into a flat byte data (hexadecimal)
                System.out.print("Enter the hex string holding flat data: ");
                userInput = scnr.next();
                imageData = stringToData(userInput);
            }
            else if (option == 6) {
                // Display image store inside a imageData array
                System.out.println("Displaying image...");
                ConsoleGfx.displayImage(imageData);
            }
            else if (option == 7) {
                // Takes the data stored in the imageData and converts into readable data
                // It prints this representation
                imageData = encodeRle(imageData);
                System.out.println("RLE representation: " + toRleString(imageData));

            }
            else if (option == 8) {
                // Takes the data in the imageData and converts it into its hexadecimal representation
                imageData = encodeRle(imageData);
                System.out.println("RLE hex values: " + toHexString(imageData));
            }
            else if (option == 9) {
                // Takes the data in imageData (raw data) and converts it in to hexadecimal representation
                System.out.println("Flat hex values: " + toHexString(imageData));
            }
            else if (option == 0) {
                // If user chooses menu option 0, it gives exit false and exits loop ending the run
                exit = true;
            }
            else {
                // If user does not input a number from 0-9, it returns an error
                System.out.println("Error! Invalid input.");
            }
        }
    }

    // Method 1: Converts byte array into a string using hexadecimals
    public static String toHexString(byte[] data) {
        String hexString = "";
        int remainder = 0;

        for (int i = 0; i < data.length; i++) {
            remainder = data[i] % 16;

            if (remainder >= 10) {
            // If remainder is greater than 10, it is represented by its hexadecimal
                remainder += 'a' - 10;
                hexString += (char) remainder;
            }
            else {
                hexString += remainder;
            }

        }
        return hexString;
    }

    // Method 2: Counts the number of appearances of different values in an array
    public static int countRuns(byte[] flatData) {
        int num = 1;
        int count = 1;

        for (int i = 0; i < flatData.length - 1; i++) {
        // Counts the number of appearances of the same value if not, a new count is established
            if (flatData[i] == flatData[i + 1]) {
                num++;
                if (num == 16) {
                // If run is longer than 15, then it begins a new run
                    count++;
                    num = 1;
                }
            } else if (flatData[i] != flatData[i + 1]) {
                count++;
                num = 1;

            }
        }
        return count;
    }



    // Method 3: Converts raw data array into an encoded array
    public static byte[] encodeRle(byte[] flatData) {
        int length = countRuns(flatData) * 2;
        byte[] res = new byte[length];

        byte num = 1;
        byte index = 0;

        for (int i = 0; i < flatData.length - 1; i++) {
            if (flatData[i] == flatData[i + 1]) {
                num++;

                if (num == 16) {
                // If run is longer than 15, then it begins a new run
                    res[index] = 15;
                    res[index + 1] = flatData[i];
                    index += 2;
                    num = 1;
                    }
                }
                else {
                // Run ends if element is not the same as the one next to them, and input is gathered into another array
                res[index] = num;
                res[index + 1] = flatData[i];
                index += 2;
                num = 1;
            }
            res[index] = num;
            res[index + 1] = flatData[flatData.length - 1];

        }
        return res;
    }

    // Method 4: Determines the size of the byte when decompressed
    public static int getDecodedLength(byte[] rleData) {
        int res = 0;

        for (int i = 0; i < rleData.length; i++) {
            if (i % 2 == 0)
            // Targets the even indices of an array
                res += rleData[i];

        }
        return res;
    }
    // Method 5: Returns decompressed array of the compressed input array
    public static byte[] decodeRle(byte[] rleData) {
        int size = getDecodedLength(rleData);
        int index = 0;

        byte[] res = new byte[size];

        for (int i = 0; i < rleData.length; i +=2) {
        // Makes sure that the indices are seen as pairs rather than individuals
            byte value = rleData[i + 1];
            int repeats = rleData[i];

            for(int j = 0; j < repeats; j++) {
                res[index] = value;
                index++;
            }
        }
        return res;
    }
    // Method 6: Converts input string into an array
    public static byte[] stringToData(String dataString) {
        byte[] res = new byte[dataString.length()];

        for(int i = 0; i < dataString.length(); i++) {
            res[i] = Byte.parseByte(String.valueOf(dataString.charAt(i)), 16);
            // Converts the string into its hexadecimal partner in one step

        }
        return res;
    }
    // Method 7: Translate RLE data into a more readable form
    public static String toRleString(byte[] rleData) {
        String res = "";

        for (int i = 0; i < rleData.length - 1; i += 2) {
            // Reads through the array in pairs
            res += rleData[i];
            int temp = (rleData[i + 1]) % 16;

            if (temp >= 10) {
            // Adds the hexadecimal with the delimiter
                temp += 'a' - 10;
            }
            else {
                temp += '0';
            }
            res += (char) temp + ":";
        }

        res = res.substring(0, (res.length() - 1));
        return res;
    }

    // Method 8: Translates human-readable RLE to RLE format (inverse method of 7)
    public static byte[] stringToRle(String rleString) {
        int size = 1;
        int index = 0;

        for(int i = 0; i < rleString.length(); i++) {
            if (rleString.charAt(i) == ':') {
                size++;
            }
        }
        byte[] res = new byte[size * 2];
        String[] string = rleString.split(":");

        for(int i = 0; i < string.length; i++) {
        // Runs through the array of strings from the original and assigns a byte
        // Checks if the string element contains two or three characters
            if (string[i].length() == 2){
                res[index] = Byte.parseByte(String.valueOf(string[i].charAt(0)));
                index++;
                res[index] = Byte.parseByte(String.valueOf(string[i].charAt(1)), 16);
                index++;
            }
            else{
                res[index] = Byte.parseByte(string[i].substring(0, 2));
                index++;
                res[index] = Byte.parseByte(String.valueOf(string[i].charAt(2)), 16);
                index++;
            }

        }
        return res;
    }
}


