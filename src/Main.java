import javax.swing.*;
import java.io.*;
import java.util.Random;
import java.util.Scanner;

public class Main {

    //String variable initialisation.
    static String userChoice;
    static String key = "";
    static String path;

    //Scanner initialisation.
    static Scanner SCANNER = new Scanner(System.in);

    public static void main(String[] args) throws IOException {

        while (true) {
            System.out.println("———\nWould you like to encrypt or decrypt a file (\u001B[35mE\u001B[0m/\u001B[35mD\u001B[0m)?");

            userChoice = SCANNER.nextLine();

            if(userChoice.equalsIgnoreCase("no")){
                System.out.println("\n\u001B[31mThe program has been terminated.\u001B[37m\n");
                break;
            }

            //Prompt the user to input their file via a JFileChooser pop-up.
            System.out.println("\nSelect a file.");
            chooseFile();
        }
    }

    public static void chooseFile() throws IOException {

        //Create a file choosing prompt.
        JFileChooser FILE_DIALOG = new JFileChooser();
        FILE_DIALOG.showDialog(null, "Select file");
        File CHOSEN_FILE = FILE_DIALOG.getSelectedFile();

        //Display the user's chosen path for confirmation.
        if(CHOSEN_FILE == null){
            System.out.println("\n\u001B[31mError: No valid file path chosen.\u001B[0m\n");
        }
        else {
            path = CHOSEN_FILE.getAbsolutePath();
            System.out.println("\u001B[35m" + path + "\u001B[0m");
            key = "";
        }

        //Create an output file.
        File outputFile;
        FileInputStream inputStream;
        FileOutputStream outputStream;

        //Create the decryption key for the user.
        Random RANDOMIZER = new Random();
        int number;

        for(int i = 0; i < 6; i++){
            number = 49 + RANDOMIZER.nextInt(9);
            key = key.concat(String.valueOf((char)(number + 60)));
        }

        //Creating the input file stream.
        inputStream = new FileInputStream(CHOSEN_FILE);

        if(userChoice.equalsIgnoreCase("e")){
            outputFile = new File(CHOSEN_FILE.getAbsolutePath() + ".enc");

            //Creating the output file stream.
            outputStream = new FileOutputStream(outputFile);

            //Call the encryptData function with inputStream, outputStream, and key as parameters.
            System.out.println("\nEncrypting...");
            encryptData(inputStream, outputStream, key);

            outputFile.setReadOnly();
        }
        else if (userChoice.equalsIgnoreCase("d")){
            String NEW_PATH = path + path.substring(path.length() - 8);
            outputFile = new File(NEW_PATH.replace(path.substring(path.length() - 8), "") + "_dec" + path.substring(path.length() - 8, path.length() - 4));

            //Creating the output file stream.
            outputStream = new FileOutputStream(outputFile);

            //Call the decryptData function with inputStream, outputStream, and key as parameters.
            System.out.println("\nDecrypting...");
            decryptData(inputStream, outputStream, CHOSEN_FILE);
        }
        else {
            System.out.println("\n\u001B[31mError: Choose one of the two options provided(E/D).\u001B[0m\n");
        }
    }

