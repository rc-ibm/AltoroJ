package com.ibm.security.appscan.altoromutual.servlet;

import java.io.IOException;
import java.util.Arrays;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * This servlet allows this application to use 'static' pages from
 * the .NET version of the application 'AS IS' by converting requests
 * for .aspx files into requests for .jsp files with the same name
 * Servlet implementation class RedirectServlet
 * 
 * @author Alexei
 */
public class RedirectServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    // Define a whitelist of allowed URLs
    private static final String[] ALLOWED_URLS = {
        "/login.jsp",
        "/home.jsp",
        "/about.jsp"
    };

    /**
     * @see HttpServlet#HttpServlet()
     */
    public RedirectServlet() {
        super();
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String url = request.getServletPath().toString();
        if (url.endsWith(".aspx")) {
            url = url.substring(0, url.lastIndexOf(".aspx")) + ".jsp";
        }

        // Check if the URL is in the whitelist
        if (isAllowedUrl(url)) {
            RequestDispatcher dispatcher = request.getRequestDispatcher(url);
            dispatcher.forward(request, response);
        } else {
            // Handle the case where the URL is not allowed
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "URL not found");
        }
    }

    // Method to check if a URL is in the whitelist
    private boolean isAllowedUrl(String url) {
        return Arrays.stream(ALLOWED_URLS).anyMatch(allowedUrl -> allowedUrl.equals(url));
    }
}