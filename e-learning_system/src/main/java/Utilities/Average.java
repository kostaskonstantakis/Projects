/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Utilities;

import gr.csd.uoc.cs359.winter2019.logbook.model.Rating;
import java.util.List;

/**
 *
 * @author gussl
 */
public class Average {
    public static int GetAverage(List<Rating> rating_average)
    {
        int average_rating = 0;
        int number_of_rating = 0;
        
        for(Rating avg : rating_average)
        {
            average_rating += avg.getRate();
            number_of_rating++;
        }
        
        return (number_of_rating !=0) ? (average_rating / number_of_rating) : 0;
    }
}
