package com.apd.tema2.intersections;

import com.apd.tema2.Main;
import com.apd.tema2.entities.Intersection;
import com.apd.tema2.entities.Car;
import java.util.*;
import java.util.concurrent.*;

public class ComplexIntersectionMaintenance implements Intersection {
    // Define your variables here.
    private int nLane, oLane, x;
    private ConcurrentHashMap<Integer, ArrayBlockingQueue<Integer>> mapTotal;
    private ConcurrentHashMap<Integer, ArrayBlockingQueue<Integer>> mapNew;
    private int[] array;
    private Semaphore sem;
    private CyclicBarrier barrier;

    public ComplexIntersectionMaintenance(int nLane, int oLane, int x) {
        this.barrier = new CyclicBarrier(Main.carsNo);
        this.mapTotal = new ConcurrentHashMap<Integer, ArrayBlockingQueue<Integer>>();
        this.mapNew = new ConcurrentHashMap<Integer, ArrayBlockingQueue<Integer>>();
        this.array = new int[oLane];
        this.oLane = oLane;
        this.nLane = nLane;
        this.x = x;
        this.sem = new Semaphore(1);

        for (int i = 0; i < nLane; i++)
            this.mapNew.put(i, new ArrayBlockingQueue<Integer>(Main.carsNo));
        
        for (int i = 0; i < oLane; i++) {
            this.array[i] = 0;
            this.mapTotal.put(i, new ArrayBlockingQueue<Integer>(Main.carsNo));
            try {
                this.mapNew.get(getNewLane(i)).put(i);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    private int getNewLane(int id) {
        return Math.min(this.nLane - 1, id / (this.oLane / this.nLane));
    }
    public void await() {
        try {
            this.barrier.await();
        } catch (InterruptedException | BrokenBarrierException e) {
            e.printStackTrace();
        }
    }

    public void addQueue(int id, int lane) {
        try {
            this.mapTotal.get(lane).put(id);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    private boolean isEmpty(int lane) {
        return null == this.mapTotal.get(lane).peek();
    }

    private void swap(int lane) {
        // schimba coada de asteptare pentru noile benzi
        int x = -1;
        try {
            x = this.mapNew.get(lane).take();
            if (x != -1) {
                if (this.mapTotal.get(x).peek() != null)
                    this.mapNew.get(lane).put(x);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
    }

    public void go(int lane) {
        try {
            sem.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        try {
            int i = getNewLane(lane);
            int firstlane = this.mapNew.get(i).peek();
            int  firstCar = this.mapTotal.get(firstlane).take();
            // intra pe banda
            System.out.println("Car " + firstCar + " from the lane " + firstlane + " has entered lane number " + i);
            
            this.array[firstlane]++;
            if (this.array[firstlane] == this.x || isEmpty(firstlane)) {
                if (isEmpty(firstlane)) { // da e goala o sterg
                    System.out.println("The initial lane " + Integer.valueOf(firstlane)
                                        + " has been emptied and removed from the new lane queue");
                    swap(getNewLane(firstlane));
                } else { // daca au trecut x masini o mut la urma
                    System.out.println("The initial lane " + Integer.valueOf(firstlane)
                                        + " has no permits and is moved to the back of the new lane queue");
                    swap(getNewLane(firstlane));
                }
                this.array[firstlane] = 0;
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        sem.release();
    }
}