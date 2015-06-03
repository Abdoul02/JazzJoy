package com.csform.businessportfolio;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnDismissListener;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.csform.businessportfolio.adapter.LeftMenuAdapter;
import com.csform.businessportfolio.fragment.AboutUsFragment;
import com.csform.businessportfolio.fragment.EventsFragment;
import com.csform.businessportfolio.fragment.OurTeamFragment;
import com.csform.businessportfolio.fragment.PortfolioFragment;
import com.csform.businessportfolio.fragment.TicketsFragment;
import com.csform.businessportfolio.fragment.WebViewFragment;
import com.csform.businessportfolio.model.Category;
import com.csform.businessportfolio.model.Client;
import com.csform.businessportfolio.model.LeftMenuItem;
import com.csform.businessportfolio.model.OurTeam;
import com.csform.businessportfolio.syncdata.SyncData;


public class MainActivity extends BaseActivity {



	private Dialog mSplashScreenDialog;
	private boolean mShouldSetContentView = true;

	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private ActionBarDrawerToggle mDrawerToggle;
	private List<LeftMenuItem> mLeftMenuItems;

	private String mTitle;

	private SyncData mSyncData;
	//private SyncOfflineData mSyncData;
	private ArrayList<OurTeam> mOurTeams;
	private ArrayList<Client> mClients;
	private ArrayList<Category> mCategories;
	
	private boolean mShouldFinish = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setMainViews();
		
