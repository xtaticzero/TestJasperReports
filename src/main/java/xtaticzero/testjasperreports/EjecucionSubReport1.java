package xtaticzero.testjasperreports;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRMapCollectionDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;

/**
 * Hello world!
 *
 */
public class EjecucionSubReport1 {

	public static void main(String[] args) {
		System.out.println("Test JasperReports");

		String reportPath;
		String compiledReportPath;
		
		reportPath			= "/resportsDesigns/subReport1.jrxml";
			
		InputStream inputStream = null;
		
		JRDataSource dataSource = null;
		System.out.println("Ok, JRDataSource created");
            
		try{
			
			inputStream = EjecucionSubReport1.class.getResourceAsStream(reportPath);
			
			/*
			           Caso de leer un result set y llenar un mapa, 
					   para crear un JRMapCollectionDataSource. 
			*/		   
			Collection<Map<String,?>> colData = new ArrayList<Map<String,?>>();
            for(int numReg=0;numReg<10;numReg++){
				Map<String,Object> regMap = new HashMap<String, Object>();
				
				regMap.put("id", new Integer(numReg*100));
				regMap.put("descripcion", "campo_2_reg"+numReg);
				regMap.put("campo3", "campo_1_reg"+numReg);
				
				colData.add(regMap);
			}
			
			dataSource = new JRMapCollectionDataSource(colData);
			
			Map parameters = new HashMap();
            
			JasperReport jasperReport = null;
			/* Caso 1) cargamos el jrxml y lo compilamnos : lento */
			
			JasperDesign jasperDesign = JRXmlLoader.load(inputStream);			
			jasperReport = JasperCompileManager.compileReport(jasperDesign);
			
			
			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);
            
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			
			/*   Salida a un flujo en memoria de bytes , este, se peude 
			     regresar o enviar por la salida de un servlet
			
			JasperExportManager.exportReportToPdfStream(jasperPrint, baos);
			
			System.out.println("Ok, JasperExportManager executed");
			
			byte[] pdfBytes = null;

			pdfBytes = baos.toByteArray();
			
			*/
			
			
			
			/*   Salida a directa a un archivo en el filesystem  */
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
			
			String nombreArchivoPDFSalida = "./SubReporteSalida_"+sdf.format(new Date())+".pdf";
			
			FileOutputStream fosSalida = new FileOutputStream(nombreArchivoPDFSalida);
			
			JasperExportManager.exportReportToPdfStream(jasperPrint, fosSalida);
			
		}catch(Exception ex){
			ex.printStackTrace(System.err);
		}
	}
}
