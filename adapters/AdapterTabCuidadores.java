package com.Notifications.patientssassistant.adapters;


import com.Notifications.patientssassistant.R;
import com.Notifications.patientssassistant.fragments.*;
import java.util.Locale;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.support.v13.app.FragmentPagerAdapter;


public class AdapterTabCuidadores extends FragmentPagerAdapter {
	
	Context context;
	
	public AdapterTabCuidadores(FragmentManager fm, Context context) {
		super(fm);		
		this.context = context;
	}
	
	@Override
	public Fragment getItem(int index) {
		TabCuidadoresF pag=new TabCuidadoresF();
		if(index < 4) {
			switch(index) {
			case 0:				
				return pag.EnviarParametrosFragTabCui1();
			case 1:				
				return pag.EnviarParametrosFragTabCui2();
			case 2:				
				return pag.EnviarParametrosFragTabCui3();			
			}
		}
		return null;		
	}

	@Override
	public int getCount() {
		return 3;
	}
	
	@Override
	public CharSequence getPageTitle(int position) {
		Locale l = Locale.getDefault();
		switch (position) {
		case 0:
			return context.getResources().getString(R.string.DatoPers).toUpperCase(l); 
		case 1:
			return context.getResources().getString(R.string.HoraTrab).toUpperCase(l);
		case 2:
			return context.getResources().getString(R.string.Permisos).toUpperCase(l);
		}
		return null;
	}
	
	
}