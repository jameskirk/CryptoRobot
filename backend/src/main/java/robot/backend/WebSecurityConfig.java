package robot.backend;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configurers.provisioning.InMemoryUserDetailsManagerConfigurer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import robot.backend.security.JWTAuthenticationFilter;
import robot.backend.security.JWTLoginFilter;

import java.util.Arrays;

@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable().authorizeRequests()
                // No need authentication.
                .antMatchers("/").permitAll() //
                .antMatchers(HttpMethod.POST, "/login").permitAll() //
                .antMatchers(HttpMethod.GET, "/login").permitAll() // For Test on Browser
                .antMatchers(HttpMethod.POST, "/get_ticker_names").permitAll()
                .antMatchers(HttpMethod.GET, "/get_ticker_names").permitAll()
                .antMatchers(HttpMethod.POST, "/get_ticker_info").permitAll()
                .antMatchers(HttpMethod.GET, "/get_ticker_info").permitAll()
                // Need authentication.
                //.anyRequest().authenticated()
                //
                .and()
                //
                // Add Filter 1 - JWTLoginFilter
                //
                .addFilterBefore(new JWTLoginFilter("/login", authenticationManager()),
                        UsernamePasswordAuthenticationFilter.class)
                //
                // Add Filter 2 - JWTAuthenticationFilter
                //
                .addFilterBefore(new JWTAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        return bCryptPasswordEncoder;
    }


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {

        //String password = "123";

        //String encrytedPassword = this.passwordEncoder().encode(password);
        //System.out.println("Encoded password of 123=" + encrytedPassword);

        InMemoryUserDetailsManagerConfigurer<AuthenticationManagerBuilder> //
                mngConfig = auth.inMemoryAuthentication();

        // Defines 2 users, stored in memory.
        // ** Spring BOOT >= 2.x (Spring Security 5.x)
        // Spring auto add ROLE_
        //UserDetails u1 = User.withUsername("tom").password(encrytedPassword).roles("USER").build();
        //UserDetails u2 = User.withUsername("jerry").password(encrytedPassword).roles("USER").build();

        //mngConfig.withUser("tom").password(encrytedPassword).roles("USER");
        //mngConfig.withUser(u2);

        // If Spring BOOT < 2.x (Spring Security 4.x)):
        // Spring auto add ROLE_
        mngConfig.withUser("tom").password("123").roles("USER");
        mngConfig.withUser("user").password("password").roles("USER");

    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("*"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("authorization", "content-type", "x-auth-token" , "token"));
        configuration.setExposedHeaders(Arrays.asList("x-auth-token", "token"));
        //        res.addHeader("Access-Control-Allow-Origin", "*");
//        res.addHeader("Access-Control-Expose-Headers", "*");
//        res.addHeader("Access-Control-Allow-Headers", "*");
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

}