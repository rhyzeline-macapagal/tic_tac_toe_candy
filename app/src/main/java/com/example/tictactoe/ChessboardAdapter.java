package com.example.tictactoe;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.media.MediaPlayer;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tictactoe.Fragments.GameFragment;

import java.util.ArrayList;
import java.util.Random;

public class ChessboardAdapter extends RecyclerView.Adapter<ChessboardAdapter.ViewHolder> {
    private MediaPlayer mp;
    private Context context;
    private ArrayList<Bitmap> arrBms, arrBmTest;
    private Bitmap bmO, bmX, draw;
    private ArrayList<Bitmap> arrStroke;
    private String winCharacter = "o";
    private Animation anim_x_o, animStroke, animWin;
    private boolean checkMax = true;
    private int depth = 0;

    public ChessboardAdapter(Context context, ArrayList<Bitmap> arrBms) {
        this.context = context;
        this.arrBms = arrBms;
        bmO = BitmapFactory.decodeResource(context.getResources(), R.drawable.o);
        bmX = BitmapFactory.decodeResource(context.getResources(), R.drawable.x);
        draw = BitmapFactory.decodeResource(context.getResources(), R.drawable.draw);
        arrStroke = new ArrayList<>();
        arrStroke.add(BitmapFactory.decodeResource(context.getResources(), R.drawable.stroke1));
        arrStroke.add(BitmapFactory.decodeResource(context.getResources(), R.drawable.stroke2));
        arrStroke.add(BitmapFactory.decodeResource(context.getResources(), R.drawable.stroke3));
        arrStroke.add(BitmapFactory.decodeResource(context.getResources(), R.drawable.stroke4));
        arrStroke.add(BitmapFactory.decodeResource(context.getResources(), R.drawable.stroke5));
        arrStroke.add(BitmapFactory.decodeResource(context.getResources(), R.drawable.stroke6));
        arrStroke.add(BitmapFactory.decodeResource(context.getResources(), R.drawable.stroke7));
        arrStroke.add(BitmapFactory.decodeResource(context.getResources(), R.drawable.stroke8));
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_table, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        holder.img_item_table.setImageBitmap(arrBms.get(position));
        anim_x_o = AnimationUtils.loadAnimation(context, R.anim.anim_x_o);
        holder.img_item_table.setAnimation(anim_x_o);
        animStroke = AnimationUtils.loadAnimation(context, R.anim.anim_stroke);
        GameFragment.img_stroke.setAnimation(animStroke);
        animWin = AnimationUtils.loadAnimation(context, R.anim.anim_stroke);
        if (MainActivity.multiPlayer){
            playWithComputer(holder, position);

        }else{
            playWith2Player(holder, position);

        }
        if (!checkWin()) checkDraw();
    }


