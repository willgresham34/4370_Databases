USE flashcards_db;

-- 1) Seed 4 base users (unchanged)
INSERT INTO
  Users (username, password, firstName, lastName)
VALUES
  (
    'aliceA12',
    '$2a$10$eiXrg17gVwljo9aiekPlOu49bF6JrizJkwq01kmQUDcPmYl1/4k4.',
    'Alice',
    'Anderson'
  ),
  (
    'bobB34',
    '$2a$10$eiXrg17gVwljo9aiekPlOu49bF6JrizJkwq01kmQUDcPmYl1/4k4.',
    'Bob',
    'Brown'
  ),
  (
    'carolC56',
    '$2a$10$eiXrg17gVwljo9aiekPlOu49bF6JrizJkwq01kmQUDcPmYl1/4k4.',
    'Carol',
    'Clark'
  ),
  (
    'daveD78',
    '$2a$10$eiXrg17gVwljo9aiekPlOu49bF6JrizJkwq01kmQUDcPmYl1/4k4.',
    'Dave',
    'Davis'
  );

-- 2) Manually insert 20 sets (5 per user), with college-subject categories
INSERT INTO
  Sets (
    setId,
    userId,
    setName,
    setDescription,
    setCategory
  )
VALUES
  -- Alice sets 
  (
    1,
    1,
    'Alice Set 1',
    'Alice for class Mathematics',
    'Mathematics'
  ),
  (
    2,
    1,
    'Alice Set 2',
    'Alice for class Biology',
    'Biology'
  ),
  (
    3,
    1,
    'Alice Set 3',
    'Alice for class Chemistry',
    'Chemistry'
  ),
  (
    4,
    1,
    'Alice Set 4',
    'Alice for class History',
    'History'
  ),
  (
    5,
    1,
    'Alice Set 5',
    'Alice for class Physics',
    'Physics'
  ),
  -- Bob sets 
  (
    6,
    2,
    'Bob Set 1',
    'Bob for class Literature',
    'Literature'
  ),
  (
    7,
    2,
    'Bob Set 2',
    'Bob for class Philosophy',
    'Philosophy'
  ),
  (
    8,
    2,
    'Bob Set 3',
    'Bob for class Economics',
    'Economics'
  ),
  (
    9,
    2,
    'Bob Set 4',
    'Bob for class Psychology',
    'Psychology'
  ),
  (
    10,
    2,
    'Bob Set 5',
    'Bob for class Sociology',
    'Sociology'
  ),
  -- Carol sets
  (
    11,
    3,
    'Carol Set 1',
    'Carol for class Computer Science',
    'Computer Science'
  ),
  (
    12,
    3,
    'Carol Set 2',
    'Carol for class Statistics',
    'Statistics'
  ),
  (
    13,
    3,
    'Carol Set 3',
    'Carol for class Art',
    'Art'
  ),
  (
    14,
    3,
    'Carol Set 4',
    'Carol for class Music',
    'Music'
  ),
  (
    15,
    3,
    'Carol Set 5',
    'Carol for class Theater',
    'Theater'
  ),
  -- Dave sets 
  (
    16,
    4,
    'Dave Set 1',
    'Dave for class Engineering',
    'Engineering'
  ),
  (
    17,
    4,
    'Dave Set 2',
    'Dave for class Business',
    'Business'
  ),
  (
    18,
    4,
    'Dave Set 3',
    'Dave for class Finance',
    'Finance'
  ),
  (
    19,
    4,
    'Dave Set 4',
    'Dave for class Law',
    'Law'
  ),
  (
    20,
    4,
    'Dave Set 5',
    'Dave for class Medicine',
    'Medicine'
  );

INSERT INTO
  Folders (folderId, userId, folderName)
VALUES
  (1, 1, 'Major Related Classes'),
  (2, 2, 'Ology Classes'),
  (3, 3, 'Carols Empty Folder');

INSERT INTO
  Set_Folders (setId, folderId)
VALUES
  -- Alice Major
  (2, 1),
  (4, 1),
  (5, 1),
  -- Bob Ology
  (9, 2),
  (10, 2);