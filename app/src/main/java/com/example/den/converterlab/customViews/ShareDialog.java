package com.example.den.converterlab.customViews;

import android.annotation.TargetApi;
import android.app.DialogFragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.widget.ShareActionProvider;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.den.converterlab.R;
import com.example.den.converterlab.db.DataBaseHelper;
import com.example.den.converterlab.db.UseDataBaseController;
import com.example.den.converterlab.models.Currenci;
import com.example.den.converterlab.models.Organizations;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Den on 21.09.15.
 */
public class ShareDialog extends DialogFragment implements View.OnClickListener {

    private ImageView mImasgeShare;
    private UseDataBaseController mUseDataBaseController;
    private Bundle mBundle;

    private LinearLayout linearLayout;
    private  List<Currenci> listCurrenci;

    private Button mButtonShare;
    private ShareActionProvider mShareActionProvider;
    private Organizations mOrganizations;

    public ShareDialog() {
    }

    @TargetApi(23)
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_fragment_share, container);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        showCoursesByOrg(view);

        mButtonShare = (Button) view.findViewById(R.id.btShare_FDD);
        mButtonShare.setOnClickListener(this);

        return view;
    }

    private void showCoursesByOrg(View view) {
        mUseDataBaseController = new UseDataBaseController(new DataBaseHelper(getActivity()));
        mOrganizations = new Organizations();

        mBundle = getArguments();

        mOrganizations = mUseDataBaseController.getOrganizationFromDB(mBundle.getString("idOrg"));

        mImasgeShare = (ImageView) view.findViewById(R.id.imgShare_DFS);

        linearLayout = new LinearLayout(getActivity());
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        listCurrenci= new ArrayList<>();
        listCurrenci = mOrganizations.getCurrencies().getCurrencyList();

        ShareTitle shareTitle = new ShareTitle(getActivity());
        shareTitle.setConteiner(mOrganizations);
        linearLayout.addView(shareTitle);

        for (int i = 0; i < listCurrenci.size(); i++) {
            ShareModel shareModel = new ShareModel(getActivity());
            shareModel.setConteiner(listCurrenci.get(i));
            linearLayout.addView(shareModel);
        }
        mImasgeShare.setImageBitmap(getBitmapFromView(linearLayout));

    }

    public Bitmap getBitmapFromView(View view) {
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        int width = display.getWidth();
        int height = display.getHeight();

        linearLayout.measure(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        view.layout(0, 0, width, linearLayout.getMeasuredHeight());
        Bitmap bitmap = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Drawable bgDrawable =view.getBackground();
        if (bgDrawable!=null)
            bgDrawable.draw(canvas);
        else
            canvas.drawColor(Color.WHITE);
        view.draw(canvas);
        return bitmap;
    }

    @Override
    public void onClick(View v) {
        sendIntent();
        this.dismiss();
    }

    private void sendIntent() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        File file = saveImage();
        intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
        intent.setType("image/*");
        startActivity(intent);
    }


    public File saveImage() {
        String folderToSave = Environment.getExternalStorageDirectory().toString();
        File file = new File(folderToSave, String.valueOf(mOrganizations.getId().hashCode())+".jpg");
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(file);
            Bitmap bitmap = getBitmapFromView(linearLayout);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();
            MediaStore.Images.Media.insertImage(getActivity().getContentResolver(),
                    file.getAbsolutePath(), file.getName(), file.getName());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }
}
