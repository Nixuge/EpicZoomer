package me.nixuge.epiczoomer.manager.obj;

import lombok.Getter;

// GLITCHES LEFT:
// When zooming & cancelling 

public class ZoomObject {
     //TODO: replace this by config value
    private static int ANIMATION_TIME_FOR_100_PERCENT = 1000; //ms

    private double startPercent;

    private long startTime;

    @Getter
    private int targetZoomPercent;

    private int zoomPercentDifferenceStartFinish;
    
    private float animationTime; // Could be an int but for easier calculations might as well make it af loat

    private boolean reducingZoom;
    
    public ZoomObject(int startPercent, int targetPercent) {
        this.startTime = System.currentTimeMillis();
        this.startPercent = startPercent;
        this.zoomPercentDifferenceStartFinish = 99;
        this.targetZoomPercent = targetPercent;
        this.animationTime = getAnimationTime(99);
        this.reducingZoom = false;
    }

    public void updateTargetPercent(int newTargetPercent) {
        this.startPercent = getPercentageToSet();
        this.startTime = System.currentTimeMillis();
        
        // this.startPercent = this.targetZoomPercent;
        // System.out.println(startPercent);
        
        getZoomPercentDiff(newTargetPercent);

        // System.out.println(this.zoomPercentDifferenceStartFinish);

        this.animationTime = getAnimationTime(zoomPercentDifferenceStartFinish);
        this.targetZoomPercent = newTargetPercent;
    }

    private void getZoomPercentDiff(int newTargetPercent) {
        this.zoomPercentDifferenceStartFinish = newTargetPercent - this.targetZoomPercent;
        if (this.zoomPercentDifferenceStartFinish < 0) {
            this.reducingZoom = true;
            this.zoomPercentDifferenceStartFinish = -this.zoomPercentDifferenceStartFinish;
        } else {
            this.reducingZoom = false;
        }
    }

    public boolean hasAnimationEnded() {
        // Getpercentagetoset shouldn't get called here at all
        return getAnimationTimeProgress() > this.animationTime && this.targetZoomPercent < getPercentageToSet();
    }
    public long getAnimationTimeProgress() {
        return System.currentTimeMillis() - this.startTime;
    }

    private int getAnimationTime(int percentChange) {
        // Notes:
        // This is rather dirty, but only gets run whenever the percentage changes, so not a big problem
        // This adapts how much you zoom by the speed,
        // Eg. if you zoom 100x%, you'll have the same zoom speed as if you zoomed 25%

        // ANIMATION_TIME_FOR_100_PERCENT = 100;
        // System.out.println(percentChange);
        // System.out.println(ANIMATION_TIME_FOR_100_PERCENT);
        // System.out.println((percentChange / 100.0) * ANIMATION_TIME_FOR_100_PERCENT);

        return (int)((percentChange / 100.0) * ANIMATION_TIME_FOR_100_PERCENT);
    }

    public double getCurrentProgressPercent() {
        // if (!hasAnimationEnded()) {
        //     // System.out.println((this.startTime + this.animationTime) + " vs " + System.currentTimeMillis());
        //     float timePercent = (getAnimationTimeProgress() / animationTime) * 2; // tanh needs a *2
        //     // System.out.println((Math.tanh(timePercent)) + " vs ");   
        // }
        // Todo: bitshift
        float timePercent = (getAnimationTimeProgress() / animationTime) * 2; // tanh needs a *2
        return Math.tanh(timePercent);
    }

    public double getPercentageToSet() {
        // int toAdd = 
        // System.out.println((getCurrentProgressPercent() * getCurrentProgressPercent()) * (startPercent + zoomPercentDifferenceStartFinish));
        if (this.reducingZoom) {
            return startPercent - (getCurrentProgressPercent()) * zoomPercentDifferenceStartFinish; 
        } else {
            return startPercent + (getCurrentProgressPercent()) * zoomPercentDifferenceStartFinish; 
        }
    }
}
