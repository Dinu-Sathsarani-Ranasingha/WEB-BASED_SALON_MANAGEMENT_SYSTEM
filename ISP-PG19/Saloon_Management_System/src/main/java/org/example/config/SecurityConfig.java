package org.example.config;

import org.example.service.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration
public class SecurityConfig {

    private final CustomUserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;

    public SecurityConfig(CustomUserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        // 1. Static Resources & Category Images
                        .requestMatchers("/css/**", "/js/**", "/images/**", "/static/**", "/webjars/**", "/uploads/**").permitAll()
                        .requestMatchers("/", "/home", "/login", "/register", "/services", "/packages", "/styles").permitAll()

                        // 2. Salon & Package APIs (Role-Based)
                        // Everyone (even guests) can VIEW services and packages
                        .requestMatchers(HttpMethod.GET, "/api/salon/**", "/api/packages/**").permitAll()
                        // Only OWNER can modify the catalog (Add/Edit/Delete)
                        .requestMatchers("/api/salon/**").hasRole("OWNER")
                        .requestMatchers("/api/packages/**").hasAnyRole("OWNER", "RECEPTIONIST")

                        // Now allows STAFF to fetch their own data for the schedule table
                        .requestMatchers("/api/appointments/daily").hasAnyRole("OWNER", "RECEPTIONIST", "STAFF")
                        // 3. Booking & Cart APIs (Customer Access)
                        // Customers handle their own bookings and history
                        .requestMatchers("/api/cart/**", "/api/appointments/**", "/api/bookings/**").hasRole("CUSTOMER")

                        // Allows both staff and customers (who are booking) to see the employee list
                        .requestMatchers("/api/employees/staff-list").hasAnyRole("CUSTOMER", "RECEPTIONIST", "OWNER", "STAFF")

                        // 4. Dashboard & UI Access
                        .requestMatchers("/owner/**").hasRole("OWNER")
                        .requestMatchers("/reception/**").hasAnyRole("RECEPTIONIST", "OWNER")
                        .requestMatchers("/staff/**").hasAnyRole("STAFF", "RECEPTIONIST", "OWNER")
                        .requestMatchers("/customer/**").hasRole("CUSTOMER")

                        // Let everyone (guests included) view the main feedback and style pages
                         .requestMatchers(HttpMethod.GET, "/feedback", "/style").permitAll()
                        // Restrict all actions (creating, editing, deleting, saving) to CUSTOMERS only
                         .requestMatchers("/feedback/**", "/rating/**", "/style/**").hasRole("CUSTOMER")


                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .permitAll()
                        .successHandler(authenticationSuccessHandler())
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout=true")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .permitAll()
                );

        return http.build();
    }

    @Bean
    public AuthenticationSuccessHandler authenticationSuccessHandler() {
        return (request, response, authentication) -> {
            String role = authentication.getAuthorities().stream()
                    .map(grantedAuthority -> grantedAuthority.getAuthority())
                    .filter(auth -> auth.startsWith("ROLE_"))
                    .map(auth -> auth.substring(5))
                    .findFirst()
                    .orElse("CUSTOMER");

            String redirectUrl = switch (role) {
                case "OWNER" -> "/owner/dashboard";
                case "RECEPTIONIST" -> "/reception/dashboard";
                case "STAFF" -> "/staff/dashboard";
                default -> "/customer/dashboard";
            };

            response.sendRedirect(redirectUrl);
        };
    }
}