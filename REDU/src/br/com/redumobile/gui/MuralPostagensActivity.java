package br.com.redumobile.gui;

import java.util.ArrayList;
import java.util.List;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import br.com.redumobile.R;
import br.com.redumobile.adapter.PostagemAdapter;
import br.com.redumobile.entity.util.PostHelper;
import br.com.redumobile.entity.util.PostHelperSeralizable;
import br.com.redumobile.exception.FacadeException;
import br.com.redumobile.oauth.Aplicacao;

public class MuralPostagensActivity extends ActivityPadrao {
	public ListView listViewPostagens;
	public PostagemAdapter adapter;
	public List<PostHelper> postagens = null;
	public View footerView;
	public static int pageView;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		postagens = new ArrayList<PostHelper>();

		super.onCreate(savedInstanceState);
		
		pageView = 1;
		
		setContentView(R.layout.lista_postagens);
		
		//Instancia uma AsyncTask para conectar com API do Redu e buscar as postagens
		new CarregarPostagens().execute();

		 ImageView imgBtoAtualizar = (ImageView) findViewById(R.id.btnAtualizar);
	     imgBtoAtualizar.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					pageView = 1;
					postagens.clear();
					//Instancia uma AsyncTask para conectar com API do Redu e buscar as postagens
					new CarregarPostagens().execute();
				}
		});
	     
	}
	
	
	public void setAdapter(){
		listViewPostagens = (ListView) findViewById(R.id.list_view_postagens);
		listViewPostagens.setFastScrollEnabled(true);
		listViewPostagens.setScrollingCacheEnabled(false);
		adapter = new PostagemAdapter(MuralPostagensActivity.this,	postagens);

		if( listViewPostagens.getFooterViewsCount() == 0){
			
			Button mostrar = new Button(Aplicacao.contexto);
			
			mostrar.setBackgroundDrawable(Aplicacao.contexto.getResources().getDrawable(R.drawable.bnt_mostrar_mais));
			mostrar.setText("Mostrar mais resultado");
			
		mostrar.setOnClickListener(new Button.OnClickListener(){
     	   public void onClick(View v){
     		   
     		  pageView++;
     		  new CarregarPostagens().execute();
     		  
     		 listViewPostagens.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
     		 listViewPostagens.setStackFromBottom(true);
      	   }
         });
			
			
			listViewPostagens.addFooterView(mostrar);
		}
		
		
		listViewPostagens.setAdapter(adapter);

		listViewPostagens.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int postion, long id) {

				try{
					((Vibrator)getSystemService(VIBRATOR_SERVICE)).vibrate(300);	
					br.com.redumobile.entity.util.PostHelper postagemSelecionada = postagens.get(postion);
					
	     		    Intent intent = new Intent(MuralPostagensActivity.this, DetalhePostagemActivity.class);
	     		    Bundle extras = new Bundle();
	     		    extras.putSerializable("postagemSelecionada", new PostHelperSeralizable(postagemSelecionada));
	     		    intent.putExtras(extras);
	     		    
	     			startActivity(intent);
	     			
					}catch (Exception e) {
						e.getMessage();
					}
			}
		});
		
		
	}
	
	
	class CarregarPostagens extends AsyncTask<Void, Void, Void>{
		
		private ProgressDialog progressDialog;
		
		@Override
		 protected void onPreExecute() {
		    progressDialog = new ProgressDialog(MuralPostagensActivity.this);
		    progressDialog.setMessage("Aguarde...");
		    progressDialog.show();
		    
		 }


		@Override
		protected Void doInBackground(Void... params) {
			
			try {
				
				do{
					List<br.com.redumobile.entity.Status> postagensNovas = Aplicacao.fachada.getPostagens(pageView);
					for (br.com.redumobile.entity.Status statusNovo : postagensNovas) {
						postagens.add(new PostHelper(statusNovo));
					}
					
					if(postagens != null && postagens.size() < 4){
						pageView++;
					}
				}while(postagens != null && postagens.size() < 4);
				
			} catch (FacadeException e) {
				ToastPadrao.tornarTexto(Aplicacao.contexto, "Erro de conexão com a API do Redu. Tente novamente mais tarde!",
						ToastPadrao.DURACAO_LONGA,
						ToastPadrao.TOAST_ERRO).show();
			}

			return null;
		}
		
		@Override
	    protected void onPostExecute(Void result) {
			progressDialog.setMessage("Montando a página");
		    setAdapter();
		    progressDialog.dismiss();
		}
	}
	
		
	

}
