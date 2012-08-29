package br.com.redumobile.util;

import java.util.List;

import android.graphics.Bitmap;
import android.widget.ImageView;
import br.com.redumobile.entity.User;
import br.com.redumobile.entity.util.PostHelper;

public class ThreadCarregarThumbAutor extends Thread {
	
	public int position;
	public ImageView fotoPerfil;
	public User autor;
	public List<PostHelper> postagens;
	
	public ThreadCarregarThumbAutor(int position, ImageView fotoPerfil,
			User autor,List<PostHelper> postagens){
		this.autor = autor;
		this.fotoPerfil = fotoPerfil;
		this.position = position;
		this.postagens = postagens;
	}

	@Override
	public void run() {
		try {
			Bitmap bmp = Util.loadBitmap(autor.getUrlFotoPerfil());
            fotoPerfil.setImageBitmap(bmp);
            
            postagens.get(position).setThumbAutor(bmp);
		}catch (Exception e) {
			
		}
	}
}
