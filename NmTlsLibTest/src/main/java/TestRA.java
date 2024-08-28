import java.io.IOException;

import javax.servlet.AsyncContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet(urlPatterns = { "/Test" }, asyncSupported = true)
public class TestRA extends HttpServlet {

	private static final long serialVersionUID = 1L;

	/*
	 * 
	 * http://localhost:8080/TestNetconfOverTLS/Test?ip=192.168.100.242&operation=connect
	 * http://localhost:8080/TestNetconfOverTLS/Test?ip=192.168.100.242&operation=disconnect
	 */

	public TestRA() {
		super();
	}

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String operation = request.getParameter("operation");
		String ipAddress = request.getParameter("ip");
		final AsyncContext acontext = request.startAsync();
		NetconfThread tr = new NetconfThread(acontext, operation, ipAddress);
		acontext.start(tr);
	}

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}