    private void checkDraw() {
        int count = 0;
        for (int i = 0; i < arrBms.size(); i++){
            if (arrBms.get(i)!=null){
                count++;
            }
        }
        if (count == 9){
            mp = MediaPlayer.create(context, R.raw.start);
            mp.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mediaPlayer) {
                    mp.start();
                }
            });

            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    mp.release();
                }
            });
            GameFragment.img_stroke.startAnimation(animStroke);
            GameFragment.rl_win.setVisibility(View.VISIBLE);
            GameFragment.rl_win.setAnimation(animWin);
            GameFragment.rl_win.startAnimation(animWin);
            GameFragment.img_win.setImageBitmap(draw);
            GameFragment.txt_win.setText("draw");
        }
    }

    private void playWith2Player(ViewHolder holder, int position) {
        holder.img_item_table.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (arrBms.get(position)==null&&!checkWin()){
                    if(GameFragment.turnO){
                        mp = MediaPlayer.create(context, R.raw.mode);
                        mp.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                            @Override
                            public void onPrepared(MediaPlayer mediaPlayer) {
                                mp.start();
                            }
                        });

                        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                            @Override
                            public void onCompletion(MediaPlayer mediaPlayer) {
                                mp.release();
                            }
                        });
                        arrBms.set(position, bmO);
                        GameFragment.turnO = false;
                        GameFragment.txt_turn.setText("turn of X");
                    }else{
                        mp = MediaPlayer.create(context, R.raw.mode);
                        mp.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                            @Override
                            public void onPrepared(MediaPlayer mediaPlayer) {
                                mp.start();
                            }
                        });

                        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                            @Override
                            public void onCompletion(MediaPlayer mediaPlayer) {
                                mp.release();
                            }
                        });
                        arrBms.set(position, bmX);
                        GameFragment.turnO = true;
                        GameFragment.txt_turn.setText("turn of O");
                    }
                    holder.img_item_table.startAnimation(anim_x_o);
                    if (checkWin()){
                        win();
                    }
                    notifyItemChanged(position);
                }
            }
        });
    }

    private void playWithComputer(ViewHolder holder, int position) {
        holder.img_item_table.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (arrBms.get(position) == null && !checkWin() && GameFragment.turnO) {
                    if (GameFragment.turnO) {
                        mp = MediaPlayer.create(context, R.raw.mode);
                        mp.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                            @Override
                            public void onPrepared(MediaPlayer mediaPlayer) {
                                mp.start();
                            }
                        });

                        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                            @Override
                            public void onCompletion(MediaPlayer mediaPlayer) {
                                mp.release();
                            }
                        });
                        arrBms.set(position, bmO);
                        GameFragment.turnO = false;
                        GameFragment.txt_turn.setText("turn of X");
                    }
                    holder.img_item_table.startAnimation(anim_x_o);
                    if (checkWin()) {
                        win();
                    }
                    notifyItemChanged(position);
                    Handler handler = new Handler();
                    Runnable r = new Runnable() {
                        @Override
                        public void run() {
                            mp = MediaPlayer.create(context, R.raw.mode);
                            mp.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                                @Override
                                public void onPrepared(MediaPlayer mediaPlayer) {
                                    mp.start();
                                }
                            });

                            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                                @Override
                                public void onCompletion(MediaPlayer mediaPlayer) {
                                    mp.release();
                                }
                            });
                            // Computer makes a random move
                            int randomPosition;
                            do {
                                randomPosition = new Random().nextInt(arrBms.size());
                            } while (arrBms.get(randomPosition) != null); // Keep generating until finding an empty cell
                            arrBms.set(randomPosition, bmX);

                            if (checkWin()) {
                                win();
                            } else {
                                GameFragment.turnO = true;
                                GameFragment.txt_turn.setText("turn of O");
                            }
                            notifyItemChanged(randomPosition);
                        }
                    };
                    if (!checkWin()) {
                        handler.postDelayed(r, 1000);
                    }
                }
            }
        });
    }

    private ArrayList<Mark> solver(Bitmap bm) {
        ArrayList<Mark> arrPoints = new ArrayList<>();
        for (int i = 0; i < 9; i++){
            if(arrBmTest.get(i)==null){
                if (bm==bmX) {
                    arrBmTest.set(i, bmX);
                }else{
                    arrBmTest.set(i, bmO);
                }
                if (checkWinTmp(bm)==-100){
                    if (bm==bmX){
                        depth++;
                        ArrayList<Mark> arr = solver(bmO);
                        depth--;
                        int minimum = 50;
                        int id = 50;
                        for (int j = 0; j < arr.size(); j++){
                            if (minimum>arr.get(j).getPoint()){
                                minimum = arr.get(j).getPoint();
                                id = i;
                            }
                        }
                        if (minimum!=50&&id!=50){
                            arrPoints.add(new Mark(i, minimum));
                        }
                    }else{
                        depth++;
                        ArrayList<Mark> arr = solver(bmX);
                        depth--;
                        int maximum = -50;
                        int id = -50;
                        for (int j = 0; j < arr.size(); j++){
                            if (maximum < arr.get(j).getPoint()){
                                maximum = arr.get(j).getPoint();
                                id = i;
                            }
                        }
                        if (maximum!=-50&&id!=-50) {
                            arrPoints.add(new Mark(i, maximum));
                        }

                    }
                }else{
                    if (bm == bmX){
                        arrPoints.add(new Mark(i, checkWinTmp(bm) - depth));
                    }else{
                        arrPoints.add(new Mark(i, -(checkWinTmp(bm) - depth)));
                    }
                }
                arrBmTest.set(i,null);
            }
        }
        return arrPoints;
    }

    private int checkWinTmp(Bitmap bm) {
        int countRow = 0;
        for (int i = 0; i < 9; i++){
            if(i%3==0){
                countRow = 0;
            }
            if(arrBmTest.get(i)==bm){
                countRow++;
            }
            if (countRow==3){
                return 10;
            }
        }
        if (arrBmTest.get(0)==arrBmTest.get(3)&&arrBmTest.get(3)==arrBmTest.get(6)&&arrBmTest.get(0)==bm
                ||arrBmTest.get(1)==arrBmTest.get(4)&&arrBmTest.get(4)==arrBmTest.get(7)&&arrBmTest.get(1)==bm
                ||arrBmTest.get(2)==arrBmTest.get(5)&&arrBmTest.get(5)==arrBmTest.get(8)&&arrBmTest.get(2)==bm){
            return 10;
        }
        if (arrBmTest.get(0)==arrBmTest.get(4)&&arrBmTest.get(4)==arrBmTest.get(8)&&arrBmTest.get(0)==bm) return 10;
        if (arrBmTest.get(2)==arrBmTest.get(4)&&arrBmTest.get(4)==arrBmTest.get(6)&&arrBmTest.get(2)==bm) return 10;
        int count = 0;
        for (int i = 0; i < 9; i++){
            if (arrBmTest.get(i)!=null){
                count++;
            }
        }
        if (count==9){
            return 0;
        }
        return -100;
    }

    private void win() {

        GameFragment.img_stroke.startAnimation(animWin);
        GameFragment.rl_win.setVisibility(View.VISIBLE);
        GameFragment.rl_win.setAnimation(animWin);
        GameFragment.rl_win.startAnimation(animWin);
        if (winCharacter.equals("o")){
            mp = MediaPlayer.create(context, R.raw.start);
            mp.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mediaPlayer) {
                    mp.start();
                }
            });

            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    mp.release();
                }
            });
            GameFragment.img_win.setImageBitmap(bmO);
            MainActivity.scoreO++;
            GameFragment.win_o.setText("O: " + MainActivity.scoreO);
        }else{
            mp = MediaPlayer.create(context, R.raw.start);
            mp.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mediaPlayer) {
                    mp.start();
                }
            });

            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    mp.release();
                }
            });
            GameFragment.img_win.setImageBitmap(bmX);
            MainActivity.scoreX++;
            GameFragment.win_x.setText("X: " + MainActivity.scoreX);
        }
        GameFragment.txt_win.setText("win");
    }

    private boolean checkWin() {
        if(arrBms.get(0)==arrBms.get(3)&&arrBms.get(3)==arrBms.get(6)&&arrBms.get(0)!=null){
            GameFragment.img_stroke.setImageBitmap(arrStroke.get(2));
            checkWinCharacter(0);
            return true;
        }else if(arrBms.get(1)==arrBms.get(4)&&arrBms.get(4)==arrBms.get(7)&&arrBms.get(1)!=null){
            GameFragment.img_stroke.setImageBitmap(arrStroke.get(3));
            checkWinCharacter(1);
            return true;
        }else if(arrBms.get(2)==arrBms.get(5)&&arrBms.get(5)==arrBms.get(8)&&arrBms.get(2)!=null){
            GameFragment.img_stroke.setImageBitmap(arrStroke.get(4));
            checkWinCharacter(2);
            return true;
        }else if(arrBms.get(0)==arrBms.get(1)&&arrBms.get(1)==arrBms.get(2)&&arrBms.get(0)!=null){
            GameFragment.img_stroke.setImageBitmap(arrStroke.get(5));
            checkWinCharacter(0);
            return true;
        }else if(arrBms.get(3)==arrBms.get(4)&&arrBms.get(4)==arrBms.get(5)&&arrBms.get(3)!=null){
            GameFragment.img_stroke.setImageBitmap(arrStroke.get(6));
            checkWinCharacter(3);
            return true;
        }else if(arrBms.get(6)==arrBms.get(7)&&arrBms.get(7)==arrBms.get(8)&&arrBms.get(6)!=null){
            GameFragment.img_stroke.setImageBitmap(arrStroke.get(7));
            checkWinCharacter(6);
            return true;
        }else if(arrBms.get(0)==arrBms.get(4)&&arrBms.get(4)==arrBms.get(8)&&arrBms.get(0)!=null){
            GameFragment.img_stroke.setImageBitmap(arrStroke.get(1));
            checkWinCharacter(0);
            return true;
        }else if(arrBms.get(2)==arrBms.get(4)&&arrBms.get(4)==arrBms.get(6)&&arrBms.get(2)!=null){
            GameFragment.img_stroke.setImageBitmap(arrStroke.get(0));
            checkWinCharacter(2);
            return true;
        }
        return false;
    }

    private void checkWinCharacter(int i) {
        if (arrBms.get(i)==bmO){
            winCharacter = "o";
        }else{
            winCharacter = "x";
        }
    }

    @Override
    public int getItemCount() {
        return arrBms.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        private ImageView img_item_table;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img_item_table = itemView.findViewById(R.id.img_item_table);
        }
    }

    public ArrayList<Bitmap> getArrBms() {
        return arrBms;
    }

    public void setArrBms(ArrayList<Bitmap> arrBms) {
        this.arrBms = arrBms;
    }
}