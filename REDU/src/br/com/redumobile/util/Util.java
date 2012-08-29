package br.com.redumobile.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Logger;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import br.com.redumobile.oauth.Aplicacao;

public class Util {

	public static SimpleDateFormat dateFormatDB = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	public static SimpleDateFormat dateFormatBrazil = new SimpleDateFormat("dd/MM/yyyy");

	public static SimpleDateFormat dateFormatFilename = new SimpleDateFormat("ddMMyyyyHHmmss");

	public static Date getCurrentDateTime() {
		Calendar gregorianCalendar = Calendar.getInstance();
		return gregorianCalendar.getTime();
	}

	public static Date convertStringToDate(String dateString) {
		Date date = null;

		try {
			if (dateString.length() == 8) {
				dateString = dateString.substring(0, 4) + "-"
						+ dateString.substring(4, 6) + "-"
						+ dateString.substring(6, 8) + " 00:00:00";
			}
			date = dateFormatDB.parse(dateString);
		} catch (ParseException pe) {
			pe.printStackTrace();
		}

		return date;
	}
	
	public static Date convertStringToDateFromStatus(String dateString) {
		Date date = null;

		try {
			if (dateString.length() >= 8) {
				dateString = dateString.substring(0, 4) + "-"
						+ dateString.substring(5, 7) + "-"
						+ dateString.substring(8, 10) + " " + dateString.substring(11, 19);
			}
			date = dateFormatDB.parse(dateString);
		} catch (ParseException pe) {
			pe.printStackTrace();
		}

		return date;
	}
	
	public static String getLoginFromHref(String href){
		
		String login = "";
		try {
			
			String array[] = href.split("/");
			login = array[array.length-1];
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		return login;
		
	}
	
	public static Bitmap loadBitmap(String url) {
	    Bitmap bitmap = null;
	    InputStream in = null;
	    BufferedOutputStream out = null;

	    try {
	        in = new BufferedInputStream(new URL(url).openStream());

	        final ByteArrayOutputStream dataStream = new ByteArrayOutputStream(8192);
	        out = new BufferedOutputStream(dataStream,8192);
	        copy(in, out, 8192); //8k
	        out.flush();

	        final byte[] data = dataStream.toByteArray();
	        BitmapFactory.Options options = new BitmapFactory.Options();
	        //options.inSampleSize = 1;

	        bitmap = BitmapFactory.decodeByteArray(data, 0, data.length,options);
	    } catch (IOException e) {
	    	e.printStackTrace();
	    } finally {
	        try {
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}finally{
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
	    }

	    return bitmap;
	}
	
	public static void copy(InputStream in, OutputStream out,  int bufferSize)	   throws IOException	{
	   byte[] buf = new byte[bufferSize];
	   int len = 0;
	   while ((len = in.read(buf)) >= 0)
	   {
	      out.write(buf, 0, len);
	   }
	}
	
	public static String converteData(Date dtData){
		
       try {  
    	  String dataFormatada = dateFormatDB.format(dtData);
          return dataFormatada;  
       } catch (Exception ex) {  
          return "Erro na conversão da data";  
       }  
    }  
	
	
	public static String getDataPostagemEmPalavras(Date dataCriacao){
		
		Date hoje = new Date();
		int hojeMin = (int) ((hoje.getTime() / (1000*60)) );
		int dataCriacaoMin = (int) ((dataCriacao.getTime() / (1000*60)) );
		
		int diferencaMin = hojeMin - dataCriacaoMin; 
		
		if(diferencaMin == 0 || diferencaMin == 1){
			
			int hojeSec = (int) (hoje.getTime() / (1000) ) ;
			int dataCriacaoSec = (int) (dataCriacao.getTime() / (1000)) ;
			int diferencaSeg = hojeSec - dataCriacaoSec;

			if(diferencaSeg <= 4){
				return "há menos de 5 segundos";
			}else if(diferencaSeg >= 5 && diferencaSeg <= 9){
				return "há menos de 10 segundos";
			}else if(diferencaSeg >= 10 && diferencaSeg <= 19){
				return "há menos de 20 segundos";
			}else if(diferencaSeg >= 20 && diferencaSeg <= 39){
				return "há 30 segundos";
			}else if(diferencaSeg >= 40 && diferencaSeg <= 59){
				return "há menos de 1 minuto";
			}else{
				return "há 1 minuto";
			}
			
		}else if(diferencaMin >= 2 && diferencaMin <= 44){
			
			return "há " + diferencaMin + " minutos";
			
		}else if(diferencaMin >= 45 && diferencaMin <= 89){
			
			return "há aproximadamente 1 hora";
			
		}else if(diferencaMin >= 90 && diferencaMin <= 1439){

			long horas = Math.round(diferencaMin/ 60);
			return "há " + horas +" horas";
			
		}else if(diferencaMin >= 1440 && diferencaMin <= 2529){
			
			return "há aproximadamente 1 dia";
			
		}else if(diferencaMin >= 2530 && diferencaMin <= 43199){
			
			long dias = Math.round(diferencaMin/ 1440);
			return "há " + dias +" dias";
			
		}else if(diferencaMin >= 43200 && diferencaMin <= 86399){
			return "há aproximadamente 1 mês";
		}else if(diferencaMin >= 86400 && diferencaMin <= 525599){
			long meses = Math.round(diferencaMin/ 43200);
			return "há " + meses +" meses";
		}else{
			
		   int diferencaAnos = diferencaMin / 525600;
		   int minute_offset_for_leap_year = (diferencaAnos / 4) * 1440;
           int remainder = ((diferencaMin - minute_offset_for_leap_year) % 525600);
           if (remainder < 131400){
        	   return "há aproximadamente " + diferencaAnos + " anos";
           }else if (remainder < 394200){
        	   return "há mais de " + diferencaAnos + " anos";
           }else{
        	   return "há aproximadamente " + (diferencaAnos+1) + " anos";
           }
			
		}
		
	}
	
	public static boolean testandoConexaoInternet(){
		 boolean lblnRet = false;
	        try
	        {
	            ConnectivityManager cm = (ConnectivityManager)
	            Aplicacao.contexto.getSystemService(Context.CONNECTIVITY_SERVICE); 
	            if (cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isAvailable() && cm.getActiveNetworkInfo().isConnected()) { 
	                lblnRet = true; 
	            } else { 
	                lblnRet = false; 
	            }
	        }catch (Exception e) {
	            e.getMessage();
	        }
	        return lblnRet;
	}
	
	 public static Bitmap retrieveImageFromURL(String urlString) {
		 Bitmap bm = null;
		 try
	        {
                 URL url = new URL(urlString); 
                 URLConnection conn = url.openConnection(); 
                conn.connect(); 
                 BufferedInputStream bis = new BufferedInputStream(conn.getInputStream()); 
                 bm = BitmapFactory.decodeStream(url.openStream()); 
	        }catch (Exception e) {
	            e.getMessage();
	        }
                 return bm; 
        } 

	
}
