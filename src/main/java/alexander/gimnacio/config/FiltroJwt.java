package alexander.gimnacio.config;


import alexander.gimnacio.repository.UsuarioRepository;
import alexander.gimnacio.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class FiltroJwt extends OncePerRequestFilter {

    private final JwtService servicioJwt;
    private final UsuarioRepository repoUsuario;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        String auth = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (auth != null && auth.startsWith("Bearer ")) {
            String token = auth.substring(7);
            if (servicioJwt.validarToken(token)) {
                String correo = servicioJwt.obtenerCorreoDesdeToken(token);
                var usuario = repoUsuario.findByCorreoElectronico(correo).orElse(null);
                if (usuario != null) {
                    var authToken = new UsernamePasswordAuthenticationToken(
                            correo,
                            null,
                            List.of(new SimpleGrantedAuthority(usuario.getRol().name()))
                    );
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
        }
        chain.doFilter(request, response);
    }
}
