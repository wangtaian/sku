package ink.itwo.sku.listener;

import java.util.Map;

import ink.itwo.sku.entity.Sku;
import ink.itwo.sku.entity.TagType;
import ink.itwo.sku.entity.Tags;

/**
 Created by wangtaian on 2019/4/6. */
@FunctionalInterface
public interface OnSkuViewListener {
    void onTagsClick(Tags tags, Map<TagType, Tags> checkTags, Sku sku);
}
