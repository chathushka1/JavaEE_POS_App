package lk.ijse.jsp.servlet;

import lk.ijse.jsp.dto.ItemDTO;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;

@WebServlet(urlPatterns = "/pages/item")
public class ItemServletAPI extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/company", "root", "1234");
           /* PreparedStatement pstm = connection.prepareStatement("select * from item");
            ResultSet result = pstm.executeQuery();
            JsonArrayBuilder arrayBuilderItem = Json.createArrayBuilder();*/
            String option = req.getParameter("option");

            switch (option) {
                case "getAll":
                    PreparedStatement pstm = connection.prepareStatement("select * from item");
                    ResultSet result = pstm.executeQuery();
                    resp.addHeader("Content-Type", "application/json");
                    resp.addHeader("Access-Control-Allow-Origin", "*");
                    JsonArrayBuilder arrayBuilderItem = Json.createArrayBuilder();


                    while (result.next()) {
                        String code = result.getString(1);
                        String description = result.getString(2);
                        String qtyOnHand = result.getString(3);
                        String unitPrice = result.getString(4);

                        JsonObjectBuilder itemObject = Json.createObjectBuilder();
                        itemObject.add("code", code);
                        itemObject.add("description", description);
                        itemObject.add("qtyOnHand", qtyOnHand);
                        itemObject.add("unitPrice", unitPrice);
                        arrayBuilderItem.add(itemObject.build());

                    }

                    resp.setContentType("application/json");
                    resp.getWriter().print(arrayBuilderItem.build());
                    JsonObjectBuilder responseObj = Json.createObjectBuilder();
                    responseObj.add("Status", "ok");
                    responseObj.add("message", "Successfully Loaded...!");
                    responseObj.add("data", arrayBuilderItem.build());

                    break;
                case "search":
                    String itemCode = req.getParameter("code");
                    if (itemCode != null && !itemCode.isEmpty()) {
                        PreparedStatement pstm1 = connection.prepareStatement("SELECT * FROM item WHERE code = ?");
                        pstm1.setString(1, itemCode);
                        ResultSet rst1 = pstm1.executeQuery();
                        JsonArrayBuilder jsonArrayBuilder1 = Json.createArrayBuilder();
                        while (rst1.next()) {
                            JsonObjectBuilder itemObject = Json.createObjectBuilder();
                            itemObject.add("code", rst1.getString("code"));
                            itemObject.add("description", rst1.getString("name"));
                            itemObject.add("qty", rst1.getDouble("qty"));
                            itemObject.add("price", rst1.getDouble("price"));

                            jsonArrayBuilder1.add(itemObject.build());
                        }
                        resp.setContentType("application/json");
                        JsonObjectBuilder responseObj1 = Json.createObjectBuilder();
                        responseObj1.add("Status", "ok");
                        responseObj1.add("message", "Successfully Loaded...!");
                        responseObj1.add("data", jsonArrayBuilder1.build());
                        resp.getWriter().print(responseObj1.build());


                    }
                    break;
                case "loadCode":
                    PreparedStatement pstm2 = connection.prepareStatement("SELECT code FROM item");
                    try (ResultSet rst2= pstm2.executeQuery()) {
                        JsonArrayBuilder jsonArrayBuilder2 = Json.createArrayBuilder();
                        while (rst2.next()) {
                            String id = rst2.getString("code");

                            JsonObjectBuilder itemObject = Json.createObjectBuilder();
                            itemObject.add("code", id);

                            jsonArrayBuilder2.add(itemObject);
                        }

                        resp.setContentType("application/json");
                        JsonObjectBuilder responseObj1 = Json.createObjectBuilder();
                        responseObj1.add("Status", "ok");
                        responseObj1.add("message", "Successfully Loaded...!");
                        responseObj1.add("data", jsonArrayBuilder2.build());
                        resp.getWriter().print(responseObj1.build());
                    }

            }

            } catch(ClassNotFoundException e){
                throw new RuntimeException(e);
            } catch(SQLException e){
            JsonObjectBuilder error = Json.createObjectBuilder();
            error.add("Status", "Error");
            error.add("message", e.getLocalizedMessage());
            error.add("data", "");
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().print(error.build());

        }

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String code = req.getParameter("code");
        String itemName = req.getParameter("description");
        String qtyOnHand = req.getParameter("qtyOnHand");
        String unitPrice = req.getParameter("unitPrice");
        String option = req.getParameter("option");
//
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/company", "root", "1234");
            switch (option) {
                case "add":
                    PreparedStatement pstm = connection.prepareStatement("insert into item values(?,?,?,?)");
                    pstm.setObject(1, code);
                    pstm.setObject(2, itemName);
                    pstm.setObject(3, qtyOnHand);
                    pstm.setObject(4, unitPrice);
                    resp.addHeader("content-type","application/json");
                    if (pstm.executeUpdate() > 0) {
                        //resp.getWriter().println("Item Added..!");
                        //resp.sendRedirect("item");
                        JsonObjectBuilder objectItemBuilder = Json.createObjectBuilder();
                        objectItemBuilder.add("state", "Ok");
                        objectItemBuilder.add("message", "Successfully Added...!");
                        objectItemBuilder.add("data", "");
                        resp.getWriter().print(objectItemBuilder.build());
                    }
                    break;
                case "delete":
                    PreparedStatement pstm2 = connection.prepareStatement("delete from item where code=?");
                    pstm2.setObject(1, code);
                    resp.addHeader("content-type","application/json");
                    if (pstm2.executeUpdate() > 0) {
                        //resp.getWriter().println("Item Deleted..!");
                        JsonObjectBuilder deleteObject = Json.createObjectBuilder();
                        deleteObject.add("state", "Ok");
                        deleteObject.add("message", "Successfully deleted...!");
                        deleteObject.add("data", "");
                        resp.getWriter().print(deleteObject.build());
                        //resp.sendRedirect("/jsonp/pages/item.html");
                    }
                    break;
                case "update":
                    PreparedStatement pstm3 = connection.prepareStatement("update Item set description=?,qtyOnHand=?,unitPrice=? where code=?");
                    pstm3.setObject(1, itemName);
                    pstm3.setObject(2, qtyOnHand);
                    pstm3.setObject(3, unitPrice);
                    pstm3.setObject(4, code);
                    if (pstm3.executeUpdate() > 0) {
                        resp.getWriter().println("Item Updated..!");
                        resp.sendRedirect("/jsonp/pages/item.html");
                    }
                    break;
            }
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            JsonObjectBuilder response = Json.createObjectBuilder();
            response.add("state", "Error");
            response.add("message", e.getMessage());
            response.add("data", "");
            resp.getWriter().print(response.build());
            //throw new RuntimeException(e);
        }
    }
}