		mSyncData = new SyncData(this);
//		mSyncData = new SyncOfflineData(this);
		mSyncData.execute();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (mSyncData != null) {
			mSyncData.cancel(true);
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public void showSplashScreen() {
		mSplashScreenDialog = new Dialog(this, R.style.SplashScreenStyle);
		mSplashScreenDialog.setContentView(R.layout.splash_screen);
		mSplashScreenDialog.show();
		
		//TextView welcomeText = (TextView) mSplashScreenDialog.findViewById(R.id.splash_screen_welcome);
		//welcomeText.setTypeface(sRobotoThin);
		
		mSplashScreenDialog.setOnCancelListener(new OnCancelListener() {

			@Override
			public void onCancel(DialogInterface dialogInterface) {
				mShouldSetContentView = false;
				finish();
			}
		});
		mSplashScreenDialog.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss(DialogInterface dialog) {
				if (mShouldSetContentView) {
					//TODO This is the first selected tab after app is opened
					selectItem(LeftMenuItem.LEFT_MENU_EVENTS);
				}
			}
		});
	}

	@Override
	public void onResume() {
		super.onResume();

	}

	@Override
	public void onPause() {

		super.onPause();
	}

	@SuppressLint("NewApi")
	private void setMainViews() {
		setContentView(R.layout.main_activity);

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
			getActionBar().setHomeButtonEnabled(true);
		}


		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ListView) findViewById(R.id.left_drawer);
		mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
		mDrawerLayout.setFocusableInTouchMode(false);
		initLeftMenuItem();
		mDrawerList.setAdapter(new LeftMenuAdapter(this, mLeftMenuItems));

		mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
				R.drawable.ic_drawer, R.string.drawer_open,
				R.string.drawer_close) {
			public void onDrawerClosed(View view) {
				super.onDrawerClosed(view);
				setMainTitle(mTitle);
				supportInvalidateOptionsMenu();
				mShouldFinish = false;
			}

			public void onDrawerOpened(View drawerView) {
				super.onDrawerOpened(drawerView);
				setMainTitle(getResources().getString(R.string.app_name));
				supportInvalidateOptionsMenu();
			}
		};
		mDrawerLayout.setDrawerListener(mDrawerToggle);
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onBackPressed() {
		if (!mShouldFinish && !mDrawerLayout.isDrawerOpen(mDrawerList)) {
			makeToast(R.string.confirm_exit);
			mDrawerLayout.openDrawer(mDrawerList);
			mShouldFinish = true;
		} else if (!mShouldFinish && mDrawerLayout.isDrawerOpen(mDrawerList)) {
			mDrawerLayout.closeDrawer(mDrawerList);
		} else {
			super.onBackPressed();
		}
	}

	private void initLeftMenuItem() {
		//TODO You can remove any line of code if you don't have info about certain category,
		//or you can reorder them as you want
		mLeftMenuItems = new ArrayList<LeftMenuItem>();
		mLeftMenuItems.add(new LeftMenuItem(R.drawable.left_menu_about_us, getString(R.string.about_us), LeftMenuItem.LEFT_MENU_ABOUT));
		mLeftMenuItems.add(new LeftMenuItem(R.drawable.left_menu_our_team, getString(R.string.lineup), LeftMenuItem.LEFT_MENU_OUR_TEAM));
		mLeftMenuItems.add(new LeftMenuItem(R.drawable.left_menu_clients, getString(R.string.events), LeftMenuItem.LEFT_MENU_EVENTS));
		mLeftMenuItems.add(new LeftMenuItem(R.drawable.left_menu_tickets, getString(R.string.tickets), LeftMenuItem.LEFT_MENU_TICKETS));
		mLeftMenuItems.add(new LeftMenuItem(R.drawable.left_menu_facebook, getString(R.string.facebook), LeftMenuItem.LEFT_MENU_FACEBOOK));
		mLeftMenuItems.add(new LeftMenuItem(R.drawable.left_menu_twiter, getString(R.string.twitter), LeftMenuItem.LEFT_MENU_TWITTER));
		mLeftMenuItems.add(new LeftMenuItem(R.drawable.left_menu_linkedin, getString(R.string.Instagram), LeftMenuItem.LEFT_MENU_LINKED_IN));


	}

	private class DrawerItemClickListener implements
			ListView.OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			selectItem(id);
			parent.setSelection(position);
		}
	}

	private void selectItem(long tag) {
		//TODO If you have added item in initLeftMenuItem(), this is the place
		//where you should add action on item click. No need to pay attention on
		//order you put items in initLeftMenuItem() method, like shown below:
		//(Facebook is in initLeftMenuItem() method added before Twitter, but
		//in this method it is added after Twitter, but ordering in left menu
		//is still: Facebook before Twitter)
		mDrawerLayout.closeDrawer(mDrawerList);
		Fragment fragment = AboutUsFragment.newInstance();
		if (tag == LeftMenuItem.LEFT_MENU_ABOUT) {
			fragment = AboutUsFragment.newInstance();
			mTitle = getString(R.string.about_us);
		} else if (tag == LeftMenuItem.LEFT_MENU_EVENTS) {
			fragment = EventsFragment.newInstance();
			mTitle = getString(R.string.events);
		} else if (tag == LeftMenuItem.LEFT_MENU_TICKETS) {
			fragment = TicketsFragment.newInstance();
			mTitle = getString(R.string.tickets);
		} else if (tag == LeftMenuItem.LEFT_MENU_OUR_TEAM) {
			fragment = OurTeamFragment.newInstance(mOurTeams);
			mTitle = getString(R.string.lineup);
		} else if (tag == LeftMenuItem.LEFT_MENU_PORTFOLIO) {
			fragment = PortfolioFragment.newInstance(mCategories);
			mTitle = getString(R.string.menu);
		} else if (tag == LeftMenuItem.LEFT_MENU_FACEBOOK) {
			fragment = WebViewFragment.newInstance("https://www.facebook.com/standardbankjoyofjazz");
			mTitle = getString(R.string.facebook);
		} else if (tag == LeftMenuItem.LEFT_MENU_LINKED_IN) {
			fragment = WebViewFragment.newInstance("http://www.instagram.com/joyofjazz_");
			mTitle = getString(R.string.Instagram);
		} else if (tag == LeftMenuItem.LEFT_MENU_FLICKR) {
			fragment = WebViewFragment.newInstance("http://www.joyofjazz.co.za");
			mTitle = getString(R.string.hospitality);
		}
		getSupportFragmentManager()
				.beginTransaction()
				.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
				.replace(R.id.content_frame, fragment)
				.commitAllowingStateLoss();
	}

	public void hideSplashScreen() {
		if (mSplashScreenDialog != null) {
			mSplashScreenDialog.dismiss();
			mSplashScreenDialog = null;
		}
	}

	@SuppressLint("NewApi")
	public void setMainTitle(String title) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().setTitle(title);
		} else {
			setTitle(title);
		}
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		mDrawerToggle.onConfigurationChanged(newConfig);
	}

	public void setClients(ArrayList<Client> clients) {
		mClients = clients;
	}

	public void setOurTeams(ArrayList<OurTeam> ourTeam) {
		mOurTeams = ourTeam;
	}

	public void setCategories(ArrayList<Category> categories) {
		mCategories = categories;
	}
}
