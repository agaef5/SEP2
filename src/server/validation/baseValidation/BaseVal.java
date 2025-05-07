package server.validation.baseValidation;

/**
 * The {@code BaseVal} class provides utility methods for basic validation of input values,
 * such as checking if a string is empty or null and verifying if an integer is positive.
 */
public class BaseVal
{
  /**
   * Validates if the provided string is null or empty.
   *
   * @param string The string to be validated.
   * @return {@code true} if the string is null or empty; {@code false} otherwise.
   */
  public static boolean validate(String string){
    return string == null || string.isEmpty();
  }

  /**
   * Validates if the provided integer is a positive number.
   *
   * @param integer The integer to be validated.
   * @return {@code true} if the integer is greater than zero; {@code false} otherwise.
   */
  public static boolean valPosInt(int integer){
    // validate positive int
    return integer > 0;
  }
}
