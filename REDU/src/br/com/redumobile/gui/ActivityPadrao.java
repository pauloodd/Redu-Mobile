package br.com.redumobile.gui;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;

public class ActivityPadrao extends Activity {
	ProgressDialog  progressDialog;
	
	protected void iniciarActivity(Intent intent, boolean fecharAtual) {
		startActivity(intent);
		if (fecharAtual == true) {
			finish();
		}
	}

	protected void trocarTela(Class<? extends Activity> classeProxActivity,
			boolean fecharAtual) {
		trocarTela(classeProxActivity, null, fecharAtual);
	}

	protected void trocarTela(Class<? extends Activity> classeProxActivity,
			Intent intentExtras, boolean fecharAtual) {
		Intent i = new Intent(this, classeProxActivity);
		if (intentExtras != null) {
			i.putExtras(intentExtras);
		}
		iniciarActivity(i, fecharAtual);
	}

	protected void trocarTela(String acao, Uri informacoes, boolean fecharAtual) {
		Intent i = new Intent(acao, informacoes);
		iniciarActivity(i, fecharAtual);
	}
	

}