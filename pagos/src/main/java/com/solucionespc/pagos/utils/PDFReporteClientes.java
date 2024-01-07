package com.solucionespc.pagos.utils;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import com.solucionespc.pagos.dto.ReporteCliente;
import com.solucionespc.pagos.entity.Pago;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;

public class PDFReporteClientes {

    private final ReporteCliente reporte;

    public PDFReporteClientes(ReporteCliente reporte) {
        this.reporte = reporte;
    }


    public void export(HttpServletResponse response) throws DocumentException, IOException {
        Document document = new Document(PageSize.LETTER, 80, 80, 50, 50);
        PdfWriter.getInstance(document, response.getOutputStream());

        document.open();

        BaseColor negro = BaseColor.BLACK;
        // Crear una fuente en negrita
        Font boldFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, negro);

        // Agregar título en negrita
        Paragraph title = new Paragraph("Reporte de clientes", boldFont);
        title.setAlignment(Element.ALIGN_CENTER);
        title.setSpacingAfter(30f);
        document.add(title);



        document.close();
    }
}
