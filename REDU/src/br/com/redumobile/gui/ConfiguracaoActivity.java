package br.com.redumobile.gui;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.ZoomControls;
import br.com.redumobile.R;

public class ConfiguracaoActivity extends ActivityPadrao {
	
	public SeekBar sb;
	public ZoomControls zoomControlFonte;
	public TextView escolhido;
	public TextView fonte_size;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        
	 super.onCreate(savedInstanceState);
	 setContentView(R.layout.configuracao);
    
	 try {
		 
		 escolhido = (TextView)findViewById(R.id.percent);
		 sb = (SeekBar)findViewById(R.id.slider);
		 zoomControlFonte = (ZoomControls)findViewById(R.id.zoomControlsFonte);
		 fonte_size = (TextView) findViewById(R.id.fonte_size);
		 
		 sb.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			
			 @Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				
					
					escolhido.setText(Integer.toString(progress)+"m");
					
					if(progress == 0){
						escolhido.setText("Sem atualização");
					}
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				
			}
		});
		 
		zoomControlFonte.setOnZoomInClickListener(new OnClickListener() {
			        public void onClick(View v) {
			        	float size = fonte_size.getTextSize();
			        	size = size + 2;
			        	fonte_size.setTextSize(size);
			        }
			});
		zoomControlFonte.setOnZoomOutClickListener(new OnClickListener() {
			        public void onClick(View v) {
			        	float size = fonte_size.getTextSize();
			        	size = size - 2;
			        	if(size <= 6){
			        		size = 6;
			        	}
			        	fonte_size.setTextSize(size);
			        }
			});
		
	 } catch (Exception e) {
		 // TODO Auto-generated catch block
		 e.printStackTrace();
	 }

        
 }

	

}
