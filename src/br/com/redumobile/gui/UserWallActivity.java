package br.com.redumobile.gui;

import eu.erikw.PullToRefreshListView;
import eu.erikw.PullToRefreshListView.OnRefreshListener;
import greendroid.widget.ActionBar;
import greendroid.widget.ActionBarItem;
import greendroid.widget.LoaderActionBarItem;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import br.com.redumobile.R;
import br.com.redumobile.ReduMobile;
import br.com.redumobile.adapter.StatusAdapter;
import br.com.redumobile.entity.Status;
import br.com.redumobile.entity.User;
import br.com.redumobile.oauth.ReduClient;
import br.com.redumobile.util.DaemonThread;

public final class UserWallActivity extends WallActivity {
	private ReduMobile application;
	private static ReduClient client;
	private boolean firstStart;
	private View footerView;
	private final int ITEM_COMPOSE = 2;
	private final int ITEM_GO_HOME = 0;
	private final int ITEM_UPDATE = 1;
	private LoaderActionBarItem itemUpdate;
	private PullToRefreshListView listTimeline;
	private StatusAdapter listTimelineAdapter;
	private volatile boolean loadingMore;
	private final int MAX_NUM_PAGES = 25;
	private final int MENU_LOGOUT = 0;
	private volatile int page;
	private ArrayList<Status> timeline;
	private boolean timelineLoaded;
	private static String userFullName;
	private static String userId;

