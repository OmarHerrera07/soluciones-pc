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

public class PDFCorteExporter {
	
	private final List<Corte> infoCorteEfectivo;
	
	private final List<Corte> infoCorteTransferencia;
	
	private final Usuario usuario;
	
	public PDFCorteExporter(List<Corte> infoCorteEfectivo,List<Corte> infoCorteTransferencia,Usuario usuario) {
		this.infoCorteEfectivo = infoCorteEfectivo;
		this.infoCorteTransferencia = infoCorteTransferencia;
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
        asunto.setSpacingAfter(20f);
        document.add(asunto);
        
        Paragraph efectivo = new Paragraph("Efectivo: ", boldFont);
        efectivo.setAlignment(Element.ALIGN_LEFT);
        efectivo.setSpacingAfter(40f);
        document.add(efectivo);


        Font fontTableHead = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14, negro);
        PdfPTable tableEfectivo = new PdfPTable(2);

        tableEfectivo.setTotalWidth(PageSize.A4.getWidth()); // Establece el ancho total de la tabla al ancho de la página
        tableEfectivo.setLockedWidth(true);
        tableEfectivo.getDefaultCell().setBorder(Rectangle.NO_BORDER); // El
        
        PdfPCell cell1;


        cell1 = new PdfPCell(new Phrase("Cliente",fontTableHead));
        cell1.setBorder(Rectangle.NO_BORDER);
        cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
        tableEfectivo.addCell(cell1);

        cell1 = new PdfPCell(new Phrase("Pago",fontTableHead));
        cell1.setBorder(Rectangle.NO_BORDER);
        cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
        tableEfectivo.addCell(cell1);
        Font fontTableBody = FontFactory.getFont(FontFactory.HELVETICA, 14, negro);
        Integer paddig = 8;
        Float totalEfectivo = 0f;
        
        for (Corte corte : infoCorteEfectivo) {
        	cell1 = new PdfPCell(new Phrase(corte.getNombreCliente(),fontTableBody));
        	cell1.setBorder(Rectangle.NO_BORDER);
        	cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
        	cell1.setPadding(paddig);
        	tableEfectivo.addCell(cell1);

            cell1 = new PdfPCell(new Phrase("$"+Float.toString(corte.getTotalPago())+"0",fontTableBody));
            cell1.setBorder(Rectangle.NO_BORDER);
            cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell1.setPadding(paddig);
            tableEfectivo.addCell(cell1);
            totalEfectivo += corte.getTotalPago();

        }
        
        document.add(tableEfectivo);
	    Font totalFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14, negro);
	    Paragraph total = new Paragraph("Total: $"+totalEfectivo+"0", totalFont);
	    total.setAlignment(Element.ALIGN_RIGHT);
	    total.setSpacingAfter(15f);
	    document.add(total);
	    
        Paragraph linea = new Paragraph("------------------------------------------------------------------------------------------------ ", fontTableBody);
        linea.setAlignment(Element.ALIGN_CENTER);
        linea.setSpacingAfter(40f);
        document.add(linea);
	    
        Paragraph transferencia = new Paragraph("Transferencia: ", boldFont);
        transferencia.setAlignment(Element.ALIGN_LEFT);
        transferencia.setSpacingAfter(40f);
        document.add(transferencia);
        
        
        PdfPTable tableTransferencia = new PdfPTable(2);

        tableTransferencia.setTotalWidth(PageSize.A4.getWidth()); // Establece el ancho total de la tabla al ancho de la página
        tableTransferencia.setLockedWidth(true);
        tableTransferencia.getDefaultCell().setBorder(Rectangle.NO_BORDER); // El
        
        PdfPCell cell2;


        cell2 = new PdfPCell(new Phrase("Cliente",fontTableHead));
        cell2.setBorder(Rectangle.NO_BORDER);
        cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
        tableTransferencia.addCell(cell2);

        cell2 = new PdfPCell(new Phrase("Pago",fontTableHead));
        cell2.setBorder(Rectangle.NO_BORDER);
        cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
        tableTransferencia.addCell(cell2);
        Float CorteTransferencia = 0f;
        
        for (Corte corte : infoCorteTransferencia) {
        	cell2 = new PdfPCell(new Phrase(corte.getNombreCliente(),fontTableBody));
        	cell2.setBorder(Rectangle.NO_BORDER);
        	cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
        	cell2.setPadding(paddig);
        	tableTransferencia.addCell(cell2);

        	cell2 = new PdfPCell(new Phrase("$"+Float.toString(corte.getTotalPago())+"0",fontTableBody));
        	cell2.setBorder(Rectangle.NO_BORDER);
        	cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell2.setPadding(paddig);
            tableTransferencia.addCell(cell2);
            CorteTransferencia += corte.getTotalPago();

        }
        
        document.add(tableTransferencia);
	    Paragraph totalTransferencia = new Paragraph("Total: $"+CorteTransferencia+"0", totalFont);
	    totalTransferencia.setAlignment(Element.ALIGN_RIGHT);
	    totalTransferencia.setSpacingAfter(30f);
	    document.add(totalTransferencia);
	    
	    document.add(linea);
	    Float totalFinalprint = CorteTransferencia+totalEfectivo;
	    Paragraph totalFinal = new Paragraph("Total: $"+totalFinalprint+"0", totalFont);
	    totalFinal.setAlignment(Element.ALIGN_RIGHT);
	    totalFinal.setSpacingAfter(15f);
	    document.add(totalFinal);
        
	    
	    Paragraph caja = new Paragraph("Caja: "+usuario.getNombre(), totalFont);
	    caja.setAlignment(Element.ALIGN_LEFT);
	    caja.setSpacingAfter(10f);
	    document.add(caja);	  

        document.close();
    }
	

}
