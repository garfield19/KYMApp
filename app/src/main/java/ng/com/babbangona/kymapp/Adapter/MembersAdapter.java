package ng.com.babbangona.kymapp.Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import es.dmoral.toasty.Toasty;
import ng.com.babbangona.kymapp.BGCard;
import ng.com.babbangona.kymapp.CaptureFailedInputPickerTemplate;
import ng.com.babbangona.kymapp.CapturePicture;
import ng.com.babbangona.kymapp.Data.POJO.Capture;
import ng.com.babbangona.kymapp.Data.POJO.Members;
import ng.com.babbangona.kymapp.Data.Remote.ApiUtils;
import ng.com.babbangona.kymapp.Database.DatabaseHelper;
import ng.com.babbangona.kymapp.HarvestAdvance;
import ng.com.babbangona.kymapp.Home;
import ng.com.babbangona.kymapp.KivaCapture;
import ng.com.babbangona.kymapp.OldOrNew;
import ng.com.babbangona.kymapp.PreviousCollectionMethod;
import ng.com.babbangona.kymapp.R;

public class MembersAdapter extends RecyclerView.Adapter<MembersAdapter.ViewHolder>{

    private List<Members> mName,mStatus,mRole,mID;
    private LayoutInflater mInflater;
    private MembersAdapter.ItemClickListener mClickListener;
    Context context;

    // data is passed into the constructor
    public MembersAdapter(Context context, List<Members> name) {
        this.mInflater = LayoutInflater.from(context);
        this.mName = name;
    }

    // inflates the row layout from xml when needed
    @Override
    public MembersAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.single_group_member, parent, false);
        return new MembersAdapter.ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(MembersAdapter.ViewHolder holder, int position) {

        final Members member = mName.get(position);

       String done = member.getDone();
       String pass = member.getPass_done();
       String fail_picture = member.getFail_picture_done();

       // String done = "1";
        String picked = member.getPicker();

        holder.member_name.setText(member.getMemberName());
        holder.member_id.setText(member.getUniqueMemberId());

        DatabaseHelper mDatabaseHelper2 = new DatabaseHelper(holder.itemView.getContext());

        int bgFilledNoPass = mDatabaseHelper2.bgCardNoPass(member.getUniqueMemberId());

        if(picked.equals("1")){
            holder.member_role.setText("Input Collector");
        }else{
            holder.member_role.setText(member.getMemberRole());
        }

        if(done.equals("1")){
            holder.attachment.setBackgroundColor(Color.GREEN);
        }else{
            if((fail_picture.equals("1") || pass.equals("1")) && bgFilledNoPass <= 0){
                holder.attachment.setBackgroundColor(Color.YELLOW);
            }else if (bgFilledNoPass > 0){
                holder.attachment.setBackgroundColor(Color.parseColor("#ffc000"));
            }else{
                holder.attachment.setBackgroundColor(Color.RED);
            }

        }
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mName.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView member_name, member_id, member_role;
        ImageView attachment;

        ViewHolder(View itemView) {
            super(itemView);
            member_name = itemView.findViewById(R.id.person_name);
            member_id = itemView.findViewById(R.id.person_id);
            member_role = itemView.findViewById(R.id.person_role);
            attachment = itemView.findViewById(R.id.person_thumbnail);
            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
            String chosen_member = mName.get(getAdapterPosition()).getUniqueMemberId().toString();
            String member_name = mName.get(getAdapterPosition()).getMemberName().toString();
            String picker = mName.get(getAdapterPosition()).getPicker().toString();
            String mem_id = mName.get(getAdapterPosition()).getMember_id().toString();
            String ik = mName.get(getAdapterPosition()).getIkNumber().toString();



            SharedPreferences p = PreferenceManager.getDefaultSharedPreferences(v.getContext());
            SharedPreferences.Editor edit = p.edit();
            edit.putString("unique_member_id", chosen_member);
            edit.putString("chosen_member_name", member_name);
            edit.putString("old_member_id", mem_id);
            edit.putString("chosen_member_ik", ik);
            edit.apply();

            String done =  mName.get(getAdapterPosition()).getDone().toString();
            String pass = mName.get(getAdapterPosition()).getPass_done().toString();
            String fail_picture = mName.get(getAdapterPosition()).getFail_picture_done().toString();
            String approved = mName.get(getAdapterPosition()).getApproved().toString();


            DatabaseHelper mDatabaseHelper2 = new DatabaseHelper(v.getContext());
            int bgFilledNoPass = mDatabaseHelper2.bgCardNoPass(chosen_member);

            if(done.equals("1")){
                Toasty.error(v.getContext(),"Already Complete",Toast.LENGTH_SHORT).show();
            }else{
                if((fail_picture.equals("1") || pass.equals("1")) && bgFilledNoPass <= 0){
                    Intent i = new Intent(v.getContext(), KivaCapture.class);
                    edit.putString("picker", "yes");
                    edit.apply();
                    i.putExtra("member_id", chosen_member);
                    i.putExtra("member_name", member_name);
                    i.putExtra("type","capture_ip");
                    i.putExtra("picker", "");
                    v.getContext().startActivity(i);
                }else if (bgFilledNoPass > 0){
                    edit.putString("picker", "yes");
                    edit.apply();
                    //Toasty.error(v.getContext(),"Sync up picture for Process Control to review and approve template recapture",Toast.LENGTH_SHORT).show();



                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(v.getContext());
                    alertDialogBuilder.setMessage("Wannan Dan-kungiyan ya fadi gwajin fiska ta KYM.\n" +
                                    "\n" +
                                    "In kana so ka kara gwada gwajin fiska ta KYM ka yi kamar Baka:\n" +
                                    "1. Ka Sync KYM app (Ka tabbatar akwai network mai kyau).\n" +
                                    "\n" +
                                    "2. Kira wayar mai kula da harkokin application Ka kuma bashi lambar IK domin ya bincika.\n" +
                                    "\n" +
                                    "3. Ka sync ka kuma kara sync domin ka sami rahoto no KYM ya yi\n --------------------------------" +
                                    "\n --------------------------------" +
                                    "This member has failed KYM Facial Verification before. \n" +
                            "\n" +
                            "If you want to try KYM again you must do the following steps:\n" +
                            "\n" +
                            "1) Sync your KYM Application (make sure you have good network)\n" +
                            "2) Contact Product Support and give them IK Number so that they can approve\n" +
                            "3) Sync sync your KYM Application after Product Support has given approval"
                             );
                    alertDialogBuilder.setPositiveButton("OK",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface arg0, int arg1) {

                                }
                            });

                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.setCancelable(false);
                    alertDialog.show();






                }else{
                    Intent i = new Intent(v.getContext(), OldOrNew.class);
                    if (picker.equals("1")) {

                        i.putExtra("member_id", chosen_member);
                        i.putExtra("member_name", member_name);
                        i.putExtra("picker", "yes");
                        edit.putString("picker", "yes");
                        edit.apply();
                        i.putExtra("type","");
                    } else {
                        i.putExtra("member_name", member_name);
                        i.putExtra("member_id", chosen_member);
                        i.putExtra("picker", "no");
                        edit.putString("picker", "no");
                        edit.apply();
                        i.putExtra("type","");
                    }

                    v.getContext().startActivity(i);
                }

            }


