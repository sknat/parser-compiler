package shared;


public abstract class Value {

	public String asString () throws EvalException
	{
		throw new EvalException (
				"Expected a String value, but got " + this + " instead."
				);
	}

	public boolean asBoolean () throws EvalException
	{
		throw new EvalException (
				"Expected a Boolean value, but got " + this + " instead."
				);
	}

	public int asInteger () throws EvalException
	{
		throw new EvalException (
				"Expected an integer value, but got " + this + " instead."
				);
	}

}

