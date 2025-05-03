
/*
This code was taken and modfied from Project 2 by Prof Meena. 
*/
package com.flashcards.p3.flashcard_webapp.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.flashcards.p3.flashcard_webapp.components.AuthInterceptor;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final AuthInterceptor _authInterceptor;

    @Autowired
    public WebConfig(AuthInterceptor authInterceptor) {
        _authInterceptor = authInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(_authInterceptor)
                .addPathPatterns("/profile/*");
    }

}
