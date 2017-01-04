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
import com.ihunter.taskee.Constants;
import com.ihunter.taskee.R;
import com.ihunter.taskee.TaskeeApplication;
import com.ihunter.taskee.activities.TaskEditorActivity;
import com.ihunter.taskee.data.Plan;
import com.ihunter.taskee.dialogs.ConfirmDialog;
import com.ihunter.taskee.interfaces.PlanItemInterface;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class PlanItemViewHolder extends RecyclerView.ViewHolder {

    PlanItemInterface planItemView;
    Realm realm;
    Plan plan;

    @BindView(R.id.plan_title)
    TextView planTitle;

    @BindView(R.id.plan_description)
    TextView planDescription;

    @BindView(R.id.plan_date)
    TextView planDate;

    @BindView(R.id.priority_tag)
    AppCompatImageView priorityTag;

    @BindView(R.id.plan_image)
    AppCompatImageView planImage;

    public PlanItemViewHolder(View v, PlanItemInterface planItemView) {
        super(v);
        ButterKnife.bind(this, v);
        realm = Realm.getInstance(TaskeeApplication.getRealmConfiugration());
        this.planItemView = planItemView;
    }

    public void bind(Plan plan) {
        this.plan = plan;
        if(!TextUtils.isEmpty(plan.getImage())){
            Glide.with(itemView.getContext()).load(Uri.parse(plan.getImage())).placeholder(R.drawable.zzz_download).into(planImage);
            planImage.setVisibility(VISIBLE);
        }else{
            planImage.setImageBitmap(null);
            planImage.setVisibility(GONE);
        }
        planTitle.setText(plan.getTitle());
        planDescription.setText(plan.getDescription());
        planDate.setText(Constants.getFullDateTime(plan.getTimestamp()));
        switch (plan.getPriority()){
            case 1:
                priorityTag.setColorFilter(ContextCompat.getColor(itemView.getContext(), R.color.low_priority));
                break;
            case 2:
                priorityTag.setColorFilter(ContextCompat.getColor(itemView.getContext(), R.color.medium_priority));
                break;
            case 3:
                priorityTag.setColorFilter(ContextCompat.getColor(itemView.getContext(), R.color.high_priority));
                break;
        }
    }

    @OnClick({R.id.edit_task, R.id.delete_task})
    protected void editTask(View v){
        switch (v.getId()){
            case R.id.edit_task:
                Intent updatePlanIntent = new Intent(v.getContext(), TaskEditorActivity.class);
                updatePlanIntent.putExtra("item_id", plan.getId());
                v.getContext().startActivity(updatePlanIntent);
                break;
            case R.id.delete_task:
                new ConfirmDialog(v.getContext())
                        .setTitle(v.getContext().getString(R.string.line_delete_task))
                        .setIcon(R.drawable.zzz_close_circle_outline)
                        .setPositiveButton(v.getContext().getString(R.string.word_yes), new ConfirmDialog.OnPositiveButton() {
                            @Override
                            public void onClick(View v, Dialog dialog) {
                                realm.executeTransaction(new Realm.Transaction() {
                                    @Override
                                    public void execute(Realm realm) {
                                        planItemView.onItemDelete(getAdapterPosition());
                                    }
                                });
                                dialog.dismiss();
                            }})
                        .setNegativeButton(v.getContext().getString(R.string.word_no), new ConfirmDialog.OnNegativeButton() {
                            @Override
                            public void onClick(View v, Dialog dialog) {
                                dialog.dismiss();
                            }})
                        .show();
                break;
        }
    }

}