package com.solucionespc.pagos.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.solucionespc.pagos.dto.InfoRecibo;
import com.solucionespc.pagos.dto.MesesRecibo;
import com.solucionespc.pagos.entity.Pago;

public class PDFRecibo {
	private final InfoRecibo pago;
	
	private final List<MesesRecibo> meses;
	
	private Float abono;
	
	private Float abonoRestado;
	
	public PDFRecibo(InfoRecibo pago, List<MesesRecibo> meses,Float abono,Float abonoRestado) {
		this.pago = pago;
		this.meses = meses;
		this.abono = abono;
		this.abonoRestado = abonoRestado;
	}
	
	
	public byte[] getPdfBytes() throws IOException, DocumentException {
		  ByteArrayOutputStream baos = new ByteArrayOutputStream();
		  
		// Convertir las dimensiones de la hoja de la impresora a puntos (1 pulgada = 72 puntos)
		float anchoPuntos = (float) (2.5 * 72); // Asumiendo un ancho de 2.5 pulgadas
		float altoPuntos = (float) (3 * 72); // Asumiendo un alto de 11 pulgadas (típico para recibos)

		// Crear un nuevo tamaño de página con las dimensiones de la impresora
		Rectangle pageSize = new Rectangle(anchoPuntos, altoPuntos);
	    // Tamaño personalizado para la impresora térmica Bixolon (80 mm de ancho, 58 mm de alto)
	    Document document = new Document(pageSize, 10, 10, 10, 10);
	    PdfWriter.getInstance(document, baos);


	    document.open();

	    BaseColor negro = BaseColor.BLACK;
	    Font boldFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, negro);

	    Paragraph title = new Paragraph("Soluciones PC", boldFont);
	    title.setAlignment(Element.ALIGN_CENTER);
	    title.setSpacingAfter(10f);
	    document.add(title);
	    
	    
        // Obtener la fecha actual
        LocalDate fechaActual = LocalDate.now();

        // Definir el formato deseado (MM/dd/yyyy)
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        // Formatear la fecha
        String fechaFormateada = fechaActual.format(formato);
        
        
        
	    Font fontTableHead = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 6, negro);
	 // Crear una tabla con dos columnas
	    PdfPTable tableHead = new PdfPTable(2);
	    tableHead.setWidthPercentage(100); // Hacer que la tabla ocupe todo el ancho de la página

	    // Crear celdas sin bordes
	    PdfPCell cell;

	    // Agregar RFC a la izquierda
	    cell = new PdfPCell(new Phrase("RFC: "+pago.getRfc(),fontTableHead));
	    cell.setBorder(Rectangle.NO_BORDER);
	    cell.setHorizontalAlignment(Element.ALIGN_LEFT);
	    tableHead.addCell(cell);

	    // Agregar fecha a la derecha
	    cell = new PdfPCell(new Phrase("Fecha: "+fechaFormateada,fontTableHead));
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
	    
	    SimpleDateFormat formatoMes = new SimpleDateFormat("MMMM", new Locale("es", "ES"));

        // Obtén el nombre del mes en español
        
	    Font fontTableBody = FontFactory.getFont(FontFactory.HELVETICA, 6, negro);
	    // Agregar filas a la tabla
	    for (MesesRecibo mes : meses) {
	        cell = new PdfPCell(new Phrase("1",fontTableBody));
	        cell.setBorder(Rectangle.NO_BORDER);
	        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	        table.addCell(cell);
	        
	        String nombreMes = formatoMes.format(mes.getFecha());
	        cell = new PdfPCell(new Phrase("Pago del mes "+nombreMes,fontTableBody));
	        cell.setBorder(Rectangle.NO_BORDER);
	        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	        table.addCell(cell);

	        cell = new PdfPCell(new Phrase("$"+mes.getPrecio(),fontTableBody));
	        cell.setBorder(Rectangle.NO_BORDER);
	        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	        table.addCell(cell);
	    }
	    // Agregar la tabla al documento
	    System.out.println("ESTE ES EL ABONo");
	    System.out.println(abono);
	    if(abono == null) {
	    	abono = 0f;
	    }
	    if(abono > 0) {
	        cell = new PdfPCell(new Phrase("1",fontTableBody));
	        cell.setBorder(Rectangle.NO_BORDER);
	        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	        table.addCell(cell);
	        
	        cell = new PdfPCell(new Phrase("abono",fontTableBody));
	        cell.setBorder(Rectangle.NO_BORDER);
	        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	        table.addCell(cell);

	        cell = new PdfPCell(new Phrase("$"+abono,fontTableBody));
	        cell.setBorder(Rectangle.NO_BORDER);
	        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	        table.addCell(cell);
	    }
	    table.setSpacingAfter(15f);
	    document.add(table);
	    Font totalFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 8, negro);
	    if(abonoRestado>0) {
		    
		    Paragraph restoAbono = new Paragraph("abono: $"+abonoRestado, totalFont);
		    restoAbono.setAlignment(Element.ALIGN_RIGHT);
		    restoAbono.setSpacingAfter(15f);
		    document.add(restoAbono);
	    }
	    	    	    	    	    	    	    	    

	    Paragraph total = new Paragraph("Total: $"+pago.getTotal(), totalFont);
	    total.setAlignment(Element.ALIGN_RIGHT);
	    total.setSpacingAfter(15f);
	    document.add(total);
	    
	    Font cajaFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 6, negro);

	    Paragraph caja = new Paragraph("Cobrado por: "+pago.getNombreUsuario(), cajaFont);
	    caja.setAlignment(Element.ALIGN_LEFT);
	    caja.setSpacingAfter(10f);
	    document.add(caja);	    
	    document.close();
	    return baos.toByteArray();

	}
}
