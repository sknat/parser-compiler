package out;
import shared.*;
import java.io.*;

public class RunOutput {
	public static void main (String[] argv) throws IOException, EvalException { 

		BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("---Grammar running under java code---");
		while (true) {
			System.out.println("Type an input to be parsed and computed");
			System.out.println("Use <&q> to quit and <&f ./file.ext> to input from a file");
			String in = stdIn.readLine();
			StringLexer stdInLexer = new StringLexer(in);
			if (stdInLexer.lookWord().equals("&q")) {System.out.println("--------------------Terminated---------------------"); break;} else
				if (stdInLexer.lookWord().equals("&f")) {
					stdInLexer.getWord();
					Lexer fileLexer=new Lexer(stdInLexer.getWord());
					ExpressionFactory expressionFactory = new ExpressionFactory(fileLexer);
					System.out.println(expressionFactory);;
				}
				else {
					Lexer writeLexer=new Lexer("./~temp.txt");
					System.out.println(in);
					writeLexer.write(in);
					Lexer fileLexer=new Lexer("./~temp.txt");
					ExpressionFactory expressionFactory = new ExpressionFactory(fileLexer);
					System.out.println(expressionFactory);;

				}
			System.out.println("----------------------------------------------");
		}
	}
}
//--------------------------------------------
//---Fichier PréCompilé ExpressionFactory.java
class ExpressionFactory {
	Expression expression;
	Parser parser;
	public ExpressionFactory(Lexer lexer) throws IOException {
		Parser parser = new Parser(lexer);
		expression = parser.parse();
	}
	public Value eval() throws EvalException {
		return expression.eval();
	}
	public String represent() throws EvalException {
		return expression.toString();
	}
	public String toString() {
		try {return "Parsed as : "+represent()+" ; computed as : "+eval();} 
		catch (EvalException e) {e.printStackTrace(); return "Evaluation error";}
	}	
}	
class Eegram extends Expression { 
	@Override
	public Value eval() throws EvalException {
		throw new EvalException ("This method should have been overidden");
	}
}
class Eegram_1 extends Eegram {
	Eees $$1;
	Eegramnxt $$3;
	Eegram_1(Eees $$1,Eegramnxt $$3) {this.$$1=$$1;this.$$3=$$3;}
	@Override
	public Value eval() throws EvalException {
		String $1=$$1.eval().asString();
		String $3=$$3.eval().asString();
		VString res = new VString("class ExpressionFactory {\n"+
				"Expression expression;\n"+
				"Parser parser;\n"+
				"public ExpressionFactory(Lexer lexer) throws IOException {\n"+
				"Parser parser = new Parser(lexer);\n"+
				"expression = parser.parse();\n"+
				"}\n"+
				"public Value eval() throws EvalException {\n"+
				"return expression.eval();\n"+
				"}\n"+
				"public String represent() throws EvalException {\n"+
				"return expression.toString();\n"+
				"}\n"+
				"public String toString() {\n"+
				"try {return \"Parsed as : \"+represent()+\" ; computed as : \"+eval();} \n"+
				"catch (EvalException e) {e.printStackTrace(); return \"Evaluation error\";}\n"+
				"}	\n"+				
				"}	\n"+
				$1+
				$3+
				"class EConstant extends Expression {\n"+
				"final Value v;\n"+
				"EConstant(Value v){this.v=v;}\n"+
				"@Override\n"+
				"public Value eval() throws EvalException {return v;}\n"+
				"}\n"+
				"class VBoolean extends Value {\n"+
				"final boolean v;\n"+
				"public String toString () {return (new Boolean (v)).toString();}\n"+
				"VBoolean (boolean v) {this.v = v;}\n"+
				"@Override  \n"+
				"public boolean asBoolean () {return v;}\n"+
				"}\n"+
				"class VString extends Value {\n"+
				"final String v;\n"+
				"public String toString () {return v;}\n"+
				"VString (String v) {this.v = v;}\n"+
				"@Override  \n"+
				"public String asString () {return v;}\n"+
				"}\n"+
				"class VInteger extends Value {\n"+
				"final int v;\n"+
				"public String toString () {return (new Integer (v)).toString();}\n"+
				"VInteger (int v) {this.v = v;}\n"+
				"@Override  \n"+
				"public int asInteger () {return v;}\n"+
				"}\n"+
				"class Parser {\n"+
				"private final Lexer lexer;\n"+
				"public Parser (Lexer lexer) {this.lexer=lexer;}\n"+
				"public Expression parse() throws IOException{\n"+
				"return consume"+((Eees_1) $$1).$$1+" (0);\n"+
				"}\n"+
				$1+
				$3+
				"}\n" );
		return res;
	}
	public String toString () {
		String $1=$$1.toString();
		String $3=$$3.toString();
		return $1+";;"+$3;
	};
}
class Eegramnxt extends Expression { 
	@Override
	public Value eval() throws EvalException {
		throw new EvalException ("This method should have been overidden");
	}
}
class Eegramnxt_1 extends Eegramnxt {
	Eees $$1;
	Eegramnxt $$3;
	Eegramnxt_1(Eees $$1,Eegramnxt $$3) {this.$$1=$$1;this.$$3=$$3;}
	@Override
	public Value eval() throws EvalException {
		String $1=$$1.eval().asString();
		String $3=$$3.eval().asString();
		VString res = new VString($1 + $3 );
		return res;
	}
	public String toString () {
		String $1=$$1.toString();
		String $3=$$3.toString();
		return $1+";;"+$3;
	};
}
class Eegramnxt_2 extends Eegramnxt {
	Eees $$1;
	Eegramnxt_2(Eees $$1) {this.$$1=$$1;}
	@Override
	public Value eval() throws EvalException {
		String $1=$$1.eval().asString();
		VString res = new VString($1);
		return res;
	}
	public String toString () {
		String $1=$$1.toString();
		return $1+";;";
	};
}
class Eees extends Expression { 
	@Override
	public Value eval() throws EvalException {
		throw new EvalException ("This method should have been overidden");
	}
}
class Eees_1 extends Eees {
	String $$1;
	Eebody $$3;
	Eees_1(String $$1,Eebody $$3) {this.$$1=$$1;this.$$3=$$3;}
	@Override
	public Value eval() throws EvalException {
		String $1=$$1;
		String $3=$$3.eval().asString();
		VString res = new VString("");
		int n=0;
		Ebodynxt tmp = ((Ebody) $$3).$$3;
		while (tmp!=null) {tmp=tmp.$$3; n++;}
		String s="private E"+$1+" consume"+$1+" (int lvl) throws IOException {\n"+
				"E"+$1+" res;\n"+
				"lexer.mark();\n";
		for (int i=1;i<n;i++) {
			s=s+"try {res = consume"+$1+"_"+i+" (lvl); lexer.dropMark(); return res;} catch (SyntaxError e"+i+") {\n"+
					"lexer.reset();\n"+
					"lexer.mark();\n";
		};
		s=s+"try {res = consume"+$1+"_"+n+" (lvl); lexer.dropMark(); return res;} catch (SyntaxError e"+n+") {\n"+
				"lexer.reset();\n"+
				"lexer.mark();\n"+
				"throw new SyntaxError (\"Error parsing \"+this);\n"+
				"}\n";
		for (int i=0;i<n;i++) {s=s+"}\n";}
		res=new VString(s+$3); // );
		return res;
	}
	public String toString () {
		String $1=$$1+"";
		String $3=$$3.toString();
		return $1+"::="+$3;
	};
}
class Eebody extends Expression { 
	@Override
	public Value eval() throws EvalException {
		throw new EvalException ("This method should have been overidden");
	}
}
class Eebody_1 extends Eebody {
	Eeline $$1;
	Eebodynxt $$3;
	Eebody_1(Eeline $$1,Eebodynxt $$3) {this.$$1=$$1;this.$$3=$$3;}
	@Override
	public Value eval() throws EvalException {
		String $1=$$1.eval().asString();
		String $3=$$3.eval().asString();
		VString res = new VString(text);
		return res;
	}
	public String toString () {
		String $1=$$1.toString();
		String $3=$$3.toString();
		return $1+"|"+$3;
	};
}
class Eebodynxt extends Expression { 
	@Override
	public Value eval() throws EvalException {
		throw new EvalException ("This method should have been overidden");
	}
}
class Eebodynxt_1 extends Eebodynxt {
	Eeline $$1;
	Eebodynxt $$3;
	Eebodynxt_1(Eeline $$1,Eebodynxt $$3) {this.$$1=$$1;this.$$3=$$3;}
	@Override
	public Value eval() throws EvalException {
		String $1=$$1.eval().asString();
		String $3=$$3.eval().asString();
		VString res = new VString(text);
		return res;
	}
	public String toString () {
		String $1=$$1.toString();
		String $3=$$3.toString();
		return $1+"|"+$3;
	};
}
class Eebodynxt_2 extends Eebodynxt {
	Eeline $$1;
	Eebodynxt_2(Eeline $$1) {this.$$1=$$1;}
	@Override
	public Value eval() throws EvalException {
		String $1=$$1.eval().asString();
		VString res = new VString(text);
		return res;
	}
	public String toString () {
		String $1=$$1.toString();
		return $1;
	};
}
class Eeline extends Expression { 
	@Override
	public Value eval() throws EvalException {
		throw new EvalException ("This method should have been overidden");
	}
}
class Eeline_1 extends Eeline {
	Eescheme $$1;
	String $$2;
	Eeline_1(Eescheme $$1,String $$2) {this.$$1=$$1;this.$$2=$$2;}
	@Override
	public Value eval() throws EvalException {
		String $1=$$1.eval().asString();
		String $2=$$2;
		VString res = new VString(text);
		return res;
	}
	public String toString () {
		String $1=$$1.toString();
		String $2=$$2+"";
		return $1+$2;
	};
}
class Eescheme extends Expression { 
	@Override
	public Value eval() throws EvalException {
		throw new EvalException ("This method should have been overidden");
	}
}
class Eescheme_1 extends Eescheme {
	Eeterminal $$1;
	Eeschemenxt $$2;
	Eescheme_1(Eeterminal $$1,Eeschemenxt $$2) {this.$$1=$$1;this.$$2=$$2;}
	@Override
	public Value eval() throws EvalException {
		String $1=$$1.eval().asString();
		String $2=$$2.eval().asString();
		VString res = new VString(text);
		return res;
	}
	public String toString () {
		String $1=$$1.toString();
		String $2=$$2.toString();
		return $1+$2;
	};
}
class Eeschemenxt extends Expression { 
	@Override
	public Value eval() throws EvalException {
		throw new EvalException ("This method should have been overidden");
	}
}
class Eeschemenxt_1 extends Eeschemenxt {
	Eeterminal $$1;
	Eeschemenxt $$2;
	Eeschemenxt_1(Eeterminal $$1,Eeschemenxt $$2) {this.$$1=$$1;this.$$2=$$2;}
	@Override
	public Value eval() throws EvalException {
		String $1=$$1.eval().asString();
		String $2=$$2.eval().asString();
		VString res = new VString(text);
		return res;
	}
	public String toString () {
		String $1=$$1.toString();
		String $2=$$2.toString();
		return $1+$2+"->";
	};
}
class Eeterminal extends Expression { 
	@Override
	public Value eval() throws EvalException {
		throw new EvalException ("This method should have been overidden");
	}
}
class Eeterminal_1 extends Eeterminal {
	String $$1;
	Eeterminal_1(String $$1) {this.$$1=$$1;}
	@Override
	public Value eval() throws EvalException {
		String $1=$$1;
		VString res = new VString(text);
		return res;
	}
	public String toString () {
		String $1=$$1+"";
		return $1;
	};
}
class EConstant extends Expression {
	final Value v;
	EConstant(Value v){this.v=v;}
	@Override
	public Value eval() throws EvalException {return v;}
}
class VBoolean extends Value {
	final boolean v;
	public String toString () {return (new Boolean (v)).toString();}
	VBoolean (boolean v) {this.v = v;}
	@Override  
	public boolean asBoolean () {return v;}
}
class VString extends Value {
	final String v;
	public String toString () {return v;}
	VString (String v) {this.v = v;}
	@Override  
	public String asString () {return v;}
}
class VInteger extends Value {
	final int v;
	public String toString () {return (new Integer (v)).toString();}
	VInteger (int v) {this.v = v;}
	@Override  
	public int asInteger () {return v;}
}
//--------------------------------------------
//---Fichier PréCompilé Parser.java
class Parser {
	private final Lexer lexer;
	public Parser (Lexer lexer) {this.lexer=lexer;}
	public Expression parse() throws IOException{
		return consumeegram (0);
	}
	private Eegram consumeegram (int lvl) throws IOException {
		Eegram res;
		lexer.mark();
		try {res = consumeegram_1 (lvl); lexer.dropMark(); return res;} catch (SyntaxError e1) {
			lexer.reset();
			lexer.mark();
			throw new SyntaxError ("Error parsing "+this);
		}
	}
	private Eegram_1 consumeegram_1 (int lvl) throws IOException {
		Eees $1 = consumeees (lvl+1);
		lexer.assumeToken(";;");
		Eegramnxt $3 = consumeegramnxt (lvl+1);
		if ((lvl==0)&&(!lexer.eof())) {throw new SyntaxError("EOF should have been reached at this point");}
		return new Eegram_1 ($1,$3);
	}
	private Eegramnxt consumeegramnxt (int lvl) throws IOException {
		Eegramnxt res;
		lexer.mark();
		try {res = consumeegramnxt_1 (lvl); lexer.dropMark(); return res;} catch (SyntaxError e1) {
			lexer.reset();
			lexer.mark();
			try {res = consumeegramnxt_2 (lvl); lexer.dropMark(); return res;} catch (SyntaxError e2) {
				lexer.reset();
				lexer.mark();
				throw new SyntaxError ("Error parsing "+this);
			}
		}
	}
	private Eegramnxt_1 consumeegramnxt_1 (int lvl) throws IOException {
		Eees $1 = consumeees (lvl+1);
		lexer.assumeToken(";;");
		Eegramnxt $3 = consumeegramnxt (lvl+1);
		if ((lvl==0)&&(!lexer.eof())) {throw new SyntaxError("EOF should have been reached at this point");}
		return new Eegramnxt_1 ($1,$3);
	}
	private Eegramnxt_2 consumeegramnxt_2 (int lvl) throws IOException {
		Eees $1 = consumeees (lvl+1);
		lexer.assumeToken(";;");
		if ((lvl==0)&&(!lexer.eof())) {throw new SyntaxError("EOF should have been reached at this point");}
		return new Eegramnxt_2 ($1);
	}
	private Eees consumeees (int lvl) throws IOException {
		Eees res;
		lexer.mark();
		try {res = consumeees_1 (lvl); lexer.dropMark(); return res;} catch (SyntaxError e1) {
			lexer.reset();
			lexer.mark();
			throw new SyntaxError ("Error parsing "+this);
		}
	}
	private Eees_1 consumeees_1 (int lvl) throws IOException {
		String $1 = lexer.getString ();
		lexer.assumeToken("::=");
		Eebody $3 = consumeebody (lvl+1);
		if ((lvl==0)&&(!lexer.eof())) {throw new SyntaxError("EOF should have been reached at this point");}
		return new Eees_1 ($1,$3);
	}
	private Eebody consumeebody (int lvl) throws IOException {
		Eebody res;
		lexer.mark();
		try {res = consumeebody_1 (lvl); lexer.dropMark(); return res;} catch (SyntaxError e1) {
			lexer.reset();
			lexer.mark();
			throw new SyntaxError ("Error parsing "+this);
		}
	}
	private Eebody_1 consumeebody_1 (int lvl) throws IOException {
		Eeline $1 = consumeeline (lvl+1);
		lexer.assumeToken("|");
		Eebodynxt $3 = consumeebodynxt (lvl+1);
		if ((lvl==0)&&(!lexer.eof())) {throw new SyntaxError("EOF should have been reached at this point");}
		return new Eebody_1 ($1,$3);
	}
	private Eebodynxt consumeebodynxt (int lvl) throws IOException {
		Eebodynxt res;
		lexer.mark();
		try {res = consumeebodynxt_1 (lvl); lexer.dropMark(); return res;} catch (SyntaxError e1) {
			lexer.reset();
			lexer.mark();
			try {res = consumeebodynxt_2 (lvl); lexer.dropMark(); return res;} catch (SyntaxError e2) {
				lexer.reset();
				lexer.mark();
				throw new SyntaxError ("Error parsing "+this);
			}
		}
	}
	private Eebodynxt_1 consumeebodynxt_1 (int lvl) throws IOException {
		Eeline $1 = consumeeline (lvl+1);
		lexer.assumeToken("|");
		Eebodynxt $3 = consumeebodynxt (lvl+1);
		if ((lvl==0)&&(!lexer.eof())) {throw new SyntaxError("EOF should have been reached at this point");}
		return new Eebodynxt_1 ($1,$3);
	}
	private Eebodynxt_2 consumeebodynxt_2 (int lvl) throws IOException {
		Eeline $1 = consumeeline (lvl+1);
		if ((lvl==0)&&(!lexer.eof())) {throw new SyntaxError("EOF should have been reached at this point");}
		return new Eebodynxt_2 ($1);
	}
	private Eeline consumeeline (int lvl) throws IOException {
		Eeline res;
		lexer.mark();
		try {res = consumeeline_1 (lvl); lexer.dropMark(); return res;} catch (SyntaxError e1) {
			lexer.reset();
			lexer.mark();
			throw new SyntaxError ("Error parsing "+this);
		}
	}
	private Eeline_1 consumeeline_1 (int lvl) throws IOException {
		Eescheme $1 = consumeescheme (lvl+1);
		String $2 = lexer.getString ();
		if ((lvl==0)&&(!lexer.eof())) {throw new SyntaxError("EOF should have been reached at this point");}
		return new Eeline_1 ($1,$2);
	}
	private Eescheme consumeescheme (int lvl) throws IOException {
		Eescheme res;
		lexer.mark();
		try {res = consumeescheme_1 (lvl); lexer.dropMark(); return res;} catch (SyntaxError e1) {
			lexer.reset();
			lexer.mark();
			throw new SyntaxError ("Error parsing "+this);
		}
	}
	private Eescheme_1 consumeescheme_1 (int lvl) throws IOException {
		Eeterminal $1 = consumeeterminal (lvl+1);
		Eeschemenxt $2 = consumeeschemenxt (lvl+1);
		if ((lvl==0)&&(!lexer.eof())) {throw new SyntaxError("EOF should have been reached at this point");}
		return new Eescheme_1 ($1,$2);
	}
	private Eeschemenxt consumeeschemenxt (int lvl) throws IOException {
		Eeschemenxt res;
		lexer.mark();
		try {res = consumeeschemenxt_1 (lvl); lexer.dropMark(); return res;} catch (SyntaxError e1) {
			lexer.reset();
			lexer.mark();
			throw new SyntaxError ("Error parsing "+this);
		}
	}
	private Eeschemenxt_1 consumeeschemenxt_1 (int lvl) throws IOException {
		Eeterminal $1 = consumeeterminal (lvl+1);
		Eeschemenxt $2 = consumeeschemenxt (lvl+1);
		lexer.assumeToken("->");
		if ((lvl==0)&&(!lexer.eof())) {throw new SyntaxError("EOF should have been reached at this point");}
		return new Eeschemenxt_1 ($1,$2);
	}
	private Eeterminal consumeeterminal (int lvl) throws IOException {
		Eeterminal res;
		lexer.mark();
		try {res = consumeeterminal_1 (lvl); lexer.dropMark(); return res;} catch (SyntaxError e1) {
			lexer.reset();
			lexer.mark();
			throw new SyntaxError ("Error parsing "+this);
		}
	}
	private Eeterminal_1 consumeeterminal_1 (int lvl) throws IOException {
		String $1 = lexer.getString ();
		if ((lvl==0)&&(!lexer.eof())) {throw new SyntaxError("EOF should have been reached at this point");}
		return new Eeterminal_1 ($1);
	}
}
//--------------------------------------------
