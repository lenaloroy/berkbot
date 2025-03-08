import java.util.Scanner;
import java.io.*;

public class Chatbot {

    public static void main(String[] args) throws IOException{
        Scanner scanner = new Scanner(System.in);

        // Start the chat
        intro(scanner);
    }

    // Introduction
    public static void intro(Scanner scanner) throws IOException{
        User user = new User();
        BerkBotHello();
    
        // Get user name and check if a file exists
        setUserName(user, getValidInput(scanner, "What's your name? "));
    
        if (userFileExists(user)){
            loadUserData(user);
            System.out.println("Welcome back, " + getUserName(user) + ".");
    
            System.out.print("Would you like to retake the quiz? (yes/no): ");
            String retakeQuiz = scanner.nextLine().toLowerCase();
    
            if (retakeQuiz.equals("yes")){
                System.out.println("Let's do the quiz again!");
                resetUserData(user);
            }else{
                System.out.println("Let's go over your answers.");
                referencePreviousAnswers(user, scanner);
                closingStatement(getUserName(user));
                return; 
            }
        }else{
            System.out.println("Hi " + getUserName(user) + ", nice to meet you.");
            httydIntroduction();
        }
    
        // Print the updated character list
        printCharacterList(httydCharacters);
    
        System.out.println("What are your 3 favorite characters from How to Train Your Dragon:");
    
        // Ask for top 3 favorite characters
        for (int i = 0; i < 3; i++){
            String prompt = "Character " + (i + 1) + ": ";
            String character = getValidCharacter(scanner, httydCharacters, prompt, user);
            setFavCharacter(user, i, character);
    
            System.out.println(randomResponse());
    
            // Ask the user why they like this character
            String reason = "";
            while (reason.isEmpty()){
                System.out.print("Why do you like " + character + "? ");
                reason = scanner.nextLine();
                if (reason.isEmpty()){
                    System.out.println("Please provide a reason.");
                }
            }
            setCharacterOpinion(user, i, reason);
    
            System.out.println("Thank you");
        }
    
        // Ask for the favorite movie
        setFavMovie(user, getValidMovie(scanner));
        System.out.println(numberToLetter(getFavMovie(user)));
    
        // Ask for the movie opinion
        String movieOpinion = "";
        while (movieOpinion.isEmpty()) {
            System.out.print("Why do you like this movie?");
            movieOpinion = scanner.nextLine();
            if (movieOpinion.isEmpty()) {
                System.out.println("Please provide a reason.");
            }
        }
        setMovieOpinion(user, movieOpinion);
    
        // Save user data
        saveUserData(user);
    
        // Closing statement
        closingStatement(getUserName(user));
    }

    public static final String[] httydCharacters = {"toothless", "hiccup", "astrid", "snotlout", "fishlegs", "ruffnut", "tuffnut", "stoick", "gobber", "valka"};
    
    // Greeting message
    public static void BerkBotHello(){
        System.out.println("Hi. I'm BerkBot, your favourite chatbot.");
        System.out.println("I'd love to chat with you.");
    }

    // Introduction to the topic
    public static void httydIntroduction(){
        System.out.println("My favorite movies are the How to Train Your Dragon movies.");
        System.out.println("I'd love to talk about them.");
    }

    // Generate response for favourite movie
    public static String numberToLetter(String movie){
        String film = "N/A"; 

        if (movie.equals("1")){
            film = "first";
        }else if (movie.equals("2")){
            film = "second";
        }else if (movie.equals("3")){
            film = "last";
        }

        return "I loved the " + film + " How to Train Your Dragon movie too.";
    }

    // Generate random response for characters
    public static String randomResponse(){
        final String[] responses = {"I love that character!", "They're one of my favourites too.", "Couldn't pick a better option myself."};
        int randomNumber = (int) (Math.random() * responses.length);
        return responses[randomNumber];
    }

    // Print the list of available characters
    public static void printCharacterList(String[] characters){
        System.out.println("Here are the characters:");
        for (int i = 0; i < characters.length; i++){
            System.out.println(characters[i]);
        }
    }

    // Validate input
    public static String getValidInput(Scanner scanner, String message){
        String input = "";
        
        while (input.isEmpty()) {
            System.out.print(message);
            input = scanner.nextLine();
            if (input.isEmpty()) {
                System.out.println("Invalid input. Please try again.");
            }
        }
        
        return input;
    }

    // Validate movie input
    public static String getValidMovie(Scanner scanner){
        String movie = "";

        // Using a while loop to validate movie input
        while (!movie.equals("1") && !movie.equals("2") && !movie.equals("3")){
            System.out.print("Which of the 3 How to Train Your Dragon movies is your favorite (1, 2, or 3)? ");
            movie = scanner.nextLine();
            if (!movie.equals("1") && !movie.equals("2") && !movie.equals("3")){
                System.out.println("Invalid. Please enter 1, 2, or 3.");
            }
        }
        return movie;
    }
    
