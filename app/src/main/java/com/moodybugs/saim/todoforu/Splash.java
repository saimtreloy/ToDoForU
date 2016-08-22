package com.moodybugs.saim.todoforu;

import android.content.Intent;

import com.daimajia.androidanimations.library.Techniques;
import com.viksaa.sssplash.lib.activity.AwesomeSplash;
import com.viksaa.sssplash.lib.cnst.Flags;
import com.viksaa.sssplash.lib.model.ConfigSplash;

public class Splash extends AwesomeSplash {


    @Override
    public void initSplash(ConfigSplash configSplash) {
        setTheme(R.style.AppThemeNoActionBar);
        //Customize Circular Reveal
        configSplash.setBackgroundColor(R.color.colorPrimary); //any color you want form colors.xml
        configSplash.setAnimCircularRevealDuration(2000); //int ms
        configSplash.setRevealFlagX(Flags.REVEAL_RIGHT);  //or Flags.REVEAL_LEFT
        configSplash.setRevealFlagY(Flags.REVEAL_BOTTOM); //or Flags.REVEAL_TOP

        //Customize Logo
        configSplash.setLogoSplash(R.drawable.ic_splash);


        //Customize Title
        configSplash.setTitleSplash("Todo For You");
        configSplash.setTitleTextColor(R.color.colorWhite);
        configSplash.setTitleTextSize(50f);
        configSplash.setAnimTitleDuration(3000);
        configSplash.setAnimTitleTechnique(Techniques.FlipInX);
        configSplash.setTitleFont("font.ttf");
    }

    @Override
    public void animationsFinished() {
        Intent intent = new Intent(Splash.this, HomePage.class);
        startActivity(intent);
        finish();
    }
}
