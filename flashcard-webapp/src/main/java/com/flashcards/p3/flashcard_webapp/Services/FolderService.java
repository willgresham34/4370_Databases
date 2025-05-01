package com.flashcards.p3.flashcard_webapp.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
    private final AccountService accountService;

    @Autowired
    public FolderService(DataSource dataSource, AccountService accountService) {
        this.dataSource = dataSource;
        this.accountService = accountService;
    }

    /**
     * Returns all folders belonging to the current logged in user.
     * 
     * @return a list containing all folders owned by the current user.
     */
    public List<Folder> getFolders() {
        throw new UnsupportedOperationException("getFoldersByUserId not implemented");
    }

    /**
     * Returns the folder with the specified folderId
     * 
     * @param folderId  the id of the folder to search for.
     * 
     * @return the folder.
     */
    public Folder getFolderById(String folderId) {
        throw new UnsupportedOperationException("getFolderById not implemented");
    }

    /**
     * Creates a folder that will belong to the current logged in user.
     * 
     * @param folderName    the name of the new folder.
     * 
     * @return {@code true} if the update changes any rows, {@code false} otherwise
     * @throws SQLException if the SQL statement is invalid.
     */
    public boolean addFolder(String folderName) throws SQLException {
        final String sql = "insert into Folders (userId, folderName) values (?, ?)";

        try (Connection conn = dataSource.getConnection();
            PreparedStatement folderStmt = conn.prepareStatement(sql)) {
                folderStmt.setString(1, accountService.getLoggedInUser().getUserId());
                folderStmt.setString(2, folderName);

                int rowsAffected = folderStmt.executeUpdate();
                return rowsAffected > 0;

        }
    }

    /**
     * Adds a set to the specified folder.
     * 
     * @param setId
     * @param folderId
     * @return {@code true} if the update changes any rows, {@code false} otherwise
     * @throws SQLException if the SQL statement is invalid.
     */
    public boolean addSetToFolder(String setId, Folder folder) throws SQLException {
        final String sql = "insert into Set_Folders (setId, folderId)";

        if (!folder.getUser().equals(accountService.getLoggedInUser())) {
            throw new IllegalArgumentException("Folder does not belong to user");
        }
        try (Connection conn = dataSource.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, setId);
                stmt.setString(2, folder.getFolderId());

                int rowsAffected = stmt.executeUpdate();
                return rowsAffected > 0;
        }            
    }

    /**
     * Adds or removes a set from the favorites folder. If the user does not have
     * a favorites folder and wants to add a set, creates a folder for them.
     * 
     * @param setId     the set to add/remove from the favorites folder
     * @param isAdd     specifies whether the set is being added or removed.
     * @return {@code true} if favorites folder updated, {@code false} otherwise
     * @throws SQLException if the SQL statement is invalid.
     */
    public boolean setToFavorites(String setId, Boolean isAdd) throws SQLException {
        final String loggedInUserId = accountService.getLoggedInUser().getUserId();
        final String sql1 = "select setId from Folders where userId = ? and folderName = 'Favorites'";
        final String sql2 = "insert into Set_Folders (setId, folderId) values (?, ?)";
        final String sql3 = "delete from Set_Folders where setId = ? and folderId = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement pstmt1 = conn.prepareStatement(sql1);
             PreparedStatement pstmt2 = conn.prepareStatement(sql2);
             PreparedStatement pstmt3 = conn.prepareStatement(sql3);
        ) {
            pstmt1.setString(1, loggedInUserId);
            ResultSet rs1 = pstmt1.executeQuery();
            
            if (rs1.next() == false && isAdd == true) {
                addFolder("Favorites");
                pstmt1.setString(1, loggedInUserId);
                rs1 = pstmt1.executeQuery();
            } else if (rs1.next() == false && isAdd == false) {
                throw new SQLException("Favorites folder does not exist");
            }
            String favoriteFolderId = rs1.getString("setId");
            int affectedRows;
            if (isAdd) {
                pstmt2.setString(1, setId);
                pstmt2.setString(2, favoriteFolderId);
                affectedRows = pstmt2.executeUpdate();
            } else {
                pstmt3.setString(1, setId);
                pstmt3.setString(2, favoriteFolderId);
                affectedRows = pstmt3.executeUpdate();
            }
            return affectedRows > 0;
        }
    }

    /**
     * Updates the given folder with new information.
     * 
     * @param folder    the folder to update within the database.
     * @return {@code true} if folder updated, {@code false} otherwise
     * @throws SQLException if SQL statement is invalid
     * @throws IllegalArgumentException if user tries to edit their
     *         favorites folder or a folder owned by a different user.
     */
    public boolean updateFolder(Folder folder) throws SQLException, IllegalArgumentException {
        if (folder.getFolderName().equals("Favorites")) {
            throw new IllegalArgumentException("Cannot edit favorites folder");
        } else if (!folder.getUser().equals(accountService.getLoggedInUser())) {
            throw new IllegalArgumentException("Folder does not belong to user");
        } 

        final String sql = "UPDATE Folders SET folderName = ? WHERE folderId = ?";

        try (Connection conn = dataSource.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, folder.getFolderName());
                pstmt.setString(2, folder.getFolderId());

                int rowsAffected = pstmt.executeUpdate();
                return rowsAffected > 0;
        }
    }

    /**
     * Deletes the specified folder.
     * 
     * @param folder    the folder to delete
     * @return {@code true} if folder deleted, {@code false} otherwise
     * @throws SQLException if SQL statement is invalid
     * 
     */
    public boolean deleteFolder(Folder folder) throws SQLException {

        if (folder.getFolderName().equals("Favorites")) {
            throw new IllegalArgumentException("Cannot delete favorites folder");
        } else if (!folder.getUser().equals(accountService.getLoggedInUser())) {
            throw new IllegalArgumentException("Folder does not belong to user");
        }

        final String sql = "DELETE FROM Folders WHERE folderId = ?";

        try (Connection conn = dataSource.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(2, folder.getFolderId());

                int rowsAffected = pstmt.executeUpdate();
                return rowsAffected > 0;
        }
    }
}
