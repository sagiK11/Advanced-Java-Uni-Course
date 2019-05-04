import java.util.Random;

public class WordsBank {
	private String[] words = { "computer" , "radio" , "table" , "smartphone" , "tablet" , "train" , "airplane" , "hammer" , "picture" , "remote" };

	public String getRandomWord() {
		int randomIndex = new Random( ).nextInt( words.length );
		return words[ randomIndex ];
	}
}
