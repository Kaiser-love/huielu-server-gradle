//package com.service.config;
//
//import com.service.controller.LoginSuccessHandle;
//import UserService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//
//@Configuration
//public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
//    final
//    UserService userService;
//
//    @Autowired
//    public SecurityConfiguration(UserService userService) {
//        this.userService = userService;
//    }
//
//
//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.userDetailsService(userService).passwordEncoder(new BCryptPasswordEncoder());
//        auth.inMemoryAuthentication().withUser("lizhengda").password(new BCryptPasswordEncoder().encode("lzd")).roles("admin");
//    }
//
//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        http
//                .authorizeRequests()
//                .antMatchers("/**").permitAll()
//                .antMatchers("/index").permitAll()
//                .antMatchers("/static/**").permitAll()
//                .antMatchers("/login").permitAll()
//                .antMatchers("/loginwithauthcode").permitAll()
//                .antMatchers("/requesthead").permitAll()
//                .antMatchers("/admin/**").access("hasRole('admin')")
//                .anyRequest().authenticated()
//                .and().formLogin().loginPage("/getlogin").permitAll().loginProcessingUrl("/login").defaultSuccessUrl("/index").successHandler(new LoginSuccessHandle())
//                .and().logout().logoutUrl("/logout").logoutSuccessUrl("/index")
//                .and().rememberMe().tokenValiditySeconds(604800).rememberMeParameter("remember-me")
//                .rememberMeCookieName("remember-me").key("remember-me").userDetailsService(userService);
//        http.csrf().disable();
//    }
//}