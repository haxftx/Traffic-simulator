package com.apd.tema2.factory;

import com.apd.tema2.Main;
import com.apd.tema2.entities.*;
import com.apd.tema2.intersections.*;
import com.apd.tema2.utils.Constants;

import static java.lang.Thread.sleep;

/**
 * Clasa Factory ce returneaza implementari ale InterfaceHandler sub forma unor
 * clase anonime.
 */
public class IntersectionHandlerFactory {

    public static IntersectionHandler getHandler(String handlerType) {
        // simple semaphore intersection
        // max random N cars roundabout (s time to exit each of them)
        // roundabout with exactly one car from each lane simultaneously
        // roundabout with exactly X cars from each lane simultaneously
        // roundabout with at most X cars from each lane simultaneously
        // entering a road without any priority
        // crosswalk activated on at least a number of people (s time to finish all of
        // them)
        // road in maintenance - 2 ways 1 lane each, X cars at a time
        // road in maintenance - 1 way, M out of N lanes are blocked, X cars at a time
        // railroad blockage for s seconds for all the cars
        // unmarked intersection
        // cars racing
        return switch (handlerType) {
            case "simple_semaphore" -> new IntersectionHandler() {
                @Override
                public void handle(Car car) {
                    // ajunge la semafor
                    System.out.println("Car " + Integer.valueOf(car.getId())
                                        + " has reached the semaphore, now waiting...");
                    try { // asteapta sa fie verde
                        sleep(car.getWaitingTime());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } // pleaca de la semafor
                    System.out.println("Car " + Integer.valueOf(car.getId()) + " has waited enough, now driving...");
                    
                }
            };
            case "simple_n_roundabout" -> new IntersectionHandler() {
                @Override
                public void handle(Car car) {
                    // ajunge in intersectie
                    System.out.println("Car " + Integer.valueOf(car.getId())
                                        + " has reached the roundabout, now waiting...");
                    // intra daca poate
                    ((SimpleIntersection)Main.intersection).acquire();
                    System.out.println("Car " + Integer.valueOf(car.getId()) + " has entered the roundabout");
                    
                    try { // timpul de traversare
                        sleep(car.getWaitingTime());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } // iese din intersectie
                    System.out.println("Car " + Integer.valueOf(car.getId()) + " has exited the roundabout after "
                                                    + ((SimpleIntersection)Main.intersection).getTime() + " seconds");
                    
                    ((SimpleIntersection)Main.intersection).release();

                }
            };
            case "simple_strict_1_car_roundabout" -> new IntersectionHandler() {
                @Override
                public void handle(Car car) {
                    // ajunge in intersectie
                    ((SimpleIntersectionStrict1Car)Main.intersection).acquire(car.getStartDirection());
                    System.out.println("Car " + Integer.valueOf(car.getId()) + " has reached the roundabout");
                    
                    // verifica daca au ajuns din toate directile cate o masina
                    while (((SimpleIntersectionStrict1Car)Main.intersection).Isstart() == false) {
                        if (((SimpleIntersectionStrict1Car)Main.intersection).ceckLane())
                            break;
                        
                    }
                    ((SimpleIntersectionStrict1Car)Main.intersection).start();
                    
                    System.out.println("Car " + Integer.valueOf(car.getId()) + " has entered the roundabout from lane "
                                                                            +Integer.valueOf(car.getStartDirection()));
                    
                    try { // timpul de traversare
                        sleep(((SimpleIntersectionStrict1Car)Main.intersection).getTime());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println("Car " + Integer.valueOf(car.getId()) + " has exited the roundabout after "
                                        + ((SimpleIntersectionStrict1Car)Main.intersection).getTimeS() + " seconds");
                    
                    ((SimpleIntersectionStrict1Car)Main.intersection).release(car.getStartDirection());
                }
            };
            case "simple_strict_x_car_roundabout" -> new IntersectionHandler() {
                @Override
                public void handle(Car car) {                    
                    // ajung in intersectie
                    System.out.println("Car " + Integer.valueOf(car.getId()) + 
                                    " has reached the roundabout, now waiting...");
                    
                    // toate masinile ajung in intersectie
                    ((SimpleIntersectionStrictXCar)Main.intersection).await();

                    // intra in intersectie cate x masini din sens
                    ((SimpleIntersectionStrictXCar)Main.intersection).acquire(car.getStartDirection());
                    System.out.println("Car " + Integer.valueOf(car.getId()) + 
                        " was selected to enter the roundabout from lane " + Integer.valueOf(car.getStartDirection()));
                    
                    // intra pe toate sensurile cate x masini
                    ((SimpleIntersectionStrictXCar)Main.intersection).awaitXCar();

                    try { // timpul de traversare
                        System.out.println("Car " + Integer.valueOf(car.getId()) + 
                                " has entered the roundabout from lane " + Integer.valueOf(car.getStartDirection()));
                        sleep(((SimpleIntersectionStrictXCar)Main.intersection).getTime());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    synchronized (System.out) { // iese din intersectie
                        System.out.println("Car " + Integer.valueOf(car.getId()) + " has exited the roundabout after "
                                        + ((SimpleIntersectionStrictXCar)Main.intersection).getTimeS() + " seconds");
                    }
                    // ies cele x masini de pe fiecare sens
                    ((SimpleIntersectionStrictXCar)Main.intersection).awaitXCar();
                    ((SimpleIntersectionStrictXCar)Main.intersection).release(car.getStartDirection());

                }
                
            };
            case "simple_max_x_car_roundabout" -> new IntersectionHandler() {
                @Override
                public void handle(Car car) {
                    // Get your Intersection instance

                    try {
                        sleep(car.getWaitingTime());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } // NU MODIFICATI
                    // ajung in intersectie
                    System.out.println("Car " + Integer.valueOf(car.getId()) + " has reached the roundabout from lane "
                                                                            +Integer.valueOf(car.getStartDirection()));
                    // intra in intersectie
                    ((SimpleIntersectionMaxXCar)Main.intersection).acquire(car.getStartDirection());
                    System.out.println("Car " + Integer.valueOf(car.getId()) + " has entered the roundabout from lane "
                                                                            +Integer.valueOf(car.getStartDirection()));
                    try {
                        sleep(((SimpleIntersectionMaxXCar)Main.intersection).getTime()); // timpul de asteptare
                    } catch (InterruptedException e) { 
                        e.printStackTrace(); 
                    } // iese din intersectie
                    System.out.println("Car " + Integer.valueOf(car.getId()) + " has exited the roundabout after "
                                            + ((SimpleIntersectionMaxXCar)Main.intersection).getTimeS() + " seconds");
                    ((SimpleIntersectionMaxXCar)Main.intersection).release(car.getStartDirection());
                    
                }
            };
            case "priority_intersection" -> new IntersectionHandler() {
                @Override
                public void handle(Car car) {
                    // Get your Intersection instance

                    try {
                        sleep(car.getWaitingTime());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } // NU MODIFICATI
                    if (car.getPriority() == 1) { // low priority
                        // ajung in intersectie
                        System.out.println("Car " + Integer.valueOf(car.getId()) + 
                                                " with low priority is trying to enter the intersection...");
                        
                        ((IntersectionPriority)Main.intersection).ceckLowPriority(); // intru daca pot
                        System.out.println("Car " + Integer.valueOf(car.getId())
                                            + " with low priority has entered the intersection");
                        
                    } else {
                        ((IntersectionPriority)Main.intersection).acquire();
                        System.out.println("Car " + Integer.valueOf(car.getId())
                                            + " with high priority has entered the intersection");
                            //}
                        try {   
                            sleep(2000); // timpul in intersectie
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        System.out.println("Car " + Integer.valueOf(car.getId())
                                            + " with high priority has exited the intersection");
                        ((IntersectionPriority)Main.intersection).release();
                        
                    }
                }
            };
            case "crosswalk" -> new IntersectionHandler() {
                @Override
                public void handle(Car car) {
                    String red = new String(" has now red light");
                    String green = new String(" has now green light");
                    String message = null;
                    while (!((IntersectionPedestrian)Main.intersection).isFinishedPedestrian()) {             
                        if (((IntersectionPedestrian)Main.intersection).isPassPedestrian()) {
                            message = red;
                            try { // astept sa treaca pietonii
                                sleep(Constants.PEDESTRIAN_PASSING_TIME);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            } 
                        } else {
                            message = green;
                            try { // cat e verde pot trece incontinu economisesc calcule
                                sleep(Constants.PEDESTRIAN_COUNTER_TIME);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        if (!message.equals(((IntersectionPedestrian)Main.intersection).getMessage(car.getId()))) {
                            ((IntersectionPedestrian)Main.intersection).setMessage(car.getId(), message);
                            System.out.println("Car " + car.getId() + message);
                        }
                    }
                    ((IntersectionPedestrian)Main.intersection).await();
                    if (message == red) { // daca ultima data a fost rosu, acum pot trece permanent
                        System.out.println("Car " + car.getId() + green);
                    }
                }
                
            };
            case "simple_maintenance" -> new IntersectionHandler() {
                @Override
                public void handle(Car car) {
                    // ajunge in intersectie
                    System.out.println("Car " + Integer.valueOf(car.getId()) + " from side number "
                                    + Integer.valueOf(car.getStartDirection()) + " has reached the bottleneck");
                    // intra x in intersectie 
                    ((SimpleIntersectionMaintenance)Main.intersection).acquireAwait(car.getStartDirection());
                    System.out.println("Car " + Integer.valueOf(car.getId()) + " from side number "
                                    + Integer.valueOf(car.getStartDirection()) + " has passed the bottleneck");
                    // ies x din intersectie
                    ((SimpleIntersectionMaintenance)Main.intersection).releaseAwait(car.getStartDirection());
                    
                }
            };
            case "complex_maintenance" -> new IntersectionHandler() {
                @Override
                public void handle(Car car) {
                    
                    System.out.println("Car " + Integer.valueOf(car.getId()) + " has come from the lane number "
                                                                        + Integer.valueOf(car.getStartDirection()));
                    // adaug pe banda veche id masinii
                    ((ComplexIntersectionMaintenance)Main.intersection).addQueue(car.getId(), car.getStartDirection());

                    ((ComplexIntersectionMaintenance)Main.intersection).await(); // astept toate msinile
                    // scoat o masina si trece
                    ((ComplexIntersectionMaintenance)Main.intersection).go(car.getStartDirection());
                    
                }
            };
            case "railroad" -> new IntersectionHandler() {
                @Override
                public void handle(Car car) {
                    
                    synchronized (Main.intersection) {
                        // ajung la linia de cale ferata
                        System.out.println("Car " + Integer.valueOf(car.getId()) + " from side number "
                                        + Integer.valueOf(car.getStartDirection()) + " has stopped by the railroad");
                        // adaug masina in coada
                        ((IntersectionReilroad)Main.intersection).addQueue(car.getStartDirection(), car.getId());
                    }
                    ((IntersectionReilroad)Main.intersection).await(); // astept toate masinile
                    if (car.getId() == 0) { // daca e prima masina zice ca a trecut trenul
                        System.out.println("The train has passed, cars can now proceed");
                    }
                    ((IntersectionReilroad)Main.intersection).await();// astept mesajul ca a trecut trenul
                    // gasesc masinile din coada si le permit sa treaca in ordinea in care au venit
                    synchronized (Main.intersection) {
                        Integer id = ((IntersectionReilroad)Main.intersection).getId(car.getStartDirection());
                        System.out.println("Car " + id + " from side number " + 
                                            Integer.valueOf(car.getStartDirection()) + " has started driving");
                    }

                }
            };
            default -> null;
        };
    }
}
