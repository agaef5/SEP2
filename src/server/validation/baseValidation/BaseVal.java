package server.validation.baseValidation;

public class BaseVal
{
  public static boolean validate(String string){
    return string == null || string.isEmpty();
  }

  public static boolean valPosInt(int integer){
//    validate positive int
    return integer > 0;
  }
}
