package ru.zarudny.app.exception;

public class StorageException extends RuntimeException {

  public StorageException(String message, Throwable cause) {
    super(message, cause);
  }
}
