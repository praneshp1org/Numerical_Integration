package com.company;

// *******************************************************
// FILE: Integrate.java
//
// Methods for numerical integration -- the program is
// menu driven allowing the user to select functions to
// integrate and numerical methods to use to approximate
// the integrals.
//
// *******************************************************


import java.util.Scanner;
import java.text.DecimalFormat;

public class Integrate
{
    public static DecimalFormat fmt = new DecimalFormat("0.################");
    public static DecimalFormat fmt2 = new DecimalFormat("0.########");
    public static Scanner scan = new Scanner (System.in);

    // ==========================================
    // Give the user a menu of options until he/
    // she wishes to stop.
    // ==========================================
    public static void main (String[] args)
    {
        String choice = "X";
        int whichFunction = 1;

        System.out.println ();
        System.out.println ("Numerical Integration");
        System.out.println ("=====================");
        System.out.println ();

        while (!choice.equals("Q"))
        {
            choice = menu();
            if (choice.equals("C"))
                whichFunction = chooseFunction();
            else if (choice.equals("I"))
                integrate(whichFunction);
            else if (choice.equals("A"))
                integrateAdapt (whichFunction);
            else if (choice.equals("R"))
                integrateRomberg(whichFunction);
            else if (choice.equals("M"))
                monteCarlo(whichFunction);
            else if (!choice.equals("Q"))
                System.out.println ("Not a valid choice - try again!");
        }
    }

    // ====================================================
    // Print a menu of options; read and return the user's
    // choice as a String.
    // ====================================================
    public static String menu ()
    {
        System.out.println("\nMain Menu");
        System.out.println ("** Choose a function - C");
        System.out.println ("** Integrate a function using composite rules - I");
        System.out.println ("** Integrate using Adaptive Simpson - A");
        System.out.println ("** Integrate using Romberg - R");
        System.out.println ("** Integrate using Monte Carlo - M");
        System.out.println ("** Quit -- Q");
        System.out.print ("\nEnter your choice (C, I, A, R, M or Q): ");
        String choice = scan.next();
        return choice.toUpperCase();
    }

    // =======================================================
    // Print a menu of function choices; read and return the
    // user's choice as an integer.
    // =======================================================
    public static int chooseFunction()
    {
        int whichOne = 0;
        int n = 7;          // number of functions to choose from

        while (whichOne <= 0 || whichOne > n)
        {
            System.out.println ("\nFunction Choices:");
            System.out.println ("1. f(x) = 4 / (1 + x^2)");
            System.out.println ("2. f(x) = Math.sqrt(x) * Math.log(x)");
            System.out.println ("3. f(x) = cos(x)");
            System.out.println ("4. f(x) = 1 / (1 + 100x^2)");
            System.out.println ("5. f(x) = Math.sqrt(Math.abs(x))");
            System.out.println ("6. f(x) = 100/x^2 * Math.sin(10/x)");
            System.out.println ("7. f(x) = x^16 * cos(x^16)");
            System.out.print ("\nEnter the number for the function: ");
            whichOne = scan.nextInt();
            if (whichOne <= 0 || whichOne > n)
                System.out.println ("Invalid choice - try again.");
        }
        return whichOne;
    }


    // ===================================================================
    // Perform numerical integration using the three composite quadrature
    // rules (Midpoint, Trapezoid, and Simpson) on the function whose
    // number is sent as a parameter.  The endpoints of the interval
    // and the number of subdivisions of the interval are read in; the
    // results are printed out.
    // ===================================================================
    public static void integrate(int fcnNum)
    {
        System.out.print ("\nEnter the interval endpoints a and b: ");
        double a = scan.nextDouble();
        double b = scan.nextDouble();
        System.out.print ("Enter the number of subintervals n: ");
        int n = scan.nextInt();
        double simp = Simpson (a, b, n, fcnNum);
        double trap = Trapezoid (a, b, n, fcnNum);
        double mid = MidPoint (a, b, n, fcnNum);
        System.out.println ("\nIntegral Using Composite Rules... ");
        System.out.println ("Interval: [" + a + ", " + b + "]");
        System.out.println ("Number of subdivisions: " + n);
        System.out.println ("Step size h: " + (b-a)/n);
        System.out.println ();
        System.out.println("Rule\t\tApproximation\t\t\tError");
        System.out.println ("Midpoint\t" + fmt.format(mid)
                + "\t\t" + fmt.format(Error(mid,fcnNum)));
        System.out.println ("Trapezoid\t" + fmt.format(trap)
                + "\t\t" + fmt.format(Error(trap,fcnNum)));
        System.out.println ("Simpson\t\t" + fmt.format(simp)
                + "\t\t" + fmt.format(Error(simp,fcnNum)));

        System.out.println ("\nMidpoint Error Est: " +
                "Add the formula here!");
    }


    // =========================================================
    // Perform numerical integration using Adaptive Simpson.
    // The endpoints of the interval and the tolerance are read
    // in. The recursive method AdaptSimpson is called to
    // recursively evaluate the integral.
    // =========================================================
    public static void integrateAdapt (int fcnNum)
    {
        System.out.print ("\nEnter the interval endpoints a and b: ");
        double a = scan.nextDouble();
        double b = scan.nextDouble();
        System.out.print ("Enter the tolerance: ");
        double tol = scan.nextDouble();
        System.out.println ("\nAdaptive Simpson: " +
                AdaptSimp(a,b,tol/2,fcnNum));

    }

