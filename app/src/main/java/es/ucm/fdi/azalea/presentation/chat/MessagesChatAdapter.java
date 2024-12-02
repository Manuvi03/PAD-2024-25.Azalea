package es.ucm.fdi.azalea.presentation.chat;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.Guideline;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.List;
import java.util.Objects;

import es.ucm.fdi.azalea.R;
import es.ucm.fdi.azalea.business.model.MessageModel;
import es.ucm.fdi.azalea.presentation.parent.EventsParentAdapter;


public class MessagesChatAdapter extends RecyclerView.Adapter<MessagesChatAdapter.ViewHolder> {

    private final String TAG = "MessagesChatAdapter";
    private List<MessageModel> mMessagesData;
    private final LayoutInflater mInflater;
    private String myUserId;

    public MessagesChatAdapter(List<MessageModel> chatsInfoArrayList, String myUserId, Context context) {
        Log.d(TAG, "creacion del adpater de los msgs");
        this.mInflater = LayoutInflater.from(context);
        this.mMessagesData = chatsInfoArrayList;
        this.myUserId = myUserId;
    }

    public void setmMessagesData(List<MessageModel> messages){
        this.mMessagesData = messages;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.message_chat_recyclerview_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MessageModel message = mMessagesData.get(position);
        holder.bind(message);
    }

    @Override
    public int getItemCount() {
            return mMessagesData != null ? mMessagesData.size() : 0;
        }

    // Clase interna ViewHolder para el RecyclerView
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private CardView cardView;
        private TextView message;
        private Guideline guideline_end;
        private Guideline guideline_start;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            message = itemView.findViewById(R.id.message_inputs);
            cardView = itemView.findViewById(R.id.message_cardView);
            guideline_end = itemView.findViewById(R.id.guideline_end);
            guideline_start = itemView.findViewById(R.id.guideline_start);
        }

        @SuppressLint("ResourceAsColor")
        public void bind(MessageModel messageModel){

            message.setText(messageModel.getMessage());
            ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) cardView.getLayoutParams();

            if(Objects.equals(messageModel.getSenderId(), Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid())) {
                message.setTextColor(R.color.Dark_Green);
                cardView.setCardBackgroundColor(
                        ContextCompat.getColor(itemView.getContext(), R.color.Misty_Rose));

                layoutParams.startToStart = ConstraintLayout.LayoutParams.UNSET; //Quitamos el constraint del start
                layoutParams.endToEnd = ConstraintLayout.LayoutParams.PARENT_ID; //Anyadimos el constraint del end
                layoutParams.startToEnd = guideline_start.getId();
                layoutParams.endToStart = ConstraintLayout.LayoutParams.UNSET;
                layoutParams.horizontalBias = 1f;
            }
            else{
                message.setTextColor(R.color.Lapis_Lazuli);
                cardView.setCardBackgroundColor(
                        ContextCompat.getColor(itemView.getContext(), R.color.Cyclomen));

                layoutParams.endToEnd = ConstraintLayout.LayoutParams.UNSET; //Quitamos el constraint del end
                layoutParams.startToStart = ConstraintLayout.LayoutParams.PARENT_ID; //Anyadimos el constraint del start
                layoutParams.endToStart = guideline_end.getId();
                layoutParams.startToEnd = ConstraintLayout.LayoutParams.UNSET;
                layoutParams.horizontalBias = 0f;
            }
            cardView.setLayoutParams(layoutParams);
        }
    }
}