/*
            if(done.equals("1")){
                Toasty.error(v.getContext(),"Already Complete",Toast.LENGTH_SHORT).show();
            }else if((fail_picture.equals("1")) && bgFilledNoPass <= 0){
                //todo and continue flag = 1
                Intent i = new Intent(v.getContext(), KivaCapture.class);
                edit.putString("picker", "yes");
                edit.apply();
                i.putExtra("member_id", chosen_member);
                i.putExtra("member_name", member_name);
                i.putExtra("type","capture_ip");
                i.putExtra("picker", "");
                v.getContext().startActivity(i);

            }
            else if(pass.equals("1") ){

                //todo if olld tg take to the old tg class
                Intent i = new Intent(v.getContext(), KivaCapture.class);
                edit.putString("picker", "yes");
                edit.apply();
                v.getContext().startActivity(i);

            }else if(approved.equals("1") ){

                //todo if old tg take to the old tg class
                Intent i = new Intent(v.getContext(), CaptureFailedInputPickerTemplate.class);
                edit.putString("picker", "yes");
                edit.apply();
                v.getContext().startActivity(i);

            }else if(bgFilledNoPass > 0) {
                //todo and continue flag = 1z
                edit.putString("picker", "yes");
                edit.apply();
                Toasty.error(v.getContext(),"Sync up picture for Process Control to review and approve template recapture",Toast.LENGTH_SHORT).show();
            } else {

                Intent i = new Intent(v.getContext(), OldOrNew.class);
                if (picker.equals("1")) {

                    i.putExtra("member_id", chosen_member);
                    i.putExtra("member_name", member_name);
                    i.putExtra("picker", "yes");
                    edit.putString("picker", "yes");
                    edit.apply();
                    i.putExtra("type","");
                } else {
                    i.putExtra("member_name", member_name);
                    i.putExtra("member_id", chosen_member);
                    i.putExtra("picker", "no");
                    edit.putString("picker", "no");
                    edit.apply();
                    i.putExtra("type","");
                }

                v.getContext().startActivity(i);


            }


 */


        }
    }


    // allows clicks events to be caught
    public void setClickListener(MembersAdapter.ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }



}
