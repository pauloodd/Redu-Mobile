package br.com.redumobile.auxiliar;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

public class AuxiliarNotificacao {
	public static void cancelarNotificacao(Context contexto, int idNotificacao) {
		NotificationManager gerencNotificacao = (NotificationManager) contexto
				.getSystemService(Context.NOTIFICATION_SERVICE);
		gerencNotificacao.cancel(idNotificacao);
	}

	public static Notification criarNotificacao(Context contexto,
			int idRecursoIcone, String mensagemBarraStatus, String titulo,
			String mensagem, Class<? extends Activity> classeActivity) {
		Notification notificacao = new Notification(idRecursoIcone,
				mensagemBarraStatus, System.currentTimeMillis());

		PendingIntent i = PendingIntent.getActivity(contexto, 0, new Intent(
				contexto, classeActivity), 0);

		notificacao.setLatestEventInfo(contexto, titulo, mensagem, i);
		
		return notificacao;
	}

	public static int notificar(Context contexto, Notification notificacao) {
		int idNotificacao = (int) System.currentTimeMillis();

		NotificationManager gerencNotificacao = (NotificationManager) contexto
				.getSystemService(Context.NOTIFICATION_SERVICE);
		gerencNotificacao.notify(idNotificacao, notificacao);

		return idNotificacao;
	}
	
	public static void cancelarTodasNotificacao(Context contexto) {
		NotificationManager gerencNotificacao = (NotificationManager) contexto
				.getSystemService(Context.NOTIFICATION_SERVICE);
		gerencNotificacao.cancelAll();
	}
}
