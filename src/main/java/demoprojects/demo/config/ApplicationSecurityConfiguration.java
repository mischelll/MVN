package demoprojects.demo.config;

import demoprojects.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class ApplicationSecurityConfiguration extends WebSecurityConfigurerAdapter {
//    private final UserService userService;
//
//    public ApplicationSecurityConfiguration(UserService userService) {
//        this.userService = userService;
//    }
//
@Bean("authenticationManager")
@Override
public AuthenticationManager authenticationManagerBean() throws Exception {
    return super.authenticationManagerBean();
}
//
//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.userDetailsService(userService);
//    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .cors().disable()
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("video/mp4","/vendor/**","/js/**", "/img/**","/css/**").permitAll()
                .antMatchers("/","/mvn/users/register","/about","/about/goal",
                        "/mvn/users/registration/{code}/{username}","/mvn/users/auth/email-verification").permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/mvn/users/login")
                .permitAll()
                .usernameParameter("username")
                .passwordParameter("password")
                .defaultSuccessUrl("/home",true)
                .and()
                .rememberMe()
                .key("rem-me-key")
                .rememberMeParameter("remember") // it is name of checkbox at login page
                .rememberMeCookieName("rememberlogin") // it is name of the cookie
                .tokenValiditySeconds(100) // remember for number of seconds
                .and()
                .logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/mvn/users/login")
                .permitAll()
                .and()
                .exceptionHandling().accessDeniedPage("/unauthorized");
    }
}
