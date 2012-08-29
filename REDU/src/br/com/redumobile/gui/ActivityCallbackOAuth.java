package br.com.redumobile.gui;

import android.app.Notification;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import br.com.redumobile.R;
import br.com.redumobile.auxiliar.AuxiliarNotificacao;
import br.com.redumobile.entity.User;
import br.com.redumobile.oauth.Aplicacao;

public class ActivityCallbackOAuth extends ActivityPadrao {
	
	@Override
	protected void onCreate(Bundle icicle) {
		try {
			
		        String authUrl = Aplicacao.autenticacao.getOAuthService().getAuthorizationUrl(null);
		        
				Uri uriTokens = Uri.parse(authUrl);
				trocarTela(Intent.ACTION_VIEW, uriTokens, false);
				
		} catch (Exception e) {
			Log.e(Aplicacao.NOME_APP, e.getMessage());

			ToastPadrao.tornarTexto(Aplicacao.contexto,
					"Erro ao tentar se conectar ao serviço API do Redu",
					ToastPadrao.DURACAO_LONGA, ToastPadrao.TOAST_ERRO).show();
		}

		super.onCreate(icicle);
	}

	@Override
	protected void onNewIntent(Intent intent) {
		Uri uriResultado = intent.getData();

		try {
			Aplicacao.autenticacao.guardarTokens(uriResultado);
			User usuarioLogado = Aplicacao.getUsuario();
			Aplicacao.setUsuario(usuarioLogado);

			ToastPadrao.tornarTexto(Aplicacao.contexto, "Carregando...",
					ToastPadrao.DURACAO_LONGA, ToastPadrao.TOAST_NOTIFICACAO)
					.show();

			Notification notifUsuarioLogado = AuxiliarNotificacao
					.criarNotificacao(Aplicacao.contexto, R.drawable.icon,
							"Obrigado! Você está logado agora", "Olá " + usuarioLogado.getFirstName() + " " + usuarioLogado.getLastName(),
							"Clique aqui para ver sua tela principal",
							MainMenuActivity.class);

			int idNotifUsuarioLogado = AuxiliarNotificacao.notificar(
					Aplicacao.contexto, notifUsuarioLogado);

			Intent intentExtras = new Intent();
			intentExtras.putExtra("idNotifUsuarioLogado", idNotifUsuarioLogado);
			trocarTela(ActivityInicial.class, intentExtras, true);
		} catch (Exception e) {

			ToastPadrao.tornarTexto(Aplicacao.contexto,
					"Erro ao tentar se conectar com a API do Redu. Favor tente novamente mais tarde!",
					ToastPadrao.DURACAO_LONGA, ToastPadrao.TOAST_ERRO).show();
		}

		super.onNewIntent(intent);
	}
}
