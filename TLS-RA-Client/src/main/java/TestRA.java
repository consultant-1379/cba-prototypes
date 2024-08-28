import java.io.IOException;

import javax.servlet.AsyncContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import com.ericsson.oss.mediation.adapter.tls.exception.TlsChannelException;
import com.ericsson.oss.mediation.adapter.tls.exception.TlsException;

@WebServlet(urlPatterns = { "/Test" }, asyncSupported = true)
public class TestRA extends HttpServlet {

	private static final long serialVersionUID = 1L;

	/*
	 *
	 * http://localhost:8080/TLS-RA-Client/Test?operation=connectShort
	 * http://localhost:8080/TLS-RA-Client/Test?operation=connectLong
	 * http://localhost:8080/TLS-RA-Client/Test?operation=disconnect
	 * http://localhost:8080/TLS-RA-Client/Test?operation=status
	 *
	 * http://localhost:8080/TLS-RA-Client/Test?operation=getPattern
	 *
	 * http://localhost:8080/TLS-RA-Client/Test?operation=executeAsync&message=HELLO
	 * http://localhost:8080/TLS-RA-Client/Test?operation=executeAsync&message=GET
	 * http://localhost:8080/TLS-RA-Client/Test?operation=executeAsync&message=CLOSE
	 *
	 * http://localhost:8080/TLS-RA-Client/Test?operation=executeCommand&message=HELLO
	 * http://localhost:8080/TLS-RA-Client/Test?operation=executeCommand&message=GET
	 * http://localhost:8080/TLS-RA-Client/Test?operation=executeCommand&message=CLOSE
	 *
	 * http://localhost:8080/TLS-RA-Client/Test?operation=write&message=HELLO
	 * http://localhost:8080/TLS-RA-Client/Test?operation=write&message=GET
	 * http://localhost:8080/TLS-RA-Client/Test?operation=write&message=CLOSE
	 *
	 * http://localhost:8080/TLS-RA-Client/Test?operation=readint
	 * http://localhost:8080/TLS-RA-Client/Test?operation=read&bytes=1000
	 */

	public TestRA() {
		super();
	}

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		int bytesToRead;
		if(request.getParameter("bytes") != null){
			bytesToRead = Integer.valueOf(request.getParameter("bytes"));
		} else {
			bytesToRead = -1;
		}
		RequestInformation info = new RequestInformation(request.getParameter("operation"), request.getParameter("message"), bytesToRead);
		final AsyncContext acontext = request.startAsync();
		ThreadReal tr = new ThreadReal(acontext, info);
		acontext.start(tr);
	}

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
}