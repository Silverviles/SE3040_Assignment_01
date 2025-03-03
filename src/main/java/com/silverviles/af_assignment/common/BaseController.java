package com.silverviles.af_assignment.common;

import com.silverviles.af_assignment.security.JWTService;
import com.silverviles.af_assignment.service.MasterService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
public class BaseController {
    @Autowired
    protected JWTService jwtService;

    @Autowired
    protected MasterService masterService;

    protected String extractUsernameFromRequest(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            return jwtService.extractUsername(token);
        } else {
            throw new IllegalArgumentException("Invalid Authorization header");
        }
    }
}
