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
    static ArrayList<String> tokens = new ArrayList<String>();
    static int arrayCounter = 0;

    // Main
    public static void main(String[] args) {
        try {
            br = new BufferedReader(new FileReader("dat.dat"));
        } catch (FileNotFoundException fnfe) {
            System.out.println(fnfe.getMessage());
        }
        System.out.println("===============================================");
        // Read and confirm file
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
        // Readfile call
        readFile();
        tokens.remove(tokens.size() - 1);
        System.out.println(syntax);

        if (isProgram()) {
            System.out.println("Yups");
        }

        System.out.println("File read successfully.");
    }

    // <program> = program begin <statement_list> end
    public static boolean isProgram() {
        if (tokens.remove(0).equals("program")) {
            if (tokens.remove(0).equals("begin")) {
                if (isStatementList()) {
                    if (tokens.remove(tokens.size() - 1).equals("end")) {
                        return true;
                    }
                }
            }
        }
        System.out.println("Program");
        return false;
    }

    // <statement_list> = <statement> {;<statement>}
    public static boolean isStatementList() {
        if (isStatement()) {
            while(tokens.get(0).equals(";")){
                if(isStatement());
            }
            return true;
        }
        System.out.println("Not statement list");
        return false;
    }

    // <statement> = <assignment_statement> | <if_statement> | <loop_statement>
    public static boolean isStatement() {
        if (isAssignmentStatement() || isIfState() || isLoopState()) {
            return true;
        }
        System.out.println("Not statement");
        return false;
    }

    // <assignment_statement> = <variable> = <expression>
    public static boolean isAssignmentStatement() {
        if (isVariable()) {
            if (tokens.remove(0).equals("=")) {
                if (isExpression()) {
                    return true;
                }
            }
        }
        System.out.println("Not assignment statement");
        return false;
    }

    // <variable> = identifier (An identifier is a string that begins with a letter
    // followed by 0 or more letters and/or digits)
    public static boolean isVariable() {
        if (Character.isAlphabetic(tokens.remove(0).charAt(0))) {
            return true;
        }
        System.out.println("Not variable");
        return false;
    }

    // <expression> = <term> { (+|-) <term>}
    public static boolean isExpression() {
        if (isTerm()) {
            do {
                if (!(tokens.get(0).equals("+") || tokens.get(0).equals("-"))) {
                    tokens.remove(0);
                    System.out.println("Not + | -");
                    return false;
                }
            } while (isTerm());
            return true;
        }
        System.out.println("Not expression");
        return false;
    }

    // <term> = <factor> {(* | /) <factor> }
    public static boolean isTerm() {
        if (isFactor()) {
             while (tokens.get(0).equals("*") || tokens.get(0).equals("/")){
                 tokens.remove(0);
                if(isFactor());
            }
            return true;
        }
        System.out.println("Not term");
        return false;
    }

    // <factor> = identifier | int_constant | (<expr>)
    public static boolean isFactor() {
        String s = tokens.remove(0);
        if (s.charAt(0) == '(') {
            if (isExpression()) {
                if (tokens.remove(0).equals(")"))
                    return true;
            }
            System.out.println("Not bracketed expression");
            return false;
        }
        if (Character.isAlphabetic(s.charAt(0))) {
            return true;
        }
        try {
            Integer.parseInt(s);
            return true;
        } catch (Exception e) {
            System.out.println("Not factor");
            return false;
        }
    }

    // <if_statement> = if (<logic_expression>) then <statement>
    public static boolean isIfState() {
        if (tokens.remove(0).equals("if")) {
            if (tokens.remove(0).equals("(")) {
                if (isLogicExpression()) {
                    if (tokens.remove(0).equals(")")) {
                        if (tokens.remove(0).equals("then")) {
                            if (isStatement()) {
                                return true;
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    // <logic_expression> = <variable> (< | >) <variable> (Assume that logic
    // expressions have only less than or greater than operators)
    public static boolean isLogicExpression() {
        if (isVariable()) {
            if (tokens.remove(0).equals("<") || tokens.remove(0).equals(">")) {
                if (isVariable()) {
                    return true;
                }
            }
        }
        return false;
    }

    // <loop_statement> = loop (<logic_expression>) <statement>
    public static boolean isLoopState() {
        if (tokens.remove(0).equals("loop")) {
            if (tokens.remove(0).equals("(")) {
                if (isLogicExpression()) {
                    if (tokens.remove(0).equals(")")) {
                        if (isStatement()) {
                            return true;
                        }
                    }
                }
            }

        }
        return false;
    }

    public static void readFile() {
        getChar();
        do {
            lex();
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            tokens.add(lexeme);
            lexeme = "";
        } while (nextToken != EOF);
    }

    // Append character to lexeme
    public static void addChar() {
        lexeme += nextChar;
    }

    public static void getChar() {
        int current;
        try {
            // Read character
            current = br.read();
            if (current != EOF) {
                nextChar = (char) current;
                if (Character.isAlphabetic(nextChar))
                    charClass = LETTER;
                else if (Character.isDigit(nextChar))
                    charClass = DIGIT;
                else
                    charClass = UNKNOWN;
            } else {
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
