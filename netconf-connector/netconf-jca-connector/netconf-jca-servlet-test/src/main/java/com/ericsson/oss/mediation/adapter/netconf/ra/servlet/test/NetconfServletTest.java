/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ericsson.oss.mediation.adapter.netconf.ra.servlet.test;

import com.ericsson.oss.mediation.adapter.netconf.jca.api.NetconfConnection;
import com.ericsson.oss.mediation.adapter.netconf.jca.api.NetconfConnectionFactory;
import com.ericsson.oss.mediation.util.netconf.api.NetconManagerConstants;
import com.ericsson.oss.mediation.util.netconf.api.exception.NetconfManagerException;
import com.ericsson.oss.mediation.util.transport.api.TransportManager;
import com.ericsson.oss.mediation.util.transport.api.TransportManagerCI;
import com.ericsson.oss.mediation.util.transport.api.TransportSessionType;
import com.ericsson.oss.mediation.util.transport.api.factory.TransportFactory;
import com.ericsson.oss.mediation.util.transport.provider.TransportProviderImpl;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.resource.ResourceException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 
 * @author xvaltda
 */
@WebServlet(name = "NetconfServletTest", urlPatterns = { "/netconfTest" })
public class NetconfServletTest extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     * 
     * @param request
     *            servlet request
     * @param response
     *            servlet response
     * @throws ServletException
     *             if a servlet-specific error occurs
     * @throws IOException
     *             if an I/O error occurs
     */

    private NetconfConnectionFactory netconfConnectionFactory;

    private NetconfConnection netconfConnection;

    private TransportManagerCI transportManagerCI;

    protected void processRequest(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        transportManagerCI = new TransportManagerCI();
        transportManagerCI.setHostname("192.168.100.208");
        transportManagerCI.setPassword("sgsn123");
        transportManagerCI.setUsername("borusgsn");
        transportManagerCI.setPort(22);
        transportManagerCI.setSocketConnectionTimeoutInMillis(20000);
        transportManagerCI.setSessionType(TransportSessionType.SUBSYSTEM);
        transportManagerCI.setSessionTypeValue("netconf");

        Map<String, Object> configProperties = new HashMap<String, Object>();

        List<String> list = new ArrayList<String>();
        list.add("urn:ietf:params:xml:ns:netconf:base:1.0");

        list.add("urn:ietf:params:xml:ns:netconf:base:1.1");
        configProperties.put(NetconManagerConstants.CAPABILITIES_KEY, list);

        TransportFactory transportFactory = TransportProviderImpl
                .getFactory("SSH");
        TransportManager sshTransportManager = transportFactory
                .createTransportManager(transportManagerCI);

        try {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet NetconfServletTest</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet NetconfServletTest at "
                    + request.getContextPath() + "</h1>");

            InitialContext initialContext = new InitialContext();
            netconfConnectionFactory = (NetconfConnectionFactory) initialContext
                    .lookup("java:/eis/netconfresource");

            netconfConnection = netconfConnectionFactory
                    .createNetconfConnection(sshTransportManager,
                            configProperties);

            System.out.println("Trying to connect with the node");
            netconfConnection.connect();

            System.out.println("Trying to get data from the node");
            System.out.println(netconfConnection.get().getData());

            out.println("</body>");
            out.println("</html>");
        } catch (ResourceException ex) {
            Logger.getLogger(NetconfServletTest.class.getName()).log(
                    Level.SEVERE, null, ex);
        } catch (NetconfManagerException ex) {
            Logger.getLogger(NetconfServletTest.class.getName()).log(
                    Level.SEVERE, null, ex);
        } catch (NamingException ex) {
            Logger.getLogger(NetconfServletTest.class.getName()).log(
                    Level.SEVERE, null, ex);
        } finally {
            out.close();
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     * 
     * @param request
     *            servlet request
     * @param response
     *            servlet response
     * @throws ServletException
     *             if a servlet-specific error occurs
     * @throws IOException
     *             if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     * 
     * @param request
     *            servlet request
     * @param response
     *            servlet response
     * @throws ServletException
     *             if a servlet-specific error occurs
     * @throws IOException
     *             if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     * 
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
