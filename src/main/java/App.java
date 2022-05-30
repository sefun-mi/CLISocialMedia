import Entities.*;
import Dao.*;
import java.util.Scanner;
import java.util.List;


public class App
{
    private static final int OPTION_REGISTER = 1;
    private static final int OPTION_LOGIN  = 2;
    private static final int OPTION_EXIT   = 3;



    private static final int OPTION_CREATE_POST   = 1;
    private static final int OPTION_VIEW_All_POSTS= 2;
    private static final int OPTION_COMMENT_ON_POST  = 3;
    public static final int OPTION_VIEW_ALL_COMMENT_IN_POST =4;
    private static final int OPTION_SIGN_OUT  = 5;

    private static boolean isUserLoggedIn=false;
    private static User loggedInUser;

    private static final Scanner scanner;


    static {

        scanner = new Scanner(System.in);
        scanner.useDelimiter("\n");

        try{
            UserDao.getAllUsers();
        }catch(Exception e){
            System.out.println("CONNECTION ERROR");
        }
    }
    public static void main( String[] args )
    {
        boolean quitter=false;
        while(!quitter)
        {
            System.out.println("------------------");
            int option = homeOptions();
            System.out.println();

            if (option == OPTION_EXIT)
            {
                quitter = true;
                System.out.println("App Exited");
            }
            else if (option == OPTION_REGISTER)
            {
                registerUser();
            }
            else if (option == OPTION_LOGIN)
            {
                userLogin();
            }
            else
            {       System.out.println("INVALID ENTRY");

            }

        }
    }

    private static int homeOptions() {
        int option;

        while (true) {

            System.out.printf("%d -> Sign Up%n", OPTION_REGISTER);
            System.out.printf("%d -> Sign in%n", OPTION_LOGIN);
            System.out.printf("%d -> Quit%n", OPTION_EXIT);
            System.out.print("Select an option to continue: ");

            String input = scanner.next();

            try {
                option = Integer.parseInt(input);
                break;
            } catch (Exception e) {
                System.out.println(input + " is not a valid entry (i.e integer)");
            }

            System.out.println();
        }

        return option;
    }


    private static void registerUser() {
        String username = "";
        boolean userExist=true;

        while (userExist) {
            System.out.print("Choose a username: ");
            username = scanner.next().trim();

            if (UserDao.isUserExists(username)) {
                System.out.println("Username is taken");

            }
            else{
                userExist=false;
            }
        }

        System.out.print("Choose a password: ");
        String password = scanner.next().trim();

        System.out.print("confirm password: ");
        String confirmPassword=scanner.next();

        User user ;
        if(password.equals(confirmPassword))
        {
            user=new User(username,password);
            if (UserDao.addUser(user))
                System.out.println("User Registered");
            else
                System.out.println("Registration Error");
        }
        else
        {
            System.out.println("Passwords not the same");
            registerUser();
        }
    }


    public static void userLogin()
    {
        String username;
        String password;
        System.out.print("Enter your username: ");
        username= scanner.next().trim();
        System.out.print("Enter your password: ");
        password=scanner.next();

        User user=UserDao.getUser(username);

        if(user==null || !password.equals(user.getPassword()))
        {
            System.out.println("Invalid Username Or Password");
        }
        else
        {
            System.out.println("Login Successful");
            loggedInUser=user;
            isUserLoggedIn=true;
            processesInLoggedInState();
        }
    }

    public static void processesInLoggedInState()
    {
        while (isUserLoggedIn) {
            System.out.println();
            int option = loggedInHomeOption();
            System.out.println();

            if (option == OPTION_CREATE_POST) {
                createPost();
            }
            else if (option == OPTION_VIEW_All_POSTS) {
                allPosts();
            }
            else if (option == OPTION_COMMENT_ON_POST) {
                allPosts();
                commentOnPost();
            }
            else if (option == OPTION_VIEW_ALL_COMMENT_IN_POST) {
                allPosts();
                viewAllCommentInPost();
            }

            else if (option == OPTION_SIGN_OUT) {
                System.out.println("You're now signed out!");
                isUserLoggedIn = false;
            }
            else
                System.out.println("Please choose a valid option");
        }
    }


    public static int loggedInHomeOption()
    {
        int option;

        while (true) {

            System.out.printf("%d -> Create Post%n", OPTION_CREATE_POST);
            System.out.printf("%d -> View All Posts%n",OPTION_VIEW_All_POSTS );
            System.out.printf("%d -> Comment On Post%n", OPTION_COMMENT_ON_POST);
            System.out.printf("%d -> View All Comments In A Post%n", OPTION_VIEW_ALL_COMMENT_IN_POST);
            System.out.printf("%d -> Log out%n", OPTION_SIGN_OUT);

            System.out.print("Please choose one to continue: ");
            String input = scanner.next();

            try {
                option = Integer.parseInt(input);
                break;
            } catch (Exception e) {
                System.out.println(input + " is not a valid entry (i.e integer)");
            }

            System.out.println();
        }

        return option;
    }

    private static void createPost()
    {
        String title;
        String content;

        System.out.print("Enter the Title of your Post: ");
        title=scanner.next().trim();

        System.out.print("Enter the post contents: ");
        content = scanner.next().trim();

        Post post = new Post(loggedInUser, title, content);

        if (PostDao.addPost(post)) {
            System.out.println("Post created successfully!");
        }
        else {
            System.out.println("Failed to create post.");
        }
    }

    private static void allPosts()
    {
        List<Post> posts=PostDao.getAllPosts();
        for (Post p :
                posts) {
            System.out.println(p.toString());
        }
    }


    private static void commentOnPost()
    {
        String postTitle;
        String content;
        System.out.print("Enter the title of the post: ");
        postTitle= scanner.next().trim();

        Post post = PostDao.getPost(postTitle);

        if (post == null)
            System.out.println("Post not found!");

        else
        {

            System.out.print("Enter the content of your post");
            content= scanner.next();

            Comment comment=new Comment(loggedInUser,post,content);
            if(CommentDao.addComment(comment))
            {
                System.out.println("Comment Successful");
            }
            else
            {
                System.out.println("Can't Create Comment");
            }
        }
    }

    private static void viewAllCommentInPost() {
        String postTitle;
        System.out.print("Enter the title of the post: ");
        postTitle= scanner.next().trim();

        Post post = PostDao.getPost(postTitle);

        if (post == null)
            System.out.println("Post not found!");

        else {
            List<Comment> comments=CommentDao.getAllComments(post);
            for (Comment c :
                    comments) {
                System.out.println(c.toString());
            }
        }
    }


}
