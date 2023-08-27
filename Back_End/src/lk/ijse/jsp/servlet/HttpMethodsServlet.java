package lk.ijse.jsp.servlet;

import javax.json.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(urlPatterns = "/pages/test")
public class HttpMethodsServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        JsonReader reader = Json.createReader(req.getReader());
        JsonObject jsonObject = reader.readObject();
        String c_id = jsonObject.getString("id");
        String c_name = jsonObject.getString("name");
        String c_address = jsonObject.getString("address");
        String c_salary = jsonObject.getString("salary");


//        System.out.println("DO GET "+id+" "+name+" "+address+" "+salary);
        System.out.println("DO GET "+c_id+" "+c_name+" "+c_address+" "+c_salary);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
      /*  String id = req.getParameter("id");
        String name = req.getParameter("name");
        String address = req.getParameter("address");
        String salary = req.getParameter("salary");

        System.out.println("DO POST "+id+" "+name+" "+address+" "+salary);*/
      /*  JsonReader reader = Json.createReader(req.getReader());
        JsonArray jsonValues = reader.readArray();

        for (JsonValue jsonValue : jsonValues) {
            JsonObject jsonObject = jsonValue.asJsonObject();
            String id = jsonObject.getString("id");
            String name = jsonObject.getString("name");
            String address = jsonObject.getString("address");
            String salary = jsonObject.getString("salary");
            System.out.println(id+ ":"+ name +" "+address+" "+salary);
        }*/
        JsonReader reader = Json.createReader(req.getReader());
        JsonArray jsonValues = reader.readArray();

        for (JsonValue  jsonValue: jsonValues){
            JsonObject jsonObject = jsonValue.asJsonObject();

            String oid = jsonObject.getString("oid");
            String date = jsonObject.getString("date");
            JsonArray orderDetails = jsonObject.getJsonArray("orderDetails");
            System.out.println(oid + "" + date);
            for (JsonValue jValue : orderDetails){
                JsonObject jsonObject1 = jValue.asJsonObject();

                String code = jsonObject1.getString("code");

                System.out.println(code);
            }

        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String id = req.getParameter("id");
        String name = req.getParameter("name");
        String address = req.getParameter("address");
        String salary = req.getParameter("salary");

        System.out.println("DO PUT "+id+" "+name+" "+address+" "+salary);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String id = req.getParameter("id");
        String name = req.getParameter("name");
        String address = req.getParameter("address");
        String salary = req.getParameter("salary");

        System.out.println("DO DELETE "+id+" "+name+" "+address+" "+salary);
    }


}