    // ========================================================
    // Perform numerical integration using the Monte Carlo
    // method. The endpoints and the number of random points
    // to sample are read in.
    // ========================================================
    public static void monteCarlo (int fcnNum)
    {
        System.out.print ("\nEnter the interval endpoints a and b: ");
        double a = scan.nextDouble();
        double b = scan.nextDouble();
        System.out.print ("Enter the number of points to sample: ");
        int n = scan.nextInt();
        double x;
        double sum = 0;
        for (int i = 0; i < n; i++)
        {
            x = Math.random() * (b - a) + a;
            sum += f(x, fcnNum);
        }
        double approx = (b - a) * sum/n;
        System.out.println ("\nMonte Carlo with " + n + " points: " +
                fmt.format(approx));
    }

    // ============================================================
    // Perform numerical integration using the Romberg method.
    // A triangular array of successive approximations is printed.
    // ============================================================
    public static void integrateRomberg(int fcnNum)
    {
        double[][] T = new double [20][20];

        System.out.print ("\nEnter the interval endpoints a and b: ");
        double a = scan.nextDouble();
        double b = scan.nextDouble();
        System.out.print ("Enter the tolerance: ");
        double tol = scan.nextDouble();

        boolean quit = false;
        int k = 0;
        int j = 0;
        System.out.println ("\nRomberg: ");
        int n = 1;
        while (k < 20 && !quit)
        {
            j = 0;
            T[k][0] = Trapezoid (a, b, n, fcnNum);
            System.out.print (fmt2.format(T[k][0]));
            for (j = 1; j <= k; j++)
            {
                double FourJ = Math.pow(4, j);
                T[k][j] = (FourJ*T[k][j-1] - T[k-1][j-1])/(FourJ - 1);
                System.out.print ("\t" + fmt2.format(T[k][j]));
            }
            System.out.println ();
            n = n * 2;
            if (k !=0 && Math.abs(T[k][k] - T[k-1][k-1]) < tol)
                quit = true;
            k++;
        }
    }


    // ====================================================================
    // Compute the numerical approximation of the integral of the given
    // function (determined by the parameter fcnNum) on the interval
    // [a,b] using the composite Midpoint rule with n subdivisions of
    // [a,b]; return the result.
    // ====================================================================
    public static double MidPoint (double a, double b, int n, int fcnNum)
    {
        return 0;
    }

    // ====================================================================
    // Compute the numerical approximation of the integral of the given
    // function (determined by the parameter fcnNum) on the interval
    // [a,b] using the composite Trapezoid rule with n subdivisions of
    // [a,b]; return the result.
    // ====================================================================
    public static double Trapezoid (double a, double b, int n, int fcnNum)
    {
        double h = (b - a)/n;
        double x = a;
        double sum = 0;

        for (int i = 1; i < n; i++)
        {
            x = a + i * h;
            sum += f(x, fcnNum);
        }

        sum = 2 * sum + (f(a, fcnNum) + f(b, fcnNum));

        return sum * h / 2;
    }


    // ====================================================================
    // Compute the numerical approximation of the integral of the given
    // function (determined by the parameter fcnNum) on the interval
    // [a,b] using the composite Simpson rule with n subdivisions of
    // [a,b]; return the result.
    // ====================================================================
    public static double Simpson (double a, double b, int n, int fcnNum)
    {
        double h = (b - a)/n;
        double x = a;
        double mid = x + h/2;
        double sum = f(x, fcnNum);
        int i = 0;

        while (i < n - 1)
        {
            i++;
            x = a + i * h;
            sum = sum + 4 * f(mid, fcnNum) + 2 * f(x, fcnNum);
            mid = x + h/2;
        }

        sum = sum + 4 * f(mid, fcnNum) + f(b, fcnNum);
        return sum * h / 6;
    }

    // ========================================================
    // Recursive algorithm for the Adaptive Simpson method.
    // ========================================================
    public static double AdaptSimp (double a, double b, double tol, int fcnNum)
    {
        double S1, S2;
        double approx;
        double mid;

        System.out.println ("Points " + a + "  " + b);
        S1 = Simpson (a, b, 1, fcnNum);
        S2 = Simpson (a, b, 2, fcnNum);

        if (Math.abs (S1 - S2) < 15 * tol)
            approx = S2;
        else
        {
            mid = (a + b)/2;
            double ans1 = AdaptSimp (a, mid, tol/2, fcnNum);
            double ans2 = AdaptSimp (mid, b, tol/2, fcnNum);
            approx = ans1 + ans2;
        }

        return (approx);
    }


    // ==================================================================
    // Compute and return f(x) for a given x and a function f determined
    // by the parameter fcnNum.
    // ==================================================================
    public static double f(double x, int fcnNum)
    {
        double value;     // f(x)

        switch (fcnNum)
        {
            case 1: value = 4 / (1 + x*x);
                break;
            case 2: value = Math.sqrt(x) * Math.log(x);
                break;
            case 3: value = Math.cos(x);
                break;
            case 4: value = 1 / (1 + 100*x*x);
                break;
            case 5: value = Math.sqrt(Math.abs(x));
                break;
            case 6: value = 100/(x*x) * Math.sin(10/x);
                break;
            case 7: double x16 = Math.pow(x, 16);
                value = x16 * Math.cos(x16);
                break;
            default: value = 0;
        }
        return value;
    }


    // ===================================================================
    // Compute and return the absolute value of the error in approximating
    // the integral of the given function (determined by "fcnNum") by
    // the parameter approx.
    // ===================================================================
    public static double Error (double approx, int fcnNum)
    {
        double actual;   // correct value of the integral

        switch (fcnNum)
        {
            case 1: actual = Math.PI;
                break;
            case 2: actual = -4./9;
                break;
            case 3: actual = 2 * Math.sin(1);
                break;
            case 4: actual = 0.2 * Math.atan(10);
                break;
            case 5: actual = 4./3;
                break;
            case 6: actual = -1.426024763463;
                break;
            case 7: actual = 0.0491217;
                break;
            default: actual = 0;
        }
        return (Math.abs(actual - approx));
    }
}
