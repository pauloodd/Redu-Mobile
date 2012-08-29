package br.com.redumobile.gui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import br.com.redumobile.R;
import br.com.redumobile.auxiliar.AuxiliarMatematica;

public class ToastPadrao {
	public static final int DURACAO_CURTA = Toast.LENGTH_SHORT;
	public static final int DURACAO_LONGA = Toast.LENGTH_LONG;
	public static final int TOAST_ATENCAO = 4;
	public static final int TOAST_ERRO = 2;
	public static final int TOAST_NOTIFICACAO = 1;
	public static final int TOAST_SUCESSO = 3;

	public static Toast tornarTexto(Context contexto, String msg, int duracao,
			int tipo) throws RuntimeException {
		duracao = (int) AuxiliarMatematica.clamp(duracao, DURACAO_CURTA,
				DURACAO_LONGA);

		LayoutInflater inflater = (LayoutInflater) contexto
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		Toast toast = null;

		if (tipo >= TOAST_NOTIFICACAO && tipo <= TOAST_ATENCAO) {
			int idLayout;
			if (tipo == TOAST_NOTIFICACAO) {
				idLayout = R.layout.toastpadraonotificacao;
			} else if (tipo == TOAST_ERRO) {
				idLayout = R.layout.toastpadraoerro;
			} else if (tipo == TOAST_SUCESSO) {
				idLayout = R.layout.toastpadraosucesso;
			} else {
				idLayout = R.layout.toastpadraoatencao;
			}

			View layout = inflater.inflate(idLayout, null);
			((TextView) layout.findViewById(R.id.toastPadraoEtqMsg))
					.setText(msg);

			toast = new Toast(contexto);
			toast.setView(layout);
			toast.setDuration(duracao);
		} else {
			throw new RuntimeException("O tipo de toast informado é inválido!");
		}

		return toast;
	}
}