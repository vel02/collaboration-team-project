package cs.collaboration.yescredit.util;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import cs.collaboration.yescredit.ui.apply.fragment.StepTwoFragment;

public class BackgroundImageResize extends AsyncTask<Uri, Integer, byte[]> {

    private static final String TAG = "BackgroundImageResize";
    private static final double MB_THRESHOLD = 5.0;
    private static final double MB = 1000000.0;

    private final StepTwoFragment stepTwoFragment;
    Bitmap bitmap;

    public BackgroundImageResize(StepTwoFragment stepTwoFragment, Bitmap bitmap) {
        this.stepTwoFragment = stepTwoFragment;
        if (bitmap != null) {
            this.bitmap = bitmap;
        }
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        Toast.makeText(stepTwoFragment.getActivity(), "compressing image", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected byte[] doInBackground(Uri... uris) {
        Log.d(TAG, "doInBackground: started.");

        if (bitmap == null) {
            try {
                bitmap = MediaStore.Images.Media.getBitmap(stepTwoFragment.requireActivity().getContentResolver(), uris[0]);
                Log.d(TAG, "doInBackground: bitmap size: megabytes: " + bitmap.getByteCount() / MB + " MB");
            } catch (IOException e) {
                Log.e(TAG, "doInBackground: IOException: ", e.getCause());
            }
        }

        byte[] bytes = null;
        for (int i = 1; i < 11; i++) {
            if (i == 10) {
                Toast.makeText(stepTwoFragment.getActivity(), "That image is too large.", Toast.LENGTH_SHORT).show();
                break;
            }
            bytes = getBytesFromBitmap(bitmap, 100 / i);
            Log.d(TAG, "doInBackground: megabytes: (" + (11 - i) + "0%) " + bytes.length / MB + " MB");
            if (bytes.length / MB < MB_THRESHOLD) {
                return bytes;
            }
        }
        return bytes;
    }

    @Override
    protected void onPostExecute(byte[] bytes) {
        super.onPostExecute(bytes);
        stepTwoFragment.setBytes(bytes);
        stepTwoFragment.executeUploadTask();
    }

    // convert from bitmap to byte array
    private static byte[] getBytesFromBitmap(Bitmap bitmap, int quality) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, stream);
        return stream.toByteArray();
    }
}
