package shared;

public abstract class Expression {
  public abstract Value eval () throws EvalException;

}
