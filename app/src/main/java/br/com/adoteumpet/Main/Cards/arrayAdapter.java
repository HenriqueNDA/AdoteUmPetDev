package br.com.adoteumpet.Main.Cards;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import br.com.adoteumpet.R;

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
        Glide.with(getContext()).load(card_item.getProfileImageUrl()).into(img);


        return  convertView;


    }
}
