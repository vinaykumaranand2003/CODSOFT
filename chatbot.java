import java.util.Scanner;

public class SimpleChatBot {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Hello! I'm a simple chatbot. How can I assist you today?");

        while (true) {
            System.out.print("> ");
            String userInput = scanner.nextLine().toLowerCase();

            // Exit the chatbot if the user says "bye"
            if (userInput.contains("bye")) {
                System.out.println("Goodbye! Have a great day!");
                break;
            }

            // Respond to greetings
            if (userInput.contains("hello") || userInput.contains("hi")) {
                System.out.println("Hello! How can I help you?");
            }
            // Respond to asking about the chatbot
            else if (userInput.contains("who are you") || userInput.contains("what are you")) {
                System.out.println("I'm a simple chatbot created to assist you with basic questions.");
            }
            // Respond to asking about the weather
            else if (userInput.contains("weather")) {
                System.out.println("I'm not equipped to check the weather right now, but I hope it's sunny!");
            }
            // Respond to asking about time
            else if (userInput.contains("time")) {
                System.out.println("I can't tell the time, but it's always a good time to chat!");
            }
            // Catch-all response for unknown queries
            else {
                System.out.println("I'm not sure how to respond to that. Can you ask something else?");
            }
        }

        scanner.close();
    }
}