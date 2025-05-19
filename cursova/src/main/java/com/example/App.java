package com.example;

import com.example.utils.testing.Test;

public class App 
{
    public static void main( String[] args )
    {
        Test.runThrasholdP(10);
        Test.runThrasholdQ(10);

        Test.runSublistParamP(10);
        Test.runSublistParamQ(10);

        Test.runThreadsP(10);
        Test.runThreadsQ(10);

        Test.runSingle(10);
    }
}