package com.homework.getfood;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.DigitsKeyListener;
import android.view.KeyEvent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.view.View;
import android.widget.Toast;

import co.ceryle.radiorealbutton.RadioRealButton;
import co.ceryle.radiorealbutton.RadioRealButtonGroup;

public class DialogDetail extends Dialog {
    final private static String[] spicyDegreeString = new String[]{"(微辣)","(中辣)", "(猛辣)"};
    private static int spicyDegreeID = 0;
    private Button yesButton;
    private Button noButton;
    private TextView foodName;
    private ImageView foodImage;
    private TextView foodPrice;
    private TextView foodNumber;
    private Integer totalPrice;
    private Integer onePrice;
    private RadioRealButtonGroup canSpicyButtonGroup;
    private RadioRealButton minSpicyBtn = (RadioRealButton) findViewById(R.id.minButton);
    private RadioRealButton midSpicyBtn = (RadioRealButton) findViewById(R.id.midButton);
    private RadioRealButton maxSpicyBtn = (RadioRealButton) findViewById(R.id.maxButton);
    private String foodNameStr;//从外界设置的title文本
    private String messageStr;//从外界设置的消息文本
    private String foodPriceStr;
    private onNoOnclickListener noOnclickListener;
    private onYesOnclickListener yesOnclickListener;
    private boolean canSpicy;
    private ImageButton addButton;
    private ImageButton minusButton;
    private Integer foodNum = 1;
    private String yesStr, noStr;
    private int imageID;
    private Context mContext;
    public DialogDetail(Context context,int imageId) {
        super(context, R.style.DetailDialog);
        spicyDegreeID = 0;
        mContext = context;
        imageID = imageId;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_select);
        setCanceledOnTouchOutside(false);

        initView();

        initData();

        initEvent();
    }

    private void initView(){
        yesButton = (Button) findViewById(R.id.yes);
        noButton = (Button) findViewById(R.id.no);

        foodNumber = (TextView) findViewById(R.id.foodNum);

        foodName = (TextView) findViewById(R.id.foodName);
        foodPrice = (TextView) findViewById(R.id.foodPrice);

        foodImage = (ImageView) findViewById(R.id.foodImage);

        canSpicyButtonGroup = (RadioRealButtonGroup) findViewById(R.id.canSpicyGroup);

        addButton = (ImageButton) findViewById(R.id.addButton);
        minusButton = (ImageButton) findViewById(R.id.minusButton);

        canSpicyButtonGroup.setOnClickedButtonListener(new RadioRealButtonGroup.OnClickedButtonListener() {
            @Override
            public void onClickedButton(RadioRealButton button, int position) {
                //Toast.makeText(mContext, "Clicked! Position: " + position, Toast.LENGTH_SHORT).show();
                spicyDegreeID = position;
            }
        });


        if (canSpicy == false){
            canSpicyButtonGroup.setVisibility(View.GONE);
        }else{
            canSpicyButtonGroup.setVisibility(View.VISIBLE);
        }

        foodImage.setImageResource(imageID);
    }

    private void initEvent(){
        //设置确定按钮被点击后，向外界提供监听
        yesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (yesOnclickListener != null) {
                    System.out.println("Yes!");
                    yesOnclickListener.onYesClick();
                }
            }
        });
        //设置取消按钮被点击后，向外界提供监听
        noButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (noOnclickListener != null) {
                    System.out.println("No");
                    noOnclickListener.onNoClick();
                }
            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                String num = foodNumber.getText().toString();
                Integer n;
                if(num.length() >= 1) n = Integer.parseInt(num);
                else n = 0;
                if (n < 1000) n++;
                totalPrice = n * onePrice;
                foodNum = n;
                foodPrice.setText(totalPrice.toString() + "¥");
                foodNumber.setText(n.toString());
            }
        });
        minusButton.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                String num = foodNumber.getText().toString();
                Integer n;
                if(num.length() >= 1) n = Integer.parseInt(num);
                else n = 0;
                if (n > 1) n--;
                totalPrice = n * onePrice;
                foodNum = n;
                foodPrice.setText(totalPrice.toString() + "¥");
                foodNumber.setText(n.toString());
            }
        });

    }
    private void initData(){
        if (foodNameStr != null){
            foodName.setText(foodNameStr);
        }
        if (foodPriceStr != null){
            foodPrice.setText(foodPriceStr);
        }
    }
    public void setName(String name){
        foodNameStr = name;
    }
    public void setInfo(String name, String price, boolean spicy){
        onePrice = Integer.parseInt(price);
        foodPriceStr = price + "¥";
        foodNameStr = name;
        canSpicy = spicy;
    }
    public Integer getInfo(){
        return foodNum;
    }
    public interface onYesOnclickListener {
        public void onYesClick();
    }

    public interface onNoOnclickListener {
        public void onNoClick();
    }

    public void setNoOnclickListener(String str, onNoOnclickListener onNoOnclickListener) {
        if (str != null) {
            noStr = str;
        }
        this.noOnclickListener = onNoOnclickListener;
    }
    public static String getSpicy(){
        return spicyDegreeString[spicyDegreeID];
    }
    /**
     * 设置确定按钮的显示内容和监听
     *
     * @param str
     * @param onYesOnclickListener
     */
    public void setYesOnclickListener(String str, onYesOnclickListener onYesOnclickListener) {
        if (str != null) {
            yesStr = str;
        }
        this.yesOnclickListener = onYesOnclickListener;
    }

}
