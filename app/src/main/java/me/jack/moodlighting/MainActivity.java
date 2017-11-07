package me.jack.moodlighting;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.annotation.ColorInt;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ListPopupWindow;
import android.text.Editable;
import android.text.TextWatcher;
import android.transition.Fade;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.Toast;

import com.thebluealliance.spectrum.SpectrumDialog;

import java.io.DataInput;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {
    ColorGridListAdapter colorGridListAdapter = new ColorGridListAdapter(this);

    @Override
    public void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.activity_main);
        findViewById(R.id.start).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(colorGridListAdapter.colors.size() == 0){
                    final String url = "http://" + getCurrentIP()  + ":5000/fade?pauseTime=" +
                            ((EditText) findViewById(R.id.pauseText)).getText() +
                            "&fadeTime=" + ((EditText) findViewById(R.id.fadeText)).getText() +
                            "&random=" + getRandomValue() +
                            "&colors=" + generateColorList(new ArrayList<Integer>(Arrays.asList(Color.BLACK,Color.BLACK,Color.BLACK)));
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                new URL(url).openStream();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                    return;
                }
                if (colorGridListAdapter.colors.size() < 3) {
                    Toast.makeText(MainActivity.this, "Min 3 colours", Toast.LENGTH_SHORT).show();
                    return;
                }
                final String url = "http://" + getCurrentIP()  + ":5000/fade?pauseTime=" +
                        ((EditText) findViewById(R.id.pauseText)).getText() +
                        "&fadeTime=" + ((EditText) findViewById(R.id.fadeText)).getText() +
                        "&random=" + getRandomValue() +
                        "&colors=" + generateColorList(colorGridListAdapter.colors);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            new URL(url).openStream();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        });
        findViewById(R.id.stop).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String url = "http://" + getCurrentIP()  + ":5000/stop";
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            new URL(url).openStream();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        });

        ((SeekBar) findViewById(R.id.pauseSlider)).setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                ((EditText) findViewById(R.id.pauseText)).setText(i + "");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        ((SeekBar) findViewById(R.id.fadeSlider)).setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                ((EditText) findViewById(R.id.fadeText)).setText(i + "");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        ((EditText) findViewById(R.id.fadeText)).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                try {
                    int num = Integer.parseInt(charSequence.toString());
                    ((SeekBar) findViewById(R.id.fadeSlider)).setProgress(num);
                } catch (Exception e) {
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        ((EditText) findViewById(R.id.pauseText)).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                try {
                    int num = Integer.parseInt(charSequence.toString());
                    ((SeekBar) findViewById(R.id.pauseSlider)).setProgress(num);
                } catch (Exception e) {
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        findViewById(R.id.addColorButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SpectrumDialog.Builder d = new SpectrumDialog.Builder(MainActivity.this);
                SpectrumDialog dialog = d.setColors(new int[]{Color.RED, Color.GREEN, Color.BLUE, Color.CYAN, Color.YELLOW, Color.DKGRAY, Color.GRAY, Color.MAGENTA, Color.WHITE}).setDismissOnColorSelected(true).build();
                dialog.show(getSupportFragmentManager(), "");
                dialog.setOnColorSelectedListener(new SpectrumDialog.OnColorSelectedListener() {
                    @Override
                    public void onColorSelected(boolean positiveResult, @ColorInt int color) {
                        if (positiveResult) {
                            colorGridListAdapter.colors.add(color);
                            ((GridView) findViewById(R.id.gridView)).invalidateViews();
                        }
                    }
                });
            }
        });

        ((GridView) findViewById(R.id.gridView)).setAdapter(colorGridListAdapter);

        ((GridView) findViewById(R.id.gridView)).setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                colorGridListAdapter.colors.remove(position);
                ((GridView) findViewById(R.id.gridView)).invalidateViews();
            }
        });


        ((ListView) findViewById(R.id.left_drawer)).setAdapter(new SideDrawListAdapter(this));
        ((ListView) findViewById(R.id.left_drawer)).setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 4) {
                    updateIP();
                    return;
                }
                FadePreset p = FadePreset.presets.get(position - 1);
                final String url = "http://" + getCurrentIP()  + ":5000/fade?pauseTime=" +
                        p.getPauseTime() +
                        "&fadeTime=" + p.getFadeTime() +
                        "&random=" + p.getRand() +
                        "&colors=" + generateColorList(p.getColors());
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            new URL(url).openStream();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        });
    }

    public void updateIP() {
        final EditText ipText = new EditText(this);
        ipText.setHint(getCurrentIP());

        new AlertDialog.Builder(this)
                .setTitle("Update UP")
                .setMessage("Please enter the new IP of the MoodLighting server")
                .setView(ipText)
                .setPositiveButton("Update", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        String ip = ipText.getText().toString();
                        setCurrentIP(ip);
                        Toast.makeText(MainActivity.this, getCurrentIP(), Toast.LENGTH_LONG).show();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                    }
                })
                .show();
    }

    public String getCurrentIP() {
        SharedPreferences settings = getPreferences(0);
        return settings.getString("IP", "192.168.0.159");
    }

    public void setCurrentIP(String newValue) {
        SharedPreferences settings = getPreferences(0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("IP", newValue);
        editor.commit();
    }

    public String getRandomValue() {
        boolean value = ((CheckBox) findViewById(R.id.random)).isChecked();
        if (value)
            return "True";
        else
            return "False";
    }

    public String generateColorList(ArrayList<Integer> col) {
        String list = "";
        for (int i = 0; i != col.size(); i++) {
            list += Color.red(col.get(i)) + "," + Color.green(col.get(i)) + "," + Color.blue(col.get(i));
            if (i != col.size() - 1)
                list += ":";
        }
        return list;
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

}