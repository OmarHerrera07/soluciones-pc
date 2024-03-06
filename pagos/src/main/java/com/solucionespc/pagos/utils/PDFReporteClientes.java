package com.solucionespc.pagos.utils;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.solucionespc.pagos.dto.MesesRecibo;
import com.solucionespc.pagos.dto.ReporteCliente;
import com.solucionespc.pagos.entity.Pago;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class PDFReporteClientes {

    private final List<ReporteCliente> reporte;

    public PDFReporteClientes(List<ReporteCliente> reporte) {
        this.reporte = reporte;
    }


    public void export(HttpServletResponse response) throws DocumentException, IOException {
        Document document = new Document(PageSize.LETTER, 80, 80, 50, 50);
        PdfWriter.getInstance(document, response.getOutputStream());

        document.open();

        BaseColor negro = BaseColor.BLACK;
        // Crear una fuente en negrita
        Font boldFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14, negro);

        // Agregar título en negrita
        Paragraph title = new Paragraph("Reporte de clientes con adeudo", boldFont);
        title.setAlignment(Element.ALIGN_CENTER);
        title.setSpacingAfter(30f);
        document.add(title);

        // Obtener la fecha actual
        LocalDate fechaActual = LocalDate.now();

        // Definir el formato deseado (MM/dd/yyyy)
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        // Formatear la fecha
        String fechaFormateada = fechaActual.format(formato);

        Paragraph asunto = new Paragraph("Fecha: " + fechaFormateada, boldFont);
        asunto.setAlignment(Element.ALIGN_RIGHT);
        asunto.setSpacingAfter(40f);
        document.add(asunto);


        Font fontTableHead = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, negro);
        PdfPTable table = new PdfPTable(5);

        float[] columnWidths = new float[] {2f, 1f, 1f,1f,1.5f};
        table.setWidths(columnWidths);

        table.setTotalWidth(PageSize.A4.getWidth()); // Establece el ancho total de la tabla al ancho de la página
        table.setLockedWidth(true);
        table.getDefaultCell().setBorder(Rectangle.NO_BORDER); // El
        PdfPCell cell;


        cell = new PdfPCell(new Phrase("Nombre",fontTableHead));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Fecha de pago",fontTableHead));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Colonia",fontTableHead));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
        
        cell = new PdfPCell(new Phrase("Teléfono",fontTableHead));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Días De Atraso",fontTableHead));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);



        Font fontTableBody = FontFactory.getFont(FontFactory.HELVETICA, 12, negro);
        DateFormat formatoSalida = new SimpleDateFormat("dd/MM/yyyy");
        Integer paddig = 8;
        for (ReporteCliente r : reporte) {
            cell = new PdfPCell(new Phrase(r.getNombre(),fontTableBody));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setPadding(paddig);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase(formatoSalida.format(r.getFechaPago()),fontTableBody));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setPadding(paddig);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase(r.getColonia(),fontTableBody));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setPadding(paddig);
            table.addCell(cell);
            
            cell = new PdfPCell(new Phrase(r.getTelefono(),fontTableBody));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setPadding(paddig);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase(Integer.toString(r.getDiasAtraso()),fontTableBody));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setPadding(paddig);
            table.addCell(cell);
        }

        document.add(table);

        document.close();
    }
}
