/* This query combines data from the Folders and Users tables
to return a list of folders, and the users who created them,
created by the user with a given userId. It is used at the 
getUserFolders() method to get folders created by the logged in user
as well as getFoldersByUserId(String userId)
It is used at: http://localhost:8080/profile/currentUser */
SELECT 
    f.folderId, f.folderName,
    u.userId, u.firstName, u.lastName
FROM 
    Folders f, Users u
WHERE
    f.userId = u.userId and
    f.userId = ?;

/* This query combines data from the Folders and Users tables
to return the folder, and the user who created it, with the
given folderId.
It is used at: <insert path here> */
SELECT
    f.folderId, f.folderName,
    u.userId, u.firstName, u.lastName, u.username
FROM Folders f, Users u
WHERE
    f.userId = u.userId and
    f.folderId = ?;

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

/* This query adds a new folder into the Folders table.
It is used at: http://localhost:8080/profile/currentUser */
insert into Folders (userId, folderName) values (?, ?);

/* This query adds a new set to a given folder.
It is used at: <insert path here> */
insert into Set_Folders (setId, folderId) values (?, ?);

/* This query updates the folder information for a given folder.
It is used at: <insert path here> */
UPDATE Folders SET folderName = ? WHERE folderId = ?;

/* This folder deletes the given folder from the Folders table.
It is used at: <insert path here> */
DELETE FROM Folders WHERE folderId = ?;

/* This query deletes the given set from a given folder.
It is used at: <insert path here> */
DELETE FROM Set_Folders WHERE setId = ? AND folderId = ?;

/* This query combines data from the Sets and Users tables to return 
set information for the given setId, including user attributes.
It is used at: http://localhost:8080/set-details?setId=() */
select * from Sets s, Users u where s.userId = u.userId and setId = ?;

/* This query returns Flashcard information for all flashcards 
belonging to the set with the given setId. 
It is used at: <insert path here> 
It is (also) used internally by other methods in SetService.java.*/
select * from Flashcards where setId = ?;

/* This query combines data from the Sets and Users tables to return 
a list of every set, including flashcard count and user information,
created by the user specified by s.userId.
It is used at: http://localhost:8080/profile/currentUser */
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
It is used at: http://localhost:8080 (the home page) */
select s.setId, s.setName, s.setDescription, s.setCategory,
u.userId, u.firstName, u.lastName,
(SELECT COUNT(*) FROM Flashcards f where f.setId = s.setId) as numCards
from Sets s, Users u where s.userId = u.userId
ORDER BY s.setId DESC;

/* This query adds a new set into the Sets table.
It is used at: <insert path here> */
insert into Sets (userId, setName, setDescription, setCategory) values (?, ?, ?, ?);

/* This query adds a new flashcard belonging to the given set into the Flashcards table
It is used at: <insert path here> */
insert into Flashcards (setId, cardTerm, cardDesc) values (?, ?, ?);

/* This query finds the userId associated with a flashcard by
joining it with the sets table.
It is used at: <insert path here> */
WITH flashcard AS (SELECT setId FROM Flashcards WHERE cardId = ?)
SELECT userId FROM Sets s JOIN flashcard ON flashcard.setId = s.setId

/* This query updates the given flashcard with new term and description.
It is used at: <insert path here> */
UPDATE Flashcards SET cardTerm = ?, cardDesc = ? WHERE cardId = ?

/* This query returns the userId associated with a given set.
It is used at: <insert path here> */
SELECT userId from Sets Where setId = ?

/* This query updates the given set with new name, description, and category.
It is used at: <insert path here> */
UPDATE Sets SET setName = ?, setDescription = ?, setCategory = ? WHERE setId = ?

/* This query deletes the given set.
It is used at: <insert path here> */
DELETE FROM Sets WHERE setId = ?

/* This query deletes the given flashcard.
It is used at: <insert path here> */
DELETE FROM Flashcards WHERE cardId = ?