package com.jiepier.filemanager.widget;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.jiepier.filemanager.R;
import com.jiepier.filemanager.base.BaseActivity;
import com.jiepier.filemanager.event.ChangeThemeEvent;
import com.jiepier.filemanager.util.RxBus.RxBus;
import com.jiepier.filemanager.util.SettingPrefUtil;
import com.jiepier.filemanager.util.ThemeUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by sll on 2016/5/17.
 */
public class ColorsDialog extends DialogFragment
    implements AdapterView.OnItemClickListener {

  private static ColorsDialog instance;

  public static ColorsDialog launch(Activity activity) {
    Fragment fragment = activity.getFragmentManager().findFragmentByTag("DialogFragment");
    if (fragment != null) {
      activity.getFragmentManager().beginTransaction().remove(fragment).commit();
    }
    if (instance == null)
      instance = new ColorsDialog();
    return instance;
  }

  private Map<String, ColorDrawable> colorMap = new HashMap<>();

  @Override public Dialog onCreateDialog(Bundle savedInstanceState) {
    setCancelable(true);
    View view = View.inflate(getActivity(), R.layout.dialog_md_colors, null);
    GridView gridView = (GridView) view.findViewById(R.id.grid);
    gridView.setAdapter(new MDColorsAdapter());
    gridView.setOnItemClickListener(this);
    return new MaterialDialog.Builder(getActivity())
            .customView(view,false)
            .positiveText(R.string.cancel)
            .build();
  }

  class MDColorsAdapter extends BaseAdapter {

    @Override public int getCount() {
      return ThemeUtil.themeColorArr.length;
    }

    @Override public Object getItem(int position) {
      return ThemeUtil.themeColorArr[position];
    }

    @Override public long getItemId(int position) {
      return position;
    }

    @Override public View getView(int position, View convertView, ViewGroup parent) {
      if (convertView == null) {
        convertView = View.inflate(getActivity(), R.layout.item_md_colors, null);
      }

      if (!colorMap.containsKey(String.valueOf(position))) {
        colorMap.put(String.valueOf(position),
            new ColorDrawable(getResources().getColor(ThemeUtil.themeColorArr[position][0])));
      }

      ImageView imgColor = (ImageView) convertView.findViewById(R.id.ivColor);
      ColorDrawable colorDrawable = colorMap.get(String.valueOf(position));
      imgColor.setImageDrawable(colorDrawable);

      View imgSelected = convertView.findViewById(R.id.ivSelected);
      imgSelected.setVisibility(
          SettingPrefUtil.getThemeIndex(getActivity()) == position ? View.VISIBLE : View.GONE);

      return convertView;
    }
  }

  @Override public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
    if (position == SettingPrefUtil.getThemeIndex(getActivity())) {
      dismiss();
      return;
    }
    SettingPrefUtil.setThemeIndex(getActivity(), position);
    dismiss();
    RxBus.getDefault().post(new ChangeThemeEvent());
  }

}
