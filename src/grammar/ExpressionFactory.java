package grammar;

import java.io.IOException;
import java.util.LinkedList;

import shared.Lexer;

public class ExpressionFactory {
	private EGram grammar;
	private Parser parser;
	public ExpressionFactory(Lexer lexer) throws IOException {
		parser = new Parser(lexer);
		grammar = parser.parse();
	}

	public String toString() {
		return "package out;\n"+
				"import shared.*;\n"+
				"import java.io.*;\n"+
				"\n"+
				"public class RunOutput {\n"+
				"public static void main (String[] argv) throws IOException, EvalException { \n"+
				"\n"+
				"BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));\n"+
				"System.out.println(\"---Grammar running under java code---\");\n"+
				"while (true) {\n"+
				"System.out.println(\"Type an input to be parsed and computed\");\n"+
				"System.out.println(\"Use <&q> to quit and <&f ./file.ext> to input from a file\");\n"+
				"String in = stdIn.readLine();\n"+
				"StringLexer stdInLexer = new StringLexer(in);\n"+
				"if (stdInLexer.lookWord().equals(\"&q\")) {System.out.println(\"--------------------Terminated---------------------\"); break;} else\n"+
				"if (stdInLexer.lookWord().equals(\"&f\")) {\n"+
				"stdInLexer.getWord();\n"+
				"Lexer fileLexer=new Lexer(stdInLexer.getWord());\n"+
				"ExpressionFactory expressionFactory = new ExpressionFactory(fileLexer);\n"+
				"System.out.println(expressionFactory);;\n"+
				"}\n"+
				"else {\n"+
				"Lexer writeLexer=new Lexer(\"./~temp.txt\");\n"+
				"System.out.println(in);\n"+
				"writeLexer.write(in);\n"+
				"Lexer fileLexer=new Lexer(\"./~temp.txt\");\n"+
				"ExpressionFactory expressionFactory = new ExpressionFactory(fileLexer);\n"+
				"System.out.println(expressionFactory);;\n"+
				"\n"+
				"}\n"+
				"System.out.println(\"----------------------------------------------\");\n"+
				"}\n"+
				"}\n"+
				"}\n"+
				"//--------------------------------------------\n"+
				"//---Fichier PréCompilé ExpressionFactory.java\n"+
				getClassCode()+
				"//--------------------------------------------\n"+
				"//---Fichier PréCompilé Parser.java\n"+
				getFuncCode()+
				"//--------------------------------------------\n";
	}

	private String getFuncCode() {
		return grammar.getFuncCode();
	}
	private String getClassCode() {
		return grammar.getClassCode();
	}

}
///////////////Expressions concerning grammar's grammar
enum Type {INT,STR,BOOL,LNK,OP}

class TypeAndName {
	public Type type;
	public String name;
	TypeAndName(Type type, String name) {this.type=type; this.name = name;}
}

class ETerminal extends GrammarExpression {
	String name;
	Type type;
	ETerminal(String name) {
		this.name=name;
		if (name.equals("int")) {type=Type.INT;} else
			if (name.equals("boolean")) {type=Type.BOOL;} else
				if (name.equals("String")) {type=Type.STR;} else
					if (name.charAt(0)=='\'') {type=Type.OP;this.name=(String) name.subSequence(1, name.length());} else
					{type=Type.LNK;}
	}
	String getFuncCode(int i) {
		if (type==Type.BOOL) {return "boolean $"+i+" = lexer.getBoolean ();\n";} else
			if (type==Type.STR) {return "String $"+i+" = lexer.getString ();\n";} else
				if (type==Type.INT) {return "int $"+i+" = lexer.getInt ();\n";} else
					if (type==Type.LNK) {return "E"+name+" $"+i+" = consume"+name+" (lvl+1);\n";} else
						if (type==Type.OP) {return "lexer.assumeToken(\""+name+"\");\n";} else
						{return "parsing error -- UNKNOW type : "+name;}
	}
	boolean getUsedVars(int i) {
		if ((type==Type.BOOL)||(type==Type.STR)||(type==Type.LNK)||(type==Type.INT)) {return true;} else
		{return false;}
	}

	TypeAndName getUsedVarTypesAndNames(int i) {
		{return new TypeAndName(type,name);}
	}
}

