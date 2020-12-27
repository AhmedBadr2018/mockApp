package com.badr.mockapp;

import android.graphics.Color;

public class ResColors {

    public int resColorRSRP(double RSRPValue) {

        int selectedResColor = 0;

        if (RSRPValue <= -110) selectedResColor = Color.parseColor("#000A00");
        else if (RSRPValue > -110 && RSRPValue <= -100) selectedResColor = Color.parseColor("#E51304");
        else if (RSRPValue > -100 && RSRPValue <= -90) selectedResColor = Color.parseColor("#FAFD0C");
        else if (RSRPValue > -90 && RSRPValue <= -80) selectedResColor = Color.parseColor("#02FA0E");
        else if (RSRPValue > -80 && RSRPValue <= -70) selectedResColor = Color.parseColor("#0B440D");
        else if (RSRPValue > -70 && RSRPValue < -60) selectedResColor = Color.parseColor("#0EFFF8");
        else if (RSRPValue >= -60) selectedResColor = Color.parseColor("#0007FF");

        return selectedResColor;
    }

    public int resColorRSRQ(double RSRQValue) {

        int selectedResColor = 0;

        if (RSRQValue <= -19.5) selectedResColor = Color.parseColor("#000000");
        else if (RSRQValue > -19.5 && RSRQValue <= -14) selectedResColor = Color.parseColor("#ff0000");
        else if (RSRQValue > -14 && RSRQValue <= -9) selectedResColor = Color.parseColor("#ffee00");
        else if (RSRQValue > -9 && RSRQValue < -3) selectedResColor = Color.parseColor("#80ff00");
        else if (RSRQValue >= -3) selectedResColor = Color.parseColor("#3f7806");

        return selectedResColor;
    }

    public int resColorSINR(double SINRValue) {

        int selectedResColor = 0;

        if (SINRValue <= 0) selectedResColor = Color.parseColor("#000000");
        else if (SINRValue > 0 && SINRValue <= 5) selectedResColor = Color.parseColor("#F90500");
        else if (SINRValue > 5 && SINRValue <= 10) selectedResColor = Color.parseColor("#FD7632");
        else if (SINRValue > 10 && SINRValue <= 15) selectedResColor = Color.parseColor("#FBFD00");
        else if (SINRValue > 15 && SINRValue <= 20) selectedResColor = Color.parseColor("#00FF06");
        else if (SINRValue > 20 && SINRValue <= 25) selectedResColor = Color.parseColor("#027500");
        else if (SINRValue > 25 && SINRValue <= 30) selectedResColor = Color.parseColor("#0EFFF8");
        else if (SINRValue >= 30) selectedResColor = Color.parseColor("#0000F0");

        return selectedResColor;
    }

}
