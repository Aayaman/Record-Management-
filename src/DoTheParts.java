import java.util.Scanner;
import java.io.PrintWriter;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.EOFException;
import java.io.IOException;

public class DoTheParts{
	
	/**
     * Initializes the book counters for each genre file.
     */
    static int countCCB = 0;
    static int countHCB = 0;
    static int countMTV = 0;
    static int countMRB = 0;
    static int countNEB = 0;
    static int countOTR = 0;
    static int countSSM = 0;
    static int countTPA = 0;

    /**
     * This method processes the input file, reads the book data from the specified files, 
     * validates it, and writes any errors to a separate syntax error file.
     */
    public static void do_part1() {
        // Declare variables for reading input files, handling errors, and processing book data
        Scanner sc = null;
        PrintWriter printException = null;
        Scanner readBooksFile = null;
        long numOfFiles;
        String fileName = "";
        String bookData = "";
        
        // Attempt to open the file containing the list of input filenames
        try {
            sc = new Scanner(new FileInputStream("/Users/anasmanguer/Desktop/Record-Management-/data/input_file_names.txt"));
        } catch (FileNotFoundException e) {
            System.out.println("File part1_input_file_names.txt is not found or could not be opened ");
            System.exit(0);
        }
        
        // Read the number of files and prepare for file processing
        numOfFiles = sc.nextLong(); // Read long value representing the number of files
        sc.nextLine(); // Move to the next line after reading the long value 
        int countFiles = 0;
        fileName = sc.nextLine();
        
        // Iterate over each file as per the number of files specified
        while (countFiles <= numOfFiles && sc.hasNextLine()) {
            
            // Attempt to open the current book data file
            try {
                readBooksFile = new Scanner(new FileInputStream("/Users/anasmanguer/Desktop/Record-Management-/data/" + fileName));
            } catch (FileNotFoundException e) {
                System.out.println("The File " + fileName + " could not be opened or created.");
                System.exit(0);
            }
            
            // Process each line of book data in the current file
            while (readBooksFile.hasNextLine()) {
                bookData = readBooksFile.nextLine();
                try {
                    // Open a PrintWriter to log any syntax errors
                    printException = new PrintWriter(new FileOutputStream("/Users/anasmanguer/Desktop/Record-Management-/data/syntax_error_file.csv", true));
                    // Call countFields to validate the book data
                    countFields(bookData, fileName);
                } catch (FileNotFoundException e) {
                    System.out.println("File syntax_error_file.txt could not be found or could not be created!");
                    System.exit(0);
                } catch (TooManyFieldsException e) {
                    printException.println(e);
                } catch (TooFewFieldsException e) {
                    printException.println(e);
                } catch (UnknownGenreException e) {
                    printException.println(e);
                } catch (MissingFieldException e) {
                    printException.println(e);
                }
                
                // Close the PrintWriter after logging errors
                printException.close();
            }
            
            // Move to the next file name and increment the counter
            fileName = sc.nextLine();
            countFiles++;
            
            // Close the Scanner object for the current book file
            readBooksFile.close();
        }
        
        // Close the main Scanner object for reading the list of file names
        sc.close();
    }

    /**
     * Processes and validates book data, ensuring all required fields (title, author, 
     * price, ISBN, genre, year) are present and correctly formatted. The method also 
     * checks for syntax errors and sorts valid book entries into the appropriate genre file.
     *
     * @param bookData The book record to process, containing six fields: title, author, 
     *                 price, ISBN, genre, and year.
     * @param fileName The name of the file from which the book data was read.
     * @throws TooManyFieldsException If there are more fields than expected.
     * @throws TooFewFieldsException If there are fewer fields than required.
     * @throws UnknownGenreException If the genre is not recognized.
     * @throws MissingFieldException If any required field is missing.
     */

