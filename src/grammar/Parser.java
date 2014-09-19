package grammar;

import java.io.IOException;
import shared.Lexer;
import shared.SyntaxError;

public class Parser {

	private final Lexer lexer;
	// Constructor.
	public Parser (Lexer lexer) {this.lexer=lexer;}

	///////////////Concerning grammar's grammar

	private ESchemeNxt consumeSchemeNxt (ESchemeNxt head) throws IOException {
		String pick = lexer.lookWord();
		if (pick.equals("->")) {lexer.getWord(); return head;} 
		else {
			head.add(new ETerminal(lexer.getWord()));
			return consumeSchemeNxt(head);
		}
	}

	private EScheme consumeScheme () throws IOException {
		ETerminal et = new ETerminal(lexer.getWord());
		ESchemeNxt nxt = consumeSchemeNxt (new ESchemeNxt());
		return new EScheme(et,nxt);
	}
	
	private ELine consumeLine () throws IOException {
		EScheme sch = consumeScheme ();
		lexer.assumeToken("[[");
		String cod = lexer.getWordUntil("]]", false);
		lexer.assumeToken("]]");
		return new ELine(sch,cod);
	}

	private EBodyNxt consumeBodyNxt (EBodyNxt head) throws IOException {
		String pick = lexer.lookWord();
		if (pick.equals("|")) 	{
			lexer.getWord();
			head.add(consumeLine());
			return consumeBodyNxt(head);
		} 
		else {
			return head;
		}
	}

	private EBody consumeBody () throws IOException {
		ELine ln = consumeLine();
		EBodyNxt nxt = consumeBodyNxt (new EBodyNxt());
		return new EBody(ln,nxt);
	}

	private EEes consumeEes () throws IOException {
		String s = lexer.getWordUntil("::=",false);
		Type type;
		if (s.charAt(0)=='I') {type=Type.INT;} else
			if (s.charAt(0)=='B') {type=Type.BOOL;} else
				if (s.charAt(0)=='S') {type=Type.STR;} else
				{throw new SyntaxError(" -- Error: unknown type "+s+" -- ");}
		lexer.getWord();
		EBody bd = consumeBody();
		return new EEes(s.substring(1),bd,type);
	}

	private EGramNxt consumeGramNxt (EGramNxt head) throws IOException {
		if (!lexer.getWord().equals(";;")) {throw new SyntaxError("';;' expected");}
		if (lexer.eof()) {
			return head;
		} 
		else {
			head.add(consumeEes());
			return consumeGramNxt(head);
		}
	}
	private EGram consumeGram () throws IOException {
		EEes ee = consumeEes();
		EGramNxt gn = consumeGramNxt(new EGramNxt());
		return new EGram(ee,gn);
	}
	
	public EGram parse () throws IOException{
		return consumeGram();
	}
}