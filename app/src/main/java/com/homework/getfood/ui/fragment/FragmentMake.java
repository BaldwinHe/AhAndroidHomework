package com.homework.getfood.ui.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.homework.getfood.util.CartListener;
import com.homework.getfood.ui.adapter.AdapterSectionedBaseFoodList;
import com.homework.getfood.util.PinnedHeaderListView;
import com.homework.getfood.R;
import com.homework.getfood.util.UpdateListener;
import com.homework.getfood.bean.FoodBean;
import com.homework.getfood.bean.OrderBean;
import com.homework.getfood.context.AppContext;
import com.homework.getfood.ui.activity.ActivityCheck;
import com.homework.getfood.ui.activity.ActivityMain;
import com.homework.getfood.ui.adapter.AdapterCart;
import com.homework.getfood.ui.adapter.AdapterFoodTypeList;
import com.homework.getfood.util.TimeFetcher;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * 点餐页面Fragment
 */
public class FragmentMake extends Fragment implements View.OnClickListener, CartListener, UpdateListener {

    @BindView(R.id.view_type_list)
    ListView foodtypeListView;

    @BindView(R.id.view_pinned_list)
    PinnedHeaderListView pinnedListView;

    private boolean isScroll = true;
    private AdapterFoodTypeList adapter;

    private ArrayList<String> leftStr;
    private ArrayList<Boolean> flagArray;
    private ArrayList<ArrayList<String>> rightStr;
    private View rootView;

    private HashMap<String,FoodBean> foodMap;
    private ImageView shopping_cart;
    private TextView defaultText;
    private ListView shoppingListView;
    private FrameLayout cardLayout;

    private LinearLayout cardShopLayout;
    private View bg_layout;
    private TextView settlement;
    private AdapterCart adapterCart;
    @SuppressLint("StaticFieldLeak")
    public static TextView shoppingPrice;

    @SuppressLint("StaticFieldLeak")
    public static TextView shoppingNum;