	public static void countFields(String bookData, String fileName) throws TooManyFieldsException, TooFewFieldsException, UnknownGenreException, MissingFieldException {

        //title, authors, price, isbn, genre, year
        String arrayOfFields[] = {"", "", "", "", "", ""};
        int fieldCount = 0;
        boolean Quotes = false;
        int startOfField = 0;
        int endOfField = 0;
        String bookGenreFile = "";
        
        // Iterate through the book data and separate fields by commas, handling quotes
        for (int i = 0; i < bookData.length() && fieldCount < arrayOfFields.length - 1; i++) {
            char c = bookData.charAt(i);

            // Toggle inQuotes flag when encountering a double-quote
            if (c == '"') {
                Quotes = !Quotes;
            } else if (c == ',' && !Quotes) {
                // When outside of quotes and encountering a comma, count a field
                endOfField = i;
                arrayOfFields[fieldCount] = bookData.substring(startOfField, endOfField);
                fieldCount++;
                startOfField = i + 1;
            }
        }

        // Add the last field if the line does not end with a comma
        endOfField = bookData.length();
        if (fieldCount < arrayOfFields.length && endOfField != startOfField) {
            arrayOfFields[fieldCount] = bookData.substring(startOfField, endOfField);
            fieldCount++;

            // Check for missing fields and throw respective exceptions
            if (arrayOfFields[0].trim().equals("")) {
                throw new MissingFieldException("syntax error in file: " + fileName +
                        "\n====================\nError: missing title" +
                        "\nRecord: " + bookData);
            } else if (arrayOfFields[1].trim().equals("")) {
                throw new MissingFieldException("syntax error in file: " + fileName +
                        "\n====================\nError: missing author" +
                        "\nRecord: " + bookData);
            } else if (arrayOfFields[2].trim().equals("")) {
                throw new MissingFieldException("syntax error in file: " + fileName +
                        "\n====================\nError: missing price" +
                        "\nRecord: " + bookData);
            } else if (arrayOfFields[3].trim().equals("")) {
                throw new MissingFieldException("syntax error in file: " + fileName +
                        "\n====================\nError: missing ISBN" +
                        "\nRecord: " + bookData);
            } else if (arrayOfFields[4].trim().equals("")) {
                throw new MissingFieldException("syntax error in file: " + fileName +
                        "\n====================\nError: missing genre" +
                        "\n Record: " + bookData);
            } else if (arrayOfFields[5].trim().equals("")) {
                throw new MissingFieldException("syntax error in file: " + fileName +
                        "\n====================\nError: missing year" +
                        "\nRecord: " + bookData);
            } else {
                // If all fields are present, validate the genre and sort the book
                switch (arrayOfFields[4]) {
                    case "CCB":
                        bookGenreFile = "Cartoons_Comics_Books.csv";
                        sortBooksByGenre(bookGenreFile, bookData);
                        countCCB++;
                        break;
                    case "HCB":
                        bookGenreFile = "Hobbies_Collectibles_Books.csv";
                        sortBooksByGenre(bookGenreFile, bookData);
                        countHCB++;
                        break;
                    case "MTV":
                        bookGenreFile = "Movies_TV_Books.csv";
                        sortBooksByGenre(bookGenreFile, bookData);
                        countMTV++;
                        break;
                    case "MRB":
                        bookGenreFile = "Music_Radio_Books.csv";
                        sortBooksByGenre(bookGenreFile, bookData);
                        countMRB++;
                        break;
                    case "NEB":
                        bookGenreFile = "Nostalgia_Eclectic_Books.csv";
                        sortBooksByGenre(bookGenreFile, bookData);
                        countNEB++;
                        break;
                    case "OTR":
                        bookGenreFile = "Old_Time_Radio_Books.csv";
                        sortBooksByGenre(bookGenreFile, bookData);
                        countOTR++;
                        break;
                    case "SSM":
                        bookGenreFile = "Sports_Sports_Memorabilia_Books.csv";
                        sortBooksByGenre(bookGenreFile, bookData);
                        countSSM++;
                        break;
                    case "TPA":
                        bookGenreFile = "Trains_Planes_Automobiles_Books.csv";
                        sortBooksByGenre(bookGenreFile, bookData);
                        countTPA++;
                        break;
                    default:
                        throw new UnknownGenreException("syntax error in file: " + fileName +
                                "\n====================\nError: invalid genre" +
                                "\nRecord: " + bookData);
                }
            }
        }

        // Check if there are too many or too few fields
        if (fieldCount > arrayOfFields.length) {
            throw new TooManyFieldsException("syntax error in file " + fileName +
                    "\n====================\nError: too many fields" +
                    "\nRecord: " + bookData);
        } else if (fieldCount < arrayOfFields.length) {
            throw new TooFewFieldsException("syntax error in file " + fileName +
                    "\n====================\nError: too few fields" +
                    "\nRecord: " + bookData);
        }
    }
		
		
	/**
     * Appends book data to the appropriate genre file based on the provided file name.
     *
     * @param fileName The name of the genre file where the book data should be written.
     * @param bookData The book record data to be appended to the file.
     */
    public static void sortBooksByGenre(String fileName, String bookData) {
        PrintWriter pw = null;
        try {
            pw = new PrintWriter(new FileOutputStream("/Users/anasmanguer/Desktop/Record-Management-/data/" + fileName, true));
        }
        catch (FileNotFoundException e) {
            System.out.println("File " + fileName + " could not be found or could not be created!");
            System.exit(0);
        }
        pw.println(bookData);
        pw.close();
    }

