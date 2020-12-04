package br.com.adoteumpet.Chat;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import br.com.adoteumpet.R;

public class ChatAdapter extends RecyclerView.Adapter<ChatViewHolder> {
    private List<ChatObject> chatLists;
    private Context context;

    public ChatAdapter(List<ChatObject> petchsLists, Context context){
        this.chatLists = petchsLists;
        this.context = context;
    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat, null, false);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutView.setLayoutParams(lp);
        ChatViewHolder rcv = new ChatViewHolder((layoutView));
        return rcv;
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {
        holder.mMessage.setText(chatLists.get(position).getMessage());
        if (chatLists.get(position).getcurrentUser()){
            holder.mMessage.setGravity(Gravity.END);
            holder.mMessage.setTextColor(Color.parseColor("#FFFFFF"));
            holder.mContainer.setBackgroundColor(Color.parseColor("#0084FF"));
            holder.mImage.setVisibility(View.INVISIBLE);
        } else {
            holder.mMessage.setGravity(Gravity.START);
            holder.mMessage.setTextColor(Color.parseColor("#000000"));
            holder.mContainer.setBackgroundColor(Color.parseColor("#d3d3d3"));
            holder.mImage.setVisibility(View.VISIBLE);
            if (!chatLists.get(position).getprofileImageUrl().equals("default")){
                Glide.with(context).load(chatLists.get(position).getprofileImageUrl()).into(holder.mImage);
            }
        }

    }

    @Override
    public int getItemCount() {
        return this.chatLists.size();
    }
}
