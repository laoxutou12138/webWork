package my.library.classes.filter;


import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;

import java.io.IOException;

@WebFilter(filterName = "AdminFilter", urlPatterns = "/login")
public class AdminFilter implements Filter {
    @Override
    public void doFilter(ServletRequest req,
                         ServletResponse resp,
                         FilterChain chain) throws IOException,
            ServletException {
        String role = req.getParameter("role");
        if ("0".equals(role)) {
            req.getRequestDispatcher("/admin/login").forward(req, resp);
        } else {
            req.getRequestDispatcher("/login").forward(req, resp);
        }
    }
}