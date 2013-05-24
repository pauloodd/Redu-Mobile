package br.com.redumobile.gui;

import greendroid.app.GDActivity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

public abstract class WallActivity extends GDActivity {
	public static final class DestructionIntent {
		public static final String ACTION_DESTROYING_ACTIVITY = "DESTROYING_ACTIVITY";
	}

	private final class DestructionReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			finish();
		}
	}

	private DestructionReceiver destructionReceiver;

	protected abstract void loadBreadcrumb();

	protected abstract void loadStatuses();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		destructionReceiver = new DestructionReceiver();

		registerReceiver(destructionReceiver, new IntentFilter(
				DestructionIntent.ACTION_DESTROYING_ACTIVITY));
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

		unregisterReceiver(destructionReceiver);
	}

	protected abstract void updateStatuses();
}
