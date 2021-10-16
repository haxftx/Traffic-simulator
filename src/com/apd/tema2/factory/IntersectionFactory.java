package com.apd.tema2.factory;

import com.apd.tema2.entities.Intersection;
import com.apd.tema2.intersections.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Prototype Factory: va puteti crea cate o instanta din fiecare tip de implementare de Intersection.
 */
public class IntersectionFactory {
    

    public static Intersection getIntersection(String handlerType, String[] args) {
        switch (handlerType) {
            case "simple_n_roundabout":
                return new SimpleIntersection(Integer.parseInt(args[0]), Integer.parseInt(args[1]));
            case "simple_strict_1_car_roundabout":
                return new SimpleIntersectionStrict1Car(Integer.parseInt(args[0]), Integer.parseInt(args[1]));
            case "simple_strict_x_car_roundabout":
                return new SimpleIntersectionStrictXCar(Integer.parseInt(args[0]), Integer.parseInt(args[1]), Integer.parseInt(args[2]));
            case "simple_max_x_car_roundabout":
                return new SimpleIntersectionMaxXCar(Integer.parseInt(args[0]), Integer.parseInt(args[1]), Integer.parseInt(args[2]));
            case "priority_intersection":
                return new IntersectionPriority(Integer.parseInt(args[0]), Integer.parseInt(args[1]));
            case "crosswalk":
                return new IntersectionPedestrian(Integer.parseInt(args[0]), Integer.parseInt(args[1]));
            case "simple_maintenance":
                return new SimpleIntersectionMaintenance(Integer.parseInt(args[0]));
            case "complex_maintenance":
                return new ComplexIntersectionMaintenance(Integer.parseInt(args[0]), Integer.parseInt(args[1]), Integer.parseInt(args[2]));
            case "railroad":
                return new IntersectionReilroad();
            default:
                return null;
        }
    }

}
