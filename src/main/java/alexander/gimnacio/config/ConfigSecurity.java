package alexander.gimnacio.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod; // 👈 importa esto
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class ConfigSecurity {

    private final FiltroJwt filtroJwt;             // tu filtro JWT
    private final PuntoEntradaJwt puntoEntradaJwt; // entrypoint 401

    @Bean
    public SecurityFilterChain cadenaDeFiltrosDeSeguridad(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.configurationSource(fuenteConfiguracionCors()))
                .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(e -> e.authenticationEntryPoint(puntoEntradaJwt))
                .authorizeHttpRequests(autz -> autz
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                        .requestMatchers("/api/autenticacion/**").permitAll()
                        .requestMatchers("/api/ubicaciones/**").permitAll()
                        .requestMatchers("/api/contacto/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/clases").permitAll()

                        .requestMatchers("/api/admin/**").hasAuthority("ADMINISTRADOR")

                        .requestMatchers("/api/clases/**").authenticated()

                        .anyRequest().authenticated()
                )
                .addFilterBefore(filtroJwt, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder codificadorContrasena() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CorsConfigurationSource fuenteConfiguracionCors() {
        CorsConfiguration cfg = new CorsConfiguration();
        cfg.setAllowedOrigins(List.of("*"));
        cfg.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        cfg.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type", "Accept"));
        cfg.setExposedHeaders(Arrays.asList("Authorization"));
        cfg.setAllowCredentials(false);

        UrlBasedCorsConfigurationSource fuente = new UrlBasedCorsConfigurationSource();
        fuente.registerCorsConfiguration("/**", cfg);
        return fuente;
    }
}
