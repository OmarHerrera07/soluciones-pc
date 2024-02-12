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
		 int nombrelimit=24;
		 int usuariolimit=20;
		 int colonialimit=24;
		 int centrado = 6;
		 int espacio = 11;
		 int numCol = 15 + meses.size()+2 + espacio;
		 
		 if(TipoTicket == 3 || TipoTicket == 4 || TipoTicket == 2) {
			 numCol+=2;
		 }
		 
		 if(pago.getNombreCliente().length()<24){
			 nombrelimit= pago.getNombreCliente().length();
		 }

		 if(pago.getNombreUsuario().length()<20){
			 usuariolimit= pago.getNombreUsuario().length();
		 }

		if(pago.getColonia().length()<24){
			colonialimit= pago.getColonia().length();
		}


		 String nombre = "Soluciones PC";
		 String rfc = "RFC: ZARE881013I12";
		 String telefono = "Tel. 6161651227";
		 SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		 
		 Extenso e = new Extenso();
		 
		 e.setNumber(101.85);
		 
		 printer.setOutSize(numCol, 42);
		 printer.printCharAtCol(1, 1, 42, "");
		 	 
		 printer.printTextWrap(1, 2,8+centrado, 42, nombre);
		 
		 
		 printer.printTextWrap(2, 3,6+centrado, 42, rfc);
		 printer.printTextWrap(3, 4,7+centrado, 42, telefono);
		 
		 
		 printer.printTextWrap(4, 5, 8, 42, "");
		 printer.printTextWrap(5,6, 15+centrado, 42, "Fecha: "+sdf.format(pago.getFecha())); 
		 printer.printTextWrap(6, 7, 8, 42, "");
		 printer.printTextWrap(7,8, 5, 42, "Cliente: "+pago.getNombreCliente().substring(0, nombrelimit));
		printer.printTextWrap(8,9, 5, 42, "Colonia: "+pago.getColonia().substring(0, colonialimit));
		 printer.printTextWrap(9, 10, 8+centrado, 42, "");
		 printer.printTextWrap(10, 11, 1+centrado, 16+centrado, "Mes de Pago");
		 printer.printTextWrap(10,11, 21+centrado, 42, "Subtotal");
		 
		 	     	        
		 int colPagos = 12;
	     for(MesesRecibo mes : meses) {
	    	 String fechaFormateada = sdf.format(mes.getFecha());
			 printer.printTextWrap(colPagos, colPagos+1, 1+centrado, 26, fechaFormateada);
			 printer.printTextWrap(colPagos,colPagos+1, 22+centrado, 42, "$"+mes.getPrecio()+"0");
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
			 printer.printTextWrap(colPagos, colPagos+1, 5+centrado, 16+centrado, "abono");
			 printer.printTextWrap(colPagos,colPagos+1, 22+centrado, 32+centrado, "$"+abono+"0");
			 colPagos+=1;
			 System.out.println("ENTROOOO EN LA CONDICIÓN");
	     }
	     
	     if(TipoTicket == 4) {
			 printer.printTextWrap(colPagos, colPagos+1, 3+centrado, 16+centrado, "abono");
			 printer.printTextWrap(colPagos,colPagos+1, 20+centrado, 42, "- $"+abono+"0");
			 colPagos+=1;
	     }
		 
		 printer.printTextWrap(colPagos, colPagos+1, 8+centrado, 42, "");
		 colPagos+=1;
		 printer.printTextWrap(colPagos, colPagos+1, 15+centrado, 42, "Total: $"+pago.getTotal()+"0");
		 colPagos+=1;
		 printer.printTextWrap(colPagos, colPagos+1, 8+centrado, 42, "");
		 colPagos+=1;
		 printer.printTextWrap(colPagos, colPagos+1, 5, 42, "Cobrado por: "+pago.getNombreUsuario().substring(0,usuariolimit));
		 colPagos+=1;
		 //printer.printTextWrap(colPagos, colPagos+1, 5, 42, pago.getNombreUsuario());
		 printer.printCharAtCol(colPagos+espacio, 1, 42, "");
		 
		 
		 
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
