package controlador;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import logica.ConfiguracionServicio;
import modelo.Configuracion;

@WebServlet("/api/config")
public class ConfigApiServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Content-Type", "application/json");
        
        try {
            ConfiguracionServicio servicio = new ConfiguracionServicio();
            Configuracion config = servicio.obtenerConfiguracion();
            
            JsonObject json = new JsonObject();
            json.addProperty("ip_servidor", config != null ? config.getIpServidor() : "localhost");
            
            PrintWriter out = response.getWriter();
            out.print(new Gson().toJson(json));
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}