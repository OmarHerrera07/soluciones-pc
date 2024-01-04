package com.solucionespc.pagos.utils;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


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
import com.solucionespc.pagos.entity.Pago;
import jakarta.servlet.http.HttpServletResponse;

public class PDFExporter {
	
	private final Pago pago;
	
	public PDFExporter(Pago pago) {
		this.pago = pago;
	}

	public void export(HttpServletResponse response) throws DocumentException, IOException {
		
		// Convertir las dimensiones de la hoja de la impresora a puntos (1 pulgada = 72 puntos)
		float anchoPuntos = (float) (2.5 * 72); // Asumiendo un ancho de 2.5 pulgadas
		float altoPuntos = (float) (3 * 72); // Asumiendo un alto de 11 pulgadas (típico para recibos)

		// Crear un nuevo tamaño de página con las dimensiones de la impresora
		Rectangle pageSize = new Rectangle(anchoPuntos, altoPuntos);
	    // Tamaño personalizado para la impresora térmica Bixolon (80 mm de ancho, 58 mm de alto)
	    Document document = new Document(pageSize, 10, 10, 10, 10);
	    
	    PdfWriter writer = PdfWriter.getInstance(document, response.getOutputStream());

	    document.open();

	    BaseColor negro = BaseColor.BLACK;
	    Font boldFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, negro);

	    Paragraph title = new Paragraph("Solucione PC", boldFont);
	    title.setAlignment(Element.ALIGN_CENTER);
	    title.setSpacingAfter(10f);
	    document.add(title);

	    Font fontTableHead = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 6, negro);
	 // Crear una tabla con dos columnas
	    PdfPTable tableHead = new PdfPTable(2);
	    tableHead.setWidthPercentage(100); // Hacer que la tabla ocupe todo el ancho de la página

	    // Crear celdas sin bordes
	    PdfPCell cell;

	    // Agregar RFC a la izquierda
	    cell = new PdfPCell(new Phrase("RFC: DJOPFPOFSDN",fontTableHead));
	    cell.setBorder(Rectangle.NO_BORDER);
	    cell.setHorizontalAlignment(Element.ALIGN_LEFT);
	    tableHead.addCell(cell);

	    // Agregar fecha a la derecha
	    cell = new PdfPCell(new Phrase("fecha: 12/23/2020",fontTableHead));
	    cell.setBorder(Rectangle.NO_BORDER);
	    cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
	    tableHead.addCell(cell);

	    // Agregar la tabla al documento
	    document.add(tableHead);

	    
	    
	    
	    document.add(new Paragraph(" "));

	    // Resto de tu código para generar el contenido del PDF

	    // Ahora, ajustamos el tamaño del documento según el contenido
	    document.setPageSize(document.getPageSize());
	 // Crear una tabla con tres columnas
	    PdfPTable table = new PdfPTable(3);
	 // Establecer los anchos de las columnas
	    float[] columnWidths = new float[] {1f, 2f, 1f};
	    table.setWidths(columnWidths);
	    // Crear celdas sin bordes
	    
	    
	    cell = new PdfPCell(new Phrase("Cantidad",fontTableHead));
	    cell.setBorder(Rectangle.NO_BORDER);
	    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	    table.addCell(cell);

	    cell = new PdfPCell(new Phrase("Concepto",fontTableHead));
	    cell.setBorder(Rectangle.NO_BORDER);
	    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	    table.addCell(cell);

	    cell = new PdfPCell(new Phrase("Total",fontTableHead));
	    cell.setBorder(Rectangle.NO_BORDER);
	    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	    table.addCell(cell);
	    
	    
	    Font fontTableBody = FontFactory.getFont(FontFactory.HELVETICA, 6, negro);
	    // Agregar filas a la tabla
	    for (int i = 0; i < 1; i++) {
	        cell = new PdfPCell(new Phrase("1",fontTableBody));
	        cell.setBorder(Rectangle.NO_BORDER);
	        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	        table.addCell(cell);

	        cell = new PdfPCell(new Phrase("Pago del mes de septiembre ",fontTableBody));
	        cell.setBorder(Rectangle.NO_BORDER);
	        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	        table.addCell(cell);

	        cell = new PdfPCell(new Phrase("$290.00 ",fontTableBody));
	        cell.setBorder(Rectangle.NO_BORDER);
	        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	        table.addCell(cell);
	    }
	    // Agregar la tabla al documento
	    table.setSpacingAfter(15f);
	    document.add(table);
	    
	    
	    Font totalFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 8, negro);
	    Paragraph total = new Paragraph("Total: $898.00			", totalFont);
	    total.setAlignment(Element.ALIGN_RIGHT);
	    total.setSpacingAfter(15f);
	    document.add(total);
	    
	    Font cajaFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 6, negro);

	    Paragraph caja = new Paragraph("Cobrado por: Omar Herrera Santos", cajaFont);
	    caja.setAlignment(Element.ALIGN_LEFT);
	    caja.setSpacingAfter(10f);
	    document.add(caja);




	    document.close();
	}



}