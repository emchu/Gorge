package com.security.redis;

import jdk.nashorn.internal.runtime.regexp.joni.Config;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer;

public class SecurityInitializer extends AbstractSecurityWebApplicationInitializer {

    public SecurityInitializer() {
        super(SecurityConfig.class, Config.class);
    }

}