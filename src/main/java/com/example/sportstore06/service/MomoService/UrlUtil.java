package com.example.sportstore06.service.MomoService;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;


public class UrlUtil {
    public static String getBaseUrl(HttpServletRequest request) {
        String baseUrl = ServletUriComponentsBuilder
                .fromRequestUri(request)
                .replacePath(null)
                .build()
                .toUriString();

        return baseUrl;
    }
}