    /**
     * Reads book data from multiple genre CSV files, validates the data (ISBN, price, year),
     * and stores valid books in an array. Valid books are then serialized and saved to binary files.
     * Syntax errors, including invalid ISBN, price, and year, are logged into a separate file.
     */

	public static void do_part2() {
        // Declare the PrintWriter object for logging errors
        PrintWriter printException = null;
        
        // Declare the Scanner object for reading book data from files
        Scanner scanBookData = null;
        
        // Declare the ObjectOutputStream object for writing serialized books
        ObjectOutputStream oos = null;
        
        // Declare variables for book data and validation flags
        String bookData = "";
        boolean isbn10Valide = false;
        boolean isbn13Valide = false;
        boolean isPriceValide = false;
        boolean isYearValide = false;

        // List of genre files to process
        String[] files = {
            "Cartoons_Comics_Books.csv", "Hobbies_Collectibles_Books.csv", 
            "Movies_TV_Books.csv", "Music_Radio_Books.csv", 
            "Nostalgia_Eclectic_Books.csv", "Old_Time_Radio_Books.csv", 
            "Sports_Sports_Memorabilia_Books.csv", "Trains_Planes_Automobiles_Books.csv"
        };

        // Loop through each file to process book data
        for (int i = 0; i < files.length; i++) {
            try {
                // Try to open the book data file and error log file
                scanBookData = new Scanner(new FileInputStream("/Users/anasmanguer/Desktop/Record-Management-/data/" + files[i]));
                printException = new PrintWriter(new FileOutputStream("/Users/anasmanguer/Desktop/Record-Management-/data/semantic_error_file.csv", true));
            } catch (FileNotFoundException e) {
                // Handle error if file cannot be found or opened
                System.out.println("File " + files[i] + " could not be found or opened.");
                System.exit(0);
            }

            // Initialize the array of Book objects for the current genre
            Book[] booksInFile = null;
            switch (i) {
                case 0: booksInFile = new Book[countCCB]; 
                break;
                case 1: booksInFile = new Book[countHCB]; 
                break;
                case 2: booksInFile = new Book[countMTV]; 
                break;
                case 3: booksInFile = new Book[countMRB]; 
                break;
                case 4: booksInFile = new Book[countNEB]; 
                break;
                case 5: booksInFile = new Book[countOTR]; 
                break;
                case 6: booksInFile = new Book[countSSM];
                break;
                case 7: booksInFile = new Book[countTPA]; 
                break;
            }

            // Initialize the counter for valid books
            int valideBookCounter = 0;

            // Read the file line by line and process each book record
            while (scanBookData.hasNextLine()) {
                bookData = scanBookData.nextLine();  // Read a line of book data

                // Parse the line into individual book data fields
                String[] bookSingleData = parseLine(bookData);

                // Assign parsed data to book variables
                String title = bookSingleData[0];
                String author = bookSingleData[1];
                double price = Double.parseDouble(bookSingleData[2]);
                String isbn = bookSingleData[3];
                String genre = bookSingleData[4];
                int year = Integer.parseInt(bookSingleData[5]);

                // Validate the price
                try {
                    isPriceValide = validatePrice(price, files[i], bookData);
                } catch (BadPriceException e) {
                    printException.println(e);  // Log error if price is invalid
                    isPriceValide = false;
                }


                // Validate the ISBN (10 or 13)
                if (isbn.trim().length() == 10) {
                    try {
                        isbn10Valide = validateisbn10(isbn, files[i], bookData);
                    } catch (BadIsbn10Exception e) {
                        printException.println(e);  // Log error if ISBN-10 is invalid
                        isbn10Valide = false;
                    }
                } else if (isbn.length() == 13) {
                    try {
                        isbn13Valide = validateisbn13(isbn, files[i], bookData);
                    } catch (BadIsbn13Exception e) {
                        printException.println(e);  // Log error if ISBN-13 is invalid
                        isbn13Valide = false;
                    }
                }

                

                // Validate the publication year
                try {
                    isYearValide = validateYear(year, files[i], bookData);
                } catch (BadYearException e) {
                    printException.println(e);  // Log error if year is invalid
                    isYearValide = false;
                }

                // If all validations pass, create a Book object and add it to the array
                if ((isbn10Valide || isbn13Valide) && isPriceValide && isYearValide) {
                    booksInFile[valideBookCounter] = new Book(title, author, price, isbn, genre, year);
                    valideBookCounter++;  // Increment the counter for valid books
                }
            }

            // Resize the array of valid books if necessary
            if (valideBookCounter < booksInFile.length) {
                Book[] booksInArray = new Book[valideBookCounter];
                System.arraycopy(booksInFile, 0, booksInArray, 0, valideBookCounter);  // Copy valid books to the new array

                // Update the count for the genre
                switch (i) {
                    case 0: countCCB = valideBookCounter; 
                    break;
                    case 1: countHCB = valideBookCounter; 
                    break;
                    case 2: countMTV = valideBookCounter; 
                    break;
                    case 3: countMRB = valideBookCounter; 
                    break;
                    case 4: countNEB = valideBookCounter; 
                    break;
                    case 5: countOTR = valideBookCounter; 
                    break;
                    case 6: countSSM = valideBookCounter; 
                    break;
                    case 7: countTPA = valideBookCounter; 
                    break;
                }

                // Serialize the array of valid books and save it to a file
                try {
                    oos = new ObjectOutputStream(new FileOutputStream("/Users/anasmanguer/Desktop/Record-Management-/data/" + files[i] + ".ser"));
                    oos.writeObject(booksInArray);  // Write the array of valid books to the file
                    oos.close();
                } catch (IOException e) {
                    System.out.println("Error while saving serialized file: " + files[i]);
                    System.exit(0);  // Exit if there's an error during serialization
                }
            }

            // Close the Scanner and PrintWriter resources
            scanBookData.close();
            printException.close();
        }
    }
	
