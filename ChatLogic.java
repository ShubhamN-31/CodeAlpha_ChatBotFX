import java.util.Random;

public class ChatLogic {
    private Random random = new Random();

    public String getResponse(String input) {
        String cleanInput = input.toLowerCase().trim();

        if (cleanInput.contains("hi") || cleanInput.contains("hello") || cleanInput.contains("hey")) {
            String[] greetings = {
                    "Hey there! 😊",
                    "Hello! How's your coding going?",
                    "Hi! I'm ready to help. 🤖"
            };
            return greetings[random.nextInt(greetings.length)];
        }

        if (cleanInput.contains("how are you")) {
            String[] moods = {
                    "I'm feeling digital and fast! ⚡",
                    "Doing great, just waiting for your next command.",
                    "All systems go! How about you?"
            };
            return moods[random.nextInt(moods.length)];
        }

        if (cleanInput.contains("sql") || cleanInput.contains("database")) {
            return "SQL is great! Are you working on Joins or just basic Queries? 📊";
        }

        if (cleanInput.contains("bye")) {
            return "Catch you later! Happy coding! 👋";
        }

        String[] fallbacks = {
                "I'm not sure I follow. Can you explain that differently? 🤔",
                "That sounds interesting, but I don't have an answer for it yet!",
                "Hmm, I'm still learning. Try asking about 'SQL' or 'Java'!"
        };
        return fallbacks[random.nextInt(fallbacks.length)];
    }
}