class ESchemeNxt extends GrammarExpression {
	LinkedList<ETerminal> list;
	ESchemeNxt() {list=new LinkedList<ETerminal>();}
	public void add(ETerminal s) {list.add(s);}
	public ETerminal getFirst() {return list.getFirst();}
	String getFuncCode() {
		String s="";
		int i=2;
		for (ETerminal eterm: list) {
			s = s+eterm.getFuncCode(i);
			i++;
		}
		return s; 
	}
	String getUsedVars(String prev) {
		int i=2;
		for (ETerminal eterm: list) {
			if (eterm.getUsedVars(i)) {
				if (!prev.equals("")) {prev=prev+",";} 
				prev=prev+"$"+i;
			}
			i++;
		}
		return prev;  
	}


	void getUsedVarTypesAndNames(LinkedList<TypeAndName> queue) {
		int i=2;
		for (ETerminal eterm: list) {
			queue.add(eterm.getUsedVarTypesAndNames(i));
			i++;
		}
	}
}

class EScheme extends GrammarExpression {
	ETerminal first;
	ESchemeNxt next;
	EScheme(ETerminal first,ESchemeNxt next) {this.first=first;this.next=next;}
	String getFuncCode() {
		return first.getFuncCode(1)+next.getFuncCode();
	}
	String getUsedVars() {
		String s = "";
		if (first.getUsedVars(1)) s="$1";
		return next.getUsedVars(s); 
	}

	LinkedList<TypeAndName> getUsedVarTypesAndNames() {
		LinkedList<TypeAndName> res = new LinkedList<TypeAndName>();
		res.add(first.getUsedVarTypesAndNames(1));
		next.getUsedVarTypesAndNames(res);
		return res;
	}
}

class ELine extends GrammarExpression {
	EScheme scheme;	
	String code;
	ELine(EScheme scheme,String code) {this.scheme=scheme;this.code=code;}
	String getFuncCode(String name, int i) {
		String s = "private E"+name+"_"+i+" consume"+name+"_"+i+" (int lvl) throws IOException {\n"+
				scheme.getFuncCode()+
				"if ((lvl==0)&&(!lexer.eof())) {throw new SyntaxError(\"EOF should have been reached at this point\");}\n"+
				"return new E"+name+"_"+i+" ("+scheme.getUsedVars()+");\n"+
				"}\n";
		return s;
	}

	String getClassCode(String name, int i,Type type) {
		LinkedList<TypeAndName> varList = scheme.getUsedVarTypesAndNames();
		String sub1="", sub2="",sub3="",sub4="",sub5="",sub6="", vSub="", asSub="", typeSub="";
		if (type==Type.INT) {vSub="VInteger";asSub="asInteger"; typeSub="int";} else
			if (type==Type.BOOL) {vSub="VBoolean";asSub="asBoolean"; typeSub="boolean";} else
				if (type==Type.STR) {vSub="VString";asSub="asString"; typeSub="String";}
		int j=1;
		for (TypeAndName typeAndName : varList) {
			if (typeAndName.type==Type.LNK) {
				sub1=sub1+"E"+typeAndName.name+" $$"+j+";\n";
				if (!sub2.equals("")) {sub2=sub2+",";}
				sub2=sub2+"E"+typeAndName.name+" $$"+j;
			}
			if ((typeAndName.type==Type.INT)||(typeAndName.type==Type.STR)||(typeAndName.type==Type.BOOL)) {
				sub1=sub1+typeAndName.name+" $$"+j+";\n";
				if (!sub2.equals("")) {sub2=sub2+",";}
				sub2=sub2+typeAndName.name+" $$"+j;
			}
			if (typeAndName.type!=Type.OP) {
				sub3=sub3+"this.$$"+j+"=$$"+j+";";
				if (sub6.equals("")) {sub6="$"+j;} else {sub6=sub6+"+$"+j;}
			
				if ((typeAndName.type==Type.INT)||(typeAndName.type==Type.BOOL)||(typeAndName.type==Type.STR)) {
					sub4=sub4+typeSub+" $"+j+"=$$"+j+";\n";
					sub5=sub5+"String $"+j+"=$$"+j+"+\"\";\n";
				} else {
					sub4=sub4+typeSub+" $"+j+"=$$"+j+".eval()."+asSub+"();\n";
					sub5=sub5+"String $"+j+"=$$"+j+".toString();\n";	
				}
			}
			if (typeAndName.type==Type.OP) {
				if (sub6.equals("")) {sub6="\""+typeAndName.name+"\"";} else
					{sub6=sub6+"+\""+typeAndName.name+"\"";} 
			}
			j++;
		}		
		return  "class E"+name+"_"+i+" extends E"+name+" {\n"+
		sub1+				
		"E"+name+"_"+i+"("+sub2+") {"+sub3+"}\n"+
		"@Override\n"+
		"public Value eval() throws EvalException {\n"+
		sub4+
		vSub+" res = new "+vSub+"("+code+");\n"+
		"return res;\n"+
		"}\n"+
		"public String toString () {\n" +
		sub5+
		"return "+sub6+";\n" +
		"};\n"+
		"}\n";
	}
}

