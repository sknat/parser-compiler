package calculator;
import shared.*;
import java.io.IOException;

public class RunCalculator {

	public static void main (String[] argv) throws IOException, EvalException { 
		Lexer lexer=new Lexer("./code_calc.txt");
		ExpressionFactory ef = new ExpressionFactory(lexer);
		System.out.println(ef);
	}
}

