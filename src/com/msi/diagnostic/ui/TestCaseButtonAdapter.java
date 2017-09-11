package com.msi.diagnostic.ui;

import java.util.ArrayList;

import com.msi.diagnostic.R;
import com.msi.diagnostic.data.TestCase;
import com.msi.diagnostic.data.TestResult;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class TestCaseButtonAdapter extends BaseAdapter {
    private static final String TAG = "TestCaseButtonAdapter";
    private static final boolean LOCAL_LOG = false;

    private ArrayList<TestCase> mTestCases = null;
    private LayoutInflater mInflater;
    private ItemClickListener mListener;
    private int mSelectItem;

    public TestCaseButtonAdapter(Context context) {
        mInflater = (LayoutInflater)
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setData(ArrayList<TestCase> testCases) {
        mTestCases = testCases;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return (mTestCases == null) ? 0 : mTestCases.size();
    }

    @Override
    public Object getItem(int position) {
        int maxSize = getCount() - 1;
        if (position > maxSize)
            return null;

        return mTestCases.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private void setTextColor(TextView textView, int status) {
        textView.setTextColor(Color.rgb(192, 192, 192));
        if (status == TestResult.PASS)
            textView.setTextColor(Color.rgb(0, 238, 0));
        else if (status == TestResult.FAIL)
            textView.setTextColor(Color.rgb(238, 0, 0));
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        View view = convertView;
        if (view == null) {
            view = mInflater.inflate(R.layout.testcase_button_item, parent, false);
            TextView testCaseName = (TextView) view.findViewById(R.id.testCaseName);
            TextView resultTextView = (TextView) view.findViewById(R.id.resultTextView);
            ImageView imageView = (ImageView) view.findViewById(R.id.imageView);
            imageView.setImageResource(R.drawable.test);

            viewHolder = new ViewHolder();
            viewHolder.mTestCaseName = testCaseName;
            viewHolder.mResultTextView = resultTextView;
            viewHolder.mImageView = imageView;
            view.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        TextView testCaseName = viewHolder.mTestCaseName;
        TextView resultTextView = viewHolder.mResultTextView;
        ImageView imageView = viewHolder.mImageView;
        TestCase testCase = mTestCases.get(position);
        testCaseName.setText(testCase.getCaption());
        TestResult result = TestResult.create(testCase.getResult());
        resultTextView.setText(result.getResultAsString());
        setTextColor(resultTextView, result.getResult());

        if(position == mSelectItem) {
            viewHolder.mImageView.setVisibility(View.VISIBLE);
        }
        else {
            viewHolder.mImageView.setVisibility(View.INVISIBLE);
        }

        if (LOCAL_LOG)
            Log.d(TAG, "name=" + testCase.getName() + " position=" + position);

        return view;
    }

    public void setSelectItem(int selectItem) {
        mSelectItem = selectItem;
    }

    private class ViewHolder {
        TextView mTestCaseName;
        TextView mResultTextView;
        ImageView mImageView;
    }
}
