package br.com.redumobile.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import br.com.redumobile.R;
import br.com.redumobile.entity.Course;

public class CourseAdapter extends BaseAdapter {
	private LayoutInflater inflater;
	private List<Course> ambientes;
    private Context context;

    public CourseAdapter(Context context, List<Course> ambientes) {
        this.context = context;
        this.ambientes = ambientes;
        inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    
	
    @Override
    public int getCount() {
        return ambientes.size();
    }

    @Override
    public Object getItem(int position) {
        return ambientes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		
	     view = inflater.inflate(R.layout.detalhe_curso, null);
		 

		 TextView hambiente = (TextView) view.findViewById(R.id.ambiante);
		 Course ava = ambientes.get(position);
		 hambiente.setText(ava.getNome());
		
	    view.setTag(getItem(position));

		return view;
	}

}
