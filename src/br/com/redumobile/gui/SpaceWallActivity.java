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
import br.com.redumobile.entity.Space;
import br.com.redumobile.entity.Status;
import br.com.redumobile.oauth.ReduClient;
import br.com.redumobile.util.DaemonThread;

public final class SpaceWallActivity extends WallActivity {
	private ReduMobile application;
	private ReduClient client;
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
	private volatile int page;
	private int spaceId;
	private volatile String spaceName;
	private ArrayList<Status> timeline;
	private boolean timelineLoaded;

	private void auxUpdateStatuses(final int page) {
		new DaemonThread(new Runnable() {
			@Override
			public void run() {
				boolean loadMore = true;

				ArrayList<Status> newStatuses = client.getSpaceTimeline(
						String.valueOf(spaceId), page, false);
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
									SpaceWallActivity.this,
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
				if (spaceName == null) {
					Space space = client.getSpace(String.valueOf(spaceId));
					if (space == null) {
						runOnUiThread(new Runnable() {
							@Override
							public void run() {
								Toast.makeText(
										SpaceWallActivity.this,
										"Algumas informações não puderam ser obtidas",
										Toast.LENGTH_LONG).show();
							}
						});
					} else {
						spaceName = space.getName();
					}
				}

				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						if (spaceName != null) {
							ImageView imgSpace = (ImageView) findViewById(R.id.spaceWallActivityImgSpace);
							imgSpace.setVisibility(View.VISIBLE);

							TextView lblName = (TextView) findViewById(R.id.spaceWallActivityLblName);
							lblName.setText(spaceName);
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
							.getSpaceTimeline(String.valueOf(spaceId), page++,
									false);

					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							if (newStatuses != null) {
								timeline.addAll(newStatuses);

								listTimelineAdapter.notifyDataSetChanged();
							} else {
								Toast.makeText(
										SpaceWallActivity.this,
										"Algumas informações não puderam ser obtidas",
										Toast.LENGTH_LONG).show();
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

		setActionBarContentView(R.layout.space_wall_activity);

		getAB().setType(ActionBar.Type.Empty);

		application = ReduMobile.getInstance();

		client = application.getClient();

		spaceId = getIntent().getIntExtra("spaceId", -1);
		if (spaceId == -1) {
			throw new RuntimeException("A id da disciplina não foi informada");
		}
		spaceName = getIntent().getStringExtra("spaceName");

		loadBreadcrumb();

		timeline = new ArrayList<Status>();

		listTimelineAdapter = new StatusAdapter(this, timeline);

		footerView = LayoutInflater.from(this).inflate(
				R.layout.list_statuses_footer, null);

		listTimeline = (PullToRefreshListView) findViewById(R.id.spaceWallActivityListTimeline);
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

		addActionBarItem(ActionBarItem.Type.GoHome, ITEM_GO_HOME);

		itemUpdate = (LoaderActionBarItem) addActionBarItem(
				ActionBarItem.Type.Refresh, ITEM_UPDATE);
		itemUpdate.setLoading(true);

		addActionBarItem(ActionBarItem.Type.Compose, ITEM_COMPOSE);
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
			Intent intent = new Intent(this, PostOnSpaceWallActivity.class);
			intent.putExtra("spaceId", spaceId);
			intent.putExtra("spaceName", spaceName);

			startActivity(intent);
		} else {
			clickHandled = super.onHandleActionBarItemClick(item, position);
		}

		return clickHandled;
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
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);

		MenuItem menuLogout = menu.add(0, 0, 0, "Desconectar");
		menuLogout.setIcon(R.drawable.logout_icon);

		return true;
	}
}