import java.io.*;
import java.util.*;

public class BarnesHutBody{

    double X, Y, Vx, Vy, M;
    String S;

    double a; //acceleration 
    double ax;
    double ay;

    double F; 
    double Fx = 1;
    double Fy = 1;

    int type;//0 for internal nodes, 1 for final nodes(leafs?!)
    int cnt;//counts the sub nodes

    double length;

    BarnesHutBody parent;
    BarnesHutBody child1, child2, child3, child4;//quad tree 

    BarnesHutBody next;

    public BarnesHutBody(double X, double Y, double Vx, double Vy, double M, String S, int type) {

        this.X = X;
        this.Y = Y;
        this.Vx = Vx;
        this.Vy = Vy;                                         
        this.M = M;
        this.S = S;
        this.cnt = 0;
        this.type = type;
        child1 = child2 = child3 = child4 = null;
        next = null;
        length = 0;
        parent = null;
        Fx = Fx;
        Fy = Fy;

    }
}
