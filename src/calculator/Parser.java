package calculator;

import java.io.IOException;

import shared.Expression;
import shared.Lexer;
import shared.SyntaxError;


public class Parser {
	private final Lexer lexer;
	public Parser (Lexer lexer) {this.lexer=lexer;}
	public boolean verbose = false;
	//***************************** CODE FLOTTANT
	
	private Efun_2 consumefun_2 (int lvl) throws IOException {
		if (verbose) {System.out.println("consumefun2 "+lvl);}
		lexer.assumeToken("(");
		Eexpr $2 = consumeexpr (lvl+1);
		lexer.assumeToken(")");
		if ((lvl==0)&&(!lexer.eof())) {throw new SyntaxError("EOF should have been reached at this point");}
		return new Efun_2 ($2);
	}
	
	private Efun_1 consumefun_1 (int lvl) throws IOException {
		if (verbose) {System.out.println("consumefun1 "+lvl);}
		int $1 = lexer.getInt ();
		if ((lvl==0)&&(!lexer.eof())) {throw new SyntaxError("EOF should have been reached at this point");}
		return new Efun_1 ($1);
	}
	
	private Efun consumefun (int lvl) throws IOException {
		Efun res;
		lexer.mark();
		try {res = consumefun_1 (lvl); lexer.dropMark(); return res;} catch (SyntaxError e1) {
			lexer.reset();
			lexer.mark();
			try {res = consumefun_2 (lvl); lexer.dropMark(); return res;} catch (SyntaxError e2) {
				lexer.reset();
				throw new SyntaxError ("Error parsing "+this);
			}
		}   
	}

	private Eprod consumeprod_3 (int lvl) throws IOException {
		if (verbose) {System.out.println("consumeprod3 "+lvl);}
		Efun $1 = consumefun (lvl+1);
		if ((lvl==0)&&(!lexer.eof())) {throw new SyntaxError("EOF should have been reached at this point");}
		return new Eprod_3($1);
	}

	private Eprod consumeprod_2 (int lvl) throws IOException {
		if (verbose) {System.out.println("consumeprod2 "+lvl);}
		Efun $1 = consumefun (lvl+1);
		lexer.assumeToken("/");
		Eprod $3 = consumeprod (lvl+1);
		if ((lvl==0)&&(!lexer.eof())) {throw new SyntaxError("EOF should have been reached at this point");}
		return new Eprod_2($1,$3);
	}

	private Eprod consumeprod_1 (int lvl) throws IOException {
		if (verbose) {System.out.println("consumeprod1 "+lvl);}
		Efun $1 = consumefun (lvl+1);
		lexer.assumeToken("*");
		Eprod $3 = consumeprod (lvl+1);
		if ((lvl==0)&&(!lexer.eof())) {throw new SyntaxError("EOF should have been reached at this point");}
		return new Eprod_1($1,$3);
	}

	private Eprod consumeprod (int lvl) throws IOException {
		Eprod res;
		lexer.mark();
		try {res = consumeprod_1 (lvl); lexer.dropMark(); return res;} catch (SyntaxError e1) {
			lexer.reset(); 
			lexer.mark();
			try {res = consumeprod_2 (lvl); lexer.dropMark(); return res;} catch (SyntaxError e2) {
				lexer.reset(); 
				lexer.mark();
				try {res = consumeprod_3 (lvl); lexer.dropMark(); return res;} catch (SyntaxError e3) {
					lexer.reset();
					throw new SyntaxError ("Error parsing "+this);
				}
			}
		}   
	}

	private Eexpr consumeexpr_3 (int lvl) throws IOException {
		if (verbose) {System.out.println("consumeexpr3 "+lvl);}
		Eprod $1 = consumeprod (lvl+1);
		if ((lvl==0)&&(!lexer.eof())) {throw new SyntaxError("EOF should have been reached at this point");}
		return new Eexpr_3($1);
	}

	private Eexpr consumeexpr_2 (int lvl) throws IOException {
		if (verbose) {System.out.println("consumeexpr2 "+lvl);}
		Eprod $1 = consumeprod (lvl+1);
		lexer.assumeToken("-");
		Eexpr $3 = consumeexpr (lvl+1);
		if ((lvl==0)&&(!lexer.eof())) {throw new SyntaxError("EOF should have been reached at this point");}
		return new Eexpr_2($1,$3);
	} 

	private Eexpr consumeexpr_1 (int lvl) throws IOException {
		if (verbose) {System.out.println("consumeexpr1 "+lvl);}
		Eprod $1 = consumeprod (lvl+1);
		lexer.assumeToken("+");
		Eexpr $3 = consumeexpr (lvl+1);
		if ((lvl==0)&&(!lexer.eof())) {throw new SyntaxError("EOF should have been reached at this point");}
		return new Eexpr_1($1,$3);
	}

	private Eexpr consumeexpr (int lvl) throws IOException {
		Eexpr res;
		lexer.mark();
		try {res = consumeexpr_1 (lvl); lexer.dropMark(); return res;} catch (SyntaxError e1) {
			lexer.reset();
			lexer.mark();
			try {res = consumeexpr_2 (lvl); lexer.dropMark(); return res;} catch (SyntaxError e2) {
				lexer.reset();
				lexer.mark();
				try {res = consumeexpr_3 (lvl); lexer.dropMark(); return res;} catch (SyntaxError e3) {
					lexer.reset();
					throw new SyntaxError ("Error parsing "+this);
				}
			}
		}   
	}

	public Expression parse() throws IOException{
		return consumeexpr (0);
	}
	//***************************** CODE FLOTTANT
}