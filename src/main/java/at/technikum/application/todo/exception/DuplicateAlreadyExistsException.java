package at.technikum.application.todo.exception;

import at.technikum.server.http.Response;

// @HttpStatus(status = Status.CONFILCT)
public class DuplicateAlreadyExistsException extends RuntimeException {
    public DuplicateAlreadyExistsException() {
    }

  public DuplicateAlreadyExistsException(String message) {
    super(message);
  }

  public DuplicateAlreadyExistsException(String message, Throwable cause) {
    super(message, cause);
  }

  public DuplicateAlreadyExistsException(Throwable cause) {
    super(cause);
  }

  public DuplicateAlreadyExistsException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
