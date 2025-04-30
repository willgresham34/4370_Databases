package com.flashcards.p3.flashcard_webapp.services;

import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.flashcards.p3.flashcard_webapp.models.Set;

@Service
public class LikesService {
    
    private final DataSource dataSource;

    @Autowired
    public LikesService(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public List<Set> getLikedSets(String userId) throws SQLException {
        throw new UnsupportedOperationException("getLikedSets not implemented");
    }
}
