package com.ihunter.taskee.mvptest;

public class TestPresenter {

    private TestView testView;

    public TestPresenter(TestView testView){
        this.testView = testView;
    }

    public void isEqualTo(int first, int second){
        testView.EqualResult(first == second);
    }

}
