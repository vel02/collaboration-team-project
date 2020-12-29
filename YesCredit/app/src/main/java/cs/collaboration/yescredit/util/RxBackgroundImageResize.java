package cs.collaboration.yescredit.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.util.Log;

import java.io.ByteArrayOutputStream;

import javax.inject.Inject;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableOnSubscribe;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

//source: https://stackoverflow.com/questions/3879992/how-to-get-bitmap-from-an-uri
public class RxBackgroundImageResize {

    public static final double MB = 1000000.0;
    public static final double MB_THRESHOLD = 5.0;
    private static final String TAG = "BackgroundImageResize";
    private final Context context;
    private final CompositeDisposable disposable;
    private Bitmap bitmap;
    private Uri uri;
    private byte[] bytes;

    private OnExecuteUploadListener listener;

    @Inject
    public RxBackgroundImageResize(final Context context) {
        this.context = context;
        this.disposable = new CompositeDisposable();
    }

    public void setOnExecuteUploadListener(final OnExecuteUploadListener listener) {
        this.listener = listener;
    }

    public void execute(Bitmap bitmap) {
        this.bitmap = bitmap;
        this.observeData();
    }

    public void execute(Uri uri) {
        this.uri = uri;
        this.observeData();
    }

    private void observeData() {

        Observable.create((ObservableOnSubscribe<byte[]>) emitter -> {

            if (bitmap == null) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    bitmap = ImageDecoder.decodeBitmap(ImageDecoder.createSource(context.getContentResolver(), uri));
                } else {
                    bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), uri);
                }
                Log.d(TAG, "doInBackground: bitmap original size: megabytes: " + bitmap.getByteCount() / MB + "MB");
            }

            byte[] bytes;
            if (!emitter.isDisposed()) {
                for (int i = 1; i < 11; i++) {
                    if (i == 10) {
                        Log.d(TAG, "BackgroundImageResize: That image is too large.");
                        break;
                    }
                    bytes = getBytesFromBitmap(bitmap, 100 / i);
                    Log.d(TAG, "doInBackground: megabyte: (" + (11 - i) + "0%) " + bytes.length / MB + " MB");
                    if (bytes.length / MB < MB_THRESHOLD) {
                        emitter.onNext(bytes);
                        emitter.onComplete();
                        break;
                    }
                }
            }

        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<byte[]>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        disposable.add(d);
                    }

                    @Override
                    public void onNext(byte @NonNull [] bytes) {
                        RxBackgroundImageResize.this.bytes = bytes;
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.d(TAG, "onError: Error compressing photo: " + e.getMessage());
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {
                        listener.onExecuteUpload(bytes);
                        uri = null;
                        bitmap = null;
                        disposable.clear();
                    }
                });
    }

    private byte[] getBytesFromBitmap(Bitmap bitmap, int quality) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, stream);
        return stream.toByteArray();
    }

    public interface OnExecuteUploadListener {
        void onExecuteUpload(byte[] bytes);
    }
}
