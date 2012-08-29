package br.com.redumobile.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import br.com.redumobile.R;
import br.com.redumobile.entity.Answer;

public class AnswerAdapter extends BaseAdapter {
	
	private LayoutInflater inflater;
	private List<Answer> respostas;
    private Context context;

    public AnswerAdapter(Context context, List<Answer> respostas) {
        this.context = context;
        this.respostas = respostas;
        inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    
	
    @Override
    public int getCount() {
        return respostas.size();
    }

    @Override
    public Object getItem(int position) {
        return respostas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		
	     view = inflater.inflate(R.layout.detalhe_answer, null);

		 TextView resposta_texto = (TextView) view.findViewById(R.id.resposta_texto);
//		 Course ava = respostas.get(position);
		 if(position == 2 || position == 4){
		 resposta_texto.setText("Todos os componentes da interface do Redu agora são opensource! \n " +
			" Vocês são livres para utilizar os CSS e JavaScript dentro nas suas aplicações. Para incluir e utilizar basta ir em: http://developers.redu.com.br/ui-components \n" + 
			"No canto superior direito tem um índice de todos os elementos e sobre como cria-los. \n " +
			"Redu Bootstrap \n" +
			"http://developers.redu.com.br \n" +
			"Dependências Antes de começar a utilizar o Redu Bootstrap, é preciso estar atento aos seguintes requsitios: jQuery O bootstrap faz uso do jQuery 1.7.);");
		 }
	    view.setTag(getItem(position));

		return view;
	}

}
