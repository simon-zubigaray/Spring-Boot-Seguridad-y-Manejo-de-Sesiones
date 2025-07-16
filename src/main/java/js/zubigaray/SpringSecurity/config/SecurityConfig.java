package js.zubigaray.SpringSecurity.config;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.session.ConcurrentSessionFilter;
import org.springframework.security.web.session.HttpSessionEventPublisher;

import java.io.IOException;

/**
 * Clase de configuración de seguridad de Spring Security.
 * Define la configuración de autenticación, manejo de sesiones y login.
 */
@Configuration // Indica que esta clase contiene beans de configuración de Spring.
@EnableWebSecurity // Habilita la configuración de seguridad web de Spring Security.
public class SecurityConfig {

    /**
     * Configura la cadena de filtros de seguridad de Spring Security.
     *
     * @param http el objeto HttpSecurity para configurar.
     * @return el SecurityFilterChain construido.
     * @throws Exception si ocurre un error en la configuración.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Configura las reglas de autorización
                .authorizeHttpRequests(auth -> auth
                        // Permite acceso público a la URL "/v1/index2"
                        .requestMatchers("/v1/index2").permitAll()
                        // Requiere autenticación para cualquier otra URL
                        .anyRequest().authenticated()
                )
                // Configura el formulario de login
                .formLogin(form -> form
                        // Define el comportamiento después de login exitoso
                        .successHandler(successHandler())
                        // Permite el acceso al login sin estar autenticado
                        .permitAll()
                )
                // Configura el logout
                .logout(logout -> logout
                        .logoutUrl("/logout") // Endpoint para cerrar sesión
                        .logoutSuccessUrl("/login?logout=true") // Redirección al cerrar sesión
                        .invalidateHttpSession(true) // Invalida la sesión al hacer logout
                        .deleteCookies("JSESSIONID") // Borra la cookie de sesión
                )
                // Configura el manejo de sesiones
                .sessionManagement(session -> session
                        // Siempre se crea una sesión para el usuario autenticado
                        .sessionCreationPolicy(SessionCreationPolicy.ALWAYS)
                        // Si la sesión no es válida, redirige al login
                        .invalidSessionUrl("/login")
                        // Evita ataques de fijación de sesión
                        .sessionFixation(fixation -> fixation.migrateSession())
                        // Configura el número máximo de sesiones por usuario
                        .maximumSessions(1)
                            .maxSessionsPreventsLogin(false) // Permite que una nueva sesión invalide la anterior
                            .expiredUrl("/login?expired=true") // Redirige si la sesión expira
                            .sessionRegistry(sessionRegistry()) // Registra sesiones activas
                );

        // Construye y retorna el objeto SecurityFilterChain
        return http.build();
    }

    /**
     * Bean que controla sesiones concurrentes de usuarios.
     * Se encarga de detectar sesiones expiradas y redirigir al login.
     *
     * @param sessionRegistry el registro de sesiones activo.
     * @return un filtro de sesión concurrente.
     */
    @Bean
    public ConcurrentSessionFilter concurrentSessionFilter(SessionRegistry sessionRegistry) {
        return new ConcurrentSessionFilter(sessionRegistry, event -> {
            try {
                // Obtiene la respuesta original para redirigir al login
                HttpServletResponse response = event.getResponse();
                response.sendRedirect("/login?expired"); // Redirige con un parámetro indicando expiración
            } catch (IOException e) {
                // Manejo de error en caso de fallo al redirigir
                throw new RuntimeException("Error al redirigir tras la expiración de sesión", e);
            }
        });
    }

    /**
     * Bean que publica eventos de sesión HTTP.
     * Es necesario para que Spring Security controle sesiones activas y expiradas.
     *
     * @return el publicador de eventos de sesión.
     */
    @Bean
    public HttpSessionEventPublisher httpSessionEventPublisher() {
        // Permite que los eventos de sesión (creación/destrucción) se propaguen correctamente
        return new HttpSessionEventPublisher();
    }

    /**
     * Bean que gestiona el registro de sesiones activas de los usuarios.
     *
     * @return una instancia de SessionRegistryImpl.
     */
    @Bean
    public SessionRegistry sessionRegistry() {
        // Este bean almacena y gestiona las sesiones activas en memoria
        return new SessionRegistryImpl();
    }

    /**
     * Define el comportamiento después de un login exitoso.
     * Redirige al usuario a la URL "/v1/session".
     *
     * @return el handler de éxito de autenticación.
     */
    public AuthenticationSuccessHandler successHandler() {
        // Al autenticarse correctamente, redirige a /v1/session
        return (request, response, authentication) -> {
            response.sendRedirect("v1/session");
        };
    }
}