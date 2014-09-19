package calculator;

import java.io.IOException;

import shared.*;

public class ExpressionFactory {
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


//***************************** CODE FLOTTANT

class Efun_1 extends Efun {
	int $1;
	Efun_1(int $1) {this.$1=$1;}
	@Override
	public Value eval() throws EvalException {
		return new VInteger($1);
	}
	public String toString () {return ""+$1;};
}

class Efun_2 extends Efun {
	Eexpr $2;
	Efun_2(Eexpr $2) {this.$2=$2;}
	@Override
	public Value eval() throws EvalException {
		return new VInteger($2.eval().asInteger());
	}
	public String toString () {return "("+$2.toString()+")";};
}

class Efun extends Expression { 
	@Override
	public Value eval() throws EvalException {
		throw new EvalException ("This method should have been overidden");
	}
}

///////////////

class Eprod_1 extends Eprod {
	Efun $1;
	Eprod $3;
	Eprod_1(Efun $1, Eprod $3) {this.$1=$1;this.$3=$3;}
	@Override
	public Value eval() throws EvalException {
		return new VInteger($1.eval().asInteger()*$3.eval().asInteger());
	}
	public String toString () {return $1.toString()+"*"+$3.toString();};
}

class Eprod_2 extends Eprod {
	Efun $$1;
	Eprod $$3;
	Eprod_2(Efun $$1, Eprod $$3) {this.$$1=$$1;this.$$3=$$3;}
	@Override
	public Value eval() throws EvalException {
		int $1=$$1.eval().asInteger();
		int $3=$$3.eval().asInteger();
		return new VInteger($1/$3);
	}
	public String toString () {
		String $1=$$1.toString();
		String $3=$$3.toString();
		return $1+"/"+$3;
		};
}

class Eprod_3 extends Eprod {
	Efun $1;
	Eprod_3(Efun $1) {this.$1=$1;}
	@Override
	public Value eval() throws EvalException {
		return new VInteger($1.eval().asInteger());
	}
	public String toString () {return $1.toString();};
}

class Eprod extends Expression { 
	@Override
	public Value eval() throws EvalException {
		throw new EvalException ("This method should have been overidden");
	}
}

/////////////

class Eexpr_1 extends Eexpr {
	Eprod $1;
	Eexpr $3;
	Eexpr_1(Eprod $1, Eexpr $3) {this.$1=$1;this.$3=$3;}
	@Override
	public Value eval() throws EvalException {
		return new VInteger($1.eval().asInteger()+$3.eval().asInteger());
	}
	public String toString () {return $1.toString()+"+"+$3.toString();};
}

class Eexpr_2 extends Eexpr {
	Eprod $1;
	Eexpr $3;
	Eexpr_2(Eprod $1, Eexpr $3) {this.$1=$1;this.$3=$3;}
	@Override
	public Value eval() throws EvalException {
		return new VInteger($1.eval().asInteger()-$3.eval().asInteger());
	}
	public String toString () {return $1.toString()+"-"+$3.toString();};
}

class Eexpr_3 extends Eexpr {
	Eprod $1;
	Eexpr_3(Eprod $1) {this.$1=$1;}
	@Override
	public Value eval() throws EvalException {
		return new VInteger($1.eval().asInteger());
	}
	public String toString () {return $1.toString();};
}

class Eexpr extends Expression { 
	@Override
	public Value eval() throws EvalException {
		throw new EvalException ("This method should have been overidden");
	}
}
	
//***************************** CODE FLOTTANT


/////////////////Expressions concerning final parser

class EConstant extends Expression {
	final Value v;
	EConstant(Value v){this.v=v;}
	@Override
	public Value eval() throws EvalException {return v;}
}

/////////////////Définition des classes de valeurs terminales -- booleens, chaines et entiers////////////////////
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
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

  