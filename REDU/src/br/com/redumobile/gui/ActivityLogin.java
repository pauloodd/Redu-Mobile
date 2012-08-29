package br.com.redumobile.gui;

import android.app.Notification;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import br.com.redumobile.R;
import br.com.redumobile.auxiliar.AuxiliarNotificacao;
import br.com.redumobile.entity.User;
import br.com.redumobile.oauth.Aplicacao;

public class ActivityLogin extends ActivityPadrao {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        
    	setContentView(R.layout.tela_login);
    	
        Button botaoLogin = (Button)findViewById(R.id.btLogin);
        
        botaoLogin.setOnClickListener(new Button.OnClickListener(){
     	   public void onClick(View v){
     		   
     		  CheckBox manterContectado = (CheckBox) findViewById(R.id.manterContectado);
     		  Aplicacao.manterConectado = manterContectado.isChecked();
     		   
     		  ToastPadrao.tornarTexto(Aplicacao.contexto, "Carregando...",
						ToastPadrao.DURACAO_LONGA,
						ToastPadrao.TOAST_NOTIFICACAO).show();


				trocarTela(ActivityCallbackOAuth.class, true);
     	   }
        });
        
        super.onCreate(savedInstanceState);
    }
    
    
    @Override
	protected void onNewIntent(Intent intent) {
		Uri uriResultado = intent.getData();

		try {
			Aplicacao.autenticacao.guardarTokens(uriResultado);

			ToastPadrao.tornarTexto(Aplicacao.contexto, "Carregando...",
					ToastPadrao.DURACAO_LONGA, ToastPadrao.TOAST_NOTIFICACAO)
					.show();
			
			User usuarioLogado = Aplicacao.fachada.getUserME();
			Aplicacao.setUsuario(usuarioLogado);

			Notification notifUsuarioLogado = AuxiliarNotificacao
					.criarNotificacao(Aplicacao.contexto, R.drawable.icon,
							"Obrigado! Você está logado agora", "Olá " + usuarioLogado.getFirstName() + " " + usuarioLogado.getLastName(),
							"Clique aqui para ver sua tela principal",
							MainMenuActivity.class);

			int idNotifUsuarioLogado = AuxiliarNotificacao.notificar(
					Aplicacao.contexto, notifUsuarioLogado);

			Intent intentExtras = new Intent();
			intentExtras.putExtra("idNotifUsuarioLogado", idNotifUsuarioLogado);
			
			trocarTela(MuralPostagensActivity.class, intentExtras, true);
		} catch (Exception e) {
			Log.e(Aplicacao.NOME_APP, e.getMessage());

			ToastPadrao.tornarTexto(Aplicacao.contexto,
					"Erro ao tentar se conectar ao serviço",
					ToastPadrao.DURACAO_LONGA, ToastPadrao.TOAST_ERRO).show();
		}

		super.onNewIntent(intent);
	}
}