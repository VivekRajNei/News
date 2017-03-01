/*
* Copyright (c) <2017> <Vivek Rajendran>
*
* Permission is hereby granted, free of charge, to any person obtaining a copy
* of this software and associated documentation files (the "Software"), to deal
* in the Software without restriction, including without limitation the rights
* to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
* copies of the Software, and to permit persons to whom the Software is
* furnished to do so, subject to the following conditions:
*
* The above copyright notice and this permission notice shall be included in all
* copies or substantial portions of the Software.
*
* THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
* IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
* FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
*AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
* LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
* OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
* SOFTWARE.
*/

package es.esy.vivekrajendran.news.network;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import es.esy.vivekrajendran.news.data.UserPref;


public class NewsAsync extends AsyncTask<String, Void, String> {

    public static final String TAG = "TAG";
    public static final String NEWS = "news";
    public static final String PROVIDERS = "providers";
    public static final String IMAGES = "images";
    public static final String VIDEO = "video";
    private Context context;

    public NewsAsync(Context context) {
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... params) {
        String jsonString = doNetworkOperation(params[0]);
        doParse(jsonString ,params);
        return params[1];
    }

    private void doParse(String json, String[] param) {
        String path = param[1];
        switch (path) {
            case NEWS:
                new NewsParser.LatestNewsParser(context)
                        .resolveJSON(json);
                break;
            case PROVIDERS:
                new NewsParser.ProviderParser(context)
                        .resolveJSON(json);
                break;
            case IMAGES:
                new MultimediaParser.ImageParser(context)
                        .resolveImage(json);
                break;
            case VIDEO:
                new MultimediaParser.VideoParser(context)
                        .resolveVideo(json);
                break;
            default:
                throw new IllegalArgumentException("No valid param[1] found");
        }
    }

    @Override
    protected void onPostExecute(String string) {
        switch (string) {
            case NEWS:
                UserPref.getInstance(context).setNewsTime();
                break;
            case PROVIDERS:
                UserPref.getInstance(context).setProviderTime();
                break;
            case IMAGES:
                UserPref.getInstance(context).setImageTime();
                break;
            case VIDEO:
                UserPref.getInstance(context).setVideoTime();
                break;
            default:
                throw new IllegalArgumentException("OnPostExecute: String mismatch found");
        }
    }

    private String doNetworkOperation(String params) {
        HttpURLConnection mURLConnection;
        StringBuilder mStringBuilder;

        try {
            URL url = new URL(params);
            mURLConnection = (HttpURLConnection) url.openConnection();
            mURLConnection.setRequestMethod("GET");
            mURLConnection.setConnectTimeout(10000);
            mURLConnection.setReadTimeout(30000);

            BufferedReader mBufferedReader = new BufferedReader(
                    new InputStreamReader(mURLConnection.getInputStream()));

            String temp;
            mStringBuilder = new StringBuilder();
            while ((temp = mBufferedReader.readLine()) != null) {
                mStringBuilder.append(temp);
            }
            Log.i(TAG, "doNetworkOperation: " + mStringBuilder.toString());
            return mStringBuilder.toString();
        } catch (Exception e) {
            e.printStackTrace();
            Log.i(TAG, "doNetworkOperation: " + e.getMessage());
            return "Err" + e.toString();
        }
    }
}
