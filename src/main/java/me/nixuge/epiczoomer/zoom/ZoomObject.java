package me.nixuge.epiczoomer.zoom;

import lombok.Getter;
import me.nixuge.epiczoomer.McMod;
import me.nixuge.epiczoomer.config.ConfigCache;

public class ZoomObject {
    // Vars
    
    // Why those vars?
    // The function I'm using to calculate the animation is:
    // (tanh(x * z - w) + 1) / 2
    // with:
    // x being the current animation percent (0-1)
    // z being TANH_ENDING_VALUE, basically an offset to make sure the result ends up between 0 and 1. Equals to "w + 2(.5 to avoid bump at end)"
    // w being TANH_STARTING_VALUE_OFFSET, basically an offset since by default the function goes aproximatively from -2 to 2
    //// Note about that one:
    //// Technically the function doesn't start curving at -2, in fact at -2 it's still clearly not flat.
    //// However, every value above 2 (except maybe up to 2.5?) feels way too slow of a start. It's just horrible to use.
    //// 2 on the other hand is almost perfect, except with HIGH animation times it tends to sometimes "bump" a bit at the start/end
    //// Still, keeping it like that for the time being (and prolly forever)
    // the +1 being to bring it out of negatives (by default outputs -1 to 1, with this 0 to 2)
    // the /2 being to bring it between 0 and 1 (as we saw before our output was between 0 and 2, so divide by 2)
    private static float TANH_STARTING_VALUE_OFFSET = 2f;
    private static float TANH_ENDING_VALUE = TANH_STARTING_VALUE_OFFSET + 2.5f;

    private static ConfigCache configCache = McMod.getInstance().getConfigCache();

    private double startPercent;

    private long startTime;

    @Getter private int targetZoomPercent;

    private double zoomPercentDifferenceStartFinish;
    
    private float animationTime;

    private boolean instaZoom = false;
    
    // To be called on init/% change
    public ZoomObject(int startPercent, int targetPercent) {
        this.startTime = System.currentTimeMillis();
        this.startPercent = startPercent;
        this.zoomPercentDifferenceStartFinish = 99;
        this.targetZoomPercent = targetPercent;
        this.animationTime = getAnimationTotalTime(99);

        this.instaZoom = this.animationTime < 15 || configCache.getMsForHundredPercentZoom() < 15;
    }

    public void updateTargetPercent(int newTargetPercent) {
        this.startPercent = getPercentageToSet();
        this.startTime = System.currentTimeMillis();
        
        this.zoomPercentDifferenceStartFinish = newTargetPercent - this.startPercent;

        this.animationTime = getAnimationTotalTime(Math.abs(zoomPercentDifferenceStartFinish));
        this.targetZoomPercent = newTargetPercent;

        this.instaZoom = this.animationTime < 15;
    }

    private float getAnimationTotalTime(double percentChange) {
        // This adapts how much you zoom by the speed,
        // Eg. if you zoom 100x%, you'll have the same zoom speed as if you zoomed 25%
        int msHundred = configCache.getMsForHundredPercentZoom();
        if (msHundred < 16) // Buggy under 15
            msHundred = 15;
        
        if (configCache.isAdaptativeZoomTime()) {
            int animTime = (int)((percentChange / 100.0) * msHundred);
            if (animTime > configCache.getMaxZoomTime())
                return configCache.getMaxZoomTime();
            return animTime;
        }
        // This does not if you have the option disabled
        return msHundred;
    }

    // private void printDebugInfo() {
    //     DecimalFormat df = new DecimalFormat("0", DecimalFormatSymbols.getInstance(Locale.ENGLISH));
    //     df.setMaximumFractionDigits(340); // 340 = DecimalFormat.DOUBLE_FRACTION_DIGITS
    //     System.out.println(
    //         "startpercent: " + startPercent + "\n" +
    //         "targetPercent: " + targetZoomPercent + "\n" +
    //         "startTime: " + startTime + "\n" +
    //         "animationTime: " + df.format(animationTime) + "\n" +
    //         "zoomDiff: " + zoomPercentDifferenceStartFinish + "\n"
    //     );
    // }

    // To be called every frame
    public boolean hasAnimationEnded() {
        // Technically a bit unoptimized calling getPercentageToSet here,
        // but the first condition already filters out most calls to it.
        // This is due to animationTime being a bit inaccurate, as I assume that
        // thanh(2) == 1, while in fact it's more like tanh(2) == 0.964
        return getAnimationTimeProgress() > this.animationTime && Math.abs(this.targetZoomPercent - getPercentageToSet()) < 0.01;
    }

    public long getAnimationTimeProgress() {
        return System.currentTimeMillis() - this.startTime;
    }


    public double getCurrentProgressPercent() {
        // See "TANH_STARTING_VALUE_OFFSET" for values & function meaning
        return (Math.tanh(((getAnimationTimeProgress() / animationTime) * TANH_ENDING_VALUE) - TANH_STARTING_VALUE_OFFSET) + 1) / 2;
    }

    public double getPercentageToSet() {
        if (this.instaZoom) 
            return targetZoomPercent;
        
        double percentage = startPercent + (getCurrentProgressPercent() * zoomPercentDifferenceStartFinish);
        if (percentage < 2) // Mitigate last bump (disable to see its effect)
            return 1;
        return percentage; 
    }
}
