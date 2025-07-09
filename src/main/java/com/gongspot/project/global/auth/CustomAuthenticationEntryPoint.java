package com.gongspot.project.global.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gongspot.project.common.code.status.ErrorStatus;
import com.gongspot.project.common.code.ErrorReasonDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {

        ErrorReasonDTO errorResponse = ErrorStatus._UNAUTHORIZED.getReasonHttpStatus();

        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(errorResponse.getHttpStatus().value());

        ObjectMapper mapper = new ObjectMapper();
        response.getWriter().write(mapper.writeValueAsString(errorResponse));
    }
}
