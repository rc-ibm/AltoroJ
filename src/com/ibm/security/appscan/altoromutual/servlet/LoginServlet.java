import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.nio.charset.StandardCharsets;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.ibm.security.appscan.Log4AltoroJ;
import com.ibm.security.appscan.altoromutual.util.DBUtil;
import com.ibm.security.appscan.altoromutual.util.ServletUtil;

public class LoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Log out
        try {
            HttpSession session = request.getSession(false);
            session.removeAttribute(ServletUtil.SESSION_ATTR_USER);
        } catch (Exception e) {
            // Do nothing
        } finally {
            response.sendRedirect("index.jsp");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Log in
        // Create session if there isn't one:
        HttpSession session = request.getSession(true);

        String username = null;

        try {
            username = request.getParameter("uid");
            if (username != null)
                username = username.trim().toLowerCase();

            String password = request.getParameter("passw");
            password = password.trim().toLowerCase(); // In real life, the password is usually case sensitive and this cast would not be done

            if (!DBUtil.isValidUser(username, password)) {
                Log4AltoroJ.getInstance().logError("Login failed >>> User: " + username + " >>> Password: " + password);
                throw new Exception("Login Failed: We're sorry, but this username or password was not found in our system. Please try again.");
            }
        } catch (Exception ex) {
            request.getSession(true).setAttribute("loginError", ex.getLocalizedMessage());
            response.sendRedirect("login.jsp");
            return;
        }

        // Handle the cookie using ServletUtil.establishSession(String)
        try {
            // Generate a secure token or identifier
            String token = generateSecureToken(username);

            // Create a cookie with the secure token
            Cookie accountCookie = new Cookie("accountToken", token);
            accountCookie.setSecure(true);
            accountCookie.setHttpOnly(true);
            response.addCookie(accountCookie);

            response.sendRedirect(request.getContextPath() + "/bank/main.jsp");
        } catch (Exception ex) {
            ex.printStackTrace();
            response.sendError(500);
        }

        return;
    }

    private String generateSecureToken(String username) {
        try {
            // Use a secure hash algorithm like SHA-256 or SHA-512
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.reset();

            // Add a salt value to prevent rainbow table attacks
            String salt = "ThisIsMySalt";
            String input = salt + username;

            // Hash the input string
            byte[] hashedBytes = messageDigest.digest(input.getBytes(StandardCharsets.UTF_8));

            // Convert the hashed bytes to a hexadecimal string
            StringBuilder token = new StringBuilder();
            for (byte b : hashedBytes) {
                token.append(String.format("%02x", b));
            }

            return token.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}