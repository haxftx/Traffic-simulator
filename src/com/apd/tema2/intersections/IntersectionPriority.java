package com.apd.tema2.intersections;

import com.apd.tema2.entities.Intersection;
import com.apd.tema2.entities.Car;
import java.util.*;
import java.util.concurrent.*;

public class IntersectionPriority implements Intersection {
    // Define your variables here.
    private int higth;
    private Semaphore sem, existPriority;
    
    public IntersectionPriority(int higth, int low) {
        this.sem = new Semaphore(higth);
        this.existPriority = new Semaphore(1);
        this.higth = higth;
    }
    
    public void ceckLowPriority() {
        try {
            this.existPriority.acquire();
            this.existPriority.release();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void acquire() {
        try {
            synchronized (this) {
                if (this.existPriority.availablePermits() == 1) {
                    this.existPriority.acquire();
                }
            }
            this.sem.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public void release() {
        this.sem.release();
        if (this.sem.availablePermits() == this.higth) {
            this.existPriority.release();
        }
    }
}