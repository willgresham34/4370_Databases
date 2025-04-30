package com.flashcards.p3.flashcard_webapp.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.SessionScope;

import com.flashcards.p3.flashcard_webapp.models.Folder;

@Service
@SessionScope
public class FolderService {
    private final DataSource dataSource;

    @Autowired
    public FolderService(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public boolean addFolder(String userId, String folderName) throws SQLException {
        final String sql = "insert into Folders (userId, folderName)";

        try (Connection conn = dataSource.getConnection();
            PreparedStatement folderStmt = conn.prepareStatement(sql)) {
                folderStmt.setString(1, userId);
                folderStmt.setString(2, folderName);

                int rowsAffected = folderStmt.executeUpdate();
                return rowsAffected > 0;

        }
    }

    public boolean addSetToFolder(String setId, String folderId) throws SQLException{
        final String sql = "inset into Set_Folders (setId, folderId)";

        try (Connection conn = dataSource.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, setId);
                stmt.setString(2, folderId);

                int rowsAffected = stmt.executeUpdate();
                return rowsAffected > 0;
        }            
    }

    public List<Folder> getFoldersByUserId(String userId) {
        throw new UnsupportedOperationException("getFoldersByUserId not implemented");
    }

    public Folder getFolderById(String folderId) {
        throw new UnsupportedOperationException("getFolderById not implemented");
    }
}
