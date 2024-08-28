
import java.io.IOException;
import java.io.PipedInputStream;

import javax.servlet.AsyncContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import org.xml.sax.SAXException;

@WebServlet(urlPatterns={"/TestRASAX"}, asyncSupported=true)
public class TestProducerConsumer extends HttpServlet {

    private static final long serialVersionUID = 1L;
    /*
     * localhost:8080/TestRAmvn/Test?cnt=3 localhost:8080/TestRAmvn/Test?cnt=3&time=444 time je sleep time
     */

    //    @Resource(mappedName = "java:/eis/SSHConnectionFactory")
    //    private RAConnectionFactory connectionFactory;

    public TestProducerConsumer() {
        super();
    }

    @Override
    protected void doGet(final HttpServletRequest doGet, final HttpServletResponse response) throws ServletException, IOException{

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

        final int pipeSize=100*1024;
        final PipedInputStream pipedInputStream = new PipedInputStream(pipeSize);
        final DelayedInputStream delayedInputStream = new DelayedInputStream(pipedInputStream);

        final AsyncContext acontext = doGet.startAsync();
        final ThreadRealSAX tr = new ThreadRealSAX(acontext, counter, timeI, delayedInputStream);
        try {
            final ThreadParser tp = new ThreadParser(delayedInputStream);
            //            acontext.start(tr);
            //            acontext.start(tp);
            tr.start();
            tp.start();

        } catch (final SAXException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    protected void doPost(final HttpServletRequest request, final HttpServletResponse response) throws ServletException,
    IOException {
        doGet(request, response);
    }

}