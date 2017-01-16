package com.Notifications.patientssassistant;


import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;


public class MetodosValidacionesExtras {

	private static final String PATTERN_EMAIL = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
	private static String diaSemana="", mesAnio="";
	private static int day, year;


	//VALIDAR EMAIL
	public static boolean validateEmail(String email) {
		Pattern pattern = Pattern.compile(PATTERN_EMAIL);
		Matcher matcher = pattern.matcher(email);
		return matcher.matches();
	}


	//METODOS PARA CARGAR IMAGENES NUEVAS DESDE GALERIA

	//PASO 1. TOMAR IMAGEN
	public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {
			final int halfHeight = height / 2;
			final int halfWidth = width / 2;

			// Calculate the largest inSampleSize value that is a power of 2 and keeps both
			// height and width larger than the requested height and width.
			while ((halfHeight / inSampleSize) > reqHeight && (halfWidth / inSampleSize) > reqWidth) {
				inSampleSize *= 2;
			}
		}
		return inSampleSize;
	}

	public static Bitmap decodeSampledBitmapFromResource(Uri resId, Context context, int reqWidth, int reqHeight) throws Exception {
		// First decode with inJustDecodeBounds=true to check dimensions
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeStream(context.getContentResolver().openInputStream(resId), null, options);

		// Calculate inSampleSize
		options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeStream(context.getContentResolver().openInputStream(resId), null, options);
	}

	//PASO 2. LA IMAGEN ANTERIOR ES REDIMENSIONADA
	public static Bitmap redimensionarImagenMaximo(Bitmap mBitmap, float newWidth, float newHeigth){
		int width = mBitmap.getWidth();
		int height = mBitmap.getHeight();
		float scaleWidth = ((float) newWidth) / width;
		float scaleHeight = ((float) newHeigth) / height;
		Matrix matrix = new Matrix();
		matrix.postScale(scaleWidth, scaleHeight);
		return Bitmap.createBitmap(mBitmap, 0, 0, width, height, matrix, false);
	}


	//QUE DIA ES
	public static String CalcularDiaDeSemana(Context context, Date fecha)
	{
		Calendar calendario = new GregorianCalendar();
		calendario.setTime(fecha);
		int dia = calendario.get(Calendar.DAY_OF_WEEK);
		day = calendario.get(Calendar.DAY_OF_MONTH);

		switch (dia) {
			case 1:diaSemana=context.getResources().getString(R.string.Dom); break;
			case 2:diaSemana=context.getResources().getString(R.string.Lun); break;
			case 3:diaSemana=context.getResources().getString(R.string.Mar); break;
			case 4:diaSemana=context.getResources().getString(R.string.Mie); break;
			case 5:diaSemana=context.getResources().getString(R.string.Jue); break;
			case 6:diaSemana=context.getResources().getString(R.string.Vie); break;
			case 7:diaSemana=context.getResources().getString(R.string.Sab); break;
			default:diaSemana=""; break;
		}
		return diaSemana;
	}


	//QUE MES ES
	public static String CalcularMesDelAnio(Context context, Date fecha)
	{
		Calendar calendario = new GregorianCalendar();
		calendario.setTime(fecha);
		int mes = calendario.get(Calendar.MONTH);
		year = calendario.get(Calendar.YEAR);

		switch (mes) {
			case 0:mesAnio=context.getResources().getString(R.string.Ene); break;
			case 1:mesAnio=context.getResources().getString(R.string.Feb); break;
			case 2:mesAnio=context.getResources().getString(R.string.Marz); break;
			case 3:mesAnio=context.getResources().getString(R.string.Abr); break;
			case 4:mesAnio=context.getResources().getString(R.string.May); break;
			case 5:mesAnio=context.getResources().getString(R.string.Jun); break;
			case 6:mesAnio=context.getResources().getString(R.string.Jul); break;
			case 7:mesAnio=context.getResources().getString(R.string.Ago); break;
			case 8:mesAnio=context.getResources().getString(R.string.Sep); break;
			case 9:mesAnio=context.getResources().getString(R.string.Oct); break;
			case 10:mesAnio=context.getResources().getString(R.string.Nov); break;
			case 11:mesAnio=context.getResources().getString(R.string.Dic); break;
			default:mesAnio=""; break;
		}
		return mesAnio;
	}


	public static String CalcularLaFecha(Context context, Date fecha) {
		String queDia = CalcularDiaDeSemana(context, fecha);
		String queMes = CalcularMesDelAnio(context, fecha);
		String laFecha=queDia+", "+queMes+" "+day+", "+year;
		return laFecha;
	}


}