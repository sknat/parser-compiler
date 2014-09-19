package shared;

import java.io.IOException;

public class SyntaxError extends IOException {
  private static final long serialVersionUID = 1L;

  public SyntaxError(String message) {
    super(message);
  }
}