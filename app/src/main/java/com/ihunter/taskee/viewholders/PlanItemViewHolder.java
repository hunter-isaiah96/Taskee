package com.ihunter.taskee.viewholders;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;
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

    Activity activity;
    PlanItemInterface planItemView;
    Realm realm;
    Plan plan;

    @BindView(R.id.plan_title)
    TextView planTitle;

    @BindView(R.id.plan_note)
    TextView planNote;

    @BindView(R.id.plan_date)
    TextView planDate;

    @BindView(R.id.priority_tag)
    AppCompatImageView priorityTag;

    @BindView(R.id.plan_image)
    AppCompatImageView planImage;

    public PlanItemViewHolder(View v, PlanItemInterface planItemView, Activity activity) {
        super(v);
        ButterKnife.bind(this, v);
        realm = Realm.getInstance(TaskeeApplication.getRealmConfiugration());
        this.planItemView = planItemView;
        this.activity = activity;
    }

    public void bind(Plan plan, int count) {
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        if (getAdapterPosition() == count - 1) {
            layoutParams.bottomMargin = (int) itemView.getContext().getResources().getDimension(R.dimen.item_margin);
            layoutParams.topMargin = (int) itemView.getContext().getResources().getDimension(R.dimen.item_margin);
        }else{
            layoutParams.bottomMargin = 0;
            layoutParams.topMargin = (int) itemView.getContext().getResources().getDimension(R.dimen.item_margin);
        }
        itemView.setLayoutParams(layoutParams);
        this.plan = plan;

        switch (plan.getPriority()) {
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

        if (!TextUtils.isEmpty(plan.getImage())) {
            Glide.with(itemView.getContext()).load(plan.getImage()) .into(planImage);
            planImage.setVisibility(VISIBLE);
        } else {
            Glide.clear(planImage);
            planImage.setVisibility(GONE);
        }
        if (!TextUtils.isEmpty(plan.getNote())) {
            planNote.setText(plan.getNote());
            planNote.setVisibility(VISIBLE);
        } else {
            planNote.setVisibility(GONE);
        }

        planTitle.setText(plan.getTitle());
        planDate.setText(Constants.getFullDateTime(plan.getTimestamp()));
    }

    @OnClick({R.id.edit_task, R.id.delete_task})
    protected void editTask(View v) {
        switch (v.getId()) {
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
                            }
                        })
                        .setNegativeButton(v.getContext().getString(R.string.word_no), new ConfirmDialog.OnNegativeButton() {
                            @Override
                            public void onClick(View v, Dialog dialog) {
                                dialog.dismiss();
                            }
                        })
                        .show();
                break;
        }
    }

}