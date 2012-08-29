package br.com.redumobile.oauth;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Process;
import br.com.redumobile.entity.User;
import br.com.redumobile.entity.util.Configuracoes;
import br.com.redumobile.exception.FacadeException;
import br.com.redumobile.facade.Facade;

public class Aplicacao {
	public static Autenticacao autenticacao;
	public static Context contexto;
	public static boolean manterConectado;
	public static final String NOME_APP = "ReduMobile";
	public static final String NOME_APP_MD5_SHA1 = "b48d62a8c521747b88ce77f95931537093933249";
	public static SharedPreferences preferencias;
	public static User usuario;
	public static Facade fachada;
	public static Configuracoes config;

	public static void inicializar(Context contexto) {
		Aplicacao.contexto = contexto;

		autenticacao = new Autenticacao();

		manterConectado = true;

		preferencias = obterPreferencias(contexto);

		fachada = Facade.getInstance();
		
	}
	
	public static void matarAplicacao() {
		Process.killProcess(Process.myPid());
	}

	private static SharedPreferences obterPreferencias(Context contexto) {
		SharedPreferences preferencias = contexto.getSharedPreferences(
				NOME_APP_MD5_SHA1, 0);

		return preferencias;
	}
	
	
	public static User getUsuario() {
		if(usuario == null){
			try {
				usuario = Aplicacao.fachada.getUserME();
			} catch (FacadeException e) {
				e.printStackTrace();
			}
		}
		return usuario;
	}

	public static void setUsuario(User usuario) {
		Aplicacao.usuario = usuario;
	}
}
