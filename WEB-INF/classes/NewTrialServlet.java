import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;
import java.util.Vector;
import javax.servlet.annotation.WebServlet;

@WebServlet("/CreateTrial")
public class NewTrialServlet extends HttpServlet{

	public void init(ServletConfig config){
		try{
			super.init(config);
		}
		catch(Exception e){
			e.printStackTrace();
		}	
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response){

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

			//------Trial creation STARTS------

			//retrieve values from register's forms
			String address = request.getParameter("addAddress");
			String date = request.getParameter("addDate");

			//save values in the database
			int res = stat.executeUpdate("insert into file(address, date) VALUES (\"" + address + "\", \"" + date + "\");");

			//!!!!!!---------   DEBUGGING - For class presentation only - Creating a JSP with all registered users example !!!!!!---------

			ResultSet res2 = stat.executeQuery("SELECT * FROM file;");
			Vector<Trial> trialList = new Vector<Trial>();

			while(res2.next()){
				//Corregir formato de la fecha para impresión en el jsp
				String reformatDate = res2.getString("creation_date");
				//2018-mm-dd -> dd-mm-2018
				String day = reformatDate.substring(8);
				String month = reformatDate.substring(5,7);
				String year =  reformatDate.substring(0,4);
				reformatDate = day + "/" + month + "/" + year;
				Trial aux = new Trial(res2.getString("name"), reformatDate);
				trialList.add(aux);
			}

			stat.close();
			con.close();

			request.setAttribute("trialList",trialList);
			RequestDispatcher disp =  getServletContext().getRequestDispatcher("/showRegisteredTrial.jsp");

			if(disp!=null){
				disp.forward(request,response);
			}
		}

		catch(Exception e){
			e.printStackTrace();
		}
	}
}