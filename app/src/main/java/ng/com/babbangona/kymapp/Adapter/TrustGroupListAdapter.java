package ng.com.babbangona.kymapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;
import ng.com.babbangona.kymapp.Data.POJO.Members;
import ng.com.babbangona.kymapp.Database.DatabaseHelper;
import ng.com.babbangona.kymapp.MembersActivity;
import ng.com.babbangona.kymapp.R;

public class TrustGroupListAdapter extends RecyclerView.Adapter<TrustGroupListAdapter.ViewHolder> implements Filterable {

    private List<String> mStatus;
    private List<Members> mName;
    private List<Members> memberListFiltered;
    private LayoutInflater mInflater;
    Context context;
    private TrustGroupListAdapter.ItemClickListener mClickListener;

    // data is passed into the constructor
    public TrustGroupListAdapter(Context context, List<Members> name) {
        this.mInflater = LayoutInflater.from(context);
       // this.mStatus = status;
        this.mName = name;
        this.memberListFiltered = name;
    }

    // inflates the row layout from xml when needed
    @Override
    public TrustGroupListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.single_trust_group, parent, false);
        return new TrustGroupListAdapter.ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(TrustGroupListAdapter.ViewHolder holder, int position) {
        final Members tg = memberListFiltered.get(position);


      holder.tg_name.setText(tg.getIkNumber());

        DatabaseHelper mDatabaseHelper2 = new DatabaseHelper(holder.itemView.getContext());

        int total = mDatabaseHelper2.getTGCount(tg.getIkNumber());
        int done = mDatabaseHelper2.getDoneTGSum(tg.getIkNumber());

//        Log.d("gar done " + tg.getIkNumber(), String.valueOf(done));
//        Log.d("gar total" + tg.getIkNumber(), String.valueOf(total));

        if(done/total == 1){
            holder.attachment.setBackgroundColor(Color.GREEN);
        }else{
            holder.attachment.setBackgroundColor(Color.RED);
        }

    }

    // total number of rows
    @Override
    public int getItemCount() {
        return memberListFiltered.size();
    }




    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tg_name, amount, location;
        ImageView attachment;

        ViewHolder(View itemView) {
            super(itemView);
            tg_name = itemView.findViewById(R.id.tg_name);
             attachment = itemView.findViewById(R.id.thumbnail);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            String chosen_ik =  memberListFiltered.get(getAdapterPosition()).getIkNumber().toString();
            DatabaseHelper mDatabaseHelper2 = new DatabaseHelper(v.getContext());
            int total = mDatabaseHelper2.getTGCount(chosen_ik);
            int done = mDatabaseHelper2.getDoneTGSum(chosen_ik);

            Log.d("TOTAL SUM:", String.valueOf(total));
            Log.d("TOTAL DONE:", String.valueOf(done));

            if(done/total == 1){
                Toasty.error(v.getContext(),"Already Complete",Toast.LENGTH_SHORT).show();
                attachment.setBackgroundColor(Color.GREEN);
                Intent i=new Intent(v.getContext(),MembersActivity.class);
                i.putExtra("ik_number", chosen_ik);
                v.getContext().startActivity(i);
            }else{
                attachment.setBackgroundColor(Color.RED);
                Intent i=new Intent(v.getContext(),MembersActivity.class);
                i.putExtra("ik_number", chosen_ik);
                v.getContext().startActivity(i);
            }


        }
    }


    public void setClickListener(TrustGroupListAdapter.ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }

    @Override
    public Filter getFilter() {
        return new Filter(){
            @Override
            protected FilterResults performFiltering(CharSequence charSequence){

                String charString = charSequence.toString().trim();

                if(charString.isEmpty()){

                    memberListFiltered = mName;
                }else{
                    List<Members> filteredList = new ArrayList<>();

                    Log.d("CHECK", "charList" + mName.toString());

                    for(Members row : mName){

                        if (row.getIkNumber().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }

                    memberListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = memberListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults){

                memberListFiltered = (List<Members>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

}
