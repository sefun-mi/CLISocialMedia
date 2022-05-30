import Dao.*;
import Entities.*;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class AppTest
{

    @Test
    public void testUserAdded() {
        //Add users to the system
        UserDao.addUser(new User("bebs", "bebs"));
        UserDao.addUser(new User("dbd", "dbd"));

        //retrieve all users in the db
        List<User> users = UserDao.getAllUsers();

        //assert that the db is not empty
        assertNotNull(users);

        //Check the number of users are correct
        assertEquals(2, users.size());
    }

    @Test
    public void testAddPost() {
        UserDao.addUser(new User("bebs", "bebs"));

        User user = UserDao.getUser("bebs");
        assertNotNull(user);

        //Create and Add post
        Post post = new Post(user, "Java errors and stackoverflow", "Java gives a lot of errors");
        PostDao.addPost(post);

        //retrieve post from db
        List<Post> posts = PostDao.getAllPosts();
        //check db post
        assertNotNull(posts);
        assertEquals(1, posts.size());

    }

    @Test
    public void testAddComment() {
        UserDao.addUser(new User("bebs", "bebs"));

        User user = UserDao.getUser("bebs");
        assertNotNull(user);

        Post post = new Post(user, "Java errors and stackoverflow", "Java gives a lot of errors");
        PostDao.addPost(post);

        List<Post> posts = PostDao.getAllPosts();
        assertNotNull(posts);
        assertEquals(1, posts.size());

        Comment comment = new Comment(user,post,"You are just starting.");
        CommentDao.addComment(comment);
        List<Comment> comments = CommentDao.getAllComments(post);
        assertNotNull(comments);
        assertEquals(1,comments.size());

    }
}