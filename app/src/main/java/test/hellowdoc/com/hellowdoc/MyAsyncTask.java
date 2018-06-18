package test.hellowdoc.com.hellowdoc;

import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;

import com.squareup.okhttp.Call;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.lang.ref.WeakReference;

public class MyAsyncTask extends AsyncTask<Void, Void, ResponseDetails> {
    private static final String TAG = MyAsyncTask.class.getSimpleName();

    public interface MyAsyncTaskListener {
        void onTaskCompleted(ResponseDetails dataModel);
    }

    private MyAsyncTaskListener mMyAsyncTaskListener;
    private WeakReference<Context> mContext;
    private String url;

    private boolean isCanceled;
    private Call call;

    public MyAsyncTask(@NonNull Context context, String url, @NonNull MyAsyncTaskListener listener) {
        this.mContext = new WeakReference<>(context);
        this.url = url;
        this.mMyAsyncTaskListener = listener;
    }

    @Override
    protected ResponseDetails doInBackground(Void... voids) {

        ResponseDetails responseDetails = null;

        final OkHttpClient client = new OkHttpClient();
        Log.d(TAG + " : url", url + "");
        final Request httpRequest = new Request.Builder().url(url).build();

        call = client.newCall(httpRequest);
        try {
            final Response httpResponse = call.execute();

            final int status = httpResponse.code();
            final String responseJson = httpResponse.body().string();
            Log.d(TAG + " : status", status + "");
            Log.d(TAG + " : responseJson", responseJson);

            responseDetails = GsonUtil.fromJson(responseJson, ResponseDetails.class, url);
        } catch (IOException e) {
            Log.d(TAG + " : ERR", e.getMessage());
        }

        return responseDetails;
    }

    public void cancelActive() {
        if (call != null) {
            call.cancel();
        }
        isCanceled = true;
        cancel(true);
    }


    @Override
    protected void onPostExecute(ResponseDetails dataModel) {
        if (!isCanceled && mMyAsyncTaskListener != null) {
            mMyAsyncTaskListener.onTaskCompleted(dataModel);
        }
        super.onPostExecute(dataModel);
    }
}
