
import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

import javax.servlet.AsyncContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet(urlPatterns={"/Test"}, asyncSupported=true)
public class TestRA extends HttpServlet {

    private static final long serialVersionUID = 1L;
    /*
     * localhost:8080/TestRAmvn/Test?cnt=3 localhost:8080/TestRAmvn/Test?cnt=3&time=444 time je sleep time
     */

        //    @Resource(mappedName = "java:/eis/SSHConnectionFactory")
    //    private RAConnectionFactory connectionFactory;

    public TestRA() {
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
        ThreadReal tr = new ThreadReal(acontext, counter, timeI);
        acontext.start(tr);
    }
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {
        doGet(request, response);
    }

}