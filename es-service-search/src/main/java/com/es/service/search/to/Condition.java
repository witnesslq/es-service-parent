package com.es.service.search.to;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import com.es.service.common.util.PinYinHelper;
import com.es.service.search.type.PinyinType;
import com.es.service.search.util.KeyWordUtil;

/**
 * 
 * 查询条件
 * 
 * @author hailin0@yeah.net
 * @createDate 2016年8月30日
 * 
 */
public class Condition implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 6146524171166292503L;

    /**
     * 字段名
     */
    private String filed;

    /**
     * 字段值
     */
    private String value;

    /**
     * 加权值
     */
    private float boost;

    /**
     * value转换为拼音搜索
     */
    private PinyinType pattern;

    /**
     * value空格分隔
     */
    private boolean spaceSplit;

    
    
    /**
     * 
     */
    public Condition() {
        super();
    }

    public Condition(String filed, String value) {
        super();
        this.filed = filed;
        this.value = value.trim();
    }

    /**
     * @param filed
     * @param value
     * @param valueConvertPinyin
     */
    public Condition(String filed, String value, PinyinType valueConvertPinyin) {
        super();
        this.filed = filed;
        this.value = value.trim();
        this.pattern = valueConvertPinyin;
    }

    /**
     * @param filed
     * @param value
     * @param boost
     */
    public Condition(String filed, String value, float boost) {
        super();
        this.filed = filed;
        this.value = value;
        this.boost = boost;
    }

    /**
     * @param filed
     * @param value
     * @param spaceSplit
     */
    public Condition(String filed, String value, boolean spaceSplit) {
        super();
        this.filed = filed;
        this.value = value.trim();
        this.spaceSplit = spaceSplit;
    }

    public String getFiled() {
        return filed;
    }

    public float getBoost() {
        return boost;
    }

    public String getValue() {
        if (pattern != null) {
            Set<String> words = new HashSet<String>();
            switch (pattern) {
            case PINYIN:
                words.addAll(PinYinHelper.getInstance().getPinYin_Index(value));
                break;
            case PINYIN_PREFIX:
                words.addAll(PinYinHelper.getInstance().getPinYinPrefix_Index(value));
                break;
            case PINYIN_ALL:
                words.addAll(PinYinHelper.getInstance().getPinYin_Index(value));
                words.addAll(PinYinHelper.getInstance().getPinYinPrefix_Index(value));
                break;
            default:
                break;
            }
            return StringUtils.join(words, ",");
        }
        return value;
    }
    
    public boolean isSpaceSplit() {
        return spaceSplit && value.contains(" ");
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((filed == null) ? 0 : filed.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Condition other = (Condition) obj;
        if (filed == null) {
            if (other.filed != null)
                return false;
        } else if (!filed.equals(other.filed))
            return false;
        return true;
    }

    public static boolean isContainFiled(String filed, List<Condition> conditions) {
        if (conditions != null) {
            for (Condition condition : conditions) {
                if (condition.getFiled().equals(filed)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static List<Condition> getContainByFiled(String filed, List<Condition> conditions) {
        List<Condition> newconditions = new ArrayList<Condition>();
        if (conditions != null) {
            for (Condition condition : conditions) {
                if (condition.getFiled().equals(filed)) {
                    newconditions.add(condition);
                }
            }
        }
        return newconditions;
    }

    public boolean isNeedBoost() {
        return boost == 0f ? false : true;
    }
}