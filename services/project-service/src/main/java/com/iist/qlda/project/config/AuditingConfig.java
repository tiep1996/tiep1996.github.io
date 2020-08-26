package com.iist.qlda.project.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
public class AuditingConfig {
    @Bean
    public AuditorAware auditorProvider() {

        return new AuditorAwareImpl();
    }

    public static class AuditorAwareImpl implements AuditorAware<String> {

        @Override
        public Optional<String> getCurrentAuditor() {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (null == authentication || !authentication.isAuthenticated()) {
                return null;
            }
            Object obj =  authentication.getPrincipal();
            String username = null;
            if (obj instanceof UserDetails) {
                username = ((UserDetails) obj).getUsername();
            } else {
                username = obj.toString();
            }
            return Optional.ofNullable(username);
        }
    }
}
