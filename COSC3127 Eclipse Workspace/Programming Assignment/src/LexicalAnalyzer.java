import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;

public class LexicalAnalyzer {

	// Character Classes
	static final int LETTER = 0;
	static final int DIGIT = 1;
	static final int UNKNOWN = 99;

	// Token Codes
	static final int INT_LIT = 10;
	static final int IDENT = 11;
	static final int ASSIGN_OP = 20;
	static final int ADD_OP = 21;
	static final int SUB_OP = 22;
	static final int MULT_OP = 23;
	static final int DIV_OP = 24;
	static final int LEFT_PAREN = 25;
	static final int RIGHT_PAREN = 26;
	static final int LESS_OP = 27;
	static final int GREAT_OP = 28;
	static final int ASSN_OP = 29;
	static final int TERMINAL = 30;
	static final int EOF = -1;

	// Variables
	static int charClass;
	static String lexeme = "";
	static char nextChar;
	static int lexLen;
	static int token;
	static int nextToken;
	static File file;
	static Scanner input = new Scanner(System.in);
	static BufferedReader br;
	static String syntax = "";
	static ArrayList<Token> tokens = new ArrayList<Token>();
	static int arrayCounter = 0;

	//Main
	public static void main(String[] args) {
		try {
			br = new BufferedReader(new FileReader("dat.dat"));
		} catch (FileNotFoundException fnfe) {
			System.out.println(fnfe.getMessage());
		}
		System.out.println("===============================================");
		//Read and confirm file
		while (true) {
			System.out.print("Enter a file name: ");
			file = new File(input.nextLine());

			if (!file.exists()) {
				System.out.println("Invalid file name...");
				System.out.println();
				continue;
			} else {
				break;
			}
		}
		//Readfile call
		readFile();
		
		System.out.println(syntax);
		
		if(isProgram()) {
			
		}
		
		
		System.out.println("File read successfully.");
	}

	public static boolean isProgram() {
		if(tokens[0].token.toLowerCase() == "program") {
			if(tokens[1].token.toLowerCase() == "begin") {
				if(tokens[tokens.length].token.toLowerCase() == "end") {
					return true;
				}
			}
		}
		return false;		
	}
	public boolean isStatementList() {
		return false;
	}
	public boolean isStatement() {
		return false;
		
	}
	public boolean isAssignmentStatement() {
		return false;
		
	}
	public boolean isVariable() {
		return false;
		
	}
	public boolean isExpression() {
		return false;
		
	}
	public boolean isTerm() {
		return false;
		
	}
	public boolean isFactor() {
		return false;
		
	}
	public boolean isIfState() {
		return false;
		
	}
	public boolean isLogicExpression() {
		return false;
		
	}
	public boolean isLoopState() {
		return false;
		
	}
	
	
	
	public static void readFile(){
		getChar();
		do {
			lex();
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			tokens.add(new Token(lexeme, charClass));
			lexeme = "";
		} while(nextToken != EOF);
	}
	
		//Append character to lexeme
	public static void addChar() {
			lexeme += nextChar;
		}
	
	public static void getChar() {
			int current;
			try {
				//Read character
				current = br.read();
			if(current != EOF) {
				nextChar = (char) current;
				if(Character.isAlphabetic(nextChar)) charClass = LETTER;
				else if(Character.isDigit(nextChar)) charClass = DIGIT;
				else charClass = UNKNOWN;
			}else {
				charClass = EOF;
			}
			
		} catch (IOException ioe) {
			System.out.println(ioe.getMessage());
		}
	}

	public static void getNonBlank() {
		while (Character.isSpace(nextChar)) {
			getChar();
		}
	}

	public static void lookup(char ch) {
		switch (ch) {
		case '(':
			addChar();
			nextToken = LEFT_PAREN;
			break;
		case ')':
			addChar();
			nextToken = RIGHT_PAREN;
			break;
		case '+':
			addChar();
			nextToken = ADD_OP;
			break;
		case '-':
			addChar();
			nextToken = SUB_OP;
			break;
		case '*':
			addChar();
			nextToken = MULT_OP;
			break;
		case '/':
			addChar();
			nextToken = DIV_OP;
			break;	
		case '>':
			addChar();
			nextToken = GREAT_OP;
			break;	
		case '<':
			addChar();
			nextToken = LESS_OP;
			break;	
		case '=':
			addChar();
			nextToken = ASSN_OP;
			break;	
		case ';':
			addChar();
			nextToken = TERMINAL;
			break;	
		
		}
		
	}

	public static void lex() {
		lexLen = 0;

		getNonBlank();

		switch (charClass) {

		case LETTER:
			addChar();
			getChar();
			while (charClass == LETTER || charClass == DIGIT) {
				addChar();
				getChar();
			}
			nextToken = IDENT;
			break;

		case DIGIT:
			addChar();
			getChar();
			while (charClass == DIGIT) {
				addChar();
				getChar();
			}
			nextToken = INT_LIT;
			break;

		case UNKNOWN:
			lookup(nextChar);
			getChar();
			break;
		
		case EOF:
			nextToken = EOF;
		}
		
		System.out.println("Next token is " + nextToken + ", Next lexeme is " + lexeme);

	}
}
