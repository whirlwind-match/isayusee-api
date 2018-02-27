package com.isayusee.api.config;

import org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest;
import org.springframework.boot.autoconfigure.security.oauth2.resource.AuthoritiesExtractor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@EnableWebSecurity
@EnableConfigurationProperties
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class SecurityConfig {
    @EnableResourceServer
    @Configuration
    public static class ResourceServerConfig extends ResourceServerConfigurerAdapter {
        @Override
        public void configure(HttpSecurity http) throws Exception {
            // see https://spring.io/blog/2017/09/15/security-changes-in-spring-boot-2-0-m4
            http
                    .authorizeRequests()
                    .antMatchers(HttpMethod.OPTIONS, "/**") // CORS pre-flight
                    .permitAll()
                    .requestMatchers(EndpointRequest.to("status", "info"))
                    .permitAll()
                    .requestMatchers(EndpointRequest.toAnyEndpoint())
                    .hasRole("ACTUATOR")
                    .requestMatchers(PathRequest.toStaticResources().atCommonLocations())
                    .permitAll()
                    .antMatchers("/**")
                    .hasRole("USER");
        }
    }

    @Bean
    public AuthoritiesExtractor authoritiesExtractor() {
        return map -> {
            String nickname = (String) map.get("nickname");
            if ("nealeu".equals(nickname)) {
                return AuthorityUtils.createAuthorityList("ROLE_USER", "ROLE_ACTUATOR ");
            }
            else {
                return AuthorityUtils.createAuthorityList("ROLE_USER");
            }
        };
    }
    @Bean
    public FilterRegistrationBean corsFilter() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOrigin("*");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        UrlBasedCorsConfigurationSource configurationSource = new UrlBasedCorsConfigurationSource();
        configurationSource.registerCorsConfiguration("/api/**", config);
        FilterRegistrationBean bean = new FilterRegistrationBean<>(new CorsFilter(configurationSource));
        bean.setOrder(0);
        return bean;
    }
}
