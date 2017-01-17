/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2014-2015 bboyfeiyu@gmail.com ( Mr.Simple )
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.jiepier.filemanager.util.imageloader.utils;

import android.util.Log;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.lang.reflect.Field;

public class ImageViewHelper {

    // todo : 配置类中
    private static int DEFAULT_WIDTH = 200;
    private static int DEFAULT_HEIGHT = 200;

    /**
     * {@inheritDoc}
     * <p/>
     * Width is defined by target {@link ImageView view} parameters,
     * configuration parameters or device display dimensions.<br />
     * Size computing algorithm:<br />
     * 1) Get the actual drawn <b>getWidth()</b> of the View. If view haven't
     * drawn yet then go to step #2.<br />
     * 2) Get <b>layout_width</b>. If it hasn't exact value then go to step #3.<br />
     * 3) Get <b>maxWidth</b>.
     */
    public static int getImageViewWidth(ImageView imageView) {
        if (imageView != null) {
            final ViewGroup.LayoutParams params = imageView.getLayoutParams();
            int width = 0;
            if (params != null && params.width != ViewGroup.LayoutParams.WRAP_CONTENT) {
                width = imageView.getWidth(); // Get actual image width
            }
            if (width <= 0 && params != null) {
                width = params.width; // Get layout width parameter
            }
            if (width <= 0) {
                width = getImageViewFieldValue(imageView, "mMaxWidth");
            }
            return width;
        }
        return DEFAULT_WIDTH;
    }

    /**
     * {@inheritDoc}
     * <p/>
     * Height is defined by target {@link ImageView view} parameters,
     * configuration parameters or device display dimensions.<br />
     * Size computing algorithm:<br />
     * 1) Get the actual drawn <b>getHeight()</b> of the View. If view haven't
     * drawn yet then go to step #2.<br />
     * 2) Get <b>layout_height</b>. If it hasn't exact value then go to step #3.
     * <br />
     * 3) Get <b>maxHeight</b>.
     */
    public static int getImageViewHeight(ImageView imageView) {
        if (imageView != null) {
            final ViewGroup.LayoutParams params = imageView.getLayoutParams();
            int height = 0;
            if (params != null
                    && params.height != ViewGroup.LayoutParams.WRAP_CONTENT) {
                height = imageView.getHeight(); // Get actual image height
            }
            if (height <= 0 && params != null) {
                // Get layout height parameter
                height = params.height;
            }
            if (height <= 0) {
                height = getImageViewFieldValue(imageView, "mMaxHeight");
            }
            return height;
        }
        return DEFAULT_HEIGHT;
    }

    private static int getImageViewFieldValue(Object object, String fieldName) {
        int value = 0;
        try {
            Field field = ImageView.class.getDeclaredField(fieldName);
            field.setAccessible(true);
            int fieldValue = (Integer) field.get(object);
            if (fieldValue > 0 && fieldValue < Integer.MAX_VALUE) {
                value = fieldValue;
            }
        } catch (Exception e) {
            Log.e("", e.getMessage());
        }
        return value;
    }
}
