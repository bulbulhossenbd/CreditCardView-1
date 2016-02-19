package movile.com.creditcardguide;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by FelipeSilvestre on 17/02/16.
 */
public class FieldsPageAdapter extends PagerAdapter {

    private View viewHolder;
    private List<CreditCardFragment.Step> steps;

    public FieldsPageAdapter(View viewHolder, List<CreditCardFragment.Step> steps) {
        this.viewHolder = viewHolder;
        this.steps = steps;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        int resId = 0;

        switch (steps.get(position)) {
            case FLAG:
                resId = R.id.page_zero;
                break;
            case NUMBER:
                resId = R.id.page_one;
                break;
            case EXPIRE_DATE:
                resId = R.id.page_two;
                break;
            case CVV:
                resId = R.id.page_three;
                break;
            case NAME:
                resId = R.id.page_four;
                break;
        }

        return viewHolder.findViewById(resId);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
    }

    @Override
    public int getCount() {
        return 5;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((View) object);
    }
}