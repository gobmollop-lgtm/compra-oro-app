package controlador;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;
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

    private static final String UPLOAD_DIR = "images" + File.separator + "compras";

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
                : "$";

            FondoServicio fondoServicio = new FondoServicio();
            BigDecimal saldoDisponible = fondoServicio.obtenerSaldoDisponible(usuarioId);

            if (total.compareTo(saldoDisponible) > 0) {
                String mensajeError = "Saldo insuficiente. Saldo disponible: " + simboloMoneda + saldoDisponible.setScale(2, RoundingMode.HALF_UP);
                request.setAttribute("error", mensajeError);
                request.getRequestDispatcher("registrar-compra.jsp").forward(request, response);
                return;
            }

            String rutaFoto = null;
            Part filePart = request.getPart("fotoCompra");
            if (filePart != null && filePart.getSize() > 0) {
                String fileName = filePart.getSubmittedFileName();
                if (fileName != null && !fileName.isEmpty()) {
                    String ext = fileName.substring(fileName.lastIndexOf(".")).toLowerCase();
                    if (ext.equals(".jpg") || ext.equals(".jpeg") || ext.equals(".png")) {
                        String uniqueName = "compra_" + UUID.randomUUID().toString() + ext;

                        // ✅ Usar getRealPath() con verificación
                        String appPath = request.getServletContext().getRealPath("");
                        if (appPath == null) {
                            // Fallback: usar la carpeta temporal del sistema
                            appPath = System.getProperty("java.io.tmpdir");
                        }
                        String savePath = appPath + File.separator + "images" + File.separator + "compras";
                        File dir = new File(savePath);
                        if (!dir.exists()) dir.mkdirs();

                        Path filePath = Paths.get(savePath, uniqueName);
                        Files.copy(filePart.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
                        rutaFoto = "images/compras/" + uniqueName;
                    }
                }
            }

Compra compra = new Compra();
compra.setClienteId(clienteId);
compra.setUsuarioId(usuarioId);
compra.setPesoGramos(peso);
compra.setKilate(kilate);
compra.setPunto(punto);
compra.setRutaFoto(rutaFoto);
compra.setPrecioGramo(precioGramo);
compra.setTotal(total);
// >>> AÑADIR ESTA LÍNEA <<<
compra.setEstado("Pendiente"); // Estado por defecto

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
    
}