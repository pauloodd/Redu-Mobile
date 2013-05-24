package br.com.redumobile.gui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import br.com.redumobile.R;
import br.com.redumobile.ReduMobile;

public final class LoginActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.login_activity);

		final ReduMobile application = ReduMobile.getInstance();
		ReduMobile.inicializar(getApplicationContext());
		
		if (application.retrieveUserDate()) {
			Intent intent = new Intent(this, UserWallActivity.class);
			intent.putExtra("userId", application.getUserLogin());

			startActivity(intent);
			finish();	
		} else {
			Button btnSignIn = (Button) findViewById(R.id.loginActivityBtnSignIn);
			btnSignIn.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					CheckBox chkKeepConnected = (CheckBox) findViewById(R.id.loginActivityChkKeepConnected);

					application.setKeepConnected(chkKeepConnected.isChecked());

					startActivity(new Intent(LoginActivity.this,
							OAuthCallbackActivity.class));
					finish();
				}
			});
		}
	}
}
