package br.com.redumobile.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public final class DateFormatter {
	private ThreadLocal<SimpleDateFormat> formatter;

	public DateFormatter(final String format) {
		formatter = new ThreadLocal<SimpleDateFormat>() {
			@Override
			protected SimpleDateFormat initialValue() {
				return new SimpleDateFormat(format, Locale.getDefault());
			}
		};
	}

	public String formata(Date date) {
		return getDataPostagemEmPalavras(date);
	}

	public Date parse(String date) {
		Date parsedDate = null;
		try {
			parsedDate = formatter.get().parse(date);
		} catch (ParseException e) {
		}

		return parsedDate;
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
			if(horas == 1l){
				return "há " + horas +" hora";
			}
			return "há " + horas +" horas";
			
		}else if(diferencaMin >= 1440 && diferencaMin <= 2529){
			
			return "há aproximadamente 1 dia";
			
		}else if(diferencaMin >= 2530 && diferencaMin <= 43199){
			
			long dias = Math.round(diferencaMin/ 1440);
			if(dias == 1l){
				return "há " + dias +" dias";
			}
			return "há " + dias +" dias";
			
		}else if(diferencaMin >= 43200 && diferencaMin <= 86399){
			return "há aproximadamente 1 mês";
		}else if(diferencaMin >= 86400 && diferencaMin <= 525599){
			long meses = Math.round(diferencaMin/ 43200);
			if(meses == 1l){
				return "há " + meses +" meses";
			}
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
}
