package de.dhbw_mannheim.tit09a.tcom.mediencenter.server.controller_deprecated;

//import java.io.BufferedInputStream;
//import java.io.IOException;
//import java.nio.file.FileSystemException;
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.nio.file.StandardOpenOption;
//
//import javax.servlet.ServletException;
//import javax.servlet.ServletOutputStream;
//import javax.servlet.annotation.WebServlet;
//import javax.servlet.http.HttpServlet;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
//import org.apache.catalina.connector.ClientAbortException;
//
//import de.dhbw_mannheim.tit09a.tcom.mediencenter.server.NFileManager;
//import de.dhbw_mannheim.tit09a.tcom.mediencenter.server.NFileManager.FileType;
//import de.dhbw_mannheim.tit09a.tcom.mediencenter.server.util.NIOUtil;
//import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.interfaces.Server;
//import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.interfaces.Session;
//import de.root1.simon.Lookup;
//import de.root1.simon.Simon;

/**
 * Servlet implementation class StreamServlet
 */
//@WebServlet("/StreamServlet")
public class  StreamServlet //extends HttpServlet
{
	/**
	 * @throws Exception
	 * @see HttpServlet#HttpServlet()
	 */
	public StreamServlet() throws Exception
	{
		super();

	}
//
//	/**
//	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
//	 */
//	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
//	{
//		ServletOutputStream stream = null;
//		BufferedInputStream buf = null;
//		try
//		{
//			// Get parameters
//			String sessionId = request.getParameter("sessionid");
//			String path = request.getParameter("path");
//
//			// Check not null
//			if (sessionId == null || path == null)
//			{
//				response.sendError(HttpServletResponse.SC_BAD_REQUEST, "You have to add parameter 'sessionid' and 'path'!");
//				return;
//			}
//
//			// Get session for 'sessionid'
//			Lookup nameLookup = Simon.createNameLookup(Server.IP, Server.REGISTRY_PORT);
//			Server server = (Server) nameLookup.lookup(Server.BIND_NAME);
//			Session session = server.getSession(sessionId);
//			if (session == null)
//			{
//				response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Session ID invalid. No session found for the id:" + sessionId);
//				return;
//			}
//
//			// Get absolute server path for 'path'
//			NFileManager fileMan = NFileManager.getInstance();
//			Path absSvrPath = null;
//			try
//			{
//				absSvrPath = fileMan.toValidatedAbsoluteServerPath(session, path, FileType.FILE, false);
//			}
//			catch (FileSystemException e)
//			{
//				response.sendError(HttpServletResponse.SC_FORBIDDEN, e.toString());
//				return;
//			}
//
//			stream = response.getOutputStream();
//
//			// set response contentType and headers
//			response.setContentType(NIOUtil.probeContentType(absSvrPath));
//			response.addHeader("Content-Disposition", "attachment; filename=" + absSvrPath.getFileName().toString());
//
//			long length = NIOUtil.sizePerFile(absSvrPath);
//			response.setContentLength(length > Integer.MAX_VALUE ? Integer.MAX_VALUE : (int) length);
//
//			buf = new BufferedInputStream(Files.newInputStream(absSvrPath, StandardOpenOption.READ));
//			int readBytes = 0;
//			// read from the file; write to the ServletOutputStream
//			while ((readBytes = buf.read()) != -1)
//				stream.write(readBytes);
//
//		}
//		catch (Exception e)
//		{
//			if (!(e instanceof ClientAbortException))
//				e.printStackTrace();
//			else
//				System.out.println(e);
//		}
//		finally
//		{
//			if (stream != null)
//				stream.close();
//			if (buf != null)
//				buf.close();
//		}
//
//	}
//
//	/**
//	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
//	 */
//	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
//	{
//		// TODO Auto-generated method stub
//	}

}
