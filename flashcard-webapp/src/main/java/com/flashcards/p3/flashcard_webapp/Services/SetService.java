package com.flashcards.p3.flashcard_webapp.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.SessionScope;

import com.flashcards.p3.flashcard_webapp.models.Flashcard;
import com.flashcards.p3.flashcard_webapp.models.Set;
import com.flashcards.p3.flashcard_webapp.models.User;
import com.flashcards.p3.flashcard_webapp.models.FullSet;

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
        final String sql = "insert into Sets (userId, setName, setDescription, setCategory) values (?, ?, ?);";

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
        final String sql = "insert into Flashcards (setId, cardTerm, cardDesc) values (?, ?, ?);";

        try (Connection conn = dataSource.getConnection();
            PreparedStatement cardStmt = conn.prepareStatement(sql)) {
                cardStmt.setString(1, setId);
                cardStmt.setString(2, newCard.getTerm());
                cardStmt.setString(3, newCard.getcardDesc());

                int rowsAffected = cardStmt.executeUpdate();
                return rowsAffected > 0;

        }
    }

    public FullSet constructFullSet(String setId) throws SQLException {

        final String sql = "select * from Sets s, Users u where s.userId = u.userId and setId = ?;";

        try (Connection conn = dataSource.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql);
        ) {
            //Fetch set info
            pstmt.setString(1, setId);
            ResultSet rs = pstmt.executeQuery();
            rs.next();
            User user = new User(rs.getString("userId"), 
                                    rs.getString("firstName"),
                                    rs.getString("lastName"));
            List <Flashcard> cards = getCards(setId);
            FullSet set = new FullSet(setId, user, rs.getString("setId"),
                                        rs.getString("setDescription"), rs.getString("setCategory"),
                                        cards.size(), cards);
            return set;
        }
    }
    
    public List <Flashcard> getCards(String setId) throws SQLException{
        final String sql = "select * from Flashcards where setId = ?;";
        try (Connection conn = dataSource.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, setId);
                ResultSet rs = pstmt.executeQuery();
                List <Flashcard> setCards = new ArrayList<Flashcard>();
                while (rs.next()) {
                    Flashcard tempCard = new Flashcard(rs.getString("cardId"), setId, rs.getString("cardTerm"), rs.getString("cardDesc"));
                    setCards.add(tempCard);
                }
                return setCards;
            }
    }

    public List <Set> currentUserSets() throws SQLException {
        String currentUserId = accountService.getLoggedInUser().getUserId();
        String sql = """
                select s.setId, s.setName, s.setDescription, s.setCategory,
                u.userId, u.firstName, u.lastName,
                (SELECT COUNT(*) FROM Flashcards f where f.setId = s.setId) as numCards
                from Sets s, Users u where s.userId = u.userId and s.userId = ?;
        """;
        try (Connection conn = dataSource.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, currentUserId);
                ResultSet rs = pstmt.executeQuery();
                List <Set> userSets = new ArrayList<Set> ();
                while (rs.next()) {
                    User tempUser = new User(rs.getString("userId"), 
                                         rs.getString("firstName"),
                                         rs.getString("lastName"));
                    Set tempSet = new Set(rs.getString("setId"), tempUser, rs.getString("setName"), 
                                          rs.getString("setDescription"), rs.getString("setCategory"), rs.getInt("numCards"));
                    userSets.add(tempSet);
                }
                return userSets;
            }
            
    }

    public List <Set> getSetsByCategory(String category) throws SQLException {
        String sql =  """
                select s.setId, s.setName, s.setDescription, s.setCategory,
                u.userId, u.firstName, u.lastName,
                (SELECT COUNT(*) FROM Flashcards f where f.setId = s.setId) as numCards
                from Sets s, Users u where s.userId = u.userId and s.setCategory = ?;
        """;
        try (Connection conn = dataSource.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, category);
                ResultSet rs = pstmt.executeQuery();
                List <Set> catSets = new ArrayList<Set> ();
                while (rs.next()) {
                    User tempUser = new User(rs.getString("userId"), 
                                         rs.getString("firstName"),
                                         rs.getString("lastName"));
                    Set tempSet = new Set(rs.getString("setId"), tempUser, rs.getString("setName"), 
                                          rs.getString("setDescription"), rs.getString("setCategory"), rs.getInt("numCards"));
                    catSets.add(tempSet);
                }
                return catSets;
            }
        
    }

    public List <Set> getSetsByName(String name) throws SQLException {
        String sql = """
            select s.setId, s.setName, s.setDescription, s.setCategory,
            u.userId, u.firstName, u.lastName,
            (SELECT COUNT(*) FROM Flashcards f where f.setId = s.setId) as numCards
            from Sets s, Users u where s.userId = u.userId and s.setName = ?;
        """;
        try (Connection conn = dataSource.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, name);
                ResultSet rs = pstmt.executeQuery();
                List <Set> nameSets = new ArrayList<Set> ();
                while (rs.next()) {
                    User tempUser = new User(rs.getString("userId"), 
                                         rs.getString("firstName"),
                                         rs.getString("lastName"));
                    Set tempSet = new Set(rs.getString("setId"), tempUser, rs.getString("setName"), 
                                          rs.getString("setDescription"), rs.getString("setCategory"), rs.getInt("numCards"));
                    nameSets.add(tempSet);
                }
                return nameSets;
            }
    }

    public List <Set> getNewestSets() throws SQLException {
        String sql = """
                select s.setId, s.setName, s.setDescription, s.setCategory,
                u.userId, u.firstName, u.lastName,
                (SELECT COUNT(*) FROM Flashcards f where f.setId = s.setId) as numCards
                from Sets s, Users u where s.userId = u.userId
                ORDER BY s.setId DESC;
        """;
        try (Connection conn = dataSource.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
                ResultSet rs = pstmt.executeQuery();
                List <Set> sets = new ArrayList<Set> ();
                while (rs.next()) {
                    User tempUser = new User(rs.getString("userId"), 
                                         rs.getString("firstName"),
                                         rs.getString("lastName"));
                    Set tempSet = new Set(rs.getString("setId"), tempUser, rs.getString("setName"), 
                                          rs.getString("setDescription"), rs.getString("setCategory"), rs.getInt("numCards"));
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

    public boolean updateFlashcardTerm (String cardId, String term) throws SQLException {
        String sql1 = """
                WITH flashcard AS (SELECT setId FROM Flashcards WHERE cardId = ?)
                SELECT userId FROM Sets s JOIN flashcard ON flashcard.setId = s.setId
                """;
        String sql2  ="UPDATE Flashcards SET cardTerm = ? WHERE cardId = ?";

        try (Connection conn = dataSource.getConnection();
            PreparedStatement pstmt1 = conn.prepareStatement(sql1);
            PreparedStatement pstmt2 = conn.prepareStatement(sql2)) {
                pstmt1.setString(1, cardId);
                pstmt2.setString(1, term);
                pstmt2.setString(2, cardId);

                ResultSet rs1 = pstmt1.executeQuery();
                rs1.next();
                String userId = rs1.getString("userId");

                if (userId.equals(accountService.getLoggedInUser().getUserId())) {
                    pstmt2.executeUpdate();
                    return true;
                } else {
                    throw new IllegalArgumentException("Cannot update another user's card");
                }
            }
    }

    public boolean updateFlashcardDesc (String cardId, String desc) throws SQLException {
        String sql1 = """
                WITH flashcard AS (SELECT setId FROM Flashcards WHERE cardId = ?)
                SELECT userId FROM Sets s JOIN flashcard ON flashcard.setId = s.setId
                """;
        String sql2  = "UPDATE Flashcards SET cardDesc = ? WHERE cardId = ?";

        try (Connection conn = dataSource.getConnection();
            PreparedStatement pstmt1 = conn.prepareStatement(sql1);
            PreparedStatement pstmt2 = conn.prepareStatement(sql2)) {
                pstmt1.setString(1, cardId);
                pstmt2.setString(1, desc);
                pstmt2.setString(2, cardId);

                ResultSet rs1 = pstmt1.executeQuery();
                rs1.next();
                String userId = rs1.getString("userId");

                if (userId.equals(accountService.getLoggedInUser().getUserId())) {
                    pstmt2.executeUpdate();
                    return true;
                } else {
                    throw new IllegalArgumentException("Cannot update another user's card");
                }
            }
    }

    public boolean updateSetName (String setId, String name) throws SQLException {
        String sql1 = "SELECT userId from Sets Where setId = ?";
        String sql2 = "UPDATE Sets SET setName = ? WHERE setId = ?";

        try (Connection conn = dataSource.getConnection();
            PreparedStatement pstmt1 = conn.prepareStatement(sql1);
            PreparedStatement pstmt2 = conn.prepareStatement(sql2)) {
                pstmt1.setString(1, setId);
                pstmt2.setString(1, name);
                pstmt2.setString(2, setId);

                ResultSet rs1 = pstmt1.executeQuery();
                rs1.next();
                String userId = rs1.getString("userId");

                if(userId.equals(accountService.getLoggedInUser().getUserId())) {
                    pstmt2.executeUpdate();
                    return true;
                } else {
                    throw new IllegalArgumentException("Cannot update another user's set");
                }
        }
    }

    public boolean updateSetDescription (String setId, String desc) throws SQLException {
        String sql1 = "SELECT userId from Sets Where setId = ?";
        String sql2 = "UPDATE Sets SET setDescription = ? WHERE setId = ?";
        try (Connection conn = dataSource.getConnection();
            PreparedStatement pstmt1 = conn.prepareStatement(sql1);
            PreparedStatement pstmt2 = conn.prepareStatement(sql2)) {
                pstmt1.setString(1, setId);
                pstmt2.setString(1, desc);
                pstmt2.setString(2, setId);

                ResultSet rs1 = pstmt1.executeQuery();
                rs1.next();
                String userId = rs1.getString("userId");

                if(userId.equals(accountService.getLoggedInUser().getUserId())) {
                    pstmt2.executeUpdate();
                    return true;
                } else {
                    throw new IllegalArgumentException("Cannot update another user's set");
                }
        }
    }

    public boolean updateSetCategory (String setId, String category) throws SQLException {
        String sql1 = "SELECT userId from Sets Where setId = ?";
        String sql2 = "UPDATE Sets SET setCategory = ? WHERE setId = ?";

        try (Connection conn = dataSource.getConnection();
            PreparedStatement pstmt1 = conn.prepareStatement(sql1);
            PreparedStatement pstmt2 = conn.prepareStatement(sql2)) {
                pstmt1.setString(1, setId);
                pstmt2.setString(1, category);
                pstmt2.setString(2, setId);

                ResultSet rs1 = pstmt1.executeQuery();
                rs1.next();
                String userId = rs1.getString("userId");

                if(userId.equals(accountService.getLoggedInUser().getUserId())) {
                    pstmt2.executeUpdate();
                    return true;
                } else {
                    throw new IllegalArgumentException("cannot update another user's set");
                }
        }
    }

    public boolean deleteSet(String setId) throws SQLException{
        String sql = "DELETE FROM Sets WHERE setId = ?";

        try (Connection conn = dataSource.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, setId);
                int rowsAffected = pstmt.executeUpdate();
                return rowsAffected > 0;
            }
    }

    public boolean deleteFlashcard(String cardId) throws SQLException{
        String sql = "DELETE FROM Flashcards WHERE cardId = ?";
        try (Connection conn = dataSource.getConnection();
        PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, cardId);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        }
    }
}
