package com.homework.store.controller.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.homework.store.model.HttpTrace;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.testcontainers.shaded.org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Service
public class HttpTraceFilter implements Filter {

    private static final Logger log = LoggerFactory.getLogger(HttpTraceFilter.class);
    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper objectMapper;

    public HttpTraceFilter(RabbitTemplate rabbitTemplate, ObjectMapper objectMapper) {
        this.rabbitTemplate = rabbitTemplate;
        this.objectMapper = objectMapper;
    }


    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String method = httpRequest.getMethod();
        String uri = httpRequest.getRequestURI();
        String queryString = httpRequest.getQueryString();
        String body = IOUtils.toString(httpRequest.getInputStream(), StandardCharsets.UTF_8);
        String trace = String.format("%s %s?%s %s", method, uri, queryString, body);

        HttpTrace httpTrace = new HttpTrace(method, uri, queryString, body, trace);

        rabbitTemplate.convertAndSend("http-trace", objectMapper.writeValueAsString(httpTrace));

        chain.doFilter(request, response);
    }
}
