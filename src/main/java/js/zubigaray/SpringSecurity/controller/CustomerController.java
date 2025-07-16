package js.zubigaray.SpringSecurity.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("v1")
public class CustomerController {

    private final SessionRegistry sessionRegistry;

    public CustomerController(SessionRegistry sessionRegistry){
        this.sessionRegistry = sessionRegistry;
    }

    @GetMapping("/index")
    public String index() {
        return "Hello world";
    }

    @GetMapping("/index2")
    public String index2() {
        return "Hello world not secured";
    }

    /**
     * Endpoint para obtener información de la sesión actual y del usuario autenticado.
     * Retorna el ID de la sesión y los datos del usuario si está autenticado.
     */
    @GetMapping("/session")
    public ResponseEntity<?> getDetailsSession(HttpServletRequest request) {

        // Obtiene el objeto de autenticación actual desde el contexto de seguridad de Spring
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Extrae el "principal" que representa al usuario autenticado
        Object principal = authentication.getPrincipal();

        // Verifica si el usuario autenticado es una instancia válida de nuestra clase User
        // Si no lo es, significa que no hay sesión activa o no está autenticado correctamente
        if (!(principal instanceof User)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuario no autenticado");
        }

        // Convierte el principal al tipo User para acceder a sus atributos
        User user = (User) principal;

        // Obtiene el ID de la sesión HTTP actual desde el objeto request
        String sessionId = request.getSession().getId();

        // Prepara un mapa para almacenar los datos que se enviarán como respuesta
        Map<String, Object> response = new HashMap<>();

        // Mensaje de prueba o confirmación
        response.put("response", "Hello world");

        // Incluye el ID de la sesión actual
        response.put("sessionId", sessionId);

        // Incluye los datos del usuario autenticado (nombre, email, etc.)
        response.put("sessionUser", user);

        // Devuelve una respuesta HTTP 200 con los datos del mapa en formato JSON
        return ResponseEntity.ok(response);
    }
}