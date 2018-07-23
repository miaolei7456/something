package com.example.move;


import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Bitmap.Config;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.widget.Toast;
 
public class MainActivity extends Activity implements
		android.view.GestureDetector.OnGestureListener {
	// �������Ƽ����ʵ��
	GestureDetector detector;
	ImageView imageView;
	String path;
	int desiredMinimumWidth;
	int desiredMinimumHeight;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		desiredMinimumWidth = dm.widthPixels;
		desiredMinimumHeight = dm.heightPixels;
		
		// �������Ƽ����
		detector = new GestureDetector(this, this);
		imageView = (ImageView)findViewById(R.id.image1);
		
		path = getPath();
        Bitmap bitmap = BitmapFactory.decodeFile(path);
        Bitmap bmp = zoomImg(bitmap, desiredMinimumWidth, desiredMinimumHeight);
		Bitmap bmpShow = Bitmap.createBitmap(desiredMinimumWidth, desiredMinimumHeight, Config.ARGB_8888);
		bmpShow.eraseColor(0x00000000);
		
		int []pixels = new int[bmp.getWidth()*bmp.getHeight()*4];
		bmp.getPixels(pixels, 0, bmp.getWidth(), 0, 0, bmp.getWidth(), bmp.getHeight());
		if(desiredMinimumHeight-bmp.getHeight() > 0)
			bmpShow.setPixels(pixels, 0, desiredMinimumWidth, 0, (desiredMinimumHeight-bmp.getHeight())/2, bmp.getWidth(), bmp.getHeight());
		else
			bmpShow.setPixels(pixels, 0, bmp.getWidth(), (desiredMinimumWidth-bmp.getWidth())/2, 0, bmp.getWidth(), bmp.getHeight());
		imageView.setImageBitmap(bmpShow);
	}
 
	// ����activity�ϵĴ����¼�����GestureDetector����
	public boolean onTouchEvent(MotionEvent me) {
		return detector.onTouchEvent(me);
	}
 
	@Override
	public boolean onDown(MotionEvent arg0) {
		return false;
	}
 
	/**
	 * �������
	 * 
	 */
	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		float minMove = 120; // ��С��������
		float minVelocity = 0; // ��С�����ٶ�
		float beginX = e1.getX();
		float endX = e2.getX();
		float beginY = e1.getY();
		float endY = e2.getY();
        int rating = 0;
        
		if (beginX - endX > minMove && Math.abs(velocityX) > minVelocity) { // ��
			rating = 3;
			Toast.makeText(this,  "3", Toast.LENGTH_SHORT).show();
		} else if (endX - beginX > minMove && Math.abs(velocityX) > minVelocity) { // �һ�
			rating = 2;
			Toast.makeText(this,  "2", Toast.LENGTH_SHORT).show();
		} else if (beginY - endY > minMove && Math.abs(velocityY) > minVelocity) { // �ϻ�
			rating = 1;
			Toast.makeText(this,  "1", Toast.LENGTH_SHORT).show();
		} else if (endY - beginY > minMove && Math.abs(velocityY) > minVelocity) { // �»�
			rating = 4;
			Toast.makeText(this,  "4", Toast.LENGTH_SHORT).show();
		}
		rating(path, rating);
		
		path = getPath();
        Bitmap bitmap = BitmapFactory.decodeFile(path);
        Bitmap bmp = zoomImg(bitmap, desiredMinimumWidth, desiredMinimumHeight);
		Bitmap bmpShow = Bitmap.createBitmap(desiredMinimumWidth, desiredMinimumHeight, Config.ARGB_8888);
		bmpShow.eraseColor(0x00000000);
		
		int []pixels = new int[bmp.getWidth()*bmp.getHeight()*4];
		bmp.getPixels(pixels, 0, bmp.getWidth(), 0, 0, bmp.getWidth(), bmp.getHeight());
		if(desiredMinimumHeight-bmp.getHeight() > 0)
			bmpShow.setPixels(pixels, 0, desiredMinimumWidth, 0, (desiredMinimumHeight-bmp.getHeight())/2, bmp.getWidth(), bmp.getHeight());
		else
			bmpShow.setPixels(pixels, 0, bmp.getWidth(), (desiredMinimumWidth-bmp.getWidth())/2, 0, bmp.getWidth(), bmp.getHeight());
		imageView.setImageBitmap(bmpShow);
		
		return false;
	}
 
	@Override
	public void onShowPress(MotionEvent arg0) {
		// TODO Auto-generated method stub
 
	}
 
	@Override
	public boolean onSingleTapUp(MotionEvent arg0) {
		// TODO Auto-generated method stub
		return false;
	}
 
	@Override
	public void onLongPress(MotionEvent arg0) {
		// TODO Auto-generated method stub
 
	}
 
	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
 
		return false;
	}

	public native String getPath();
	public native int rating(String path, int rating);

    static {
        System.loadLibrary("get_path");
    }
    
    public static Bitmap zoomImg(Bitmap bm, int newWidth ,int newHeight){
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height; 
        float scale = (scaleWidth < scaleHeight)?scaleWidth:scaleHeight;
        
        Matrix matrix = new Matrix();
        matrix.postScale(scale, scale);
        Bitmap newbm = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, true);
        
        Log.v("mlwp", "new width: " + newbm.getWidth() + " height: " + newbm.getHeight());
        
        return newbm;
     }
}
