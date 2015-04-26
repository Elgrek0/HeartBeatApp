package upatras.heartbeatapp;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Random;

import database.DatabaseAccess;
import exceptions.NoDataException;
import exceptions.ParameterLengthMismatch;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.support.v4.app.TaskStackBuilder;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;

public class HeartBeatSpectrum extends View{

	Paint g;
	Rect bg_Rect;
	int width,height;
	LinkedList<Rect> rects;
	int[] pallete = generateColors(2000);
	boolean rendering = true;
	Random rand;
	int bufferSize = 200;
	DatabaseAccess db;
	
	public HeartBeatSpectrum(Context context) {
		super(context);
        db = new DatabaseAccess(context,"HeartBeat");
        String[] var_names={"id" ,"frequency","strength" ,"timems" ,"date"};
        Class[] types={Integer.class,Float.class,Integer.class,Integer.class,Integer.class};
        try {
            db.createtable("Beats",var_names,types);
        } catch (ParameterLengthMismatch parameterLengthMismatch) {
            parameterLengthMismatch.printStackTrace();
        }
		initDraw();
	}
	
	public HeartBeatSpectrum(Context context,AttributeSet attr){
		super(context,attr);
		db = new DatabaseAccess(context,"HeartBeat");
        String[] var_names={"id" ,"frequency","strength" ,"timems" ,"date"};
        Class[] types={Integer.class,Float.class,Integer.class,Integer.class,Integer.class};
        try {
            db.createtable("Beats",var_names,types);
        } catch (ParameterLengthMismatch parameterLengthMismatch) {
            parameterLengthMismatch.printStackTrace();
        }
        initDraw();
    }
	
	public HeartBeatSpectrum(Context context,AttributeSet attr,int defStyle){
		super(context,attr,defStyle);
        db = new DatabaseAccess(context,"HeartBeat");
        String[] var_names={"id" ,"frequency","strength" ,"timems" ,"date"};
        Class[] types={Integer.class,Float.class,Integer.class,Integer.class,Integer.class};
        try {
            db.createtable("Beats",var_names,types);
        } catch (ParameterLengthMismatch parameterLengthMismatch) {
            parameterLengthMismatch.printStackTrace();
        }
		initDraw();
	}
	
	public void initDraw(){
		g = new Paint(Paint.ANTI_ALIAS_FLAG);
		DisplayMetrics displayMetrics = new DisplayMetrics();
		((Activity) getContext()).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
		width = displayMetrics.widthPixels;
		height = displayMetrics.heightPixels;
		bg_Rect = new Rect(0,0,width,height);
		
		rects = new LinkedList<Rect>();
		rand = new Random();
		for(int i=0;i<bufferSize;i++){
			rects.add(new Rect(i*3,4*height/5-RandomUtils.randomInt((int)(height*0.5)),i*3+1,4*height/5));
		}
		
		
		
		
		render();
		
	}
	
	public void render(){
		
		
		
		Thread t = new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				int cntr=0;
				while(rendering){
					
					
					for(int i=0;i<rects.size();i++){
						float strength = rand.nextInt((int)(height*0.5));
						rects.set(i,new Rect(i*3,4*height/5-(int)strength,i*3+1,4*height/5));
						//rects[i] = new Rect(i*3,4*height/5-rand.nextInt((int)(height*0.5)),i*3+1,4*height/5);
						db.commitNewHBSample(strength);
						//cntr++;
						//System.out.println("Sample:"+cntr);
						postInvalidate();
						try {
							synchronized (this){wait(rand.nextInt(200)+800);}
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
					}
					
					
					
					
					
				}
			}
		});
		t.start();
		
		
		
	}
	
	@Override
	protected void onDraw(Canvas c){
		
		g.setColor(Color.BLACK);
		g.setStyle(Style.FILL);
		c.drawRect(bg_Rect, g);
		
		
		g.setStyle(Style.STROKE);
		/*for(int i=0;i<rects.size();i++){
			g.setColor(pallete[(int)(i*2)%pallete.length]);
			c.drawRect(rects.get(i), g);
		}*/
		

				float min = 40,max=60;
				float inc = (max-min);
				
				//long time = System.currentTimeMillis();

        try {
            Object []datat;
            datat = db.getData(30, "Beats", Float.class, "frequency");
            Float[] data = Arrays.copyOf(datat, datat.length, Float[].class);
            //System.out.println(System.currentTimeMillis()-time);
            for (int i = 0; i < data.length; i++) {
                g.setColor(pallete[(int) (i * 2) % pallete.length]);
                System.out.println(data[i] * 60);
                c.drawLine(i * 10, 0, i * 10, 200 * (data[i] * 60 - min) / inc, g);
                //Log.d("", msg);
            }
        }
        catch (NoDataException e) {
            e.printStackTrace();
        }
				//c.drawRect(new Rect((int)db.getSpecificData(i),height/2-100,(int)db.getSpecificData(i)+1,height/2),g);
				

		
		
	}
	

	public int[] getRGBValues(int RGBint){
		int Blue,Green,Red;
		Blue =  RGBint & 255;
		Green = (RGBint >> 8) & 255;
		Red =   (RGBint >> 16) & 255;
		int[] values = {Red,Green,Blue};
		return values;
	}
	
	public int getRGB(int RGBint){
		int Blue,Green,Red;
		Blue =  RGBint & 255;
		Green = (RGBint >> 8) & 255;
		Red =   (RGBint >> 16) & 255;
		
		return Color.rgb(Red, Green, Blue);
	}
	
	public int getRGB2(int color){
		int rgb;
		rgb = Color.red(color);
		rgb = (rgb << 8) + Color.green(color);
		rgb = (rgb << 8) + Color.blue(color);
		return rgb;
	}
	
	public int[] generateColors(int n)
	{
		
		int[] cols = new int[n];
		float[] hsv = new float[3];
		for(int i = 0; i < n; i++)
		{
			
			//cols[i] = Color.getHSBColor(((float) Math.log10(i) / (float) Math.log10(n)), 0.85f, 1.0f);
			hsv[0] = i/2;
			hsv[1] = 0.75f;
			hsv[2] = 1.0f;
			cols[i] = Color.HSVToColor(hsv);
		}
		return cols;
	}
	
}