	//Parsing the book data from the file to an array 
	public static String[] parseLine(String line) {
        String[] fields = new String[6]; // Fixed size array
        int fieldIndex = 0;
        String currentField = "";
        boolean insideQuotes = false;

        for (int i = 0; i < line.length(); i++) {
            char currentChar = line.charAt(i);

            if (currentChar == '"') {
                // Toggle insideQuotes when we encounter a quote
                insideQuotes = !insideQuotes;
            } else if (currentChar == ',' && !insideQuotes) {
                // If we're not inside quotes, comma indicates end of a field
                fields[fieldIndex] = currentField;
                currentField = ""; // Reset currentField for the next field
                fieldIndex++;

                // Stop if we reach the fixed field limit (6)
                if (fieldIndex >= fields.length) {
                    break;
                }
            } else {
                // Append the current character to the current field
                currentField += currentChar;
            }
        }

        // Add the last field (if we haven't reached the field limit)
        if (fieldIndex < fields.length) {
            fields[fieldIndex] = currentField;
        }

        return fields;
    }
	
	
	
	
	
	
	/**
     * Validates a 10-digit ISBN number using the ISBN-10 checksum.
     *
     * @param isbn10 The 10-digit ISBN to validate.
     * @return boolean True if the ISBN is valid.
     * @throws BadIsbn10Exception If the ISBN is invalid.
     */
    
