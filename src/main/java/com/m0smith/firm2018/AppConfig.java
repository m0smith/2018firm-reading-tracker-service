package com.m0smith.firm2018;

import com.auth0.spring.security.api.JwtWebSecurityConfigurer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;


@Configuration
@EnableWebSecurity(debug = true)
public class AppConfig extends WebSecurityConfigurerAdapter {

    @Value(value = "${auth0.apiAudience}")
    private String apiAudience;
    @Value(value = "${auth0.issuer}")
    private String issuer;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        JwtWebSecurityConfigurer
	    .forRS256(apiAudience, issuer)
	    .configure(http)
	    .authorizeRequests()
	    .antMatchers(HttpMethod.GET, "/", "/*.html","/css/*.css","/js/*.js",
			 "/image/*","/favicon.ico").permitAll()
	    .antMatchers(HttpMethod.GET, "/reg/**").hasAuthority("read:user")
	    .antMatchers(HttpMethod.POST, "/reg/**").hasAuthority("write:user")
	    .antMatchers(HttpMethod.GET, "/read/**").hasAuthority("read:chapter")
	    .antMatchers(HttpMethod.PUT, "/read/**").hasAuthority("write:chapter")
	    .antMatchers(HttpMethod.DELETE, "/read/**").hasAuthority("write:chapter")
	    .anyRequest().authenticated();
    }
}
