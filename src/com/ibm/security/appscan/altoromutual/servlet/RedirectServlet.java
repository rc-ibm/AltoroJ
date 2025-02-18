package com.ibm.security.appscan.altoromutual.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
    private static final List<String> VALID_URLS = new ArrayList<>();

    static {
        // Initialize the list of valid URLs
        VALID_URLS.add("/home.jsp");
        VALID_URLS.add("/about.jsp");
        // Add more valid URLs as needed
    }

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
        
        // Check if the requested URL is in the list of valid URLs
        if (isValidUrl(url)) {
            RequestDispatcher dispatcher = request.getRequestDispatcher(url);
            dispatcher.forward(request, response);
        } else {
            // Handle invalid URLs, e.g., by sending an error page
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Invalid URL");
        }
    }

    private boolean isValidUrl(String url) {
        return VALID_URLS.contains(url);
    }
}