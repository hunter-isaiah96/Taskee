package com.ihunter.taskee.activities;

import android.support.v4.app.Fragment;

import com.ihunter.taskee.fragments.AllTasksFragment;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertThat;

public class MainActivityTest {

    @Before
    public void setUp() throws Exception {

    }

    @Test
    public void isFragmentCorrectInstance(){
        Fragment fragment = new AllTasksFragment();
        assertThat(fragment, instanceOf(AllTasksFragment.class));
    }

}