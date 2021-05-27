package my.library.classes.servlet;


import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import my.library.classes.service.UserService;

import java.io.IOException;

@WebServlet(name = "LoginServlet", urlPatterns = "/login")
public class UserServlet extends HttpServlet {

    private UserService userService = new UserService();

    @Override
    protected void doGet(HttpServletRequest req,HttpServletResponse resp) throws ServletException, IOException {
        this.doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req,HttpServletResponse resp) throws ServletException, IOException {
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        String result = userService.login(username, password,
                req.getSession());
        if ("1".equals(result)) {
            resp.sendRedirect("/main.jsp");
        } else {
            req.getRequestDispatcher("/index.jsp?message=" + result).forward(req, resp);
        }
    }
}
