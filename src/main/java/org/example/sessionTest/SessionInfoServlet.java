package org.example.sessionTest;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

/**
 * 用于演示 Servlet API 中的 Session 管理机制
 * 具体演示 在 HttpServletTest 项目中：
 */
public class SessionInfoServlet extends HttpServlet {


    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
//        // 简单一点，在页面上输出  "This is the service"
//        response.getOutputStream().print("This is the service");

        // get current session or，if necessary , creat a new one
        // false 就是当 有 session 的时候就拿到那个 session，如果没有就不创建；
        // true 就是当 session 存在就拿存在的session ,没有就 创建一个。
        HttpSession mySession = request.getSession(true);

        // mime type to return is HTML
        response.setContentType("text/html");

        // get a handle to the output stream
        PrintWriter out = response.getWriter();

        // generate HTML document
        out.println("<html>");
        out.println("<head>");
        out.println("<title>Session Info Servlet</title>");
        out.println("</head>");
        out.println("<body>");
        out.println("<h3>Session Information</h3>");
        out.println("NEW Session:"+mySession.isNew());
        out.println("<br/>");
        out.println("Session ID:"+mySession.getId());
        out.println("<br/>");
        out.println("Session Creation Time:"+new Date(mySession.getCreationTime()));
        out.println("<br/>");
        out.println("Session Last Accessed Time:"+new Date(mySession.getLastAccessedTime()));
        out.println("<h3>Request Information</h3>");
        out.println("Session ID from Request:"+request.getRequestedSessionId());
        out.println("<br/>");
        out.println("Session ID via Cookie:"+request.isRequestedSessionIdFromCookie());
        out.println("<br/>");
        out.println("Session ID via rewritten URL:"+request.isRequestedSessionIdFromURL());
        out.println("<br/>");
        out.println("Valid Session ID:"+request.isRequestedSessionIdValid());
        out.println("<br/>");
        out.println("<a href = "+response.encodeURL("SessionIfoServlet"));
        out.println(" />");
    }

}
