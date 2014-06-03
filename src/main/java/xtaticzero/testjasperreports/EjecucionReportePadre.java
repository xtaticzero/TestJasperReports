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
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRMapCollectionDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;

/**
 * Hello world!
 *
 */
public class EjecucionReportePadre {

	public static void main(String[] args) {
		System.out.println("Test JasperReports");

		String reportPath;
		
		reportPath			= "/resportsDesigns/reportWitSubReport.jrxml";
		String subReportPath;
		
		subReportPath			= "/resportsDesigns/subReport1.jrxml";
			
		InputStream inputStream = null;
		
		System.out.println("Ok, JRDataSource created");
            
		try{
			InputStream inputStreamSR = null;
			
			inputStreamSR=EjecucionReportePadre.class.getResourceAsStream(subReportPath);
			JasperReport jasperReportSR = null;
			/* Caso 1) cargamos el jrxml y lo compilamnos : lento */
			
			JasperDesign jasperDesignSR = JRXmlLoader.load(inputStreamSR);			
			jasperReportSR = JasperCompileManager.compileReport(jasperDesignSR);
						
			//---------------------------------------------------------------------------
			inputStream = EjecucionReportePadre.class.getResourceAsStream(reportPath);
			JRDataSource dataSourcePadre = new JREmptyDataSource();
			
			Map parameters = new HashMap();
            
			JRDataSource dataSourceHijo = null;
			Collection<Map<String,?>> colData = new ArrayList<Map<String,?>>();
            for(int numReg=0;numReg<10;numReg++){
				Map<String,Object> regMap = new HashMap<String, Object>();
				
				regMap.put("id", new Integer(numReg*100));
				regMap.put("descripcion", "campo_2_reg"+numReg);
				regMap.put("campo3", "campo_1_reg"+numReg);
				
				colData.add(regMap);
			}
			
			dataSourceHijo = new JRMapCollectionDataSource(colData);
			parameters.put("dsSubreport1"   ,dataSourceHijo);
			parameters.put("subReport1"     ,jasperReportSR);
            
			JasperReport jasperReport = null;
			/* Caso 1) cargamos el jrxml y lo compilamnos : lento */
			
			JasperDesign jasperDesign = JRXmlLoader.load(inputStream);			
			jasperReport = JasperCompileManager.compileReport(jasperDesign);
			
			
			/* Caso 2) cargamos el reporte ya compilado (.jasper) mas rapido
			
			InputStream compiledReportStream = App.class.getResourceAsStream(compiledReportPath);
			jasperReport = (JasperReport) JRLoader.loadObject(compiledReportStream);
			*/
			
            
			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSourcePadre);
            
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
			
			String nombreArchivoPDFSalida = "./reportWitSubReport_"+sdf.format(new Date())+".pdf";
			
			FileOutputStream fosSalida = new FileOutputStream(nombreArchivoPDFSalida);
			
			JasperExportManager.exportReportToPdfStream(jasperPrint, fosSalida);
			
		}catch(Exception ex){
			ex.printStackTrace(System.err);
		}
	}
}
