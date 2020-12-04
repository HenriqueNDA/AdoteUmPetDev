package br.com.adoteumpet.Petchs;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import br.com.adoteumpet.ChatActivity;
import br.com.adoteumpet.R;

public class PetchViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView mUserId, mUsername, mResponsibleName;
    public ImageView mUserImage;
    public PetchViewHolder(@NonNull View itemView) {
        super(itemView);
        itemView.setOnClickListener(this);
        mUserId = (TextView) itemView.findViewById(R.id.UserId);
        mUsername = (TextView) itemView.findViewById(R.id.Username);
        mResponsibleName = (TextView) itemView.findViewById(R.id.responsibleName);
        mUserImage = (ImageView) itemView.findViewById(R.id.UserImage);
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(view.getContext(), ChatActivity.class);
        Bundle b = new Bundle();
        b.putString("petchId", mUserId.getText().toString());
        intent.putExtras(b);
        view.getContext().startActivity(intent);
    }
}
