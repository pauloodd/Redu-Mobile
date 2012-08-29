package br.com.redumobile.gui;

import java.util.List;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import br.com.redumobile.R;
import br.com.redumobile.adapter.AnswerAdapter;
import br.com.redumobile.entity.Activity;
import br.com.redumobile.entity.Answer;
import br.com.redumobile.entity.Help;
import br.com.redumobile.entity.Lecture;
import br.com.redumobile.entity.Space;
import br.com.redumobile.entity.Status;
import br.com.redumobile.entity.User;
import br.com.redumobile.entity.util.PostHelperSeralizable;
import br.com.redumobile.oauth.Aplicacao;
import br.com.redumobile.util.Util;

public class DetalhePostagemActivity extends ActivityPadrao {

	public LinearLayout listaAnswers;
	public AnswerAdapter adapter;
	public List<Status> respostas = null;
	public LayoutInflater inflater;
	public PostHelperSeralizable postegemSelecionada;
	public TextView semComentarios;
	public TextView qtd_comentarios;
	public TextView qtd_duvidas;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.lista_answer);
		
		inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		listaAnswers = (LinearLayout) findViewById(R.id.list_view_respostas);
		
		try {
			Intent i = getIntent();
			Bundle extras = i.getExtras();
			postegemSelecionada = (PostHelperSeralizable) extras
					.get("postagemSelecionada");

			ImageView fotoPerfil = (ImageView) findViewById(R.id.imgPerfil);
			TextView curso = (TextView) findViewById(R.id.curso);
			TextView disciplina = (TextView) findViewById(R.id.disciplina);
			TextView modulo = (TextView) findViewById(R.id.modulo);
			TextView usuarioNome = (TextView) findViewById(R.id.usuario_nome);
			TextView conteudo_post = (TextView) findViewById(R.id.conteudo_post);
			TextView local_post = (TextView) findViewById(R.id.local_post);
			TextView tempo_passado = (TextView) findViewById(R.id.tempo_passado);
			LinearLayout view_curso = (LinearLayout) findViewById(R.id.view_curso);
			LinearLayout view_disciplina = (LinearLayout) findViewById(R.id.view_disciplina);
			LinearLayout view_modulo = (LinearLayout) findViewById(R.id.view_modulo);
			LinearLayout view_nome_usuario =  (LinearLayout) findViewById(R.id.view_nome_usuario);
			
			Button btnDuvida = (Button) findViewById(R.id.btn_duvida);
			btnDuvida.setOnClickListener(new Button.OnClickListener(){
     	   public void onClick(View v){
	      		  ((Vibrator)getSystemService(VIBRATOR_SERVICE)).vibrate(300);
	      		  
		      		Intent intent = new Intent(DetalhePostagemActivity.this, ResponderPostagemActivity.class);
	     		    Bundle extras = new Bundle();
	     		    extras.putSerializable("postagemSelecionada", postegemSelecionada);
	     		    extras.putString("tipoResposta", "Help");
	     		    intent.putExtras(extras);
	     		    
	     			startActivity(intent);
	      	   }
	         });
			Button btnComentar = (Button) findViewById(R.id.btn_comentar);
			btnComentar.setOnClickListener(new Button.OnClickListener(){
		     	   public void onClick(View v){
		      		  ((Vibrator)getSystemService(VIBRATOR_SERVICE)).vibrate(300);
		      		  
			      		Intent intent = new Intent(DetalhePostagemActivity.this, ResponderPostagemActivity.class);
		     		    Bundle extras = new Bundle();
		     		    extras.putSerializable("postagemSelecionada", postegemSelecionada);
		     		    extras.putString("tipoResposta", "Activity");
		     		    intent.putExtras(extras);
		     		    
		     			startActivity(intent);
		      	   }
		         });
			
			qtd_comentarios = (TextView) findViewById(R.id.qtd_comentarios);
			qtd_duvidas = (TextView) findViewById(R.id.qtd_duvidas);
			semComentarios = (TextView) findViewById(R.id.sem_comentarios);
			
			User autor = null;
			
			if(postegemSelecionada.getPostagemOriginal() instanceof Activity){
				Activity postOriginal = (Activity) postegemSelecionada.getPostagemOriginal();
				String texto = postOriginal.getText();
				conteudo_post.setText(texto);
				
				tempo_passado.setText(Util.getDataPostagemEmPalavras(postOriginal.getDataCriacao()));
				autor = postOriginal.getAutor();
				
				usuarioNome.setText(autor.getFirstName() + " "
						+ autor.getLastName());
				
				Bitmap bmp = Util.loadBitmap(autor.getUrlFotoPerfil());
				fotoPerfil.setImageBitmap(bmp);
			}else if(postegemSelecionada.getPostagemOriginal() instanceof Help){
				Help postOriginal = (Help) postegemSelecionada.getPostagemOriginal();
				String texto = postOriginal.getText();
				conteudo_post.setText(texto);
				
				tempo_passado.setText(Util.getDataPostagemEmPalavras(postOriginal.getDataCriacao()));
				autor = postOriginal.getAutor();
				
				usuarioNome.setText(autor.getFirstName() + " "
						+ autor.getLastName());
				
				Bitmap bmp = Util.loadBitmap(autor.getUrlFotoPerfil());
				fotoPerfil.setImageBitmap(bmp);
			}
			
			if(postegemSelecionada.getMuralPublicadoAula() != null){
				
				Lecture aula = postegemSelecionada.getMuralPublicadoAula();
				
				local_post.setText(aula.getNome());
				modulo.setText(aula.getModulo().getNome());
				disciplina.setText(aula.getModulo().getDisciplina().getNome());
				curso.setText(aula.getModulo().getDisciplina().getCurso().getNome());
				
				
			}else if(postegemSelecionada.getMuralPublicadoDisciplina() != null){
				
				view_modulo.setVisibility(View.GONE);
				Space disciplinaLocalPost = postegemSelecionada.getMuralPublicadoDisciplina();
				
				disciplina.setText(disciplinaLocalPost.getNome());
				curso.setText(disciplinaLocalPost.getCurso().getNome());
				local_post.setVisibility(View.GONE);
				
				btnDuvida.setVisibility(View.GONE);
			}else if(postegemSelecionada.getMuralPublicadoUser() != null){
				
				view_curso.setVisibility(View.GONE);
				view_disciplina.setVisibility(View.GONE);
				view_modulo.setVisibility(View.GONE);
				
				local_post.setTextColor(R.color.preto);
				
				User usuarioLocalPost = postegemSelecionada.getMuralPublicadoUser();
				String nomeLocalMuralPostagem = usuarioLocalPost.getFirstName() + " " + usuarioLocalPost.getLastName();
				
				if(nomeLocalMuralPostagem.equals(autor.getFirstName() + " " + autor.getLastName())){
					local_post.setText("comentou no seu próprio mural");
				}else{
					local_post.setText("comentou no mural de " + nomeLocalMuralPostagem);
				}
				
				LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
				lp.setMargins(0, 20, 0, 0);
				view_nome_usuario.setLayoutParams(lp);
				
				btnDuvida.setVisibility(View.GONE);
			}

		} catch (Exception e) {
			e.getMessage();
		}
		
		//Instancia uma AsyncTask para conectar com API do Redu e buscar os comentarios
		new CarregarComentarios().execute();

	}

	public void setAdapter() {
		
		int qtdDuvidas = 0;
		int qtdComentarios = 0;
		int count = 1;
		if(respostas != null && !respostas.isEmpty()){
			for (Status resposta : respostas) {
				View view = inflater.inflate(R.layout.detalhe_answer, null);
				view.setId(count);
				TextView resposta_texto = (TextView) view.findViewById(R.id.resposta_texto);
				ImageView fotoPerfil = (ImageView) view.findViewById(R.id.imgPerfil);
				TextView tempo_passado = (TextView) view.findViewById(R.id.tempo_passado);
				TextView usuarioNome = (TextView) view.findViewById(R.id.usuario_nome);
				
				resposta_texto.setText(resposta.getText());
				tempo_passado.setText(Util.getDataPostagemEmPalavras(resposta.getDataCriacao()));
				
				User autorResposta = null;
				if(resposta instanceof Activity){
					autorResposta = ((Activity) resposta).getAutor();
					qtdComentarios++;
				}else if (resposta instanceof Help) {
					autorResposta = ((Help) resposta).getAutor();
					qtdDuvidas++;
				}else if (resposta instanceof Answer) {
					autorResposta = ((Answer) resposta).getAutor();
					qtdComentarios++;
				}
				
				usuarioNome.setText(autorResposta.getFirstName() + " " + autorResposta.getLastName());
				
				Bitmap bmp = Util.loadBitmap(autorResposta.getUrlFotoPerfil());
				fotoPerfil.setImageBitmap(bmp);
				
				
				count++;
				listaAnswers.addView(view);
			}
			
			semComentarios.setVisibility(View.GONE);
		}else{
			semComentarios.setVisibility(View.VISIBLE);
		}
		
		
		qtd_comentarios.setText("" + qtdComentarios);
		qtd_duvidas.setText("" + qtdDuvidas);
	}

	class CarregarComentarios extends AsyncTask<Void, Void, Void> {

		private ProgressDialog progressDialog;

		@Override
		protected void onPreExecute() {
			progressDialog = new ProgressDialog(DetalhePostagemActivity.this);
			progressDialog.setMessage("Carregando respostas...");
			progressDialog.show();

		}

		@Override
		protected Void doInBackground(Void... params) {

			try {
				
				if(postegemSelecionada.getPostagemOriginal() != null){
					
					if(postegemSelecionada.getPostagemOriginal() instanceof Activity){
						Activity activity = (Activity) postegemSelecionada.getPostagemOriginal();
						List<br.com.redumobile.entity.Status> respostasNovas = Aplicacao.fachada.pesquisaRepostasStatus(activity.getAnswer());
						
						respostas = respostasNovas;
					}else if(postegemSelecionada.getPostagemOriginal() instanceof Help){
						Help ajuda = (Help) postegemSelecionada.getPostagemOriginal();
						List<br.com.redumobile.entity.Status> respostasNovas = Aplicacao.fachada.pesquisaRepostasStatus(ajuda.getAnswer());
						
						respostas = respostasNovas;
					}
				}
				
			} catch (Exception e) {
				ToastPadrao
						.tornarTexto(
								Aplicacao.contexto,
								"Erro de conexão com a API do Redu. Tente novamente mais tarde!",
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
