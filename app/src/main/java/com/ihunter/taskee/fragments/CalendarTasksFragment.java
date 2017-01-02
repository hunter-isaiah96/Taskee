package com.ihunter.taskee.fragments;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;
import com.ihunter.taskee.TaskeeApplication;
import com.ihunter.taskee.R;
import com.ihunter.taskee.activities.MainActivity;
import com.ihunter.taskee.adapters.PlanItemAdapter;
import com.ihunter.taskee.data.Plan;
import com.ihunter.taskee.ui.EmptyRecyclerView;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 * Created by Master Bison on 12/21/2016.
 */

public class CalendarTasksFragment extends Fragment implements CompactCalendarView.CompactCalendarViewListener, AppBarLayout.OnOffsetChangedListener, DatePickerDialog.OnDateSetListener{

    @BindView(R.id.appbar)
    AppBarLayout appBar;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.toolbar_title)
    AppCompatTextView toolbar_title;

    @BindView(R.id.toolbar_calendar_indicator)
    AppCompatImageView toolbar_indicator;

    @BindView(R.id.toolbar_expand_container)
    LinearLayout toolbar_expand_container;

    @BindView(R.id.plansList)
    EmptyRecyclerView plansList;

    @BindView(R.id.todo_list_empty_view)
    LinearLayout todoListEmptyView;

    @BindView(R.id.toolbar_calendar)
    CompactCalendarView calendarView2;
    Realm realm;

    boolean isExpanded = true;
    boolean shouldExecuteOnResume = false;
    Calendar lastDateSelected;

    SimpleDateFormat title_date = new SimpleDateFormat("EEE, MMM dd - yyyy");
    List<Event> eventList;
    RealmResults<Plan> eventPlans;
    PlanItemAdapter calendarPlansAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        realm = Realm.getInstance(TaskeeApplication.getRealmConfiugration());
        lastDateSelected = Calendar.getInstance();
        eventList = new ArrayList<>();
        View view = inflater.inflate(R.layout.fragment_calendar, container, false);
        ButterKnife.bind(this, view);

        ((MainActivity)getActivity()).setToolbar(toolbar);

        appBar.setExpanded(true);
        appBar.addOnOffsetChangedListener(this);

        setupCalendarView(lastDateSelected);

        calendarView2.setListener(this);

        plansList.setLayoutManager(new LinearLayoutManager(getContext()));
        plansList.setNestedScrollingEnabled(false);
        plansList.setEmptyView(todoListEmptyView);
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Do something that differs the Activity's menu here
        inflater.inflate(R.menu.calendar_view_menu, menu);
        menu.findItem(R.id.select_date).getIcon().setColorFilter(ContextCompat.getColor(getContext(), android.R.color.white), PorterDuff.Mode.SRC_IN);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();

        switch(itemId){
            case R.id.select_date:
                DatePickerDialog.newInstance(CalendarTasksFragment.this,
                        Calendar.getInstance().get(Calendar.YEAR),
                        Calendar.getInstance().get(Calendar.MONTH),
                        Calendar.getInstance().get(Calendar.DAY_OF_MONTH))
                        .show(getActivity().getFragmentManager(), "Datepickerdialog");;
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @OnClick(R.id.toolbar_expand_container)
    protected void expandToolbarClick() {
        appBar.setExpanded(!isExpanded);
    }

    @Override
    public void onResume() {
        super.onResume();
        if(!shouldExecuteOnResume){
            shouldExecuteOnResume = true;
        }else{
            setupCalendarView(lastDateSelected);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        realm.close();
    }

    @Override
    public void onDayClick(Date dateClicked) {

        lastDateSelected = toCalendar(dateClicked);
        setToolbarTitle(title_date.format(dateClicked.getTime()));
        // Get Selected Date @12:00 AM
        Calendar selectedDateStart = Calendar.getInstance();
        selectedDateStart.setTime(dateClicked);
        selectedDateStart.set(Calendar.HOUR, 0);
        selectedDateStart.set(Calendar.MINUTE, 0);
        selectedDateStart.set(Calendar.SECOND, 0);
        selectedDateStart.set(Calendar.MILLISECOND, 0);
        selectedDateStart.set(Calendar.AM_PM, Calendar.AM);

        // Get Selected Date @11:59 PM
        Calendar selectedDateEnd = Calendar.getInstance();
        selectedDateEnd.setTime(dateClicked);
        selectedDateEnd.set(Calendar.HOUR, 11);
        selectedDateEnd.set(Calendar.MINUTE, 59);
        selectedDateEnd.set(Calendar.SECOND, 59);
        selectedDateEnd.set(Calendar.MILLISECOND, 999);
        selectedDateEnd.set(Calendar.AM_PM, Calendar.PM);

        RealmResults<Plan> plans = getResultsInTime(selectedDateStart.getTime().getTime(), selectedDateEnd.getTime().getTime());
        calendarPlansAdapter.getplans(plans);

    }

    @Override
    public void onMonthScroll(Date firstDayOfNewMonth) {
        setToolbarTitle(title_date.format(firstDayOfNewMonth.getTime()));
        setupCalendarView(toCalendar(firstDayOfNewMonth));
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        if (verticalOffset == 0) {
            isExpanded = true;
        } else {
            isExpanded = false;
        }
        animateChevron();
    }

    public void addCalendarEvents(){
        eventList.clear();
        eventPlans = getAllTasks();
        for(int i = 0; i < eventPlans.size(); i++){
            int colorRes = 0;
            if(eventPlans.get(i).getPriority() == 1){
                colorRes = R.color.low_priority;
            }else if(eventPlans.get(i).getPriority() == 2){
                colorRes = R.color.medium_priority;
            }else if(eventPlans.get(i).getPriority() == 3){
                colorRes = R.color.high_priority;
            }
            eventList.add(new Event(ContextCompat.getColor(getContext(),colorRes), eventPlans.get(i).getTimestamp()));
        }
        calendarView2.removeAllEvents();
        calendarView2.addEvents(eventList);
    }

    public void setupCalendarView(Calendar calendar) {

        addCalendarEvents();

        lastDateSelected = calendar;
        setToolbarTitle(new SimpleDateFormat("EEE, MMM dd - yyyy").format(calendar.getTime()));
        calendar.set(Calendar.HOUR, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.set(Calendar.AM_PM, Calendar.AM);

        Calendar nextDay = Calendar.getInstance();
        nextDay.setTime(calendar.getTime());
        nextDay.set(Calendar.HOUR, 11);
        nextDay.set(Calendar.MINUTE, 59);
        nextDay.set(Calendar.SECOND, 59);
        nextDay.set(Calendar.MILLISECOND, 999);
        nextDay.set(Calendar.AM_PM, Calendar.PM);

        calendarPlansAdapter = new PlanItemAdapter(getContext(), getResultsInTime(calendar.getTimeInMillis(), nextDay.getTimeInMillis()));
        calendarPlansAdapter.setCalendarFragment(this);
        plansList.setAdapter(calendarPlansAdapter);
    }

    public RealmResults<Plan> getAllTasks(){
        return realm.where(Plan.class).findAllSorted("id", Sort.DESCENDING);
    }

    public RealmResults<Plan> getResultsInTime(long selectedDateStart, long selectedDateEnd){
        return realm.where(Plan.class).greaterThanOrEqualTo("timestamp", selectedDateStart).lessThan("timestamp", selectedDateEnd).findAllSorted("id", Sort.DESCENDING);
    }

    public static Calendar toCalendar(Date date){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal;
    }

    public void setToolbarTitle(String title){
        toolbar_title.setText(title);
    }

    public void animateChevron(){
        toolbar_indicator.animate().rotation(isExpanded ? 0 : 180).setDuration(getResources().getInteger(android.R.integer.config_shortAnimTime));
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        calendar.set(Calendar.MONTH, monthOfYear);
        calendarView2.setCurrentDate(calendar.getTime());
        setupCalendarView(calendar);

    }
}
