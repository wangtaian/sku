package ink.itwo.sku.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ink.itwo.common.util.CollectionUtil;
import ink.itwo.sku.R;
import ink.itwo.sku.entity.Sku;
import ink.itwo.sku.entity.TagType;
import ink.itwo.sku.entity.Tags;
import ink.itwo.sku.listener.OnSkuViewListener;

/**
 Created by wangtaian on 2019/4/4. */
public class SkuView extends LinearLayout {
    private Context context;
    private List<Sku> skus = new ArrayList<>();
    //    private Set<Tags> checkTags = new HashSet<>();
    private Map<TagType, Tags> checkTags = new HashMap<>();
    private Map<TagType, List<Tags>> typeMap = new HashMap<>();
    private Map<TagType, TagAdapter> adapterMap = new HashMap<>();
    private OnSkuViewListener onSkuViewListener;

    public SkuView(Context context) {
        super(context);
        init(context, null);
    }

    public SkuView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public SkuView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    public SkuView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    private void init(Context context, @Nullable AttributeSet attrs) {
        this.context = context;
        LayoutInflater.from(context).inflate(R.layout.layout_container, this, true);
        setOrientation(VERTICAL);
    }
    protected int getTagViewLayoutId() {
        return R.layout.layout_tag;
    }

    public void setSkus(@NonNull List<Sku> skus) {
        this.skus.clear();
        this.skus.addAll(skus);
        removeAllViews();
        post(this::initView);
    }

    public void setOnSkuViewListener(OnSkuViewListener onSkuViewListener) {
        this.onSkuViewListener = onSkuViewListener;
    }

    private void initView() {
        typeMap.clear();
        adapterMap.clear();
        typeMap.putAll(formatData());
        if (CollectionUtil.isEmpty(typeMap)) {
            return;
        }
        List<TagType> list = new ArrayList<>(typeMap.keySet());
        Collections.sort(list, (o1, o2) -> o1.getSort() - o2.getSort());
        for (TagType type : list) {
            List<Tags> tagsList = typeMap.get(type);
            View view = LayoutInflater.from(context).inflate(R.layout.layout_sku, null);
            TextView tvTagType = view.findViewById(R.id.tag_type);
            RecyclerView recyclerView = view.findViewById(R.id.recycler_view);
            FlexboxLayoutManager flexboxLayoutManager = new FlexboxLayoutManager(context);
            flexboxLayoutManager.setFlexDirection(FlexDirection.ROW);
            flexboxLayoutManager.setFlexWrap(FlexWrap.WRAP);
            flexboxLayoutManager.setJustifyContent(JustifyContent.FLEX_START);
            recyclerView.setLayoutManager(flexboxLayoutManager);
            tvTagType.setText(type.getType());
            TagAdapter adapter = new TagAdapter(getTagViewLayoutId(), tagsList);
            adapter.setTagClick(item -> {
                item.toggle();
                if (item.isCheck()) {
                    checkTags.put(item.getType(), item);
                } else {
                    checkTags.remove(item.getType());
                }
                notifyDataSetChanged();
                if (onSkuViewListener != null) {
                    Collection<Tags> values = checkTags.values();
                    for (Sku sku : skus) {
                        boolean b = sku.getTags().containsAll(values);
                        if (b && sku.getTags().size() == values.size()) {
                            //后台数据的sku中保护现在点选的tags，并且tag的数量一致时 已选择所有tag，返回该条sku
                            onSkuViewListener.onTagsClick(item, checkTags, sku);
                            return;
                        }
                    }
                    onSkuViewListener.onTagsClick(item, checkTags, null);
                }
            });
            adapterMap.put(type, adapter);
            adapter.bindToRecyclerView(recyclerView);
            addView(view);
        }
    }

    public synchronized void notifyDataSetChanged() {
        for (TagType type : typeMap.keySet()) {
            List<Tags> tagsList = typeMap.get(type);
            for (Tags tags : tagsList) {
                //逐个遍历tag是否支持可用
                tags.setEnable(tagsEnable(tags));
            }
        }
        for (TagType type : adapterMap.keySet()) {
            adapterMap.get(type).notifyDataSetChanged();
        }
    }

    private boolean tagsEnable(Tags tags) {
        //已点选的tag
        Map<TagType, Tags> tempMap = new HashMap<>(checkTags);
        //待判断的tags放入tempMap中，有相同的 tagType 会替换掉
        tempMap.put(tags.getType(), tags);
        //由已点选的tag+待判断的tag组成一个list，用这个list去后台sku列表中判断，这个list是否可存在
        List<Tags> tempList = new ArrayList<>(tempMap.values());
        for (Sku sku : skus) {
            Set<Tags> tagInSku = sku.getTags();
            //遍历从后台获取的sku，对比后台的sku中的Set<Tags>是否含有 tempList
            boolean b = tagInSku.containsAll(tempList);
            if (b) return true;
        }
        return false;
    }

    private Map<TagType, List<Tags>> formatData() {
        Map<TagType, List<Tags>> map = new HashMap<>();
        for (Sku sku : skus) {
            Set<Tags> tags = sku.getTags();
            if (CollectionUtil.isEmpty(tags)) {
                continue;
            }
            for (Tags tag : tags) {
                if (tag.getType() == null)
                    continue;
                List<Tags> tagsList = map.get(tag.getType());
                if (tagsList == null) {
                    tagsList = new ArrayList<>();
                }
                if (!tagsList.contains(tag)) {
                    tagsList.add(tag);
                }
                map.put(tag.getType(), tagsList);
            }
        }
        return map;
    }

}
