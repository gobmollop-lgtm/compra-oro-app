package controlador;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;

@WebServlet("/ExportarComprasExcelServlet")
public class ExportarComprasExcelServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String tablaHTML = request.getParameter("tablaHTML");
        String nombreUsuario = request.getParameter("nombreUsuario");

        if (tablaHTML == null || tablaHTML.trim().isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Tabla vacía");
            return;
        }

        if (nombreUsuario == null || nombreUsuario.trim().isEmpty()) {
            nombreUsuario = "Usuario";
        }

        Document doc = Jsoup.parse(tablaHTML);
        Elements filas = doc.select("tr");

        if (filas.isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "No hay datos en la tabla");
            return;
        }

        // Determinar el número de columnas (máximo de la primera fila que tenga datos)
        int numColumnas = 0;
        for (Element fila : filas) {
            int cols = fila.select("th, td").size();
            if (cols > numColumnas) {
                numColumnas = cols;
            }
        }

        if (numColumnas == 0) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Tabla sin columnas");
            return;
        }

        XSSFWorkbook wb = new XSSFWorkbook();
        Sheet sheet = wb.createSheet("Compras de " + nombreUsuario);

        // === Estilo para título del comprador ===
        XSSFFont titleFont = wb.createFont();
        titleFont.setBold(true);
        titleFont.setFontHeightInPoints((short) 14);

        XSSFCellStyle titleStyle = wb.createCellStyle();
        titleStyle.setFont(titleFont);
        titleStyle.setFillForegroundColor(IndexedColors.LIGHT_BLUE.getIndex());
        titleStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        titleStyle.setBorderBottom(BorderStyle.THIN);
        titleStyle.setBorderTop(BorderStyle.THIN);
        titleStyle.setBorderLeft(BorderStyle.THIN);
        titleStyle.setBorderRight(BorderStyle.THIN);

        // === Estilo para encabezado de tabla ===
        XSSFFont headerFont = wb.createFont();
        headerFont.setBold(true);
        headerFont.setColor(IndexedColors.WHITE.getIndex());

        XSSFCellStyle headerStyle = wb.createCellStyle();
        headerStyle.setFont(headerFont);
        headerStyle.setFillForegroundColor(IndexedColors.DARK_BLUE.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        headerStyle.setBorderBottom(BorderStyle.THIN);
        headerStyle.setBorderTop(BorderStyle.THIN);
        headerStyle.setBorderLeft(BorderStyle.THIN);
        headerStyle.setBorderRight(BorderStyle.THIN);

        // === Estilo para celdas de datos ===
        XSSFCellStyle cellStyle = wb.createCellStyle();
        cellStyle.setBorderBottom(BorderStyle.THIN);
        cellStyle.setBorderTop(BorderStyle.THIN);
        cellStyle.setBorderLeft(BorderStyle.THIN);
        cellStyle.setBorderRight(BorderStyle.THIN);

        // === Línea 1: Título del comprador ===
        Row titleRow = sheet.createRow(0);
        Cell titleCell = titleRow.createCell(0);
        titleCell.setCellValue("Comprador: " + nombreUsuario);
        titleCell.setCellStyle(titleStyle);

        // Fusionar celdas del título según el número real de columnas
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, numColumnas - 1));

        // === Línea 2: Espacio vacío (opcional) ===
        sheet.createRow(1);

        // === Línea 3 en adelante: Tabla de datos ===
        int filaNum = 2;
        for (Element fila : filas) {
            Row row = sheet.createRow(filaNum++);
            Elements columnas = fila.select("th, td");
            for (int colNum = 0; colNum < columnas.size(); colNum++) {
                Cell cell = row.createCell(colNum);
                cell.setCellValue(columnas.get(colNum).text());
                if (columnas.get(colNum).tagName().equalsIgnoreCase("th")) {
                    cell.setCellStyle(headerStyle);
                } else {
                    cell.setCellStyle(cellStyle);
                }
            }
        }

        // Ajustar ancho de columnas dinámicamente
        for (int i = 0; i < numColumnas; i++) {
            sheet.setColumnWidth(i, 4000);
        }

        // Nombre del archivo
        String fileName = "compras_" + nombreUsuario.replaceAll("[^a-zA-Z0-9_\\s]", "_") + ".xlsx";
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");

        wb.write(response.getOutputStream());
        wb.close();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        resp.sendRedirect("compras-por-usuario.jsp");
    }
}