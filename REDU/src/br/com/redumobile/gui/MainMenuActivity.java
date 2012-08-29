package br.com.redumobile.gui;

import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import br.com.redumobile.R;
import br.com.redumobile.auxiliar.AuxiliarNotificacao;
import br.com.redumobile.oauth.Aplicacao;

public class MainMenuActivity extends ActivityPadrao {
	
	 @Override
	    public void onCreate(Bundle savedInstanceState) {
	        
		 super.onCreate(savedInstanceState);
	     setContentView(R.layout.menu_principal);
	        
	     
	     ImageView imgBtoAtualizar = (ImageView) findViewById(R.id.btnAtualizar);
	     imgBtoAtualizar.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					((Vibrator)getSystemService(VIBRATOR_SERVICE)).vibrate(300);
					ToastPadrao.tornarTexto(Aplicacao.contexto, "Atualizando...",
							ToastPadrao.DURACAO_LONGA,
							ToastPadrao.TOAST_NOTIFICACAO).show();
				}
			});
	     
	     Button botaoLogin = (Button)findViewById(R.id.btnDT);
        botaoLogin.setOnClickListener(new Button.OnClickListener(){
     	   public void onClick(View v){
     		  ((Vibrator)getSystemService(VIBRATOR_SERVICE)).vibrate(300);
     		  trocarTela(MuralPostagensActivity.class, false);
     	   }
        });
	        
        Button botaoLogin2 = (Button)findViewById(R.id.btnVoz);
        botaoLogin2.setOnClickListener(new Button.OnClickListener(){
     	   public void onClick(View v){
     		   ((Vibrator)getSystemService(VIBRATOR_SERVICE)).vibrate(300);
				  trocarTela(ListaCursosActivity.class, false);
     	   }
        });
        
        Button botaoLogin3 = (Button)findViewById(R.id.btnFotoVid);
        botaoLogin3.setOnClickListener(new Button.OnClickListener(){
     	   public void onClick(View v){
     		  ((Vibrator)getSystemService(VIBRATOR_SERVICE)).vibrate(300);
     		  ToastPadrao.tornarTexto(Aplicacao.contexto, "Ainda em construção!",
						ToastPadrao.DURACAO_LONGA,
						ToastPadrao.TOAST_ERRO).show();
     		  
//     		  ((Vibrator)getSystemService(VIBRATOR_SERVICE)).vibrate(300);
//     		  Intent intentTelaDois = new Intent(MainMenuActivity.this, TelaDois.class);
//				startActivity(intentTelaDois);
     	   }
        });


        Button botaoLogin4 = (Button)findViewById(R.id.btnGeoloc);
        botaoLogin4.setOnClickListener(new Button.OnClickListener(){
     	   public void onClick(View v){
     		  ((Vibrator)getSystemService(VIBRATOR_SERVICE)).vibrate(300);
     		  ToastPadrao.tornarTexto(Aplicacao.contexto, "Ainda em construção!",
						ToastPadrao.DURACAO_LONGA,
						ToastPadrao.TOAST_ERRO).show();
     	   }
        });
        
        
        Button botaoLogin5 = (Button)findViewById(R.id.btnConfig);
        botaoLogin5.setOnClickListener(new Button.OnClickListener(){
     	   public void onClick(View v){
     		  ((Vibrator)getSystemService(VIBRATOR_SERVICE)).vibrate(300);
			  trocarTela(ConfiguracaoActivity.class, false);
     	   }
        });
        
        Button botaoLogin6 = (Button)findViewById(R.id.btnSair);
        botaoLogin6.setOnClickListener(new Button.OnClickListener(){
     	   public void onClick(View v){
     		  ((Vibrator)getSystemService(VIBRATOR_SERVICE)).vibrate(300);
     		  ToastPadrao.tornarTexto(Aplicacao.contexto, "Saindo...",
						ToastPadrao.DURACAO_CURTA,
						ToastPadrao.TOAST_NOTIFICACAO).show();
     		   
     		Editor editorPreferencias = Aplicacao.preferencias.edit();
			editorPreferencias.remove("access_token");
			
			AuxiliarNotificacao.cancelarTodasNotificacao(Aplicacao.contexto);
			
			trocarTela(ActivityLogin.class, true);
     	   }
        });
	    }

}
