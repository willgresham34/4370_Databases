package com.flashcards.p3.flashcard_webapp.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.SessionScope;

import com.flashcards.p3.flashcard_webapp.models.Flashcard;
import com.flashcards.p3.flashcard_webapp.models.Set;

@Service
@SessionScope
public class SetService {
    private final DataSource dataSource;

    @Autowired
    public SetService(DataSource dataSource) {
        this.dataSource = dataSource;
    }


    public boolean addSet(Set newSet, String userId) throws SQLException {
        final String sql = "insert into Sets (userId, setName, setDescription, setCategory)";

        try (Connection conn = dataSource.getConnection();
            PreparedStatement setStmt = conn.prepareStatement(sql)) {
                setStmt.setString(1, userId);
                setStmt.setString(2, newSet.getName());
                setStmt.setString(3, newSet.getDesc());
                setStmt.setString(4, newSet.getCategory());

                int rowsAffected = setStmt.executeUpdate();
                return rowsAffected > 0;
        }
    }

    public boolean addFlashcard(Flashcard newCard, String setId) throws SQLException {
        final String sql = "insert into Flashcards (setId, cardTerm, cardDesc)";

        try (Connection conn = dataSource.getConnection();
            PreparedStatement cardStmt = conn.prepareStatement(sql)) {
                cardStmt.setString(1, setId);
                cardStmt.setString(2, newCard.getTerm());
                cardStmt.setString(3, newCard.getcardDesc());

                int rowsAffected = cardStmt.executeUpdate();
                return rowsAffected > 0;

        }
    }

    public boolean updateSet(Set existingSet) throws SQLException {
        throw new UnsupportedOperationException("updateSet not implemented");
    }

    public boolean updateFlashcard(Flashcard existingCard) {
        throw new UnsupportedOperationException("updateCard not implemented");
    }

    public boolean getSet(String setId) {
        throw new UnsupportedOperationException("getSet not implemented");
    }

    public boolean getCard(String cardId) {
        throw new UnsupportedOperationException("getCard not implemented");
    }
    
    public boolean likeToDatabase(String setId, Boolean isLiked) {
        throw new UnsupportedOperationException("likeToDatabase not implemented");
    }
}
