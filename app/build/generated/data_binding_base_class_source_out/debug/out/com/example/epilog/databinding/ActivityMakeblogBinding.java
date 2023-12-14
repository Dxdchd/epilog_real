// Generated by view binder compiler. Do not edit!
package com.example.epilog.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import androidx.viewpager2.widget.ViewPager2;
import com.example.epilog.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;
import me.relex.circleindicator.CircleIndicator3;

public final class ActivityMakeblogBinding implements ViewBinding {
  @NonNull
  private final ScrollView rootView;

  @NonNull
  public final Button addimg;

  @NonNull
  public final ImageButton blogback;

  @NonNull
  public final EditText blogtext;

  @NonNull
  public final Button datebtn;

  @NonNull
  public final ImageView feel;

  @NonNull
  public final LinearLayout feelWeather;

  @NonNull
  public final ImageButton help;

  @NonNull
  public final CircleIndicator3 indicator;

  @NonNull
  public final EditText locationbtn;

  @NonNull
  public final Button makesuc;

  @NonNull
  public final EditText title;

  @NonNull
  public final ImageView viewimg;

  @NonNull
  public final ViewPager2 viewpager;

  @NonNull
  public final ImageView weather;

  private ActivityMakeblogBinding(@NonNull ScrollView rootView, @NonNull Button addimg,
      @NonNull ImageButton blogback, @NonNull EditText blogtext, @NonNull Button datebtn,
      @NonNull ImageView feel, @NonNull LinearLayout feelWeather, @NonNull ImageButton help,
      @NonNull CircleIndicator3 indicator, @NonNull EditText locationbtn, @NonNull Button makesuc,
      @NonNull EditText title, @NonNull ImageView viewimg, @NonNull ViewPager2 viewpager,
      @NonNull ImageView weather) {
    this.rootView = rootView;
    this.addimg = addimg;
    this.blogback = blogback;
    this.blogtext = blogtext;
    this.datebtn = datebtn;
    this.feel = feel;
    this.feelWeather = feelWeather;
    this.help = help;
    this.indicator = indicator;
    this.locationbtn = locationbtn;
    this.makesuc = makesuc;
    this.title = title;
    this.viewimg = viewimg;
    this.viewpager = viewpager;
    this.weather = weather;
  }

  @Override
  @NonNull
  public ScrollView getRoot() {
    return rootView;
  }

  @NonNull
  public static ActivityMakeblogBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ActivityMakeblogBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.activity_makeblog, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ActivityMakeblogBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.addimg;
      Button addimg = ViewBindings.findChildViewById(rootView, id);
      if (addimg == null) {
        break missingId;
      }

      id = R.id.blogback;
      ImageButton blogback = ViewBindings.findChildViewById(rootView, id);
      if (blogback == null) {
        break missingId;
      }

      id = R.id.blogtext;
      EditText blogtext = ViewBindings.findChildViewById(rootView, id);
      if (blogtext == null) {
        break missingId;
      }

      id = R.id.datebtn;
      Button datebtn = ViewBindings.findChildViewById(rootView, id);
      if (datebtn == null) {
        break missingId;
      }

      id = R.id.feel;
      ImageView feel = ViewBindings.findChildViewById(rootView, id);
      if (feel == null) {
        break missingId;
      }

      id = R.id.feelWeather;
      LinearLayout feelWeather = ViewBindings.findChildViewById(rootView, id);
      if (feelWeather == null) {
        break missingId;
      }

      id = R.id.help;
      ImageButton help = ViewBindings.findChildViewById(rootView, id);
      if (help == null) {
        break missingId;
      }

      id = R.id.indicator;
      CircleIndicator3 indicator = ViewBindings.findChildViewById(rootView, id);
      if (indicator == null) {
        break missingId;
      }

      id = R.id.locationbtn;
      EditText locationbtn = ViewBindings.findChildViewById(rootView, id);
      if (locationbtn == null) {
        break missingId;
      }

      id = R.id.makesuc;
      Button makesuc = ViewBindings.findChildViewById(rootView, id);
      if (makesuc == null) {
        break missingId;
      }

      id = R.id.title;
      EditText title = ViewBindings.findChildViewById(rootView, id);
      if (title == null) {
        break missingId;
      }

      id = R.id.viewimg;
      ImageView viewimg = ViewBindings.findChildViewById(rootView, id);
      if (viewimg == null) {
        break missingId;
      }

      id = R.id.viewpager;
      ViewPager2 viewpager = ViewBindings.findChildViewById(rootView, id);
      if (viewpager == null) {
        break missingId;
      }

      id = R.id.weather;
      ImageView weather = ViewBindings.findChildViewById(rootView, id);
      if (weather == null) {
        break missingId;
      }

      return new ActivityMakeblogBinding((ScrollView) rootView, addimg, blogback, blogtext, datebtn,
          feel, feelWeather, help, indicator, locationbtn, makesuc, title, viewimg, viewpager,
          weather);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}