package br.com.adoteumpet;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class arrayAdapter extends ArrayAdapter<cards> {
    Context context;

    public arrayAdapter(Context context, int resourceId, List<cards> items){
        super(context, resourceId, items);
    }

    public View getView(int position, View convertView, ViewGroup parent){
        cards card_item = getItem(position);

        if( convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item, parent, false);
        }

        TextView nome = (TextView) convertView.findViewById(R.id.nome);
        ImageView img = (ImageView) convertView.findViewById(R.id.image);

        nome.setText(card_item.getNome());
        img.setImageResource(R.drawable.rectangle_6);
        return  convertView;


    }
}
