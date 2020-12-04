package br.com.adoteumpet.Petchs;

import android.content.Context;
import android.speech.RecognizerIntent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import br.com.adoteumpet.R;

public class PetchAdapter extends RecyclerView.Adapter<PetchViewHolder> {
    private List<PetchObject> petchsLists;
    private Context context;

    public PetchAdapter(List<PetchObject> petchsLists, Context context){
        this.petchsLists = petchsLists;
        this.context = context;
    }

    @NonNull
    @Override
    public PetchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.items_petch, null, false);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutView.setLayoutParams(lp);
        PetchViewHolder rcv = new PetchViewHolder((layoutView));
        return rcv;
    }

    @Override
    public void onBindViewHolder(@NonNull PetchViewHolder holder, int position) {
        holder.mUserId.setText(petchsLists.get(position).getUserId());
        holder.mUsername.setText(petchsLists.get(position).getName());
        if (!petchsLists.get(position).getResponsiblePetOtherUser().equals("Sem Nome")){
            holder.mResponsibleName.setText("Responsável: " + petchsLists.get(position).getResponsiblePetOtherUser());
        }
        if (!petchsLists.get(position).getprofileImageUrl().equals("default")){
            Glide.with(context).load(petchsLists.get(position).getprofileImageUrl()).into(holder.mUserImage);
        }
    }

    @Override
    public int getItemCount() {
        return this.petchsLists.size();
    }
}
