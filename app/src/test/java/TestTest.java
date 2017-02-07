import com.ihunter.taskee.mvptest.TestPresenter;
import com.ihunter.taskee.mvptest.TestView;

import org.junit.Test;

public class TestTest {

    @Test
    public void equals(){
        MockTestView mockTestView = new MockTestView();
        TestPresenter presenter = new TestPresenter(mockTestView);
        presenter.isEqualTo(1, 2);
    }

    public class MockTestView implements TestView{

        @Override
        public void EqualResult(boolean isEqual) {
            if(isEqual){
                System.out.println("It's true");
            }else{
                System.out.println("It's false");
            }
        }
    }

}
