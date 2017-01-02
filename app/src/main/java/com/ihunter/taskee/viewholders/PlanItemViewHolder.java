package com.ihunter.taskee.viewholders;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ihunter.taskee.R;
import com.ihunter.taskee.TaskeeApplication;
import com.ihunter.taskee.Test;
import com.ihunter.taskee.adapters.PlanItemAdapter;
import com.ihunter.taskee.data.Plan;
import com.ihunter.taskee.dialogs.ConfirmDialog;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;

public class PlanItemViewHolder extends RecyclerView.ViewHolder {

    PlanItemAdapter adapter;
    Realm realm;
    Plan plan;

    @BindView(R.id.plan_title)
    TextView planTitle;

    @BindView(R.id.plan_description)
    TextView planDescription;

    @BindView(R.id.plan_time)
    TextView planTime;

    @BindView(R.id.priority_tag)
    AppCompatImageView priorityTag;

    @BindView(R.id.plan_image)
    AppCompatImageView planImage;

    public PlanItemViewHolder(View v, PlanItemAdapter adapter) {
        super(v);
        ButterKnife.bind(this, v);
        realm = Realm.getInstance(TaskeeApplication.getRealmConfiugration());
        this.adapter = adapter;
    }

    public void bind(Plan plan) {
        this.plan = plan;
        if(!TextUtils.isEmpty(plan.getImage())){
            Glide.with(itemView.getContext()).load(Uri.parse(plan.getImage())).into(planImage);
            planImage.setVisibility(View.VISIBLE);
        }else{
            planImage.setVisibility(View.GONE);
        }
        planTitle.setText(plan.getTitle());
        planDescription.setText(plan.getDescription());
        planTime.setText(new SimpleDateFormat("MMM dd, yyyy - ").format(plan.getTimestamp()) + new SimpleDateFormat("h:mm a").format(new Date(plan.getTimestamp())).toLowerCase());
        if (plan.getPriority() == 1) {
            priorityTag.setColorFilter(ContextCompat.getColor(itemView.getContext(), R.color.low_priority));
        } else if (plan.getPriority() == 2) {
            priorityTag.setColorFilter(ContextCompat.getColor(itemView.getContext(), R.color.medium_priority));
        } else if (plan.getPriority() == 3) {
            priorityTag.setColorFilter(ContextCompat.getColor(itemView.getContext(), R.color.high_priority));
        }

    }

    public void openPath(Uri uri){
        InputStream is = null;
        try {
            is = itemView.getContext().getContentResolver().openInputStream(uri);
            //Convert your stream to data here
            is.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @OnClick({R.id.edit_task, R.id.delete_task})
    protected void editTask(View v){
        switch (v.getId()){
            case R.id.edit_task:
                Intent updatePlanIntent = new Intent(v.getContext(), Test.class);
                updatePlanIntent.putExtra("item_id", plan.getId());
                v.getContext().startActivity(updatePlanIntent);
                break;

            case R.id.delete_task:
                new ConfirmDialog(v.getContext())
                        .setTitle("Are you sure you wish to delete this item")
                        .setIcon(R.drawable.zzz_close_circle_outline)
                        .setPositiveButton("YES", new ConfirmDialog.OnPositiveButton() {
                            @Override
                            public void onClick(View v, Dialog dialog) {
                                realm.executeTransaction(new Realm.Transaction() {
                                    @Override
                                    public void execute(Realm realm) {
                                        adapter.getPlansList().deleteFromRealm(getAdapterPosition());
                                        adapter.notifyItemRemoved(getAdapterPosition());
                                        adapter.notifyItemRangeChanged(getAdapterPosition(), adapter.getPlansList().size());
                                        if(adapter.getCalendarTasksFragment() != null){
                                            adapter.refreshCalendarEvents();
                                        }
                                    }
                                });
                                dialog.dismiss();
                            }})
                        .setNegativeButton("NO", new ConfirmDialog.OnNegativeButton() {
                            @Override
                            public void onClick(View v, Dialog dialog) {
                                dialog.dismiss();
                            }})
                        .show();
                break;
        }
    }

}