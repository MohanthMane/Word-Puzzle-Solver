package com.example.wordpuzzlesolver;

import android.content.Context;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashSet;

// Utility class for solving word puzzle

public class WordUtil {

    ArrayList<String> permutations = new ArrayList<String>();  // Permutations possible for the input
    ArrayList<String> possibleStrings = new ArrayList<String>(); // Strings with meaning in permutations
    JSONObject jsonObject;  // Object that parses dictionary
    private static final String TAG = "{DEBUG INFO}";

    /*
    * Constructor for the WordUtil class
    * ApplicationContext is passed as parameter to load json file from the assets
    * Initialises jsonObject by calling loadJSONFromAsset method

     */
    public WordUtil(Context context) {
        Log.d(TAG, "WordUtil: In WordUtil constructor");
        try {
            jsonObject = new JSONObject(loadJSONFromAsset(context));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /*
    * Method that loads the json file into the memory
    * returns JSON object in the form of the String
     */
    public String loadJSONFromAsset(Context context) {
        String json = null;
        try {
            InputStream inputStream = context.getAssets().open("wordsdictionary.json");
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();
            json = new String(buffer,"UTF-8");
        } catch (Exception e) {
            Log.d("LOADING ERROR", "loadJSONFromAsset: Error while loading json");
        }
        assert json != null;
        return json;
    }


    /*
    * Method that generates permutations for a given string
    * It takes in String as only parameter
    * It avoids duplicate permutations
    * Using HashSet would be an efficient approach for this operation
     */

    // TODO: Update this algorithm to utilise previous results to generate new ones
    static HashSet<String> distinctPermute(String str)
    {
        if (str.length() == 0) {
            HashSet<String> baseRes = new HashSet<>();
            baseRes.add("");
            return baseRes;
        }

        char ch = str.charAt(0);

        String restStr = str.substring(1);

        // Recurvise call
        HashSet<String> prevRes = distinctPermute(restStr);

        HashSet<String> Res = new HashSet<>();
        for (String s : prevRes) {
            for (int i = 0; i <= s.length(); i++) {
                String f = s.substring(0, i) + ch + s.substring(i);
                Res.add(f);
            }
        }
        return Res;
    }

    /*
    * Method that generates subsequences for a string
    * It takes in two parameters
    * String in form of Character array and length of the String
    * Returns the list of subsequences for a String
     */
    static ArrayList<String> generateSubsequences(char arr[], int n)
    {
        int opsize = (int)Math.pow(2, n);
        ArrayList<String> res = new ArrayList<String>();
        for (int counter = 1; counter < opsize; counter++)
        {
            String temp = "";
            for (int j = 0; j < n; j++)
            {
                if (BigInteger.valueOf(counter).testBit(j))
                    temp += arr[j];
            }
            res.add(temp);
        }
        return res;
    }

    /*
    * getPermutation is just a wrapper method for both generateSubsequences(...) method
    * and distinctPermute(...) method
    * Final set of strings were stored in permutations array.
     */
    void getPermutation(String input) {
        ArrayList<String> subsequences = generateSubsequences(input.toCharArray(),input.length());
        HashSet<String> result = new HashSet<String>();
        for (String k :
                subsequences) {
            if(k.length() > 2)
                result.addAll(distinctPermute(k));
        }
        permutations.addAll(result);
    }

    /*
    * Method that filters out meaningful words from permutations array
    * returns list of meaningful words(possibleStrings)
     */
    ArrayList<String> generatePossibleStrings() {
        Log.d(TAG, "generatePossibleStrings: In generate");
        for (String word : permutations) {
            if(jsonObject.has(word)) {
                possibleStrings.add(word);
            }
        }
        return possibleStrings;
    }

}
