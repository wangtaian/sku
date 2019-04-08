package ink.itwo.sku.view;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import ink.itwo.sku.R;
import ink.itwo.sku.entity.Tags;

/**
 Created by wangtaian on 2019/4/4. */
public class TagAdapter extends BaseQuickAdapter<Tags, BaseViewHolder> {
    private OnTagClick tagClick;

    public TagAdapter(int layoutResId, @Nullable List<Tags> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Tags item) {
        TagView tagView = helper.getView(R.id.tv_tag);
        tagView.setText(item.getText());
        tagView.setState(item.isCheck(), item.isEnable());
        tagView.setOnClickListener(v -> {
            if (!item.isEnable()) return;
            for (Tags datum : mData) {
                if (!datum.equals(item)) {
                    datum.setCheck(false);
                }
            }
            if (tagClick != null) {
                tagClick.onClick(item);
            }
        });
    }

    public interface OnTagClick {
        void onClick(Tags item);
    }

    public void setTagClick(OnTagClick tagClick) {
        this.tagClick = tagClick;
    }
}
