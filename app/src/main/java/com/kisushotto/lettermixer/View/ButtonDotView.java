package com.kisushotto.lettermixer.View;

import android.content.Context;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.kisushotto.lettermixer.R;

public class ButtonDotView extends RelativeLayout {
    public ButtonDotView(Context context) {
        super(context);
    }

    public ButtonDotView(Context context, int amount) {
        super(context);
        init(context, amount);
    }

    public ButtonDotView(Context context, String content) {
        super(context);
        init(context, content);
    }

    public void init(Context context, int amount){

        for(int i=0; i<amount;i++){
            Button bt = new Button(context);
            bt.setBackgroundResource(R.drawable.gameplay_btn_dot);
            RelativeLayout.LayoutParams size = new LayoutParams(30, 30);
            size.leftMargin = 50*i;
            bt.setLayoutParams(size);
            this.addView(bt);
        }

        LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        this.setLayoutParams(params);
    }
    public void init(Context context, String content){
        for(int i=0; i<content.length(); i++){
            Button bt = new Button(context);
            RelativeLayout.LayoutParams size = new LayoutParams(100, 100);
            size.leftMargin = 50*i;

            bt.setBackgroundColor(getResources().getColor(R.color.transparent));
            bt.setText(content.substring(i,i+1));
            bt.setTextSize(18);

            bt.setTextColor(getResources().getColor(R.color.white));
            bt.setLayoutParams(size);
            this.addView(bt);
        }
    }
    public ButtonDotView getButtonDotView(){
        return this;
    }
    public void setText(String ans){
        for(int i=0; i<ans.length();i++){

        }
    }
}
