package my.library.classes.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import my.library.classes.javabean.User;
import my.library.classes.service.UserService;

import java.io.IOException;
import java.net.URLEncoder;

@WebServlet(name = "RegisterServlet", urlPatterns = "/register")
public class RegisterServlet extends HttpServlet {
    UserService registerService = new UserService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User register = null;

        String result = "注册失败";
        String userName, password, reader, confirmPassword;
        userName = req.getParameter("username");
        password = req.getParameter("password");
        reader = req.getParameter("reader");
        confirmPassword = req.getParameter("repassword");
        if (password != null && password.equals(confirmPassword)) {
            register = new User(userName, password, reader);
            result = registerService.register(register);
            if (result.equals("注册成功")) {
                resp.sendRedirect("/index.jsp?message=" + URLEncoder.encode(result, "utf-8"));
            } else {
                req.getRequestDispatcher("/register.jsp?message=" + result).forward(req, resp);

            }
        } else {
            req.getRequestDispatcher("/register.jsp?message=" +"两次密码不一致").forward(req, resp);
        }
    }
}
