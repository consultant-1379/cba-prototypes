
import java.io.IOException;

import javax.servlet.AsyncContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet(urlPatterns={"/TestIn2"}, asyncSupported=true)
public class TestIn2 extends HttpServlet {

    private static final long serialVersionUID = 1L;

    public TestIn2() {
        super();
    }

    protected void doGet(HttpServletRequest doGet, HttpServletResponse response) throws ServletException, IOException {

        String cnt = doGet.getParameter("cnt");
        String time = doGet.getParameter("time");
        int counter = 1;
        int timeI = 0;

        System.out.println("primio sam cnt..> " + cnt);
        System.out.println("primio sam time> " + time);
        if (null != cnt) {
            try {
                counter = Integer.parseInt(cnt);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        if (null != time) { //time in minutes
            try {
                timeI = Integer.parseInt(time);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        
        final AsyncContext acontext = doGet.startAsync();
        final ThreadInReal tr = new ThreadInReal(acontext, counter, timeI);
        acontext.start(tr);
    }
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {
        doGet(request, response);
    }

}