    public FragmentMake() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    private void initData(){
        ArrayList<FoodBean> foodData = AppContext.getData();
        foodMap = AppContext.getMap();
        leftStr = new ArrayList<String>();
        rightStr = new ArrayList<ArrayList<String>>();
        flagArray = new ArrayList<Boolean>();
        boolean Flag = true;
        for (int i = 1; i<= AppContext.getTypeNum(); i++){
            if (i == 1) flagArray.add(true);
            else flagArray.add(false);
            ArrayList<String> temp = new ArrayList<String>();
            for (int j = 0; j < foodData.size(); j++){
                FoodBean x = foodData.get(j);
                if (x.getTypeID() == i){
                    if(Flag){
                        leftStr.add(x.getType());
                        Flag = false;
                    }
                    temp.add(x.getName());
                }
                if (x.getTypeID() > i) break;
            }
            rightStr.add(temp);
            Flag = true;
        }
        bg_layout.setOnClickListener(this);
        settlement.setOnClickListener(this);
        shopping_cart.setOnClickListener(this);
    }
    private void initView(){
        shopping_cart = (ImageView)rootView.findViewById(R.id.shopping_cart);
        defaultText = (TextView)rootView.findViewById(R.id.defaultText);
        shoppingListView = (ListView)rootView.findViewById(R.id.shopproductListView);
        cardLayout = (FrameLayout)rootView.findViewById(R.id.cardLayout);
        cardShopLayout = (LinearLayout)rootView.findViewById(R.id.cardShopLayout);
        bg_layout =  (View) rootView.findViewById(R.id.bg_layout);
        settlement = (TextView)  rootView.findViewById(R.id.settlement);
        shoppingPrice = (TextView) rootView.findViewById(R.id.shoppingPrice);
        shoppingNum = (TextView) rootView.findViewById(R.id.shoppingNum);
        initData();
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.shopping_cart: // 点击购物车
                refreshCart();
                break;
            case R.id.settlement: // 点击结算按钮
                if(AppContext.getCart() == null || AppContext.getCart().isEmpty()){ // 购物车为空无法结算
                    return;
                }else{
                    ArrayList<FoodBean> fb = new ArrayList<FoodBean>();
                    fb.addAll(AppContext.getCart().values());
                    int num = (int) ((Math.random() * 9 + 1) * 1000); // 随机生成订单ID
                    Integer price = Integer.parseInt(getPrice());
                    OrderBean order = new OrderBean(num, TimeFetcher.getTime(),
                            price, fb); // 获得生成新订单
                    ActivityCheck.setOrderData(order); // 设置确认界面里的订单数据
                    Intent intent=new Intent(getActivity(), ActivityCheck.class);
                    startActivity(intent); // 进入确认界面
                }
                break;
            case R.id.bg_layout: // 点击购物车以外的部分，则取消购物车显示
                cardLayout.setVisibility(View.GONE);
                bg_layout.setVisibility(View.GONE);
                cardShopLayout.setVisibility(View.GONE);
                break;

            default:
                cardLayout.setVisibility(View.GONE);
                bg_layout.setVisibility(View.GONE);
                cardShopLayout.setVisibility(View.GONE);
        }
    }

    /**
     * 更新购物车数据
     */
    @Override
    public void updateFood() {
        adapterCart.updateData();
        adapterCart.notifyDataSetChanged();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ActivityMain.setFragment_make(this);
        rootView = inflater.inflate(R.layout.fragment_make, container, false);
        initView();
        pinnedListView = rootView.findViewById(R.id.view_pinned_list);
        ButterKnife.bind(this, rootView);
        final AdapterSectionedBaseFoodList sectionedAdapter = new AdapterSectionedBaseFoodList(rootView.getContext(), leftStr, rightStr, foodMap);
        sectionedAdapter.setCallBackListener(this);
        pinnedListView.setAdapter(sectionedAdapter);
        adapter = new AdapterFoodTypeList(rootView.getContext(), leftStr, flagArray);
        foodtypeListView.setAdapter(adapter);
        foodtypeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                isScroll = false;

                for (int i = 0; i < leftStr.size(); i++) {
                    if (i == position) {
                        flagArray.set(i,true);
                    } else {
                        flagArray.set(i,false);
                    }
                }
                adapter.notifyDataSetChanged();
                int rightSection = 0;
                for (int i = 0; i < position; i++) {
                    rightSection += sectionedAdapter.getCountForSection(i) + 1;
                }
                pinnedListView.setSelection(rightSection);
            }
        });
        adapterCart = new AdapterCart(getActivity(),AppContext.getCart());
        shoppingListView.setAdapter(adapterCart);
        adapterCart.setCartListener(this);

        pinnedListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                // 当不滚动时
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {// 判断滚动到底部
                    if (pinnedListView.getLastVisiblePosition() == (pinnedListView.getCount() - 1)) {
                        foodtypeListView.setSelection(ListView.FOCUS_DOWN);
                    }

                    // 判断滚动到顶部
                    if (pinnedListView.getFirstVisiblePosition() == 0) {
                        foodtypeListView.setSelection(0);
                    }
                }
            }

            int y = 0;
            int x = 0;
            int z = 0;

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (isScroll) {
                    for (int i = 0; i < rightStr.size(); i++) {
                        if (i == sectionedAdapter.getSectionForPosition(pinnedListView.getFirstVisiblePosition())) {
                            flagArray.set(i,true);
                            x = i;
                        } else {
                            flagArray.set(i,false);
                        }
                    }
                    if (x != y) {
                        adapter.notifyDataSetChanged();
                        y = x;
                        if (y == foodtypeListView.getLastVisiblePosition()) {
                            foodtypeListView.setSelection(z);
                        }
                        if (x == foodtypeListView.getFirstVisiblePosition()) {
                            foodtypeListView.setSelection(z);
                        }
                        if (firstVisibleItem + visibleItemCount == totalItemCount - 1) {
                            foodtypeListView.setSelection(ListView.FOCUS_DOWN);
                        }
                    }
                } else {
                    isScroll = true;
                }
            }
        });
        return rootView;
    }

    /**
     * 从购物车中删除某食物
     * @param product 需要删除的食物名称
     */
    @Override
    public void onRemoveFood(FoodBean product) {
        AppContext.getCart().remove(product.getName());
        updateFood();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        ButterKnife.bind(this, rootView);
    }

    /**
     * 设置购物车状态|价格显示
     */
    @SuppressLint("SetTextI18n")
    static public void setPrice() {
        Integer priceSum = 0, foodSum = 0;
        HashMap<String, FoodBean> cart = AppContext.getCart();
        for (FoodBean item : cart.values()){
            foodSum = foodSum + item.getCartNum();
            priceSum = priceSum + item.getCartNum() * item.getPrice();
        }

        if (foodSum > 0){
            shoppingNum.setVisibility(View.VISIBLE);
        }else{
            shoppingNum.setVisibility(View.GONE);
        }
        if (priceSum > 0){
            shoppingPrice.setVisibility(View.VISIBLE);
        }else{
            shoppingPrice.setVisibility(View.GONE);
        }
        shoppingPrice.setText(priceSum.toString() + "  ¥");
        shoppingNum.setText(foodSum.toString());
    }

    /**
     * @return 当前购物车内的总价格
     */
    private String getPrice(){
        Integer priceSum = 0;
        HashMap<String, FoodBean> cart = AppContext.getCart();
        for (FoodBean item : cart.values()){
            priceSum = priceSum + item.getCartNum() * item.getPrice();
        }
        return priceSum.toString();
    }

    /**
     * 更新购物车信息显示
     */
    public void refreshCart(){
        if (AppContext.getCart() == null || AppContext.getCart().isEmpty()) {
            defaultText.setVisibility(View.VISIBLE);
        } else {
            defaultText.setVisibility(View.GONE);
        }

        if (cardLayout.getVisibility() == View.GONE) {
            cardLayout.setVisibility(View.VISIBLE);
            cardShopLayout.setVisibility(View.VISIBLE);


        } else {
            cardLayout.setVisibility(View.GONE);
            bg_layout.setVisibility(View.GONE);
            cardShopLayout.setVisibility(View.GONE);
        }
    }
}
