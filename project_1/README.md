# CSCI 4370: Project 1: Spring 2025

**Instructor: Sami Menik, PhD**

Due date: Please check the respective assignment submission box on eLC.

Read this document in full before you start working on the assignment. You may have to read it more than one time.

### Policy Information

You are strictly prohibited from sharing, publishing, distributing, or storing any part of this assignment, including complete or partial solutions, with others. Such actions constitute violations of academic honesty policy and copyright law.

Please remember the academic honesty policy for the course. The work you submit must be your own.

This is a group project, and you must work with your team registered on eLC. You can not submit this work without a team.

All members in your team are responsible for equal contribution and active communication within the team. While collaboration is essential, each member remains individually accountable for the full completion of the project irrespective of the amount of contributions received from others. You will have the opportunity to record team member contributions using the designated form in agreement with your team members, in accordance with the academic honesty policy.

Every member in the group must be fully aware of every line of code you submit in case the instructional staff questions.

### Implementation Instructions

In this project you will download a starter code and implement the project on top of the starter code. The starter code has Javadoc comments that specifies what your implementation should do.

Please find the starter code here: https://outlookuga-my.sharepoint.com/:f:/g/personal/sm19812_uga_edu/Esp4n9cPpv5MprE3vpczYIgBRohYqzCP2VjK61yo-SPg0A?e=nR7GWa

*Note: Some details are intentionally left out for the students to figure out the best approach by discussing with the team members. However, students should meet all explicitly specified requirements to receive points. Please ask if you have any doubts.*

Please note the following important points:
- You must implement the provided Java interfaces and use the provided classes as needed.
- You should not modify any provided code without explicit written permission from the instructor. Doing such changes will make it impossible to grade your project and will result in a grade of 0. You are allowed to modify the given Driver class.
- You can include additional private methods in the implementing classes as needed. 
- Your implementation must use the interfaces as types whenever it is possible instead of using concrete implementing classes as types. 
- You can not use any external libraries in this project.
- You can use Java built in data structures.

After completing the implementation, follow the steps below to demonstrate your relational algebra evaluation engine.
- Use the existing Driver class with a main method in uga.cs4370.mydbimpl package.
- Print five interesting queries in english. 
  - These queries should use tables and data from the 'uni_in_class' database.
  - You can modify and use the queries from the in class activities.
- Implement each query using the relational algebra engine that you implemented and print the resulting relation.
  - Each team member should contribute at least 1 query and its corresponding relational algebra query implementation. Each team should have a minimum of 4 queries.
  - Each query must have operation compositions and should use more than two tables.
  - Adjust the queries and their predicates as needed to make sure that the results are not empty and also not greater than around 50 rows.

### Deliverables

You should submit the following deliverables on eLC as described below.
- `README.txt` that includes group number, team members names and contributions made by each team member.
- **Group member contribution form** that is signed by each team member. You cannot earn points for this assignment without this deliverable. You can find the form on Piazza.
- Completed source code project as a zip file. Make sure to submit the whole project. Quick check: Your submission includes the `pom.xml` file at the project root level if you are submitting the right directory.

### General Instructions

Create a zip file of the deliverables and upload it on eLC under the respective assignment submission box.

The assignment accepts late submissions as explained in the syllabus on eLC.

*Note: Projects that fail to adhere to the specified structure and instructions, do not compile, or cannot be executed will unfortunately receive a grade of 0.*

*Note: If you have any questions, please post on Piazza.*
