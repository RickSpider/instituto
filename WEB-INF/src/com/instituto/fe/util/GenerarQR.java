package com.instituto.fe.util;

import java.awt.image.BufferedImage;
import java.io.UnsupportedEncodingException;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
;

public class GenerarQR {

	public static BufferedImage createQR(String data, String charset, int width, int height ) {

		BitMatrix matrix;
		try {
			matrix = new MultiFormatWriter().encode(new String(data.getBytes(charset), charset),
					BarcodeFormat.QR_CODE, width, height);
			
			BufferedImage img = new BufferedImage(width,height, BufferedImage.TYPE_INT_RGB);
			
			for (int y = 0; y < height; y++) {
	            for (int x = 0; x < width; x++) {
	                int grayValue = matrix.get(x, y) ? 0 : 255; // Adjusted condition
	                int pixelValue = (grayValue << 16) | (grayValue << 8) | grayValue; // Convert to RGB
	                img.setRGB(x, y, pixelValue);
	            }
	        }
			
			return img;
		} catch (UnsupportedEncodingException | WriterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

}
