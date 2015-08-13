import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.TreeMap;

public class Concordance {
	// Class variables
	String inputFilePath;
	// Maps each word to a FreqAndOccur object
	TreeMap<String, FreqAndOccur> frequencyMap = new TreeMap<String, FreqAndOccur>();
	boolean printSentenceNumbers = false;
	int sentenceCount = 1;

	public static void main(String[] args) {
		// handle command line args
		if (args.length < 1) {
			System.out.println("Usage: java Concordance <path> [L] \n");
			System.out.println("<path> \t Path to the input text file");
			System.out
					.println("L \t Optional parameter that displays line numbers");
		} else {
			Concordance concordance = new Concordance(args[0]);
			if (args.length > 1 && args[1].toLowerCase().equals("l")) {
				concordance.printSentenceNumbers = true;
			}
			concordance.printConcordance();
		}

	}

	// Prints the concordance generated
	public Concordance(String inputFilePath) {
		this.inputFilePath = inputFilePath;
		generateConcordance();
	}

	private void printConcordance() {
		for (String key : frequencyMap.keySet()) {
			System.out
					.printf("%-24s  %d", key, frequencyMap.get(key).frequency);
			if (printSentenceNumbers) {
				System.out.print(":"
						+ frequencyMap.get(key).sentenceNumbers.toString());

			}
			System.out.println();
		}

	}

	// reads each line and calls LineParser to get wordList as well as update
	// sentenceCount
	private void generateConcordance() {

		try {
			BufferedReader bufferedReader = new BufferedReader(new FileReader(
					inputFilePath));
			String line = "";

			LineParser lineParser = new LineParser();
			while ((line = bufferedReader.readLine()) != null) {
				ArrayList<String> wordList = lineParser.parseLine(line, this);
				processLine(wordList, sentenceCount);
			}

			bufferedReader.close();
		} catch (FileNotFoundException e) {
			System.out.println("File not found " + inputFilePath);
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("Error in reading file " + inputFilePath);
			e.printStackTrace();
		}

	}

	// Updates the frequency and sentence number of the parsed word.
	private void processLine(ArrayList<String> wordList, int sentenceCount) {
		for (String s : wordList) {
			if (frequencyMap.containsKey(s)) {
				frequencyMap.get(s).addFreqAndOccur(sentenceCount);
				;
			} else {
				frequencyMap.put(s, new FreqAndOccur(sentenceCount));
			}
		}
	}

}
