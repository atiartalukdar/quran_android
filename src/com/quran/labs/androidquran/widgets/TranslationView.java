package com.quran.labs.androidquran.widgets;

import java.util.List;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.preference.PreferenceManager;
import android.text.SpannableStringBuilder;
import android.text.style.StyleSpan;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.quran.labs.androidquran.R;
import com.quran.labs.androidquran.common.QuranAyah;
import com.quran.labs.androidquran.data.ApplicationConstants;
import com.quran.labs.androidquran.data.QuranInfo;

public class TranslationView extends LinearLayout {

   private Context mContext;
   private int mDividerColor;
   private int mLeftRightMargin;
   private int mTopBottomMargin;

   public TranslationView(Context context){
      super(context);
      init(context);
   }

   public TranslationView(Context context, AttributeSet attrs){
      super(context, attrs);
      init(context);
   }

   public TranslationView(Context context, AttributeSet attrs, int defStyle){
      super(context, attrs, defStyle);
      init(context);
   }

   public void init(Context context){
      mContext = context;
      setOrientation(VERTICAL);

      Resources resources = getResources();
      mDividerColor = resources.getColor(R.color.translation_hdr_color);
      mLeftRightMargin = resources.getDimensionPixelSize(
              R.dimen.translation_left_right_margin);
      mTopBottomMargin = resources.getDimensionPixelSize(
              R.dimen.translation_top_bottom_margin);
   }

   public void setAyahs(List<QuranAyah> ayat){
      removeAllViews();

      int currentSura = 0;
      boolean isFirst = true;
      SpannableStringBuilder ayatInSura = new SpannableStringBuilder();
      for (QuranAyah ayah : ayat){
         if (ayah.getSura() != currentSura){
            if (ayatInSura.length() > 0){
               addTextForSura(ayatInSura);
            }
            ayatInSura.clear();
            currentSura = ayah.getSura();
            addSuraHeader(currentSura);

            isFirst = true;
         }

         if (!isFirst){ ayatInSura.append("\n\n"); }
         isFirst = false;
         int start = ayatInSura.length();
         // Ayah Header
         ayatInSura.append(ayah.getSura() + ":" + ayah.getAyah());
         int end = ayatInSura.length();
         ayatInSura.setSpan(new StyleSpan(Typeface.BOLD), start, end, 0);
         ayatInSura.append("\n");
         start = end+1;
         
         // Ayah Text
         ayatInSura.append(ayah.getText());
         end = ayatInSura.length();
         ayatInSura.setSpan(new StyleSpan(Typeface.BOLD), start, end, 0);
         ayatInSura.append("\n");
         start = end+1;
         
         // Translation
         ayatInSura.append(ayah.getTranslation());
         end = ayatInSura.length();
      }
      if (ayatInSura.length() > 0){
         addTextForSura(ayatInSura);
      }
   }

   private void addTextForSura(SpannableStringBuilder stringBuilder){
      boolean nightMode = PreferenceManager.getDefaultSharedPreferences(mContext).
    		  getBoolean(ApplicationConstants.PREF_NIGHT_MODE, false);
      
      TextView translationText = new TextView(mContext);
      translationText.setTextAppearance(mContext, R.style.translation_text);
      if (nightMode) {
    	 translationText.setTextColor(Color.WHITE);
    	 setBackgroundColor(Color.BLACK);
      }
      translationText.setText(stringBuilder);
      LinearLayout.LayoutParams params = new LayoutParams(
              LayoutParams.MATCH_PARENT,
              LayoutParams.WRAP_CONTENT);
      params.setMargins(mLeftRightMargin, mTopBottomMargin,
              mLeftRightMargin, mTopBottomMargin);
      addView(translationText, params);
   }

   private void addSuraHeader(int currentSura){
      View view = new View(mContext);
      
      view.setBackgroundColor(getResources().getColor(R.color.translation_sura_header));
      LinearLayout.LayoutParams params = new LayoutParams(
              LayoutParams.MATCH_PARENT, 2);
      params.topMargin = mTopBottomMargin;
      addView(view, params);

      String suraName = QuranInfo.getSuraName(mContext, currentSura, true);
      TextView headerView = new TextView(mContext);
      params = new LayoutParams(LayoutParams.MATCH_PARENT,
              LayoutParams.WRAP_CONTENT);
      params.leftMargin = mLeftRightMargin;
      params.topMargin = mTopBottomMargin / 2;
      params.bottomMargin = mTopBottomMargin / 2;
      headerView.setText(suraName);
      headerView.setTextAppearance(mContext, R.style.translation_sura_title);
      addView(headerView, params);

      view = new View(mContext);
      view.setBackgroundColor(mDividerColor);
      addView(view, LayoutParams.MATCH_PARENT, 2);
   }
}
