package br.com.redumobile.gui;

import android.os.Bundle;
import android.os.Handler;
import br.com.redumobile.R;
import br.com.redumobile.oauth.Aplicacao;
import br.com.redumobile.util.Util;

public class ActivityInicial extends ActivityPadrao {
	
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	Aplicacao.inicializar(getApplicationContext());
    	
    	super.onCreate(savedInstanceState);

    	setContentView(R.layout.tela_inicial);
    	
        Handler handler = new Handler(); 
        handler.postDelayed(new Runnable() { 
             public void run() {
            	 
//            	 ThreadVerificaNovasPostagem thread = new ThreadVerificaNovasPostagem((Vibrator)getSystemService(VIBRATOR_SERVICE));
//            	 thread.start();
            	 
            	 if(Util.testandoConexaoInternet()){
            		 if (Aplicacao.autenticacao.precisaLogar(Aplicacao.preferencias) == false) {
            			 trocarTela(MainMenuActivity.class, true);
            		 } else {
            			 trocarTela(ActivityLogin.class, true);
            		 }
            	 }else{
            		 trocarTela(SemConexaoInternetActivity.class, true);
            	 }
            	 
             } 
        }, 1000); 
    	

    }
    
    
}