	//Validate ISBN 10 method 
	public static boolean validateisbn10( String isbn10 , String fileName , String bookData ) throws BadIsbn10Exception {
		boolean isValideisbn10 = false;
		int sum = 0;
		String isbnArray[] = isbn10.trim().split("");
		
		for (int i = 0; i < isbnArray.length; i++) {
			if(isbnArray[i].equalsIgnoreCase("X")) {
				throw new BadIsbn10Exception("semantic error in file: " + fileName +
                        "\n====================\nError: invalid ISBN 10" +
                        "\n Record: "+ bookData );
			}
			int singleIsbn = Integer.parseInt(isbnArray[i]);
	        sum += (10 - i) * singleIsbn;
	    }
		
		if(sum % 11 == 0) {
			isValideisbn10 = true;
		}else {
			throw new BadIsbn10Exception("semantic error in file: " + fileName +
                                         "\n====================\nError: invalid ISBN 10" +
                                         "\n Record: "+ bookData );
		}
		return isValideisbn10;
	}
	
	//Validate ISBN 13 method 
    /**
     * Validates a 13-digit ISBN number using the ISBN-13 checksum.
     *
     * @param isbn13 The 13-digit ISBN to validate.
     * @return boolean True if the ISBN is valid.
     * @throws BadIsbn13Exception If the ISBN is invalid.
     */
    public static boolean validateisbn13(String isbn13, String fileName, String bookData) throws BadIsbn13Exception {
        boolean isValideisbn13 = false;
        int sum = 0;
        int singleIsbn = 0;
        String isbnArray[] = isbn13.trim().split("");
        for (int i = 0; i < isbnArray.length; i++) {
            if(isbnArray[i].equalsIgnoreCase("X")) {
                throw new BadIsbn13Exception("semantic error in file: " + fileName +
                                            "\n====================\nError: invalid ISBN 13" +
                                            "\nRecord: "+ bookData );
            }
            singleIsbn = Integer.parseInt(isbnArray[i]);
            if(i % 2 != 0) {
                sum += 3 * singleIsbn;
            } else {
                sum += singleIsbn;
            }
        }
        if(sum % 10 == 0) {
            isValideisbn13 = true;
        } else {
            throw new BadIsbn13Exception("semantic error in file: " + fileName +
                                        "\n====================\nError: invalid ISBN 13" +
                                        "\nRecord: "+ bookData );
        }
        return isValideisbn13;
    }

    //Validate Price method
    /**
     * Validates that the price is a positive number.
     *
     * @param price The price of the book.
     * @return boolean True if the price is valid.
     * @throws BadPriceException If the price is invalid.
     */
    public static boolean validatePrice(double price, String fileName, String bookData) throws BadPriceException {
        // Check if price is a positive number
        if(price > 0) {
            return true;
        } else {
            throw new BadPriceException("semantic error in file: " + fileName +
                                        "\n====================\nError: invalid price" +
                                        "\nRecord: "+ bookData );
        }
    }

