package my.library.classes.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import my.library.classes.javabean.User;
import my.library.classes.service.UserService;
import my.library.classes.util.ConString;

import java.io.IOException;
import java.util.UUID;

@WebServlet(name = "PersonalInfoServlet", urlPatterns = "/personal" + "/upload")
@MultipartConfig()
public class PersonalInfoServlet extends HttpServlet {

    private UserService userService = new UserService();

    @Override
    protected void doGet(HttpServletRequest req,HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req,HttpServletResponse resp) throws ServletException, IOException {
        User user = new User(
                req.getParameter("username"),
                req.getParameter("nickname"),
                req.getParameter("sex"),
                req.getParameter("cellphone"),
                req.getParameter("email"),
                req.getParameter("remarks"));

        Part part = req.getPart("avatar");
        if (part.getSize() > 0) {
            String fileName = part.getSubmittedFileName();
            String[] fileNames = fileName.split("\\.");
            String uuid = UUID.randomUUID().toString();
            String file =
                    uuid + "." + fileNames[fileNames.length - 1];
            part.write(ConString.HEADER_FILE_DIR + file);
            user.setHeader("/header/" + file);
        }

        String message = userService.uploadUserInfo(user,
                req.getSession());
        req.setAttribute("fresh", true);
        req.getRequestDispatcher("/personalInfo.jsp?message=" + message).forward(req, resp);

    }
}