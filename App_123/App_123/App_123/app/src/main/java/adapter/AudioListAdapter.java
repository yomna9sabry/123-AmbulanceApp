package adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.app_123.R;

import java.io.File;

import model.TimeAgo;

public class AudioListAdapter extends RecyclerView.Adapter<AudioListAdapter.AudioViewHolder> {
    private File[] allFiles;
    Context context;
    private  TimeAgo timeAgo;
    private OnItemClickListener itemClickListener;
    public AudioListAdapter(Context context, File[]allFiles,OnItemClickListener itemClickListener){
        this.context=context;
        this.allFiles=allFiles;
        this.itemClickListener=itemClickListener;
    }

    @NonNull
    @Override
    public AudioViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_record, parent, false);
        timeAgo=new TimeAgo();
        return new AudioViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AudioViewHolder holder, int position) {
        holder.txt_title.setText(allFiles[position].getName());
        holder.txt_date.setText(timeAgo.getTimeAgo(allFiles[position].lastModified()));

    }

    @Override
    public int getItemCount() {
        return allFiles.length;
    }

    public class AudioViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView txt_title;
        TextView txt_date;
        ImageView image_audio;

        public AudioViewHolder(@NonNull View itemView) {
            super(itemView);
            image_audio=itemView.findViewById(R.id.image_audio);
            txt_title=itemView.findViewById(R.id.txt_title);
            txt_date=itemView.findViewById(R.id.txt_date);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            itemClickListener.mClickListener(allFiles[getAdapterPosition()],getAdapterPosition());
        }
    }
    public  interface OnItemClickListener{
        void mClickListener(File file,int position);
    }
}
