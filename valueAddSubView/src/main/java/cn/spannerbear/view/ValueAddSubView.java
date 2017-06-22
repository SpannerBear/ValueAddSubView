package cn.spannerbear.view;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import cn.bannerbear.view.R;

/**
 * Created by SpannerBear on 2017/3/22.
 * use to: 加减控件
 * 基本功能:
 * 加减数量
 * 设置最大最小值
 */

public class ValueAddSubView extends FrameLayout {
    
    private ImageView mIvSub;
    private ImageView mIvAdd;
    private EditText mEtCount;
    private View mDividerLeft;
    private View mDividerRight;
    
    protected int mDefaultValue = 0;//默认数值
    protected int mMinValue = 0;
    protected int mMaxValue = 99;
    protected int mCurrValue = mDefaultValue;//当前数值
    
    protected boolean mIsListenValueChangeAfterOnAddOrSub = true;//加减是否触发监听
    protected boolean mShouldListenFlag = true;//当前是否应该监听数值改变
    private boolean mIgnore = false;//是否无视处理逻辑
    protected boolean mIsOutReset = false;//是否越界重置
    
    private OnValueAddOrSubListener mOnValueAddOrSubListener;//外部监听器
    private OnValueChangeListener mOnValueChangeListener;
    
    public ValueAddSubView(@NonNull Context context) {
        this(context, null);
    }
    
