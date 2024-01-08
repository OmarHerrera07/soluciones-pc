package com.solucionespc.pagos.utils;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.solucionespc.pagos.dto.Corte;
import com.solucionespc.pagos.entity.Usuario;

import jakarta.servlet.http.HttpServletResponse;

public class PDFCorte {
	
	private final List<Corte> infoCorte;
	
	private final Usuario usuario;
	
	public PDFCorte(List<Corte> infoCorte,Usuario usuario) {
		this.infoCorte = infoCorte;
		this.usuario = usuario;
	}
	
    public void export(HttpServletResponse response) throws DocumentException, IOException {
        Document document = new Document(PageSize.LETTER, 80, 80, 50, 50);
        PdfWriter.getInstance(document, response.getOutputStream());

        document.open();

        BaseColor negro = BaseColor.BLACK;
        // Crear una fuente en negrita
        Font boldFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16, negro);

        // Agregar título en negrita
        Paragraph title = new Paragraph("Corte", boldFont);
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


        Font fontTableHead = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14, negro);
        PdfPTable table = new PdfPTable(2);

        table.setTotalWidth(PageSize.A4.getWidth()); // Establece el ancho total de la tabla al ancho de la página
        table.setLockedWidth(true);
        table.getDefaultCell().setBorder(Rectangle.NO_BORDER); // El
        PdfPCell cell;


        cell = new PdfPCell(new Phrase("Cliente",fontTableHead));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Pago",fontTableHead));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
        Font fontTableBody = FontFactory.getFont(FontFactory.HELVETICA, 14, negro);
        Integer paddig = 8;
        Float totalCorte = 0f;
        
        for (Corte corte : infoCorte) {
            cell = new PdfPCell(new Phrase(corte.getNombreCliente(),fontTableBody));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setPadding(paddig);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("$"+Float.toString(corte.getTotalPago()),fontTableBody));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setPadding(paddig);
            table.addCell(cell);
            totalCorte += corte.getTotalPago();

        }
        
        document.add(table);
	    Font totalFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14, negro);
	    Paragraph total = new Paragraph("Total: $"+totalCorte, totalFont);
	    total.setAlignment(Element.ALIGN_RIGHT);
	    total.setSpacingAfter(15f);
	    document.add(total);
	    
	    Paragraph caja = new Paragraph("Caja: "+usuario.getNombre(), totalFont);
	    caja.setAlignment(Element.ALIGN_LEFT);
	    caja.setSpacingAfter(10f);
	    document.add(caja);	  

        document.close();
    }
	

}