class EBodyNxt extends GrammarExpression {
	LinkedList<ELine> lines;
	EBodyNxt() {lines=new LinkedList<ELine>();}
	public void add(ELine line) {lines.add(line);}
	public ELine getFirst() {return lines.getFirst();}
	String getFuncCode(String name) {
		int i=2;
		String s="";
		for (ELine line: lines) {s=s+line.getFuncCode(name,i); i++;}
		return s; 
	}
	String getClassCode(String name,Type type) {
		String s="";
		int i=2;
		for (ELine line: lines) {s=s+line.getClassCode(name, i,type); i++;}
		return s; 
	}
}

class EBody extends GrammarExpression {
	ELine startLine;
	EBodyNxt next;
	EBody(ELine startLine,EBodyNxt next) {this.startLine=startLine;this.next=next;}
	String getFuncCode(String name) {
		return startLine.getFuncCode(name,1)+next.getFuncCode(name);
	}
	String getClassCode(String name,Type type) {
		return startLine.getClassCode(name,1,type)+next.getClassCode(name,type);
	}
}

class EEes extends GrammarExpression {
	String name;
	EBody body;
	Type type;
	EEes(String name,EBody body,Type type) {this.name=name;this.body=body;this.type=type;}
	String getFuncCode() {
		int n=1+body.next.lines.size();
		String s="private E"+name+" consume"+name+" (int lvl) throws IOException {\n"+
				"E"+name+" res;\n"+
				"lexer.mark();\n";
		for (int i=1;i<n;i++) {
			s=s+"try {res = consume"+name+"_"+i+" (lvl); lexer.dropMark(); return res;} catch (SyntaxError e"+i+") {\n"+
					"lexer.reset();\n"+
					"lexer.mark();\n";
		};
		s=s+"try {res = consume"+name+"_"+n+" (lvl); lexer.dropMark(); return res;} catch (SyntaxError e"+n+") {\n"+
				"lexer.reset();\n"+
				"lexer.mark();\n"+
				"throw new SyntaxError (\"Error parsing \"+this);\n"+
				"}\n";
		for (int i=0;i<n;i++) {s=s+"}\n";}
		return s+body.getFuncCode(name);
	}
	String getClassCode() {
		return "class E"+name+" extends Expression { \n"+
				"@Override\n"+
				"public Value eval() throws EvalException {\n"+
				"throw new EvalException (\"This method should have been overidden\");\n"+
				"}\n"+
				"}\n"+
				body.getClassCode(name,type);
	}
}
////////////////ok
class EGramNxt extends GrammarExpression {
	LinkedList<EEes> ees;
	EGramNxt() {ees=new LinkedList<EEes>();}
	public void add(EEes ee) {ees.add(ee);}
	String getFuncCode() {
		String s="";
		for (EEes ee: ees) {s=s+ee.getFuncCode();}
		return s; 
	}
	String getClassCode() {
		String s="";
		for (EEes ee: ees) {s=s+ee.getClassCode();}
		return s; 
	}
}

class EGram extends GrammarExpression {
	EEes ee;
	EGramNxt next;
	EGram(EEes ee,EGramNxt next) {this.ee=ee;this.next=next;}
	//Returns the code to be placed in ExpressionFactory
	String getFuncCode() {
		return 	"class Parser {\n"+
				"private final Lexer lexer;\n"+
				"public Parser (Lexer lexer) {this.lexer=lexer;}\n"+
				"public Expression parse() throws IOException{\n"+
				"return consume"+ee.name+" (0);\n"+
				"}\n"+
				ee.getFuncCode()+
				next.getFuncCode()+
				"}\n";
	}
	//Returns the parser code
	String getClassCode() {
		return 	"class ExpressionFactory {\n"+
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
				ee.getClassCode()+
				next.getClassCode()+
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
				"}\n";
	}
}

