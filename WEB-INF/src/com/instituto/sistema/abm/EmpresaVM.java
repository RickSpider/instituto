package com.instituto.sistema.abm;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;

import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.image.AImage;
import org.zkoss.util.media.AMedia;
import org.zkoss.util.media.Media;
import org.zkoss.zk.ui.util.Notification;

import com.doxacore.util.SystemInfo;
import com.instituto.modelo.Empresa;
import com.instituto.util.TemplateViewModelLocal;

public class EmpresaVM extends TemplateViewModelLocal {

	private Empresa empresaSelected;

	@Init(superclass = true)
	public void initEmpresaVM() {

		this.cargarDatos();

	}

	@AfterCompose(superclass = true)
	public void afterComposeEmpresaVM() {

	}

	@Override
	protected void inicializarOperaciones() {
		// TODO Auto-generated method stub

	}

	public void cargarDatos() {

		this.empresaSelected = this.reg.getObjectById(Empresa.class.getName(), 1);

		if (this.empresaSelected == null) {

			this.empresaSelected = new Empresa();
			return;
		}

		try {
			this.logoFile = new AImage(this.empresaSelected.getPathLogo());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private Media logoFile;

	@Command
	@NotifyChange("*")
	public void uploadFile(@BindingParam("file") Media file) {

		if (file.getName().contains(".png")) {

			this.empresaSelected.setPathLogo(SystemInfo.SISTEMA_PATH_ABSOLUTO + "/logo/" + file.getName());
			this.logoFile = file;
		} else {

			this.mensajeInfo("Archivo no valido, debe ser .png.");

		}

	}

	@Command
	@NotifyChange("*")
	public void guardar() {

		this.empresaSelected = this.save(this.empresaSelected);

		this.guardarLogo();

		Notification.show("Datos Actualizados.");

	}

	public void guardarLogo() {

		if (this.logoFile == null) {

			return;

		}

		File logo = new File(this.empresaSelected.getPathLogo());

		Path directorioPath = Paths.get(logo.getParent());

		try {

			if (!Files.exists(directorioPath)) {

				Files.createDirectories(directorioPath);
			}

			try (InputStream inputStream = this.logoFile.getStreamData();

					OutputStream outputStream = new BufferedOutputStream(
							new FileOutputStream(this.empresaSelected.getPathLogo()))) {

				byte[] buffer = new byte[65536];
				int bytesRead;
				while ((bytesRead = inputStream.read(buffer)) != -1) {
					outputStream.write(buffer, 0, bytesRead);
				}

			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println(e);
		}

	}

	public Empresa getEmpresaSelected() {
		return empresaSelected;
	}

	public void setEmpresaSelected(Empresa empresaSelected) {
		this.empresaSelected = empresaSelected;
	}

	public Media getLogoFile() {
		return logoFile;
	}

	public void setLogoFile(Media logoFile) {
		this.logoFile = logoFile;
	}

}
