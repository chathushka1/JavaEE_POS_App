package lk.ijse.jsp.servlet;

import javax.json.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.*;

@WebServlet(urlPatterns = {"/pages/customer"})
public class CustomerServletAPI extends HttpServlet {


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            /*<!--when the response received catch it and set it to the table-->*/

            Class.forName("com.mysql.jdbc.Driver");
        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/company", "root", "1234");
       /* PreparedStatement pstm = connection.prepareStatement("select * from customer");
        ResultSet rst = pstm.executeQuery();
        resp.addHeader("Access-Control-Allow-Origin","*");*/
        String option = req.getParameter("option");
       /* String JSON = "[";
        while (rst.next()) {
            String customer="{";
            String id = rst.getString(1);
            String name = rst.getString(2);
            String address = rst.getString(3);
            customer+="\"id\":\""+id+"\",";
            customer+="\"name\":\""+name+"\",";
            customer+="\"address\":\""+address+"\"";
            customer+="}";
            JSON+=customer+",";
        }
        JSON=JSON.substring(0,JSON.length()-1);
        JSON+="]";
        resp.setContentType("application/json");
        resp.getWriter().print(JSON);
*/
            switch (option) {
                case "getAll":
                    PreparedStatement pstm = connection.prepareStatement("select * from customer");
                    ResultSet rst = pstm.executeQuery();
                    resp.addHeader("Content-Type", "application/json");
                    resp.addHeader("Access-Control-Allow-Origin", "*");
                    JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();

                    while (rst.next()) {
                        String id = rst.getString(1);
                        String name = rst.getString(2);
                        String address = rst.getString(3);

                        JsonObjectBuilder customerObject = Json.createObjectBuilder();
                        customerObject.add("id", id);
                        customerObject.add("name", name);
                        customerObject.add("address", address);
                        arrayBuilder.add(customerObject.build());

                    }

                    resp.setContentType("application/json");
                    JsonObjectBuilder responseObj = Json.createObjectBuilder();

                    resp.getWriter().print(arrayBuilder.build());
                    responseObj.add("Status", "ok");
                    responseObj.add("message", "Successfully Loaded...!");
                    responseObj.add("data", arrayBuilder.build());
                    resp.getWriter().print(responseObj.build());
                    break;

                case "search":
                    String cusId = req.getParameter("cusId");
                    PreparedStatement pstm1 = connection.prepareStatement("SELECT * FROM customer WHERE id = ?");
                    pstm1.setString(1, cusId);
                    ResultSet rst1 = pstm1.executeQuery();
                    JsonArrayBuilder jsonArrayBuilder1 = Json.createArrayBuilder();
                    while (rst1.next()) {
                        JsonObjectBuilder itemObject = Json.createObjectBuilder();
                        itemObject.add("id", rst1.getString("id"));
                        itemObject.add("name", rst1.getString("name"));
                        itemObject.add("address", rst1.getString("address"));

                        jsonArrayBuilder1.add(itemObject);
                    }

                    resp.setContentType("application/json");
                    JsonObjectBuilder responseObj1 = Json.createObjectBuilder();
                    responseObj1.add("Status", "ok");
                    responseObj1.add("message", "Successfully Loaded...!");
                    responseObj1.add("data", jsonArrayBuilder1.build());
                    resp.getWriter().print(responseObj1.build());
                    break;





           /* resp.setContentType("text/html");
            resp.getWriter().println("<table border='1'>");
            resp.getWriter().println("<tr><th>Customer ID</th><th>Customer Name</th><th>Customer Address</th></tr>");
            while (rst.next()) {
                String id = rst.getString(1);
                String name = rst.getString(2);
                String address = rst.getString(3);
                resp.getWriter().println("<tr><td>" + id + "</td><td>" + name + "</td><td>" + address + "</td></tr>");
            }
            resp.getWriter().println("</table>");*/


            /*ArrayList<CustomerDTO> allCustomers = new ArrayList<>();

            while (rst.next()) {
                String id = rst.getString(1);
                String name = rst.getString(2);
                String address = rst.getString(3);
                allCustomers.add(new CustomerDTO(id, name, address));
            }

            req.setAttribute("keyOne", allCustomers);

            req.getRequestDispatcher("customer.html").forward(req, resp);


*/
            }
    } catch (ClassNotFoundException e) {
        throw new RuntimeException(e);
    } catch (SQLException e) {
        throw new RuntimeException(e);
    }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String cusID = req.getParameter("cusID");
        String cusName = req.getParameter("cusName");
        String cusAddress = req.getParameter("cusAddress");
        String cusSalary = req.getParameter("cusSalary");
        String option = req.getParameter("option");
        resp.addHeader("Access-Control-Allow-Origin","*");

        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/company", "root", "1234");

            /*switch (option) {
                case "add":*/
                    PreparedStatement pstm = connection.prepareStatement("insert into customer values(?,?,?)");
                    pstm.setObject(1, cusID);
                    pstm.setObject(2, cusName);
                    pstm.setObject(3, cusAddress);
                  //  pstm.setObject(4, cusSalary);
                    resp.addHeader("content-type","application/json");


                    if (pstm.executeUpdate() > 0) {
                        //resp.getWriter().println("Customer Added..!");

                        JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
                        objectBuilder.add("state", "Ok");
                        objectBuilder.add("message", "Successfully Added...!");
                        objectBuilder.add("data", "");
                        resp.getWriter().print(objectBuilder.build());
                    }
             /*       break;
                case "delete":
                    PreparedStatement pstm2 = connection.prepareStatement("delete from Customer where id=?");
                    pstm2.setObject(1, cusID);
                    resp.addHeader("Access-Control-Allow-Origin","*");

                    resp.addHeader("content-type","application/json");
                    if (pstm2.executeUpdate() > 0) {
                        //resp.getWriter().println("Customer Deleted..!");

                        JsonObjectBuilder deleteObject = Json.createObjectBuilder();
                        deleteObject.add("state", "Ok");
                        deleteObject.add("message", "Successfully deleted...!");
                        deleteObject.add("data", "");
                        resp.getWriter().print(deleteObject.build());
                    }
                    break;


            }*/


          //  resp.sendRedirect("/jsonp/pages/customer");

        } catch (ClassNotFoundException e) {
           /* JsonObjectBuilder response = Json.createObjectBuilder();
            response.add("state", "Error");
            response.add("message", e.getMessage());
            response.add("data", "");
            resp.getWriter().print(response.build());
            resp.sendError(400);*/
             new RuntimeException(e);
        } catch (SQLException e) {
            JsonObjectBuilder response = Json.createObjectBuilder();
            response.add("state", "Error");
            response.add("message", e.getMessage());
            response.add("data", "");
            resp.getWriter().print(response.build());
            //resp.sendError(400);
            //throw new RuntimeException(e);
        }
    }
        @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.addHeader("Access-Control-Allow-Origin", "*");
        resp.addHeader("Content-Type", "application/json");
        String cusID = req.getParameter("id");


        try {
            Class.forName("com.mysql.jdbc.Driver");

            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/AssCompany", "root", "1234");
            PreparedStatement pstm2 = connection.prepareStatement("delete from customer where id=?");
            pstm2.setObject(1, cusID);
            if (pstm2.executeUpdate() > 0) {
                JsonObjectBuilder responseObj = Json.createObjectBuilder();
                responseObj.add("Status","ok");
                responseObj.add("message","Successfully Deleted...!");
                responseObj.add("data","");
                resp.setStatus(HttpServletResponse.SC_OK);
                resp.getWriter().print(responseObj.build());

            }
        } catch (ClassNotFoundException | SQLException e) {
            JsonObjectBuilder error = Json.createObjectBuilder();
            error.add("Status","Error");
            error.add("message",e.getLocalizedMessage());
            error.add("data","");
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().print(error.build());

        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        JsonReader reader = Json.createReader(req.getReader());
        JsonObject jsonObject = reader.readObject();
        String cusID = jsonObject.getString("cusID");
        String cusName = jsonObject.getString("cusName");
        String cusAddress = jsonObject.getString("cusAddress");
        String salary = jsonObject.getString("cusSalary");
        resp.addHeader("Access-Control-Allow-Origin","*");

        try {
            Class.forName("com.mysql.jdbc.Driver");

            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/company", "root", "sanu1234");

            PreparedStatement pstm3 = connection.prepareStatement("update Customer set name=?,address=? where id=?");
            pstm3.setObject(3, cusID);
            pstm3.setObject(1, cusName);
            pstm3.setObject(2, cusAddress);
            if (pstm3.executeUpdate() > 0) {
                resp.getWriter().println("Customer Updated..!");
            }
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    protected void doOptions(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.addHeader("Access-Control-Allow-Origin", "*");
        resp.addHeader("Access-Control-Allow-Methods","PUT,DELETE");
        resp.addHeader("Access-Control-Allow-Headers","content-type");

    }
}
