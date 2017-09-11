package com.msi.diagnostic.app;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.util.Log;

import com.msi.diagnostic.R;
import com.msi.diagnostic.data.DetectionVerifiedInfo;
import com.msi.diagnostic.data.TestItem;
import com.msi.diagnostic.ui.SpeakerFrontTestCaseView;

public class SpeakerFrontPagePresenter extends AbstractTestCasePresenter
{
    private static final String TAG = "SpeakerFrontPagePresenter";
    private static final boolean LOCAL_LOG = true;
    private SpeakerFrontTestCaseView mView;
    private Context mContext;
    private MediaPlayer mMediaPlayer = null;
    private AudioManager mAudioManager;
    private int mMaxVolume;
    private int mCurrentVolume;
    private boolean mOn_Off = false;
    private MediaPlayer msetBtn;
    private MediaPlayer[] mRandomVoice;
    private final int C_VOICE = 0;
    private final int O_VOICE = 1;
    private final int P_VOICE = 2;
    private final int Y_VOICE = 3;
    private final int F_VOICE = 4;
    private final int J_VOICE = 5;
    private final int K_VOICE = 6;
    private final int L_VOICE = 7;

    private static class TestItemId
    {
        public static final int SPEAKER_FRONT = 0;
    }

    private static final String[] TEST_ITEM_NAME = {
        "isValidSpeakerFront"
    };

    public SpeakerFrontPagePresenter(SpeakerFrontTestCaseView view) {
        super(view);
        mView = view;
        mContext = mView.getDiagnosticApp().getAppContext();
        init();
    }

    private void init() {
        // Set the path of music files
        mMediaPlayer = getRandomVoice();
        // Get volume value
        mAudioManager = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
        mAudioManager.setMode(AudioManager.MODE_IN_CALL);
        mMaxVolume = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        mCurrentVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        // Set the sound to the maximum
        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, mMaxVolume, 0);
    }

    /**
     * Terminate playback and release resources.
     *
     * @author jack
     */
    private void releaseMediaPlayer() {
        if (mMediaPlayer != null) {
            mMediaPlayer.stop();
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }

    @Override
    public void OnButtonClick(int buttonId) {
        // Receives the log file path
        switch (buttonId) {
        case R.id.speaker_front_play:
            playVideo();
            break;

        case R.id.speaker_front_stop:
            stopVideo();
            break;

        case R.id.speaker_front_c:
            msetBtn = mRandomVoice[C_VOICE];
            getFail_Pass();
            break;

        case R.id.speaker_front_o:
            msetBtn = mRandomVoice[O_VOICE];
            getFail_Pass();
            break;

        case R.id.speaker_front_p:
            msetBtn = mRandomVoice[P_VOICE];
            getFail_Pass();
            break;

        case R.id.speaker_front_y:
            msetBtn = mRandomVoice[Y_VOICE];
            getFail_Pass();
            break;

        case R.id.speaker_front_f:
            msetBtn = mRandomVoice[F_VOICE];
            getFail_Pass();
            break;

        case R.id.speaker_front_j:
            msetBtn = mRandomVoice[J_VOICE];
            getFail_Pass();
            break;

        case R.id.speaker_front_k:
            msetBtn = mRandomVoice[K_VOICE];
            getFail_Pass();
            break;

        case R.id.speaker_front_l:
            msetBtn = mRandomVoice[L_VOICE];
            getFail_Pass();
            break;

        case R.id.speaker_front_fail:
            verifyItemResult(TestItemId.SPEAKER_FRONT, false);
            mView.finishTestCase();
            break;
        default:
            break;
        }
    }

    private void playVideo() {
        try {
            if (mMediaPlayer == null) {
                mMediaPlayer = getRandomVoice();
            }
            mOn_Off = true;
            mMediaPlayer.setLooping(true);
            mMediaPlayer.start();
            mView.setTextView(R.string.speaker_front_playing);
        } catch (Exception e) {
            releaseMediaPlayer();
            init();
            e.printStackTrace();
        }
    }

    private void stopVideo() {
        try{
        if (mOn_Off == true) {
            mView.setTextView(R.string.speaker_front_stoping);
            mMediaPlayer.pause();
        } else {
            mView.setTextView(R.string.speaker_front_plzplayVoice);
            init();
        }}
        catch (Exception e) {
            releaseMediaPlayer();
            init();
            e.printStackTrace();
        }
    }

    private void getFail_Pass() {
        if (mOn_Off == true) {
            if (msetBtn.equals(mMediaPlayer)) {
                mView.finishTestCase();
                verifyItemResult(TestItemId.SPEAKER_FRONT, true);
            } else {
                mView.setTextView(R.string.speaker_front_errinfo);
            }
        } else {
            mView.setTextView(R.string.speaker_front_plzplayVoice);
            verifyItemResult(TestItemId.SPEAKER_FRONT, false);
        }
    }

    private void verifyItemResult(int testItemId, boolean result) {
        releaseMediaPlayer();
        // Restore the initial volume
        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC,
                mCurrentVolume, 0);
        String itemName = TEST_ITEM_NAME[testItemId];
        TestItem testItem = getTestItemByName(itemName);
        testItem.verify(new DetectionVerifiedInfo(result));
    }

    @Override
    public void resume() {
        if (LOCAL_LOG)
            Log.d(TAG, "resume");
        super.resume();
    }

    @Override
    public void pause() {
        if (LOCAL_LOG)
            Log.d(TAG, "pause");
        releaseMediaPlayer();
        mAudioManager.setMode(AudioManager.MODE_NORMAL);
        super.pause();
    }

    public boolean ismOn_Off() {
        return mOn_Off;
    }

    public void setmOn_Off(boolean mOn_Off) {
        this.mOn_Off = mOn_Off;
    }

    private MediaPlayer getRandomVoice() {
        MediaPlayer[] RandomVoice = new MediaPlayer[8];
        RandomVoice[C_VOICE] = MediaPlayer.create(mContext, R.raw.c_r);
        RandomVoice[O_VOICE] = MediaPlayer.create(mContext, R.raw.o_r);
        RandomVoice[P_VOICE] = MediaPlayer.create(mContext, R.raw.p_r);
        RandomVoice[Y_VOICE] = MediaPlayer.create(mContext, R.raw.y_r);
        RandomVoice[F_VOICE] = MediaPlayer.create(mContext, R.raw.f_l);
        RandomVoice[J_VOICE] = MediaPlayer.create(mContext, R.raw.j_l);
        RandomVoice[K_VOICE] = MediaPlayer.create(mContext, R.raw.k_l);
        RandomVoice[L_VOICE] = MediaPlayer.create(mContext, R.raw.l_l);
        mRandomVoice = RandomVoice;
        return RandomVoice[(int) (Math.random() * 8)];
    }
}