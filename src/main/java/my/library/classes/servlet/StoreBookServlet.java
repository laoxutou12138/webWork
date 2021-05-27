package my.library.classes.servlet;

import com.alibaba.fastjson.JSON;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import my.library.classes.service.BookService;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.util.HashMap;

@WebServlet(name = "StoreBookServlet", urlPatterns = "/book/store")
public class StoreBookServlet extends HttpServlet {

    private BookService bookService = new BookService();

    @Override
    protected void doPost(HttpServletRequest req,HttpServletResponse resp) throws ServletException, IOException {
        String paramJson = IOUtils.toString(
                req.getInputStream(), "UTF-8");
        HashMap<String, Object> parseObject =
                JSON.parseObject(paramJson,
                        HashMap.class);
        String username = (String) parseObject.get("user");
        String bookId = (String) parseObject.get("book");
        String message = bookService.storeBook(username, bookId);
        resp.getWriter().print(message);

    }
}