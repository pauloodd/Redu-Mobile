package br.com.redumobile.gui;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;
import br.com.redumobile.R;

public class TelaDois extends ActivityPadrao {
	
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        
	 super.onCreate(savedInstanceState);
	 setContentView(R.layout.teladois);
    
	 try {
		 
//		 User user = Aplicacao.fachada.getUserME();
//		 User user = Aplicacao.fachada.getUserPorLogin("renatofurtado25");
//		 ImageView imageView = (ImageView) findViewById(R.id.imageProfile);
//		
//		 Bitmap bmp = Util.loadBitmap(user.getUrlFotoPerfil()); 
//		imageView.setImageBitmap(bmp); 
         
		 //>>>>>>>>>>>>>>>>>>>>>>>>>>>
//		OAuthRequest request = new OAuthRequest(Verb.POST, "http://redu.com.br/api/lectures/2828-teste-conteudo/statuses");
//		Token token = new Token(Aplicacao.autenticacao.getAccessToken(), Autenticacao.COSUMER_SECRET);
//	    Aplicacao.autenticacao.getOAuthService().signRequest(token, request);
//	    request.addBodyParameter("status[text]", "COmentario TESTE FINAL!!!!");
//	    request.addBodyParameter("status[type]", "Activity");
//	    Response response = request.send();
//	    String resposta = response.getBody();
		
//		System.out.println(resposta);
		 //>>>>>>>>>>>>>>>>>>>>>>>>>>>
//		 http://redu.com.br/api/statuses/2834/answers
		 //http://redu.com.br/api/sujects/1964

//		OAuthRequest request = new OAuthRequest(Verb.GET, "http://redu.com.br/api/lectures/2834-aula-dois-asdasdasdas/statuses");
//		Token token = new Token(Aplicacao.autenticacao.getAccessToken(), Autenticacao.COSUMER_SECRET);
//	    Aplicacao.autenticacao.getOAuthService().signRequest(token, request);
//	    Response response = request.send();
//	    String resposta = response.getBody();
		 
//		 Uri alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
//         if(alert == null){
//             // alert is null, using backup
//             alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//             if(alert == null){  // I can't see this ever being null (as always have a default notification) but just incase
//                 // alert backup is null, using 2nd backup
//                 alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);               
//             }
//         } 
         
//         Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//         Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
//         r.play();

		 String ns = Context.NOTIFICATION_SERVICE;
		 NotificationManager mNotificationManager = (NotificationManager) getSystemService(ns);
		 
		 CharSequence tickerText = "Hello";
		 long when = System.currentTimeMillis();

		 Notification notification = new Notification(R.drawable.icon, tickerText, when);
		 notification.defaults |= Notification.DEFAULT_SOUND;
		 notification.defaults |= Notification.DEFAULT_VIBRATE;
		 notification.ledARGB = 0xFF000000;
		 notification.ledOnMS = 300;
		 notification.ledOffMS = 1000;
		 notification.flags |= Notification.FLAG_SHOW_LIGHTS;
		 
		 Context context = getApplicationContext();
		 CharSequence contentTitle = "Horácio José";
		 CharSequence contentText = "Comentou no mural da aula Iteração Humano Computador";
		 Intent notificationIntent = new Intent(this, MainMenuActivity.class);
		 PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

		 notification.setLatestEventInfo(context, contentTitle, contentText, contentIntent);
		 
		 mNotificationManager.notify(1, notification);
		 
		System.out.println("");
		
		
		WebView web = (WebView) findViewById(R.id.webView);
		web.loadUrl("http://www.redu.com.br/images/new/loading-horizontal.gif");
//	        GifMovieView view = new GifMovieView(this, stream);
//	        GifDecoderView view = new GifDecoderView(this, stream);
//		GifMovieView view = new GifMovieView(this, getResources().openRawResource(R.drawable.loading));                 
	        
//	        setContentView(view);
		
		
		//http://www.redu.com.br/api/subjects/1964/lectures
		
	 } catch (Exception e) {
		 // TODO Auto-generated catch block
		 e.printStackTrace();
	 }

        
 }
	

}
