package ink.itwo.sku.view;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import ink.itwo.sku.R;
import ink.itwo.sku.entity.Tags;

/**
 @author wang on 2019-07-09  10:13 */
public class TagAdapter extends RecyclerView.Adapter<TagAdapter.ViewHolder> {
    private int layoutResId;
    private List<Tags> data;
    private TagAdapter.OnTagClick tagClick;

    public TagAdapter(int layoutResId, List<Tags> data) {
        this.layoutResId = layoutResId;
        this.data = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(layoutResId, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Tags item = data.get(position);
        holder.tagView.setText(item.getText());
        holder.tagView.setState(item.isCheck(), item.isEnable());
        holder.tagView.setOnClickListener(v -> {
            if (!item.isEnable()) return;
            for (Tags datum : data) {
                if (!datum.equals(item)) {
                    datum.setCheck(false);
                }
            }
            if (tagClick != null) {
                tagClick.onClick(item);
            }
        });
    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TagView tagView;

        public ViewHolder(View itemView) {
            super(itemView);
            tagView = itemView.findViewById(R.id.tv_tag);
        }

    }

    public interface OnTagClick {
        void onClick(Tags item);
    }

    public void setTagClick(TagAdapter.OnTagClick tagClick) {
        this.tagClick = tagClick;
    }
}

