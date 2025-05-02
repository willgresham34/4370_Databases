/* This query combines data from the Folders and Users tables
to return a list of folders, and the users who created them,
created by the user with a given userId. It is used at the 
getUserFolders() method to get folders created by the logged in user.
It is used at: <insert path here> */
SELECT 
    f.folderId, f.folderName,
    u.userId, u.firstName, u.lastName
FROM 
    Folders f, Users u
WHERE
    f.userId = u.userId and
    f.userId = ?;

/* This query combines data from the Sets, Set_Folders, and Users 
tables to return a list of every set, including their flashcard count,
contained within the folder with the given folderId.
It is used at: <insert path here> */
SELECT
    s.setId, s.setName, s.setDescription, s.setCategory,
    (SELECT COUNT(*) FROM Flashcards f where f.setId = s.setId) as numCards,
    u.userId, u.firstName, u.lastName
FROM Sets s, Set_Folders sf, Users u
WHERE 
    s.setId = sf.setId and
    s.userId = u.userId and
    sf.folderId = ?;

/* This query combines data from the Sets and Users tables to return 
set information for the given setId, including user attributes.
It is used at: <insert path here> */
select * from Sets s, Users u where s.userId = u.userId and setId = ?;

/* This query returns Flashcard information for all flashcards 
belonging to the set with the given setId. 
It is used at: <insert path here> 
It is (also) used internally by other methods in SetService.java.*/
select * from Flashcards where setId = ?;

/* This query combines data from the Sets and Users tables to return 
a list of every set, including flashcard count and user information,
created by the user specified by s.userId. It is used at the
currentUserSets() method to get sets created by the current user.
It is used at: <insert path here> */
select s.setId, s.setName, s.setDescription, s.setCategory,
u.userId, u.firstName, u.lastName,
(SELECT COUNT(*) FROM Flashcards f where f.setId = s.setId) as numCards
from Sets s, Users u where s.userId = u.userId and s.userId = ?;

/* This query combines data from the Sets and Users tables to return 
a list of every set, including flashcard count and user information,
that has the setCategory specified.
It is used at: <insert path here> */
select s.setId, s.setName, s.setDescription, s.setCategory,
u.userId, u.firstName, u.lastName,
(SELECT COUNT(*) FROM Flashcards f where f.setId = s.setId) as numCards
from Sets s, Users u where s.userId = u.userId and s.setCategory = ?;

/* This query combines data from the Sets and Users tables to return 
a list of every set, including flashcard count and user information,
that has the setName specified.
It is used at: <insert path here> */
select s.setId, s.setName, s.setDescription, s.setCategory,
u.userId, u.firstName, u.lastName,
(SELECT COUNT(*) FROM Flashcards f where f.setId = s.setId) as numCards
from Sets s, Users u where s.userId = u.userId and s.setName = ?;

/* This query combines data from the Sets and Users tables to return 
a list of every set, including flashcard count and user information,
ordered by newest to oldest.
It is used at: <insert path here> */
select s.setId, s.setName, s.setDescription, s.setCategory,
u.userId, u.firstName, u.lastName,
(SELECT COUNT(*) FROM Flashcards f where f.setId = s.setId) as numCards
from Sets s, Users u where s.userId = u.userId
ORDER BY s.setId DESC;