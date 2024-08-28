
import java.io.IOException;

import javax.servlet.AsyncContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet(urlPatterns={"/TestIn"}, asyncSupported=true)
public class TestIn extends HttpServlet {

    private static final long serialVersionUID = 1L;
    /*
     * localhost:8080/TestRAmvn/Test?cnt=3 localhost:8080/TestRAmvn/Test?cnt=3&time=444 time je sleep time
     */

    //    @Resource(mappedName = "java:/eis/SSHConnectionFactory")
    //    private RAConnectionFactory connectionFactory;

    public TestIn() {
        super();
    }

    @Override
    protected void doGet(final HttpServletRequest doGet, final HttpServletResponse response) throws ServletException, IOException {

        final String cnt = doGet.getParameter("cnt");
        final String time = doGet.getParameter("time");
        int counter = 1;
        int timeI = 0;

        System.out.println("primio sam cnt..> " + cnt);
        System.out.println("primio sam time> " + time);
        if (null != cnt) {
            try {
                counter = Integer.parseInt(cnt);
            } catch (final NumberFormatException e) {
                e.printStackTrace();
            }
        }
        if (null != time) { //time in minutes
            try {
                timeI = Integer.parseInt(time);
            } catch (final NumberFormatException e) {
                e.printStackTrace();
            }
        }

        final AsyncContext acontext = doGet.startAsync();
        final ThreadInReal tr = new ThreadInReal(acontext, counter, timeI);
        acontext.start(tr);
    }

    @Override
    protected void doPost(final HttpServletRequest request, final HttpServletResponse response) throws ServletException,
    IOException {
        doGet(request, response);
    }

}