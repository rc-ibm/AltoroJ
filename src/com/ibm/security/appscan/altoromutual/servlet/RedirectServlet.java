public class RedirectServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final String BASE_URL = "/";
    
    public RedirectServlet() {
        super();
    }
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String url = request.getServletPath().toString();
        if (url.endsWith(".aspx")) {
            url = url.substring(0, url.lastIndexOf(".aspx")) + ".jsp";
        }
        
        // Validate the URL to prevent unvalidated URL forwarding
        if (isValidUrl(url)) {
            RequestDispatcher dispatcher = request.getRequestDispatcher(url);
            dispatcher.forward(request, response);
        } else {
            // Handle invalid URL, e.g., return an error page
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Invalid URL");
        }
    }
    
    private boolean isValidUrl(String url) {
        // Define a list of allowed URLs or patterns
        String[] allowedUrls = {
            "/login.jsp",
            "/home.jsp",
            "/about.jsp"
        };
        
        // Check if the URL matches any of the allowed URLs
        for (String allowedUrl : allowedUrls) {
            if (url.equals(allowedUrl)) {
                return true;
            }
        }
        
        // If the URL does not match any of the allowed URLs, return false
        return false;
    }
}