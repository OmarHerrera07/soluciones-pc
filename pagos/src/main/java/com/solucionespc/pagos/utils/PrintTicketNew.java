package com.solucionespc.pagos.utils;

import java.awt.Desktop;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintException;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.SimpleDoc;
import javax.print.attribute.HashPrintRequestAttributeSet;

import javax.print.attribute.PrintRequestAttributeSet;

import com.solucionespc.pagos.dto.InfoRecibo;
import com.solucionespc.pagos.dto.MesesRecibo;

import br.com.adilson.util.Extenso;
import br.com.adilson.util.PrinterMatrix;

public class PrintTicketNew {
	
	public static void printTicket(InfoRecibo pago, List<MesesRecibo> meses,Float abono, Integer TipoTicket) { 
		 PrinterMatrix printer = new PrinterMatrix();
		 
		 int numCol = 12 + meses.size()+2;
		 
		 if(TipoTicket == 3 || TipoTicket == 4 || TipoTicket == 2) {
			 numCol+=2;
		 }
		 
		 
		 String nombre = "Soluciones PC";
		 SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		 
		 Extenso e = new Extenso();
		 
		 e.setNumber(101.85);
		 
		 printer.setOutSize(numCol, 32);
		 
		 printer.printCharAtCol(1, 1, 32, "=");		 
		 printer.printTextWrap(1, 2, 8, 32, nombre);
		 printer.printTextWrap(2, 3, 8, 32, "");
		 printer.printTextWrap(3,4, 15, 32, "Fecha: "+sdf.format(pago.getFecha())); 
		 printer.printTextWrap(4, 5, 8, 32, "");
		 printer.printTextWrap(5,6, 1, 20, "RFC: "+pago.getRfc()); 
		 printer.printTextWrap(6, 7, 8, 32, "");
		 printer.printTextWrap(7, 8, 5, 16, "Mes de Pago");
		 printer.printTextWrap(7, 8, 20, 32, "Subtotal");
		 
		 	     	        
		 int colPagos = 9;
	     for(MesesRecibo mes : meses) {
	    	 String fechaFormateada = sdf.format(mes.getFecha());
			 printer.printTextWrap(colPagos, colPagos+1, 5, 16, fechaFormateada);
			 printer.printTextWrap(colPagos,colPagos+1, 20, 32, "$"+mes.getPrecio()+"0");
			 colPagos+=1;
	    	 
	     }
	        
			/**
			 * Estado 1: ticket normal 
			 * Estado 2: Solo abono
			 * Estado 3: Meses mas abono
			 * Estado 4: Usando el abono x
			 */
	     
	     System.out.println("Imprimiendo ticket");
	     if(TipoTicket == 3 || TipoTicket == 2) {
			 printer.printTextWrap(colPagos, colPagos+1, 5, 16, "abono");
			 printer.printTextWrap(colPagos,colPagos+1, 20, 32, "$"+abono+"0");
			 colPagos+=1;
			 System.out.println("ENTROOOO EN LA CONDICIÓN");
	     }
	     
	     if(TipoTicket == 4) {
			 printer.printTextWrap(colPagos, colPagos+1, 5, 16, "abono");
			 printer.printTextWrap(colPagos,colPagos+1, 18, 32, "- $"+abono+"0");
			 colPagos+=1;
	     }
		 
		 printer.printTextWrap(colPagos, colPagos+1, 8, 32, "");
		 colPagos+=1;
		 printer.printTextWrap(colPagos, colPagos+1, 15, 32, "Total: $"+pago.getTotal()+"0");
		 colPagos+=1;
		 printer.printTextWrap(colPagos, colPagos+1, 8, 32, "");
		 colPagos+=1;
		 printer.printTextWrap(colPagos, colPagos+1, 1, 32, "Cobrado por: ");
		 colPagos+=1;
		 printer.printTextWrap(colPagos, colPagos+1, 1, 32, pago.getNombreUsuario());
		 
		 
		 
		 printer.toFile("ticket.txt");
		 
		 FileInputStream inputStream = null;
		 
		 try {
			 inputStream = new FileInputStream("ticket.txt");
		 }catch(FileNotFoundException ex) {
			 ex.printStackTrace();
		 }
		 
		 if(inputStream == null) {
			 return;
		 }
		 
		 DocFlavor docFormat = DocFlavor.INPUT_STREAM.AUTOSENSE;
		 Doc document = new SimpleDoc(inputStream, docFormat, null);
		 
		 PrintRequestAttributeSet attributeSet = new HashPrintRequestAttributeSet();
		 
		 PrintService defaultPrintService = PrintServiceLookup.lookupDefaultPrintService();
		 
		 if(defaultPrintService !=null) {
			 DocPrintJob printJob = defaultPrintService.createPrintJob();
			 try {
				 printJob.print(document, attributeSet);
			 }catch(Exception e1) {
				 System.err.println("Error: "+ e1.toString());
				 
			 }
		 }else {
			 System.err.println("No hay impresora instalada");
		 }
	}
	
	
    public static void printFile(String filePath) {
        try {
            FileInputStream fileInputStream = new FileInputStream(filePath);
            DocFlavor docFlavor = DocFlavor.INPUT_STREAM.AUTOSENSE;
            Doc doc = new SimpleDoc(fileInputStream, docFlavor, null);

            PrintService[] printServices = PrintServiceLookup.lookupPrintServices(null, null);

            if (printServices.length > 0) {
                PrintService printService = printServices[0]; // Selecciona la primera impresora disponible

                DocPrintJob printJob = printService.createPrintJob();
                printJob.print(doc, null);
            } else {
                System.out.println("No se encontraron impresoras disponibles.");
            }

            fileInputStream.close();
        } catch (IOException | PrintException e) {
            e.printStackTrace();
        }
    }
    
    public static void main(String[] args) {
        String filePath = "ticket.txt";
        printFile(filePath);
    }
		 
	 
}
