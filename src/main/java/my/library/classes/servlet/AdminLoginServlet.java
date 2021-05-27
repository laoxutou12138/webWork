package my.library.classes.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import my.library.classes.service.UserService;

import java.io.IOException;
import java.net.URLEncoder;

@WebServlet(name = "AdminLoginServlet", urlPatterns = "/admin/login")
public class AdminLoginServlet extends HttpServlet {
    private UserService userService = new UserService();

    @Override
    protected void doGet(HttpServletRequest req,HttpServletResponse resp) throws ServletException, IOException {
        this.doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req,HttpServletResponse resp) throws ServletException, IOException {
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        String result = userService.adminLogin(username, password,
                req.getSession());
        if ("1".equals(result)) {
            resp.sendRedirect("/admin/main.jsp");
        } else {
            req.getRequestDispatcher("/index.jsp?message=" + URLEncoder.encode(result, "utf-8")).forward(req, resp);
        }
    }
}