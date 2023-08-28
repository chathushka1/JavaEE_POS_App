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
            PreparedStatement pstm = connection.prepareStatement("select * from item");
            ResultSet result = pstm.executeQuery();
           // resp.addHeader("Content-Type","application/json");
/*
            ArrayList<ItemDTO> allItems = new ArrayList<>();

            while (rst.next()) {
                String code = rst.getString(1);
                String name = rst.getString(2);
                int qtyOnHand = rst.getInt(3);
                double unitPrice = rst.getDouble(4);
                allItems.add(new ItemDTO(code, name, qtyOnHand, unitPrice));
            }

            req.setAttribute("keyTwo", allItems);

            req.getRequestDispatcher("item.html").forward(req, resp);*/
/*


            String JSON = "[";
            while (rst.next()) {
                String item="{";
                String code = rst.getString(1);
                String description = rst.getString(2);
                String qtyOnHand = rst.getString(3);
                String unitPrice = rst.getString(4);
                item+="\"code\":\""+code+"\",";
                item+="\"description\":\""+description+"\",";
                item+="\"qtyOnHand\":\""+qtyOnHand+"\"";
                item+="\"unitPrice\":\""+unitPrice+"\"";
                item+="}";
                JSON+=item+",";
            }
            JSON=JSON.substring(0,JSON.length()-1);
            JSON+="]";
            resp.setContentType("application/json");
            resp.getWriter().print(JSON);

*/

            JsonArrayBuilder arrayBuilderItem = Json.createArrayBuilder();
            while (result.next()){
                String code = result.getString(1);
                String description = result.getString(2);
                String qtyOnHand = result.getString(3);
                String unitPrice = result.getString(4);

                JsonObjectBuilder itemObject= Json.createObjectBuilder();
                itemObject.add("code",code);
                itemObject.add("description",description);
                itemObject.add("qtyOnHand",qtyOnHand);
                itemObject.add("unitPrice",unitPrice);
                arrayBuilderItem.add(itemObject.build());

            }

            resp.setContentType("application/json");
            resp.getWriter().print(arrayBuilderItem.build());








        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
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
