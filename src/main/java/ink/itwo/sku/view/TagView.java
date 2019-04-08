package ink.itwo.sku.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Checkable;

import ink.itwo.sku.R;

/**
 Created by wangtaian on 2019/4/4. */
public class TagView extends android.support.v7.widget.AppCompatTextView implements Checkable {
    private boolean checked = false, clickEnable = true;

    public TagView(Context context) {
        super(context);
    }

    public TagView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TagView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void setChecked(boolean checked) {
        if (this.checked == checked) {
            return;
        }
        this.checked = checked;
        switchStyle();
    }

    @Override
    public boolean isChecked() {
        return checked;
    }

    @Override
    public void toggle() {
        checked = !checked;
        switchStyle();
    }

    public void setClickEnable(boolean clickEnable) {
        this.clickEnable = clickEnable;
        switchStyle();
    }

    public void setState(boolean checked, boolean clickEnable) {
        this.checked = checked;
        this.clickEnable = clickEnable;
        switchStyle();
    }

    protected void switchStyle() {
        if (clickEnable) {
            setBackgroundResource(checked ? R.drawable.tag_background_check : R.drawable.tag_background_normal);
            setTextColor(getResources().getColor(checked ? R.color.tag_check : R.color.tag_normal));
        } else {
            setBackgroundResource(R.drawable.tag_background_disable);
            setTextColor(getResources().getColor(R.color.tag_disable));
        }
    }
}
