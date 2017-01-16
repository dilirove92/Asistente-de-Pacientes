package com.Notifications.patientssassistant.adapters;


import com.Notifications.patientssassistant.R;
import com.Notifications.patientssassistant.fragments.*;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.support.v13.app.FragmentPagerAdapter;
import java.util.Locale;


public class AdapterTabPacientes extends FragmentPagerAdapter {
	
	Context context;
	
	public AdapterTabPacientes(FragmentManager fm, Context context) {
		super(fm);	
		this.context=context;
	}
	
	@Override
	public Fragment getItem(int index) {
		TabPacientesF pag=new TabPacientesF();
		if(index < 4) {
			switch(index) {
			case 0:				
				return pag.EnviarParametrosFragTabPac1();
			case 1:				
				return pag.EnviarParametrosFragTabPac2();
			case 2:				
				return pag.EnviarParametrosFragTabPac3();			
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
			return context.getResources().getString(R.string.EstaSalu).toUpperCase(l);
		case 2:
			return context.getResources().getString(R.string.DatosFam).toUpperCase(l);
		}
		return null;
	}
	
	
}