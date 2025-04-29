package com.flashcards.p3.flashcard_webapp.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.flashcards.p3.flashcard_webapp.dtos.LoginUserDto;
import com.flashcards.p3.flashcard_webapp.dtos.RegisterUserDto;
import com.flashcards.p3.flashcard_webapp.models.User;

public class AccountService {
    private final DataSource dataSource;
    private final BCryptPasswordEncoder passwordEncoder;
    private User loggedInUser = null;

    @Autowired
    public AccountService(DataSource dataSource) {
        this.dataSource = dataSource;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    public boolean registerUser(RegisterUserDto user) throws SQLException {
        final String sql1 = "insert into Users (username, password, firstName, lastName) values (?, ?, ?, ?)";

        try (Connection conn = dataSource.getConnection();
                PreparedStatement registerStmt = conn.prepareStatement(sql1)) {
            registerStmt.setString(1, user.getUsername());
            registerStmt.setString(2, passwordEncoder.encode(user.getPassword()));
            registerStmt.setString(3, user.getFirstName());
            registerStmt.setString(4, user.getLastName());

            int rowsAffected = registerStmt.executeUpdate();
            return rowsAffected > 0;
        }
    }

    public boolean loginUser(LoginUserDto user) throws SQLException {

        final String sql = "select * from User where username = ?";

        try (Connection conn = dataSource.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            try (ResultSet rs = pstmt.executeQuery()) {

                while (rs.next()) {
                    String storedPasswordHash = rs.getString("password");
                    boolean isPassMatch = passwordEncoder.matches(user.getPassword(), storedPasswordHash);

                    if (isPassMatch) {
                        String userId = rs.getString("userId");
                        String firstName = rs.getString("firstName");
                        String lastName = rs.getString("lastName");

                        User loggedInUser = new User(userId, firstName, lastName);
                        this.loggedInUser = loggedInUser;
                    }
                    return isPassMatch;
                }
            }
        }
        return false;
    }

    public void unAuthenticate() {
        loggedInUser = null;
    }

    public User getLoggedInUser() {
        return loggedInUser;
    }

    public boolean isAuthenticated() {
        return loggedInUser != null;
    }
}
