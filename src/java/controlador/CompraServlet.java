package controlador;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import logica.CompraServicio;
import modelo.Compra;

@WebServlet("/registrar-compra")
public class CompraServlet extends HttpServlet {
    @Override
    protected void doOptions(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "POST, OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Permitir CORS
        response.setHeader("Access-Control-Allow-Origin", "*");
        
        String contentType = request.getContentType();
        if (contentType != null && contentType.contains("application/json")) {
            procesarJson(request, response);
        } else {
            // Flujo original de JSP
            try {
                int clienteId = Integer.parseInt(request.getParameter("clienteId"));
                BigDecimal peso = new BigDecimal(request.getParameter("peso"));
                BigDecimal kilate = new BigDecimal(request.getParameter("kilate"));
                BigDecimal punto = new BigDecimal(request.getParameter("punto"));
                String observaciones = request.getParameter("observaciones");

                Compra compra = new Compra();
                compra.setClienteId(clienteId);
                compra.setPesoGramos(peso);
                compra.setKilate(kilate);
                compra.setPunto(punto);
                compra.setObservaciones(observaciones);

                CompraServicio servicio = new CompraServicio();
                servicio.registrar(compra);

                response.sendRedirect("lista-compras.jsp?exito=1");
            } catch (Exception e) {
                e.printStackTrace();
                request.setAttribute("error", "Error al registrar compra");
                request.getRequestDispatcher("registrar-compra.jsp").forward(request, response);
            }
        }
    }

    private void procesarJson(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        StringBuilder sb = new StringBuilder();
        String line;
        BufferedReader reader = request.getReader();
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }

        try {
            JsonObject json = JsonParser.parseString(sb.toString()).getAsJsonObject();
            int clienteId = json.get("clienteId").getAsInt();
            BigDecimal peso = new BigDecimal(json.get("peso").getAsString());
            BigDecimal kilate = new BigDecimal(json.get("kilate").getAsString());
            BigDecimal punto = new BigDecimal(json.get("punto").getAsString());
            String observaciones = json.has("observaciones") ? json.get("observaciones").getAsString() : "";

            Compra compra = new Compra();
            compra.setClienteId(clienteId);
            compra.setPesoGramos(peso);
            compra.setKilate(kilate);
            compra.setPunto(punto);
            compra.setObservaciones(observaciones);

            CompraServicio servicio = new CompraServicio();
            servicio.registrar(compra);

            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().write("{\"status\":\"success\"}");
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"status\":\"error\", \"message\":\"" + e.getMessage() + "\"}");
        }
    }
}