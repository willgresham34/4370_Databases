package com.flashcards.p3.flashcard_webapp.components;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.flashcards.p3.flashcard_webapp.services.AccountService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class AuthInterceptor implements HandlerInterceptor {

    private AccountService _accountService;

    @Autowired
    public AuthInterceptor(AccountService accountService) {
        _accountService = accountService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request,
            HttpServletResponse response, Object handler) throws Exception {
        if (!_accountService.isAuthenticated()) {
            response.sendRedirect("/auth/login");
            return false;
        }
        return true;
    }

}
