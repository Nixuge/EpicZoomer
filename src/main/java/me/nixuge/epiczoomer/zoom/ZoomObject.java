package me.nixuge.epiczoomer.zoom;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

import lombok.Getter;

public class ZoomObject {
    // Vars
    
    // Why those vars?
    // The function I'm using to calculate the animation is:
    // (tanh(x * z - w) + 1) / 2
    // with:
    // x being the current animation percent (0-1)
    // z being TANH_ENDING_VALUE, basically an offset to make sure the result ends up between 0 and 1. Equals to "w + 2"
    // w being TANH_STARTING_VALUE_OFFSET, basically an offset since by default the function goes aproximatively from -2 to 2
    //// Note about that one:
    //// Technically the function doesn't start curving at -2, in fact at -2 it's still clearly not flat.
    //// However, every value above 2 (except maybe up to 2.5?) feels way too slow of a start. It's just horrible to use.
    //// 2 on the other hand is almost perfect, except with HIGH animation times it tends to sometimes (rarely) "bump" a bit at the start
    //// Still, keeping it like that for the time being (and prolly forever)
    // the +1 being to bring it out of negatives (by default outputs -1 to 1, with this 0 to 2)
    // the /2 being to bring it between 0 and 1 (as we saw before our output was between 0 and 2, so divide by 2)
    private static float TANH_STARTING_VALUE_OFFSET = 2;
    private static float TANH_ENDING_VALUE = TANH_STARTING_VALUE_OFFSET + 2;

    //TODO: replace this by config value
    private static int ANIMATION_TIME_FOR_100_PERCENT = 1000; //ms

    private double startPercent;

    private long startTime;

    @Getter private int targetZoomPercent;

    private double zoomPercentDifferenceStartFinish;
    
    private float animationTime; // Could be an int but for easier calculations might as well make it af loat
    
    // To be called on init/% change
    public ZoomObject(int startPercent, int targetPercent) {
        ANIMATION_TIME_FOR_100_PERCENT = 200;
        this.startTime = System.currentTimeMillis();
        this.startPercent = startPercent;
        this.zoomPercentDifferenceStartFinish = 99;
        this.targetZoomPercent = targetPercent;
        this.animationTime = getAnimationTotalTime(99);
        printDebugInfo();
    }

    public void updateTargetPercent(int newTargetPercent) {
        this.startPercent = getPercentageToSet();
        this.startTime = System.currentTimeMillis();
        
        this.zoomPercentDifferenceStartFinish = newTargetPercent - this.startPercent;

        this.animationTime = getAnimationTotalTime(Math.abs(zoomPercentDifferenceStartFinish));
        this.targetZoomPercent = newTargetPercent;
        printDebugInfo();
    }

    private float getAnimationTotalTime(double percentChange) {
        // Notes:
        // This adapts how much you zoom by the speed,
        // Eg. if you zoom 100x%, you'll have the same zoom speed as if you zoomed 25%
        // except it can't be above 500ms*
        // TODO: change that max value
        // TODO: add a toggle between modes

        int animTime = (int)((percentChange / 100.0) * ANIMATION_TIME_FOR_100_PERCENT);
        if (animTime > 500)
            animTime = 500;
        return animTime;
    }

    // To be called every frame
    private void printDebugInfo() {
        DecimalFormat df = new DecimalFormat("0", DecimalFormatSymbols.getInstance(Locale.ENGLISH));
        df.setMaximumFractionDigits(340); // 340 = DecimalFormat.DOUBLE_FRACTION_DIGITS
        System.out.println(
            "startpercent: " + startPercent + "\n" +
            "targetPercent: " + targetZoomPercent + "\n" +
            "startTime: " + startTime + "\n" +
            "animationTime: " + df.format(animationTime) + "\n" +
            "zoomDiff: " + zoomPercentDifferenceStartFinish + "\n"
        );
    }

    public boolean hasAnimationEnded() {
        // Technically a bit unoptimized calling getPercentageToSet here,
        // but the first condition already filters out most calls to it.
        // This is due to animationTime being a bit inaccurate, as I assume that
        // thanh(2) == 1, while in fact it's more like tanh(2) == 0.964
        return getAnimationTimeProgress() > this.animationTime && Math.abs(this.targetZoomPercent - getPercentageToSet()) < .01;
    }

    public long getAnimationTimeProgress() {
        return System.currentTimeMillis() - this.startTime;
    }


    public double getCurrentProgressPercent() {
        // See "TANH_STARTING_VALUE_OFFSET" for values & function meaning
        return (Math.tanh(((getAnimationTimeProgress() / animationTime) * TANH_ENDING_VALUE) - TANH_STARTING_VALUE_OFFSET) + 1) / 2;
    }

    public double getPercentageToSet() {
        System.out.println(getCurrentProgressPercent());
        return startPercent + (getCurrentProgressPercent() * zoomPercentDifferenceStartFinish); 
    }
}
