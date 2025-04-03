package Server.services.authentication;

import Shared.LoginRequest;
import Shared.LoginRespond;
import Shared.RegisterRequest;
import Shared.RegisterRespond;


public interface AuthentificationService
{
   RegisterRespond  registerUser (RegisterRequest request);
   LoginRespond loginUser(LoginRequest request);
}
