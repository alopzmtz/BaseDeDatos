import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;
import java.util.Vector;
import javax.servlet.annotation.WebServlet;

@WebServlet("/ShowClients")
public class ShowClientServlet extends HttpServlet{

	public void init(ServletConfig config){
		try{
			super.init(config);
		}
		catch(Exception e){
			e.printStackTrace();
		}	
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response){
		updateAdmin(request, response);
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response){
		updateAdmin(request, response);
	}

	public void updateAdmin(HttpServletRequest request, HttpServletResponse response){

		try{

			//------Connection to mySQL setup STARTS-------
			//credentials
			String dbbase = getServletContext().getInitParameter("base");
			String dbusuario = getServletContext().getInitParameter("usuario");
			String dbpassword = getServletContext().getInitParameter("pass");
			Class.forName("com.mysql.jdbc.Driver");

			//general setup - driver setup, connection, statement setup
			String url = "jdbc:mysql://localhost/"+dbbase+"?useSSL=false&allowPublicKeyRetrieval=true";
			Connection con = DriverManager.getConnection(url,dbusuario,dbpassword);
			Statement stat = con.createStatement();

			//------Connection to mySQL setup ENDS----------

			ResultSet clientContents = stat.executeQuery("SELECT * from client join users on id_User=userID;");
			Vector<Client> clientList = new Vector<Client>();
			Vector<String> usernameList = new Vector<String>();

			while(clientContents.next()){
				Client aux = new Client(Long.valueOf(clientContents.getString("CompanyID")), clientContents.getString("name"), clientContents.getString("contact"), Long.valueOf(clientContents.getString("id_User")));
				clientList.add(aux);
				String responsibleUsername = clientContents.getString("username");
				usernameList.add(responsibleUsername);
			}


			request.setAttribute("clientList",clientList);
			request.setAttribute("usernameList",usernameList);

			RequestDispatcher disp = getServletContext().getRequestDispatcher("/verCliente.jsp");
			if(disp!=null){
				disp.forward(request,response);
			}

			stat.close();
			con.close();

		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
}

