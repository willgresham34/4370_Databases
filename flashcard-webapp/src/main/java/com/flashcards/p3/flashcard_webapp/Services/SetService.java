package com.flashcards.p3.flashcard_webapp.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.object.RdbmsOperation;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.SessionScope;

import com.flashcards.p3.flashcard_webapp.models.Flashcard;
import com.flashcards.p3.flashcard_webapp.models.Set;
import com.flashcards.p3.flashcard_webapp.models.fullSet;

@Service
@SessionScope
public class SetService {
    private final DataSource dataSource;
    private final AccountService accountService;

    @Autowired
    public SetService(DataSource dataSource, AccountService accountService) {
        this.dataSource = dataSource;
        this.accountService = accountService;
    }


    public boolean addSet(Set newSet, String userId) throws SQLException {
        final String sql = "insert into Sets (userId, setName, setDescription, setCategory) values (?, ?, ?)";

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
        final String sql = "insert into Flashcards (setId, cardTerm, cardDesc) values (?, ?, ?)";

        try (Connection conn = dataSource.getConnection();
            PreparedStatement cardStmt = conn.prepareStatement(sql)) {
                cardStmt.setString(1, setId);
                cardStmt.setString(2, newCard.getTerm());
                cardStmt.setString(3, newCard.getcardDesc());

                int rowsAffected = cardStmt.executeUpdate();
                return rowsAffected > 0;

        }
    }

    public List <fullSet> constructFullSet(String setId) throws SQLException {

        final String sql = "select * from Sets where setId = ?";

        try (Connection conn = dataSource.getConnection();
                PreparedStatement pstmt1 = conn.prepareStatement(sql);
                ) {

                    //Fetch set info
                    pstmt1.setString(1, setId);
                    ResultSet rs1 = pstmt1.executeQuery();
                    rs1.next();
                    String[] setInfo = {rs1.getString("setName"), rs1.getString("setDescription"), rs1.getString("setCategory")};
                    List <Flashcard> cards = getCards(setId);

                    fullSet set = new fullSet(setId, setInfo[0], setInfo[1], setInfo[2], cards);
                    return List.of(set);
                
                }
            }
    
    public List <Flashcard> getCards(String setId) throws SQLException{
        final String sql = "select * from Flashcards where setId = ?";
        try (Connection conn = dataSource.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, setId);
                ResultSet rs = pstmt.executeQuery();
                List <Flashcard> setCards = new ArrayList<Flashcard> ();
                while (rs.next()) {
                    Flashcard tempCard = new Flashcard(rs.getString("cardId"), setId, rs.getString("cardTerm"), rs.getString("cardDesc"));
                    setCards.add(tempCard);
                }
                return setCards;
            }
    }

    public List <Set> currentUserSets() throws SQLException {
        String currentUserId = accountService.getLoggedInUser().getUserId();
        String sql = "select * from Sets where userId = ?";

        try (Connection conn = dataSource.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, currentUserId);
                ResultSet rs = pstmt.executeQuery();
                List <Set> userSets = new ArrayList<Set> ();
                while (rs.next()) {
                    Set tempSet = new Set(rs.getString("setId"), rs.getString("setName"), rs.getString("setDescription"), rs.getString("setCategory"));
                    userSets.add(tempSet);
                }
                return userSets;
            }
            
    }

    public List <Set> getSetsByCategory(String category) throws SQLException {
        String sql = "select * from Sets where setCategory = ?";

        try (Connection conn = dataSource.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, category);
                ResultSet rs = pstmt.executeQuery();
                List <Set> catSets = new ArrayList<Set> ();
                while (rs.next()) {
                    Set tempSet = new Set(rs.getString("setId"), rs.getString("setName"), rs.getString("setDescription"), rs.getString("setCategory"));
                    catSets.add(tempSet);
                }
                return catSets;
            }
        
    }

    public List <Set> getSetsByName(String name) throws SQLException {
        String sql = "select * from Sets where setName = ?";
        try (Connection conn = dataSource.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, name);
                ResultSet rs = pstmt.executeQuery();
                List <Set> nameSets = new ArrayList<Set> ();
                while (rs.next()) {
                    Set tempSet = new Set(rs.getString("setId"), rs.getString("setName"), rs.getString("setDescription"), rs.getString("setCategory"));
                    nameSets.add(tempSet);
                }
                return nameSets;
            }
    }

    public List <Set> getNewestSets() throws SQLException {
        String sql = "select * from Sets order by setId desc";
        try (Connection conn = dataSource.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
                ResultSet rs = pstmt.executeQuery();
                List <Set> sets = new ArrayList<Set> ();
                while (rs.next()) {
                    Set tempSet = new Set(rs.getString("setId"), rs.getString("setName"), rs.getString("setDescription"), rs.getString("setCategory"));
                    sets.add(tempSet);
                }
                return sets;
            }
    }

    public int countFavorite(String setId) throws SQLException{
        String sql = """
                WITH setIdThree AS (SELECT folderId FROM Set_Folders WHERE setId = ?),
                folderNames AS (SELECT folderName FROM Folders f LEFT JOIN setIdThree ON setIdThree.folderId = f.folderId)
                SELECT COUNT(*) AS fav_count FROM folderNames WHERE folderName = "Favorites"
                """;
        
        try (Connection conn = dataSource.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, setId);
                ResultSet rs = pstmt.executeQuery();
                rs.next();
                int count = rs.getInt("fav_count");
                return count;
            }
    }
}
