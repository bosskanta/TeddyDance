package com.example.teddydance;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

public class HighScore {

    private final String filename = "score.txt";

    public void saveScore(Context context, String playerName, int score)  {
        // Write file
        FileOutputStream outputStream = null;
        try {
            outputStream = context.openFileOutput(filename, Context.MODE_APPEND);
        String strToWrite = playerName + "," + score + "\n";
        outputStream.write(strToWrite.getBytes());
        outputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private ArrayList<HashMap<String, String>> sort(ArrayList<HashMap<String, String>> arrayList) {
        Collections.sort(arrayList, new Comparator<HashMap< String,String >>() {
            @Override
            public int compare(HashMap<String, String> map1, HashMap<String, String> map2) {
                int val1 = Integer.valueOf(map1.get("score"));
                int val2 = Integer.valueOf(map2.get("score"));
                if(val1 > val2) {
                    return -1;
                } else if(val1 < val2) {
                    return 1;
                }
                return 0;
            }
        });
        return arrayList;
    }

    public ArrayList<HashMap<String, String>> read_sortedScore(Context context, ArrayList<HashMap<String, String>> arrayList) {
        try{
            BufferedReader inputReader = new BufferedReader(new InputStreamReader(context.openFileInput(filename)));
            String line = "";
            try {
                //Read each line
                while(inputReader.ready()) {
                    line = inputReader.readLine();
                    if(line.isEmpty())
                        break;
                    String[] lineData = line.split(",");
                    String player = lineData[0];
                    String score = lineData[1];

                    HashMap<String, String> map = new HashMap<>();
                    map.put("player", player);
                    map.put("score", score);
                    arrayList.add(map);
                }
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }

        } catch (FileNotFoundException fe) {
            Log.e("FileNotFoundException", fe.getMessage());
        }

        arrayList = sort(arrayList);

        return arrayList;
    }

}