    //The encryptData method encrypts any document using a key, inputStream, and outputStream.
    public static void encryptData(FileInputStream iStream, FileOutputStream oStream, String key) throws IOException {

        //Input bytes array that stores the collected values.
        byte[] INPUT_BYTES_ARRAY = new byte[(int) iStream.available()];
        iStream.read(INPUT_BYTES_ARRAY);

        //Output bytes array that stores the manipulated value.
        byte[] outputBytesArray = new byte[INPUT_BYTES_ARRAY.length];
        int counter = 0;

        for (int i = 0; i < INPUT_BYTES_ARRAY.length; i++) {
            if (((int) key.charAt(0) - 60) - ((int) key.charAt(2) - 60) == 0) {
                outputBytesArray[i] = (byte) ((INPUT_BYTES_ARRAY[i] + ((int) key.charAt(0) - 60) + ((int) key.charAt(2) - 60)) % 256);
            } else {
                outputBytesArray[i] = (byte) ((INPUT_BYTES_ARRAY[i] + ((int) key.charAt(0) - 60) - ((int) key.charAt(2) - 60)) % 256);
            }

            counter++;

            //Further change the values based on the encryption key.
            if(counter == 1){
                outputBytesArray[i] = (byte) ((INPUT_BYTES_ARRAY[i] + ((int) key.charAt(0) - 60)) % 256);
            }
            else if(counter == 2){
                outputBytesArray[i] = (byte) ((INPUT_BYTES_ARRAY[i] + ((int) key.charAt(1) - 60)) % 256);
            }
            else if(counter == 3){
                outputBytesArray[i] = (byte) ((INPUT_BYTES_ARRAY[i] + ((int) key.charAt(2) - 60)) % 256);
            }
            else if(counter == 4){
                outputBytesArray[i] = (byte) ((INPUT_BYTES_ARRAY[i] + ((int) key.charAt(3) - 60)) % 256);
            }
            else if(counter == 5){
                outputBytesArray[i] = (byte) ((INPUT_BYTES_ARRAY[i] + ((int) key.charAt(4) - 60)) % 256);
            }
            else {
                outputBytesArray[i] = (byte) ((INPUT_BYTES_ARRAY[i] + ((int) key.charAt(1) - 60)) % 256);
                counter = 0;
            }
        }

        //Write to the output file.
        oStream.write(outputBytesArray);

        //Print the decryption key.
        System.out.println("\u001B[35mYour decryption key is: " + key + "\u001B[0m");
        System.out.println("The chosen file has been encrypted successfully.");
    }

    public static void decryptData(FileInputStream iStream, FileOutputStream oStream, File oldFile) throws IOException {
        //Prompt the user for the decryption key.
        System.out.println("\nWhat is your decryption key?");
        String key = SCANNER.nextLine();
        int counter = 0;

        //Run the code if the decryption key isn't a null value.
        if (key != null) {
            byte[] INPUT_BYTES_ARRAY = new byte[(int) iStream.available()];
            iStream.read(INPUT_BYTES_ARRAY);
            byte[] outputBytes = new byte[INPUT_BYTES_ARRAY.length];

            for (int i = 0; i < INPUT_BYTES_ARRAY.length; i++) {
                if(((int)key.charAt(0) - 60) - ((int)key.charAt(2) - 60) == 0) {
                    outputBytes[i] = (byte) ((INPUT_BYTES_ARRAY[i] - ((int) key.charAt(0) - 60) - ((int) key.charAt(2) - 60)) % 256);
                }
                else{
                    outputBytes[i] = (byte) ((INPUT_BYTES_ARRAY[i] - ((int) key.charAt(0) - 60) + ((int) key.charAt(2) - 60)) % 256);
                }

                counter++;

                //Further change values based on index of the decryption key.
                if(counter == 1){
                    outputBytes[i] = (byte) ((INPUT_BYTES_ARRAY[i] - ((int) key.charAt(0) - 60)) % 256);
                }
                else if(counter == 2){
                    outputBytes[i] = (byte) ((INPUT_BYTES_ARRAY[i] - ((int) key.charAt(1) - 60)) % 256);
                }
                else if(counter == 3){
                    outputBytes[i] = (byte) ((INPUT_BYTES_ARRAY[i] - ((int) key.charAt(2) - 60)) % 256);
                }
                else if(counter == 4){
                    outputBytes[i] = (byte) ((INPUT_BYTES_ARRAY[i] - ((int) key.charAt(3) - 60)) % 256);
                }
                else if(counter == 5){
                    outputBytes[i] = (byte) ((INPUT_BYTES_ARRAY[i] - ((int) key.charAt(4) - 60)) % 256);
                }
                else {
                    outputBytes[i] = (byte) ((INPUT_BYTES_ARRAY[i] - ((int) key.charAt(1) - 60)) % 256);
                    counter = 0;
                }
            }

            //Delete the encrypted file.
            oldFile.delete();

            //Write to the decryption file.
            oStream.write(outputBytes);

            System.out.println("\nThe chosen file has been decrypted successfully.");
        }
    }
}