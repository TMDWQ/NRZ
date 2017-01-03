package com.men_cloths.util;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

/**
 * Created by Administrator on 2016/12/24.
 */

public class ImageCat {
                           //uri            输入的宽      输出的高
    public static void cat(Uri photoUri, int outWith, int outHeight, Activity activity,int code){
        Intent intent = new Intent("com.android.camera.action.CROP")
                .setDataAndType(photoUri, "image/*").putExtra("crop", "true")
                .putExtra("return-data", true);
        // .putExtra("scale", true)
        // // 黑边
        // .putExtra("scaleUpIfNeeded", true)
        // // 黑边
        intent.putExtra("aspectX", outWith);
        intent.putExtra("aspectY", outHeight);
        intent.putExtra("outputX", outWith);
        intent.putExtra("outputY", outHeight);
        activity.startActivityForResult(intent,code);
    }

    public static Bitmap getBitmap(Intent picdata) {
        if(picdata==null){
            return null;
        }
        Bundle extras = picdata.getExtras();
        if (extras != null) {
            // 取得SDCard图片路径做显示
            Bitmap photo = extras.getParcelable("data");
            return photo;
        }
        return null;
    }

//    public void saveBitmap() {
//
//        File f = new File("/sdcard/namecard/", picName);
//        if (f.exists()) {
//            f.delete();
//        }
//        try {
//            FileOutputStream out = new FileOutputStream(f);
//            bm.compress(Bitmap.CompressFormat.PNG, 90, out);
//            out.flush();
//            out.close();
//            Log.i(TAG, "已经保存");
//        } catch (FileNotFoundException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        } catch (IOException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//
//    }


}
