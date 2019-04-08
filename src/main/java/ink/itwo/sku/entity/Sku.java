package ink.itwo.sku.entity;

import java.util.Set;

import ink.itwo.common.util.CollectionUtil;

/**
 Created by wangtaian on 2019/4/4. */
public class Sku {
    protected Object skuId;
    protected double price;
    protected int count;
    protected Set<Tags> tags;

    public Object getSkuId() {
        return skuId;
    }

    public Sku setSkuId(Object skuId) {
        this.skuId = skuId;
        return this;
    }

    public double getPrice() {
        return price;
    }

    public Sku setPrice(double price) {
        this.price = price;
        return this;
    }

    public int getCount() {
        return count;
    }

    public Sku setCount(int count) {
        this.count = count;
        return this;
    }

    public Set<Tags> getTags() {
        return tags;
    }

    public Sku setTags(Set<Tags> tags) {
        this.tags = tags;
        return this;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Sku)) {
            return false;
        }
        Sku sku = (Sku) obj;
        if (sku.getSkuId() != null && sku.getSkuId().equals(skuId)) {
            return true;
        }
        if (!CollectionUtil.isEmpty(sku.getTags()) && !CollectionUtil.isEmpty(tags)) {
            if (sku.getTags().size() != tags.size()) {
                return false;
            }
            return sku.tags.containsAll(tags);
        }
        return false;

    }
}