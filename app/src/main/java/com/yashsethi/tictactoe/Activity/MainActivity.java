package com.yashsethi.tictactoe.Activity;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.yashsethi.tictactoe.R;

public class MainActivity extends AppCompatActivity {

    TextView pvp,pvb,rate;
    Intent intent = null;

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        findId();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
        );
        setContentView(R.layout.activity_main);
        findId();
        intent = new Intent(MainActivity.this, GamePlay.class);
        final Animation myAnim = AnimationUtils.loadAnimation(MainActivity.this , R.anim.level_dialog_anim);
        pvb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pvb.startAnimation(myAnim);
                intent.putExtra("gameMode", "PvB");
                showDialogBox();
            }
        });

        pvp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pvp.startAnimation(myAnim);
                intent.putExtra("gameMode", "PvP");
                showDialogBox();
            }
        });

        rate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("market://details?id=" + MainActivity.this.getPackageName());
                Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                        Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                        Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                try {
                    startActivity(goToMarket);
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("http://play.google.com/store/apps/details?id=" + MainActivity.this.getPackageName())));
                }
            }
        });
    }

    private void showDialogBox() {

        LayoutInflater factory = LayoutInflater.from(this);
        final View detailsDialogView = factory.inflate(R.layout.name_level_layout, null);
        final AlertDialog playerDialog = new AlertDialog.Builder(this,R.style.LevelDialog).create();
        playerDialog.setView(detailsDialogView);
        playerDialog.setCancelable(false);
        playerDialog.show();
        final RadioGroup radioGroup = playerDialog.findViewById(R.id.radioGroup);

        final TextView player1 = detailsDialogView.findViewById(R.id.player1_et);
        final TextView player2 = detailsDialogView.findViewById(R.id.player2_et);
        TextView chooseLevelTxt = detailsDialogView.findViewById(R.id.level_txt);
        View divider = detailsDialogView.findViewById(R.id.divider);

        if(intent.getStringExtra("gameMode").equals("PvP")) {
            radioGroup.setVisibility(View.GONE);
            player2.setText("");
            player2.setFocusable(true);
            chooseLevelTxt.setVisibility(View.GONE);
            divider.setVisibility(View.GONE);
            detailsDialogView.findViewById(R.id.continue_tv).setPadding(0,90,0,0);
        } else {
            radioGroup.setVisibility(View.VISIBLE);
            player2.setText("Computer");
            player2.setFocusable(false);
            chooseLevelTxt.setVisibility(View.VISIBLE);
            divider.setVisibility(View.VISIBLE);
        }

        detailsDialogView.findViewById(R.id.continue_tv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(player1.getText().length() > 0 && player2.getText().length() > 0 && (radioGroup.getCheckedRadioButtonId() != -1 || intent.getStringExtra("gameMode").equals("PvP"))) {
                    intent.putExtra("player1", player1.getText().toString());
                    intent.putExtra("player2", player2.getText().toString());
                    if(intent.getStringExtra("gameMode").equals("PvB")) {
                        View radioButton = radioGroup.findViewById(radioGroup.getCheckedRadioButtonId());
                        int index = radioGroup.indexOfChild(radioButton);
                        if(index == 0) {
                            intent.putExtra("level", "easy");
                        } else if(index == 1) {
                            intent.putExtra("level","medium");
                        } else {
                            intent.putExtra("level","hard");
                        }
                    }
                    startActivity(intent);
                    playerDialog.dismiss();
                } else {
                    Toast.makeText(MainActivity.this, "Fill all fields", Toast.LENGTH_SHORT).show();
                }
            }
        });

        detailsDialogView.findViewById(R.id.no).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playerDialog.dismiss();
            }
        });
    }

    private void findId() {
        pvp = findViewById(R.id.pvp);
        pvb = findViewById(R.id.pvb);
        rate = findViewById(R.id.rate);
    }

    @Override
    public void onBackPressed() {

        LayoutInflater factory = LayoutInflater.from(this);
        final View deleteDialogView = factory.inflate(R.layout.dialog_layout, null);
        final AlertDialog exitDialog = new AlertDialog.Builder(this).create();
        exitDialog.setView(deleteDialogView);
        exitDialog.setCancelable(false);
        deleteDialogView.findViewById(R.id.yes).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.this.finishAffinity();
            }
        });
        deleteDialogView.findViewById(R.id.no).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exitDialog.dismiss();
            }
        });
        exitDialog.show();
    }
}
