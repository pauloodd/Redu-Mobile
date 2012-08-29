package br.com.redumobile.gui;

import java.util.List;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import br.com.redumobile.R;
import br.com.redumobile.adapter.CourseAdapter;
import br.com.redumobile.entity.Course;
import br.com.redumobile.exception.FacadeException;
import br.com.redumobile.oauth.Aplicacao;

public class ListaCursosActivity extends ActivityPadrao {
	public ListView listViewCoursos;
	public CourseAdapter adapter;
	public List<Course> coursos = null;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.lista_courses);
		
		//Instancia uma AsyncTask para conectar com API do Redu e buscar os ambientes
		new CarregarAmbientes().execute();

		
	}
	
	
	public void setAdapter(){
		listViewCoursos = (ListView) findViewById(R.id.list_view_courses);
		adapter = new CourseAdapter(ListaCursosActivity.this,	coursos);

		listViewCoursos.setAdapter(adapter);

		listViewCoursos.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int postion, long id) {

				
				
			}
		});
		
		
	}
	
	
	class CarregarAmbientes extends AsyncTask<Void, Void, Void>{
		
		private ProgressDialog progressDialog;
		
		@Override
		 protected void onPreExecute() {
		    progressDialog = new ProgressDialog(ListaCursosActivity.this);
		    progressDialog.setMessage("Aguarde...");
		    progressDialog.show();
		    
		 }


		@Override
		protected Void doInBackground(Void... params) {
			
			try {
				coursos = Aplicacao.fachada.getCoursesPorUser();
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
