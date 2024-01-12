package com.solucionespc.pagos.utils;

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
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.SimpleDoc;
import javax.print.attribute.HashPrintRequestAttributeSet;

import javax.print.attribute.PrintRequestAttributeSet;

import com.solucionespc.pagos.dto.InfoRecibo;
import com.solucionespc.pagos.dto.MesesRecibo;

import br.com.adilson.util.Extenso;
import br.com.adilson.util.PrinterMatrix;

public class PrintTicket {
	
	
	public static void printTicket(InfoRecibo pago, List<MesesRecibo> meses) { 
		 PrinterMatrix printer = new PrinterMatrix();
		 
		 int numCol = 12 + meses.size()+2;
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
	        
//		 printer.printTextWrap(9, 10, 5, 16, "01/11/2024");
//		 printer.printTextWrap(9, 10, 20, 32, "$240.00");
//		 
//		 printer.printTextWrap(10, 11, 5, 16, "01/11/2024");
//		 printer.printTextWrap(10, 11, 20, 32, "$240.00");
		 
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
	 public static void main(String... args) throws IOException, GeneralSecurityException {
		 
		 String nombreImpresora = "Canon TS3100 series";
	        FileInputStream archivo = null;
	        try {
	            archivo = new FileInputStream("recibo.pdf");
	        } catch (FileNotFoundException e) {
	            e.printStackTrace();
	        }
	        if (archivo == null) {
	            return;
	        }
	        DocFlavor formato = DocFlavor.INPUT_STREAM.AUTOSENSE;
	        Doc documento = new SimpleDoc(archivo, formato, null);
	        PrintService[] impresoras = PrintServiceLookup.lookupPrintServices(formato, null);
	        PrintService impresora = null;
	        for (PrintService p : impresoras) {
	            if (p.getName().equals(nombreImpresora)) {
	                impresora = p;
	                break;
	            }
	        }
	        if (impresora != null) {
	            DocPrintJob trabajoImpresion = impresora.createPrintJob();
	            try {
	                trabajoImpresion.print(documento, null);
	            } catch (Exception e) {
	                System.err.println("Error: " + e.toString());
	            }
	        } else {
	            System.err.println("No se encontró la impresora.");
	        }
	    }
		 
	 
}
