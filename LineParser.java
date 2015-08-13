import java.util.ArrayList;

public class LineParser {
	/*
	 * Class for parsing words from line and updating sentenceCount in
	 * Concordance class
	 */

	public ArrayList<String> parseLine(String line, Concordance concordance) {

		// Go through the input line and update the sentenceCount in concordance
		for (int i = 0; i < line.length(); i++) {
			char c = line.charAt(i);
			// check if c is a sentence ending punctuation.
			// special check for period to avoid counting periods in words
			// like (i.e., e.g.)
			if (";?!".indexOf(c) != -1) {
				concordance.sentenceCount++;
			}
			// no periods two spaces left from c
			else if (i > 2 && c == '.' && line.charAt(i - 2) != '.') {

				if ((i + 2) < line.length()) {
					// we are not dealing with words like i.e, e.g
					if (line.charAt(i + 2) != '.') {
						concordance.sentenceCount++;
					}
				} else {
					concordance.sentenceCount++;
				}
			}

		}

		// Go through the input line and split it into words
		ArrayList<String> wordList = new ArrayList<String>();
		String buffer = "";
		for (int i = 0; i < line.length(); i++) {
			char c = line.charAt(i);
			// check if we encounter the end of a word

			if ((" ,;?!:\"\\]\\[\\(\\)".indexOf(c) != -1)
					|| (i > 2 && c == '.' && line.charAt(i - 2) != '.'
							&& (i + 2) < line.length() && line.charAt(i + 2) != '.')) {
				// check if the buffer is indeed a word
				String checkedBufferWord = checkBufferWord(buffer);
				if (checkedBufferWord != null) {
					wordList.add(checkedBufferWord);
				}
				buffer = "";
			} else {
				buffer = buffer + c;
			}
		}

		// check the last buffer
		String checkedBufferWord = checkBufferWord(buffer);
		if (checkedBufferWord != null) {
			wordList.add(checkedBufferWord);
		}

		return wordList;
	}

	private String checkBufferWord(String word) {
		word = word.trim().toLowerCase(); // remove unwanted spaces and convert
											// to lowercase

		// Ignore empty strings, and strings that are not words
		if (word.length() == 0 || !isWord(word))
			return null;

		return word;
	}

	// Returns true if the parameter word is composed of just letters and
	// possibly apostrophe and period
	private boolean isWord(String word) {
		// ignore single apostrophe
		if (word.equals("'"))
			return false;

		// check each char and determine whether the word is composed of
		// letters, apostrophe or period
		boolean isWord = true;
		for (char s : word.toCharArray()) {
			isWord = isWord
					&& ((s >= 'a' && s <= 'z') || (s == '\'') || (s == '.'));
		}

		return isWord;
	}
}
