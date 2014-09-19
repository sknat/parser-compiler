package grammar;
import shared.*;
import java.io.*;

public class RunGrammar {
	public static void main (String[] argv) throws IOException, EvalException { 

		BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("---Grammar compilator---");
		ExpressionFactory expressionFactory;
		System.out.println("Type a grammar input to be parsed and computed");
		System.out.println("Use <&f ./file.ext> to input from a file");
		String in = stdIn.readLine();
		StringLexer stdInLexer = new StringLexer(in);
		if (stdInLexer.lookWord().equals("&f")) {
			stdInLexer.getWord();
			Lexer fileLexer=new Lexer(stdInLexer.getWord());
			expressionFactory = new ExpressionFactory(fileLexer);
		}
		else {
			Lexer writeLexer=new Lexer("./~temp.txt");
			writeLexer.write(in);
			Lexer fileLexer=new Lexer("./~temp.txt");
			expressionFactory = new ExpressionFactory(fileLexer);
		}
		Lexer outLexer=new Lexer("./src/out/RunOutput.java");
		outLexer.write(expressionFactory.toString());
		System.out.println("---------------Transcoded------------------");
	}
}


