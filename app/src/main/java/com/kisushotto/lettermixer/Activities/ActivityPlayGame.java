package com.kisushotto.lettermixer.Activities;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.kisushotto.lettermixer.R;
import com.kisushotto.lettermixer.Ultils.TimeFormatter;
import com.kisushotto.lettermixer.View.ButtonDotView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class ActivityPlayGame extends AppCompatActivity {
    private Button btn_ans_1;
    private Button btn_ans_2;
    private Button btn_ans_3;
    private Button btn_ans_4;
    private Button btn_ans_5;
    private Button btn_ans_6;

    private Button btn_ans_place_1;
    private Button btn_ans_place_2;
    private Button btn_ans_place_3;
    private Button btn_ans_place_4;
    private Button btn_ans_place_5;
    private Button btn_ans_place_6;

    private Button btn_sub;
    private Button btn_del;
    private Button btn_hint;
    private Button btn_quit;
    private Button btn_help;

    private Button btn_pause_play;

    private LinearLayout ans_group_3;
    private LinearLayout ans_group_4;
    private LinearLayout ans_group_5;
    private LinearLayout ans_group_6;

    private ScrollView scroll_3_letter;
    private ScrollView scroll_4_letter;
    private ScrollView scroll_5_letter;
    private ScrollView scroll_6_letter;

    private TextView txt_time;
    private TextView txt_hint_count;

    //các index cần thiết
    private static int letters_inputed = 0;

    //các biến phụ
    private int time_count;
    private CountDownTimer countDownTimer;

    //các mảng phụ cần thiết
    private static List<Integer> input_order = new ArrayList<>();


    private List<Button> btn_ans_list;
    private List<Button> btn_ans_place_list;
    private List<Integer> letter_completed;
    private List<LinearLayout> ans_groups_list;
    private List<ScrollView> scroll_view_list;

    HashMap<String,List<String>> gameData = new HashMap<String, List<String>>() {
        {
            put("letters", Arrays.asList("abcdef"));

            put("time", Arrays.asList("200"));

            put("3",Arrays.asList("abc", "ace", "adc", "abd"));
            put("4",Arrays.asList("abcd", "bcde"));
            put("5",Arrays.asList("adcfe", "acbef"));
            put("6",Arrays.asList("abcdef", "bcdeaf"));
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_game);

        btn_ans_1 = findViewById(R.id.btn_ans_1);
        btn_ans_2 = findViewById(R.id.btn_ans_2);
        btn_ans_3 = findViewById(R.id.btn_ans_3);
        btn_ans_4 = findViewById(R.id.btn_ans_4);
        btn_ans_5 = findViewById(R.id.btn_ans_5);
        btn_ans_6 = findViewById(R.id.btn_ans_6);

        btn_ans_place_1 = findViewById(R.id.btn_ans_place_1);
        btn_ans_place_2 = findViewById(R.id.btn_ans_place_2);
        btn_ans_place_3 = findViewById(R.id.btn_ans_place_3);
        btn_ans_place_4 = findViewById(R.id.btn_ans_place_4);
        btn_ans_place_5 = findViewById(R.id.btn_ans_place_5);
        btn_ans_place_6 = findViewById(R.id.btn_ans_place_6);

        btn_sub = findViewById(R.id.btn_sub);
        btn_del = findViewById(R.id.btn_del);
        btn_help = findViewById(R.id.btn_help);
        btn_hint = findViewById(R.id.btn_hint);

        //xét heigh cho nút gợi ý bằng nút trợ giúp
        btn_help.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                btn_help.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                int x = btn_help.getMeasuredWidth();
                btn_hint.getLayoutParams().height = x;
            }
        });


        btn_pause_play = findViewById(R.id.btn_pause);
        btn_pause_play.setTag("0");

        ans_group_3 = findViewById(R.id.ans_3_letter);
        ans_group_4 = findViewById(R.id.ans_4_letter);
        ans_group_5 = findViewById(R.id.ans_5_letter);
        ans_group_6 = findViewById(R.id.ans_6_letter);

        scroll_3_letter = findViewById(R.id.scroll_3_letter);
        scroll_4_letter = findViewById(R.id.scroll_4_letter);
        scroll_5_letter = findViewById(R.id.scroll_5_letter);
        scroll_6_letter = findViewById(R.id.scroll_6_letter);

        txt_time = findViewById(R.id.txt_time);
        txt_hint_count = findViewById(R.id.txt_hint_count);



        btn_ans_list = Arrays.asList(btn_ans_1,btn_ans_2, btn_ans_3, btn_ans_4, btn_ans_5, btn_ans_6);

        btn_ans_place_list = Arrays.asList(btn_ans_place_1,btn_ans_place_2, btn_ans_place_3,
                btn_ans_place_4, btn_ans_place_5, btn_ans_place_6);

        ans_groups_list = Arrays.asList(ans_group_3, ans_group_4, ans_group_5, ans_group_6);

        letter_completed = Arrays.asList(0,0,0,0);

        scroll_view_list = Arrays.asList(scroll_3_letter, scroll_4_letter, scroll_5_letter, scroll_6_letter);


        //////////
        //set data
        Setup_Btn_Ans_Array(btn_ans_list);
        Data_Loader(gameData.get("letters").get(0), btn_ans_list);
        initialAnswerGroup(gameData, ans_groups_list);
        //end set data

        ////////////
        //sự kiện cho nút submit
        btn_sub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //nếu đáp án hợp lệ
                if(letters_inputed >=3) {
                    //lấy đáp án đã nhập
                    String ans = "";
                    for(int i = 0; i< btn_ans_place_list.size(); i++){
                        ans = ans + btn_ans_place_list.get(i).getText();
                    }


                    ////////////////////////////////////////////////////
                    //nếu đáp án đúng
                    List<String> ans_list = gameData.get(String.valueOf(letters_inputed));
                    if(ans_list.contains(ans)){
                        //hiển thị đáp án theo thứ tự trả lời
                        int completed_ans = letter_completed.get(letters_inputed -3);

                        ans_groups_list.get(letters_inputed - 3).removeViewAt(completed_ans);
                        letter_completed.set(letters_inputed - 3, completed_ans + 1);

                        ButtonDotView btn_ans_dot = new ButtonDotView(getApplicationContext(), ans);
                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        if(completed_ans == 0) {
                            params.topMargin = 40;
                            params.gravity = Gravity.CENTER;
                            btn_ans_dot.setLayoutParams(params);
                            ans_groups_list.get(letters_inputed - 3).addView(btn_ans_dot, completed_ans);
                        }
                        else {
                            params.topMargin = 10;
                            params.gravity = Gravity.CENTER;
                            btn_ans_dot.setLayoutParams(params);
                            ans_groups_list.get(letters_inputed - 3).addView(btn_ans_dot, completed_ans);

                            scroll_view_list.get(letters_inputed - 3).scrollTo(0, 100 * completed_ans);
                        }
                    }


                    //reset ô đáp án
                    for (Button bt : btn_ans_place_list
                            ) {
                        bt.setText("");
                    }
                    //reset các ô kí tự
                    for (int i = 0; i < btn_ans_list.size(); i++) {
                        if ((btn_ans_list.get(i).getTag().toString().equals("1"))) {
                            btn_ans_list.get(i).setTag("0");
                            btn_ans_list.get(i).setBackgroundResource(R.drawable.gameplay_btn_ans);
                            btn_ans_list.get(i).setTextColor(getResources().getColor(R.color.black));
                        }
                    }
                    //reset index
                    letters_inputed = 0;
                    input_order.clear();
                }
            }
        });

        /////////////
        //sự kiện cho nút delete
        btn_del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(letters_inputed > 0){
                    letters_inputed--;
                    btn_ans_place_list.get(letters_inputed).setText("");
                    Button btn = btn_ans_list.get(input_order.get(letters_inputed));
                    input_order.remove(letters_inputed);
                    btn.setTag("0");
                    btn.setBackgroundResource(R.drawable.gameplay_btn_ans);
                    btn.setTextColor(getResources().getColor(R.color.black));
                }

            }
        });

        //sự kiện tạm ngưng chơi
        btn_pause_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                countDownTimer.cancel();
                btn_pause_play.setBackgroundResource(R.drawable.gameplay_icon_play);

                // custom dialog
                onPause();
            }
        });

    }

    //set các dữ liệu trên màn hình
    private void Data_Loader(String letterSequence, List<Button> btn_array){
        if(letterSequence.length() != btn_array.size())
            return;
        for (int i=0; i<letterSequence.length(); i++){
            String key = letterSequence.substring(i,i+1);
            btn_array.get(i).setText(key);
        }

        time_count = Integer.valueOf(gameData.get("time").get(0));
        String  time_string = TimeFormatter.getDurationString(time_count);
        txt_time.setText(time_string);

        //đồng hồ đếm ngược
        countDownTimer = new CountDownTimer(time_count*1000, 1000) {

            public void onTick(long millisUntilFinished) {
                txt_time.setText(TimeFormatter.getDurationString(time_count--));
                //here you can have your logic to set text to edittext
            }

            public void onFinish() {
                txt_time.setText("You lose =)");
            }

        }.start();
    }

    //đặt sự kiện cho các button array
    private void Setup_Btn_Ans_Array(List<Button> btn_array){
        for(int i=0;i<btn_array.size();i++){
            final Button btn_ans = btn_array.get(i);
            btn_ans.setTag("0");
            btn_ans.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if((btn_ans.getTag().toString().equals("0"))) {
                        btn_ans.setTag("1");
                        btn_ans.setBackgroundResource(R.drawable.gameplay_btn_ans_pressed);
                        btn_ans.setTextColor(getResources().getColor(R.color.white));
                        btn_ans_place_list.get(letters_inputed).setText(btn_ans.getText());
                        letters_inputed++;
                        input_order.add(btn_ans_list.indexOf(btn_ans));
                    }
                }
            });
        }
    }

    //tạo các đáp án cần tìm theo dữ liệu truyền vào
    private void initialAnswerGroup(HashMap<String,List<String>> gameData, List<LinearLayout> ans_groups_list){
        for (int i=0; i<ans_groups_list.size(); i++){
            List<String> ans_letter = gameData.get(String.valueOf(i+3));
            generateButtonDot(0, 120, i);
            for(int j=1; j<ans_letter.size()-1; j++){
                generateButtonDot(j, 50, i);
            }
            generateLastButtonDot(ans_letter.size()-1, 50, i, 50);
        }
    }

    //tạo một đáp án cần tìm
    private void generateButtonDot(int index_view_add, int top_margin, int index_letter){
        ButtonDotView btn_ans_dot = new ButtonDotView(this, index_letter+3);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.topMargin = top_margin;
        params.gravity = Gravity.CENTER;
        btn_ans_dot.setLayoutParams(params);
        ans_groups_list.get(index_letter).addView(btn_ans_dot, index_view_add);
    }

    //tạo đáp án cần tìm ở hàng cuối cừng
    private void generateLastButtonDot(int index_view_add, int top_margin, int index_letter, int bottom_margin){
        ButtonDotView btn_ans_dot = new ButtonDotView(this, index_letter+3);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.topMargin = top_margin;
        params.bottomMargin = bottom_margin;
        params.gravity = Gravity.CENTER;
        btn_ans_dot.setLayoutParams(params);
        ans_groups_list.get(index_letter).addView(btn_ans_dot, index_view_add);
    }

    //ghi đè onPause
    @Override
    protected void onPause() {
        super.onPause();
        initialPauseDialog();
    }

    //tạo dialog ngừng chơi
    public void initialPauseDialog(){
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.pause_dialog);

        Window window = dialog.getWindow();
        window.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        ImageView img_music_louder = dialog.findViewById(R.id.img_music_louder);
        ImageView img_music_quieter = dialog.findViewById(R.id.img_music_quieter);
        ImageView img_sound_louder = dialog.findViewById(R.id.img_sound_louder);
        ImageView img_sound_quiter = dialog.findViewById(R.id.img_sound_quieter);

        final ImageView img_sound_level = dialog.findViewById(R.id.img_sound_level);
        final ImageView img_music_level = dialog.findViewById(R.id.img_music_level);

        img_music_quieter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                img_music_level.setImageResource(R.drawable.volume_level_08);
                Log.i("PlayGame", "yeafsasfafas");
            }
        });
        dialog.show();
    }
}
