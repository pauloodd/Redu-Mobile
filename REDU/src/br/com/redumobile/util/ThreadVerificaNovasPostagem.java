package br.com.redumobile.util;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import br.com.redumobile.R;
import br.com.redumobile.gui.MainMenuActivity;
import br.com.redumobile.oauth.Aplicacao;

public class ThreadVerificaNovasPostagem extends Thread {

	public ThreadVerificaNovasPostagem() {
	}

	@Override
	public void run() {
		try {
			if (true) {
				
			  Handler handler = new Handler(); 
		      handler.postDelayed(new Runnable() { 
			             public void run() {
			            	 
			            	 String ns = Context.NOTIFICATION_SERVICE;
			            	 NotificationManager mNotificationManager = (NotificationManager) Aplicacao.contexto
			            			 .getSystemService(ns);
			            	 
			            	 CharSequence tickerText = "Hello";
			            	 long when = System.currentTimeMillis();
			            	 
			            	 Notification notification = new Notification(R.drawable.icon,
			            			 tickerText, when);
			            	 notification.defaults |= Notification.DEFAULT_SOUND;
			            	 notification.defaults |= Notification.DEFAULT_VIBRATE;
			            	 notification.ledARGB = 0xFF000000;
			            	 notification.ledOnMS = 300;
			            	 notification.ledOffMS = 1000;
			            	 notification.flags |= Notification.FLAG_SHOW_LIGHTS;
			            	 
			            	 Context context = Aplicacao.contexto;
			            	 CharSequence contentTitle = "Horácio José";
			            	 CharSequence contentText = "Comentou no mural da aula Iteração Humano Computador";
			            	 Intent notificationIntent = new Intent(context,MainMenuActivity.class);
			            	 PendingIntent contentIntent = PendingIntent.getActivity(context,
			            			 0, notificationIntent, 0);
			            	 
			            	 notification.setLatestEventInfo(context, contentTitle,
			            			 contentText, contentIntent);
			            	 
			            	 mNotificationManager.notify(1, notification);
			             } 
			        }, 10000); 

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
