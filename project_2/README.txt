Group Contributions:

Will Gresham: Implementing home page, bringing up posts from followed users on home page. Implementing profile page and bringing up all posts from a specific user. Fetch respective post data from database.

Connor Stephens: Implementation of people page and bookmark page. Bringing up followable users on people page and implementing follow/unfollow feature. Implementing bookmark feature and bringing up bookmarked posts on bookmark page.

Adam Wright: Making posts and parsing out hashtags; Bookmark, heart, and comment features for an individual post. Bringing up expanded post with comments, number of likes and bookmark value when clicked. Send heart, comment, bookmark and post data to database when each respective feature is used.

Anthony Campo: Implementation of hashtag search feature. Search database for hashtags with matching post Id and display.


How to run:

- Ensure that the project sql container is running in docker desktop
- In a terminal, navigate to the project folder with the "pom.xml" file.
- Run the following commands depending on your OS:

On unix like machines:
mvn spring-boot:run -Dspring-boot.run.jvmArguments='-Dserver.port=8081'
On windows command line:
mvn spring-boot:run -D"spring-boot.run.arguments=--server.port=8081"
On windows power shell:
mvn spring-boot:run --% -Dspring-boot.run.arguments="--server.port=8081"

- Open browser and navigate to http://localhost:8081/
- Create an account and log in

- To create a post, type in text with any hashtags and click "Create Post".
- To search up a post, type in the hashtag word in the "Search Hastags" field.
- To view the posts you have made, click on "Profile"
- To view posts that another user has made, click on "People" and the respective user on that page.
- To like or bookmark a post, click on the respective icons on each post
- To view bookmarked posts, click on the "Bookmarks" page.
- To comment on a post, click on the speech bubble icon and type in your comment in the field.
- To logout, click on the "Logout button located next to the "Bookmarks" button.

