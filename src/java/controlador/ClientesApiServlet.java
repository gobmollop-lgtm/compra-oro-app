package controlador;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import logica.ClienteServicio;
import modelo.Cliente;

@WebServlet("/api/clientes")
public class ClientesApiServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Content-Type", "application/json");
        
        try {
            ClienteServicio servicio = new ClienteServicio();
            List<Cliente> clientes = servicio.obtenerTodos();
            
            Gson gson = new Gson();
            JsonArray array = new JsonArray();
            for (Cliente c : clientes) {
                JsonObject obj = new JsonObject();
                obj.addProperty("id", c.getId());
                obj.addProperty("nombre", c.getNombre());
                array.add(obj);
            }
            
            PrintWriter out = response.getWriter();
            out.print(array.toString());
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}