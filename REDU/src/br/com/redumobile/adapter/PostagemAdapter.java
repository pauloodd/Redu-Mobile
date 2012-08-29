package br.com.redumobile.adapter;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import br.com.redumobile.R;
import br.com.redumobile.entity.Activity;
import br.com.redumobile.entity.Course;
import br.com.redumobile.entity.Help;
import br.com.redumobile.entity.Lecture;
import br.com.redumobile.entity.Space;
import br.com.redumobile.entity.Subject;
import br.com.redumobile.entity.User;
import br.com.redumobile.entity.util.PostHelper;
import br.com.redumobile.exception.FacadeException;
import br.com.redumobile.oauth.Aplicacao;
import br.com.redumobile.oauth.AuxiliarOAuth;
import br.com.redumobile.util.DrawableManager;
import br.com.redumobile.util.Util;

public class PostagemAdapter extends BaseAdapter {
	private LayoutInflater inflater;
	private List<PostHelper> postagens;
	private Context context;
	public ImageView fotoPerfil = null;
	public DrawableManager drawableManager;

	public PostagemAdapter(Context context, List<PostHelper> postagens) {
		this.context = context;
		this.postagens = postagens;
		this.inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.drawableManager =  new DrawableManager();
	}

	@Override
	public int getCount() {
		return postagens.size();
	}