    //Validate Year method
    /**
     * Validates that the year is between 1995 and 2010 inclusive.
     *
     * @param year The year of publication.
     * @return boolean True if the year is valid.
     * @throws BadYearException If the year is invalid.
     */
    public static boolean validateYear(int year, String fileName, String bookData) throws BadYearException {
        // Check if year is between 1995 and 2010 inclusive
        if(year >= 1995 && year <= 2010) {
            return true;
        } else {
            throw new BadYearException("semantic error in file: " + fileName +
                                        "\n====================\nError: invalid year" +
                                        "\nRecord: "+ bookData );
        }
    }

	
	public static void do_part3() {
		//Create a list to hold arrays of book objects 	each representing one binary file.
		String[] binaryFiles = {"Cartoons_Comics_Books.csv.ser", "Hobbies_Collectibles_Books.csv.ser", "Movies_TV_Books.csv.ser", "Music_Radio_Books.csv.ser", 
				"Nostalgia_Eclectic_Books.csv.ser", "Old_Time_Radio_Books.csv.ser", "Sports_Sports_Memorabilia_Books.csv.ser", "Trains_Planes_Automobiles_Books.csv.ser"};
		
		deserializedArray(binaryFiles);
	}
	public static Book[][] deserializedArray(String [] binaryFiles) {
	    // Define an array of arrays to hold Book objects for each file
	    Book[][] bookCollections = new Book[binaryFiles.length][];

	    // Iterate through each file, deserializing its contents into the respective array
	    
	    
	    for (int i = 0; i < binaryFiles.length; i++) {
	        String fileName = binaryFiles[i];
	        
	        try (ObjectInputStream readObject = new ObjectInputStream(
	                new FileInputStream("/Users/anasmanguer/Desktop/Record-Management-/data/" + fileName))) {
	        	
	            
	            // Deserialize the array of Books
	            Book[] deserializedArray = (Book[]) readObject.readObject();
	            
	            if (deserializedArray != null) {
	                // Create a new array to store copies of each Book in the deserialized array
	                bookCollections[i] = new Book[deserializedArray.length];
	                
	                // Deep copy each Book object into the new array
	                for (int j = 0; j < deserializedArray.length; j++) {
	                    bookCollections[i][j] = new Book(deserializedArray[j]); // Assuming Book has a copy constructor
	                }
	            } else {
	                // Assign an empty array if deserializedArray is null
	                bookCollections[i] = new Book[0];
	                System.out.println("Warning: Deserialized array is null for file: " + fileName);
	            }
	            
	        } catch (IOException | ClassNotFoundException e) {
	            System.out.println("Error reading from file: " + fileName + "!");
	            // Optional: Assign an empty array if there's an error, to avoid nulls in bookCollections
	            bookCollections[i] = new Book[0];
	        }
	    }

	    displayMenu(bookCollections);
	    // Return the array of arrays of Book objects
	    return bookCollections;
	}
	public static void displayMenu(Book[][]book) {
		String choice = "Cartoons_Comics_Books.csv.ser ("+book[0].length+" records)";
		int []token = new int [1];
		token[0]=0;
		int indexFile  = 0;
		int number = 0;
		String menu = "";
		int subMenu = 0 ; 
		Scanner sc = new Scanner (System.in);
		
		do {
			//Display the main menu 
			System.out.println("\n╔═══════════════════════════════════════════════════════════╗");
			System.out.println("║                      Main Menu                            ║");
			System.out.println("╚═══════════════════════════════════════════════════════════╝");
			System.out.println("║v view the selected file: "+ choice );
			System.out.println("║s Select a file to view");
			System.out.println("║x Exit");
			System.out.println("╚═══════════════════════════════════════════════════════════╝");
			System.out.println("");
			System.out.print("Enter Your Choice: ");
			System.out.println("\n");
			
			menu = sc.next().trim();
			
			switch (menu.toUpperCase()) {
			
			//Menu option (S) to select file to view 
			case "S":  
				
				do {
					//Display the sub_menu
					System.out.println("╔═══════════════════════════════════════════════════════════╗");
					System.out.println("║                         File Sub-Menu                     ║");
					System.out.println("╚═══════════════════════════════════════════════════════════╝");
					System.out.println("║1  Cartoons_Comics_Books.csv.ser           ("+ book[0].length+" records)");
					System.out.println("║2  Hobbies_Collectibles_Books.csv.ser      ("+ book[1].length+" records)");
					System.out.println("║3  Movies_TV.csv.ser                       ("+ book[2].length+" records)");
					System.out.println("║4  Music_Radio_Books.csv.ser               ("+ book[3].length+" records)");
					System.out.println("║5  Nostalgia_Eclectic_Books.csv.ser        ("+ book[4].length+" records)");
					System.out.println("║6  Old_Time_Radio.csv.ser                  ("+ book[5].length+" records)");
					System.out.println("║7  Sports_Sports_Memorabilia.csv.ser       ("+ book[6].length+" records)");
					System.out.println("║8  Trains_Plans_Automobilia.csv.ser        ("+ book[7].length+" records)");
					System.out.println("║9  Exit                                    ");
					System.out.println("╚═══════════════════════════════════════════════════════════╝\n");
					System.out.print("Enter Your Choice: ");
					
					// Input validation for submenu selection
					
					do {
	                    
	                    while (!sc.hasNextInt()) {
	                        System.out.println("Invalid input! Please enter a number between 1 and 9.");
	                        sc.next(); // Clear invalid input
	                    }
	                    
	                    subMenu = sc.nextInt();
	                    System.out.println("\n");
	                    
	                }while (subMenu < 1 || subMenu > 9);
					
					if (subMenu == 9) {
						System.out.println("Exiting file sub-menu!");
						break;
					}
					
					switch(subMenu) {
					
					case 1: 
						choice  = "Cartoons_Comics_Books.csv.ser           ("+book[0].length +" records)";
						indexFile = 0;
						break;
					case 2: 
						choice  = "Hobbies_Collectibles_Books.csv.ser      ("+book[1].length+" records)";
						indexFile = 1;
						break;
					case 3: 
						choice  = "Movies_TV.csv.ser                       ("+book[2].length+" records)";
						indexFile = 2;
						break;
					case 4: 
						choice  = "Music_Radio_Books.csv.ser               ("+book[3].length+" records)";
						indexFile = 3;
						break;
					case 5: 
						choice  = "Nostalgia_Eclectic_Books.csv.ser         ("+book[4].length+" records)";
						indexFile = 4;
						break;
					case 6: 
						choice  = "Old_Time_Radio.csv.ser                  ("+book[5].length+" records)";
						indexFile = 5;
						break;
					case 7: 
						choice  = "Sports_Sports_Memorabilia.csv.ser       ("+book[6].length+" records)";
						indexFile = 6;
						break;
					case 8: 
						choice  = "Trains_Plans_Automobilia.csv.ser        ("+book[7].length+" records)";
						indexFile = 7;
						break;
					}
				}while (subMenu >= 1 && subMenu <= 9);
                
				break;
				
			//Menu option (V) to view book records 
			case "V":
				
				System.out.println("viewing: " + choice );
				// Input validation for number of records
				do {
					System.out.println();
	                System.out.print("Enter the number of records to display (0 to end session): ");
	                while (!sc.hasNextInt()) {
	                    System.out.println("Invalid input! Please enter a valid number.");
	                    sc.next(); // Clear invalid input
	                }
	                number = sc.nextInt();	
		            
					// Check if the main array and the specific sub-array (row) are not empty
				    if (book != null && book.length > 0 && book[indexFile] != null && book[indexFile].length > 0) {
				        // Call the displayBookRecursion method if there are books to display
				        displayBookRecursion(book, number, indexFile, token);
				    } else {
				        System.out.println("\nNo books available to display in the specified file index.");
				    }
				}while (number != 0);
	            token[0] = 0;
				break;
				
			//Menu option (X) to exit 	
			case "X":
                System.out.println("System exiting! Thank you for using this program! ");
                break;
                
            //Invalid Menu option 
            default:
                System.out.println("Invalid option! Please enter 's', 'v', or 'x'.");
				
			}
			
		}while(!menu.equalsIgnoreCase("x"));
		sc.close();
	}
	public static int displayBookRecursion(Book[][] book, int n, int indexFile, int[] token) {
		
		// If n is 0, end the viewing session and return to the main menu
	    if (n == 0) {
	        System.out.println("Viewing session ended. Returning to the main menu.");
	        return 0;
	    }
	    // Check if beginning of file (BOF) is reached
	    if (token[0] < 0) {
	        System.out.println("BOF has been reached! Can't read book records anymore!");
	        return 0;
	    }
	    // Check if end of file (EOF) is reached
	    if (token[0] >= book[indexFile].length) {
	        System.out.println("EOF has been reached! Can't read book records anymore!");
	        return 0;
	    }

	    // Display the current book record
	    System.out.println("\nDisplaying book record #" + (token[0] + 1) + ": ");
	    System.out.println(book[indexFile][token[0]]);

	    // Recursive cases
	    if (n > 1) {
	    	token[0]++;
	        return displayBookRecursion(book, n - 1, indexFile, token);
	    } else if (n < -1) {
	    	token[0]--;
	        return displayBookRecursion(book, n + 1, indexFile, token);
	    }

	    // If n is 1 or -1, stop recursion
	    return 1;
	}
}