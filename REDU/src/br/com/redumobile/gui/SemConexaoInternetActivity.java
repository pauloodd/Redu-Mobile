package br.com.redumobile.gui;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import br.com.redumobile.R;
import br.com.redumobile.oauth.Aplicacao;

public class SemConexaoInternetActivity extends ActivityPadrao {
	
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	Aplicacao.inicializar(getApplicationContext());
    	
    	super.onCreate(savedInstanceState);

    	setContentView(R.layout.sem_conexao);
    	
       
    	 Button btnSair = (Button)findViewById(R.id.btSair);
    	 btnSair.setOnClickListener(new Button.OnClickListener(){
      	   public void onClick(View v){
      		 System.runFinalizersOnExit(true);
      		 System.exit(0);
      	   }
      	   
         });

    }
    
    
}



