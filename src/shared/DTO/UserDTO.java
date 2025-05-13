package shared.DTO;

public record UserDTO(String username, String email, String password, boolean isAdmin, int balance){
}
