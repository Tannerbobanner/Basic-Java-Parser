import java.io.File;
import java.util.Scanner;

public class LexicalAnalyzer {

	int charClass;
	char[] lexeme = new char[100];
	char nextChar;
	int lexLen;
	int token;
	int nextToken;
	File file = new File("");
	Scanner input;
	
	public LexicalAnalyzer(File file) {
		this.file = file;
	}
	
	public void addChar() {
		if(lexLen <= 98) {
			lexeme[lexLen++] = nextChar;
			lexeme[lexLen] = 0;
		}
		else {
			System.out.print("Error - lexeme is too long \n");
		}
	}
	public void getChar() {
		input = new Scanner(file);
		while(input.hasNext()) {
			if(Character.is)
		}
	}
	public void getNonBlank() {
		while(Character.isSpace(nextChar)) {
			getChar();
		}
	}
	public int lex() {
		
	}
}
