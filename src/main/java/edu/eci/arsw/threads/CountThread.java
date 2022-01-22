/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.eci.arsw.threads;

import java.lang.Thread;

/**
 *
 * @author hcadavid
 */
public class CountThread extends Thread{
	
	private int numero1;
	private int numero2;
    
	CountThread (int A, int B){
		numero1 = A;
		numero2 = B;
	}
	
	public void run() {
		for (int i = numero1; i <= numero2; i++) {
			System.out.print(i + " ");
		}
	}
	
}


