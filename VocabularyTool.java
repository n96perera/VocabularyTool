import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;

public class VocabularyTool {

    private static final String FILE_NAME = "vocabulary.txt";
    private Map<String, String> vocabulary = new TreeMap<>();
    private boolean isModified = false; // Track if there are any changes to save

    // Load vocabulary from file
    private void loadVocabulary() {
        File file = new File(FILE_NAME);
        if (!file.exists()) return;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(":", 2);
                if (parts.length == 2) {
                    vocabulary.put(parts[0].trim(), parts[1].trim());
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading vocabulary file: " + e.getMessage());
        }
    }

    // Save vocabulary to file only if there were modifications
    private void saveVocabulary() {
        if (!isModified) return; // Skip saving if nothing was changed

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (Map.Entry<String, String> entry : vocabulary.entrySet()) {
                writer.write(entry.getKey() + " : " + entry.getValue());
                writer.newLine();
            }
            System.out.println("Vocabulary Record saved successfully.");
        } catch (IOException e) {
            System.out.println("Error saving vocabulary: " + e.getMessage());
        }
    }

    // List all words in the vocabulary
    private void listVocabulary() {
        if (vocabulary.isEmpty()) {
            System.out.println("No words in the vocabulary");
            return;
        }

        System.out.println("\nVocabulary List:");
        vocabulary.forEach((word, meaning) -> System.out.println(word + " : " + meaning));
    }

    // Add or update a word in the vocabulary
	private void addWord(Scanner scanner) {
		String word = "";
		String meaning = "";

		// Cheking the word is not empty
		while (word.isEmpty()) {
			System.out.println("Enter the word:");
			word = scanner.nextLine().trim();
			if (word.isEmpty()) {
				System.out.println("Word cannot be empty. Please try again.");
			}
		}

		//Check if the word already exists
		if (vocabulary.containsKey(word)) {
			System.out.println("The word already exists. Current meaning: " + vocabulary.get(word));
			System.out.println("Do you want to replace the meaning? (y/n)");
			if (!confirm(scanner)) {
				System.out.println("Word is unchanged.");
				return;
			}
		}

		//Cheking that the meaning is not empty
		while (meaning.isEmpty()) {
			System.out.println("Enter the meaning:");
			meaning = scanner.nextLine().trim();
			if (meaning.isEmpty()) {
				System.out.println("Meaning cannot be empty. Please try again.");
			}
		}

		vocabulary.put(word, meaning);
		isModified = true;
		System.out.println("Word added/updated successfully.");
}


    // Search  a word in the vocabulary
    private void searchWord(Scanner scanner) {
        System.out.println("Enter the word to search:");
        String word = scanner.nextLine().trim();

        if (vocabulary.containsKey(word)) {
            System.out.println(word + " : " + vocabulary.get(word));
        } else {
            System.out.println("Word not found in the vocabulary.");
        }
    }

    // Remove a word from the vocabulary
    private void removeWord(Scanner scanner) {
        System.out.println("Enter the word to remove:");
        String word = scanner.nextLine().trim();

        if (vocabulary.containsKey(word)) {
            vocabulary.remove(word);
            isModified = true;
            System.out.println("Word removed successfully.");
        } else {
            System.out.println("Word not found in the vocabulary.");
        }
    }

    private boolean confirm(Scanner scanner) {
        while (true) {
            String choice = scanner.nextLine().trim().toLowerCase();
            if (choice.equals("y")) {
                return true;
            } else if (choice.equals("n")) {
                return false;
            } else {
                System.out.println("Invalid input. Please enter 'y' or 'n'.");
            }
        }
    }
	
	public static void main(String[] args) {
        VocabularyTool tool = new VocabularyTool();
        tool.loadVocabulary();

        try (Scanner scanner = new Scanner(System.in)) {
            boolean running = true;

            while (running) {
                System.out.println("\nEnter a command: \n l: List vocabulary \n a: Add Words \n s: Search Words \n r: Remove Words \n q: Quit");
				System.out.println("\nYour Command: ");
                String command = scanner.nextLine().trim().toLowerCase();

                switch (command) {
                    case "l":
                        tool.listVocabulary();
                        break;
                    case "a":
                        tool.addWord(scanner);
                        break;
                    case "s":
                        tool.searchWord(scanner);
                        break;
                    case "r":
                        tool.removeWord(scanner);
                        break;
                    case "q":
                        tool.saveVocabulary();
                        running = false;
                        break;
                    default:
                        System.out.println("Invalid command! Please enter 'l', 'a', 's', 'r', or 'q'.");
                }
            }
        }
    }
}
