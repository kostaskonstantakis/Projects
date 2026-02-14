import java.util.*;
import java.io.*;
import java.util.logging.*;
import java.lang.*;

public class Main extends Thread {

    static BarnesHutBody ROOT;

    static int N = 0;
    static double R = 0;
    static double X, Y, Vx, Vy, M;
    static String S;
    static Scanner scanner;
    static int repetitions = 0;
    static File input;
    static File output;
    static int threadCount=0;
    static double G=6.67e-11;
	
    static String outputfile2=null;
    static long begin;

     @Override
    public void run() {
	 for (int i = 0; i < repetitions; i++) {
	    elements = new Vector();
            foreachElement(ROOT);//compute new positions
            ROOT = null;
            ROOT = create_tree(ROOT, -R, R, -R, R, elements, N, 0, null, "UNIV");
            print_universe(ROOT, 'O', 0);
            System.out.println("\n############################################\n");
	}

        try {
             outputfile = new FileWriter(outputfile2); 
             outputfile.write(N+"\n");
             outputfile.write(R+"\n");
             print_output(ROOT);
             outputfile.close();
	     long end = System.nanoTime();
             long time = end-begin;
             System.out.println("Elapsed Time: "+time +" nanoseconds");

        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
}

    static void print_tab(int level) {
        int i;
        for (i = 0; i < level; i++) {
            System.out.print(" ");
        }
        return;
    }

    static void kentromazas(Vector<BarnesHutBody> elements, BarnesHutBody elem) {

        double m = 0;
        elem.X = 0;
        elem.Y = 0;

        for (BarnesHutBody i : elements) {
            m += i.M;
        }

        elem.M = m;

        for (BarnesHutBody temp : elements) {
            m += temp.M;
            elem.X += (temp.X * temp.M) / (double) m;
            elem.Y += (temp.Y * temp.M) / (double) m;
        }

    }

    static Vector<BarnesHutBody> create_sublist(Vector<BarnesHutBody> elements, double x1, double x2, double y1, double y2) {
        Vector<BarnesHutBody> elements2 = new Vector();

       

        for (BarnesHutBody tmp : elements) { //simultaneous traversal & removal is erroneous
            if (tmp.X >= x1 && tmp.X <= x2 && tmp.Y >= y1 && tmp.Y <= y2) {
                elements2.add(tmp);
                //elements.remove(tmp);
            }
        }

        //with 2 different iterations, it's not prone to errors.
        for (BarnesHutBody tmp : elements2) {
            elements.remove(tmp);
        }
       

        return elements2;

    }

//elements contains bodies that belong to space [x1,x2,y1,y2]
    static BarnesHutBody create_tree(BarnesHutBody R, double x1, double x2, double y1, double y2, Vector<BarnesHutBody> elements, int elems, int level, BarnesHutBody PARENT, String name) {

        //malloc node and distribute elements
        if (elems > 1) {

            R = new BarnesHutBody(1, 2, 3, 4, 5, name, 0);
            R.cnt = elems;

            R.length = x2 - x1;
            R.parent = PARENT;

            //ypologismos kentro mazas
            kentromazas(elements, R);

            //panw deksia
            int counter = 0;
            Vector<BarnesHutBody> elements2 = create_sublist(elements, x1 + (x2 - x1) / 2, x2, y1 + (y2 - y1) / 2, y2);

            String name2 = name + "A" + level;
            
            counter = elements2.size();
            R.child1 = create_tree(R, x1 + (x2 - x1) / 2, x2, y1 + (y2 - y1) / 2, y2, elements2, counter, level + 1, R, name2);

            //panw aristera
           
            elements2 = create_sublist(elements, x1, x2 - (x2 - x1) / 2, y1 + (y2 - y1) / 2, y2);
            
            name2 = name + "B" + level;
            counter = elements2.size();
            R.child2 = create_tree(R, x1, x2 - (x2 - x1) / 2, y1 + (y2 - y1) / 2, y2, elements2, counter, level + 1, R, (name2));

            //katw aristera
           
            elements2 = create_sublist(elements, x1, x2 - (x2 - x1) / 2, y1, y2 - (y2 - y1) / 2);
           
            name2 = name + "C" + level;
            counter = elements2.size();
            R.child3 = create_tree(R, x1, x2 - (x2 - x1) / 2, y1, y2 - (y2 - y1) / 2, elements2, counter, level + 1, R, (name2));

            //katw deksia
            
            elements2 = create_sublist(elements, x1 + (x2 - x1) / 2, x2, y1, y2 - (y2 - y1) / 2);
            
            name2 = name + "D" + level;
            counter = elements2.size();
            R.child4 = create_tree(R, x1 + (x2 - x1) / 2, x2, y1, y2 - (y2 - y1) / 2, elements2, counter, level + 1, R, (name2));
        } else {//0 h 1
            R = new BarnesHutBody(0, 0, 0, 0, 0, null, 1);
            R.cnt = elems;
            R.parent = PARENT;
            //kentromazas(elements,&R.M, &R.X,&R.Y);

            if (R.cnt == 1) {//vector has only one element
                R.S = elements.get(0).S;
                R.M = elements.get(0).M;
                R.X = elements.get(0).X;
                R.Y = elements.get(0).Y;

                R.Vx = elements.get(0).Vx;
                R.Vy = elements.get(0).Vy;

            }
        }

        return R;
    }

    static void print_universe(BarnesHutBody R, char space, int level) {

        if (R != null) {
            print_tab(level);
            if (R.type == 0) { //internal node

                System.out.println(space + " INTERNAL (" + R.cnt + ") " + "(" + R.X + "," + R.Y + ") (" + R.M + ") (L: " + R.length + ")");

            } else {  //leaf

                System.out.print(space + " LEAF (" + R.cnt + ")" + "(" + R.X + "," + R.Y + ") (" + R.M + ")");

                if (R.cnt == 1) {
                    System.out.print(R.S);

                }
                System.out.println("");
            }

            print_universe(R.child1, 'A', level + 1);
            print_universe(R.child2, 'B', level + 1);
            print_universe(R.child3, 'C', level + 1);
            print_universe(R.child4, 'D', level + 1);

        }
    }
static FileWriter outputfile = null;
    static void print_output(BarnesHutBody R) {

        if (R != null) {
            if (R.type == 1 && R.cnt == 1) { //internal node

                try {
                    outputfile.write(R.X + " " + R.Y + " " + R.Vx + " " + R.Vy + " " + R.M + " " + R.S+"\n");
                } catch (IOException ex) {
                    Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

            print_output(R.child1);
            print_output(R.child2);
            print_output(R.child3);
            print_output(R.child4);
        }
    }
	
static int checkSubSpace(BarnesHutBody space, BarnesHutBody element){
	
	if(element==null)return 0;
	if(space==null)return 0;

	while(element.parent!=null){
		if(element.parent==space)return 1;
		element=element.parent;
	}

	return 0;
}




static void netForce(BarnesHutBody R, BarnesHutBody elem){
	
	if(R!=null){
		
		BarnesHutBody[] children = new BarnesHutBody[4];
		children[0] = R.child1;
		children[1] = R.child2;
		children[2] = R.child3;
		children[3] = R.child4;
		
		int i;
		for(i=0;i<4;i++){
			if(children[i]!=null){
				if(children[i].type == 0){//internal node
					//check if elem exists on this universe
					
					if( 1==checkSubSpace(children[i], elem) ){
						netForce(children[i],elem);
					}else{//calculate F
					
						elem.F +=	(G * (elem.M) * children[i].M)/ ( Math.pow(elem.X-children[i].X,2)-Math.pow(elem.Y-children[i].Y,2) );
							//r=sqrt((x1-x2)^2-(y1-y2)^2) r^2=((x1-x2)^2-(y1-y2)^2)
					}
					
				}else if(children[i]!= elem && children[i].cnt==1){
					elem.F +=	(G * (elem.M) * children[i].M)/ ( Math.pow(elem.X-children[i].X,2)-Math.pow(elem.Y-children[i].Y,2) );
				}
			}
		}	
	}
}

    static Vector<BarnesHutBody> elements = new Vector();

    static void foreachElement(BarnesHutBody R) {
        if (R != null) {
            if (R.type == 1 && R.cnt == 1) { //internal node

				netForce(ROOT, R);

                R.ax = R.Fx / R.M;
                R.ay = R.Fy / R.M;	

                R.Vx = R.Vx + R.ax;
                R.Vy = R.Vy + R.ay;

                R.X = R.X + R.Vx;
                R.Y = R.Y + R.Vy;

                R.next = null;

                elements.add(R);//add again to the list to be processed 
            }
            foreachElement(R.child1);
            foreachElement(R.child2);
            foreachElement(R.child3);
            foreachElement(R.child4);
        }
    }

    public static void main(String[] args) {

        //start counting running time
        begin = System.nanoTime();
        
        repetitions = Integer.parseInt(args[0]);
	threadCount=Integer.parseInt(args[1]);
        input =new File(args[2]);  
        output =new File(args[3]); 

        try {
            scanner = new Scanner(input);

            String str;
            N = Integer.valueOf(scanner.nextLine());
            R = Double.valueOf(scanner.nextLine());

            System.out.println(N);
            System.out.println(R);

            while (scanner.hasNextLine()) { 

                String s = scanner.nextLine();

                X = Double.valueOf(s.split(" ")[0]);
                Y = Double.valueOf(s.split(" ")[1]);
                Vx = Double.valueOf(s.split(" ")[2]);
                Vy = Double.valueOf(s.split(" ")[3]);
                M = Double.valueOf(s.split(" ")[4]);
                S = new String(s.split(" ")[5]);

                BarnesHutBody element = new BarnesHutBody(X, Y, Vx, Vy, M, S, 1);

                elements.add(element);


            }
            scanner.close();

            ROOT = create_tree(ROOT, -R, R, -R, R, elements, N, 0, null, "UNIV");

        } catch (FileNotFoundException ex) {
            System.err.println(ex.getMessage());
        }

        print_universe(ROOT, 'O', 0);

        System.out.println("\n############################################\n");

		outputfile2 = args[3];
		

		try{
			for(int q=0;q<threadCount;q++)
			{	
				new Main().start();
				new Main().join();
			}

			long end = System.nanoTime();
      			long time = end-begin;
      			System.out.println("Elapsed Time: "+time +" nanoseconds");


		}catch(Exception ex) {System.err.println(ex.getMessage()); }  
      
    }

}