	private void auxUpdateStatuses(final int page) {
		new DaemonThread(new Runnable() {
			@Override
			public void run() {
				boolean loadMore = true;

				ArrayList<Status> newStatuses = client.getUserTimeline(userId,
						page, false);
				if (newStatuses != null) {
					for (int i = 0; i < newStatuses.size(); i++) {
						Status status = newStatuses.get(i);
						Status lastStatus = timeline.get(0);
						if (status.getUpdatedAt().before(
								lastStatus.getUpdatedAt())) {
							loadMore = false;

							break;
						} else {
							for (int j = 0; j < timeline.size(); j++) {
								Status currentStatus = timeline.get(i);
								if (currentStatus.getId() == status.getId()) {
									timeline.remove(currentStatus);

									break;
								}
							}
							timeline.add(0, status);
						}
					}
				} else {
					loadMore = false;

					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							Toast.makeText(
									UserWallActivity.this,
									"Algumas informações não puderam ser obtidas",
									Toast.LENGTH_LONG).show();
						}
					});
				}

				if (loadMore) {
					auxUpdateStatuses(page + 1);
				} else {
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							listTimelineAdapter.notifyDataSetChanged();

							itemUpdate.setLoading(false);
							listTimeline.onRefreshComplete();
						}
					});
				}
			}
		}).start();
	}

	@Override
	protected void loadBreadcrumb() {
		new DaemonThread(new Runnable() {
			@Override
			public void run() {
				if (userFullName == null) {
					User user = client.getUser(userId);
					if (user == null) {
						runOnUiThread(new Runnable() {
							@Override
							public void run() {
								Toast.makeText(
										UserWallActivity.this,
										"Algumas informações não puderam ser obtidas",
										Toast.LENGTH_LONG).show();
							}
						});
					} else {
						userFullName = user.getFirstName() + " "
								+ user.getLastName();
					}
				}

				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						if (userFullName != null) {
							ImageView imgUser = (ImageView) findViewById(R.id.userWallActivityImgUser);
							imgUser.setVisibility(View.VISIBLE);

							TextView lblFullName = (TextView) findViewById(R.id.userWallActivityLblFullName);
							lblFullName.setText(userFullName);
						}
					}
				});
			}
		}).start();
	}

	@Override
	protected void loadStatuses() {
		new DaemonThread(new Runnable() {
			@Override
			public void run() {
				if (page > MAX_NUM_PAGES) {
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							listTimeline.removeFooterView(footerView);

							page = 0;
						}
					});
				} else if (page > 0) {
					loadingMore = true;

					final ArrayList<Status> newStatuses = client
							.getUserTimeline(userId, page++, false);

					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							if (newStatuses != null) {
								timeline.addAll(newStatuses);

								listTimelineAdapter.notifyDataSetChanged();
							} else {
								Toast.makeText(
										UserWallActivity.this,
										"Infelizmente este usuário não permite que seu mural seja visualizado!",
										Toast.LENGTH_LONG).show();
								
								listTimeline.removeFooterView(footerView);
								page = 0;
							}

							if (!timelineLoaded) {
								itemUpdate.setLoading(false);
								listTimeline.onRefreshComplete();
								timelineLoaded = true;
							}
							loadingMore = false;
						}
					});
				}
			}
		}).start();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setActionBarContentView(R.layout.user_wall_activity);

		getAB().setType(ActionBar.Type.Empty);

		application = ReduMobile.getInstance();

		client = application.getClient();

		userId = getIntent().getStringExtra("userId");
		if (userId == null) {
			int id = getIntent().getIntExtra("userId", -1);
			if (id == -1) {
				throw new RuntimeException("A id do usuário não foi informada");
			} else {
				userId = String.valueOf(id);
			}
		} else {
			if (!userId.equals(application.getUserLogin())) {
				addActionBarItem(ActionBarItem.Type.GoHome, ITEM_GO_HOME);
			}
		}
		userFullName = getIntent().getStringExtra("userFullName");

		loadBreadcrumb();

		timeline = new ArrayList<Status>();

		listTimelineAdapter = new StatusAdapter(this, timeline);

		footerView = LayoutInflater.from(this).inflate(
				R.layout.list_statuses_footer, null);

		listTimeline = (PullToRefreshListView) findViewById(R.id.userWallActivityListTimeline);
		listTimeline.addFooterView(footerView);
		listTimeline.setAdapter(listTimelineAdapter);
		listTimeline.setOnScrollListener(new OnScrollListener() {
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				int lastInScreen = firstVisibleItem + visibleItemCount;
				if (lastInScreen == totalItemCount && !loadingMore) {
					loadStatuses();
				}
			}

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
			}
		});
		
		listTimeline.setOnRefreshListener(new OnRefreshListener() {
			
			@Override
			public void onRefresh() {
				updateStatuses();
			}
			
			
		});

		firstStart = true;

		page = 1;

		loadStatuses();

		itemUpdate = (LoaderActionBarItem) addActionBarItem(
				ActionBarItem.Type.Refresh, ITEM_UPDATE);
		itemUpdate.setLoading(false);
		listTimeline.onRefreshComplete();
		addActionBarItem(ActionBarItem.Type.Compose, ITEM_COMPOSE);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);

		MenuItem menuLogout = menu.add(0, MENU_LOGOUT, 0, "Desconectar");
		menuLogout.setIcon(R.drawable.logout_icon);

		return true;
	}

	@Override
	public boolean onHandleActionBarItemClick(ActionBarItem item, int position) {
		boolean clickHandled = true;

		int itemId = item.getItemId();
		if (itemId == ITEM_GO_HOME) {
			Intent intent = new Intent(this, UserWallActivity.class);
			intent.putExtra("userId", application.getUserLogin());

			startActivity(intent);
		} else if (itemId == ITEM_UPDATE) {
			updateStatuses();
		} else if (itemId == ITEM_COMPOSE) {
			Intent intent = new Intent(this, PostOnUserWallActivity.class);
			intent.putExtra("userId", userId);
			intent.putExtra("userFullName", userFullName);

			startActivity(intent);
		} else {
			clickHandled = super.onHandleActionBarItemClick(item, position);
		}

		return clickHandled;
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		super.onMenuItemSelected(featureId, item);

		int itemId = item.getItemId();
		if (itemId == MENU_LOGOUT) {
			application.deleteUserDate();

			sendBroadcast(new Intent(
					DestructionIntent.ACTION_DESTROYING_ACTIVITY));
			startActivity(new Intent(this, LoginActivity.class));
		}

		return false;
	}

	@Override
	protected void onStart() {
		super.onStart();

		if (firstStart) {
			firstStart = false;
		} else {
			updateStatuses();
		}
	}

	@Override
	protected void updateStatuses() {
		itemUpdate.setLoading(true);

		auxUpdateStatuses(1);
	}
}