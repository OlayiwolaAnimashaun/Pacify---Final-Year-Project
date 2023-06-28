package com.example.pacify;

import android.graphics.Bitmap;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.view.TextureView;

import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.concurrent.CopyOnWriteArrayList;

class Analyzer {
    private final CameraActivity activity;

    private final ChartDrawer chartDrawer;

    private MeasureStore store;
    private MeasureStore storeBlue;

    private final int measurementInterval = 45;
    private final int measurementLength = 15000; // ensure the number of data points is the power of two
    private final int clipLength = 3500;

    private int detectedValleys = 0;
    private int ticksPassed = 0;

    private final CopyOnWriteArrayList<Long> valleys = new CopyOnWriteArrayList<>();

    private CountDownTimer timer;

    private final Handler mainHandler;

    Analyzer(CameraActivity activity, TextureView graphTextureView, Handler mainHandler) {
        this.activity = activity;
        this.chartDrawer = new ChartDrawer(graphTextureView);
        this.mainHandler = mainHandler;
    }

    private boolean detectValley() {
        final int valleyDetectionWindowSize = 13;
        CopyOnWriteArrayList<Measurement<Integer>> subList = store.getLastStdValues(valleyDetectionWindowSize);
        if (subList.size() < valleyDetectionWindowSize) {
            return false;
        } else {
            Integer referenceValue = subList.get((int) Math.ceil(valleyDetectionWindowSize / 2f)).measurement;

            for (Measurement<Integer> measurement : subList) {
                if (measurement.measurement < referenceValue) return false;
            }

            // filter out consecutive measurements due to too high measurement rate
            return (!subList.get((int) Math.ceil(valleyDetectionWindowSize / 2f)).measurement.equals(
                    subList.get((int) Math.ceil(valleyDetectionWindowSize / 2f) - 1).measurement));
        }
    }

    private boolean detectValleyBlue()
    {
        final int valleyDetectionWindowSize = 13;

        CopyOnWriteArrayList<Measurement<Integer>> subList = storeBlue.getLastStdValues(valleyDetectionWindowSize);

        if (subList.size() < valleyDetectionWindowSize)
        {
            return false;
        }
        else
            {
                Integer referenceValue = subList.get((int) Math.ceil(valleyDetectionWindowSize / 2f)).measurement;

                for (Measurement<Integer> measurement : subList)
                {
                    if(measurement.measurement < referenceValue)
                        return false;
                }

                return (!subList.get((int) Math.ceil(valleyDetectionWindowSize / 2f)).measurement.equals(
                        subList.get((int) Math.ceil(valleyDetectionWindowSize / 2f) - 1).measurement));
            }
    }

    void measurePulse(TextureView textureView, CameraService cameraService) {

        // 20 times a second, get the amount of red on the picture.
        // detect local minimums, calculate pulse.

        store = new MeasureStore();
        storeBlue = new MeasureStore();

        detectedValleys = 0;

        timer = new CountDownTimer(measurementLength, measurementInterval) {

            @Override
            public void onTick(long millisUntilFinished) {
                if (clipLength > (++ticksPassed * measurementInterval)) return;

                Thread thread = new Thread(() -> {
                    Bitmap currentBitmap = textureView.getBitmap();
                    int pixelCount = textureView.getWidth() * textureView.getHeight();
                    int measurementRed = 0;
                    int[] pixels = new int[pixelCount];

                    currentBitmap.getPixels(pixels, 0, textureView.getWidth(), 0, 0, textureView.getWidth(), textureView.getHeight());

                    // extract the red component
                    for (int pixelIndex = 0; pixelIndex < pixelCount; pixelIndex++) {
                        measurementRed += (pixels[pixelIndex] >> 16) & 0xff;
                    }

                    store.add(measurementRed);


                    if (detectValley()) {

                        detectedValleys = detectedValleys + 1;
                        valleys.add(store.getLastTimestamp().getTime());

                        String currentValue = String.format(
                                Locale.getDefault(),
                                activity.getResources().getQuantityString(R.plurals.measurement_output_template, detectedValleys),
                                (valleys.size() == 1)
                                        ? (60f * (detectedValleys) / (Math.max(1, (measurementLength - millisUntilFinished - clipLength) / 1000f)))
                                        : (60f * (detectedValleys - 1) / (Math.max(1, (valleys.get(valleys.size() - 1) - valleys.get(0)) / 1000f))),
                                detectedValleys,
                                1f * (measurementLength - millisUntilFinished - clipLength) / 1000f);


                        sendMessage(CameraActivity.MESSAGE_UPDATE_REALTIME, currentValue);
                    }

                    Thread chartDrawerThread = new Thread(() -> chartDrawer.draw(store.getStdValues()));
                    chartDrawerThread.start();

                });
                thread.start();

            }

            @Override
            public void onFinish() {
                CopyOnWriteArrayList<Measurement<Float>> stdValues = store.getStdValues();

                if (valleys.size() == 0) {
                    mainHandler.sendMessage(Message.obtain(
                            mainHandler,
                            CameraActivity.MESSAGE_CAMERA_NOT_AVAILABLE,
                            "No valleys detected - there may be an issue when accessing the camera."));
                    return;
                }

                String currentValue = String.format(
                        Locale.getDefault(),
                        activity.getResources().getQuantityString(R.plurals.measurement_output_template, detectedValleys - 1),
                        60f * (detectedValleys - 1) / (Math.max(1, (valleys.get(valleys.size() - 1) - valleys.get(0)) / 1000f)),
                        detectedValleys - 1,
                        1f * (valleys.get(valleys.size() - 1) - valleys.get(0)) / 1000f);


                String stressValue = String.format(
                        Locale.getDefault(),
                        activity.getResources().getQuantityString(R.plurals.stressScore_output_template, detectedValleys - 1),
                        60f * (detectedValleys - 1) / (Math.max(1, (valleys.get(valleys.size() - 1) - valleys.get(0)) / 1000f)));

                sendMessage(CameraActivity.MESSAGE_UPDATE_REALTIME, currentValue);

                StringBuilder returnValueSb = new StringBuilder();
                returnValueSb.append(stressValue);
                returnValueSb.append(activity.getString(R.string.row_separator));

                sendMessage(CameraActivity.MESSAGE_UPDATE_FINAL, returnValueSb.toString());

                cameraService.stop();
            }
        };

        activity.setViewState(CameraActivity.VIEW_STATE.MEASUREMENT);
        timer.start();
    }

    void stop() {
        if (timer != null) {
            timer.cancel();
        }
    }

    void sendMessage(int what, Object message) {
        Message msg = new Message();
        msg.what = what;
        msg.obj = message;
        mainHandler.sendMessage(msg);
    }
}