    // Validate character input
    public static String getValidCharacter(Scanner scanner, String[] characters, String message, User user){
        String character = "";
    
        // Keep asking for input until a valid character is provided
        while (!validCharacter(characters, character) || alreadyChosen(user, character)){
            System.out.print(message);
            character = scanner.nextLine().toLowerCase();
    
            if (!validCharacter(characters, character)){
                System.out.println("Invalid. Select a character from the list.");
            }else if (alreadyChosen(user, character)) {
                System.out.println("Character already selected. Please choose a different character.");
            }
        }
        return character;  // Return the valid character
    }
    
    // Check if the input is a valid character
    public static boolean validCharacter(String[] characters, String character){
        for (String c : characters){
            if (c.equals(character)){
                return true;
            }
        }
        return false;
    }

    // checks if the character has already been chosen
    public static boolean alreadyChosen(User user, String character){
        for (int i = 0; i < 3; i++){  
            String favCharacter = getFavCharacter(user, i);  
            if (favCharacter != null && favCharacter.equalsIgnoreCase(character)){
                return true;
            }
        }
        return false;
    }

    // Goodbye message
    public static void closingStatement(String name){
        System.out.println("It's time for me to go. It was nice talking to you, " + name + ".");
    }

    // Method to check if the user has a previous conversation file
    public static boolean userFileExists(User user){
        File userFile = new File(getUserName(user) + ".txt");
        return userFile.exists();
    }

    // Method to load user data from the file
    public static void loadUserData(User user) throws IOException{
        BufferedReader reader = new BufferedReader(new FileReader(getUserName(user) + ".txt"));
        setUserName(user, reader.readLine());
        for (int i = 0; i < 3; i++){
            setFavCharacter(user, i, reader.readLine());
            setCharacterOpinion(user, i, reader.readLine());
        }
        setFavMovie(user, reader.readLine());
        setMovieOpinion(user, reader.readLine());
        reader.close();
    }

    //saves data into the file
    public static void saveUserData(User user) throws IOException{
        PrintWriter writer = new PrintWriter(new FileWriter(getUserName(user) + ".txt"));
        writer.println(getUserName(user));
        for (int i = 0; i < 3; i++){  
            writer.println(getFavCharacter(user, i));  
            writer.println(getCharacterOpinion(user, i));  
        }
        writer.println(getFavMovie(user));  
        writer.println(getMovieOpinion(user));  
        writer.close();
    }

    //reset data
    public static void resetUserData(User user){
        for (int i = 0; i < 3; i++){
            setFavCharacter(user, i, "N/A");
            setCharacterOpinion(user, i, "N/A");
        }
        setFavMovie(user, "N/A");
        setMovieOpinion(user, "N/A");
    }

    // Reference previous answers in the conversation
    public static void referencePreviousAnswers(User user, Scanner scanner){

        System.out.print("Do you want to see your favorite characters? (yes/no): ");
        String response = scanner.nextLine().toLowerCase();
        if (response.equals("yes")){
            System.out.println("Here you go.");
            // List all characters using getter
            for (int i = 0; i < 3; i++){
                System.out.println(getFavCharacter(user, i));
            }

            System.out.print("Do you want to know what you liked about each character? (yes/no): ");
            response = scanner.nextLine().toLowerCase();
            if (response.equals("yes")){
                System.out.println("Here you go.");
                for (int i = 0; i < 3; i++){
                    System.out.println(getFavCharacter(user, i) + " : " + getCharacterOpinion(user, i));
                }
            }else{
                System.out.println("Alright, moving on.");
            }
        }else{
            System.out.println("That's ok. Let's move on.");
        }

        System.out.print("Do you want to know what your favourite movie was? (yes/no): ");
        response = scanner.nextLine().toLowerCase();
        if (response.equals("yes")){
            System.out.println(getFavMovie(user) + " : " + getMovieOpinion(user));
            System.out.println();
        }else{
            System.out.println("That's ok.");
        }
    }

    // Setters for user data
    public static void setUserName(User user, String name){
        user.name = name;
    }

    public static String getUserName(User user){
        return user.name;
    }

    public static void setFavCharacter(User user, int i, String character){
        user.favCharacters[i] = character;
    }

    public static String getFavCharacter(User user, int i){
        return user.favCharacters[i];
    }

    public static void setCharacterOpinion(User user, int i, String opinion){
        user.characterOpinions[i] = opinion;
    }

    public static String getCharacterOpinion(User user, int i){
        return user.characterOpinions[i];
    }

    public static void setFavMovie(User user, String movie){
        user.favMovie = movie;
    }

    public static String getFavMovie(User user){
        return user.favMovie;
    }

    public static void setMovieOpinion(User user, String opinion){
        user.movieOpinion = opinion;
    }

    public static String getMovieOpinion(User user){
        return user.movieOpinion;
    }

}