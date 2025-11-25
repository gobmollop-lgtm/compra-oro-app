package controlador;

import java.io.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import logica.CompraServicio;
import logica.ConfiguracionServicio;
import logica.FondoServicio;
import modelo.Compra;
import modelo.Configuracion;

@WebServlet("/registrar-compra")
@MultipartConfig(fileSizeThreshold = 1024 * 1024, // 1 MB
                 maxFileSize = 5 * 1024 * 1024,      // 5 MB
                 maxRequestSize = 10 * 1024 * 1024) // 10 MB
public class CompraServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            HttpSession session = request.getSession();
            Integer usuarioId = (Integer) session.getAttribute("usuarioId");
            if (usuarioId == null) {
                response.sendRedirect("login.jsp");
                return;
            }

            int clienteId = Integer.parseInt(request.getParameter("clienteId"));
            BigDecimal peso = new BigDecimal(request.getParameter("peso"));
            BigDecimal kilate = new BigDecimal(request.getParameter("kilate"));
            BigDecimal punto = new BigDecimal(request.getParameter("punto"));

            BigDecimal precioGramo = kilate.multiply(punto).setScale(2, RoundingMode.HALF_UP);
            BigDecimal total = peso.multiply(precioGramo).setScale(2, RoundingMode.HALF_UP);

            ConfiguracionServicio configServicio = new ConfiguracionServicio();
            Configuracion configuracion = configServicio.obtenerConfiguracion();
            String simboloMoneda = (configuracion != null && configuracion.getMonedaSimbolo() != null) 
                ? configuracion.getMonedaSimbolo() 
                : "C$";

            FondoServicio fondoServicio = new FondoServicio();
            BigDecimal saldoDisponible = fondoServicio.obtenerSaldoDisponible(usuarioId);

            if (total.compareTo(saldoDisponible) > 0) {
                String mensajeError = "Saldo insuficiente. Saldo disponible: " + simboloMoneda + saldoDisponible.setScale(2, RoundingMode.HALF_UP);
                request.setAttribute("error", mensajeError);
                request.getRequestDispatcher("registrar-compra.jsp").forward(request, response);
                return;
            }

            // >>> MODIFICACIÓN MÍNIMA: solo cambiamos cómo se obtiene la imagen <<<
            byte[] imagenBytes = null;
            Part filePart = request.getPart("fotoCompra");
            if (filePart != null && filePart.getSize() > 0) {
                String contentType = filePart.getContentType();
                if (contentType != null && 
                    (contentType.equals("image/jpeg") || 
                     contentType.equals("image/jpg") || 
                     contentType.equals("image/png"))) {
                    
                    // Código compatible con Java 8
                    ByteArrayOutputStream buffer = new ByteArrayOutputStream();
                    InputStream input = filePart.getInputStream();
                    byte[] data = new byte[4096];
                    int nRead;
                    while ((nRead = input.read(data, 0, data.length)) != -1) {
                        buffer.write(data, 0, nRead);
                    }
                    input.close();
                    imagenBytes = buffer.toByteArray();
                }
            }

            Compra compra = new Compra();
            compra.setClienteId(clienteId);
            compra.setUsuarioId(usuarioId);
            compra.setPesoGramos(peso);
            compra.setKilate(kilate);
            compra.setPunto(punto);
            
            compra.setImagen(imagenBytes);  // ✅ Nueva forma: BLOB
            compra.setPrecioGramo(precioGramo);
            compra.setTotal(total);
            compra.setEstado("Pendiente");

            CompraServicio servicio = new CompraServicio();
            servicio.registrar(compra);

            fondoServicio.deducirMonto(usuarioId, total);

            response.sendRedirect("lista-compras.jsp?exito=1");

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Error al registrar compra: " + e.getMessage());
            request.getRequestDispatcher("registrar-compra.jsp").forward(request, response);
        }
    }

    // >>> AÑADIDO: evita el error 405 si alguien accede por GET <<<
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.sendRedirect("registrar-compra.jsp");
    }
}