	@Override
	public Object getItem(int position) {
		return postagens.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {

		try{
			TextView curso = null;
			TextView disciplina = null;
			TextView modulo = null;
			TextView usuarioNome = null;
			TextView acao_usuario = null;
			TextView local_post = null;
			TextView tempo_passado = null;
			LinearLayout view_curso = null;
			LinearLayout view_disciplina = null;
			LinearLayout view_modulo = null;
			
			String statusable =  postagens.get(position).getPostagemOriginal().getStatusable();
			if(statusable != null && statusable.contains("users")){
				view = inflater.inflate(R.layout.detalhe_post_simples, null);
				
			}else if(statusable != null && statusable.contains("space")){
				view = inflater.inflate(R.layout.detalhe_post_completo, null);
			
				curso = (TextView) view.findViewById(R.id.curso);
				disciplina = (TextView) view.findViewById(R.id.disciplina);
				modulo = (TextView) view.findViewById(R.id.modulo);
				tempo_passado = (TextView) view.findViewById(R.id.tempo_passado);
				view_curso = (LinearLayout) view.findViewById(R.id.view_curso);
				view_disciplina = (LinearLayout) view.findViewById(R.id.view_disciplina);
				view_modulo = (LinearLayout) view.findViewById(R.id.view_modulo);
				
			}else if(statusable != null && statusable.contains("lecture")){
				view = inflater.inflate(R.layout.detalhe_post_completo, null);
				
				curso = (TextView) view.findViewById(R.id.curso);
				disciplina = (TextView) view.findViewById(R.id.disciplina);
				modulo = (TextView) view.findViewById(R.id.modulo);
				tempo_passado = (TextView) view.findViewById(R.id.tempo_passado);
				view_curso = (LinearLayout) view.findViewById(R.id.view_curso);
				view_disciplina = (LinearLayout) view.findViewById(R.id.view_disciplina);
				view_modulo = (LinearLayout) view.findViewById(R.id.view_modulo);
			}
			
			fotoPerfil = (ImageView) view.findViewById(R.id.imgPerfil);
			usuarioNome = (TextView) view.findViewById(R.id.usuario_nome);
			acao_usuario = (TextView) view.findViewById(R.id.acao_usuario);
			local_post = (TextView) view.findViewById(R.id.local_post);
			
			
			
			User autor = null;
			if (postagens.get(position).getPostagemOriginal() instanceof Activity) {
				Activity post = (Activity) postagens.get(position).getPostagemOriginal();
				autor = post.getAutor();
			} else {
				Help post = (Help) postagens.get(position).getPostagemOriginal();
				autor = post.getAutor();
			}
			
			final int positionFinal = position;
			final String statusableFinal = statusable;
			
			if(statusable != null && statusable.contains("users")){
				
				final User autorFinal = autor;
				final TextView local_postFinal = (TextView) view.findViewById(R.id.local_post);
				final TextView acao_usuarioFinal = (TextView) view.findViewById(R.id.acao_usuario);
				
				local_postFinal.setText("Carregando...");
				acao_usuarioFinal.setText("Carregando...");
				
				carregarInformacoesUser(positionFinal, statusableFinal,
						autorFinal, local_postFinal, acao_usuarioFinal);
				
				
			}else if(statusable != null && statusable.contains("space")){
				
				acao_usuario.setText("comentou no mural da disciplina ");
				
				final TextView cursoFinal = (TextView) view.findViewById(R.id.curso);
				final TextView disciplinaFinal = (TextView) view.findViewById(R.id.disciplina);
				
				disciplinaFinal.setText("Carregando...");
        		cursoFinal.setText("Carregando...");
        		
				carregarInformacoesSpace(positionFinal, statusableFinal,
						cursoFinal, disciplinaFinal);
				
				local_post.setVisibility(View.GONE);
				view_modulo.setVisibility(View.GONE);
				
			}else if(statusable != null && statusable.contains("lecture")){
				
				acao_usuario.setText("comentou no mural da aula ");
				
				
				final TextView local_postFinal = (TextView) view.findViewById(R.id.local_post);
				final TextView cursoFinal = (TextView) view.findViewById(R.id.curso);
				final TextView disciplinaFinal = (TextView) view.findViewById(R.id.disciplina);
				final TextView moduloFinal = (TextView) view.findViewById(R.id.modulo);
				
				local_postFinal.setText("Carregando...");
        		moduloFinal.setText("Carregando...");
        		disciplinaFinal.setText("Carregando...");
        		cursoFinal.setText("Carregando...");
				
				carregarInformacoesLecture(local_postFinal, cursoFinal,
						disciplinaFinal, moduloFinal, positionFinal,
						statusableFinal);
				
			}
	
			if (postagens.get(position).getThumbAutor() == null) {
	
				if (autor != null && autor.getUrlFotoPerfil() != null
						&& !autor.getUrlFotoPerfil().equals("")) {
					
					drawableManager.fetchDrawableOnThread(autor.getUrlFotoPerfil(), fotoPerfil);
	
				} else {
					fotoPerfil.setVisibility(View.INVISIBLE);
				}
			} else {
				fotoPerfil.setImageBitmap(postagens.get(position).getThumbAutor() );
			}
			
			
			if (autor != null && autor.getFirstName() != null
					&& !autor.getFirstName().equals("")) {
				usuarioNome.setText(autor.getFirstName() + " "
						+ autor.getLastName());
			} else {
				usuarioNome.setText(autor.getLogin());
			}
	
			tempo_passado.setText(Util.getDataPostagemEmPalavras(postagens.get(
					position).getPostagemOriginal().getDataCriacao()));
	
			view.setTag(getItem(position));

		}catch (Exception e) {
			e.getMessage();
		}
		return view;
	}

	private void carregarInformacoesUser(final int positionFinal,
			final String statusableFinal, final User autorFinal,
			final TextView local_postFinal, final TextView acao_usuarioFinal) {
		final Handler handler = new Handler() {
		    @Override
		    public void handleMessage(Message message) {
		    	User usuarioPesquisado = (User) message.obj;
		    	
		    	if(usuarioPesquisado != null){
			    	String nomeLocalMuralPostagem = usuarioPesquisado.getFirstName() + " " + usuarioPesquisado.getLastName();
					
					if(nomeLocalMuralPostagem.equals(autorFinal.getFirstName() + " " + autorFinal.getLastName())){
						acao_usuarioFinal.setText("comentou no seu próprio mural");
						local_postFinal.setVisibility(View.INVISIBLE);
					}else{
						acao_usuarioFinal.setText("comentou no mural de ");
						local_postFinal.setText(nomeLocalMuralPostagem);
					}
		    	}
				
		    }
		};
		
		  Thread thread = new Thread() {
		        @Override
		        public void run() {
		        	User usuarioLocalPost = null;
					try {
						
						
						usuarioLocalPost = pesquisarApiUser(positionFinal, statusableFinal);
					} catch (Exception e) {
						e.printStackTrace();
					}
		        	
		        	 Message message = handler.obtainMessage(1, usuarioLocalPost);
		                handler.sendMessage(message);
		        }
		  };
		    thread.start();
	}

	private User pesquisarApiUser(int position, String statusable)
			throws JSONException {
		User usuarioLocalPost = null;
		if(postagens.get(position).getMuralPublicadoUser() == null){
			usuarioLocalPost = pesquisaUsuarioLocalMuralPost(statusable);
			postagens.get(position).setMuralPublicadoUser(usuarioLocalPost);
		}else{
			usuarioLocalPost = postagens.get(position).getMuralPublicadoUser();
		}
		return usuarioLocalPost;
	}

	private void carregarInformacoesSpace(final int positionFinal,
			final String statusableFinal, final TextView cursoFinal,
			final TextView disciplinaFinal) {
		final Handler handler = new Handler() {
		    @Override
		    public void handleMessage(Message message) {
		    	Space disciplinaLocalPost = (Space) message.obj;

		    	if(disciplinaLocalPost != null){
		    		disciplinaFinal.setText(disciplinaLocalPost.getNome());
		    		cursoFinal.setText(disciplinaLocalPost.getCurso().getNome());
		    	}
		    }
		};
		
		  Thread thread = new Thread() {
		        @Override
		        public void run() {
		        	Space disciplinaLocalPost = null;
					try {
		        		
		        		
		        		disciplinaLocalPost = pesquisarApiDisciplina(positionFinal,
								statusableFinal);
						
					} catch (Exception e) {
						e.printStackTrace();
					}
		        	 Message message = handler.obtainMessage(1, disciplinaLocalPost);
		                handler.sendMessage(message);
		        }
		        
		        
		  };
		    thread.start();
	}

	private Space pesquisarApiDisciplina(int position, String statusable)
			throws JSONException, FacadeException {
		Space disciplinaLocalPost = null;
		if(postagens.get(position).getMuralPublicadoDisciplina() == null){
			disciplinaLocalPost = pesquisaDisciplinaLocalMuralPost(statusable);
			postagens.get(position).setMuralPublicadoDisciplina(disciplinaLocalPost);
		}else{
			disciplinaLocalPost = postagens.get(position).getMuralPublicadoDisciplina();
		}
		return disciplinaLocalPost;
	}

	private void carregarInformacoesLecture(final TextView local_postFinal,
			final TextView cursoFinal, final TextView disciplinaFinal,
			final TextView moduloFinal, final int positionFinal,
			final String statusableFinal) {
		final Handler handler = new Handler() {
		    @Override
		    public void handleMessage(Message message) {
		    	Lecture aulaPesquisada = (Lecture) message.obj;
		    	
		    	if(aulaPesquisada != null){
		    		local_postFinal.setText(aulaPesquisada.getNome());
		    		moduloFinal.setText(aulaPesquisada.getModulo().getNome());
		    		disciplinaFinal.setText(aulaPesquisada.getModulo().getDisciplina().getNome());
		    		cursoFinal.setText(aulaPesquisada.getModulo().getDisciplina().getCurso().getNome());
		    	}
				
				
		    }
		};
		
		  Thread thread = new Thread() {
		        @Override
		        public void run() {
		        	Lecture aula = null;
					try {
						
						
						aula = pesquisarApiInformacoesLecture(positionFinal, statusableFinal);
					} catch (Exception e) {
						e.printStackTrace();
					}
		        	
		        	 Message message = handler.obtainMessage(1, aula);
		                handler.sendMessage(message);
		        }
		        
		        
		  };
		    thread.start();
	}

	private Lecture pesquisarApiInformacoesLecture(int position,
			String statusable) throws JSONException, FacadeException {
		Lecture aula;
		if(postagens.get(position).getMuralPublicadoAula() == null){
			aula = pesquisaAulaLocalMuralPost(statusable);
			Subject moduloAula = Aplicacao.fachada.getModuloPorId(aula.getModulo().getId());
			aula.setModulo(moduloAula);
			
			String idLoginDisciplina = moduloAula.getDisciplinaLinkID();
			Space disciplinaAula = Aplicacao.fachada.getSpacePorIdLogin(idLoginDisciplina);
			moduloAula.setDisciplina(disciplinaAula);
			
			String idLoginCurso = moduloAula.getCursoLinkID();
			Course cursoAula = Aplicacao.fachada.getCursoPorIdLogin(idLoginCurso);
			disciplinaAula.setCurso(cursoAula);
					
			postagens.get(position).setMuralPublicadoAula(aula);
		}else{
			aula = postagens.get(position).getMuralPublicadoAula();
		}
		
		return aula;
	}

	private User pesquisaUsuarioLocalMuralPost(String statusable)
			throws JSONException {
		
		String retorno = AuxiliarOAuth.obterRespostaUrlViaGet(statusable, null);
		JSONObject jsonPessoa = new JSONObject(retorno);
		
		String primeiroNome = jsonPessoa.getString("first_name");
		String segundoNome = jsonPessoa.getString("last_name");
		String friends_count = jsonPessoa.getString("friends_count");
		String localization = jsonPessoa.getString("localization");
		String email = jsonPessoa.getString("email");
		String login = jsonPessoa.getString("login");
		String birthday = jsonPessoa.getString("birthday");
		String id = jsonPessoa.getString("id");
		JSONArray thumbs = jsonPessoa.getJSONArray("thumbnails");
		JSONObject imagemPerfil = thumbs.getJSONObject(1);
		String urlImagem = imagemPerfil.getString("href");
		
		User usuarioLocalPost = new User();
		usuarioLocalPost.setFirstName(primeiroNome);
		usuarioLocalPost.setLastName(segundoNome);
		usuarioLocalPost.setLogin(login);
		usuarioLocalPost.setId(id);
		usuarioLocalPost.setLocalization(localization);
		usuarioLocalPost.setEmail(email);
		usuarioLocalPost.setFriends_count(Integer.parseInt(friends_count));
		usuarioLocalPost.setBirthday(Util.convertStringToDate(birthday));
		usuarioLocalPost.setUrlFotoPerfil(urlImagem);
		
		return usuarioLocalPost;
	}
	
	private Space pesquisaDisciplinaLocalMuralPost(String statusable) throws JSONException, FacadeException {
		
		String retorno = AuxiliarOAuth.obterRespostaUrlViaGet(statusable, null);
		
		JSONObject jsonDisciplina = new JSONObject(retorno);
		
		Space disciplina = new Space();
		disciplina.setNome(jsonDisciplina.getString("name"));
		disciplina.setId(jsonDisciplina.getString("id"));
		disciplina.setDescricao(jsonDisciplina.getString("description"));
		String created_at = jsonDisciplina.getString("created_at");
		disciplina.setDataCriacao(Util.convertStringToDate(created_at));
		
		JSONArray links = jsonDisciplina.getJSONArray("links");
		JSONObject hrefCurso = links.getJSONObject(1);
		
		String urlCurso = hrefCurso.getString("href");
		String urlSplit[] = urlCurso.split("/");
		Course curso = Aplicacao.fachada.getCursoPorIdLogin(urlSplit[urlSplit.length-1]);
		disciplina.setCurso(curso);
		
		return disciplina;
		
	}
	
	private Lecture pesquisaAulaLocalMuralPost(String statusable)throws JSONException {
		
		String retorno = AuxiliarOAuth.obterRespostaUrlViaGet(statusable, null);
		
		JSONObject jsonGeral = new JSONObject(retorno);
		JSONObject jsonAula = jsonGeral.getJSONObject("lecture");
		
		Lecture aula = new Lecture();
		
		aula.setNome(jsonAula.getString("name"));
		aula.setId(jsonAula.getString("id"));
		
		Subject modulo = new Subject();
		modulo.setId(jsonAula.getString("subject_id"));
		aula.setModulo(modulo);
		
		aula.setUsuerioCriadorId(jsonAula.getString("user_id"));
		String created_at = jsonAula.getString("created_at");
		aula.setDataCriacao(Util.convertStringToDate(created_at));
		String dataAtualizacao = jsonAula.getString("updated_at");
		aula.setDataAtualizacao(Util.convertStringToDate(dataAtualizacao));
		
		
		return aula;
		
	}
	
}
