package br.com.adoteumpet;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    private FirebaseAuth mAuth;
    String data1[], data2[];
    int image[];
    Context context;


    public MyAdapter(Context ct, String s1[], String s2[], int img[]){
        context = ct;
        data1 = s1;
        data2 = s2;
        image = img;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.botoes_linha, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.bttnperfil_titulo.setText(data1[position]);
        holder.bttnperfil_descricao.setText(data2[position]);
        holder.ivBotoes.setImageResource(image[position]);
        if(data1[position].equals("Edite seu perfil")){
            holder.mainLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, EditarperfilActivity.class);
                    context.startActivity(intent);
                }
            });
        } if(data1[position].equals("Minhas preferências")){
            holder.mainLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(context, "Nada Ainda...", Toast.LENGTH_LONG).show();
                }
            });
        } if(data1[position].equals("Convidar amigos")){
            holder.mainLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(context, "Nada Ainda...", Toast.LENGTH_LONG).show();
                }
            });
        } if(data1[position].equals("Precisa de ajuda")){
            holder.mainLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, FaqActivity.class);
                    context.startActivity(intent);
                }
            });
        } if(data1[position].equals("Sair")){
            holder.mainLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mAuth = FirebaseAuth.getInstance();
                    mAuth.signOut();
                    Intent intent = new Intent(context, HomeActivity.class);
                    context.startActivity(intent);
                    ((Activity)context).finish();
                    return;
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return image.length;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView bttnperfil_titulo, bttnperfil_descricao;
        ImageView ivBotoes;
        ConstraintLayout mainLayout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            bttnperfil_titulo = itemView.findViewById(R.id.bttnperfil_titulo);
            bttnperfil_descricao = itemView.findViewById(R.id.bttnperfil_descricao);
            ivBotoes = itemView.findViewById(R.id.ivBotoes);
            mainLayout = itemView.findViewById(R.id.mainLayout);

        }
    }
}
