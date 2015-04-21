package com.sensor.mobile;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;

public class Ayuda extends FragmentActivity {
	PagerSlidingTabStrip tabs;
	ViewPager pager;
	private MyPagerAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ayuda);
		String[] TITLES = getIntent().getStringArrayExtra("TITLES"); // Recojo
																		// el
																		// parámetro
																		// pasado
		tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
		pager = (ViewPager) findViewById(R.id.pager);
		adapter = new MyPagerAdapter(getSupportFragmentManager(), TITLES);
		pager.setAdapter(adapter);
		tabs.setBackgroundResource(R.drawable.botonamarillo);// Color del fondo
																// de las tabs
		tabs.setTextColor(getResources().getColor(android.R.color.black)); // Color
																			// de
																			// la
																			// letra
																			// de
																			// las
																			// tabs
		tabs.setAllCaps(false);
		tabs.setViewPager(pager);
		final int pageMargin = (int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, 4, getResources()
						.getDisplayMetrics());
		pager.setPageMargin(pageMargin);
	}

	public class MyPagerAdapter extends FragmentStatePagerAdapter {

		String[] TITLES;

		public MyPagerAdapter(FragmentManager fm, String[] titles) {
			super(fm);
			this.TITLES = titles;
		}

		@Override
		public int getItemPosition(Object object) {
			return POSITION_NONE;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return TITLES[position];
		}

		@Override
		public int getCount() {
			return TITLES.length;
		}

		@Override
		public Fragment getItem(int position) {
			return AyudaFragmento.newInstance(position, TITLES[position]);
		}
	}
}
