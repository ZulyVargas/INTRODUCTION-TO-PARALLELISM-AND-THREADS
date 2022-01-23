package edu.eci.arsw.blacklistvalidator;

import java.util.LinkedList;

import edu.eci.arsw.spamkeywordsdatasource.HostBlacklistsDataSourceFacade;

/**
 * 
 * @author Camilo Pichimata, Zuly Vargas 
 *
 */
public class BlackListValidator extends Thread {
	
	private static final int BLACK_LIST_ALARM_COUNT=5;
	private int initial_server, final_server;
	private int ocurrencesCount;
	private String ipaddress; 
	private HostBlacklistsDataSourceFacade skds;
	private LinkedList<Integer> blackListOcurrences;
	private int checkedListsCount;
	
	
	public BlackListValidator (int initial_server, int final_server, String ipaddress, HostBlacklistsDataSourceFacade skds){
		this.initial_server = initial_server;
		this.final_server = final_server;
		this.ocurrencesCount = 0 ;
		this.ipaddress = ipaddress;
		this.skds = skds;
		this.blackListOcurrences = new  LinkedList<Integer>();
		this.checkedListsCount = 0; 
	}
	
	/**
	 * Performs the search for the ip address in the malicious server lists.
	 *   
	 */
	public void run() {
		checkedListsCount = 0;
		for (int i = initial_server ; i <= final_server && ocurrencesCount < BLACK_LIST_ALARM_COUNT ; i++) {
			checkedListsCount = checkedListsCount + 1;
			if (skds.isInBlackListServer(i, ipaddress)){         
				blackListOcurrences.add(i);                
			    ocurrencesCount = 1 + ocurrencesCount ;
			}	
		}
	}
	
	/**
	 * Returns the number of occurrences of malicious servers.
	 * @return ocurrencesNumber - integer with the number of occurrences of malicious servers.
	 */
	public int getOcurrencesCount() {
		return ocurrencesCount;
	}

	public void setOcurrencesCount(int ocurrencesNumber) {
		this.ocurrencesCount = ocurrencesNumber;
	}
	
	/**
	 * Returns the list of blacklists where the ip address was found.
	 * @return blackListOcurrences - list of blacklists where the ip address was found.
	 */
	public LinkedList<Integer> getBlackListOcurrences() {
		return blackListOcurrences;
	}

	public void setBlackListOcurrences(LinkedList<Integer> blackListOcurrences) {
		this.blackListOcurrences = blackListOcurrences;
	}

	public int getCheckedListsCount() {
		return checkedListsCount;
	}
}
