package com.Notifications.patientssassistant.adapters;


import java.util.Locale;

import com.Notifications.patientssassistant.R;
import com.Notifications.patientssassistant.fragments.*;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.support.v13.app.FragmentPagerAdapter;


public class AdapterTabAgendaPaciente extends FragmentPagerAdapter {
	
	Context context;
	
	public AdapterTabAgendaPaciente(FragmentManager fm, Context context) {
		super(fm);	
		this.context=context;
	}
	
	@Override
	public Fragment getItem(int index) {
		TabAgendaPacientesF pag=new TabAgendaPacientesF();
		if(index < 3) {
			switch(index) {
			case 0:				
				return pag.EnviarParametrosFragTabAgePac1();
			case 1:				
				return pag.EnviarParametrosFragTabAgePac2();						
			}
		}
		return null;		
	}

	@Override
	public int getCount() {
		return 2;
	}
	
	@Override
	public CharSequence getPageTitle(int position) {
		Locale l = Locale.getDefault();
		switch (position) {
		case 0:
			return context.getResources().getString(R.string.Eventos).toUpperCase(l); 
		case 1:
			return context.getResources().getString(R.string.Rutinas).toUpperCase(l);
		}
		return null;
	}
	
	
}