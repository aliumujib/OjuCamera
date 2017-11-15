package com.alium.ojucamera.internal.repository;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import com.alium.ojucamera.internal.ui.model.PickerTile;

import org.joda.time.LocalDateTime;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.BiFunction;

/**
 * Created by abdulmujibaliu on 9/29/17.
 */

public class MediaRepository implements IMediaRepository {

    public String TAG = getClass().getSimpleName();

    public static IMediaRepository sharedInstance = new MediaRepository();

    @Override
    public Observable<List<PickerTile>> getPhotos(final Context context) {
        final List<PickerTile> pickerTiles = new ArrayList<>();
        return new Observable<List<PickerTile>>() {
            @Override
            protected void subscribeActual(Observer<? super List<PickerTile>> observer) {
                Cursor imageCursor = null;
                try {
                    final String[] columns = {MediaStore.Images.Media.DATA, MediaStore.Images.ImageColumns.ORIENTATION, MediaStore.Images.Media.DATE_MODIFIED};
                    final String orderBy = MediaStore.Images.Media.DATE_MODIFIED + " DESC";

                    imageCursor = context.getApplicationContext().getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns, null, null, orderBy);
                    if (imageCursor != null) {
                        int count = 0;
                        Log.d(TAG, String.valueOf(count));
                        while (imageCursor.moveToNext()) {
                            String imageLocation = imageCursor.getString(imageCursor.getColumnIndex(MediaStore.Images.Media.DATA));
                            File imageFile = new File(imageLocation);
                            PickerTile pickerTile = new PickerTile(Uri.fromFile(imageFile), new LocalDateTime(imageCursor.getLong(imageCursor.getColumnIndex(MediaStore.Images.Media.DATE_MODIFIED)) * 1000L), false);
                            pickerTiles.add(pickerTile);
                            count++;
                        }
                        observer.onNext(pickerTiles);
                    }
                } catch (Exception e) {
                    observer.onError(e);
                    e.printStackTrace();
                } finally {
                    if (imageCursor != null && !imageCursor.isClosed()) {
                        imageCursor.close();
                    }
                }
            }
        };
    }

    @Override
    public Observable<List<PickerTile>> getVideos(final Context context) {
        final List<PickerTile> pickerTiles = new ArrayList<>();
        return new Observable<List<PickerTile>>() {
            @Override
            protected void subscribeActual(Observer<? super List<PickerTile>> observer) {
                Cursor videoCursor = null;
                try {
                    final String[] columns = {MediaStore.Video.VideoColumns.DATA, MediaStore.Video.VideoColumns.DATE_MODIFIED};
                    final String orderBy = MediaStore.Video.Media.DATE_MODIFIED + " DESC";

                    videoCursor = context.getContentResolver().query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, columns, null, null, orderBy);
                    if (videoCursor != null) {
                        int count = 0;
                        while (videoCursor.moveToNext()) {
                            String videoLocation;
                            videoLocation = videoCursor.getString(videoCursor.getColumnIndex(MediaStore.Video.Media.DATA));
                            File videoFile = new File(videoLocation);
                            pickerTiles.add(new PickerTile(Uri.fromFile(videoFile), new LocalDateTime(videoCursor.getColumnIndex(MediaStore.Video.Media.DATE_MODIFIED) * 1000L), true));
                            count++;
                        }
                        observer.onNext(pickerTiles);
                    }
                } catch (Exception e) {
                    observer.onError(e);
                    e.printStackTrace();
                } finally {
                    if (videoCursor != null && !videoCursor.isClosed()) {
                        videoCursor.close();
                    }
                }
            }
        };
    }


    @Override
    public Observable<List<PickerTile>> getAllMedia(Context context) {
        final List<PickerTile> pickerTilesTotal = new ArrayList<>();
        return Observable.zip(getPhotos(context), getVideos(context), new BiFunction<List<PickerTile>, List<PickerTile>, List<PickerTile>>() {
            @Override
            public List<PickerTile> apply(@NonNull List<PickerTile> pickerTiles, @NonNull List<PickerTile> pickerTiles2) throws Exception {
                pickerTilesTotal.addAll(pickerTiles2);
                pickerTilesTotal.addAll(pickerTiles);
                return pickerTilesTotal;
            }
        });
    }

}
