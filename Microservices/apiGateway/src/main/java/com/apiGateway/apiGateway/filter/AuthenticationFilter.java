package com.apiGateway.apiGateway.filter;

import com.apiGateway.apiGateway.util.JWTUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {
    @Autowired
    private RouteValidator validator;
     @Autowired
    private RestTemplate template;

    @Autowired
    private JWTUtil jwtUtils;
    private static final Logger logger= LoggerFactory.getLogger(AuthenticationFilter.class);

    public AuthenticationFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return ((exchange, chain) -> {
            if (validator.isSecured.test(exchange.getRequest())) {
                //header contains token or not
                if (!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                    throw new RuntimeException("missing authorization header");
                }

                String authHeader = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
                if (authHeader != null && authHeader.startsWith("Bearer ")) {
                    authHeader = authHeader.substring(7);
                }
                try {
                    //REST call to AUTH service
                    //template.getForObject("http://IDENTITY-SERVICE/validate?token" + authHeader, String.class);
                    jwtUtils.validateToken(authHeader);
                    logger.info("After valid token of APIGATEWAY>> 2 >>>>>");

                } catch (Exception e) {
                    System.out.println("invalid access...!");
                    throw new RuntimeException("un authorized access to application");
                }
            }
            return chain.filter(exchange);
        });
    }
    public static class Config {

    }
    }