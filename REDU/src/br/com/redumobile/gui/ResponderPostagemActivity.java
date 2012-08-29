package br.com.redumobile.gui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import br.com.redumobile.R;
import br.com.redumobile.entity.util.PostHelperSeralizable;
import br.com.redumobile.exception.FacadeException;
import br.com.redumobile.oauth.Aplicacao;

public class ResponderPostagemActivity extends ActivityPadrao {
	
	public PostHelperSeralizable postegemSelecionada;
	public EditText respostaComentario;
	public String tipoResposta;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.responder_postagem);
		
		respostaComentario = (EditText) findViewById(R.id.texto_resposta);
		
		Intent i = getIntent();
		Bundle extras = i.getExtras();
		postegemSelecionada = (PostHelperSeralizable) extras
				.get("postagemSelecionada");
		tipoResposta = extras.getString("tipoResposta");
		
		respostaComentario.setOnClickListener(new EditText.OnClickListener() {

			@Override
			public void onClick(View v) {
				String conteudo = respostaComentario.getText().toString(); 
	    		if(conteudo.equals("Digite sua resposta...")){
	    			respostaComentario.setTextColor(R.color.preto);
	    			respostaComentario.setText("");
	    		}
			}

		});
		
		Button btnLimpar = (Button) findViewById(R.id.btn_limpar);
		btnLimpar.setOnClickListener(new Button.OnClickListener(){
 	   public void onClick(View v){
      		respostaComentario.setText("");
      	   }
         });
		
		Button btnEnviar = (Button) findViewById(R.id.btn_enviar);
		btnEnviar.setOnClickListener(new Button.OnClickListener(){
 	   public void onClick(View v){
 		   ((Vibrator)getSystemService(VIBRATOR_SERVICE)).vibrate(300);
      		  
 		   	new EnviarRespostaAsyncTask().execute();
      	   }
         });
		
		
	}
	
	public void enviarRespostas(){
		if(tipoResposta != null && tipoResposta.equals("Activity")){
  			String selfHref = postegemSelecionada.getPostagemOriginal().getSelfHref() + "/answers";
  			try {
				String respotaEnviar = Aplicacao.fachada.enviarResposta(selfHref, respostaComentario.getText().toString(), "Activity");
			} catch (FacadeException e) {
				e.printStackTrace();
			}
  		}else if(tipoResposta != null && tipoResposta.equals("Help")){
  			String statuses = postegemSelecionada.getPostagemOriginal().getStatusable() + "/statuses";
  			try {
  				String respotaEnviar =	Aplicacao.fachada.enviarResposta(statuses, respostaComentario.getText().toString(), "Help");
			} catch (FacadeException e) {
				e.printStackTrace();
			}
  		}
	}
	
	
	class EnviarRespostaAsyncTask extends AsyncTask<Void, Void, Void>{
		
		private ProgressDialog progressDialog;
		
		@Override
		 protected void onPreExecute() {
		    progressDialog = new ProgressDialog(ResponderPostagemActivity.this);
			if(tipoResposta != null && tipoResposta.equals("Activity")){
				progressDialog.setMessage("Enviando Comentário...");				
			}else{
				progressDialog.setMessage("Enviando pedido de Ajuda...");
			}
		    progressDialog.show();
		    
		 }


		@Override
		protected Void doInBackground(Void... params) {
			
			try {
				enviarRespostas();
			} catch (Exception e) {
				ToastPadrao.tornarTexto(Aplicacao.contexto, "Erro de conexão com a API do Redu. Tente novamente mais tarde!",
						ToastPadrao.DURACAO_LONGA,
						ToastPadrao.TOAST_ERRO).show();
			}

			return null;
		}
		
		@Override
	    protected void onPostExecute(Void result) {
		   
			progressDialog.dismiss();
			
			Intent intent = new Intent(ResponderPostagemActivity.this, DetalhePostagemActivity.class);
			Bundle extras = new Bundle();
			extras.putSerializable("postagemSelecionada", postegemSelecionada);
			intent.putExtras(extras);
			
			startActivity(intent);
			
			ResponderPostagemActivity.this.finish();
		}
	}
	
}
