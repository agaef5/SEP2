package server.services.authentication;

import shared.LoginRequest;
import shared.LoginRespond;
import shared.RegisterRequest;
import shared.RegisterRespond;


public interface AuthentificationService
{
   RegisterRespond  registerUser (RegisterRequest request);
   LoginRespond loginUser(LoginRequest request);
}
