package com.instituto.sistema.reporte;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.util.Notification;
import org.zkoss.zul.Filedownload;

import com.instituto.modelo.Empresa;
import com.instituto.util.ParamsLocal;
import com.instituto.util.TemplateViewModelLocal;

public class RG90VM extends TemplateViewModelLocal {

	private Date fecha = new Date();

	private List<Object[]> lRG90;

	@Init(superclass = true)
	public void initRG90VM() {

		this.cargarDatos();

	}

	@AfterCompose(superclass = true)
	public void afterComposeFacturacionReporteVM() {

	}

	@Override
	protected void inicializarOperaciones() {
		// TODO Auto-generated method stub

	}
	
	

	@NotifyChange("*")
	@Command()
	public void cargarDatos() {

		LocalDate localDate = this.fecha.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

		// Formatear LocalDate a 'YYYY-MM'
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");
		String formattedDate = localDate.format(formatter);

		String sql = this.um.getSql("rg90/listaRG90.sql").replace("?1", formattedDate);
		this.lRG90 = this.reg.sqlNativo(sql);

	}
	
	@Command
	public void exprtar() {
		
		if (this.lRG90.size() <= 0) {
			
			
			Notification.show("Nada que exportar.");
			
			return;
		}
		
		Empresa empresa = this.reg.getObjectById(Empresa.class.getName(), 1);
		
		LocalDate localDate = this.fecha.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

		// Formatear LocalDate a 'YYYY-MM'
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMyyyy");
		String formattedDate = localDate.format(formatter);
		
		Pattern pattern = Pattern.compile("^(\\d+)-");
        Matcher matcher = pattern.matcher(empresa.getRuc());

        String result = ""; // Inicializar resultado
        if (matcher.find()) {
            result = matcher.group(1); // Obtener la parte antes del guion
        } else {
            result = empresa.getRuc(); // Si no hay coincidencia, devolver la cadena completa
        }
		
		String filename= result+"_REG_"+formattedDate+"_C0001";
		
		//String zipFilePath = filename+".zip";

        // Crear el archivo ZIP en memoria y agregar el CSV
        try {
            byte[] zipData = createZipWithCsv(filename);
            // Guardar el archivo ZIP en el sistema de archivos
          //  saveZipToFile(zipFilePath, zipData);
            
            Filedownload.save(zipData, "application/zip", filename+".zip");
            
            //System.out.println("Archivo ZIP creado: " + zipFilePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        
    	
		
	}
	
	 private byte[] createZipWithCsv(String filename) throws IOException {
	        // Crear un ByteArrayOutputStream para almacenar el ZIP en memoria
	        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
	             ZipOutputStream zos = new ZipOutputStream(baos);
	             ByteArrayOutputStream csvBaos = new ByteArrayOutputStream();
	             BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(csvBaos, "UTF-8"))) {

	            // Escribir algunos datos de ejemplo
	            
	            for (Object[] x : this.lRG90) {
	            	
	            	StringBuilder sb = new StringBuilder();

	                for (int i = 0; i < x.length; i++) {
	                	if (x[i] instanceof Double) {
	                        // Agregar el número como entero o decimal sin formato
	                        //sb.append(BigDecimal.valueOf(Double.valueOf(x[i].toString())));
	                        
	                        BigDecimal decimalValue = BigDecimal.valueOf((Double) x[i]);
	                        sb.append(decimalValue.stripTrailingZeros().toPlainString());
	                		//sb.append("hello");
	                    } else {
	                        // Agregar cadenas directamente
	                        sb.append(x[i]);
	                    }
	                    if (i < x.length - 1) {
	                        sb.append(";"); // Añadir el separador solo si no es el último elemento
	                    }
	                }
	            	
	                writer.write(sb.toString());
		            writer.newLine();
	                
	            }
	           
	            writer.flush(); // Asegúrate de que todos los datos se escriban en el flujo

	            // Crear una entrada en el archivo ZIP para el CSV
	            ZipEntry zipEntry = new ZipEntry(filename+".csv");
	            zos.putNextEntry(zipEntry);

	            // Escribir el contenido del CSV en el archivo ZIP
	            zos.write(csvBaos.toByteArray());
	            zos.closeEntry();

	            zos.finish(); // Finaliza la escritura en el ZIP
	            return baos.toByteArray(); // Retorna los datos del ZIP en memoria
	        }
	    }

	    

	public List<Object[]> getlRG90() {
		return lRG90;
	}

	public void setlRG90(List<Object[]> lRG90) {
		this.lRG90 = lRG90;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

}