    public ValueAddSubView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }
    
    public ValueAddSubView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initViews();
        init();
        initParams(context, attrs);
    }
    
    private void initViews() {
        View view = inflate(getContext(), R.layout.view_number_add_sub, this);
        mIvAdd = (ImageView) view.findViewById(R.id.iv_add);
        mIvSub = (ImageView) view.findViewById(R.id.iv_sub);
        mEtCount = (EditText) view.findViewById(R.id.et_count);
        mDividerLeft = view.findViewById(R.id.dividerLeft);
        mDividerRight = view.findViewById(R.id.dividerRight);
    }
    
    private void initParams(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ValueAddSubView);
        int textColor = typedArray.getColor(R.styleable.ValueAddSubView_vTextColor, 0);
        int textColorRes = typedArray.getResourceId(R.styleable.ValueAddSubView_vTextColor, 0);
        int textBgColor = typedArray.getColor(R.styleable.ValueAddSubView_vTextBg, 0);
        int textBgRes = typedArray.getResourceId(R.styleable.ValueAddSubView_vTextBg, 0);
        int addIconRes = typedArray.getResourceId(R.styleable.ValueAddSubView_vAddIcon, R.drawable.selector_countview_iv_add);
        int subIconRes = typedArray.getResourceId(R.styleable.ValueAddSubView_vSubIcon, R.drawable.selector_countview_iv_reduce);
        int defaultValue = typedArray.getInt(R.styleable.ValueAddSubView_vDefaultValue, 0);
        int max = typedArray.getInt(R.styleable.ValueAddSubView_vMaxValue, 99);
        int min = typedArray.getInt(R.styleable.ValueAddSubView_vMinValue, 0);
        int maxWidth = typedArray.getInt(R.styleable.ValueAddSubView_vTextMaxWidth, 0);
        int minWidth = typedArray.getInt(R.styleable.ValueAddSubView_vTextMinWidth, 0);
        float dividerWidth = typedArray.getDimension(R.styleable.ValueAddSubView_vDividerWidth, 0);
        int dividerColor = typedArray.getColor(R.styleable.ValueAddSubView_vDividerColor, 0);
        float height = typedArray.getDimension(R.styleable.ValueAddSubView_vHeight, 0);
        
        typedArray.recycle();
        if (textColor != 0) {
            mEtCount.setTextColor(textColor);
        } else if (textColorRes != 0) {
            ColorStateList colorStateList = getResources().getColorStateList(textColorRes);
            mEtCount.setTextColor(colorStateList);
        }
        
        if (textBgColor != 0) {
            mEtCount.setBackgroundColor(textBgColor);
        } else if (textBgRes != 0) {
            mEtCount.setBackgroundResource(textBgRes);
        }
        
        mIvAdd.setBackgroundResource(addIconRes);
        mIvSub.setBackgroundResource(subIconRes);
        
        mMaxValue = max;
        mMinValue = min;
        mDefaultValue = defaultValue;
        setValue(mDefaultValue, false);
        
        if (maxWidth != 0) {
            mEtCount.setMaxWidth(maxWidth);
        }
        if (minWidth != 0) {
            mEtCount.setMinWidth(minWidth);
        }
        if (dividerWidth != 0) {
            ViewGroup.LayoutParams layoutParams = mDividerLeft.getLayoutParams();
            ViewGroup.LayoutParams layoutParams1 = mDividerRight.getLayoutParams();
            layoutParams.width = (int) dividerWidth;
            layoutParams1.width = (int) dividerWidth;
            mDividerLeft.setLayoutParams(layoutParams);
            mDividerRight.setLayoutParams(layoutParams1);
        }
        
        if (dividerColor != 0) {
            mDividerLeft.setBackgroundColor(dividerColor);
            mDividerRight.setBackgroundColor(dividerColor);
        }
        
        if (height != 0) {
            ViewGroup.LayoutParams etParams = mEtCount.getLayoutParams();
            etParams.height = (int) (height + 0.5f);
            mEtCount.setLayoutParams(etParams);
            
            ViewGroup.LayoutParams leftParams = mIvAdd.getLayoutParams();
            leftParams.height = (int) (height + 0.5f);
            leftParams.width = (int) (height + 0.5f);
            mIvAdd.setLayoutParams(leftParams);
            
            ViewGroup.LayoutParams rightParams = mIvSub.getLayoutParams();
            rightParams.height = (int) (height + 0.5f);
            rightParams.width = (int) (height + 0.5f);
            mIvSub.setLayoutParams(rightParams);
        }
        initIcon();
    }
    
    private void init() {
        mEtCount.setSelectAllOnFocus(true);//默认点击全选数值
        
        setFocusable(true);
        setFocusableInTouchMode(true);
        
        OnClickListener mOnAddSubListener = new OnClickListener() {
            @Override
            public void onClick(View v) {
                int temp = mCurrValue;
                if (v.getId() == R.id.iv_add) {
                    //加
                    temp++;
                    if (temp <= mMaxValue) {
                        mCurrValue++;
                        String format = Integer.toString(mCurrValue);
                        mEtCount.setText(format);
                        mEtCount.setSelection(format.length());
                        if (mOnValueAddOrSubListener != null) {
                            mOnValueAddOrSubListener.onValueAddOrSub(1);
                        }
                        initIcon();
                    } else {
                        showOutOfIndexMessage(true);
                    }
                } else {
                    //减
                    temp--;
                    if (temp >= mMinValue) {
                        mCurrValue--;
                        String format = Integer.toString(mCurrValue);
                        mEtCount.setText(format);
                        mEtCount.setSelection(format.length());
                        if (mOnValueAddOrSubListener != null) {
                            mOnValueAddOrSubListener.onValueAddOrSub(-1);
                        }
                        initIcon();
                    } else {
                        showOutOfIndexMessage(false);
                    }
                }
            }
        };
        mIvAdd.setOnClickListener(mOnAddSubListener);
        mIvSub.setOnClickListener(mOnAddSubListener);
        
        mEtCount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                
            }
            
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                
            }
            
            @Override
            public void afterTextChanged(Editable s) {
                afterTextChange(s);
            }
        });
        
        //焦点监听,输入空自动重置数值
        mEtCount.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) return;
                String trim = mEtCount.getText().toString().trim();
                if (trim.isEmpty()) {
                    setValue(mDefaultValue);
                } else if (parseString2Int(trim) < mMinValue) {
                    showOutOfIndexMessage(false);
                    setValue(mMinValue);
                }
            }
        });
    }
    
    /**
     * 主要逻辑
     *
     * @param s
     */
    protected void afterTextChange(Editable s) {
        if (mIgnore) {
            return;
        }
        String trim = s.toString().trim();
        if ("".equals(trim)) {
            return;
        }
        int strValue = parseString2Int(trim);
        if (strValue < mMinValue) {
            return;
        }
        
        //0是特别的,后边不会有更多的数字,并且可以处理输入多个0的情况
        if (strValue == 0 && mMinValue > 0) {
            setValue(mMinValue);
            toChangeListener(mMinValue);
            return;
        }
        
        //防止越界重置数值的监听
        if (mIsOutReset && mCurrValue == strValue) {
            mIsOutReset = false;
            return;
        }
        
        //加减监听
        if (mIsListenValueChangeAfterOnAddOrSub && mCurrValue == strValue) {
            toChangeListener(mCurrValue);
            return;
        }
        
        //处理数值上越界
        if (strValue > mMaxValue) {
            mIsOutReset = true;
            handleOut(mMaxValue, true);
            toChangeListener(mMaxValue);
            return;
        }//为了能够随意输入,不处理下越界
        
        mCurrValue = strValue;
        initIcon();
        toChangeListener(strValue);
    }
    
    private final void toChangeListener(int value) {
        if (mShouldListenFlag && mOnValueChangeListener != null) {
            mOnValueChangeListener.onValueChange(value);
        }
    }
    
    /**
     * 设置数值后调用,影响加减控件enable状态
     */
    protected final void initIcon() {
        if (!isEnabled()) return;
        mIvAdd.setEnabled(mCurrValue + 1 <= mMaxValue);
        mIvSub.setEnabled(mCurrValue - 1 >= mMinValue);
    }
    
    /**
     * 越界输入处理
     *
     * @param value
     * @param isOutOfMax
     */
    private void handleOut(int value, boolean isOutOfMax) {
        showOutOfIndexMessage(isOutOfMax);
        setValueNoLogic(value);
    }
    
    /**
     * 显示输入限制提示
     *
     * @param isOutMax true:超过最大值
     *                 false:小于最小值
     */
    protected void showOutOfIndexMessage(boolean isOutMax) {
        if (isOutMax) {
            Toast.makeText(getContext(), R.string.out_of_max, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getContext(), R.string.out_of_min, Toast.LENGTH_SHORT).show();
        }
    }
    
    /**
     * 设置数值.该方法默认能够被监听
     *
     * @param value 数值
     */
    public void setValue(int value) {
        setValue(value, true);
    }
    
    /**
     * 设置数值
     *
     * @param value        数值
     * @param shouldListen 是否应该监听这次的数值改变
     */
    public void setValue(int value, boolean shouldListen) {
        if (value < mMinValue) {
            value = mMinValue;
        } else if (value > mMaxValue) {
            value = mMaxValue;
        }
        mCurrValue = value;
        mShouldListenFlag = shouldListen;
        String valueStr = Integer.toString(mCurrValue);
        mEtCount.setText(valueStr);
        mEtCount.setSelection(valueStr.length());
        initIcon();
        mShouldListenFlag = true;
    }
    
    /**
     * 设置数值,默认不监听这次的数值改变,除非越界.适合用于初始值可能越界的情况.
     *
     * @param value
     */
    public void setValueHasIndex(int value) {
        if (value < mMinValue) {
            value = mMinValue;
            setValue(value);
        } else if (value > mMaxValue) {
            value = mMaxValue;
            setValue(value);
        } else {
            setValueNoLogic(value);
        }
    }
    
    /**
     * 设置数值,无视所有value纠正逻辑,并且此次设置没有监听
     *
     * @param value 数值
     */
    public final void setValueNoLogic(int value) {
        if (value < mMinValue) {
            value = mMinValue;
        } else if (value > mMaxValue) {
            value = mMaxValue;
        }
        mCurrValue = value;
        mIgnore = true;
        String valueStr = Integer.toString(mCurrValue);
        mEtCount.setText(valueStr);
        mEtCount.setSelection(valueStr.length());
        initIcon();
        mIgnore = false;
    }
    
    public int getValue() {
        return mCurrValue;
    }
    
    public void setMaxValue(int max) {
        this.mMaxValue = max;
        initIcon();
    }
    
    public int getMaxValue() {
        return mMaxValue;
    }
    
    public void setMinValue(int min) {
        this.mMinValue = min;
        initIcon();
    }
    
    public int getMinValue() {
        return mMinValue;
    }
    
    /**
     * 设置默认数值,在数值解析异常,或数值为空时，或没有调用{@link #setValue(int)}时,
     * 会填入改默认数值(该方法应该在{@link #setValue(int)}之前调用)
     *
     * @param defaultValue 默认数值
     */
    public void setDefaultValue(int defaultValue) {
        this.mDefaultValue = defaultValue;
        mEtCount.setText(Integer.toString(defaultValue));
    }
    
    public EditText getEditText() {
        return mEtCount;
    }
    
    /**
     * 设置数值加减监听
     *
     * @param listener 点击加减按钮时的监听
     */
    public void setOnValueAddOrSubListener(OnValueAddOrSubListener listener) {
        this.mOnValueAddOrSubListener = listener;
    }
    
    /**
     * 设置数值改变监听
     *
     * @param listener 当控件中的数值改变时,数值会在监听器中返回
     */
    public void setOnValueChangeListener(OnValueChangeListener listener) {
        this.mOnValueChangeListener = listener;
    }
    
    /**
     * 如果设置了false,点击加减键引起的数值改变将不会触发 {@link #setOnValueChangeListener(OnValueChangeListener)}中的监听,
     * 主要用于防止重复监听同一数值事件
     *
     * @param isListen 是否监听
     */
    public void isListenValueChangeOnAddOrSub(boolean isListen) {
        mIsListenValueChangeAfterOnAddOrSub = isListen;
    }
    
    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        if (enabled) {
            mEtCount.setEnabled(true);
            initIcon();
        } else {
            mIvSub.setEnabled(false);
            mIvAdd.setEnabled(false);
            mEtCount.setEnabled(false);
        }
    }
    
    @Deprecated
    private void setViewGroupChildsEnable(ViewGroup viewGroup, boolean enable) {
        for (int i = 0; i < viewGroup.getChildCount(); i++) {
            View v = viewGroup.getChildAt(i);
            if (v instanceof ViewGroup) {
                setViewGroupChildsEnable((ViewGroup) v, enable);
            } else {
                v.setEnabled(enable);
            }
            
        }
    }
    
    private int parseString2Int(String trim) {
        try {
            return Integer.parseInt(trim);
        } catch (Exception e) {
//            e.printStackTrace();
            return mDefaultValue;
        }
    }
    
    /**
     * dp转px
     *
     * @param context
     * @param dpVal
     * @return
     */
    public static int dp2px(Context context, float dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpVal, context.getResources().getDisplayMetrics());
    }
    
    public interface OnValueChangeListener {
        void onValueChange(int count);
    }
    
    public interface OnValueAddOrSubListener {
        void onValueAddOrSub(int Count);
    }
}
