import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordEncoder {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        
        String[] passwords = {"admin123", "empleado123", "password123"};
        
        for (String password : passwords) {
            String encoded = encoder.encode(password);
            System.out.println("Password: " + password);
            System.out.println("Encoded: " + encoded);
            System.out.println();
        }
    }
}
